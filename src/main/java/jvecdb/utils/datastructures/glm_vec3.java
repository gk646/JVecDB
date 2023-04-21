package jvecdb.utils.datastructures;

public final class glm_vec3 {


    public float x;
    public float y;
    public float z;

    glm_vec3() {
        x = 0;
        y = 0;
        z = 0;
    }

    public glm_vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static glm_vec3 ZERO() {
        return new glm_vec3();
    }

}
