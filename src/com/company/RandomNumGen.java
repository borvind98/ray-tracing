package com.company;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumGen {

    static double random_double(){
        Random random = new Random();
        return random.nextDouble();

    }

    static double random_double_within_interval(double rangeMin, double rangeMax){
        return ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
    }
}
