package genetic_sorting.util;

import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class MyCollections {

    public static <T> T randomSelection (Iterable<T> iterable) {

        Random rand = new Random();
        double p = rand.nextDouble();

        int n = 0;
        for (T t : iterable) {
            n++;
        }

        double current = 0;
        double increment = 1.0 / n;
        for (T t : iterable) {
            current += increment;
            if (p <= current) {
                return t;
            }
        }

        // it never happens
        throw new RuntimeException("Wrong code");
    }

    public static <T extends Weighty> T weightedRandomSelection (Iterable<T> iterable) {

        double totalWeight = 0;
        for (T t : iterable) {
            totalWeight += t.getWeight();
        }

        Random rand = new Random();
        double p = rand.nextDouble() * totalWeight;

        double current = 0;
        for (T t : iterable) {
            current += t.getWeight();
            if (p <= current) {
                return t;
            }
        }

        // it never happens
        throw new RuntimeException("Wrong code");
    }

//    public static <T> T weightedRandomSelection (Collection<T> collection, Balance<T> balance) {
//        double totalWeight = 0;
//        for (T t : collection) {
//            totalWeight += balance.weigh(t);
//        }
//
//        Random rand = new Random();
//        double p = rand.nextDouble() * totalWeight;
//
//        double current = 0;
//        for (T t : collection) {
//            current += balance.weigh(t);
//            if (p <= current) {
//                return t;
//            }
//        }
//
//        // it never happens
//        throw new RuntimeException("Wrong code");
//    }

    public static <T> T weightedRandomSelection (Iterable<T> iterable, Balance<T> balance) {
        double totalWeight = 0;
        for (T t : iterable) {
            totalWeight += balance.weigh(t);
        }

        Random rand = new Random();
        double p = rand.nextDouble() * totalWeight;

        double current = 0;
        for (T t : iterable) {
            current += balance.weigh(t);
            if (p <= current) {
                return t;
            }
        }

        // it never happens
        throw new RuntimeException("Wrong code");
    }

}
