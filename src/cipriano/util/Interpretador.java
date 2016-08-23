package cipriano.util;

import cipriano.model.Estado;
import cipriano.model.Transicao;
import cipriano.util.Enums.EstadoEnum;
import cipriano.util.Excecoes.BreakException;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Henrique on 22/08/2016.
 */
public class Interpretador {
    //Contém todas as transições do autômato
    private static Set<Transicao> transicaoList = new HashSet<>();

    //Contém apenas as transições que possuem mais de um estado destino
    private static Set<Transicao> transicaoElegivelList = new HashSet<>();

    private static String estadoInicial;
    private static List<String> estadoFinalList = new ArrayList<>();

    /**
     * Converte o AFND em AFD
     * @param paragrafos
     */
    public static void converter(ObservableList<CharSequence> paragrafos) {
        popularTransicoes(paragrafos);
    }

    /**
     * Transforma a massa de dados em uma lista de objetos legíveis no contexto da aplicação
     * @param paragrafos Lista com os dados
     */
    private static void popularTransicoes(ObservableList<CharSequence> paragrafos){
        transicaoList.clear();
        transicaoElegivelList.clear();

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

        transicaoElegivelList.addAll(
                transicaoList.stream().filter(transicao ->
                        transicao.getProximoList().size() > 1
                        || transicao.getAtual().getEstado().equals(EstadoEnum.INICIAL)
                        || transicao.getAtual().getEstado().equals(EstadoEnum.AMBOS)).collect(Collectors.toList())
        );
    }

    private static void persistirTransicao(String nomeAtual, String caractereLido, String nomeProximo) {
        Transicao transicao = new Transicao();
        try {
            transicaoList.forEach(t -> {
                if (t.getAtual().getNome().equals(nomeAtual) && t.getCaractere().equals(caractereLido)) {
                    transicao.setAtual(t.getAtual());
                    transicao.setCaractere(t.getCaractere());
                    transicao.setProximoList(t.getProximoList());
                    transicao.getProximoList().add(defineEstado(nomeProximo));
                    throw new BreakException();
                }
            });
            transicao.setAtual(defineEstado(nomeAtual));
            transicao.setCaractere(caractereLido);
            transicao.getProximoList().add(defineEstado(nomeProximo));
            transicaoList.add(transicao);
        }catch (BreakException e){
        }
    }

    private static Estado defineEstado(String nome) {
        Estado estado = new Estado();
        estado.setNome(nome);

        boolean estadoFinal = estadoFinalList.contains(nome);
        if(nome.equals(estadoInicial) && estadoFinal){
            estado.setEstado(EstadoEnum.AMBOS);
        }
        else if(nome.equals(estadoInicial)){
            estado.setEstado(EstadoEnum.INICIAL);
        }
        else if(estadoFinal){
            estado.setEstado(EstadoEnum.FINAL);
        }
        else {
            estado.setEstado(EstadoEnum.INDEFINIDO);
        }
        return estado;
    }
}
