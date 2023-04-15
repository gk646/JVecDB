package jvecdb.rendering.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import jvecdb.JVecDB;
import jvecdb.utils.enums.ExportType;

import java.util.ArrayList;
import java.util.Optional;

public class MenuBarJvec extends MenuBar {


    public MenuBarJvec() {
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
            inputDialog.setTitle("Add a new entry to the database");
            inputDialog.setHeaderText("Enter a word (a-z, A-Z, whitespace ignored)");
            inputDialog.setContentText("Text:");


            Optional<String> result = inputDialog.showAndWait();


            result.ifPresent(JVecDB::addDBEntry);
        });

        MenuItem exportDataBase = new MenuItem("Export dataBase as binary");

        exportDataBase.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Export the current database state to file");
            inputDialog.setHeaderText("Please enter a file name");
            inputDialog.setContentText("Filename:");

            Optional<String> result = inputDialog.showAndWait();


            result.ifPresent(filename -> JVecDB.exportDataBase(filename, ExportType.BINARY));
        });



        MenuItem importDataBase = new MenuItem("Import dataBase from binary");

        importDataBase.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Import a saved binary database");
            inputDialog.setHeaderText("Please enter a file name (without suffix)");
            inputDialog.setContentText("Filename:");

            Optional<String> result = inputDialog.showAndWait();


            result.ifPresent(filename ->JVecDB.importDataBase(filename+".jvecdb"));
        });

        MenuItem importWordsFromFile = new MenuItem("Import words from file");

        importWordsFromFile.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Import a saved binary database");
            inputDialog.setHeaderText("Please enter a file name (without suffix)");
            inputDialog.setContentText("Filename:");

            Optional<String> result = inputDialog.showAndWait();

            result.ifPresent(JVecDB::importWordsFromFile);
        });

        menu.getItems().addAll(aboutItem, exportDataBase,importDataBase,importWordsFromFile);

        return menu;
    }
}
