package genetic_sorting.operators.factories;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.operators.MutualCrossover;
import genetic_sorting.operators.Operator;
import genetic_sorting.operators.Reproduction;

import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class ReproductionAndCrossoverFactory implements RandomOperatorsFactory {

    private final Evaluation evaluation;
    private final int        maxHeight;
    private final double     crossoverPct;

    public ReproductionAndCrossoverFactory (Evaluation evaluation, int maxHeight,
                                            double crossoverPct) {
        this.evaluation = evaluation;
        this.maxHeight = maxHeight;
        this.crossoverPct = crossoverPct;
    }

    @Override
    public Operator makeOperator () {

        Random rand = new Random();

        if (rand.nextDouble() < crossoverPct) {
            return new MutualCrossover(evaluation, maxHeight);
        } else {
            return new Reproduction(evaluation);
        }
    }
}
