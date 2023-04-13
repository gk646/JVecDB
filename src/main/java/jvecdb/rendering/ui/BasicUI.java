package jvecdb.rendering.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import jvecdb.JVecDB;

import java.util.ArrayList;
import java.util.Optional;

public class BasicUI extends MenuBar {


    public BasicUI() {
        ArrayList<Menu> menus = new ArrayList<>();

        menus.add(createHelpMenu());
        menus.add(createSettingsMenu());
        menus.add(createFileMenu());
        getMenus().addAll(menus);
    }


    private Menu createHelpMenu() {
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> System.out.println("About selected"));
        helpMenu.getItems().add(aboutItem);


        return helpMenu;
    }

    private Menu createSettingsMenu() {

        Menu settingsMenu = new Menu("Settings");
        MenuItem preferencesItem = new MenuItem("Preferences");
        preferencesItem.setOnAction(e -> System.out.println("Preferences selected"));
        settingsMenu.getItems().add(preferencesItem);

        return settingsMenu;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");
        MenuItem aboutItem = new MenuItem("add Entry");
        aboutItem.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Enter text");
            inputDialog.setHeaderText("Please enter your text");
            inputDialog.setContentText("Text:");

            // Show the dialog and get the user input (if 'Submit' is clicked)
            Optional<String> result = inputDialog.showAndWait();

            // If the user clicked 'Submit', call the handleInputText method with the entered text
            result.ifPresent(JVecDB::addDBEntry);
        });


        menu.getItems().add(aboutItem);

        return menu;
    }


}
