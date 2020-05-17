package com.company;

public class Camera {

    Vec3 lower_left_corner;
    Vec3 horizontal;
    Vec3 vertical;
    Vec3 origin;
    Vec3 u, v, w;
    double lensRadius, time0, time1;

    Camera(double vfov, double aspectRatio, Vec3 lookFrom, Vec3 lookAt, Vec3 vUp, double focusDist, double aperture, double t0, double t1){
        this.origin = lookFrom;
        this.time0 = t0;
        this.time1 = t1;
        this.lensRadius = aperture/2;

        double theta = Extra.degreesToRadians(vfov);
        double halfHeight = Math.tan(theta/2);
        double halfWidth = aspectRatio * halfHeight;


        w = Vec3.unitVector(lookFrom.vecMinus(lookAt));
        u = Vec3.unitVector(vUp.cross(w));
        v = w.cross(u);

        lower_left_corner = origin.vecMinus(u.vecMulT(halfWidth*focusDist)).vecMinus(v.vecMulT(halfHeight*focusDist)).vecMinus(w.vecMulT(focusDist));

        horizontal = u.vecMulT(2*halfWidth*focusDist);
        vertical = v.vecMulT(2*halfHeight*focusDist);

    }

    Ray getRay(double s, double t){
        Vec3 rd = Vec3.randomInUnitDisk().vecMulT(lensRadius);
        Vec3 offset = u.vecMulT(rd.x()).vecPlus(v.vecMulT(rd.y()));
        Vec3 vec = lower_left_corner.vecPlus(horizontal.vecMulT(s)).vecPlus(vertical.vecMulT(t)).vecMinus(origin).vecMinus(offset);
        return new Ray(origin.vecPlus(offset), vec, RandomNumGen.randomDouble(time0, time1));
    }
}
