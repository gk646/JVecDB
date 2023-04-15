package jvecdb.rendering.vectorspace;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import jvecdb.JVecDB;
import jvecdb.rendering.VectorSpaceFX;
import jvecdb.rendering.vectorspace.vectorshapes.VecBox;
import jvecdb.utils.MathJVec;
import jvecdb.utils.datastructures.vectors.JVec;
import jvecdb.utils.datastructures.vectors.JVec_STR;
import jvecdb.utils.errorhandling.Alerts;

import java.util.ArrayList;

public final class VectorSpace {
    private final Group root;

    private final ArrayList<Shape3D> shapes = new ArrayList<>();

    public VectorSpace(Group root) {
        this.root = root;
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
        }
        return true;
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
}
