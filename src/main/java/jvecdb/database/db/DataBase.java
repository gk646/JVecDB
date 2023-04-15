/*
 * Copyright © 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jvecdb.database.db;

import jvecdb.JVecDB;
import jvecdb.database.db.io.DataBaseExport;
import jvecdb.database.db.io.DataBaseImport;
import jvecdb.utils.datastructures.vectors.JVec;
import jvecdb.utils.datastructures.vectors.JVec_STR;
import jvecdb.utils.enums.ExportType;
import jvecdb.utils.errorhandling.Alerts;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

import java.util.ArrayList;

public class DataBase {
    DataBaseImport dbImport = new DataBaseImport();
    DataBaseExport dbExport = new DataBaseExport();
    int scaleFactor = 5;
    public static String EXPORT_FOLDER = "JVecDB_Exports";
    public DataBase() {
        getDBFile();
    }


    private void getDBFile() {

    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void makeExportFolder() {
        if (!dbExport.testForExportFolders()) {
            throw new StartupFailure("Couldn't create export folders!");
        }
    }

    public <T extends JVec> void exportDataBase(ArrayList<T> dataBase, String fileName, ExportType exportType) {
        String saveMessage = "Couldn't export database to file";
        switch (JVecDB.ACTIVE_DATA_TYPE) {
            case STRING -> saveMessage = dbExport.exportMixedFormat((ArrayList<JVec_STR>) dataBase, fileName,exportType);
            case IMAGE -> {
            }
            case SOUND -> {
            }
        }
        if (saveMessage.contains("ERROR")) {
            Alerts.displayErrorMessage(saveMessage);
        } else {
            Alerts.displayInformationDataBaseExport(saveMessage.split("\\|"));
        }
    }
}
