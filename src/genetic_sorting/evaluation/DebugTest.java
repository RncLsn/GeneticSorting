package genetic_sorting.evaluation;

import genetic_sorting.evaluation.disorder_measures.MeanInversionDistance;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
class DebugTest {


    public static void main (String[] args) {
        testMeanInversionDistance();
    }

    private static void testMeanInversionDistance () {
        MeanInversionDistance meanInversionDistance = new MeanInversionDistance();
        List<Integer> list = Arrays.asList(9, 6, 10, 7, 8);
        double mid = meanInversionDistance.getValue(list);
        System.out.println(mid);
    }
}
