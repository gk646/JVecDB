/*
 * Copyright © 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package jvecdb.database.vectorize;

import jvecdb.utils.datastructures.JVec;
import jvecdb.utils.errorhandling.Alerts;
import org.nd4j.common.io.ClassPathResource;

import java.io.IOException;

public class Vectorizer {


    public Vectorizer() throws IOException {
        init();
    }

    private void init() throws IOException {
        String filePath = new ClassPathResource("raw_sentences.txt").getFile().getAbsolutePath();
        //log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        //SentenceIterator iter = new BasicLineIterator(filePath);
    }

    public JVec StringSimple(String s) {
        float letterVal = 0;
        float letterVar = 0;
        int len = s.length();
        int vowels = 0;
        int consonants = 0;

        for (var letter : s.toCharArray()) {
            if (Character.isWhitespace(letter)) continue;

            if (Character.isUpperCase(letter) || Character.isLowerCase(letter)) {
                char lowerCaseLetter = Character.toLowerCase(letter);

                if ("aeiou".indexOf(lowerCaseLetter) >= 0) {
                    vowels++;
                } else {
                    consonants++;
                }

                int letterValue = lowerCaseLetter - 'a' + 1;
                letterVal += letterValue;
                letterVar += letterValue * letterValue;
            } else {
                Alerts.displayErrorMessage("Invalid input. The input must be alphabetical characters or whitespace (ignored).");
            }
        }

        float avgLetterVal = letterVal / len;
        float variance = (letterVar / len) - (avgLetterVal * avgLetterVal);
        float vowelConsonantRatio = (float) vowels / (float) consonants;
        return new JVec(new float[]{len * 13 * vowelConsonantRatio, letterVal * variance, letterVal / len});
    }
}
