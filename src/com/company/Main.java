package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        // write your code here

        double aspectRatio = 16.0 / 9.0;
        int imgWidth = 240;
        int imgHeight = (int) (imgWidth / aspectRatio);
        int msaa = 1000;
        int maxDepth = 120;

        Tracer tracer = new Tracer(aspectRatio, imgWidth, imgHeight, msaa, maxDepth);
        double seqStart = System.nanoTime();
        tracer.initParallel();
        double seqEnd = System.nanoTime();

        double seqTime = (seqEnd - seqStart) / 1000000.0;

        System.out.println("Raytracing time: " + seqTime + "ms");

    }


}
