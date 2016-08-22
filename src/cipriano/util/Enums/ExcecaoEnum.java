package cipriano.util.Enums;

/**
 * Created by Henrique on 04/04/2016.
 */
public enum ExcecaoEnum {
    //TODO: Acrescentar enumeráveis
    OPERACAO ("Operação inválida");

    private String excecao;
    ExcecaoEnum(String excecao) {
        this.excecao = excecao;
    }

    public String getExcecao() {
        return excecao;
    }
}
