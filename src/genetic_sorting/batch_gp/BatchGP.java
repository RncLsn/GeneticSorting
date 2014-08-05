package genetic_sorting.batch_gp;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.evaluation.MeanInversionDistance;
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
import java.util.Date;
import java.util.Set;

/**
 * @author Alessandro Ronca
 */
public class BatchGP {

    public static final  int    INITIAL_POPULATION  = 2000;
    public static final  int    MAX_TREE_DEPTH      = 6;
    public static final  int    TEST_CASES          = 5;
    public static final  int    MAX_LIST_LENGTH     = 10;
    public static final  int    MAX_LIST_VALUE      = 20;
    public static final  double MUTATION_PCT        = 0.1;
    public static final  double CROSSOVER_PCT       = 0.9;
    public static final  double CROSSOVER_EXPANSION = 1.8;
    private static final int    GENERATIONS         = 5;

    private static final Path RESULT_PATH =
            Paths.get("Results/BatchGP_" + new Date().toString().replace(" ", "_"));


    // todo
    // 1) why does the population size increase?
    // 2) why do tall trees disappear?

    public static void main (String[] args) throws FileNotFoundException {

        try (PrintWriter fileOut = new PrintWriter(
                Files.newBufferedWriter(RESULT_PATH, Charset.defaultCharset()))) {

            String parametersString = "\nPARAMETERS" +
                                      "\nInitial population: " + INITIAL_POPULATION +
                                      "\nMax tree depth: " + MAX_TREE_DEPTH +
                                      "\nNumber of test cases: " + TEST_CASES +
                                      "\nMax list length: " + MAX_LIST_LENGTH +
                                      "\nMax list value: " + MAX_LIST_VALUE +
                                      "\n";

            System.out.println(parametersString);
            fileOut.println(parametersString);

            Population initialPopulation =
                    new Population(INITIAL_POPULATION,
                                   MAX_TREE_DEPTH,
                                   new RandomFunctionFactory(),
                                   new RandomTerminalFactory());

            Evaluation evaluation = new Evaluation(new MeanInversionDistance(),
                                                   TEST_CASES,
                                                   MAX_LIST_LENGTH,
                                                   MAX_LIST_VALUE);

            ReproductionAndCrossoverFactory recFactory =
                    new ReproductionAndCrossoverFactory(evaluation,
                                                        (int) (CROSSOVER_EXPANSION *
                                                               MAX_TREE_DEPTH),
                                                        CROSSOVER_PCT);
            UniformMutationFactory uniformFactory =
                    new UniformMutationFactory(MUTATION_PCT,
                                               new RandomFunctionFactory(),
                                               new RandomTerminalFactory());

            System.out.println("\nEXECUTION");
            System.out.println("Starting evolution...");
            long startTime = System.currentTimeMillis();
            Population evolvingPopulation = initialPopulation;
            for (int i = 0; i < GENERATIONS; i++) {
                System.out.print("\rIteration n. " + i);
                evolvingPopulation = evolvingPopulation.evolve(recFactory);
                evolvingPopulation = evolvingPopulation.evolve(uniformFactory);
            }
            System.out.println();
            long endTime = System.currentTimeMillis();
            String executionString = "Evolution completed in " + (endTime - startTime) / 1000 +
                                     " seconds";

            System.out.println(executionString);
            fileOut.println(executionString);

            Set<EvolvingSorting> correctIndividuals =
                    evolvingPopulation.getCorrectIndividuals(evaluation);

            String resultString = "\nRESULTS" +
                                  "\nTotal evolved individuals: " + evolvingPopulation.size() +
                                  "\nCorrect evolved individuals: " + correctIndividuals.size();

            System.out.println(resultString);
            fileOut.println(resultString);

            StringBuilder correctIndividualsStrBuild = new StringBuilder();
            correctIndividualsStrBuild.append("\nCorrect individuals:\n");
            for (EvolvingSorting individual : correctIndividuals) {
                correctIndividualsStrBuild.append(individual)
                                          .append(" ")
                                          .append(evaluation.fitness(individual))
                                          .append("\n");
            }
            System.out.println(correctIndividualsStrBuild);
            fileOut.println(correctIndividualsStrBuild);

            StringBuilder individualsSampleStrBuild = new StringBuilder();
            individualsSampleStrBuild.append("Sample of the evolved individuals:\n\n");
            for (int i = 0; i < 10; i++) {
                EvolvingSorting individual = evolvingPopulation.randomSelection();
                individualsSampleStrBuild.append(individual)
                                         .append(" ")
                                         .append(evaluation.fitness(individual))
                                         .append("\n");
            }
            System.out.println(individualsSampleStrBuild);
            fileOut.println(individualsSampleStrBuild);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
