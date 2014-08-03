package genetic_sorting.evaluation;

import genetic_sorting.structures.EvolvingSorting;

import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class Evaluation {

    private static final int MAX_LENGTH = 20;
    private static final int MAX_VALUE  = 99;

    Set<List<Integer>> testCases;

    public Evaluation (int numOfTestCases) {
        this(numOfTestCases, MAX_LENGTH, MAX_VALUE);
    }

    public Evaluation (int numOfTestCases, int maxLength, int maxValue) {
        testCases = new HashSet<>();
        for (int i = 0; i < numOfTestCases; i++) {
            testCases.add(randomList(maxLength, maxValue));
        }
    }

    private List<Integer> randomList (int maxLength, int maxValue) {
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();
        int length = rand.nextInt(maxLength + 1);
        for (int i = 0; i < length; i++) {
            list.add(rand.nextInt(maxValue + 1));
        }
        return list;
    }

    public double fitness(EvolvingSorting sorting) {
        // todo
        return 0.0;
    }

    @Override
    public String toString () {
        return "Evaluation{" +
               "testCases=" + testCases +
               '}';
    }
}
