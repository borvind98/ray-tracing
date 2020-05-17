package com.company;

public class Texture {

    Vec3 value(double u, double v, Vec3 point){
        return new Vec3(0,0,0);
    }

}

class SolidColor extends Texture{

    Vec3 colorValue;

    SolidColor(Vec3 color){
        this.colorValue = color;
    }

    SolidColor(double red, double green, double blue){
        this(new Vec3(red, blue, green));
    }

    Vec3 value(double u, double v, Vec3 point){
        return colorValue;
    }

}

class CheckerTexture extends Texture{

    Texture odd, even;

    CheckerTexture(Texture t0, Texture t1){
        this.odd = t1;
        this.even = t0;
    }

    Vec3 value(double u, double v, Vec3 point){
        double sines = Math.sin(10*point.x())*Math.sin(10*point.y())*Math.sin(10*point.z());
        if (sines < 0) {
            return odd.value(u,v,point);
        }
        else{
            return even.value(u,v,point);
        }
    }

}

class NoiseTexture extends Texture{
    Perlin noise;
    double scale;

    NoiseTexture(){
        this.noise = new Perlin();
    }

    NoiseTexture(double scale){
        this.noise = new Perlin();
        this.scale = scale;
    }

    @Override
    Vec3 value(double u, double v, Vec3 point){
        return new Vec3(1,1,1).vecMulT(1.0 + Math.sin(scale*point.z() + 10*noise.turbulence(point))).vecMulT(0.5);
    }
}
