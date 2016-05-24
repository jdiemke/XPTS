package org.polygonize.ats.util.math;

/**
 * The Vector4f class defines methods and attributes of a four dimensional
 * vector that is used to represent homogenous coordinates.
 * 
 * @author Johannes Diemke
 */
public class Vector4f {

    public float x;
    public float y;
    public float z;
    public float w;

    /**
     * Creates a Vector3D object initialized to zero.
     */
    public Vector4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 1.0f;
    }

    public Vector4f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0f;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector4f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = vector.w;
    }

    public Vector4f(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = 1.0f;
    }

    public Vector4f multiply(float scalar) {
        return new Vector4f(scalar * this.x, scalar * this.y, scalar * this.z, scalar * this.w);
    }

    public Vector4f divide(float scalar) {
        float reciprocal = 1.0f / scalar;

        return this.multiply(reciprocal);
    }

    public Vector3f toCartesianCoordinate() {
        return new Vector3f(this);
    }

    @Override
    public String toString() {
        return super.toString() + "[" + this.x + "," + this.y + "," + this.z + "," + this.w + "]";
    }

}