package jvecdb.utils.errorhandling;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.database.db.io.DataBaseExport;

public class Alerts {

    static Alert error, information;

    public static void displayErrorMessage(String message) {
        error = new Alert(AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(null);
        error.setContentText(message);

        error.show();
    }

    public static void displayInformationDataBaseExport(String[] messages) {
        information = new Alert(AlertType.INFORMATION);
        information.setTitle("Success!");
        information.setHeaderText("Successfully saved " + messages[1] + " entries with datatype " + JVecDB.ACTIVE_DATA_TYPE.toString());
        information.setContentText("Successfully saved " + messages[0] + " in " + DataBase.EXPORT_FOLDER);
        information.showAndWait();
    }
/*
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), event -> information.close()));
        timeline.setCycleCount(1);
        timeline.play();

 */
}
