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


        w = Vec3.unit_vector(Vec3.vec_minus(lookFrom, lookAt));
        u = Vec3.unit_vector(Vec3.cross(vUp, w));
        v = Vec3.cross(w, u);

        lower_left_corner = Vec3.vec_minus(origin, Vec3.vec_minus(Vec3.vec_mul_t(u, halfWidth), Vec3.vec_minus(Vec3.vec_mul_t(v, halfHeight), w)));

        horizontal = Vec3.vec_mul_t(u, 2*halfWidth*focusDist);
        vertical = Vec3.vec_mul_t(v, 2*halfWidth*focusDist);

    }

    Ray getRay(double s, double t){
        Vec3 rd = Vec3.vec_mul_t(Vec3.randomInUnitDisk(), lensRadius);
        Vec3 offset = Vec3.vec_plus(Vec3.vec_mul_t(u, rd.x()), Vec3.vec_mul_t(v, rd.y()));

        Vec3 vec = Vec3.vec_minus(Vec3.vec_minus(Vec3.vec_plus(lower_left_corner, Vec3.vec_plus(Vec3.vec_mul_t(horizontal, s), Vec3.vec_mul_t(vertical, t))), origin), offset);
        return new Ray(Vec3.vec_plus(origin, offset), vec);
    }
}
