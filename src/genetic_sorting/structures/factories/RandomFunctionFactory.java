package genetic_sorting.structures.factories;

import genetic_sorting.structures.expressions.*;

import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class RandomFunctionFactory {

    public Function makeFunction () {
        Random rand = new Random();
        int r = rand.nextInt(7);
        switch (r) {
            case 0:
                return new Decrement();
            case 1:
                return new GetBigger();
            case 2:
                return new GetSmaller();
            case 3:
                return new Increment();
            case 4:
                return new Iterate();
            case 5:
                return new Subtract();
            case 6:
                return new Swap();
            default:
                return null;
        }
    }
}
