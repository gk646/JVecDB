/*
 * Copyright © 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jvecdb.database;

import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.database.vectorize.Vectorizer;
import jvecdb.utils.datastructures.datavectors.JVec;
import jvecdb.utils.datastructures.datavectors.JVecSTR;
import jvecdb.utils.datastructures.std_vector;
import jvecdb.utils.enums.ExportType;
import jvecdb.utils.errorhandling.Alerts;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class VectorDB<T extends JVec> {
    public static boolean IMPORTED_FLAG = false;
    DataBase dataBase;
    Vectorizer vectorizer;
    std_vector<T> jVecDataBase = new std_vector<>();


    public boolean init() throws IOException {
        dataBase = new DataBase(this);
        vectorizer = new Vectorizer();
        dataBase.makeExportFolder();
        return true;
    }

    public JVec addStringToDB(String inputString) {
        JVecSTR vec = vectorizer.StringSimple(inputString);
        jVecDataBase.add((T) vec);
        return vec;
    }


    public void exportDataBase(String fileName, ExportType exportType) {
        new Thread(() -> dataBase.exportDataBase(jVecDataBase, fileName, exportType)).start();
    }

    public void importDataBase(String fileName) {
        jVecDataBase = (std_vector<T>) dataBase.importDataBase(fileName);
    }


    public void importVectorDataFromFile(String fileName) {
        jVecDataBase.clear();
        try {
            InputStream is = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, JVecDB.CHARSET));
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    jVecDataBase.add((T) vectorizer.StringSimple(word));
                }
            }
            JVecDB.vectorSpaceFX.addVisualEntryList(jVecDataBase);
        } catch (IOException e) {
            Alerts.displayErrorMessage("Couldn't import file");
        }
    }


    public std_vector<? extends JVec> getVectorDataBase() {
        return jVecDataBase;
    }
}
