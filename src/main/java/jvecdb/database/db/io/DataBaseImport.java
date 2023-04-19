package jvecdb.database.db.io;

import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.utils.datastructures.datavectors.JVec_STR;

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
        if(absoluteFilename.contains("\\")){
            if(JVecDB.DEBUG){
                System.out.println(absoluteFilename);
            }
            return true;
        }
        File directory = new File(DataBase.EXPORT_FOLDER + File.separator + absoluteFilename);
        return directory.exists();
    }


    public List<JVec_STR> importFromXML(String filePath) throws IOException {
        List<JVec_STR> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            JVec_STR currentVec = null;
            List<Float> currentVectorValues = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("<word>")) {
                    String word = line.substring("<word>".length(), line.indexOf("</word>"));
                    currentVec = new JVec_STR(word, new float[0]);
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


    public ArrayList<JVec_STR> importMixedFormat(String fileName, int VECTOR_LENGTH) {
        long time;
        if (JVecDB.DEBUG) {
            time = System.nanoTime();
        }
        ArrayList<JVec_STR> data = new ArrayList<>();
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
            while (true) {
                wordLength = dis.readUnsignedByte();
                wordBytes = new byte[wordLength];
                dis.readFully(wordBytes);

                vector = new float[VECTOR_LENGTH];
                for (int i = 0; i < VECTOR_LENGTH; i++) {
                    vector[i] = dis.readFloat();
                }
                JVec_STR vec = new JVec_STR(wordBytes, vector);
                data.add(vec);
                if (dis.available() == 0) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (JVecDB.DEBUG) {
            System.out.println("Imported " + data.size() + " entries in: " + (System.nanoTime() - time) / 10000);
        }
        return data;
    }
}
