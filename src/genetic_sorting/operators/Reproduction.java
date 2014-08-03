package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.Population;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Alessandro Ronca
 */
public class Reproduction implements Operator {

    private final Evaluation evaluation;

    public Reproduction (Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public Collection<EvolvingSorting> operate (Population population) {
        return Arrays.asList(population.fitnessProportionalSelection(evaluation));
    }
}
