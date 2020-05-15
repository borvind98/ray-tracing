package com.company;

public class Sphere extends Hittable {

    Vec3 center;
    double radius;
    Material material;

    Sphere(Vec3 cen, double r, Material m) {
        this.center = cen;
        this.radius = r;
        this.material = m;
    }
    @Override
    boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        Vec3 oc = Vec3.vec_minus(r.origin(), center);
        Vec3 dir = r.direction();
        double a = dir.dot(dir);
        double b = oc.dot(dir);
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - a * c;
        if (discriminant > 0) {
            double temp = ((-b - Math.sqrt(discriminant))/a);
            if (temp < t_max && temp > t_min) {
                rec.t = temp;
                rec.p = r.point_at_parameter(rec.t);
                Vec3 outwardNormal = Vec3.vec_div_t(Vec3.vec_minus(rec.p, center), radius);
                rec.setFaceNormal(r, outwardNormal);
                rec.material = material;
                return true;
            }
            temp = ((-b + Math.sqrt(discriminant))/a);
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

    @Override
    boolean boundingBox(double t0, double t1, Aabb outputBox) {
        outputBox.set(center.vecMinus(new Vec3(radius, radius, radius)),
                center.vecPlus(new Vec3(radius, radius, radius)));
        return true;
    }
}

