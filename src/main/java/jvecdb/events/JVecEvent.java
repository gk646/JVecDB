package jvecdb.events;

public class JVecEvent {


    protected final EventType type;


    public JVecEvent(EventType type) {
        this.type = type;
    }


    public EventType getType() {
        return type;
    }

}
