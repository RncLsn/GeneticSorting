package genetic_sorting.batch_gp;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.evaluation.MeanInversionDistance;
import genetic_sorting.operators.ReproductionAndCrossoverFactory;
import genetic_sorting.operators.UniformMutationFactory;
import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.Population;
import genetic_sorting.structures.RandomFunctionFactory;
import genetic_sorting.structures.RandomTerminalFactory;

import java.util.Set;

/**
 * @author Alessandro Ronca
 */
public class BatchGP {

    public static final int INITIAL_POPULATION = 2000;
    public static final int MAX_TREE_DEPTH     = 6;
    public static final int TEST_CASES         = 15;
    public static final int MAX_LIST_LENGTH    = 100;
    public static final int MAX_LIST_VALUE     = 199;

    public static void main (String[] args) {

        System.out.println("PARAMETERS");
        System.out.println("Initial population: " + INITIAL_POPULATION);
        System.out.println("Max tree depth: " + MAX_TREE_DEPTH);
        System.out.println("Number of test cases: " + TEST_CASES);
        System.out.println("Max list length: " + MAX_LIST_LENGTH);
        System.out.println("Max list value: " + MAX_LIST_VALUE);

        Population initialPopulation =
                new Population(INITIAL_POPULATION,
                               MAX_TREE_DEPTH,
                               new RandomFunctionFactory(),
                               new RandomTerminalFactory());

        Evaluation evaluation = new Evaluation(new MeanInversionDistance(),
                                               TEST_CASES,
                                               MAX_LIST_LENGTH,
                                               MAX_LIST_VALUE);

        ReproductionAndCrossoverFactory recFactory = new ReproductionAndCrossoverFactory(
                evaluation);
        UniformMutationFactory uniformFactory = new UniformMutationFactory();

        System.out.println("EXECUTION");
        System.out.println("Starting evolution...");
        long startTime = System.currentTimeMillis();
        Population evolvingPopulation = initialPopulation;
        for (int i = 0; i < 40; i++) {
            evolvingPopulation = evolvingPopulation.evolve(recFactory);
            evolvingPopulation = evolvingPopulation.evolve(uniformFactory);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Evolution completed in " +
                           String.format("%f.1", ((float) endTime - startTime) / 1000) + "seconds");

        Set<EvolvingSorting> correctIndividuals =
                evolvingPopulation.getCorrectIndividuals(evaluation);

        System.out.println("RESULTS");
        System.out.println("Total evolved individuals: " + evolvingPopulation.size());
        System.out.println("Correct evolved individuals: " + correctIndividuals.size());
    }

}
