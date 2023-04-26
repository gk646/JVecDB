package jvecdb.utils.datastructures.datavectors;

import jvecdb.JVecDB;

public final class JVecSTR extends JVec {

    private final byte[] byteString;


    public JVecSTR(String s, float[] vec) {
        super(vec);
        byteString = s.getBytes(JVecDB.CHARSET);
    }

    public JVecSTR(byte[] wordBytes, float[] vec) {
        super(vec);
        byteString = wordBytes;
    }

    public float getWorldLength() {
        return vector[0];
    }

    public float getLetterSum() {
        return vector[1];
    }

    public String getStringValue() {
        StringBuilder sb = new StringBuilder(byteString.length);
        for (byte b : byteString) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    public byte[] getByteValue() {
        return byteString;
    }

    @Override
    public String getSpecialVal() {
        return getStringValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]).append(", ");
        }
        return sb.append(getStringValue()).append("]").toString();
    }

    public static JVec ZERO() {
        return new JVecSTR("", new float[0]);
    }
}
