package cipriano.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by henrique on 22/08/16.
 */
public class Transicao {
    private Estado atual;
    private Set<Estado> proximoList = new HashSet<>();
    private String caractere;

    public Estado getAtual() {
        return atual;
    }

    public void setAtual(Estado atual) {
        this.atual = atual;
    }

    public Set<Estado> getProximoList() {
        return proximoList;
    }

    public void setProximoList(Set<Estado> proximoList) {
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
}
