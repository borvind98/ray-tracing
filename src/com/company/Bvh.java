package com.company;

import java.util.ArrayList;

public class Bvh extends Hittable {

    Hittable left;
    Hittable right;
    Aabb box;

    Bvh(HittableList list, double time0, double time1){
        new Bvh(list.list, 0, list.list.size(), time0, time1);
    }
    Bvh(ArrayList<Hittable> objectList, int start, int end, double time0, double time1){
        int axis = RandomNumGen.randomInt(0,2);
        boolean comparator;


        int objectSpan = end - start;

        if(objectSpan == 1){
            left = right = objectList.get(start);
        }
        else if(objectSpan == 2){
            comparator = compare(objectList.get(start), objectList.get(start+1), axis);
            if(comparator){
                left = objectList.get(start);
                right = objectList.get(start+1);
            }
            else{
                left = objectList.get(start+1);
                right = objectList.get(start);
            }
        }
        else {
            //sort(objectList. + start, objectList.begin() +  end, comparator);
            int mid = start + objectSpan / 2;
            left = new Bvh(objectList, start, mid, time0, time1);
            right = new Bvh(objectList, mid, end, time0, time1);
        }
        Vec3 vec = new Vec3(0,0,0);
        Aabb boxLeft = new Aabb(vec, vec);
        Aabb boxRight = new Aabb(vec, vec);
        if(!left.boundingBox(time0, time1, boxLeft) || !right.boundingBox(time0, time1, boxRight)){
            System.out.println("No bounding box in Bvh constructor");
        }
        box = Aabb.surroundingBox(boxLeft, boxRight);

    }

    boolean hit(Ray r, double tMin, double tMax, HitRecord rec){
        if(!box.hit(r, tMin, tMax)){
            return false;
        }
        boolean hitLeft = this.left.hit(r, tMin, tMax, rec);
        boolean hitRight;
        if(hitLeft){
            hitRight = this.right.hit(r, tMin, rec.t, rec);
        }
        else{
            hitRight = this.right.hit(r, tMin, tMax, rec);
        }
        return (hitLeft || hitRight);
    }
    boolean boundingBox(double t0, double t1, Aabb outputBox){
        outputBox.set(this.box);
        return true;
    }

    boolean compare(Hittable a, Hittable b, int axis){
        if(axis == 0){
            return boxXCompare(left, right);
        }
        else if(axis == 1){
            return boxYCompare(left, right);
        }
        else{
            return boxZCompare(left, right);
        }
    }


    boolean boxCompare(Hittable a, Hittable b, int axis){
        Aabb boxA = new Aabb(new Vec3(0,0,0), new Vec3(0,0,0));
        Aabb boxB = new Aabb(new Vec3(0,0,0), new Vec3(0,0,0));

        if(!a.boundingBox(0,0,boxA) || !b.boundingBox(0,0,boxB)){
            System.out.println("No bounding box in Bvh constructor");
        }

        return boxA.min().get(axis) < boxB.min().get(axis);
    }

    boolean boxXCompare(Hittable a, Hittable b){
        return boxCompare(a, b, 0);
    }
    boolean boxYCompare(Hittable a, Hittable b){
        return boxCompare(a, b, 1);
    }
    boolean boxZCompare(Hittable a, Hittable b){
        return boxCompare(a, b, 2);
    }



}
