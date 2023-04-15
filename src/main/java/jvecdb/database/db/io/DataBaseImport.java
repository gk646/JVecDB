package jvecdb.database.db.io;

import jvecdb.utils.datastructures.vectors.JVec_STR;

import java.io.BufferedReader;
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
}
