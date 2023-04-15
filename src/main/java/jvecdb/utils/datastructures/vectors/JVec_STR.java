package jvecdb.utils.datastructures.vectors;

import jvecdb.JVecDB;

public final class JVec_STR extends JVec {

    private final byte[] byteString;


    public JVec_STR(String s, float[] vec) {
        super(vec);
        byteString = s.getBytes(JVecDB.CHARSETS);
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
}
