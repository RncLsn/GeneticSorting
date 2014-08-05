package genetic_sorting.util;

import java.util.List;
import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class MyArrays {

    public static <T extends Weighty> int indexWeightedRandomSelection (List<T> list) {

        double totalWeight = 0;
        for (T t : list) {
            totalWeight += t.getWeight();
        }

        Random rand = new Random();
        double p = rand.nextDouble() * totalWeight;

        double current = 0;
        for (int i = 0; i < list.size(); i++) {
            current += list.get(i).getWeight();
            if (p <= current) {
                return i;
            }
        }

        // it never happens
        return -1;
    }

    public static <T> int indexWeightedRandomSelection (List<T> list, Balance<T> balance) {
        double totalWeight = 0;
        for (T t : list) {
            totalWeight += balance.weigh(t);
        }

        Random rand = new Random();
        double p = rand.nextDouble() * totalWeight;

        double current = 0;
        for (int i = 0; i < list.size(); i++) {
            current += balance.weigh(list.get(i));
            if (p <= current) {
                return i;
            }
        }

        // it never happens
        return -1;
    }
}
