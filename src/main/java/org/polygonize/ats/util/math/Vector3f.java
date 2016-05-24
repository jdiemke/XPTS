package org.polygonize.ats.util.math;

/**
 * The Vector3f class defines methods and attributes of a three dimensional
 * vector.
 * 
 * @author Johannes Diemke
 */
public class Vector3f {

    public float x;
    public float y;
    public float z;

    public final static Vector3f ZERO = new Vector3f(0.0f, 0.0f, 0.0f);

    /**
     * Creates a Vector3D object initialized to zero.
     */
    public Vector3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vector3f(Vector4f vector) {
        this.x = vector.x / vector.w;
        this.y = vector.y / vector.w;
        this.z = vector.z / vector.w;
    }

    public Vector3f negate() {
        return new Vector3f(-this.x, -this.y, -this.z);
    }

    public Vector3f multiply(float scalar) {
        return new Vector3f(scalar * this.x, scalar * this.y, scalar * this.z);
    }

    public Vector3f divide(float scalar) {
        float reciprocal = 1.0f / scalar;

        return this.multiply(reciprocal);
    }

    public Vector3f add(Vector3f vector) {
        return new Vector3f(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    public Vector3f substract(Vector3f vector) {
        return new Vector3f(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public float dot(Vector3f vector) {
        return this.x * vector.x + this.y * vector.y + this.z * vector.z;
    }

    public Vector3f cross(Vector3f vector) {
        return new Vector3f(this.y * vector.z - this.z * vector.y, this.z * vector.x - this.x * vector.z,
                this.x * vector.y - this.y * vector.x);
    }

    public float length() {
        return (float) Math.sqrt(this.dot(this));
    }

    public float distance(Vector3f vector) {
        return this.substract(vector).length();
    }

    public Vector3f normalize() {
        return this.divide(this.length());
    }

    public Vector4f toHomogenousCoordinate() {
        return new Vector4f(this);
    }

    @Override
    public String toString() {
        return super.toString() + "[" + this.x + "," + this.y + "," + this.z + "]";
    }

}