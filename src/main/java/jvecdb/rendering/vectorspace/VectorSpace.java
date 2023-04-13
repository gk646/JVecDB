package jvecdb.rendering.vectorspace;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import jvecdb.rendering.vectorspace.vectorshapes.VecBox;
import jvecdb.utils.datastructures.JVec;

import java.util.ArrayList;

public class VectorSpace {
    private final Group root;

    private final ArrayList<Shape3D> shapes = new ArrayList<>();

    public VectorSpace(Group root) {
        this.root = root;
    }


    public void addBox(Point3D bounds, Point3D position, Color color, JVec vector) {
        VecBox box = new VecBox(bounds, position, color, vector);
        root.getChildren().add(box);
        shapes.add(box);
    }
    public void addBox(VecShape shape) {
        root.getChildren().add(shape);
        shapes.add(shape);
    }
}
