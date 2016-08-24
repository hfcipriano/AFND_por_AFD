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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transicao transicao = (Transicao) o;

        if (!atual.equals(transicao.atual)) return false;
        if (!proximoList.equals(transicao.proximoList)) return false;
        return caractere.equals(transicao.caractere);

    }

    @Override
    public int hashCode() {
        int result = atual.hashCode();
        result = 31 * result + proximoList.hashCode();
        result = 31 * result + caractere.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return atual + " " + caractere + " " + getProximoEstadoNome();
    }

    public String getProximoEstadoNome(){
        StringBuilder sb = new StringBuilder();
        proximoList.forEach(estado -> sb.append(estado));

        return sb.toString();
    }
}
