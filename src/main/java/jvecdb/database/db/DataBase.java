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

import jvecdb.database.db.io.DataBaseExport;
import jvecdb.database.db.io.DataBaseImport;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

public class DataBase {
    DataBaseImport dbImport = new DataBaseImport();
    DataBaseExport dbExport = new DataBaseExport();
    int scaleFactor = 5;

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

    // public <T> T getOrigin(){

    //}
}
