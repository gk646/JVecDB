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
        for (final float v : vector) {
            sb.append(v).append(", ");
        }
        return sb.append(getStringValue()).append("]").toString();
    }

    public static JVec ZERO() {
        return new JVecSTR("", new float[0]);
    }
}
