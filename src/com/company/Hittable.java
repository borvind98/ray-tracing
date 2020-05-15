package com.company;

class HitRecord {

    double t = 0;
    double u, v;
    Vec3 p = new Vec3(0, 0, 0);
    Vec3 normal = new Vec3(0, 0, 0);
    boolean frontFace;
    Material material;

    void setFaceNormal(Ray r, Vec3 outwardNormal){
        frontFace = (r.direction().dot(outwardNormal) < 0);
        if(frontFace){
            normal = outwardNormal;
        }
        else{
            normal = Vec3.vec_minus(new Vec3(0,0,0), outwardNormal);
        }
    }

}

public abstract class Hittable {

    boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        return true;
    }
    boolean boundingBox(double t0, double t1, Aabb outputBox){return true;}
}
