package cipriano.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrique on 22/08/16.
 */
public class Transicao {
    private Estado atual;
    private List<Estado> proximoList = new ArrayList<>();
    private String caractere;

    public Estado getAtual() {
        return atual;
    }

    public void setAtual(Estado atual) {
        this.atual = atual;
    }

    public List<Estado> getProximoList() {
        return proximoList;
    }

    public void setProximoList(List<Estado> proximoList) {
        this.proximoList = proximoList;
    }

    public String getCaractere() {
        return caractere;
    }

    public void setCaractere(String caractere) {
        this.caractere = caractere;
    }
}
