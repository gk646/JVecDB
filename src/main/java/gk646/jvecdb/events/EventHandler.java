/*
 * MIT License
 *
 * Copyright (c) 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gk646.jvecdb.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public abstract class EventHandler implements JEventPublisher {
    protected static final Map<EventType, ArrayList<JEventListener<? extends JVecEvent>>> eventListenerMap = new EnumMap<>(EventType.class);

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

    protected static void setOnImportData(JEventListener<? extends JVecIOEvent> listener) {
        addEntryToList(EventType.IMPORT_DATA, listener);
    }
}
