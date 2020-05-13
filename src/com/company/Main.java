package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        // write your code here

        double aspectRatio = 16.0 / 9.0;
        int imgWidth = 500;
        int imgHeight = (int) (imgWidth / aspectRatio);
        int msaa = 10;
        int maxDepth = 200;
        int vfov = 20;
        Vec3 lookFrom = new Vec3(0, 1, 4);
        Vec3 lookAt = new Vec3(0,0,-1);
        Vec3 vUp = new Vec3(0, 1, 0);
        double distToFocus = Vec3.vec_minus(lookFrom,lookAt).length();
        double aperture = 2.0;

        Camera cam = new Camera(vfov, aspectRatio, lookFrom, lookAt, vUp, distToFocus, aperture);

        Tracer tracer = new Tracer(cam, imgWidth, imgHeight, msaa, maxDepth);
        double seqStart = System.nanoTime();
        tracer.initParallel();
        double seqEnd = System.nanoTime();

        double seqTime = (seqEnd - seqStart) / 1000000.0;

        System.out.println("Raytracing time: " + seqTime + "ms");

    }


}
