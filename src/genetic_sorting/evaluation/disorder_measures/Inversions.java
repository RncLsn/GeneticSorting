package genetic_sorting.evaluation.disorder_measures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Inversions implements DisorderMeasure {

    @Override
    public double getValue (List<Integer> list) {

        int nInversions = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j)) {
                    nInversions++;
                }
            }
        }

        int maxInversions = (list.size()) * (list.size() - 1) / 2;
        return (double) nInversions / maxInversions;
    }
}