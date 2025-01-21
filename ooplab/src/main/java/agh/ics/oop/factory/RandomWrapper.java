package agh.ics.oop.factory;

import java.util.Random;

//test purpose
class RandomWrapper {
    private final Random random = new Random();

    int nextInt(int origin, int bound) {
        return random.nextInt(origin, bound);
    }

    int nextInt(int origin) {
        return random.nextInt(origin);
    }

}
