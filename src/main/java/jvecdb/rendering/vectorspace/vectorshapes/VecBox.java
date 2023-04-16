package jvecdb.rendering.vectorspace.vectorshapes;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import jvecdb.utils.datastructures.datavectors.JVec;

public final class VecBox extends Box {

    private JVec vector;

    public VecBox(Point3D bounds, Point3D position, Color color) {
        super(bounds.getX(), bounds.getX(), bounds.getZ());
        setTranslateX(position.getX());
        setTranslateY(position.getY());
        setTranslateZ(position.getZ());
        setMaterial(new PhongMaterial(color));
    }

    public VecBox(Point3D bounds, Point3D position, Color color, JVec vector) {
        this(bounds, position, color);
        this.vector = vector;
    }

    public JVec getVector() {
        return vector;
    }

    public <T> T getVectorSpecialVal() {
        return vector.getSpecialVal();
    }
}
