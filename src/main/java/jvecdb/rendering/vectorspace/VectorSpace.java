package jvecdb.rendering.vectorspace;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import jvecdb.JVecDB;
import jvecdb.rendering.VectorSpaceFX;
import jvecdb.rendering.vectorspace.vectorshapes.VecBox;
import jvecdb.utils.MathJVec;
import jvecdb.utils.datastructures.datavectors.JVec;
import jvecdb.utils.datastructures.datavectors.JVec_STR;
import jvecdb.utils.errorhandling.Alerts;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public final class VectorSpace {

    private final Scene mainScene;

    private final SubScene subScene;

    private final Group root;
    private final ArrayList<Shape3D> shapes = new ArrayList<>();

    private PerspectiveCamera camera;
    Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    int radius = 150, zoomLevel;
    double lastX, lastY, moveAmount = 10, azimuth, elevation;
    float radians = 0;

    public VectorSpace(Group root, Scene mainScene, SubScene subScene) {
        this.root = root;
        this.mainScene = mainScene;
        this.subScene = subScene;
        initOrigin();
        initCamera();
        initControls();
    }

    public void addShapeToVectorSpace(Point3D bounds, Point3D position, Color color, JVec vector) {
        VecBox box = new VecBox(bounds, position, color, vector);
        root.getChildren().add(box);
        shapes.add(box);
    }

    public void addShapeToVectorSpace(Shape3D shape) {
        root.getChildren().add(shape);
        shapes.add(shape);
    }

    public void clearVectorSpace() {
        shapes.clear();
        root.getChildren().clear();
    }

    public boolean reloadVectorSpace() {
        ArrayList<? extends JVec> tempDataBase = JVecDB.vectorDB.getVectorDataBase();

        for (JVec vec : tempDataBase) {
            addShapeToVectorSpace(getShapeFromVector(vec));
            if (shapes.size() > JVecDB.MAX_DISPLAYED_VECTORS) break;
        }
        return true;
    }

    public int getVectorCount() {
        return shapes.size();
    }

    public Shape3D getShapeFromVector(JVec vec) {
        Shape3D shape = null;
        switch (JVecDB.ACTIVE_DATA_TYPE) {
            case STRING -> {
                switch (JVecDB.ACTIVE_SHAPE) {
                    case BOX -> shape = getShapeStringBOX(vec);
                    case SPHERE -> {
                    }
                }
            }
            case NULL -> Alerts.displayErrorMessage("No datatype selected for database!");
        }
        return shape;
    }

    private Shape3D getShapeStringBOX(JVec vec) {
        JVec_STR vec_str = (JVec_STR) vec;
        double radius = vec_str.getWorldLength() * VectorSpaceFX.getScaleFactor();
        double maxLetterValue = 150;

        Point3D position = MathJVec.mapLetterValueToSphereSurface(vec_str.getLetterSum(), maxLetterValue, radius);
        return new VecBox(new Point3D(5, 5, 5), position, Color.BLUE);
    }

    private void initOrigin() {
        addShapeToVectorSpace(new Point3D(5, 5, 5), new Point3D(0, 0, 0), Color.RED, JVec_STR.ZERO());
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
            if (JVecDB.DEBUG) {
                System.out.println("Rotation: " + rotateX.getAngle() + " " + rotateY.getAngle() + " " + rotateZ.getAngle());
                System.out.println("Translation: " + camera.getTranslateX() + " " + camera.getTranslateY() + " " + camera.getTranslateZ());
            }
        });
        mainScene.setOnKeyPressed(event -> {
            double cameraYaw = Math.toRadians(rotateY.getAngle()); //side
            double cameraPitch = Math.toRadians(rotateX.getAngle());
            double yaw2 = cameraYaw % Math.PI * 2;
            Point2D.Double forwardVector = new Point2D.Double(Math.sin(yaw2), Math.cos(yaw2));
            double length = Math.sqrt(forwardVector.x * forwardVector.x + forwardVector.y * forwardVector.y);
            Point2D.Double normalized = new Point2D.Double(forwardVector.x / length, forwardVector.y / length);


            Point2D.Double sideDir = new Point2D.Double(forwardVector.y, -forwardVector.x);
            length = Math.sqrt(sideDir.x * sideDir.x + sideDir.y * sideDir.y);
            Point2D.Double normalizedside = new Point2D.Double(sideDir.x / length, sideDir.y / length);

            switch (event.getCode()) {
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
            }
        });
    }

    public Point3D getCameraPosition() {
        return new Point3D(camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ());
    }
}
