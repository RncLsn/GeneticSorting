package genetic_sorting.evaluation;

import genetic_sorting.structures.EvolvingSorting;

import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class Evaluation {

    private static final int MAX_LENGTH = 20;
    private static final int MAX_VALUE  = 99;
    private final int             numOfTestCases;
    private final int             maxLength;
    private final int             maxValue;
    private final DisorderMeasure disorderMeasure;

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

    public double fitness (EvolvingSorting sorting) {
        double totalFitness = 0;
        for (List<Integer> testCase : testCases) {
            ArrayList<Integer> testCaseCopy = new ArrayList<>(testCase);
            sorting.trySorting(testCaseCopy);
            double disorder = disorderMeasure.getValue(testCaseCopy);
            totalFitness += 1 - Math.pow(2, disorder);
        }
        return totalFitness / testCases.size();
    }

    public boolean isCorrect (EvolvingSorting sorting) {
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
