package jvecdb.utils.datastructures;

import java.util.Arrays;

public class JVec {
    public final float[] vector;

    public JVec(float[] vec) {
        this.vector = vec;
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

    public static JVec zero() {
        return new JVec(new float[]{});
    }
}
