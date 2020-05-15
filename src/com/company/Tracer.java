package com.company;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;


public class Tracer {

    int cores = Runtime.getRuntime().availableProcessors();
    //int cores = 4;
    ArrayList<Hittable> list;
    Hittable world;
    Camera cam;
    Color color;
    int multiSample, imgHeight, imgWidth, maxDepth;
    ReentrantLock lock;
    ArrayList<Integer> workPool;


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


    void generateWorld(int hitWidth) {

        list = new ArrayList<>();
        list.add(new Sphere(new Vec3(0, 1, -1), 1, new Dielectrics(1.5)));
        list.add(new Sphere(new Vec3(0, 1.5, -1), 0.8, new Dielectrics(1.5)));
        list.add(new Sphere(new Vec3(0, 2, -1), 0.6, new Dielectrics(1.5)));
        list.add(new Sphere(new Vec3(-3, 1, -3), 0.6, new Dielectrics(1.5)));
        list.add(new Sphere(new Vec3(0, -100000, 0), 100000, new Lambertian(new Vec3(0.5, 0.5, 0.5))));
        list.add(new Sphere(new Vec3(-2, 1, 0), 1, new Metal(new Vec3(0.8, 0.6, 0.2), 0.5)));
        list.add(new Sphere(new Vec3(2, 1, -2), 1, new Metal(new Vec3(0.8, 0.8, 0.8), 0.0)));

        for (int j = -hitWidth; j < hitWidth; j++) {
            for (int k = -hitWidth; k < hitWidth; k++) {
                double matChooser = RandomNumGen.randomDouble();
                Vec3 center = new Vec3(j + 0.9 * RandomNumGen.randomDouble(), 0.2, k + 0.9 * RandomNumGen.randomDouble());
                if (Vec3.vec_minus(center, new Vec3(4, 0.2, 0)).length() > 0.9) {
                    if (matChooser < 0.75) {
                        //diffuse
                        Vec3 albedo = Vec3.vec_mul(Vec3.makeRandomVec(), Vec3.makeRandomVec());
                        list.add(new Sphere(center, 0.2, new Lambertian(albedo)));
                    } else if (matChooser < 0.9) {
                        //metal
                        Vec3 albedo = Vec3.makeRandomVecWithMinMax(0.5, 1);
                        double fuzz = RandomNumGen.randomDouble(0, 0.5);
                        list.add(new Sphere(center, 0.2, new Metal(albedo, fuzz)));
                    } else {
                        //glass
                        list.add(new Sphere(center, 0.2, new Dielectrics(1.5)));
                    }
                }


            }
        }
        this.world = new HittableList(list);
    }

    void initParallel() {
        int numOfCores = cores;
        lock = new ReentrantLock();
        workPool = new ArrayList<>();
        for (int i = 0; i < imgWidth; i++) {
            workPool.add(i);
        }

        Thread[] threads = new Thread[numOfCores];
        CyclicBarrier barrier = new CyclicBarrier(numOfCores);

        for (int i = 0; i < numOfCores; i++) {
            Thread t = new Thread(new Para(i, barrier, numOfCores));
            threads[i] = t;
            t.start();
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

    void trace(int ind) {
        int scanlinesCalculated = 0;
        boolean done = false;
        int index = 0;
        while (!done) {
            lock.lock();
            int size = workPool.size();
            if (size != 0) {
                index = workPool.get(size - 1);
                workPool.remove(size - 1);
            } else {
                done = true;
            }
            lock.unlock();

            for (int j = 0; j < imgHeight; j++) {
                Vec3 pixel_col = new Vec3(0, 0, 0);
                for (int s = 0; s < multiSample; s++) {
                    double u;
                    double v;
                    if (multiSample == 1) {
                        u = (double) (index) / (imgWidth - 1);
                        v = (double) (j) / (imgHeight - 1);
                    } else {
                        u = (index + RandomNumGen.randomDouble()) / (imgWidth - 1);
                        v = (j + RandomNumGen.randomDouble()) / (imgHeight - 1);
                    }

                    Ray r = cam.getRay(u, v);
                    pixel_col.add(ray_color(r, this.world, maxDepth));
                }

                color.setColor(pixel_col, multiSample, j, index);
            }
            scanlinesCalculated++;
            System.out.println("Thread: " + ind + " | scanlines calculated: " + scanlinesCalculated);
        }


    }

    static Vec3 ray_color(Ray r, Hittable world, int depth) {
        HitRecord rec = new HitRecord();
        if (depth <= 0) {
            return new Vec3(0, 0, 0);
        }

        if (world.hit(r, 0.001f, Double.MAX_VALUE, rec)) {
            Ray scattered = new Ray(new Vec3(0, 0, 0), new Vec3(0, 0, 0));
            Vec3 attenuation = new Vec3(0, 0, 0);
            if (rec.material.scatter(r, rec, attenuation, scattered)) {
                return attenuation.vecMul(ray_color(scattered, world, depth - 1));
            }
            return new Vec3(0, 0, 0);
        } else {
            Vec3 unit_direction = Vec3.unit_vector(r.direction());
            double t = 0.5 * (unit_direction.y() + 1.0);
            Vec3 v1 = new Vec3(1.0, 1.0, 1.0);
            Vec3 v2 = new Vec3(0.5, 0.7, 1.0);

            return v1.vecMulT(1 - t).vecPlus(v2.vecMulT(t));
        }
    }

    private class Para implements Runnable {

        int ind, numOfCores;
        CyclicBarrier barrier;


        Para(int ind, CyclicBarrier b, int numOfCores) {
            this.ind = ind;

            this.barrier = b;
            this.numOfCores = numOfCores;
        }

        @Override
        public void run() {
            trace(ind);
        }

    }

}
