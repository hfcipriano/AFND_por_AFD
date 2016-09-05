package cipriano.util.Enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by henrique on 22/08/16.
 */
public enum EstadoEnum {
    INICIAL     ("inicial"),
    FINAL       ("final"),
    AMBOS       ("ambos", INICIAL, FINAL),
    INDEFINIDO  ("indefinido");

    private String id;
    private List<EstadoEnum> estadoEnumList = new ArrayList<>();
    EstadoEnum(String s, EstadoEnum ...estadoEnum) {
        this.id = s;
        this.estadoEnumList = Arrays.asList(estadoEnum);
    }

    public String getId() {
        return id;
    }

    public List<EstadoEnum> getEstadoEnumList() {
        return estadoEnumList;
    }

    public boolean equals(EstadoEnum outro){
        if(this.estadoEnumList.size() > 0 && this.estadoEnumList.stream().anyMatch(estadoEnum -> estadoEnum.compareTo(outro) == 0)){
            return true;
        }
        if(outro.getEstadoEnumList().size() > 0 && outro.getEstadoEnumList().stream().anyMatch(estadoEnum -> estadoEnum.compareTo(this) == 0)){
            return true;
        }
        return this.equals((Object) outro);
    }
}
