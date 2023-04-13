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
import jvecdb.utils.DataType;
import jvecdb.utils.errorhandling.Alerts;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

import java.io.IOException;

public class JVecDB {
    public static int WIDTH = 1280, HEIGHT = 960;
    static VectorSpaceFX vectorSpace = new VectorSpaceFX();
    static VectorDB vectorDB = new VectorDB();
    public static DataType dataType = DataType.STRING;


    public JVecDB(Stage stage) {
        try {
            if (!vectorSpace.init(stage)) {
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
        switch (dataType) {
            case STRING -> {
                if (vectorSpace.addVisualEntry(vectorDB.addStringToDB((String) entry))) {

                } else {
                    Alerts.displayErrorMessage("Can't add entry to database!");
                }
            }
            case NULL -> Alerts.displayErrorMessage("No datatype for database selected!");
        }
    }
}

