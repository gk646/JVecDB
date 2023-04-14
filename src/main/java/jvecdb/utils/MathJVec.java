package jvecdb.utils;

import javafx.geometry.Point3D;

public class MathJVec {

    public static Point3D mapLetterValueToSphereSurface(double letterValue, double maxLetterValue, double radius) {
        double indexRatio = letterValue / maxLetterValue; // Normalize the letter value to [0, 1] range
        double goldenAngle = Math.PI * (3 - Math.sqrt(5)); // The golden angle in radians

        double theta = goldenAngle * letterValue;
        double z = (2 * indexRatio - 1) * radius; // Map the index ratio to the range [-radius, radius]
        double r = Math.sqrt(radius * radius - z * z); // The radial distance from the z-axis
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);

        return new Point3D(x, y, z);
    }
}
