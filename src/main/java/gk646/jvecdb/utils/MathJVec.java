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

package gk646.jvecdb.utils;

import gk646.jvecdb.utils.datastructures.Vec3;

public final class MathJVec {
    private MathJVec(){}
    public static Vec3 mapLetterValueToSphereSurface(double letterValue, double maxLetterValue, double radius) {
        double indexRatio = letterValue / maxLetterValue; // Normalize the letter value to [0, 1] range
        double goldenAngle = Math.PI * (3 - Math.sqrt(5)); // The golden angle in radians

        double theta = goldenAngle * letterValue;
        float z = (float) ((2 * indexRatio - 1) * radius); // Map the index ratio to the range [-radius, radius]
        double r = Math.sqrt(radius * radius - z * z); // The radial distance from the z-axis
        float x = (float) (r * Math.cos(theta));
        float y = (float) (r * Math.sin(theta));

        return new Vec3(x, y, z);
    }
}
