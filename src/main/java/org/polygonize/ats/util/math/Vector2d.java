package org.polygonize.ats.util.math;

public class Vector2d {

    public double x;
    public double y;

    /**
     * Creates a Vector2D object initialized to zero.
     */
    public Vector2d() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vector2d negate() {
        return new Vector2d(-this.x, -this.y);
    }

    public Vector2d multiply(float scalar) {
        return new Vector2d(scalar * this.x, scalar * this.y);
    }

    public Vector2d add(Vector2d vector) {
        return new Vector2d(this.x + vector.x, this.y + vector.y);
    }

    public Vector2d substract(Vector2d vector) {
        return new Vector2d(this.x - vector.x, this.y - vector.y);
    }

    public Vector2d divide(float scalar) {
        float reciprocal = 1.0f / scalar;

        return this.multiply(reciprocal);
    }

    public Vector2d getNormal() {
        return new Vector2d(-this.y, this.x);
    }

    public double dot(Vector2d vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    public float length() {
        return (float) Math.sqrt(this.dot(this));
    }

    public Vector2d normalize() {
        if (this.length() != 0)
            return this.divide(this.length());
        else
            return this;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + this.x + "," + this.y + "]";
    }
}
