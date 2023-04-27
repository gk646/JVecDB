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

package jvecdb.rendering.vectorspace.vectorshapes;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import jvecdb.utils.datastructures.datavectors.JVec;
import jvecdb.utils.datastructures.glm_vec3;

public final class VecBox extends Box {

    private JVec vector;

    public VecBox(glm_vec3 bounds, glm_vec3 position, Color color) {
        super(bounds.x, bounds.y, bounds.z);
        setTranslateX(position.x);
        setTranslateY(position.y);
        setTranslateZ(position.z);
        setMaterial(new PhongMaterial(color));
    }

    public VecBox(glm_vec3 bounds, glm_vec3 position, Color color, JVec vector) {
        this(bounds, position, color);
        this.vector = vector;
    }

    public JVec getVector() {
        return vector;
    }

    public <T> T getVectorSpecialVal() {
        return vector.getSpecialVal();
    }
}
