package jvecdb.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public abstract class EventHandler implements JEventPublisher {

    public static final Map<EventType, ArrayList<JEventListener<? extends JVecEvent>>> eventListenerMap = new EnumMap<>(EventType.class);


    protected static void setOnImportDB(JEventListener<JVecIOEvent> listener) {
        addEntryToList(EventType.IMPORT_DB, listener);
    }

    protected static void setOnExportDB(JEventListener<JVecIOEvent> listener) {
        addEntryToList(EventType.EXPORT_DB, listener);
    }

    private static void addEntryToList(EventType type, JEventListener<? extends JVecEvent> listener) {
        if (eventListenerMap.get(type) == null) {
            eventListenerMap.put(type, new ArrayList<>(Collections.singletonList(listener)));
        } else {
            eventListenerMap.get(type).add(listener);
        }
    }
}
