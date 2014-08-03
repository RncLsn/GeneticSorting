package genetic_sorting.evaluation;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class MeanInversionDistance implements DisorderMeasure {

    @Override
    public double getValue (List<Integer> list) {
        int totalInversionDistance = 0;
        int invertedPairs = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j)) {
                    invertedPairs++;
                    totalInversionDistance += j - i;
                }
            }
        }
        return (invertedPairs == 0) ? 0 : (double) totalInversionDistance / invertedPairs;
    }
}
