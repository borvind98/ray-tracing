package com.company;

import java.util.ArrayList;

public class HittableList extends Hittable {

    ArrayList<Hittable> list;

    HittableList(ArrayList<Hittable> l) {
        this.list = l;
    }

    boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        boolean hit_anything = false;
        double closest_so_far = t_max;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).hit(r, t_min, closest_so_far, rec)) {
                hit_anything = true;
                closest_so_far = rec.t;
            }

        }
        return hit_anything;
    }

    @Override
    boolean boundingBox(double t0, double t1, Aabb outputBox) {
        if(list.size() == 0) return false;

        Aabb tempBox = new Aabb(new Vec3(0,0,0), new Vec3(0,0,0));
        boolean firstBox = true;

        for (Hittable h: list) {
            if(!h.boundingBox(t0, t1, tempBox)) return false;
            if(firstBox){
                outputBox.set(tempBox);
            }
            else{
                outputBox.set(Aabb.surroundingBox(outputBox, tempBox));
            }
            firstBox = false;
        }
        return true;
    }
}
