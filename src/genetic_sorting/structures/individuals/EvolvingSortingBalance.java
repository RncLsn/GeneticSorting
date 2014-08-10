package genetic_sorting.structures.individuals;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.util.Balance;

/**
 * @author Alessandro Ronca
 */
public class EvolvingSortingBalance implements Balance<EvolvingSorting> {

    private final Evaluation evaluation;

    public EvolvingSortingBalance (Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public double weigh (EvolvingSorting sorting) {
        return evaluation.fitness(sorting);
    }
}
