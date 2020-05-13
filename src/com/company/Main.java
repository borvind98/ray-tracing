package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        double aspectRatio = 16.0 / 9.0;
        int imgWidth = 1280;
        int imgHeight = (int) (imgWidth / aspectRatio);
        int msaa = 200;
        int maxDepth = 150;
        int vfov = 20;
        Vec3 lookFrom = new Vec3(12, 2, 4);
        Vec3 lookAt = new Vec3(0, 1, -1);
        Vec3 vUp = new Vec3(0, 1, 0);
        double distToFocus = lookFrom.vecMinus(lookAt).length();
        double aperture = 0.1;

        Camera cam = new Camera(vfov, aspectRatio, lookFrom, lookAt, vUp, distToFocus, aperture);

        Tracer tracer = new Tracer(cam, imgWidth, imgHeight, msaa, maxDepth);
        double rayStart = System.nanoTime();
        tracer.initParallel();
        double rayEnd = System.nanoTime();

        double rayTime = (rayEnd - rayStart) / 1000000.0;
        double totalAmountOfRays = imgHeight * imgWidth * msaa;

        double sec = 1000;
        double min = 60000;
        double hour = min * 60;

        double raysPerSec = totalAmountOfRays / (rayTime / sec);

        if (rayTime > hour) {
            System.out.println("Raytracing time: " + (int) Math.floor(rayTime / hour) + (int) Math.floor((rayTime % hour) / min) + "min " + (int) Math.floor((rayTime % min) / sec) + "sec");
        } else if (rayTime > min) {
            System.out.println("Raytracing time: " + (int) Math.floor(rayTime / min) + "min " + (int) Math.floor((rayTime % min) / sec) + "sec");
        } else if (rayTime > sec) {
            System.out.println("Raytracing time: " + (int) Math.floor(rayTime / sec) + "sec " + (int) Math.floor(rayTime % sec) + "ms");
        } else {
            System.out.println("Raytracing time: " + rayTime + "ms");
        }
        System.out.println("Average rays per second: " + raysPerSec);


    }


}
