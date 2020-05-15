package com.company;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumGen {

    static double randomDouble(){
        Random random = new Random();
        return random.nextDouble();

    }

    static double randomDouble(double rangeMin, double rangeMax){
        return ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
    }

    static int randomInt(){
        Random random = new Random();
        return random.nextInt();
    }

    static int randomInt(int rangeMin, int rangeMax){
        return ThreadLocalRandom.current().nextInt(rangeMin, rangeMax);
    }
}
