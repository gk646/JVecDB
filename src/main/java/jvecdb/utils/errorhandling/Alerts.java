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

package jvecdb.utils.errorhandling;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;

public abstract class Alerts {
    static Alert error;
    static Alert information;

    private Alerts() {
    }

    public static void displayErrorMessage(String message) {
        error = new Alert(AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(null);
        error.setContentText(message);
        error.show();
    }

    public static void displayInfo(String message) {
        information = new Alert(AlertType.INFORMATION);
        information.setTitle("Info");
        information.setContentText(message);
        information.showAndWait();
    }

    public static void displayInformationDataBaseExport(String[] messages) {
        information = new Alert(AlertType.INFORMATION);
        information.setTitle("Success!");
        information.setHeaderText("Successfully saved " + messages[1] + " entries with datatype " + JVecDB.getActiveDataType().toString());
        information.setContentText("Successfully saved " + messages[0] + " in " + DataBase.EXPORT_FOLDER);
        information.showAndWait();
    }
}
