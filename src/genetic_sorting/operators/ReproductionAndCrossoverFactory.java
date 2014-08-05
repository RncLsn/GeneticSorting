package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;

import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class ReproductionAndCrossoverFactory implements RandomOperatorsFactory {

    private final Evaluation evaluation;
    private final int        maxHeight;

    public ReproductionAndCrossoverFactory (Evaluation evaluation, int maxHeight) {
        this.evaluation = evaluation;
        this.maxHeight = maxHeight;
    }

    @Override
    public Operator makeOperator () {
        Random rand = new Random();
        int r = rand.nextInt(2);
        switch (r) {
            case 0:
                return new Reproduction(evaluation);
            case 1:
                return new MutualCrossover(evaluation, maxHeight);
            default:
                return null;
        }
    }
}
