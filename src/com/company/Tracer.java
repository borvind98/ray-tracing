package com.company;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Tracer {

    int cores = Runtime.getRuntime().availableProcessors();
    ArrayList<Hitable> list;
    Hitable world;
    Camera cam;
    Color color;
    int multiSample, imgHeight, imgWidth, maxDepth;


    Tracer(Camera camera, int imgWidth, int imgHeight, int msaa, int maxDepth) {
        this.cam = camera;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        this.multiSample = msaa;
        this.maxDepth = maxDepth;

        color = new Color(imgWidth, imgHeight);
        int hitWidth = 12;
        generateWorld(hitWidth);

    }

    void generateWorld(int hitWidth){

        list = new ArrayList<>();
        list.add(new Sphere(new Vec3(0, 1, -1), 1, new Dielectrics(1.5)));
        list.add(new Sphere(new Vec3(0, -100000, 0), 100000, new Lambertian(new Vec3(0.5, 0.5, 0.5))));
        list.add(new Sphere(new Vec3(2, 0, -1), 1, new Metal(new Vec3(0.8, 0.6, 0.2), 1.0)));
        list.add(new Sphere(new Vec3(-2, 0, -1), 1, new Metal(new Vec3(0.8, 0.8, 0.8), 0.3)));

        for (int j = -hitWidth; j < hitWidth; j++) {
            for (int k = -hitWidth; k < hitWidth; k++) {
                double matChooser = RandomNumGen.random_double();
                Vec3 center = new Vec3(j+0.9* RandomNumGen.random_double(), 0.2, k+0.9*RandomNumGen.random_double());
                if(Vec3.vec_minus(center, new Vec3(4, 0.2, 0)).length() > 0.9){
                    if(matChooser < 0.8){
                        //diffuse
                        Vec3 albedo = Vec3.vec_mul(Vec3.makeRandomVec(), Vec3.makeRandomVec());
                        list.add(new Sphere(center, 0.2, new Lambertian(albedo)));
                    }
                    else if(matChooser < 0.95){
                        //metal
                        Vec3 albedo = Vec3.makeRandomVecWithMinMax(0.5, 1);
                        double fuzz = RandomNumGen.random_double_within_interval(0, 0.5);
                        list.add(new Sphere(center, 0.2, new Metal(albedo, fuzz)));
                    }
                    else{
                        //glass
                        list.add(new Sphere(center, 0.2, new Dielectrics(1.5)));
                    }
                }


            }
        }
        this.world = new HitableList(list);
    }

    void initParallel() {
        int numOfCores = cores;

        Thread[] threads = new Thread[numOfCores];
        CyclicBarrier barrier = new CyclicBarrier(numOfCores);
        int partToSplit = imgWidth / numOfCores;
        int rest = imgWidth % numOfCores;
        for (int i = 0; i < numOfCores; i++) {
            if (i == numOfCores - 1) {
                int start = (i * partToSplit);
                int end = imgWidth;
                Thread t = new Thread(new Para(i, start, end, barrier, numOfCores));
                threads[i] = t;
                t.start();
            } else {
                int start = (i * partToSplit);
                int end = ((i + 1) * partToSplit);
                Thread t = new Thread(new Para(i, start, end, barrier, numOfCores));
                threads[i] = t;
                t.start();
            }
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (Exception e) {
            return;
        }

        color.writeColors();
    }

    void trace(int start, int end, int ind) {
        for (int j = 0; j < imgHeight; j++) {
            int remaining = imgHeight - j;
            System.out.println("Thread: " + ind + " | Scanlines remaining: " + remaining);
            for (int i = start; i < end; i++) {
                Vec3 pixel_col = new Vec3(0, 0, 0);
                for (int s = 0; s < multiSample; s++) {
                    double u;
                    double v;
                    if (multiSample == 1) {
                        u =  (double) (i) / (imgWidth - 1);
                        v =  (double) (j) / (imgHeight - 1);
                    } else {
                        u =  (i + RandomNumGen.random_double()) / (imgWidth - 1);
                        v =  (j + RandomNumGen.random_double()) / (imgHeight - 1);
                    }

                    Ray r = cam.getRay(u, v);
                    pixel_col.add(ray_color(r, this.world, maxDepth));
                }

                color.setColor(pixel_col, multiSample, j, i);
            }
        }
    }

    static Vec3 ray_color(Ray r, Hitable world, int depth) {
        HitRecord rec = new HitRecord();
        if (depth <= 0) {
            return new Vec3(0, 0, 0);
        }

        if (world.hit(r, 0.001f, Double.MAX_VALUE, rec)) {
            Ray scattered = new Ray(new Vec3(0, 0, 0), new Vec3(0, 0, 0));
            Vec3 attenuation = new Vec3(0, 0, 0);
            if (rec.material.scatter(r, rec, attenuation, scattered)) {
                return attenuation.vecMul(ray_color(scattered, world, depth-1));
            }
            return new Vec3(0, 0, 0);
        } else {
            Vec3 unit_direction = Vec3.unit_vector(r.direction());
            double t = 0.5 * (unit_direction.y() + 1.0);
            Vec3 v1 = new Vec3(1.0, 1.0, 1.0);
            Vec3 v2 = new Vec3(0.5, 0.7, 1.0);

            return v1.vecMulT(1-t).vecPlus(v2.vecMulT(t));
        }
    }

    private class Para implements Runnable {

        int ind, start, end, numOfCores;
        CyclicBarrier barrier;


        Para(int ind, int start, int end, CyclicBarrier b, int numOfCores) {
            this.ind = ind;
            this.start = start;
            this.end = end;
            this.barrier = b;
            this.numOfCores = numOfCores;
        }

        @Override
        public void run() {
            trace(start, end, ind);
        }

    }

}
