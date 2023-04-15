package jvecdb.database.db.io;

import javafx.geometry.Point3D;
import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.utils.datastructures.vectors.JVec_STR;
import jvecdb.utils.enums.ExportType;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public final class DataBaseExport {
    int exportCount = 0;

    boolean isExportFolder;

    HashMap<Integer, String[]> metaData;

    public DataBaseExport() {
        metaData = new HashMap<>();
    }


    public String exportToXML(ArrayList<JVec_STR> data, String fileName) {
        if (!testForExportFolders()) {
            return "ERROR: No export folder found!";
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DataBase.EXPORT_FOLDER + File.separator + fileName + ".xml"))) {
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
        } catch (IOException e) {
            return "ERROR: Couldn't generate export\n" + e.getMessage();
        }
        exportCount++;
        return fileName + "|" + data.size();
    }

    public boolean testForExportFolders() {
        if (isExportFolder) {
            return true;
        }
        File directory = new File(DataBase.EXPORT_FOLDER);
        if (directory.exists()) {
            isExportFolder = true;
            return true;
        } else {
            isExportFolder = directory.mkdir();
        }
        return isExportFolder;
    }

    public String exportMixedFormat(ArrayList<JVec_STR> data, String fileName, ExportType exportType) {
        try (FileOutputStream fos = new FileOutputStream(DataBase.EXPORT_FOLDER + File.separator + fileName + ".jvecdb")) {
            DataOutputStream dos = new DataOutputStream(fos);
            for (JVec_STR vec_str : data) {
                byte[] wordBytes = vec_str.getByteValue();
                dos.writeInt(wordBytes.length);
                dos.write(wordBytes);
                float[] vector = vec_str.getVector();
                for (float value : vector) {
                    dos.writeFloat(value);
                }
            }
            dos.write("\n".getBytes());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

            // Write JSON metadata
            writer.write("{\n");
            writer.write("  \"database-info\": [\n");

            writer.write("    {\n");

            setupMetaData(data.size(), fileName, exportType);
            for (int i = 0; i < metaData.size(); i++) {
                writer.write("      \"" + metaData.get(i)[0] + "\": \"" + metaData.get(i)[1] + "\",\n");
            }
            writer.write("    }\n");
            writer.write("         ]\n");
            writer.write("}\n");
            writer.flush();
        } catch (IOException e) {
            return "ERROR: Couldn't generate export\n" + e.getMessage();
        }
        return fileName + "|" + data.size();
    }


    private void setupMetaData(int entries, String fileName, ExportType exportType) {
        metaData.clear();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Point3D position = JVecDB.vectorSpaceFX.getPosition();
        int index = 0;
        metaData.put(index++, new String[]{"time", now.format(formatter)});
        metaData.put(index++, new String[]{"jvecdbversion", JVecDB.VERSION});
        metaData.put(index++, new String[]{"filename", fileName});
        metaData.put(index++, new String[]{"savemode", exportType.toString()});
        metaData.put(index++, new String[]{"entries", String.valueOf(entries)});
        metaData.put(index++, new String[]{"datatype", JVecDB.ACTIVE_DATA_TYPE.toString()});
        metaData.put(index++, new String[]{"shape", JVecDB.ACTIVE_SHAPE.toString()});
        metaData.put(index++, new String[]{"x", String.valueOf(position.getX())});
        metaData.put(index++, new String[]{"y", String.valueOf(position.getY())});
        metaData.put(index++, new String[]{"z", String.valueOf(position.getZ())});
    }
}
