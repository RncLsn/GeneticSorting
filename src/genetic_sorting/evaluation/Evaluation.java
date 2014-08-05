package genetic_sorting.evaluation;

import genetic_sorting.structures.EvolvingSorting;

import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class Evaluation {

    private static final int MAX_LENGTH = 20;
    private static final int MAX_VALUE  = 99;
    private final int                                numOfTestCases;
    private final int                                maxLength;
    private final int                                maxValue;
    private final DisorderMeasure                    disorderMeasure;
    private final Hashtable<EvolvingSorting, Double> memo;

    Set<List<Integer>> testCases;

    public Evaluation (DisorderMeasure disorderMeasure, int numOfTestCases) {
        this(disorderMeasure, numOfTestCases, MAX_LENGTH, MAX_VALUE);
    }

    public Evaluation (DisorderMeasure disorderMeasure, int numOfTestCases, int maxLength,
                       int maxValue) {
        this.disorderMeasure = disorderMeasure;
        this.numOfTestCases = numOfTestCases;
        this.maxLength = maxLength;
        this.maxValue = maxValue;
        this.memo = new Hashtable<EvolvingSorting, Double>();
        testCases = new HashSet<>();
        for (int i = 0; i < numOfTestCases; i++) {
            testCases.add(randomList(maxLength, maxValue));
        }
    }

    private List<Integer> randomList (int maxLength, int maxValue) {
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();
        int length = rand.nextInt(maxLength);
        for (int i = 0; i < length; i++) {
            list.add(rand.nextInt(maxValue + 1));
        }
        return list;
    }

    /**
     * makes use of memoization.
     *
     * @param sorting
     * @return
     */
    public double fitness (EvolvingSorting sorting) {
        if (!memo.containsKey(sorting)) {
            double totalFitness = 0;
            for (List<Integer> testCase : testCases) {
                ArrayList<Integer> testCaseCopy = new ArrayList<>(testCase);
                sorting.trySorting(testCaseCopy);
                double disorder = disorderMeasure.getValue(testCaseCopy);
                totalFitness += 1 - (1.0 / Math.pow(2, disorder));
            }
            memo.put(sorting, totalFitness / testCases.size());
        }
        return memo.get(sorting);
    }

    public boolean isCorrect (EvolvingSorting sorting) {
        if (memo.containsKey(sorting)) {
            return memo.get(sorting) == 1;
        }

        for (List<Integer> testCase : testCases) {
            ArrayList<Integer> testCaseCopy = new ArrayList<>(testCase);
            sorting.trySorting(testCaseCopy);
            double disorder = disorderMeasure.getValue(testCaseCopy);
            if (disorder > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString () {
        return "Evaluation{" +
               "maxValue=" + maxValue +
               ", maxLength=" + maxLength +
               ", numOfTestCases=" + numOfTestCases +
               '}';
    }
}
