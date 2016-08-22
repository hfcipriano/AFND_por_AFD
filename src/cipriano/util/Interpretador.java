package cipriano.util;

import cipriano.model.Estado;
import cipriano.model.Transicao;
import cipriano.util.Enums.EstadoEnum;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Henrique on 22/08/2016.
 */
public class Interpretador {
    private static List<Transicao> transicaoList = new ArrayList<>();
    private static List<Transicao> transicaoTratadaList = new ArrayList<>();
    private static String estadoInicial;
    private static List<String> estadoFinalList = new ArrayList<>();

    public static void popularTransicoes(ObservableList<CharSequence> paragrafos){
        transicaoList.clear();

        Iterator<CharSequence> iterador = paragrafos.iterator();
        estadoInicial = iterador.next().toString();
        estadoFinalList = Arrays.asList(iterador.next().toString().split(" "));

        //Itera sobre as linhas do arquivo
        iterador.forEachRemaining(pagrafo -> {
            String[] elementos = pagrafo.toString().split(" ");
            String nomeAtual = elementos[0];
            String caractereLido = elementos[1];
            String nomeProximo = elementos[2];


            transicaoList.add(obterTransicao(nomeAtual, caractereLido, nomeProximo));

        });
        System.out.println();
    }

    private static Transicao obterTransicao(String nomeAtual, String caractereLido, String nomeProximo) {
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
        }catch (BreakException e){
            return transicao;
        }

        transicao.setAtual(defineEstado(nomeAtual));
        transicao.setCaractere(caractereLido);
        transicao.getProximoList().add(defineEstado(nomeProximo));
        return transicao;
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

class BreakException extends RuntimeException {
}
