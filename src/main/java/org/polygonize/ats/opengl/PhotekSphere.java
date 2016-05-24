package org.polygonize.ats.opengl;

import org.polygonize.ats.util.Vector3D;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class PhotekSphere {

    public PhotekSphere() {
    }

    static public Vector3D computePoint(float theta, float phi) {
        Vector3D point = new Vector3D();

        point.x = (float) -(Math.cos(Math.toRadians(theta)) * Math.cos(Math.toRadians(phi)));
        point.y = (float) (Math.sin(Math.toRadians(theta)));
        point.z = (float) (Math.cos(Math.toRadians(theta)) * Math.sin(Math.toRadians(phi)));

        return point;

    }

    static public Vector3D SphereMap(float x, float y, float z, float radius) {
        Vector3D vect = new Vector3D();
        vect.y = (float) (Math.acos(z / radius) / Math.PI);
        if (y >= 0)
            vect.x = (float) (Math.acos(x / (radius * Math.sin(Math.PI * (vect.y)))) / 2 * Math.PI);
        else
            vect.x = (float) ((Math.PI + Math.acos(x / (radius * Math.sin(Math.PI * (vect.y))))) / 2 * Math.PI);
        return vect;
    }

    public void CreateSphere(GL2 gl, Vector3D c, double r, int n) {
        int i, j;
        double theta1, theta2, theta3;
        Vector3D e = new Vector3D(), p = new Vector3D();

        if (r < 0)
            r = -r;
        if (n < 0)
            n = -n;
        if (n < 4 || r <= 0) {
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3d(c.x, c.y, c.z);
            gl.glEnd();
            return;
        }

        double PID2 = Math.PI / 2;

        for (j = 0; j < n / 2; j++) {
            theta1 = j * Math.PI * 2 / n - PID2;
            theta2 = (j + 1) * Math.PI * 2 / n - PID2;

            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (i = 0; i <= n; i++) {
                theta3 = i * Math.PI * 2 / n;

                e.x = Math.cos(theta2) * Math.cos(theta3);
                e.y = Math.sin(theta2);
                e.z = Math.cos(theta2) * Math.sin(theta3);
                p.x = c.x + r * e.x;
                p.y = c.y + r * e.y;
                p.z = c.z + r * e.z;

                gl.glNormal3d(e.x, e.y, e.z);
                gl.glTexCoord2d(i / (double) n * 2, 2 * (j + 1) / (double) n);
                gl.glVertex3d(p.x, p.y, p.z);

                e.x = Math.cos(theta1) * Math.cos(theta3);
                e.y = Math.sin(theta1);
                e.z = Math.cos(theta1) * Math.sin(theta3);
                p.x = c.x + r * e.x;
                p.y = c.y + r * e.y;
                p.z = c.z + r * e.z;

                gl.glNormal3d(e.x, e.y, e.z);
                gl.glTexCoord2d(i / (double) n * 2, 2 * j / (double) n);
                gl.glVertex3d(p.x, p.y, p.z);
            }
            gl.glEnd();
        }
    }

    static public void draw(GL2 gl) {
        // x = cos(theta) cos(phi)
        // y = cos(theta) sin(phi)
        // z = sin(theta)
        // -90 <= theta <= 90
        // 0 <= phi <= 360
        // (theta,phi)
        // (theta+dtheta,phi)
        // (theta+dtheta,phi+dphi)
        // (theta,phi+dphi)
        // gl.glShadeModel(GL.GL_FLAT);
        gl.glPointSize(1.f);

        // gl.glColor3f(1,0,0);
        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glBegin(GL2.GL_QUADS);
        for (int theta = -90; theta < 90; theta += 5)
            for (int phi = 0; phi < 360; phi += 5) {
                Vector3D p1 = computePoint(theta, phi);
                Vector3D p2 = computePoint(theta, (phi + 5) % 360);
                Vector3D p3 = computePoint((theta + 5), (phi + 5) % 360);
                Vector3D p4 = computePoint(theta + 5, phi);
                Vector3D tex;

                gl.glNormal3d(p1.x, p1.y, p1.z);
                gl.glTexCoord2f((phi / 360.f) * 6, (theta + 90) / 180.f * 4);
                // gl.glTexCoord2f((phi/360.f)*4, (theta+90)/180.f*4);
                gl.glVertex3d(p1.x, p1.y, p1.z);

                gl.glNormal3d(p2.x, p2.y, p2.z);
                gl.glTexCoord2f(((phi + 5) / 360.f) * 6, (theta + 90) / 180.f * 4);
                gl.glVertex3d(p2.x, p2.y, p2.z);

                gl.glNormal3d(p3.x, p3.y, p3.z);
                gl.glTexCoord2f(((phi + 5) / 360.f) * 6, (theta + 90 + 5) / 180.f * 4);
                gl.glVertex3d(p3.x, p3.y, p3.z);

                gl.glNormal3d(p4.x, p4.y, p4.z);
                gl.glTexCoord2f((phi / 360.f) * 6, (theta + 90 + 5) / 180.f * 4);
                gl.glVertex3d(p4.x, p4.y, p4.z);

            }
        gl.glEnd();
    }
}
