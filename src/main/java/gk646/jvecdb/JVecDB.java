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

package gk646.jvecdb;

import gk646.jvecdb.database.VectorDB;
import gk646.jvecdb.events.EventHandler;
import gk646.jvecdb.rendering.VectorSpaceFX;
import gk646.jvecdb.utils.datastructures.datavectors.JVec;
import gk646.jvecdb.utils.enums.DataType;
import gk646.jvecdb.utils.enums.VectorShape;
import gk646.jvecdb.utils.errorhandling.Alerts;
import gk646.jvecdb.utils.errorhandling.exceptions.StartupFailure;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class JVecDB extends EventHandler {

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String VERSION = "1.1";
    public static final boolean DEBUG = true;
    public static final int WIDTH = 1280, HEIGHT = 960, MAX_DISPLAYED_VECTORS = 25_000;
    public static final VectorSpaceFX vectorSpaceFX = new VectorSpaceFX();
    public static final VectorDB<? extends JVec> vectorDB = new VectorDB<>();
    private static DataType activeDataType = DataType.STRING;
    private static VectorShape activeShape = VectorShape.BOX;

    private JVecDB() {
    }

    public static void init(Stage stage) {
        try {
            setupListeners();
            if (!vectorSpaceFX.init(stage)) {
                throw new StartupFailure("Failed to startup!");
            }
            if (!vectorDB.init()) {
                throw new StartupFailure("Failed to startup!");
            }
        } catch (IOException e) {
            throw new StartupFailure(e.toString());
        }
    }

    private static void setupListeners() {
        setOnImportDB(event ->
                new Thread(() -> {
                    vectorDB.importDataBase(event.getEventString());
                    vectorSpaceFX.reloadVectorSpaceFX();
                }).start());

        setOnExportDB(event -> vectorDB.exportDataBase(event.getEventString(), event.getExportType()));

        setOnImportData(event -> new Thread(() -> {
            vectorDB.importVectorDataFromFile(event.getEventString());
            vectorSpaceFX.reloadVectorSpaceFX();
        }).start());
    }

    public static DataType getActiveDataType() {
        return activeDataType;
    }

    public static VectorShape getActiveShape() {
        return activeShape;
    }

    public static void setActiveDataType(DataType activeDataType) {
        JVecDB.activeDataType = activeDataType;
    }

    public static void setActiveShape(VectorShape activeShape) {
        JVecDB.activeShape = activeShape;
    }

    public static <T> void addDBEntry(T entry) {
        switch (activeDataType) {
            case STRING -> {
                if (!vectorSpaceFX.addVisualEntry(vectorDB.addStringToDB((String) entry))) {
                    Alerts.displayErrorMessage("Can't add entry to database!");
                }
            }
            case NULL -> Alerts.displayErrorMessage("No datatype for database selected!");
        }
    }
}
