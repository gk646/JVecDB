package jvecdb.events;

import jvecdb.utils.enums.ExportType;

public final class JVecIOEvent extends JVecEvent {

    private final String eventString;
    private ExportType exportType;

    public JVecIOEvent(EventType type, String fileName) {
        super(type);
        this.eventString = fileName;
    }

    public JVecIOEvent(EventType type, String fileName, ExportType exportType) {
        this(type, fileName);
        this.exportType = exportType;
    }

    @Override
    public EventType getType() {
        return type;
    }

    public String getEventString() {
        return eventString;
    }

    public ExportType getExportType() {
        return exportType;
    }
}
