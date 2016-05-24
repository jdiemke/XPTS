package org.polygonize.ats.util.math;

public class Vector2f {

    public float x;
    public float y;

    /**
     * Creates a Vector2D object initialized to zero.
     */
    public Vector2f() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vector2f negate() {
        return new Vector2f(-this.x, -this.y);
    }

    public Vector2f multiply(float scalar) {
        return new Vector2f(scalar * this.x, scalar * this.y);
    }

    public Vector2f add(Vector2f vector) {
        return new Vector2f(this.x + vector.x, this.y + vector.y);
    }

    public Vector2f substract(Vector2f vector) {
        return new Vector2f(this.x - vector.x, this.y - vector.y);
    }

    public Vector2f divide(float scalar) {
        float reciprocal = 1.0f / scalar;

        return this.multiply(reciprocal);
    }

    public Vector2f getNormal() {
        return new Vector2f(-this.y, this.x);
    }

    public float dot(Vector2f vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    public float length() {
        return (float) Math.sqrt(this.dot(this));
    }

    public Vector2f normalize() {
        return this.divide(this.length());
    }

    @Override
    public String toString() {
        return super.toString() + "[" + this.x + "," + this.y + "]";
    }
}
