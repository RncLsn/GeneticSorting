package genetic_sorting.evaluation;

import genetic_sorting.evaluation.disorder_measures.Inversions;
import genetic_sorting.structures.individuals.EvolvingSorting;
import genetic_sorting.structures.individuals.ExecutableSorting;
import genetic_sorting.structures.individuals.InvalidExpressionException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alessandro Ronca
 */
public class GeneralityTest {


    private static final int NUM_TEST_CASES  = 100;
    private static final int MAX_LIST_LENGTH = 50;
    private static final int MAX_LIST_VALUE  = 100;

    public static void main (String[] args) {

        int numTestCases = NUM_TEST_CASES;
        int maxListLength = MAX_LIST_LENGTH;
        int maxListValue = MAX_LIST_VALUE;

        if (args.length < 1) {
            System.out.println("usage: GeneralityTest <sortingTree-file>");
        }
        if (args.length > 1) {
            numTestCases = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            maxListLength = Integer.parseInt(args[2]);
        }
        if (args.length > 3) {
            maxListValue = Integer.parseInt(args[3]);
        }

        try {
            boolean correct = true;

            // load program
            EvolvingSorting sorting = EvolvingSorting.generateFromEncoding(Paths.get(args[0]));

            // test
            for (int i = 0; i < numTestCases && correct; i++) {
                Evaluation evaluation = new Evaluation(new Inversions(), 1,
                                                       maxListLength, maxListValue);
                if (!evaluation.isCorrect(new ExecutableSorting(sorting))) {

                    correct = false;

                    System.out.println("The program failed to sort the following test case:");
                    System.out.println(evaluation);
                    System.out.println("The output sequence was:");

                    List<Integer> next = evaluation.getTestCases().iterator().next();
                    sorting.trySorting(next);
                    System.out.println(next);
                }
            }

            if (correct) {
                System.out.println("The program is correct!");
            }

        } catch (IOException e) {
            System.out.println("file " + args[0] + "cannot be opened");
            System.exit(1);
        } catch (InvalidExpressionException e) {
            System.out.println("file " + args[0] + "doesn't contain a valid program");
        }

    }

    public static Set<EvolvingSorting> generalityTest (Iterable<EvolvingSorting> solutions,
                                                       int numOfTestCases, int maxListLength,
                                                       int maxListValue) {

        Evaluation generalEvaluation =
                new Evaluation(new Inversions(), numOfTestCases, maxListLength, maxListValue);

        Set<EvolvingSorting> generalSortings = new HashSet<>();

        for (EvolvingSorting sorting : solutions) {

            ExecutableSorting executableSorting = new ExecutableSorting(sorting);

            if (generalEvaluation.isCorrect(executableSorting)) {
                generalSortings.add(sorting);
            }
        }

        return generalSortings;
    }
}
