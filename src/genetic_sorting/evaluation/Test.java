package genetic_sorting.evaluation;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Test {


    public static void main (String[] args) {
        testMeanInversionDistance();
    }

    public static void testMeanInversionDistance () {
        MeanInversionDistance meanInversionDistance = new MeanInversionDistance();
        List<Integer> list = Arrays.asList(9, 6, 10, 7, 8);
        double mid = meanInversionDistance.getValue(list);
        System.out.println(mid);
    }
}
