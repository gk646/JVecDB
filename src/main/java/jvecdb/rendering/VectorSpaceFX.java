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
import javafx.scene.Camera;
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
import jvecdb.utils.datastructures.vectors.JVec;
import jvecdb.utils.errorhandling.exceptions.StartupFailure;

import java.awt.geom.Point2D;

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
    int radius = 150;
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
        vectorSpace.addBox(new Point3D(5, 5, 5), new Point3D(0, 0, 0), Color.RED, JVec.ZERO());
    }

    private void initCamera() {
        Translate translate = new Translate();
        this.camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(100000);
        camera.setTranslateZ(-150);
        camera.setFieldOfView(42);
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
                double dx = lastX - event.getSceneX();
                double dy = lastY - event.getSceneY();

                double newRotateY = (rotateY.getAngle() + dx) % 360;
                double newRotateX = rotateX.getAngle() - dy;
                newRotateX = Math.max(Math.min(newRotateX, 45), -45);

                rotateX.setAngle(newRotateX);
                rotateY.setAngle(newRotateY);

                double radianX = Math.toRadians(newRotateX);
                double radianY = Math.toRadians(newRotateY);

                double xDirection = -Math.sin(radianY) * Math.cos(radianX);
                double yDirection = -Math.sin(radianX);
                double zDirection = Math.cos(radianY) * Math.cos(radianX);

                Point3D direction = new Point3D(xDirection, yDirection, zDirection);
                Point3D up = new Point3D(0, 1, 0);
                Point3D right = direction.crossProduct(up);


                camera.getTransforms().setAll(new Rotate(newRotateX, right), new Rotate(-newRotateY, up));
            } else if (event.getButton() == MouseButton.SECONDARY) {
                {
                    double dx = lastX - event.getSceneX();
                    double dy = lastY - event.getSceneY();

                    azimuth += dx * 0.01;
                    elevation += dy * 0.01;


                    double centerX = 0;
                    double centerY = 0;
                    double centerZ = 0;

                    double x = centerX + radius * Math.cos(elevation) * Math.sin(azimuth);
                    double y = centerY + radius * Math.sin(elevation);
                    double z = centerZ + radius * Math.cos(elevation) * Math.cos(azimuth);

                    camera.setTranslateX(x);
                    camera.setTranslateY(y);
                    camera.setTranslateZ(z);
                }

                double cameraX = camera.getTranslateX();
                double cameraY = camera.getTranslateY();
                double cameraZ = camera.getTranslateZ();


                double pitch;
                if (cameraY > 0) {
                    pitch = Math.toDegrees(Math.asin(-cameraZ / new Point2D.Double(cameraZ, cameraY).distance(new Point2D.Double(0, 0))));
                    pitch = 90 - pitch;
                } else {
                    pitch = Math.toDegrees(Math.asin(-cameraZ / new Point2D.Double(cameraZ, cameraY).distance(new Point2D.Double(0, 0))));
                    pitch = -90 + pitch;
                }

                double yaw;
                if (cameraX > 0) {
                    yaw = Math.toDegrees(Math.asin(-cameraZ / new Point2D.Double(cameraX, cameraZ).distance(new Point2D.Double(0, 0))));
                    yaw = 90 - yaw;
                } else {
                    yaw = Math.toDegrees(Math.asin(-cameraZ / new Point2D.Double(cameraX, cameraZ).distance(new Point2D.Double(0, 0))));
                    yaw = -90 + yaw;
                }

                rotateX.setAngle(pitch);
                //rotateY.setAngle(-yaw);
                camera.getTransforms().clear();
                camera.getTransforms().addAll(rotateY, rotateX);
            }
            lastX = event.getSceneX();
            lastY = event.getSceneY();
            System.out.println("Rotation: " + rotateX.getAngle() + " " + rotateY.getAngle() + " " + rotateZ.getAngle());
            System.out.println("Translation: " + camera.getTranslateX() + " " + camera.getTranslateY() + " " + camera.getTranslateZ());
        });
        mainScene.setOnKeyPressed(event -> {
            double cameraYaw = Math.toRadians(rotateY.getAngle());
            double cameraPitch = Math.toRadians(rotateX.getAngle());
            double dx, dy, dz;
            switch (event.getCode()) {
                case W -> {
                    radius++;
                    dx = moveAmount * Math.sin(cameraYaw) * Math.cos(cameraPitch);
                    dy = moveAmount * Math.sin(cameraPitch);
                    dz = -moveAmount * Math.cos(cameraYaw) * Math.cos(cameraPitch);
                    camera.setTranslateX(camera.getTranslateX() - dx);
                    camera.setTranslateY(camera.getTranslateY() - dy);
                    camera.setTranslateZ(camera.getTranslateZ() + dz);
                }
                case S -> {
                    radius--;
                    dx = moveAmount * Math.sin(cameraYaw) * Math.cos(cameraPitch);
                    dy = moveAmount * Math.sin(cameraPitch);
                    dz = -moveAmount * Math.cos(cameraYaw) * Math.cos(cameraPitch);
                    camera.setTranslateX(camera.getTranslateX() + dx);
                    camera.setTranslateY(camera.getTranslateY() + dy);
                    camera.setTranslateZ(camera.getTranslateZ() - dz);
                }
                case A -> {
                    dx = moveAmount * Math.sin(cameraYaw - Math.PI / 2);
                    dz = -moveAmount * Math.cos(cameraYaw - Math.PI / 2);
                    camera.setTranslateX(camera.getTranslateX() - dx);
                    camera.setTranslateZ(camera.getTranslateZ() + dz);
                }
                case D -> {
                    dx = moveAmount * Math.sin(cameraYaw + Math.PI / 2);
                    dz = -moveAmount * Math.cos(cameraYaw + Math.PI / 2);
                    camera.setTranslateX(camera.getTranslateX() - dx);
                    camera.setTranslateZ(camera.getTranslateZ() + dz);
                }
            }
        });
    }

    private void lookAt(Camera camera, Point3D target, Point3D up) {
        Point3D zAxis = new Point3D(
                camera.getTranslateX() - target.getX(),
                camera.getTranslateY() - target.getY(),
                camera.getTranslateZ() - target.getZ()
        ).normalize();
        Point3D xAxis = up.crossProduct(zAxis).normalize();
        Point3D yAxis = zAxis.crossProduct(xAxis);

        Rotate rotateX = new Rotate(-Math.toDegrees(Math.atan2(yAxis.getZ(), yAxis.getY())), Rotate.X_AXIS);
        Rotate rotateY = new Rotate(Math.toDegrees(Math.atan2(-zAxis.getZ(), zAxis.getX())), Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(-Math.toDegrees(Math.atan2(xAxis.getY(), xAxis.getX())), Rotate.Z_AXIS);

        camera.getTransforms().setAll(rotateX, rotateY, rotateZ);
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
