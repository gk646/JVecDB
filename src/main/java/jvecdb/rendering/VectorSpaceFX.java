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

package jvecdb.rendering;


import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Shape3D;
import javafx.stage.Stage;
import jvecdb.JVecDB;
import jvecdb.rendering.vectorspace.VectorSpace;
import jvecdb.rendering.vectorspace.ui.FXMLController;
import jvecdb.rendering.vectorspace.ui.MenuBarJvec;
import jvecdb.utils.datastructures.datavectors.JVec;
import jvecdb.utils.datastructures.std_vector;
import jvecdb.utils.errorhandling.Alerts;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

import java.io.IOException;
import java.util.Collections;

public class VectorSpaceFX {

    VectorSpace vectorSpace;

    private final MenuBarJvec menuBarJvec = new MenuBarJvec();

    static int scaleFactor = 5;

    public boolean init(Stage stage) throws StartupFailure {
        return initStage(stage) & initVectorSpaceThread();
    }

    private boolean initStage(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> System.exit(1));
        BorderPane root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new StartupFailure(e);
        }

        FXMLController myController = fxmlLoader.getController();

        root.setTop(menuBarJvec);
        Group sceneRoot = new Group();
        SubScene subScene = new SubScene(sceneRoot, JVecDB.WIDTH, JVecDB.HEIGHT, true, SceneAntialiasing.BALANCED);


        myController.stackPaneCenter.getChildren().add(subScene);
        root.setCenter(myController.stackPaneCenter);
        Scene sceneWithMenu = new Scene(root, JVecDB.WIDTH, JVecDB.HEIGHT);
        stage.setScene(sceneWithMenu);
        stage.setTitle("JVecDB");

        stage.show();
        subScene.requestFocus();
        vectorSpace = new VectorSpace(root, sceneRoot, sceneWithMenu, subScene);
        return true;
    }

    private boolean initVectorSpaceThread() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    vectorSpace.keyBoardMovement();
                    Thread.sleep(16);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        return thread.isAlive();
    }

    public boolean addVisualEntry(JVec vec) {
        vectorSpace.addListToVectorSpace(Collections.singletonList(vectorSpace.getShapeFromVector(vec)));
        return true;
    }

    public void addVisualEntryList(std_vector<? extends JVec> addList) {
        std_vector<Shape3D> shapeList = new std_vector<>();
        for (JVec vec : addList) {
            shapeList.add(vectorSpace.getShapeFromVector(vec));
        }
        vectorSpace.addListToVectorSpace(shapeList);
    }

    public void reloadVectorSpaceFX() {
        vectorSpace.clearVectorSpace();
        try {
            if (!vectorSpace.reloadVectorSpace()) {
                Alerts.displayErrorMessage("Couldn't reload vectorspace with new data!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        updateInformationTreeView();
    }


    public Point3D getCameraPosition() {
        return vectorSpace.getCameraPosition();
    }

    public static int getScaleFactor() {
        return scaleFactor;
    }

    private void updateInformationTreeView() {
        FXMLController.treeItems.get(0).setValue("Entries: " + JVecDB.vectorDB.getVectorDataBase().size());
    }
}
