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
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class BatchGP implements Runnable {

    private static final Path RESULT_PATH =
            Paths.get("Results/BatchGP_" + new Date().toString().replace(" ", "_"));

    private static final Path PROPERTIES_PATH = Paths.get("Config/batchGP.properties");

    private static final int GT_PERIOD = 20;

    private final BatchProperties    properties;
    private final List<OutputStream> outStreams;
    private int     successfulRuns              = 0;
    private int     correctSortings             = 0;
    private int     generationsOfSuccessfulRuns = 0;
    private int     numberOfGeneralSortings     = 0;
    private boolean executed                    = false;


    public BatchGP (BatchProperties properties, List<OutputStream> outStreams) {
        this.properties = properties;
        this.outStreams = outStreams;
    }

    public BatchGP (BatchProperties properties) {
        this.properties = properties;
        this.outStreams = new ArrayList<>();
    }

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
            System.err.println("Configuration file is not present at path: " + PROPERTIES_PATH);
        }

        try (OutputStream outStream = Files.newOutputStream(RESULT_PATH)) {
            BatchGP batchGPRunnable = new BatchGP(properties, Arrays.asList(System.out, outStream));
            Thread t = new Thread(batchGPRunnable);
            t.start();
            t.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BatchProperties getProperties () {
        return properties;
    }

    @Override
    public void run () {
        if (executed) {
            return;
        } else {
            executed = true;
        }

//        List<PrintStream> outs;
//        if (outStream != null) {
//            PrintStream fileOut = new PrintStream(outStream);
//            // set of IO streams
//            outs = Arrays.asList(System.out, fileOut);
//        }
//        else {
//            outs = Arrays.asList(System.out);
//        }

        List<PrintStream> outs = new ArrayList<>();
        for (OutputStream outputStream : outStreams) {
            outs.add(new PrintStream(outputStream));
        }


        for (PrintStream out : outs) {
            out.println(properties);
        }

        // execute several runs

        printAllStreams(outs, "Executing " + properties.getIntProperty("runs") + " runs...");

        Set<EvolvingSorting> solutions = new HashSet<>();
        Set<EvolvingSorting> generalSortings = new HashSet<>();
        for (int i = 0; i < properties.getIntProperty("runs"); i++) {

            printAllStreams(outs, "\n----- Run n. " + (i + 1) + " -----\n");

            Set<EvolvingSorting> someSolutions;
            BatchGPResult batchGPResult;
            try {
                batchGPResult = batchGP(properties, outs);
                someSolutions = batchGPResult.getCorrectIndividuals();
            } catch (EvolutionException e) {
//                System.out.println("Run failed!");
                printAllStreams(outs, "Run failed!");
                continue;
            }

            if (!someSolutions.isEmpty()) {
                successfulRuns++;
                correctSortings += someSolutions.size();
                generationsOfSuccessfulRuns = batchGPResult.getGenerations();
                solutions.addAll(someSolutions);
            }
            if (i % GT_PERIOD == 0) {
                // periodic generality tests
                generalSortings.addAll(
                        GeneralityTest.generalityTest(
                                solutions,
                                properties.getIntProperty("generalTestCases"),
                                properties.getIntProperty("generalMaxListLength"),
                                properties.getIntProperty("generalMaxListValue")));
                solutions.clear();
            }
        }

        printAllStreams(outs, "GLOBAL RESULTS\nNumber of successful runs: " + successfulRuns +
                              "\nNumber of total correct solutions: " + solutions.size());

        // generality tests
        generalSortings.addAll(
                GeneralityTest.generalityTest(
                        solutions,
                        properties.getIntProperty("generalTestCases"),
                        properties.getIntProperty("generalMaxListLength"),
                        properties.getIntProperty("generalMaxListValue")));

        numberOfGeneralSortings = generalSortings.size();

        StringBuilder generalResultSb = new StringBuilder();
        generalResultSb.append("Number of general sortings: ").append(generalSortings.size());
        generalResultSb.append("\nGeneral sortings: \n");
        for (EvolvingSorting generalSorting : generalSortings) {
            generalResultSb.append(generalSorting).append("\n");
        }
        printAllStreams(outs, generalResultSb.toString());

        for (PrintStream printStream : outs) {
            if (!printStream.equals(System.out)) {
                printStream.close();
            }
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
    private static BatchGPResult batchGP (BatchProperties properties,
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
        int generationIndex;
        for (generationIndex = 0; generationIndex < properties.getIntProperty("generations");
             generationIndex++) {

            printAllStreams(outs, "Iteration n. " + generationIndex);

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
//        System.out.println();
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

        return new BatchGPResult(correctIndividuals, generationIndex);
    }

    public int getNumberOfCorrectSortings () {
        if (!executed) {
            throw new IllegalStateException("Runnable not executed yet");
        }

        return correctSortings;
    }

    public int getNumberOfGeneralSortings () {
        if (!executed) {
            throw new IllegalStateException("Runnable not executed yet");
        }

        return numberOfGeneralSortings;
    }

    public double getAvgGenerationsOnAllRuns () {
        if (!executed) {
            throw new IllegalStateException("Runnable not executed yet");
        }

        int runs = properties.getIntProperty("runs");
        int generationsOfFailedRuns =
                (runs - successfulRuns) * properties.getIntProperty("generations");

        return (double) (generationsOfFailedRuns + generationsOfSuccessfulRuns) / runs;
    }

    public double getAvgGenerationsOnSuccessfulRuns () {
        if (!executed) {
            throw new IllegalStateException("Runnable not executed yet");
        }

        return (double) generationsOfSuccessfulRuns / successfulRuns;
    }

    public double getAvgCorrectSortings () {
        if (!executed) {
            throw new IllegalStateException("Runnable not executed yet");
        }

        int runs = properties.getIntProperty("runs");
        return (double) correctSortings / runs;
    }

    public double getAvgGeneralSortings () {
        if (!executed) {
            throw new IllegalStateException("Runnable not executed yet");
        }

        int runs = properties.getIntProperty("runs");
        return (double) numberOfGeneralSortings / runs;
    }

    public double getGeneralityRatio () {
        if (!executed) {
            throw new IllegalStateException("Runnable not executed yet");
        }

        return (double) numberOfGeneralSortings / correctSortings;
    }
}
