package com.company;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumGen {

    static double random_double(){
        Random random = new Random();
        double rand_double = random.nextDouble();
        return rand_double;
    }

    static double random_double_within_interval(double rangeMin, double rangeMax){
        double rand_double = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
        return rand_double;
    }
}
