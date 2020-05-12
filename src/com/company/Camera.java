package com.company;

public class Camera {

    Vec3 lower_left_corner;
    Vec3 horizontal;
    Vec3 vertical;
    Vec3 origin;

    Camera(double aspectRatio){

        double horizontalWidth = 4.0f;
        double verticalHeigth = (horizontalWidth /aspectRatio);

        lower_left_corner = new Vec3(-2.0, -1.0, -1.0);
        horizontal = new Vec3(horizontalWidth, 0.0, 0.0);
        vertical = new Vec3(0.0, verticalHeigth, 0.0);
        origin = new Vec3(0.0, 0.0, 0.0);
    }

    Ray getRay(double u, double v){
        Vec3 vec = Vec3.vec_minus(Vec3.vec_plus(lower_left_corner, Vec3.vec_plus(Vec3.vec_mul_t(horizontal, u), Vec3.vec_mul_t(vertical, v))), origin);
        return new Ray(origin, vec);
    }
}
