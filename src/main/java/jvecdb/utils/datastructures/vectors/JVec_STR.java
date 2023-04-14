package jvecdb.utils.datastructures.vectors;

public final class JVec_STR extends JVec {


    public JVec_STR(float[] vec) {
        super(vec);
    }

    public float getWorldLength() {
        return vector[0];
    }

    public float getLetterSum() {
        return vector[1];
    }
}
