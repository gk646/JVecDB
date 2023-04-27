package jvecdb.events;

public interface JEventPublisher {
    default <T extends JVecEvent> void fireEventDefault(T event) {
        if (EventHandler.eventListenerMap.get(event.getType()) != null) {
            for (JEventListener listener : EventHandler.eventListenerMap.get(event.getType())) {
                listener.handleIOEvent(event);
            }
        }
    }
}
