package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;

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
//        int r = rand.nextInt(2);
//        switch (r) {
//            case 0:
//                return new Reproduction(evaluation);
//            case 1:
//                return new MutualCrossover(evaluation, maxHeight);
//            default:
//                return null;
//        }
        if (rand.nextDouble() < crossoverPct) {
            return new MutualCrossover(evaluation, maxHeight);
        } else {
            return new Reproduction(evaluation);
        }
    }
}
