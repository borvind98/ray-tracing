package com.company;

class HitRecord {

    double t = 0;
    Vec3 p = new Vec3(0, 0, 0);
    Vec3 normal = new Vec3(0, 0, 0);
    boolean frontFace;
    Material material;

    void setFaceNormal(Ray r, Vec3 outwardNormal){
        frontFace = (Vec3.dot(r.direction(), outwardNormal) < 0);
        if(frontFace){
            normal = outwardNormal;
        }
        else{
            normal = Vec3.vec_minus(new Vec3(0,0,0), outwardNormal);
        }
    }

}

public abstract class Hitable {

    boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        return true;
    }
}
