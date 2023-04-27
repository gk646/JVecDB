package jvecdb.events;

public interface JEventListener<T extends JVecEvent> {

    void handleIOEvent(T event);
}
