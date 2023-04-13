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


import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import jvecdb.JVecDB;
import jvecdb.rendering.ui.BasicUI;
import jvecdb.rendering.vectorspace.VectorSpace;
import jvecdb.utils.datastructures.JVec;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

public class VectorSpaceFX {

    VectorSpace vectorSpace;
    /**
     * Main scene
     */
    private Scene mainScene;
    /**
     * Subscene
     */
    private SubScene subScene;
    /**
     * Main group
     */
    private Group root;
    /**
     * Main stage
     */
    private Stage stage;

    /**
     * Main camera
     */
    private PerspectiveCamera camera;

    /**
     * BasicUI
     */
    private final BasicUI basicUI = new BasicUI();
    //VARIABLES
    Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    Translate translate = new Translate();
    double lastX, lastY, moveAmount = 10, azimuth, elevation;
    int zoomLevel;

    public boolean init(Stage stage) throws StartupFailure {
        initStage(stage);
        initVectorSpace();
        initOrigin();
        initCamera();
        initControls();
        return true;
    }

    private void initStage(Stage stage) {
        this.stage = stage;

        // Create a BorderPane as the root layout
        BorderPane root = new BorderPane();

        // Create and add the menu bar to the top of the root layout
        root.setTop(basicUI);

        // Create the main 3D scene and set it as the center of the root layout
        Group sceneRoot = new Group();
        SubScene subScene = new SubScene(sceneRoot, JVecDB.WIDTH, JVecDB.HEIGHT, true, SceneAntialiasing.BALANCED);
        root.setCenter(subScene);


        // Set the scene containing the BorderPane as the stage's scene
        Scene sceneWithMenu = new Scene(root, JVecDB.WIDTH, JVecDB.HEIGHT);
        this.stage.setScene(sceneWithMenu);
        this.stage.setTitle("JVecDB");

        this.root = sceneRoot;
        this.mainScene = sceneWithMenu;
        this.subScene = subScene;
        stage.show();
        subScene.requestFocus();
    }

    private void initVectorSpace() {
        vectorSpace = new VectorSpace(root);
    }

    private void initOrigin() {
        vectorSpace.addBox(new Point3D(5, 5, 5), new Point3D(0, 0, 0), Color.RED, JVec.zero());
    }

    private void initCamera() {
        Translate translate = new Translate();
        this.camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(100000);
        camera.setTranslateZ(-150);
        camera.setFieldOfView(100);
        camera.setVerticalFieldOfView(true);
        camera.getTransforms().addAll(translate);
        subScene.setCamera(camera);
    }

    private void initControls() {
        mainScene.setOnMousePressed(event -> {
            lastX = event.getSceneX();
            lastY = event.getSceneY();
        });
        mainScene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double dx = event.getSceneX() - lastX;
                double dy = event.getSceneY() - lastY;
                rotateX.setAngle(rotateX.getAngle() - dy);
                rotateY.setAngle(rotateY.getAngle() + dx);
                camera.getTransforms().clear();
                camera.getTransforms().addAll(translate, rotateX, rotateY);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                double dx = lastX - event.getSceneX();
                double dy = lastY - event.getSceneY();

                azimuth += dx * 0.005;
                elevation += dy * 0.005;

                double radius = 50;
                double centerX = 0;
                double centerY = 0;
                double centerZ = 0;

                double x = centerX + radius * Math.cos(elevation) * Math.sin(azimuth);
                double y = centerY + radius * Math.sin(elevation);
                double z = centerZ + radius * Math.cos(elevation) * Math.cos(azimuth);

                camera.setTranslateX(x);
                camera.setTranslateY(y);
                camera.setTranslateZ(z);

                //camera.lookAt(centerX, centerY, centerZ, new Point3D(0, 1, 0));

            }
            lastX = event.getSceneX();
            lastY = event.getSceneY();
        });
        mainScene.setOnKeyPressed(event -> {
            double cameraYaw = Math.toRadians(rotateY.getAngle());
            double dx, dz;

            switch (event.getCode()) {
                case S -> {
                    dx = moveAmount * Math.sin(cameraYaw);
                    dz = -moveAmount * Math.cos(cameraYaw);
                    translate.setX(translate.getX() - dx);
                    translate.setZ(translate.getZ() + dz);
                }
                case W -> {
                    dx = moveAmount * Math.sin(cameraYaw);
                    dz = -moveAmount * Math.cos(cameraYaw);
                    translate.setX(translate.getX() + dx);
                    translate.setZ(translate.getZ() - dz);
                }
                case D -> {
                    dx = moveAmount * Math.sin(cameraYaw - Math.PI / 2);
                    dz = -moveAmount * Math.cos(cameraYaw - Math.PI / 2);
                    translate.setX(translate.getX() - dx);
                    translate.setZ(translate.getZ() + dz);
                }
                case A -> {
                    dx = moveAmount * Math.sin(cameraYaw + Math.PI / 2);
                    dz = -moveAmount * Math.cos(cameraYaw + Math.PI / 2);
                    translate.setX(translate.getX() - dx);
                    translate.setZ(translate.getZ() + dz);
                }
            }
            camera.getTransforms().clear();
            camera.getTransforms().addAll(translate, rotateX, rotateY);
        });
    }

    public boolean addVisualEntry(Shape3D shape) {
        vectorSpace.addBox(shape);
        return true;
    }

    public Stage getStage() {
        return stage;
    }

    public SubScene getSubScene() {
        return subScene;
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public Group getRoot() {
        return root;
    }
}
