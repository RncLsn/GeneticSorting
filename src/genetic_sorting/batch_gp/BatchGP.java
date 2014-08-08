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
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alessandro Ronca
 */
public class BatchGP {

    private static final Path RESULT_PATH =
            Paths.get("Results/BatchGP_" + new Date().toString().replace(" ", "_"));
    private static final int RUNS = 20;

    // 1) why does the population size increase?
    // A: mutual crossover yields two individuals at each call -> fixed
    //
    // 2) why do tall trees disappear?
    // A: bug in mutual crossover -> fixed

    public static void main (String[] args) throws FileNotFoundException {

        int initialPopulation = 2000;
        int maxTreeDepth = 6;
        int testCases = 4;
        int maxListLength = 10;
        int maxListValue = 2 * maxListLength;
        double mutationPct = 0.1;
        double crossoverPct = 0.9;
        double crossoverExpansion = 1.8;
        int generations = 50;

        Set<EvolvingSorting> solutions = new HashSet<>();
        int successfulRuns = 0;

        System.out.println("Executing " + RUNS + " runs...");

        for (int i = 0; i < RUNS; i++) {

            System.out.println("\n----- Run n. " + (i + 1) + " -----\n");

            Set<EvolvingSorting> someSolutions = batchGP(new BatchParameters(initialPopulation,
                                                                             maxTreeDepth,
                                                                             testCases,
                                                                             maxListLength,
                                                                             maxListValue,
                                                                             mutationPct,
                                                                             crossoverPct,
                                                                             crossoverExpansion,
                                                                             generations));
            if (!someSolutions.isEmpty()) {
                successfulRuns++;
                solutions.addAll(someSolutions);
            }
        }

        System.out.println("Number of successful runs: " + successfulRuns);
        System.out.println("Number of total correct solutions: " + solutions.size());
    }

    private static Set<EvolvingSorting> batchGP (BatchParameters params) {

        try (PrintWriter fileOut = new PrintWriter(
                Files.newBufferedWriter(RESULT_PATH, Charset.defaultCharset()))) {

            System.out.println(params);
            fileOut.println(params);

            Population initialPopulation =
                    new Population(params.getInitialPopulation(),
                                   params.getMaxTreeDepth(),
                                   new RandomFunctionFactory(),
                                   new RandomTerminalFactory());

            Evaluation evaluation = new Evaluation(new Inversions(),
                                                   params.getTestCases(),
                                                   params.getMaxListLength(),
                                                   params.getMaxListValue());

            System.out.println(evaluation);
            fileOut.println(evaluation);

            ReproductionAndCrossoverFactory recFactory =
                    new ReproductionAndCrossoverFactory(evaluation,
                                                        (int) (params.getCrossoverExpansion() *
                                                               params.getMaxTreeDepth()),
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

            System.out.println(executionString);
            fileOut.println(executionString);

            String resultString = "\nRESULTS" +
                                  "\nFinal generation individuals: " + evolvingPopulation.size() +
                                  "\nFinal generation correct individuals: " +
                                  correctIndividuals.size();

            System.out.println(resultString);
            fileOut.println(resultString);

            StringBuilder correctIndividualsStrBuild = new StringBuilder();
            correctIndividualsStrBuild.append("\nCorrect individuals:\n");
            for (EvolvingSorting individual : correctIndividuals) {
                correctIndividualsStrBuild.append(individual).append("\n");
            }
            System.out.println(correctIndividualsStrBuild);
            fileOut.println(correctIndividualsStrBuild);

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
            System.out.println(individualsSampleStrBuild);
            fileOut.println(individualsSampleStrBuild);

            return correctIndividuals;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
