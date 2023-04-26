package jvecdb.events;

import java.util.HashMap;
import java.util.Map;

public class EventHandler {
    Map<EventType, EventListener> eventListenerMap = new HashMap<>();

    protected void setOnIOEvent(EventListener eventListener) {
        eventListenerMap.put(EventType.EXPORT_DB, eventListener);
        eventListenerMap.put(EventType.IMPORT_DATA, eventListener);
        eventListenerMap.put(EventType.IMPORT_DB, eventListener);
    }

    protected void setOnImportEven(EventListener eventListener) {

    }
}
