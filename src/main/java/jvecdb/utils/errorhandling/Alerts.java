package jvecdb.utils.errorhandling;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

public class Alerts {



    public static void displayErrorMessage(String message) {
        // Create an Alert with the error message
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Show the alert and start a timer to close it after the specified duration
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3500), event -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
