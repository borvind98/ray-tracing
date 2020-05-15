package com.company;

public class Ray {

    Vec3 origin, dir;
    double time;

    Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.dir = direction;
        this.time = 0.0;
    }

    Ray(Vec3 origin, Vec3 direction, double time) {
        this.origin = origin;
        this.dir = direction;
        this.time = time;
    }

    Vec3 origin() {
        return origin;
    }

    Vec3 direction() {
        return dir;
    }

    double time(){
        return time;
    }

    Vec3 point_at_parameter(double t) {
        return Vec3.vec_plus(origin, Vec3.vec_mul_t(dir, t));
    }

    void set(Vec3 a, Vec3 b){
        this.origin = a;
        this.dir = b;
    }

    void set(Vec3 a, Vec3 b, double time){
        this.origin = a;
        this.dir = b;
        this.time = time;
    }
}
