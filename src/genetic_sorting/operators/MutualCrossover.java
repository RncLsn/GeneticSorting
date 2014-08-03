package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.Population;

import java.util.Collection;

/**
 * @author Alessandro Ronca
 */
public class MutualCrossover implements Operator {

    private final Evaluation evaluation;

    public MutualCrossover (Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public Collection<EvolvingSorting> operate (Population population) {
        return null;
    }
}
