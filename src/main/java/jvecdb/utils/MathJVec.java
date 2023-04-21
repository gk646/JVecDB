package jvecdb.utils;

import jvecdb.utils.datastructures.glm_vec3;

public final class MathJVec {

    public static glm_vec3 mapLetterValueToSphereSurface(double letterValue, double maxLetterValue, double radius) {
        double indexRatio = letterValue / maxLetterValue; // Normalize the letter value to [0, 1] range
        double goldenAngle = Math.PI * (3 - Math.sqrt(5)); // The golden angle in radians

        double theta = goldenAngle * letterValue;
        float z = (float) ((2 * indexRatio - 1) * radius); // Map the index ratio to the range [-radius, radius]
        double r = Math.sqrt(radius * radius - z * z); // The radial distance from the z-axis
        float x = (float) (r * Math.cos(theta));
        float y = (float) (r * Math.sin(theta));

        return new glm_vec3(x, y, z);
    }
}
