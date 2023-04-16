package jvecdb.utils.datastructures.datavectors;

import org.apache.commons.math3.exception.MathArithmeticException;

import java.util.Arrays;

abstract public class JVec {
    protected final float[] vector;

    public JVec(float[] vec) {
        this.vector = vec;
    }

    public float[] getVector() {
        return vector;
    }


    public int getLength() {
        return vector.length;
    }

    public float getIndex(int index) {
        return vector[index];
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
        if (vec1.getLength() != vec2.getLength()) throw new MathArithmeticException();
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
