package org.polygonize.ats.util.math;

import com.jogamp.opengl.GL2;

/**
 * The Matrix4f class defines methods and attributes of a single precision
 * floating point 4x4 matrix.
 * 
 * @author Johannes Diemke
 * 
 *         TODO: - http://wiki.delphigl.com/index.php/gluPerspective -
 *         http://www.gamedev.net/community/forums/topic.asp?topic_id=478055
 *         //Draw the light's frustum if(drawFrustum) { glPushMatrix();
 *         MATRIX4X4
 *         frustumMatrix=(light.projectionMatrix*light.viewMatrix).GetInverse();
 *         glMultMatrixf(frustumMatrix); glutWireCube(2.0f); glPopMatrix(); }
 */
public class Matrix4f {

    public float m11, m12, m13, m14;
    public float m21, m22, m23, m24;
    public float m31, m32, m33, m34;
    public float m41, m42, m43, m44;

    public static Matrix4f constructIdentityMatrix() {
        return new Matrix4f();
    }

    public static Matrix4f constructTranslationMatrix(float dx, float dy, float dz) {
        return new Matrix4f(1.0f, 0.0f, 0.0f, dx, 0.0f, 1.0f, 0.0f, dy, 0.0f, 0.0f, 1.0f, dz, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructScalingMatrix(float kx, float ky, float kz) {
        return new Matrix4f(kx, 0.0f, 0.0f, 0.0f, 0.0f, ky, 0.0f, 0.0f, 0.0f, 0.0f, kz, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructXRotationMatrix(float angle) {
        float radians = (float) Math.toRadians(angle);

        return new Matrix4f(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, (float) Math.cos(radians), (float) -Math.sin(radians), 0.0f,
                0.0f, (float) Math.sin(radians), (float) Math.cos(radians), 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructYRotationMatrix(float angle) {
        float radians = (float) Math.toRadians(angle);

        return new Matrix4f((float) Math.cos(radians), 0.0f, (float) Math.sin(radians), 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                (float) -Math.sin(radians), 0.0f, (float) Math.cos(radians), 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructZRotationMatrix(float angle) {
        float radians = (float) Math.toRadians(angle);

        return new Matrix4f((float) Math.cos(radians), (float) -Math.sin(radians), 0.0f, 0.0f,
                (float) Math.sin(radians), (float) Math.cos(radians), 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f);
    }

    public static Matrix4f constructArbitraryRotationMatrix(Vector3f normal, float angle) {
        float radians = (float) Math.toRadians(angle);

        Vector3f n = normal.normalize();

        // construct basis vectors
        Vector3f p = new Vector3f(n.x * n.x * (1 - (float) Math.cos(radians)) + (float) Math.cos(radians),
                n.x * n.y * (1 - (float) Math.cos(radians)) + n.z * (float) Math.sin(radians),
                n.x * n.z * (1 - (float) Math.cos(radians)) - n.y * (float) Math.sin(radians));

        Vector3f q = new Vector3f(n.x * n.y * (1 - (float) Math.cos(radians)) - n.z * (float) Math.sin(radians),
                n.y * n.y * (1 - (float) Math.cos(radians)) + (float) Math.cos(radians),
                n.y * n.z * (1 - (float) Math.cos(radians)) + n.x * (float) Math.sin(radians));

        Vector3f r = new Vector3f(n.x * n.z * (1 - (float) Math.cos(radians)) + n.y * (float) Math.sin(radians),
                n.y * n.z * (1 - (float) Math.cos(radians)) - n.x * (float) Math.sin(radians),
                n.z * n.z * (1 - (float) Math.cos(radians)) + (float) Math.cos(radians));

        return new Matrix4f(p.x, q.x, r.x, 0.0f, p.y, q.y, r.y, 0.0f, p.z, q.z, r.z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructArbitraryScaleMatrix(Vector3f normal, float k) {

        Vector3f n = normal.normalize();

        // construct basis vectors
        Vector3f p = new Vector3f(1 + (k - 1) * n.x * n.x, (k - 1) * n.x * n.y, (k - 1) * n.x * n.z);

        Vector3f q = new Vector3f((k - 1) * n.x * n.y, 1 + (k - 1) * n.y * n.y, (k - 1) * n.y * n.z);

        Vector3f r = new Vector3f((k - 1) * n.x * n.z, (k - 1) * n.y * n.z, 1 + (k - 1) * n.z * n.z);

        return new Matrix4f(p.x, q.x, r.x, 0.0f, p.y, q.y, r.y, 0.0f, p.z, q.z, r.z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructXYPlaneProjectionMatrix() {
        return constructArbitraryScaleMatrix(new Vector3f(0.0f, 0.0f, 1.0f), 0.0f);
    }

    public static Matrix4f constructXZPlaneProjectionMatrix() {
        return constructArbitraryScaleMatrix(new Vector3f(0.0f, 1.0f, 0.0f), 0.0f);
    }

    public static Matrix4f constructYZPlaneProjectionMatrix() {
        return constructArbitraryScaleMatrix(new Vector3f(1.0f, 0.0f, 0.0f), 0.0f);
    }

    public static Matrix4f constructArbitraryPlaneProjectionMatrix(Vector3f normal) {
        return constructArbitraryScaleMatrix(normal, 0.0f);
    }

    public static Matrix4f constructArbitraryReflectionMatrix(Vector3f normal) {
        return constructArbitraryScaleMatrix(normal, -1.0f);
    }

    public static Matrix4f constructXYShearMatrix(float s, float t) {
        return new Matrix4f(1.0f, 0.0f, s, 0.0f, 0.0f, 1.0f, t, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructXZShearMatrix(float s, float t) {
        return new Matrix4f(1.0f, s, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, t, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4f constructYZShearMatrix(float s, float t) {
        return new Matrix4f(1.0f, 0.0f, 0.0f, 0.0f, s, 1.0f, 0.0f, 0.0f, t, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public Matrix4f() {

        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m14 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m24 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
        this.m34 = 0.0f;
        this.m41 = 0.0f;
        this.m42 = 0.0f;
        this.m43 = 0.0f;
        this.m44 = 1.0f;
    }

    public Matrix4f(float m11, float m12, float m13, float m14, float m21, float m22, float m23, float m24, float m31,
            float m32, float m33, float m34, float m41, float m42, float m43, float m44) {

        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m14 = m14;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m24 = m24;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
        this.m34 = m34;
        this.m41 = m41;
        this.m42 = m42;
        this.m43 = m43;
        this.m44 = m44;
    }

    public Matrix4f(float[] matrix) {

        this.m11 = matrix[0];
        this.m12 = matrix[1];
        this.m13 = matrix[2];
        this.m14 = matrix[3];
        this.m21 = matrix[4];
        this.m22 = matrix[5];
        this.m23 = matrix[6];
        this.m24 = matrix[7];
        this.m31 = matrix[8];
        this.m32 = matrix[9];
        this.m33 = matrix[10];
        this.m34 = matrix[11];
        this.m41 = matrix[12];
        this.m42 = matrix[13];
        this.m43 = matrix[14];
        this.m44 = matrix[15];
    }

    public Matrix4f(Matrix4f matrix) {

        this.m11 = matrix.m11;
        this.m12 = matrix.m12;
        this.m13 = matrix.m13;
        this.m14 = matrix.m14;
        this.m21 = matrix.m21;
        this.m22 = matrix.m22;
        this.m23 = matrix.m23;
        this.m24 = matrix.m24;
        this.m31 = matrix.m31;
        this.m32 = matrix.m32;
        this.m33 = matrix.m33;
        this.m34 = matrix.m34;
        this.m41 = matrix.m41;
        this.m42 = matrix.m42;
        this.m43 = matrix.m43;
        this.m44 = matrix.m44;
    }

    public Matrix4f transpose() {

        return new Matrix4f(this.m11, this.m21, this.m31, this.m41, this.m12, this.m22, this.m32, this.m42, this.m13,
                this.m23, this.m33, this.m43, this.m14, this.m24, this.m34, this.m44);
    }

    public Matrix4f multiply(float scalar) {

        return new Matrix4f(scalar * this.m11, scalar * this.m12, scalar * this.m13, scalar * this.m14,
                scalar * this.m21, scalar * this.m22, scalar * this.m23, scalar * this.m24, scalar * this.m31,
                scalar * this.m32, scalar * this.m33, scalar * this.m34, scalar * this.m41, scalar * this.m42,
                scalar * this.m43, scalar * this.m44);
    }

    public Matrix4f multiply(Matrix4f matrix) {

        Matrix4f result = new Matrix4f();

        result.m11 = this.m11 * matrix.m11 + this.m12 * matrix.m21 + this.m13 * matrix.m31 + this.m14 * matrix.m41;
        result.m21 = this.m21 * matrix.m11 + this.m22 * matrix.m21 + this.m23 * matrix.m31 + this.m24 * matrix.m41;
        result.m31 = this.m31 * matrix.m11 + this.m32 * matrix.m21 + this.m33 * matrix.m31 + this.m34 * matrix.m41;
        result.m41 = this.m41 * matrix.m11 + this.m42 * matrix.m21 + this.m43 * matrix.m31 + this.m44 * matrix.m41;

        result.m12 = this.m11 * matrix.m12 + this.m12 * matrix.m22 + this.m13 * matrix.m32 + this.m14 * matrix.m42;
        result.m22 = this.m21 * matrix.m12 + this.m22 * matrix.m22 + this.m23 * matrix.m32 + this.m24 * matrix.m42;
        result.m32 = this.m31 * matrix.m12 + this.m32 * matrix.m22 + this.m33 * matrix.m32 + this.m34 * matrix.m42;
        result.m42 = this.m41 * matrix.m12 + this.m42 * matrix.m22 + this.m43 * matrix.m32 + this.m44 * matrix.m42;

        result.m13 = this.m11 * matrix.m13 + this.m12 * matrix.m23 + this.m13 * matrix.m33 + this.m14 * matrix.m43;
        result.m23 = this.m21 * matrix.m13 + this.m22 * matrix.m23 + this.m23 * matrix.m33 + this.m24 * matrix.m43;
        result.m33 = this.m31 * matrix.m13 + this.m32 * matrix.m23 + this.m33 * matrix.m33 + this.m34 * matrix.m43;
        result.m43 = this.m41 * matrix.m13 + this.m42 * matrix.m23 + this.m43 * matrix.m33 + this.m44 * matrix.m43;

        result.m14 = this.m11 * matrix.m14 + this.m12 * matrix.m24 + this.m13 * matrix.m34 + this.m14 * matrix.m44;
        result.m24 = this.m21 * matrix.m14 + this.m22 * matrix.m24 + this.m23 * matrix.m34 + this.m24 * matrix.m44;
        result.m34 = this.m31 * matrix.m14 + this.m32 * matrix.m24 + this.m33 * matrix.m34 + this.m34 * matrix.m44;
        result.m44 = this.m41 * matrix.m14 + this.m42 * matrix.m24 + this.m43 * matrix.m34 + this.m44 * matrix.m44;

        return result;
    }

    public Vector4f multiply(Vector4f vector) {

        return new Vector4f(this.m11 * vector.x + this.m12 * vector.y + this.m13 * vector.z + this.m14 * vector.w,
                this.m21 * vector.x + this.m22 * vector.y + this.m23 * vector.z + this.m24 * vector.w,
                this.m31 * vector.x + this.m32 * vector.y + this.m33 * vector.z + this.m34 * vector.w,
                this.m41 * vector.x + this.m42 * vector.y + this.m43 * vector.z + this.m44 * vector.w);
    }

    public Vector3f multiply(Vector3f vector) {

        return new Vector3f(this.m11 * vector.x + this.m12 * vector.y + this.m13 * vector.z,
                this.m21 * vector.x + this.m22 * vector.y + this.m23 * vector.z,
                this.m31 * vector.x + this.m32 * vector.y + this.m33 * vector.z);
    }

    public void apply(GL2 gl) {
        float[] matrix = new float[] { m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44 };

        gl.glMultTransposeMatrixf(matrix, 0);
    }

    public Matrix4f inverse() {
        return new Matrix4f();
    }

    @Override
    public String toString() {
        return super.toString() + "\n[" + this.m11 + "," + this.m12 + "," + this.m13 + "," + this.m14 + ",\n" + this.m21
                + "," + this.m22 + "," + this.m23 + "," + this.m24 + ",\n" + this.m31 + "," + this.m32 + "," + this.m33
                + "," + this.m34 + ",\n" + this.m41 + "," + this.m42 + "," + this.m43 + "," + this.m44 + "]";
    }

    public float[] toArray() {
        float[] tmpArray = new float[] {

                m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44 };

        return tmpArray;
    }

}