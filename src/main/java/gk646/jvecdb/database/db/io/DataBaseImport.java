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

package gk646.jvecdb.database.db.io;

import gk646.jvecdb.JVecDB;
import gk646.jvecdb.database.db.DataBase;
import gk646.jvecdb.utils.datastructures.Vector;
import gk646.jvecdb.utils.datastructures.datavectors.JVecSTR;
import gk646.jvecdb.utils.enums.DataType;
import gk646.jvecdb.utils.enums.VectorShape;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public final class DataBaseImport {

    public DataBaseImport() {
    }

    public boolean testForImportFile(String absoluteFilename) {
        if (absoluteFilename.contains("\\")) {
            if (JVecDB.DEBUG) {
                System.out.println(absoluteFilename);
            }
            return true;
        }
        File directory = new File(DataBase.EXPORT_FOLDER + File.separator + absoluteFilename);
        return directory.exists();
    }


    public List<JVecSTR> importFromXML(String filePath) throws IOException {
        List<JVecSTR> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            JVecSTR currentVec = null;
            List<Float> currentVectorValues = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("<word>")) {
                    String word = line.substring("<word>".length(), line.indexOf("</word>"));
                    currentVec = new JVecSTR(word, new float[0]);
                } else if (line.startsWith("<value>")) {
                    float value = Float.parseFloat(line.substring("<value>".length(), line.indexOf("</value>")));
                    currentVectorValues.add(value);
                } else if (line.startsWith("</vector>")) {
                    float[] values = new float[currentVectorValues.size()];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = currentVectorValues.get(i);
                    }
                    //currentVec.getVector() = values;
                    currentVectorValues.clear();
                } else if (line.startsWith("</entry>")) {
                    data.add(currentVec);
                    currentVec = null;
                }
            }
        }
        return data;
    }


    public Vector<JVecSTR> importMixedFormat(String fileName) {
        long time;
        if (JVecDB.DEBUG) {
            time = System.nanoTime();
        }
        Vector<JVecSTR> data = new Vector<>();
        String importString = DataBase.EXPORT_FOLDER + File.separator + fileName;
        if (fileName.contains(DataBase.EXPORT_FOLDER)) {
            importString = fileName;
        }
        try (InputStream is = new FileInputStream(importString)) {
            DataInputStream dis = new DataInputStream(is);
            String line;
            int counter = 0;
            while ((line = dis.readLine()) != null) {
                readMetaData(line, counter);
                counter++;
                if (counter == DataBase.META_DATA_LENGTH) {
                    break;
                }
            }
            while (dis.available() >1) {
                data.add(new JVecSTR(dis.readNBytes(dis.readUnsignedByte()), new float[]{dis.readFloat(), dis.readFloat()}));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (JVecDB.DEBUG) {
            System.out.println("I/O of " + data.size() + " entries took: " + (System.nanoTime() - time) / 1000 + " microseconds");
        }
        return data;
    }


    private void readMetaData(String line, int lineNumber) {
        if (lineNumber > 3) {
            if (line.contains("\"datatype\"")){
                String[] parts = line.split(": ");
                JVecDB.setActiveDataType(DataType.valueOf(parts[1].substring(1, parts[1].length() - 2)));
            }else if(line.contains("\"shape\"")){
                String[] parts = line.split(": ");
                JVecDB.setActiveShape(VectorShape.valueOf(parts[1].substring(1, parts[1].length() - 2)));
            }
        }
    }
}
