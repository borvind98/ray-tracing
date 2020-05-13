package com.company;

public class Camera {

    Vec3 lower_left_corner;
    Vec3 horizontal;
    Vec3 vertical;
    Vec3 origin;
    Vec3 u, v, w;
    double lensRadius;

    Camera(double vfov, double aspectRatio, Vec3 lookFrom, Vec3 lookAt, Vec3 vUp, double focusDist, double aperture){
        this.origin = lookFrom;

        this.lensRadius = aperture/2;

        double theta = Extra.degreesToRadians(vfov);
        double halfHeight = Math.tan(theta/2);
        double halfWidth = aspectRatio * halfHeight;


        w = Vec3.unit_vector(lookFrom.vecMinus(lookAt));
        u = Vec3.unit_vector(vUp.cross(w));
        v = Vec3.cross(w, u);

        lower_left_corner = origin.vecMinus(u.mul_t(halfWidth*focusDist)).vecMinus(v.mul_t(halfHeight*focusDist)).vecMinus(w.mul_t(focusDist));

        horizontal = u.vecMulT(2*halfWidth*focusDist);
        vertical = v.vecMulT(2*halfWidth*focusDist);

    }

    Ray getRay(double s, double t){
        Vec3 rd = Vec3.randomInUnitDisk().vecMulT(lensRadius);
        Vec3 offset = u.vecMulT(rd.x()).vecPlus(v.vecMulT(rd.y()));
        Vec3 vec = lower_left_corner.vecPlus(horizontal.vecMulT(s)).vecPlus(vertical.vecMulT(t)).vecMinus(origin).vecMinus(offset);
        return new Ray(origin.vecPlus(offset), vec);
    }
}
