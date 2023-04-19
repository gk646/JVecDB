/*
 * Copyright © 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jvecdb.rendering;


import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jvecdb.JVecDB;
import jvecdb.rendering.vectorspace.VectorSpace;
import jvecdb.rendering.vectorspace.ui.MenuBarJvec;
import jvecdb.utils.datastructures.datavectors.JVec;
import jvecdb.utils.errorhandling.Alerts;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

import java.io.IOException;
import java.util.Objects;

public class VectorSpaceFX {

    VectorSpace vectorSpace;
    private Stage stage;
    private final MenuBarJvec menuBarJvec = new MenuBarJvec();

    static int scaleFactor = 5;


    public boolean init(Stage stage) throws StartupFailure {
        return initStage(stage);
    }

    private boolean initStage(Stage stage) {
        this.stage = stage;
        BorderPane root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        root.setTop(menuBarJvec);
        Group sceneRoot = new Group();
        SubScene subScene = new SubScene(sceneRoot, JVecDB.WIDTH, JVecDB.HEIGHT, true, SceneAntialiasing.BALANCED);
        root.setCenter(subScene);

        Scene sceneWithMenu = new Scene(root, JVecDB.WIDTH, JVecDB.HEIGHT);
        this.stage.setScene(sceneWithMenu);
        this.stage.setTitle("JVecDB");

        stage.show();
        subScene.requestFocus();
        vectorSpace = new VectorSpace(sceneRoot, sceneWithMenu, subScene);
        return true;
    }


    public boolean addVisualEntry(JVec vec) {
        vectorSpace.addShapeToVectorSpace(vectorSpace.getShapeFromVector(vec));
        return true;
    }

    public void reloadVectorSpaceFX() {
        vectorSpace.clearVectorSpace();
        boolean success;
        success = vectorSpace.reloadVectorSpace();
        if (!success) {
            Alerts.displayErrorMessage("Couldn't reload vectorspace with new data!");
        }
    }

    public Point3D getCameraPosition() {
        return vectorSpace.getCameraPosition();
    }

    public static int getScaleFactor() {
        return scaleFactor;
    }
}
