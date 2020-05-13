package com.company;

public class Extra {

    static double clamp(double x, double min, double max){
        if(x < min) return min;
        if(x > max) return max;
        return x;
    }

    static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }
}
