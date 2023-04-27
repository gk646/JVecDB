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

package jvecdb.rendering.vectorspace;

import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import jvecdb.JVecDB;
import jvecdb.rendering.VectorSpaceFX;
import jvecdb.rendering.vectorspace.vectorshapes.VecBox;
import jvecdb.utils.MathJVec;
import jvecdb.utils.datastructures.datavectors.JVec;
import jvecdb.utils.datastructures.datavectors.JVecSTR;
import jvecdb.utils.datastructures.glm_vec3;
import jvecdb.utils.errorhandling.Alerts;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class VectorSpace {

    private final Scene mainScene;

    private final SubScene subScene;
    private final BorderPane layoutRoot;
    private final Group root;
    private final ArrayList<Shape3D> shapes = new ArrayList<>();
    private final Set<KeyCode> keyPresses = new HashSet<>();
    private PerspectiveCamera camera;
    Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    int radius = 150, zoomLevel;
    double lastX, lastY, moveAmount = 10, azimuth, elevation;
    float radians = 0;

    public VectorSpace(BorderPane layoutRoot, Group root, Scene mainScene, SubScene subScene) {
        this.root = root;
        this.layoutRoot = layoutRoot;
        this.mainScene = mainScene;
        this.subScene = subScene;
        initOrigin();
        initCamera();
        initControls();
    }

    public void addListToVectorSpace(List<Shape3D> shape) {
        root.getChildren().addAll(shape);
        shapes.addAll(shape);
    }

    public void clearVectorSpace() {
        Platform.runLater(() -> {
            shapes.clear();
            root.getChildren().clear();
        });
    }

    public boolean reloadVectorSpace() throws InterruptedException {
        int listSize = JVecDB.vectorDB.getVectorDataBase().size();
        List<Shape3D> addList = Collections.synchronizedList(new ArrayList<>());
        final List<? extends JVec> firstHalf = JVecDB.vectorDB.getVectorDataBase().subList(0, listSize / 2);
        final List<? extends JVec> secondHalf = JVecDB.vectorDB.getVectorDataBase().subList(listSize / 2, listSize);
        if (shapes.size() > JVecDB.MAX_DISPLAYED_VECTORS) return true;
        AtomicInteger atomicInteger = new AtomicInteger(shapes.size());
        Thread thread = new Thread(() -> {
            for (JVec vec : firstHalf) {
                if (atomicInteger.compareAndSet(JVecDB.MAX_DISPLAYED_VECTORS, atomicInteger.incrementAndGet())) break;
                addList.add(getShapeFromVector(vec));
            }
        });
        thread.start();
        Thread thread1 = new Thread(() -> {
            for (JVec vec : secondHalf) {
                if (atomicInteger.compareAndSet(JVecDB.MAX_DISPLAYED_VECTORS, atomicInteger.incrementAndGet())) break;
                addList.add(getShapeFromVector(vec));
            }
        });
        thread.join();
        thread1.join();
        Platform.runLater(() -> addListToVectorSpace(addList));
        return true;
    }


    public Shape3D getShapeFromVector(JVec vec) {
        Shape3D shape = null;
        switch (JVecDB.getActiveDataType()) {
            case STRING -> {
                switch (JVecDB.getActiveShape()) {
                    case BOX -> shape = getShapeStringBOX(vec);
                    case SPHERE -> {
                    }
                }
            }
            case NULL -> Alerts.displayErrorMessage("No datatype selected for database!");
        }
        return shape;
    }

    private VecBox getShapeStringBOX(JVec vec) {
        JVecSTR vecSTR = (JVecSTR) vec;
        double distanceFromOrigin = vecSTR.getWorldLength() * VectorSpaceFX.getScaleFactor();
        double maxLetterValue = 150;

        glm_vec3 position = MathJVec.mapLetterValueToSphereSurface(vecSTR.getLetterSum(), maxLetterValue, distanceFromOrigin);
        return new VecBox(new glm_vec3(5, 5, 5), position, Color.BLUE);
    }

    private void initOrigin() {
        VecBox box = new VecBox(new glm_vec3(5, 5, 5), new glm_vec3(0, 0, 0), Color.RED, JVecSTR.ZERO());
        root.getChildren().add(box);
        shapes.add(box);
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
                double dx = (lastX - event.getSceneX()) / 3;
                double dy = (lastY - event.getSceneY()) / 3;
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
            if (JVecDB.DEBUG) {
                System.out.println("Rotation: " + rotateX.getAngle() + " " + rotateY.getAngle() + " " + rotateZ.getAngle());
                System.out.println("Translation: " + camera.getTranslateX() + " " + camera.getTranslateY() + " " + camera.getTranslateZ());
            }
        });
        layoutRoot.setOnKeyPressed(event -> {
            keyPresses.add(event.getCode());
        });
        layoutRoot.setOnKeyReleased(event -> keyPresses.remove(event.getCode()));
    }

    public void keyBoardMovement() {
        double cameraYaw = Math.toRadians(rotateY.getAngle()); //side
        double yaw2 = cameraYaw % Math.PI * 2;
        Point2D.Double forwardVector = new Point2D.Double(Math.sin(yaw2), Math.cos(yaw2));
        double length = Math.sqrt(forwardVector.x * forwardVector.x + forwardVector.y * forwardVector.y);
        Point2D.Double normalized = new Point2D.Double(forwardVector.x / length, forwardVector.y / length);


        Point2D.Double sideDir = new Point2D.Double(forwardVector.y, -forwardVector.x);
        length = Math.sqrt(sideDir.x * sideDir.x + sideDir.y * sideDir.y);
        Point2D.Double normalizedside = new Point2D.Double(sideDir.x / length, sideDir.y / length);
        for (KeyCode code : keyPresses) {
            switch (code) {
                case W -> { // Move forward
                    camera.setTranslateX(camera.getTranslateX() - normalized.x);
                    camera.setTranslateZ(camera.getTranslateZ() + normalized.y);
                }
                case S -> { // Move backward
                    camera.setTranslateX(camera.getTranslateX() + normalized.x);
                    camera.setTranslateZ(camera.getTranslateZ() - normalized.y);
                }
                case A -> { // Move left
                    camera.setTranslateX(camera.getTranslateX() - normalizedside.x);
                    camera.setTranslateZ(camera.getTranslateZ() + normalizedside.y);
                }
                case D -> { // Move right
                    camera.setTranslateX(camera.getTranslateX() + normalizedside.x);
                    camera.setTranslateZ(camera.getTranslateZ() - normalizedside.y);
                }
                case SPACE -> {
                    camera.setTranslateY(camera.getTranslateY() - 0.4);
                    layoutRoot.requestLayout();
                }
                case CONTROL -> camera.setTranslateY(camera.getTranslateY() + 0.4);
            }
        }
    }

    public Point3D getCameraPosition() {
        return new Point3D(camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ());
    }
}
