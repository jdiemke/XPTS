package org.polygonize.ats.util;

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D() {
    }

    public boolean equals(Vector3D vect) {
        /*
         * if(Math.abs(this.x - vect.x) <0.002 && Math.abs(this.y -
         * vect.y)<0.002 && Math.abs(this.z - vect.z)<0.002) return true; else
         * return false;
         */
        if (this.x == vect.x && this.y == vect.y && this.z == vect.z)
            return true;
        else
            return false;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Vector3D vect) {
        this.x = vect.x;
        this.y = vect.y;
        this.z = vect.z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3D vect) {
        this.x = vect.x;
        this.y = vect.y;
        this.z = vect.z;
    }

    public void add(Vector3D vect1, Vector3D vect2) {
        this.x = vect1.x + vect2.x;
        this.y = vect1.y + vect2.y;
        this.z = vect1.z + vect2.z;
    }

    public Vector3D add(Vector3D vect) {
        return new Vector3D(this.x + vect.x, this.y + vect.y, this.z + vect.z);
    }

    public void sub(Vector3D vect1, Vector3D vect2) {
        this.x = vect1.x - vect2.x;
        this.y = vect1.y - vect2.y;
        this.z = vect1.z - vect2.z;
    }

    public Vector3D sub(Vector3D vect) {
        return new Vector3D(this.x - vect.x, this.y - vect.y, this.z - vect.z);
    }

    public void cross(Vector3D vect1, Vector3D vect2) {
        this.x = vect1.y * vect2.z - vect1.z * vect2.y;
        this.y = vect1.z * vect2.x - vect1.x * vect2.z;
        this.z = vect1.x * vect2.y - vect1.y * vect2.x;
    }

    public Vector3D cross(Vector3D vect) {
        return new Vector3D(this.y * vect.z - this.z * vect.y, this.z * vect.x - this.x * vect.z,
                this.x * vect.y - this.y * vect.x);
    }

    public double dot(Vector3D vect) {
        return this.x * vect.x + this.y * vect.y + this.z * vect.z;
    }

    public void mult(double scalar, Vector3D vect) {
        x = vect.x * scalar;
        y = vect.y * scalar;
        z = vect.z * scalar;
    }

    public void mult(Vector3D vec1, Vector3D vec2) {
        x = vec1.x * vec2.x;
        y = vec1.y * vec2.y;
        z = vec1.z * vec2.z;
    }

    public Vector3D mult(double scalar) {
        return new Vector3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public void normalize(Vector3D vect) {
        double magSq = vect.x * vect.x + vect.y * vect.y + vect.z * vect.z;

        if (magSq > 0.0f) {
            double oneOverMag = (double) (1.0f / Math.sqrt(magSq));
            this.x = vect.x * oneOverMag;
            this.y = vect.y * oneOverMag;
            this.z = vect.z * oneOverMag;
        }
    }

    public Vector3D getNormalized() {
        Vector3D temp = new Vector3D();
        double magSq = this.x * this.x + this.y * this.y + this.z * this.z;

        if (magSq > 0.0) {
            double oneOverMag = (double) (1.0 / Math.sqrt(magSq));
            temp.x = this.x * oneOverMag;
            temp.y = this.y * oneOverMag;
            temp.z = this.z * oneOverMag;
        }
        return temp;
    }

    public double length() {
        double magSq = x * x + y * y + z * z;

        return (double) Math.sqrt(magSq);
    }

    public void rescale(double scalar) {
        this.mult(scalar / this.length());
    }

    public Vector3D getRescaled(double scalar) {
        return (new Vector3D(this)).mult(scalar / this.length());
    }

    public double getAngle(Vector3D normal) {
        return (double) Math.acos(this.dot(normal));
    }

    public Vector3D reflect(Vector3D normal) {
        Vector3D vect = new Vector3D(this.getNormalized());
        return vect.sub(normal.mult(2.0f * vect.dot(normal))).mult(this.length());
    }

}
