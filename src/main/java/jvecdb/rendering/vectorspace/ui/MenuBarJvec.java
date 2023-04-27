package jvecdb.rendering.vectorspace.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import jvecdb.JVecDB;
import jvecdb.events.EventType;
import jvecdb.events.JEventPublisher;
import jvecdb.events.JVecIOEvent;
import jvecdb.utils.enums.ExportType;
import jvecdb.utils.errorhandling.Alerts;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

public class MenuBarJvec extends MenuBar implements JEventPublisher {


    public MenuBarJvec() {
        ArrayList<Menu> menus = new ArrayList<>();
        menus.add(createFileMenu());
        menus.add(createSettingsMenu());
        menus.add(createHelpMenu());
        menus.add(createAboutMenu());
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


            result.ifPresent(filename -> fireEventDefault(new JVecIOEvent(EventType.EXPORT_DB, filename, ExportType.BINARY)));
        });


        MenuItem importDataBase = new MenuItem("Import dataBase from binary");

        importDataBase.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Import a saved binary database");
            inputDialog.setHeaderText("Please enter a file name (without suffix)");
            inputDialog.setContentText("Filename:");

            Optional<String> result = inputDialog.showAndWait();


            result.ifPresent(filename -> fireEventDefault(new JVecIOEvent(EventType.IMPORT_DB, filename + ".jvecdb")));
        });

        MenuItem importWordsFromFile = new MenuItem("Import words from file");

        importWordsFromFile.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Import a saved binary database");
            inputDialog.setHeaderText("Please enter a file name (without suffix)");
            inputDialog.setContentText("Filename:");

            Optional<String> result = inputDialog.showAndWait();

            result.ifPresent(fileName -> fireEventDefault(new JVecIOEvent(EventType.IMPORT_DATA, fileName)));
        });

        menu.getItems().addAll(aboutItem, exportDataBase, importDataBase, importWordsFromFile);

        return menu;
    }

    private Menu createAboutMenu() {
        Menu helpMenu = new Menu("About");
        MenuItem aboutItem = new MenuItem("Open GitHub page");
        aboutItem.setOnAction(e -> {
            String url = "https://github.com/gk646/JVecDB";
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException a) {
                    a.printStackTrace();
                    Alerts.displayErrorMessage("Couldn't open WebPage");
                }
            } else {
              Alerts.displayErrorMessage("Opening a web page is not supported on this platform.");
            }
        });
        helpMenu.getItems().add(aboutItem);

        return helpMenu;
    }
}
