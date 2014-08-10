package genetic_sorting.batch_gp;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.evaluation.GeneralityTest;
import genetic_sorting.evaluation.disorder_measures.Inversions;
import genetic_sorting.operators.factories.ReproductionAndCrossoverFactory;
import genetic_sorting.operators.factories.UniformMutationFactory;
import genetic_sorting.structures.Population;
import genetic_sorting.structures.factories.RandomFunctionFactory;
import genetic_sorting.structures.factories.RandomTerminalFactory;
import genetic_sorting.structures.individuals.EvolutionException;
import genetic_sorting.structures.individuals.EvolvingSorting;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class BatchGP {

    private static final Path RESULT_PATH =
            Paths.get("Results/BatchGP_" + new Date().toString().replace(" ", "_"));

    private static final Path PROPERTIES_PATH =
            Paths.get("Config/batchGP.properties");

    /**
     * @param args
     */
    public static void main (String[] args) {

        // create result path
        if (Files.notExists(RESULT_PATH.getParent())) {
            try {
                Files.createDirectory(RESULT_PATH.getParent());
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        // load properties
        BatchProperties properties = new BatchProperties();
        try {
            properties.load(Files.newInputStream(PROPERTIES_PATH));
        } catch (IOException e) {
            System.out.println("Configuration file is not present at path: " + PROPERTIES_PATH);
        }

        try (PrintStream fileOut = new PrintStream(RESULT_PATH.toFile())) {

            // set of IO streams
            List<PrintStream> outs = Arrays.asList(System.out, fileOut);

            for (PrintStream out : outs) {
                out.println(properties);
            }

            // execute several runs

            printAllStreams(outs, "Executing " + properties.getIntProperty("runs") + " runs...");

            Set<EvolvingSorting> solutions = new HashSet<>();
            int successfulRuns = 0;
            for (int i = 0; i < properties.getIntProperty("runs"); i++) {

                printAllStreams(outs, "\n----- Run n. " + (i + 1) + " -----\n");

                Set<EvolvingSorting> someSolutions = null;
                try {
                    someSolutions = batchGP(properties, outs);
                } catch (EvolutionException e) {
                    System.out.println("Run failed!");
                    continue;
                }

                if (!someSolutions.isEmpty()) {
                    successfulRuns++;
                    solutions.addAll(someSolutions);
                }
            }

            printAllStreams(outs, "Number of successful runs: " + successfulRuns +
                                  "\nNumber of total correct solutions: " + solutions.size());

            // generality tests

            Set<EvolvingSorting> generalSortings =
                    GeneralityTest.generalityTest(solutions,
                                                  properties.getIntProperty("generalTestCases"),
                                                  properties.getIntProperty("generalMaxListLength"),
                                                  properties.getIntProperty("generalMaxListValue"));

            StringBuilder generalResultSb = new StringBuilder();
            generalResultSb.append("Number of general sortings: ").append(generalSortings.size());
            generalResultSb.append("\nGeneral sortings: \n");
            for (EvolvingSorting generalSorting : generalSortings) {
                generalResultSb.append(generalSorting).append("\n");
            }
            printAllStreams(outs, generalResultSb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param outs
     * @param s
     */
    private static void printAllStreams (Iterable<? extends PrintStream> outs, String s) {
        for (PrintStream out : outs) {
            out.println(s);
        }
    }

    /**
     * @param properties
     * @param outs
     * @return
     * @throws EvolutionException
     */
    private static Set<EvolvingSorting> batchGP (BatchProperties properties,
                                                 Collection<? extends PrintStream> outs)
            throws EvolutionException {

        Population initialPopulation =
                new Population(properties.getIntProperty("initialPopulation"),
                               properties.getIntProperty("maxTreeDepth"),
                               new RandomFunctionFactory(),
                               new RandomTerminalFactory());


        UniformMutationFactory uniformFactory =
                new UniformMutationFactory(properties.getDoubleProperty("mutationPct"),
                                           new RandomFunctionFactory(),
                                           new RandomTerminalFactory());

        Set<EvolvingSorting> correctIndividuals = Collections.emptySet();

        // BEGIN: evolution

        for (PrintStream out : outs) {
            out.println("\nEXECUTION\nStarting evolution...");
        }
        long startTime = System.currentTimeMillis();
        Population evolvingPopulation = initialPopulation;
        for (int i = 0; i < properties.getIntProperty("generations"); i++) {

            printAllStreams(outs, "Iteration n. " + i);

            Evaluation evaluation = new Evaluation(new Inversions(),
                                                   properties.getIntProperty("testCases"),
                                                   properties.getIntProperty("maxListLength"),
                                                   properties.getIntProperty("maxListValue"));

            printAllStreams(outs, evaluation.toString());

            ReproductionAndCrossoverFactory recFactory =
                    new ReproductionAndCrossoverFactory(evaluation,
                                                        (int) (properties.getDoubleProperty(
                                                                "crossoverExpansion") *
                                                               properties.getIntProperty(
                                                                       "maxTreeDepth") + 1),
                                                           /*TODO test +1*/
                                                           properties.getDoubleProperty(
                                                                   "crossoverPct"));
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

        printAllStreams(outs, "Evolution completed in " +
                              (endTime - startTime) / 1000 + " seconds");

        // END: evolution

        printAllStreams(outs, "\nRESULTS" +
                              "\nFinal generation individuals: " + evolvingPopulation.size() +
                              "\nFinal generation correct individuals: " +
                              correctIndividuals.size());

        StringBuilder correctIndividualsStrBuild = new StringBuilder();
        correctIndividualsStrBuild.append("\nCorrect individuals:\n");
        for (EvolvingSorting individual : correctIndividuals) {
            correctIndividualsStrBuild.append(individual).append("\n");
        }
        printAllStreams(outs, correctIndividualsStrBuild.toString());

        return correctIndividuals;
    }

}
