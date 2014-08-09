package genetic_sorting.batch_gp;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.evaluation.Inversions;
import genetic_sorting.operators.ReproductionAndCrossoverFactory;
import genetic_sorting.operators.UniformMutationFactory;
import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.Population;
import genetic_sorting.structures.RandomFunctionFactory;
import genetic_sorting.structures.RandomTerminalFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class BatchGP {

    private static final Path RESULT_PATH =
            Paths.get("Results/BatchGP_" + new Date().toString().replace(" ", "_"));

    private static final int RUNS = 20;

    // generality test
    private static final int GENERAL_TEST_CASES      = 100;
    private static final int GENERAL_MAX_LIST_LENGTH = 200;
    private static final int GENERAL_MAX_LIST_VALUE  = 400;

    // 1) why does the population size increase?
    // A: mutual crossover yields two individuals at each call -> fixed
    //
    // 2) why do tall trees disappear?
    // A: bug in mutual crossover -> fixed

    public static void main (String[] args) throws FileNotFoundException {

        // parameters
        int initialPopulation = 2000;
        int maxTreeDepth = 6;
        int testCases = 15;
        int maxListLength = 50;
        int maxListValue = 2 * maxListLength;
        double mutationPct = 0.1;
        double crossoverPct = 0.9;
        double crossoverExpansion = 1.8;
        int generations = 50;

        BatchParameters batchParameters = new BatchParameters(initialPopulation, maxTreeDepth,
                                                              testCases, maxListLength,
                                                              maxListValue, mutationPct,
                                                              crossoverPct, crossoverExpansion,
                                                              generations);

        try (PrintStream fileOut = new PrintStream(RESULT_PATH.toFile())) {

            // set of IO streams
            List<PrintStream> outs = Arrays.asList(System.out, fileOut);

            for (PrintStream out : outs) {
                out.println(batchParameters);
            }

            // execute several runs

            String runStr = "Executing " + RUNS + " runs...";
            for (PrintStream out : outs) {
                out.println(runStr);
            }

            Set<EvolvingSorting> solutions = new HashSet<>();
            int successfulRuns = 0;
            for (int i = 0; i < RUNS; i++) {

                String curRunStr = "\n----- Run n. " + (i + 1) + " -----\n";
                for (PrintStream out : outs) {
                    out.println(curRunStr);
                }

                Set<EvolvingSorting> someSolutions = batchGP(batchParameters, outs);
                if (!someSolutions.isEmpty()) {
                    successfulRuns++;
                    solutions.addAll(someSolutions);
                }
            }

            String finalResultStr = "Number of successful runs: " + successfulRuns +
                                    "\nNumber of total correct solutions: " + solutions.size();
            for (PrintStream out : outs) {
                out.println(finalResultStr);
            }

            // generality tests

            Set<EvolvingSorting> generalSortings = generalityTest(solutions, GENERAL_TEST_CASES,
                                                                  GENERAL_MAX_LIST_LENGTH,
                                                                  GENERAL_MAX_LIST_VALUE);

            StringBuilder generalResultSb = new StringBuilder();
            generalResultSb.append("Number of general sortings: ").append(generalSortings.size());
            generalResultSb.append("\nGeneral sortings: \n");
            for (EvolvingSorting generalSorting : generalSortings) {
                generalResultSb.append(generalSorting).append("\n");
            }

            for (PrintStream out : outs) {
                out.println(generalResultSb);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<EvolvingSorting> batchGP (BatchParameters params,
                                                 Collection<? extends PrintStream> outs) {

        Population initialPopulation =
                new Population(params.getInitialPopulation(),
                               params.getMaxTreeDepth(),
                               new RandomFunctionFactory(),
                               new RandomTerminalFactory());

        Evaluation evaluation = new Evaluation(new Inversions(),
                                               params.getTestCases(),
                                               params.getMaxListLength(),
                                               params.getMaxListValue());

        for (PrintStream out : outs) {
            out.println(evaluation);
        }

        ReproductionAndCrossoverFactory recFactory =
                new ReproductionAndCrossoverFactory(evaluation,
                                                    (int) (params.getCrossoverExpansion() *
                                                           params.getMaxTreeDepth() + 1),
                                                           /*TODO test +1*/
                                                           params.getCrossoverPct());
        UniformMutationFactory uniformFactory =
                new UniformMutationFactory(params.getMutationPct(),
                                           new RandomFunctionFactory(),
                                           new RandomTerminalFactory());

        Set<EvolvingSorting> correctIndividuals = Collections.emptySet();

        // BEGIN: evolution

        System.out.println("\nEXECUTION");
        System.out.println("Starting evolution...");
        long startTime = System.currentTimeMillis();
        Population evolvingPopulation = initialPopulation;
        for (int i = 0; i < params.getGenerations(); i++) {
            System.out.print("\rIteration n. " + i);
            evolvingPopulation = evolvingPopulation.evolve(recFactory);
            evolvingPopulation = evolvingPopulation.evolve(uniformFactory);

            // a halting condition
            correctIndividuals = evolvingPopulation.getCorrectIndividuals(evaluation);
            if (!correctIndividuals.isEmpty()) {
                break;
            }
        }
        System.out.println();
        long endTime = System.currentTimeMillis();
        String executionString = "Evolution completed in " +
                                 (endTime - startTime) / 1000 + " seconds";

        // END: evolution

        for (PrintStream out : outs) {
            out.println(executionString);
        }

        String resultString = "\nRESULTS" +
                              "\nFinal generation individuals: " + evolvingPopulation.size() +
                              "\nFinal generation correct individuals: " +
                              correctIndividuals.size();

        for (PrintStream out : outs) {
            out.println(resultString);
        }

        StringBuilder correctIndividualsStrBuild = new StringBuilder();
        correctIndividualsStrBuild.append("\nCorrect individuals:\n");
        for (EvolvingSorting individual : correctIndividuals) {
            correctIndividualsStrBuild.append(individual).append("\n");
        }
        for (PrintStream out : outs) {
            out.println(correctIndividualsStrBuild);
        }

        StringBuilder individualsSampleStrBuild = new StringBuilder();
        individualsSampleStrBuild.append("FP sample of the evolved individuals:\n\n");
        for (int i = 0; i < 10; i++) {
            EvolvingSorting individual =
                    evolvingPopulation.fitnessProportionalSelection(evaluation);
            individualsSampleStrBuild
                    .append(evaluation.fitness(individual))
                    .append(" ")
                    .append(individual)
                    .append("\n");
        }
        for (PrintStream out : outs) {
            out.println(individualsSampleStrBuild);
        }

        return correctIndividuals;
    }

    private static Set<EvolvingSorting> generalityTest (Set<EvolvingSorting> solutions,
                                                        int numOfTestCases, int maxListLength,
                                                        int maxListValue) {

        Evaluation generalEvaluation =
                new Evaluation(new Inversions(), numOfTestCases, maxListLength, maxListValue);

        Set<EvolvingSorting> generalSortings = new HashSet<>();

        for (EvolvingSorting sorting : solutions) {
            if (generalEvaluation.isCorrect(sorting)) {
                generalSortings.add(sorting);
            }
        }

        return generalSortings;
    }

}
