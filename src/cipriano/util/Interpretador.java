package cipriano.util;

import cipriano.model.Estado;
import cipriano.model.Transicao;
import cipriano.util.Enums.EstadoEnum;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Henrique on 22/08/2016.
 */
public class Interpretador {
    //Contém todas as transições do autômato
    private static Set<Transicao> transicaoSet = new HashSet<>();
    private static String estadoInicial;
    private static List<String> estadoFinalList = new ArrayList<>();

    /**
     * Converte o AFND em AFD
     * @param paragrafos
     */
    public static void converter(ObservableList<CharSequence> paragrafos) {
        Set<Transicao> transicaoElegivelSet = serializarTransicoes(paragrafos);
        GraphvizUtil.gerarGrafo((gerarTransicoesConvertidas(transicaoElegivelSet)));
    }

    /**
     * Gera um grafo determinístico apartir da lista de transições elegíveis
     */
    private static Set<Transicao> gerarTransicoesConvertidas(Set<Transicao> transicaoSet) {
        Set<Transicao> transicaoElegivelSet = new HashSet<>(transicaoSet);
        transicaoSet.forEach(transicao -> persistirTransicaoElegivel(transicao, transicaoElegivelSet));
        return transicaoElegivelSet;
    }

    private static void persistirTransicaoElegivel(Transicao transicao, Set<Transicao> transicaoSet) {
        //Verifica se já existe alguma transição equivalente a transição do proximo estado
        if(!transicaoSet.stream().anyMatch(t -> t.getAtual().getNome().equals(transicao.getProximoEstadoNome()) && t.getCaractere().equals(transicao.getCaractere()))) {
            Estado estado = defineEstado(transicao.getProximoEstadoNome());

            //obterCaracteresPorEstadoETransicao(estado, transicao);
            for(String caractere : obterCaracteresPorEstadoETransicao(estado, transicao)) {
                Transicao transicaoElegivel = new Transicao();
                transicaoElegivel.setAtual(estado);
                transicaoElegivel.setCaractere(caractere);
                transicaoElegivel.getProximoList().addAll(obterEstadosProximosPorEstadoAtualECaractere(estado, caractere));
                transicaoSet.add(transicaoElegivel);

                persistirTransicaoElegivel(transicaoElegivel, transicaoSet);
            }
        }
    }
    private static Set<String> obterCaracteresPorEstadoETransicao(Estado estado, Transicao transicao) {
        Set<String> caracteresLidos = new HashSet<>();
        transicaoSet.stream()
                .filter(t -> estado.getNome().contains(t.getAtual().getNome()))
                .forEach(t -> caracteresLidos.add(t.getCaractere()));
        return  caracteresLidos;
    }

    private static List<Estado> obterEstadosProximosPorEstadoAtualECaractere(Estado atual, String caractere){
        List<Estado> estadoList = new ArrayList<>();
        transicaoSet.stream()
                .filter(t -> atual.getNome().contains(t.getAtual().getNome()) && caractere.equals(t.getCaractere()))
                .forEach(t -> estadoList.addAll(t.getProximoList()));
        return estadoList;
    }

    /**
     * Transforma a massa de dados em uma lista de objetos legíveis no contexto da aplicação
     * @param paragrafos Lista com os dados
     */
    private static Set<Transicao> serializarTransicoes(ObservableList<CharSequence> paragrafos){
        transicaoSet.clear();

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

        return new HashSet<>(transicaoSet.stream().filter(transicao ->
                        transicao.getProximoList().size() > 1
                                || transicao.getAtual().getEstado().equals(EstadoEnum.INICIAL)
                                || transicao.getAtual().getEstado().equals(EstadoEnum.AMBOS)).collect(Collectors.toSet()));
    }

    private static void persistirTransicao(String nomeAtual, String caractereLido, String nomeProximo) {
        Transicao transicao = new Transicao();
        transicaoSet.stream().filter(t -> t.getAtual().getNome().equals(nomeAtual) && t.getCaractere().equals(caractereLido)).findFirst().orElseGet(() -> {
            transicao.setAtual(defineEstado(nomeAtual));
            transicao.setCaractere(caractereLido);
            transicaoSet.add(transicao);
            return transicao;
        }).getProximoList().add(defineEstado(nomeProximo));
    }

    private static Estado defineEstado(String nome) {
        boolean estadoFinalBool = false;
        boolean estadoInicialBool = false;
        Estado estado = new Estado();
        estado.setNome(nome);

        if(nome.equals(estadoInicial)){
            estadoInicialBool = true;
        }
        estadoFinalBool = estadoFinalList.stream().filter(nome::contains).findAny().isPresent();

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
