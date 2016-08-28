package cipriano.model;

import cipriano.util.Enums.EstadoEnum;

/**
 * Created by henrique on 22/08/16.
 */
public class Estado {
    private String nome;
    private EstadoEnum estado;

    public Estado() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estado estado1 = (Estado) o;

        if (!nome.equals(estado1.nome)) return false;
        return estado == estado1.estado;

    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return nome;
    }
}
