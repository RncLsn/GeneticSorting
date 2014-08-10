package genetic_sorting.evaluation.disorder_measures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public interface DisorderMeasure {

    double getValue (List<Integer> list);
}
