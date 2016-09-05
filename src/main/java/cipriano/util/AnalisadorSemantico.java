package cipriano.util;

import cipriano.util.Excecoes.SemanticException;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Henrique on 22/08/2016.
 */
public class AnalisadorSemantico {
    /**
    *Analisa a entrada do usuário em busca de falhas na sintaxe
    *@param paragrafos Lista que contem a entrada do usuário
    *Caso econtre alguma anormalidade, é lançada a exceção, que é tratada e exibida para o usuário
    **/
    public static void analisa(ObservableList<CharSequence> paragrafos){
        Iterator<CharSequence> iterador = paragrafos.iterator();

        validaEstadoInicial(iterador.next().toString());
        validaEstadosFinais(iterador.next().toString());

        //Itera sobre as linhas do arquivo
        for(int i = 2; iterador.hasNext(); i++){
            String linha = iterador.next().toString().trim();
            if(StringUtils.isBlank(linha)){
                throw new SemanticException("Transição vazia", i, 0);
            }
            if(linha.split(" ").length != 3){
                throw new SemanticException("Transição precisa conter apenas 3 palavras separadas por um espaço", i, 0);
            }
        }
    }

    private static void validaEstadoInicial(String primeiraLinha) {
        if(StringUtils.isBlank(primeiraLinha)){
            throw new SemanticException("Estado inicial inválido", 0, 0);
        }
        if(primeiraLinha.trim().contains(" ")){
            throw new SemanticException("Só é permitido um estado inicial", 0, primeiraLinha.trim().split(" ").length);
        }
    }

    private static void validaEstadosFinais(String segundaLinha) {
        if(StringUtils.isBlank(segundaLinha)){
            throw new SemanticException("Estados finais inválidos", 1, 0);
        }
        for (String estadoFinal : segundaLinha.split(" ")) {
            if(Arrays.asList(segundaLinha.split(" ")).stream().filter(s -> estadoFinal.equals(s)).count() > 1){
                throw new SemanticException("Existe um estado final duplicado", 1, 0);
            }
        }
    }
}
