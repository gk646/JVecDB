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
package gk646.jvecdb.database.vectorize;

import gk646.jvecdb.utils.datastructures.datavectors.JVecSTR;


import java.io.IOException;

public final class Vectorizer {


    public Vectorizer() throws IOException {
        init();
    }

    private void init() throws IOException {
        //log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        //SentenceIterator iter = new BasicLineIterator(filePath);
    }

    public JVecSTR StringSimple(String s) {
        float letterVal = 0;
        int len = s.length();
        for (var letter : s.toCharArray()) {
            if (Character.isWhitespace(letter)) continue;

            if (Character.isUpperCase(letter)) {
                letterVal += letter - 'A' + 1;
            } else if (Character.isLowerCase(letter)) {
                letterVal += letter - 'a' + 1;
            } else {
                //Alerts.displayErrorMessage("Invalid input. The input must be alphabetical characters or whitespace (ignored).");
                break;
            }
        }
        return new JVecSTR(s, new float[]{len, letterVal});
    }
}
