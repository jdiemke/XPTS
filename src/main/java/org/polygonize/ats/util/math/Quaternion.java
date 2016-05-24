package org.polygonize.ats.util.math;

/**
 * 
 * q = w + xi + yj + zk
 * 
 * @author trigger
 * 
 * @see http://content.gpwiki.org/index.php/Quaternions
 * @see http://www.gamedev.net/page/resources/_/technical/math-and-physics/
 *      quaternion-powers-r1095
 * @see http://www.idevgames.com/articles/quaternions
 * @see http://www.geometrictools.com/Documentation/RotationIssues.pdf
 * @see http://www.j3d.org/matrix_faq/matrfaq_latest.html
 * @see http://www.cprogramming.com/tutorial/3d/quaternions.html
 * @see http://content.gpwiki.org/index.php/OpenGL:Tutorials:
 *      Using_Quaternions_to_represent_rotation
 * @see http://code.google.com/p/jmonkeyengine/source/browse/branches/jme3/src/
 *      core/com/jme3/math/Quaternion.java?r=6560
 * @see http://embots.dfki.de/doc/seminar_ca/ik1.pdf
 * @see http://www.arcsynthesis.org/gltut/Positioning/Tut08%20Quaternions.html
 * @see http://number-none.com/product/Understanding%20Slerp,%20Then%20Not%
 *      20Using%20It/
 *
 */
public class Quaternion {

    public float w;
    public float x;
    public float y;
    public float z;

    public final static Quaternion ADDITIVE_IDENTITY = new Quaternion(0.0f, 0.0f, 0.0f, 0.0f);
    public final static Quaternion MULTIPLICATIVE_IDENTITY = new Quaternion(1.0f, 0.0f, 0.0f, 0.0f);

    public Quaternion() {
        this.w = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(Quaternion quat) {
        this.w = quat.w;
        this.x = quat.x;
        this.y = quat.y;
        this.z = quat.z;
    }

    /**
     * implements the magnitude or norm of a quaternion
     * 
     * @return The norm of the quaternion
     */
    public float norm() {
        return (float) Math.sqrt(w * w + x * x + y * y + z * z);
    }

    public Quaternion multiply(float scalar) {
        return new Quaternion(scalar * w, scalar * x, scalar * y, scalar * z);
    }

    public Quaternion divide(float scalar) {
        float reciprocal = 1.0f / scalar;

        return this.multiply(reciprocal);
    }

    public Quaternion normalize() {
        return this.divide(this.norm());
    }

    public Quaternion multiply(Quaternion quat) {
        return new Quaternion(w * quat.w - x * quat.x - y * quat.y - z * quat.z,
                w * quat.x + x * quat.w + y * quat.z - z * quat.y, w * quat.y + y * quat.w + z * quat.x - x * quat.z,
                w * quat.z + z * quat.w + x * quat.y - y * quat.x);
    }

    /**
     * assumes a unit quaternion
     * 
     * @return
     */
    public Matrix4f toMatrix() {
        return new Matrix4f(1.0f - 2.0f * y * y - 2.0f * z * z, 2.0f * x * y - 2.0f * w * z,
                2.0f * x * z + 2.0f * w * y, 0.0f, 2.0f * x * y + 2.0f * w * z, 1.0f - 2.0f * x * x - 2.0f * z * z,
                2.0f * y * z - 2.0f * w * x, 0.0f, 2.0f * x * z - 2.0f * w * y, 2.0f * y * z + 2.0f * w * x,
                1.0f - 2.0f * x * x - 2.0f * y * y, 0.0f, 0, 0, 0, 1.0f);
    }

    public Vector3f getAxisOfRotation() {
        return new Vector3f(x, y, z).normalize();
    }

    public float getAngleOfRotation() {
        return (float) Math.toDegrees(2.0f * Math.acos(w));
    }

    public Quaternion(Vector3f axis, float angle) {
        float sin = (float) Math.sin(Math.toRadians(angle) / 2.0);
        float cos = (float) Math.cos(Math.toRadians(angle) / 2.0);

        this.w = cos;
        this.x = axis.x * sin;
        this.y = axis.y * sin;
        this.z = axis.z * sin;
    }

    public Quaternion conjugate() {
        return new Quaternion(w, -x, -y, -z);
    }

    public Quaternion inverse() {
        return conjugate().divide(norm() * norm());
    }

    public Quaternion(float pitch, float yaw, float roll) {
        Quaternion pitchQuat = new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), pitch);
        Quaternion yawQuat = new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), yaw);
        Quaternion rollQuat = new Quaternion(new Vector3f(0.0f, 0.0f, 1.0f), roll);

        Quaternion result = rollQuat.multiply(yawQuat.multiply(pitchQuat));
        w = result.w;
        x = result.x;
        y = result.y;
        z = result.z;
    }

    public Quaternion add(Quaternion quat) {
        return new Quaternion(w + quat.w, x + quat.x, y + quat.y, z + quat.z);
    }

    public Quaternion substract(Quaternion quat) {
        // TODO Auto-generated method stub
        return new Quaternion(w - quat.w, x - quat.x, y - quat.y, z - quat.z);
    }

    public Quaternion slerp(Quaternion quat, float interpolation) {
        float theta = (float) Math.acos(this.w * quat.w + this.x * quat.x + this.y * quat.y + this.z * quat.z);

        Quaternion term1 = this.multiply((float) Math.sin((1.0 - interpolation) * theta));
        Quaternion term2 = quat.multiply((float) Math.sin(interpolation * theta));

        return term1.add(term2).divide((float) Math.sin(theta));
    }

    public Quaternion slerp2(Quaternion qb, double t) {
        // quaternion to return
        Quaternion qm = new Quaternion();
        // Calculate angle between them.
        double cosHalfTheta = this.w * qb.w + this.x * qb.x + this.y * qb.y + this.z * qb.z;

        if (cosHalfTheta < 0) {
            qb.w = -qb.w;
            qb.x = -qb.x;
            qb.y = -qb.y;
            qb.z = qb.z;
            cosHalfTheta = -cosHalfTheta;
        }

        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if (Math.abs(cosHalfTheta) >= 1.0) {
            qm.w = this.w;
            qm.x = this.x;
            qm.y = this.y;
            qm.z = this.z;
            return qm;
        }
        // Calculate temporary values.
        double halfTheta = Math.acos(cosHalfTheta);
        double sinHalfTheta = Math.sqrt(1.0 - cosHalfTheta * cosHalfTheta);
        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if (Math.abs(sinHalfTheta) < 0.001) { // fabs is floating point absolute
            qm.w = (float) (this.w * 0.5f + qb.w * 0.5f);
            qm.x = (float) (this.x * 0.5f + qb.x * 0.5f);
            qm.y = (float) (this.y * 0.5f + qb.y * 0.5f);
            qm.z = (float) (this.z * 0.5f + qb.z * 0.5f);
            return qm;
        }
        double ratioA = Math.sin((1 - t) * halfTheta) / sinHalfTheta;
        double ratioB = Math.sin(t * halfTheta) / sinHalfTheta;
        // calculate Quaternion.
        qm.w = (float) (this.w * ratioA + qb.w * ratioB);
        qm.x = (float) (this.x * ratioA + qb.x * ratioB);
        qm.y = (float) (this.y * ratioA + qb.y * ratioB);
        qm.z = (float) (this.z * ratioA + qb.z * ratioB);
        return qm;
    }

    ///

    public Quaternion(Vector3f vec1, Vector3f vec2) {

    }

    public Quaternion(Matrix4f matrix) {
    }

    public Vector3f substract(Vector3f vector) {
        return new Vector3f(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    @Override
    public String toString() {
        return super.toString() + "[" + this.w + "," + this.x + "," + this.y + "," + this.z + "]";
    }

    public static void main(String[] args) {
        System.out.println("Quaternionen Testumgebung");
        System.out.println("-------------------------");

        Quaternion identity = new Quaternion(0, 50, 50, 50);
        System.out.println(identity.norm());
        System.out.println(identity.normalize().norm());
    }

}
