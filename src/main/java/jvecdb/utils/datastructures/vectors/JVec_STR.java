package jvecdb.utils.datastructures.vectors;

import java.nio.charset.StandardCharsets;

public final class JVec_STR extends JVec {

    private final byte[] stringValue;


    public JVec_STR(String s, float[] vec) {
        super(vec);
        stringValue = s.getBytes(StandardCharsets.US_ASCII);
    }

    public float getWorldLength() {
        return vector[0];
    }

    public float getLetterSum() {
        return vector[1];
    }

    public String getStringValue() {
        StringBuilder sb = new StringBuilder(stringValue.length);
        for (byte b : stringValue) {
            sb.append((char) b);
        }
        return sb.toString();
    }
}
