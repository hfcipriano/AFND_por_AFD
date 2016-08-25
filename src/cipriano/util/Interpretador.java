package cipriano.util;

import cipriano.model.Estado;
import cipriano.model.Transicao;
import cipriano.util.Enums.EstadoEnum;
import cipriano.util.Excecoes.InterpreterException;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Henrique on 22/08/2016.
 */
public class Interpretador {
    //Contém todas as transições do autômato
    private static Set<Transicao> transicaoList = new HashSet<>();
    private static String estadoInicial;
    private static List<String> estadoFinalList = new ArrayList<>();

    /**
     * Converte o AFND em AFD
     * @param paragrafos
     */
    public static String converter(ObservableList<CharSequence> paragrafos) {
        Set<Transicao> transicaoElegivelSet = serializarTransicoes(paragrafos);
        return deserializarTransicoes(gerarGrafo(transicaoElegivelSet));
    }

    private static String deserializarTransicoes(Set<Transicao> transicaoSet) {
        StringBuilder grafo = new StringBuilder();
        grafo.append(transicaoSet.parallelStream()
                .filter(t -> t.getAtual().getEstado().equals(EstadoEnum.INICIAL) || t.getAtual().getEstado().equals(EstadoEnum.AMBOS))
                .findAny()
                .orElseGet(() -> {
                    throw new InterpreterException("Estado inicial não encontrado");
                })
                .getAtual().getNome()).append("\n");

        transicaoSet.parallelStream().filter(t -> t.getAtual().getEstado().equals(EstadoEnum.FINAL) || t.getAtual().getEstado().equals(EstadoEnum.AMBOS))
                .forEach(t -> grafo.append(t.getAtual().getNome()).append(" "));
        grafo.append("\n");
        transicaoSet.forEach(transicao -> grafo.append(transicao).append("\n"));
        return grafo.toString();
    }

    /**
     * Gera um grafo determinístico apartir da lista de transições elegíveis
     */
    private static Set<Transicao> gerarGrafo(Set<Transicao> transicaoSet) {
        Set<Transicao> transicaoElegivelSet = new HashSet<>(transicaoSet);
        transicaoSet.forEach(transicao -> {
            persistirTransicaoElegivel(transicao, transicaoElegivelSet);
        });
        return transicaoElegivelSet;
    }

    private static void persistirTransicaoElegivel(Transicao transicao, Set<Transicao> transicaoSet) {
        boolean contem = false;
        for(Transicao t : transicaoSet){
            if(t.getAtual().getNome().equals(transicao.getProximoEstadoNome())){
                contem = true;
                break;
            }
        }
        if(!contem) {
            List<String> nomes = new ArrayList<>();
            transicao.getProximoList().forEach(estado -> nomes.add(estado.getNome()));

            Transicao transicaoElegivel = new Transicao();
            transicaoElegivel.setAtual(defineEstado(nomes.toArray(new String[nomes.size()])));
            transicaoElegivel.setCaractere(transicao.getCaractere());
            nomes.forEach(nome -> transicaoElegivel.getProximoList().addAll(obterTransicaoElegivelPorNome(nome, transicaoSet).getProximoList()));
            transicaoSet.add(transicaoElegivel);
            persistirTransicaoElegivel(transicaoElegivel, transicaoSet);
        }
    }

    private static Transicao obterTransicaoElegivelPorNome(String nome, Set<Transicao> transicaoSet) {
        for(Transicao transicaoElegivel : transicaoSet){
            if(transicaoElegivel.getAtual().getNome().equals(nome)){
                return transicaoElegivel;
            }
        }
        for(Transicao transicao : transicaoList){
            if(transicao.getAtual().getNome().equals(nome)){
                return transicao;
            }
        }

        for(Transicao transicao : transicaoSet){
            if(transicao.getAtual().getNome().equals(nome)){
                return transicao;
            }
        }

        return new Transicao();

        //return transicaoElegivelList.stream().filter(transicao -> transicao.getAtual().getNome().equals(nome)).findFirst().orElse(transicaoList.stream().filter(transicao -> transicao.getAtual().getNome().equals(nome)).findFirst().get());
    }

    /**
     * Transforma a massa de dados em uma lista de objetos legíveis no contexto da aplicação
     * @param paragrafos Lista com os dados
     */
    private static Set<Transicao> serializarTransicoes(ObservableList<CharSequence> paragrafos){
        transicaoList.clear();

        Iterator<CharSequence> iterador = paragrafos.iterator();
        estadoInicial = iterador.next().toString();
        estadoFinalList = Arrays.asList(iterador.next().toString().split(" "));

        //Itera sobre as linhas do arquivo
        iterador.forEachRemaining(paragrafo -> {
            String[] elementos = paragrafo.toString().split(" ");
            String nomeAtual = elementos[0];
            String caractereLido = elementos[1];
            String nomeProximo = elementos[2];
            persistirTransicao(nomeAtual, caractereLido, nomeProximo);
        });

        return new HashSet<>(transicaoList.stream().filter(transicao ->
                        transicao.getProximoList().size() > 1
                                || transicao.getAtual().getEstado().equals(EstadoEnum.INICIAL)
                                || transicao.getAtual().getEstado().equals(EstadoEnum.AMBOS)).collect(Collectors.toList()));
    }

    private static void persistirTransicao(String nomeAtual, String caractereLido, String nomeProximo) {
        Transicao transicao = new Transicao();
        transicaoList.stream().filter(t -> t.getAtual().getNome().equals(nomeAtual) && t.getCaractere().equals(caractereLido)).findFirst().orElseGet(() -> {
            transicao.setAtual(defineEstado(nomeAtual));
            transicao.setCaractere(caractereLido);
            transicaoList.add(transicao);
            return transicao;
        }).getProximoList().add(defineEstado(nomeProximo));
    }

    private static Estado defineEstado(String ...nomes) {
        boolean estadoFinalBool = false;
        boolean estadoInicialBool = false;
        Estado estado = new Estado();

        for(String nome : nomes){
            if(estado.getNome() != null) {
                estado.setNome(estado.getNome() + nome);
            }
            else {
                estado.setNome(nome);
            }
            estadoFinalBool = estadoFinalBool || estadoFinalList.contains(nome);
            estadoInicialBool = estadoInicialBool || estadoInicial.equals(nome);
        }

        if(estadoInicialBool && estadoFinalBool){
            estado.setEstado(EstadoEnum.AMBOS);
        }
        else if(estadoInicialBool){
            estado.setEstado(EstadoEnum.INICIAL);
        }
        else if(estadoFinalBool){
            estado.setEstado(EstadoEnum.FINAL);
        }
        else {
            estado.setEstado(EstadoEnum.INDEFINIDO);
        }
        return estado;
    }
}
