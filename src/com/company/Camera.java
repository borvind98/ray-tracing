package com.company;

public class Camera {

    Vec3 lower_left_corner;
    Vec3 horizontal;
    Vec3 vertical;
    Vec3 origin;

    Camera(double aspectRatio){

        float horizontalWidth = 4.0f;
        float verticalHeigth = (float) (horizontalWidth /aspectRatio);

        lower_left_corner = new Vec3(-2.0f, -1.0f, -1.0f);
        horizontal = new Vec3(horizontalWidth, 0.0f, 0.0f);
        vertical = new Vec3(0.0f, verticalHeigth, 0.0f);
        origin = new Vec3(0.0f, 0.0f, 0.0f);
    }

    Ray getRay(double u, double v){
        Vec3 vec = Vec3.vec_minus(Vec3.vec_plus(lower_left_corner, Vec3.vec_plus(Vec3.vec_mul_t(horizontal, (float)u), Vec3.vec_mul_t(vertical, (float)v))), origin);
        return new Ray(origin, vec);
    }
}
