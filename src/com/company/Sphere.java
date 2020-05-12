package com.company;

public class Sphere extends Hitable {

    Vec3 center;
    float radius;
    Material material;

    Sphere(Vec3 cen, float r, Material m) {
        this.center = cen;
        this.radius = r;
        this.material = m;
    }

    boolean hit(Ray r, float t_min, float t_max, HitRecord rec) {
        Vec3 oc = Vec3.vec_minus(r.origin(), center);
        float a = Vec3.dot(r.direction(), r.direction());
        float b = Vec3.dot(oc, r.direction());
        float c = Vec3.dot(oc, oc) - radius * radius;
        float discriminant = b * b - a * c;
        if (discriminant > 0) {
            float temp = (float) ((-b - Math.sqrt(discriminant))/a);
            if (temp < t_max && temp > t_min) {
                rec.t = temp;
                rec.p = r.point_at_parameter(rec.t);
                Vec3 outwardNormal = Vec3.vec_div_t(Vec3.vec_minus(rec.p, center), radius);
                rec.setFaceNormal(r, outwardNormal);
                rec.material = material;
                return true;
            }
            temp = (float) ((-b + Math.sqrt(discriminant))/a);
            if (temp < t_max && temp > t_min) {
                rec.t = temp;
                rec.p = r.point_at_parameter(rec.t);
                Vec3 outwardNormal = Vec3.vec_div_t(Vec3.vec_minus(rec.p, center), radius);
                rec.setFaceNormal(r, outwardNormal);
                rec.material = material;
                return true;
            }
        }
        return false;
    }
}

