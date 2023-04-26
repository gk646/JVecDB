package jvecdb.events;

public class JVecEvent {

    public enum Type {
        IMPORT_DATA, IMPORT_DB, EXPORT_DB
    }

    private final Type type;


    public JVecEvent(Type type) {
        this.type = type;
    }


    public Type getType() {
        return type;
    }
}
