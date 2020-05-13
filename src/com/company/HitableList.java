package com.company;

import java.util.ArrayList;

public class HitableList extends Hitable {

    ArrayList<Hitable> list;

    HitableList(ArrayList<Hitable> l) {
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
}
