/*
 * Copyright © 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jvecdb;

import javafx.stage.Stage;
import jvecdb.database.VectorDB;
import jvecdb.rendering.VectorSpaceFX;
import jvecdb.utils.enums.DataType;
import jvecdb.utils.enums.ExportType;
import jvecdb.utils.enums.VectorShape;
import jvecdb.utils.errorhandling.Alerts;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class JVecDB {
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String VERSION = "0.9.2";
    public static final boolean DEBUG = true;
    public static int WIDTH = 1280, HEIGHT = 960, MAX_DISPLAYED_VECTORS = 50_000;
    public static final VectorSpaceFX vectorSpaceFX = new VectorSpaceFX();
    public static final VectorDB vectorDB = new VectorDB();
    public static DataType ACTIVE_DATA_TYPE = DataType.STRING;
    public static VectorShape ACTIVE_SHAPE = VectorShape.BOX;

    public JVecDB(Stage stage) {
        try {
            if (!vectorSpaceFX.init(stage)) {
                throw new RuntimeException("Failed to startup!");
            }
            if (!vectorDB.init()) {
                throw new RuntimeException("Failed to startup!");
            }
        } catch (IOException e) {
            throw new StartupFailure(e.toString());
        }
    }


    public static <T> void addDBEntry(T entry) {
        switch (ACTIVE_DATA_TYPE) {
            case STRING -> {
                if (!vectorSpaceFX.addVisualEntry(vectorDB.addStringToDB((String) entry))) {
                    Alerts.displayErrorMessage("Can't add entry to database!");
                }
            }
            case NULL -> Alerts.displayErrorMessage("No datatype for database selected!");
        }
    }


    public static void exportDataBase(String fileName, ExportType exportType) {
        vectorDB.exportDataBase(fileName, exportType);
    }

    public static void importDataBase(String fileName) {
        vectorDB.importDataBase(fileName);
        vectorSpaceFX.reloadVectorSpaceFX();
    }


    public static void importWordsFromFile(String fileName) {
        vectorDB.importVectorDataFromFile(fileName);
        vectorSpaceFX.reloadVectorSpaceFX();
    }
}
