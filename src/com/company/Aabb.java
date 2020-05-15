package com.company;

public class Aabb {

    Vec3 min, max;

    Aabb(Vec3 a, Vec3 b){
        this.min = a;
        this.max = b;
    }

    Vec3 min(){
        return this.min;
    }

    Vec3 max(){
        return this.max;
    }

    void set(Vec3 a, Vec3 b){
        this.min = a;
        this.max = b;
    }

    void set(Aabb a){
        this.min = a.min();
        this.max = a.max();
    }

    boolean hit(Ray r, double tMin, double tMax){
        Vec3 origin = r.origin();
        Vec3 direction = r.direction();
        for (int a = 0; a < 3; a++) {
            double invD = 1.0 /direction.get(a);
            double t0 = (this.min.get(a) - origin.get(a))*invD;
            double t1 = (this.max.get(a) - origin.get(a))*invD;
            if(invD < 0.0){
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }
            if(t0 > tMin){
                tMin = t0;
            }
            if(t1 < tMax){
                tMax = t1;
            }
            if(tMax <= tMin){
                return false;
            }
        }
        return true;
    }

    static Aabb surroundingBox(Aabb box0, Aabb box1){
        Vec3 small = new Vec3(Math.min(box0.min().x(), box1.min().x()),
                                Math.min(box0.min().y(), box1.min().y()),
                                Math.min(box0.min().z(), box1.min().z()));
        Vec3 big = new Vec3(Math.max(box0.max().x(), box1.max().x()),
                            Math.max(box0.max().y(), box1.max().y()),
                            Math.max(box0.max().z(), box1.max().z()));
        return new Aabb(small, big);
    }



}
