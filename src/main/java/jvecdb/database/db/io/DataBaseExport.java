package jvecdb.database.db.io;

import jvecdb.utils.datastructures.vectors.JVec_STR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class DataBaseExport {


    public DataBaseExport() {

    }

    int importCount = 0;

    boolean isExportFolder;

    public void exportToXML(List<JVec_STR> data, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<database>\n");

            for (JVec_STR vec_str : data) {
                writer.write("  <entry>\n");
                writer.write("    <word>" + vec_str.getStringValue() + "</word>\n");
                writer.write("    <vector>\n");
                float[] vector = vec_str.getVector();
                for (float value : vector) {
                    writer.write("      <value>" + value + "</value>\n");
                }

                writer.write("    </vector>\n");
                writer.write("  </entry>\n");
            }

            writer.write("</database>\n");
        }
        importCount++;
    }

    public boolean testForExportFolders() {
        if (isExportFolder) {
            return true;
        }
        File directory = new File("JVec Exports");
        if (!directory.exists()) {
            isExportFolder = directory.mkdir();
        }
        return isExportFolder;
    }
}
