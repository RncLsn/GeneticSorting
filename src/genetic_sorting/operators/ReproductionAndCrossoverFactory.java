package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;

import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class ReproductionAndCrossoverFactory implements RandomOperatorsFactory {

    private final Evaluation evaluation;

    public ReproductionAndCrossoverFactory (Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public Operator makeOperator () {
        Random rand = new Random();
        int r = rand.nextInt(2);
        switch (r) {
            case 0:
                return new Reproduction(evaluation);
            case 1:
                return new MutualCrossover(evaluation);
            default:
                return null;
        }
    }
}
