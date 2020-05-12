package com.company;

import java.util.concurrent.CyclicBarrier;

public class Tracer {

    int cores = Runtime.getRuntime().availableProcessors();
    Hitable[] list;
    Hitable world;
    Camera cam;
    RandomNumGen rand;
    Color color;
    int multiSample, imgHeight, imgWidth, maxDepth;
    double aspectRatio;


    Tracer(double aspectRatio, int imgWidth, int imgHeight, int msaa, int maxDepth) {
        this.aspectRatio = aspectRatio;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        this.multiSample = msaa;
        this.maxDepth = maxDepth;
        list = new Hitable[4];

        //list[0] = new Sphere(new Vec3(0,0,-1), 0.5f, new Dielectrics(new Vec3(0.7f, 0.3f, 0.3f)));
        list[0] = new Sphere(new Vec3(0, 0, -1), 0.5f, new Dielectrics(1.5f));
        list[1] = new Sphere(new Vec3(0, -100.5f, -1), 100, new Lambertian(new Vec3(0.8f, 0.8f, 0.0f)));
        list[2] = new Sphere(new Vec3(1, 0, -1), 0.5f, new Metal(new Vec3(0.8f, 0.6f, 0.2f), 1.0f));
        list[3] = new Sphere(new Vec3(-1, 0, -1), 0.5f, new Metal(new Vec3(0.8f, 0.8f, 0.8f), 0.3f));
        world = new HitableList(list);

        cam = new Camera(aspectRatio);
        rand = new RandomNumGen();
        color = new Color(imgWidth, imgHeight);
    }

    void initParallel() {
        int numOfCores = cores;

        Thread[] threads = new Thread[numOfCores];
        CyclicBarrier barrier = new CyclicBarrier(numOfCores);
        int partToSplit = imgHeight / numOfCores;
        int rest = imgHeight % numOfCores;
        for (int i = 0; i < numOfCores; i++) {
            if (i == numOfCores - 1) {
                int start = (i * partToSplit);
                int end = imgHeight;
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
        for (int j = start; j < end; j++) {
            int remaining = end - j;
            System.out.println("Thread: " + ind + " | Scanlines remaining: " + remaining);
            for (int i = 0; i < imgWidth; i++) {
                Vec3 pixel_col = new Vec3(0, 0, 0);
                for (int s = 0; s < multiSample; s++) {
                    float u;
                    float v;
                    if (multiSample == 1) {
                        u = (float) (i) / (imgWidth - 1);
                        v = (float) (j) / (imgHeight - 1);
                    } else {
                        u = (float) (i + RandomNumGen.random_double()) / (imgWidth - 1);
                        v = (float) (j + RandomNumGen.random_double()) / (imgHeight - 1);
                    }

                    Ray r = cam.getRay(u, v);
                    pixel_col.add(ray_color(r, world, maxDepth));
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

        if (world.hit(r, 0.001f, Float.MAX_VALUE, rec)) {
            Ray scattered = new Ray(new Vec3(0, 0, 0), new Vec3(0, 0, 0));
            Vec3 attenuation = new Vec3(0, 0, 0);
            if (rec.material.scatter(r, rec, attenuation, scattered)) {
                return Vec3.vec_mul(attenuation, ray_color(scattered, world, depth - 1));
            }
            return new Vec3(0, 0, 0);
        } else {
            Vec3 unit_direction = Vec3.unit_vector(r.direction());
            float t = 0.5f * (unit_direction.y() + 1.0f);
            Vec3 v1 = new Vec3(1.0f, 1.0f, 1.0f);
            Vec3 v2 = new Vec3(0.5f, 0.7f, 1.0f);

            return Vec3.vec_plus(v1.mul_t(1.0f - t), v2.mul_t(t));
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
