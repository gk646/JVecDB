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

package jvecdb.utils.datastructures.datavectors;

import java.util.Arrays;

public abstract class JVec {
    protected final float[] vector;

    protected JVec(float[] vec) {
        this.vector = vec;
    }

    public float[] getVector() {
        return vector;
    }


    public int getLength() {
        return vector.length;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JVec)) {
            return false;
        }
        return Arrays.equals(((JVec) o).vector, vector);
    }

    @Override
    public String toString() {
        return Arrays.toString(vector);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        for (float num : vector) {
            hash = 31 * hash + Float.floatToIntBits(num);
        }
        return hash;
    }

    public static float dotProduct(JVec vec1, JVec vec2) {
        if (vec1.getLength() != vec2.getLength()) throw new NumberFormatException("Given vector length is not the same");
        int len = vec1.getLength();
        float result = 0;
        for (int i = 0; i < len; i++) {
            result += vec1.vector[i] * vec2.vector[i];
        }
        return result;
    }

    public double magnitude() {
        float result = 0;
        for (var v : vector) {
            result += v * v;
        }
        return Math.sqrt(result);
    }

    public <T> T getSpecialVal() {
        return null;
    }
}
