package jvecdb.database.db.io;

import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.utils.datastructures.datavectors.JVecSTR;
import jvecdb.utils.datastructures.std_vector;

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


    public std_vector<JVecSTR> importMixedFormat(String fileName, int VECTOR_LENGTH) {
        long time;
        if (JVecDB.DEBUG) {
            time = System.nanoTime();
        }
        std_vector<JVecSTR> data = new std_vector<>();
        byte[] wordBytes;
        float[] vector;
        int wordLength;
        String importString = DataBase.EXPORT_FOLDER + File.separator + fileName;
        if (fileName.contains(DataBase.EXPORT_FOLDER)) {
            importString = fileName;
        }
        try (InputStream is = new FileInputStream(importString)) {
            DataInputStream dis = new DataInputStream(is);
            String line;
            int counter = 0;
            while ((line = dis.readLine()) != null) {
                //System.out.println(line);
                counter++;
                if (counter == DataBase.META_DATA_LENGTH) {
                    break;
                }
            }
            while (dis.available() != 0) {
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
}
