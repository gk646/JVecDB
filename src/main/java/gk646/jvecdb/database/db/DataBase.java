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

package gk646.jvecdb.database.db;

import gk646.jvecdb.JVecDB;
import gk646.jvecdb.database.VectorDB;
import gk646.jvecdb.database.db.io.DataBaseExport;
import gk646.jvecdb.database.db.io.DataBaseImport;
import gk646.jvecdb.utils.datastructures.Vector;
import gk646.jvecdb.utils.datastructures.datavectors.JVec;
import gk646.jvecdb.utils.datastructures.datavectors.JVecSTR;
import gk646.jvecdb.utils.enums.ExportType;
import gk646.jvecdb.utils.errorhandling.Alerts;
import gk646.jvecdb.utils.errorhandling.exceptions.StartupFailure;

public class DataBase {
    public static final int META_DATA_LENGTH = 16;
    public static final String EXPORT_FOLDER = "JVecDB_Exports";
    VectorDB<? extends JVec> vectorDB;
    DataBaseImport dbImport = new DataBaseImport();
    DataBaseExport dbExport = new DataBaseExport();

    public DataBase(VectorDB<? extends JVec> vectorDB) {
        this.vectorDB = vectorDB;
    }


    public void makeExportFolder() {
        if (!dbExport.testForExportFolders()) {
            throw new StartupFailure("Couldn't create export folders!");
        }
    }
    @SuppressWarnings("unchecked")
    public <T extends JVec> void exportDataBase(Vector<T> dataBase, String fileName, ExportType exportType) {
        String saveMessage = "ERROR: Couldn't export database to file";
        switch (JVecDB.getActiveDataType()) {
            case STRING -> saveMessage = dbExport.exportMixedFormat((Vector<JVecSTR>) dataBase, fileName, exportType);
            case IMAGE -> {
                //EMPTY
            }
            case SOUND -> {
                //EMPTY
            }
        }
        triggerAlerts(saveMessage);
    }

    @SuppressWarnings("unchecked")
    public <T extends JVec> Vector<T> importDataBase(String fileName) {
        if (fileName.contains(".jvecdb")) {
            isValidImportPath(fileName);
            return (Vector<T>) dbImport.importMixedFormat(fileName);
        }
        return new Vector<>();
    }

    private void isValidImportPath(String fileName) {
        if (!dbImport.testForImportFile(fileName)) Alerts.displayErrorMessage("ERROR: Couldn't import database to file");
    }

    private void triggerAlerts(String saveMessage) {
        if (saveMessage.contains("ERROR")) {
            Alerts.displayErrorMessage(saveMessage);
        } else {
            Alerts.displayInformationDataBaseExport(saveMessage.split("\\|"));
        }
    }
}
