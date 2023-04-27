package jvecdb.utils.errorhandling;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;

public abstract class Alerts {
    static Alert error;
    static Alert information;

    private Alerts() {
    }

    public static void displayErrorMessage(String message) {
        error = new Alert(AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(null);
        error.setContentText(message);
        error.show();
    }

    public static void displayInfo(String message) {
        information = new Alert(AlertType.INFORMATION);
        information.setTitle("Info");
        information.setContentText(message);
        information.showAndWait();
    }

    public static void displayInformationDataBaseExport(String[] messages) {
        information = new Alert(AlertType.INFORMATION);
        information.setTitle("Success!");
        information.setHeaderText("Successfully saved " + messages[1] + " entries with datatype " + JVecDB.getActiveDataType().toString());
        information.setContentText("Successfully saved " + messages[0] + " in " + DataBase.EXPORT_FOLDER);
        information.showAndWait();
    }
}
