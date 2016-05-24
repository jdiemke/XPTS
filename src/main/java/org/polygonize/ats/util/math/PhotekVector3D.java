package org.polygonize.ats.util.math;

public class PhotekVector3D {

    public float x;
    public float y;
    public float z;

    public PhotekVector3D() {
    }

    public boolean equals(PhotekVector3D vect) {
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

    public PhotekVector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PhotekVector3D(PhotekVector3D vect) {
        this.x = vect.x;
        this.y = vect.y;
        this.z = vect.z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(PhotekVector3D vect1, PhotekVector3D vect2) {
        this.x = vect1.x + vect2.x;
        this.y = vect1.y + vect2.y;
        this.z = vect1.z + vect2.z;
    }

    public PhotekVector3D add(PhotekVector3D vect) {
        return new PhotekVector3D(this.x + vect.x, this.y + vect.y, this.z + vect.z);
    }

    public void sub(PhotekVector3D vect1, PhotekVector3D vect2) {
        this.x = vect1.x - vect2.x;
        this.y = vect1.y - vect2.y;
        this.z = vect1.z - vect2.z;
    }

    public PhotekVector3D sub(PhotekVector3D vect) {
        return new PhotekVector3D(this.x - vect.x, this.y - vect.y, this.z - vect.z);
    }

    public void cross(PhotekVector3D vect1, PhotekVector3D vect2) {
        this.x = vect1.y * vect2.z - vect1.z * vect2.y;
        this.y = vect1.z * vect2.x - vect1.x * vect2.z;
        this.z = vect1.x * vect2.y - vect1.y * vect2.x;
    }

    public PhotekVector3D cross(PhotekVector3D vect) {
        return new PhotekVector3D(this.y * vect.z - this.z * vect.y, this.z * vect.x - this.x * vect.z,
                this.x * vect.y - this.y * vect.x);
    }

    public float dot(PhotekVector3D vect) {
        return this.x * vect.x + this.y * vect.y + this.z * vect.z;
    }

    public void mult(float scalar, PhotekVector3D vect) {
        x = vect.x * scalar;
        y = vect.y * scalar;
        z = vect.z * scalar;
    }

    public PhotekVector3D getMult(float scalar) {
        return new PhotekVector3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public void mult(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public void normalize(PhotekVector3D vect) {
        float magSq = vect.x * vect.x + vect.y * vect.y + vect.z * vect.z;

        if (magSq > 0.0f) {
            float oneOverMag = (float) (1.0f / Math.sqrt(magSq));
            this.x = vect.x * oneOverMag;
            this.y = vect.y * oneOverMag;
            this.z = vect.z * oneOverMag;
        }
    }

    public PhotekVector3D getNormalized() {
        PhotekVector3D temp = new PhotekVector3D();
        float magSq = this.x * this.x + this.y * this.y + this.z * this.z;

        if (magSq > 0.0f) {
            float oneOverMag = (float) (1.0f / Math.sqrt(magSq));
            temp.x = this.x * oneOverMag;
            temp.y = this.y * oneOverMag;
            temp.z = this.z * oneOverMag;
        }
        return temp;
    }

    public float length() {
        float magSq = x * x + y * y + z * z;

        return (float) Math.sqrt(magSq);
    }

    public void rescale(float scalar) {
        this.mult(scalar / this.length());
    }

    public PhotekVector3D getRescaled(float scalar) {
        return (new PhotekVector3D(this)).getMult(scalar / this.length());
    }

    public float getAngle(PhotekVector3D normal) {
        return (float) Math.acos(this.dot(normal));
    }

    public PhotekVector3D reflect(PhotekVector3D normal) {
        PhotekVector3D vect = new PhotekVector3D(this.getNormalized());
        return vect.sub(normal.getMult(2.0f * vect.dot(normal))).getMult(this.length());
    }

}
