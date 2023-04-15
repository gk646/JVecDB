package jvecdb.database.db.io;

import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.utils.datastructures.vectors.JVec_STR;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class DataBaseImport {

    public DataBaseImport() {
    }

    public boolean testForImportFile(String absoluteFilename) {
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
        long time = System.nanoTime();
        ArrayList<JVec_STR> data = new ArrayList<>();
        try (InputStream is = new FileInputStream(DataBase.EXPORT_FOLDER + File.separator + fileName)) {
            DataInputStream dis = new DataInputStream(is);
            while (dis.available() > 0) {
                int wordLength = dis.readInt();
                if (Math.abs(wordLength) > 100) {
                    break;
                }
                byte[] wordBytes = new byte[wordLength];
                dis.readFully(wordBytes);

                String word = new String(wordBytes, JVecDB.CHARSET);
                float[] vector = new float[VECTOR_LENGTH];
                for (int i = 0; i < VECTOR_LENGTH; i++) {
                    vector[i] = dis.readFloat();
                }
                JVec_STR vec = new JVec_STR(word, vector);
                data.add(vec);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is, JVecDB.CHARSET));
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println((System.nanoTime() - time));
        for (JVec_STR vec_str : data) {
            System.out.println(vec_str);
        }
        return data;
    }
}
