/*
 * MIT License
 *
 * Copyright (c) 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jvecdb.rendering.vectorspace.ui;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import jvecdb.JVecDB;
import jvecdb.events.EventType;
import jvecdb.events.JEventPublisher;
import jvecdb.events.JVecIOEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FXMLController implements JEventPublisher {
    public static List<TreeItem<String>> treeItems = new ArrayList<>();
    @FXML
    public TreeView<String> treeView;
    @FXML
    private TreeView<File> folderBrowser;
    @FXML
    public StackPane stackPaneCenter;


    public void initialize() {
        initTreeView();
        initFolderBrowser();
    }

    private void initTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("JVec DB");

        rootItem.setExpanded(false);
        TreeItem<String> childItem1 = new TreeItem<>("DataBase Info");

        TreeItem<String> entries = new TreeItem<>("Entries: " + JVecDB.vectorDB.getVectorDataBase().size());
        TreeItem<String> dataType = new TreeItem<>("DataType: " + JVecDB.getActiveDataType().toString());
        childItem1.getChildren().addAll(entries);

        TreeItem<String> childItem3 = new TreeItem<>("Position");

        rootItem.getChildren().addAll(childItem1, childItem3);

        treeView.setRoot(rootItem);
        treeItems.add(entries);
        treeItems.add(dataType);


        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleTreeViewItemSelected(newValue)
        );
    }

    public void initFolderBrowser() {
        folderBrowser.setCellFactory(param -> new TreeCell<>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String fileName = item.getName();
                    setText(fileName);
                    setGraphic(getTreeItem().getGraphic());

                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem infoItem = new MenuItem("Info");
                    infoItem.setOnAction(event -> showFileInfoDialog(item));
                    contextMenu.getItems().add(infoItem);

                    if (item.getName().contains(".txt")) {
                        MenuItem importItem = new MenuItem("Import words from textfile");
                        importItem.setOnAction(actionEvent -> JVecDB.importWordsFromFile(item.getPath()));
                        contextMenu.getItems().add(importItem);
                    } else if (item.getName().contains(".jvecdb")) {
                        MenuItem importItem = new MenuItem("Import database");
                        importItem.setOnAction(event -> fireEventDefault(new JVecIOEvent(EventType.IMPORT_DB, item.getPath())));
                        contextMenu.getItems().addAll(importItem);
                    }
                    setContextMenu(contextMenu);
                }
            }
        });
        File rootFolder = new File(".");
        TreeItem<File> rootNode = buildFileTree(rootFolder);
        folderBrowser.setRoot(rootNode);
        folderBrowser.setShowRoot(false);

        startFileBrowserUpdateThread();
    }


    private void showFileInfoDialog(File file) {

        Dialog<Void> fileInfoDialog = new Dialog<>();
        fileInfoDialog.setTitle("File Information");
        fileInfoDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);


        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));


        gridPane.add(new Label("Name:"), 0, 0);
        gridPane.add(new Label(file.getName()), 1, 0);
        gridPane.add(new Label("Path:"), 0, 1);
        gridPane.add(new Label(file.getAbsolutePath()), 1, 1);
        gridPane.add(new Label("Size:"), 0, 2);
        gridPane.add(new Label(formatFileSize(file.length())), 1, 2);


        fileInfoDialog.getDialogPane().setContent(gridPane);

        fileInfoDialog.showAndWait();
    }

    private TreeItem<File> buildFileTree(File file) {
        TreeItem<File> root = createTreeItem(file);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    root.getChildren().add(buildFileTree(child));
                }
            }
        }

        return root;
    }

    private TreeItem<File> createTreeItem(File file) {
        TreeItem<File> treeItem = new TreeItem<>(file);
        ImageView icon = new ImageView();

        if (file.isDirectory()) {
            icon.setImage(new Image("/icons/folder.png"));
        } else {
            String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
            switch (fileType) {
                case "txt" -> icon.setImage(new Image("/icons/text.png"));
                case "jvecdb" -> icon.setImage(new Image("/icons/jvecdb.png"));
                default -> icon.setImage(new Image("/icons/file.png"));
            }
        }

        treeItem.setGraphic(icon);
        return treeItem;
    }

    @FXML
    private void handleTreeViewItemSelected(TreeItem<String> selectedItem) {
        if (selectedItem != null) {
            System.out.println("Selected item: " + selectedItem.getValue());
        }
    }


    private String formatFileSize(long sizeInBytes) {
        if (sizeInBytes < 1024) {
            return sizeInBytes + " B";
        }

        int digitGroups = (int) (Math.log10(sizeInBytes) / Math.log10(1024));
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");

        return decimalFormat.format(sizeInBytes / Math.pow(1024, digitGroups))
                + " " + ("KMGTPE").charAt(digitGroups - 1) + "B";
    }

    public void startFileBrowserUpdateThread() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1500), event -> {
            //
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        new Thread(() -> {
            long size = 0;
            while (true) {
                if (size != getDirectorySize()) {
                    Platform.runLater(() -> {
                        TreeItem<File> rootNode = buildFileTree(new File("."));
                        folderBrowser.setRoot(rootNode);
                    });
                    size = getDirectorySize();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }


    private long getDirectorySize() {
        try (Stream<Path> stream = Files.walk(Path.of("."))) {
            return stream
                    .filter(path -> path.toFile().isFile())
                    .mapToLong(path -> path.toFile().length())
                    .sum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
