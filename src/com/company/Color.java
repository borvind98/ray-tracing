package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Color {

    // Create buffered image object
    BufferedImage img;
    int imgHeight, imgWidth;
    Extra ex;
    // file object
    File f;

    int[][] pixels;

    Color(int imgWidth, int imgHeight){
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        ex = new Extra();
        pixels = new int[imgHeight][imgWidth];
    }

    void setColor(Vec3 pixel_color, int samples_per_pixel, int j, int i){
        float r = pixel_color.x();
        float g = pixel_color.y();
        float b = pixel_color.z();

        float scale = 1.0f / samples_per_pixel;
        r = (float) Math.sqrt(scale*r);
        g = (float) Math.sqrt(scale*g);
        b = (float) Math.sqrt(scale*b);

        int ir = (int) (256.99f * ex.clamp(r, 0.0, 0.999));
        int ig = (int) (255.99f * ex.clamp(g, 0.0, 0.999));
        int ib = (int) (255.99f * ex.clamp(b, 0.0, 0.999));
        int a = 255;

        int p = (a << 24) | (ir << 16) | (ig << 8) | ib; //pixel

        pixels[j][i] = p;

    }

    void writeColors(){

        img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);

        for (int j = 0; j < imgHeight; j++) {
            for (int i = 0; i < imgWidth; i++) {
                img.setRGB(i, imgHeight - j - 1, pixels[j][i]);
            }
        }

        // write image
        try {
            f = new File("D:\\IntelliJ-prosjekter\\ray-tracing\\image.png");
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
