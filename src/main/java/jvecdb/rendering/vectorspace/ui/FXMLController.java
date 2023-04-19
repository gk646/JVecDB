package jvecdb.rendering.vectorspace.ui;


import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import jvecdb.JVecDB;

import java.io.File;

public class FXMLController {

    public FXMLController() {

    }

    @FXML
    private TreeView<String> treeView;
    @FXML
    private TreeView<File> folderBrowser;

    @FXML
    public void initialize() {
        initTreeView();
        initFolderBrowser();
    }

    private void initTreeView() {
        // Create the root tree item
        TreeItem<String> rootItem = new TreeItem<>("JVec DB");
        rootItem.setExpanded(false);

        // Add child nodes
        TreeItem<String> childItem1 = new TreeItem<>("DataBase");

        TreeItem<String> childItem2 = new TreeItem<>("Entries:" + JVecDB.vectorDB.getVectorDataBase().size());
        childItem1.getChildren().add(childItem2);

        TreeItem<String> childItem3 = new TreeItem<>("Child Node 2");

        rootItem.getChildren().addAll(childItem1, childItem2);

        // Set the root item for the TreeView
        treeView.setRoot(rootItem);

        // Add an event handler for selecting tree items
        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleTreeViewItemSelected(newValue)
        );
    }
    public void initFolderBrowser() {
        folderBrowser.setCellFactory(param -> new TreeCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        });

        File rootFolder = new File(".");
        TreeItem<File> rootNode = buildFileTree(rootFolder);
        folderBrowser.setRoot(rootNode);
        folderBrowser.setShowRoot(false);
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
            // Customize the file icon based on the file type
            String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
            switch (fileType) {
                case "txt":
                    icon.setImage(new Image("/icons/jvecdb.png"));
                case "jvecdb":
                    icon.setImage(new Image("/icons/jvecdb.png"));
                    break;
                // Add more cases for other file types
                default:
                    icon.setImage(new Image("/icons/jvecdb.png"));
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
}
