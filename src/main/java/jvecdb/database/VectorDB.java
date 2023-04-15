/*
 * Copyright © 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jvecdb.database;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import jvecdb.JVecDB;
import jvecdb.database.db.DataBase;
import jvecdb.database.vectorize.Vectorizer;
import jvecdb.rendering.vectorspace.vectorshapes.VecBox;
import jvecdb.utils.MathJVec;
import jvecdb.utils.datastructures.vectors.JVec;
import jvecdb.utils.datastructures.vectors.JVec_STR;
import jvecdb.utils.enums.ExportType;
import jvecdb.utils.errorhandling.Alerts;

import java.io.IOException;
import java.util.ArrayList;

public final class VectorDB {
    DataBase dataBase;
    Vectorizer vectorizer;
    ArrayList<JVec> JVecDataBase = new ArrayList<>();


    public boolean init() throws IOException {
        dataBase = new DataBase();
        vectorizer = new Vectorizer();
        dataBase.makeExportFolder();
        return true;
    }

    public Shape3D addStringToDB(String inputString) {
        JVec vec = vectorizer.StringSimple(inputString);
        JVecDataBase.add(vec);

        System.out.println(vec);
        return getVectorSpaceShape(vec);
    }

    public ArrayList<JVec> getVectorDataBase() {
        return JVecDataBase;
    }

    public void exportDataBase(String fileName, ExportType exportType) {
        dataBase.exportDataBase(JVecDataBase, fileName, exportType);
    }

    private Shape3D getVectorSpaceShape(JVec vec) {
        Shape3D shape = null;
        switch (JVecDB.ACTIVE_DATA_TYPE) {
            case STRING -> {
                switch (JVecDB.ACTIVE_SHAPE) {
                    case BOX -> shape = getShapeStringBOX(vec);
                    case SPHERE -> {
                    }
                }
            }
            case NULL -> Alerts.displayErrorMessage("No datatype selected for database!");
        }
        return shape;
    }

    private Shape3D getShapeStringBOX(JVec vec) {
        JVec_STR vec_str = (JVec_STR) vec;
        double radius = vec_str.getWorldLength() * dataBase.getScaleFactor();
        double maxLetterValue = 150;

        Point3D position = MathJVec.mapLetterValueToSphereSurface(vec_str.getLetterSum(), maxLetterValue, radius);
        System.out.println(position);
        return new VecBox(new Point3D(5, 5, 5), position, Color.BLUE);
    }
}
