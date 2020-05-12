package com.company;

public class Ray {

    Vec3 A, B;

    Ray(Vec3 a, Vec3 b) {
        this.A = a;
        this.B = b;
    }

    Vec3 origin() {
        return A;
    }

    Vec3 direction() {
        return B;
    }

    Vec3 point_at_parameter(float t) {
        return Vec3.vec_plus(A, Vec3.vec_mul_t(B, t));
    }

    void set(Vec3 a, Vec3 b){
        this.A = a;
        this.B = b;
    }
}
