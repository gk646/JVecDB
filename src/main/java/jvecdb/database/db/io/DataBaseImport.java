package jvecdb.database.db.io;

import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.utils.datastructures.vectors.JVec_STR;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class DataBaseImport {

    public DataBaseImport() {

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


    public ArrayList<JVec_STR> importMixedFormat(String fileName) {
        ArrayList<JVec_STR> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(DataBase.EXPORT_FOLDER  + File.separator + fileName + ".mixed");
             DataInputStream dis = new DataInputStream(fis)) {

            while (dis.available() > 0) {
                int wordLength = dis.readInt();
                byte[] wordBytes = new byte[wordLength];
                dis.readFully(wordBytes);

                String word = new String(wordBytes, JVecDB.CHARSETS);

                ArrayList<Float> vectorList = new ArrayList<>();
                try {
                    while (true) {
                        float value = dis.readFloat();
                        vectorList.add(value);
                    }
                } catch (EOFException e) {
                    // End of vector data
                }

                float[] vector = new float[vectorList.size()];
                for (int i = 0; i < vectorList.size(); i++) {
                    vector[i] = vectorList.get(i);
                }

                JVec_STR vec_str = new JVec_STR(word, vector);
                data.add(vec_str);

                // Read and discard end bytes
                for (int i = 0; i < 4; i++) {
                    dis.readByte();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
