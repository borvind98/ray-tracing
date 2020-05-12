package com.company;

public class HitableList extends Hitable {

    Hitable[] list;

    HitableList(Hitable[] l) {
        this.list = l;
    }

    boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        boolean hit_anything = false;
        double closest_so_far = t_max;
        for (int i = 0; i < list.length; i++) {
            if (list[i].hit(r, t_min, closest_so_far, rec)) {
                hit_anything = true;
                closest_so_far = rec.t;
            }
        }
        return hit_anything;
    }
}
