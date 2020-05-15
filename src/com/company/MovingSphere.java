package com.company;

public class MovingSphere extends Hittable {

    Vec3 center0, center1;
    double time0, time1, radius;
    Material material;

    MovingSphere(Vec3 cen0, Vec3 cen1, double t0, double t1, double r, Material m){
        this.center0 = cen0;
        this.center1 = cen1;
        this.time0 = t0;
        this.time1 = t1;
        this.radius = r;
        this.material = m;
    }

    boolean hit(Ray r, double tMin, double tMax, HitRecord rec){
        Vec3 oc = r.origin().vecMinusT(r.time());
        double a = r.direction().squared_length();
        double halfB = oc.dot(r.direction());
        double c = oc.squared_length() - radius*radius;

        double discriminant = halfB * halfB - a * c;
        if (discriminant > 0) {
            double root = Math.sqrt(discriminant);
            double temp = ((-halfB - root)/a);
            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = r.point_at_parameter(rec.t);
                Vec3 outwardNormal = Vec3.vec_div_t(Vec3.vec_minus(rec.p, center(r.time())), radius);
                rec.setFaceNormal(r, outwardNormal);
                rec.material = material;
                return true;
            }
            temp = ((-halfB + root)/a);
            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = r.point_at_parameter(rec.t);
                Vec3 outwardNormal = Vec3.vec_div_t(Vec3.vec_minus(rec.p, center(r.time())), radius);
                rec.setFaceNormal(r, outwardNormal);
                rec.material = material;
                return true;
            }
        }
        return false;
    }

    Vec3 center(double time){
        return (center1.vecMinus(center0).vecMulT((time - time0)/(time1 - time0)).vecPlus(center0));
    }
}
