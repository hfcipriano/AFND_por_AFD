package cipriano.util.Excecoes;

/**
 * Created by Henrique on 04/04/2016.
 */
public class SemanticException extends RuntimeException {

    public SemanticException(String msg, int linha, int coluna) {
        super(msg + ". (Linha: " + linha + "; Coluna: " + coluna + ")");
    }
}
