package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.evaluation.disorder_measures.MeanInversionDistance;
import genetic_sorting.operators.factories.RandomOperatorsFactory;
import genetic_sorting.structures.Population;
import genetic_sorting.structures.factories.RandomFunctionFactory;
import genetic_sorting.structures.factories.RandomTerminalFactory;
import genetic_sorting.structures.individuals.EvolutionException;

/**
 * @author Alessandro Ronca
 */
public class DebugTest {

    public static void main (String[] args) throws EvolutionException {
//        testMutation();
        testMutualCrossover();
    }

    private static void testMutualCrossover () throws EvolutionException {
        Population population;
        final RandomFunctionFactory rff = new RandomFunctionFactory();
        final RandomTerminalFactory rtf = new RandomTerminalFactory();
        population = new Population(2, 3, rff, rtf);
        final Evaluation evaluation = new Evaluation(new MeanInversionDistance(), 1, 10, 20);

        // print generated individual
        System.out.println("Generated population");
        System.out.println(population);
        System.out.println("\n");

        Population evolvedPop = null;
        for (int i = 0; i < 2; i++) {
            evolvedPop = population.evolve(new RandomOperatorsFactory() {
                @Override
                public Operator makeOperator () {
                    return new MutualCrossover(evaluation, (int) (1.8 * 3));
                }
            });
        }

        // print individual after operation
        System.out.println("Evolved population");
        System.out.println(evolvedPop);
        System.out.println();
    }

    private static void testMutation () throws EvolutionException {
        Population population;
        final RandomFunctionFactory rff = new RandomFunctionFactory();
        final RandomTerminalFactory rtf = new RandomTerminalFactory();
        population = new Population(1, 3, rff, rtf);

        // print generated individual
        System.out.println(population);

        Population evolvedPop = population.evolve(new RandomOperatorsFactory() {
            @Override
            public Operator makeOperator () {
                return new UniformMutation(1, rff, rtf);
            }
        });

        // print individual after operation
        System.out.println(evolvedPop);
    }
}
