package cipriano.util.Enums;

/**
 * Created by henrique on 22/08/16.
 */
public enum EstadoEnum {
    INICIAL ("inicial"),
    FINAL   ("final");

    private String id;
    EstadoEnum(String s) {
        this.id = s;
    }

    public String getId() {
        return id;
    }
}
