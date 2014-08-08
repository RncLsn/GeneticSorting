package genetic_sorting.batch_gp;

/**
 * @author Alessandro Ronca
 */
public class BatchParameters {

    // from Kinnear's paper
    public static final int    DEFAULT_INITIAL_POPULATION  = 1000;
    public static final int    DEFAULT_MAX_TREE_DEPTH      = 6;
    public static final int    DEFAULT_TEST_CASES          = 15;
    public static final int    DEFAULT_MAX_LIST_LENGTH     = 30;
    public static final int    DEFAULT_MAX_LIST_VALUE      = 60;
    public static final double DEFAULT_MUTATION_PCT        = 0.1;
    public static final double DEFAULT_CROSSOVER_PCT       = 0.9;
    public static final double DEFAULT_CROSSOVER_EXPANSION = 1.8;
    public static final int    DEFAULT_GENERATIONS         = 50;

    private final int    initialPopulation;
    private final int    maxTreeDepth;
    private final int    testCases;
    private final int    maxListLength;
    private final int    maxListValue;
    private final double mutationPct;
    private final double crossoverPct;
    private final double crossoverExpansion;
    private final int    generations;

    public BatchParameters () {
        initialPopulation = DEFAULT_INITIAL_POPULATION;
        maxTreeDepth = DEFAULT_MAX_TREE_DEPTH;
        testCases = DEFAULT_TEST_CASES;
        maxListLength = DEFAULT_MAX_LIST_LENGTH;
        maxListValue = DEFAULT_MAX_LIST_VALUE;
        mutationPct = DEFAULT_MUTATION_PCT;
        crossoverPct = DEFAULT_CROSSOVER_PCT;
        crossoverExpansion = DEFAULT_CROSSOVER_EXPANSION;
        generations = DEFAULT_GENERATIONS;
    }

    public BatchParameters (int initialPopulation, int maxTreeDepth, int testCases,
                            int maxListLength,
                            int maxListValue, double mutationPct, double crossoverPct,
                            double crossoverExpansion, int generations) {
        this.initialPopulation = initialPopulation;
        this.maxTreeDepth = maxTreeDepth;
        this.testCases = testCases;
        this.maxListLength = maxListLength;
        this.maxListValue = maxListValue;
        this.mutationPct = mutationPct;
        this.crossoverPct = crossoverPct;
        this.crossoverExpansion = crossoverExpansion;
        this.generations = generations;
    }

    public int getInitialPopulation () {
        return initialPopulation;
    }

    public int getMaxTreeDepth () {
        return maxTreeDepth;
    }

    public int getTestCases () {
        return testCases;
    }

    public int getMaxListLength () {
        return maxListLength;
    }

    public int getMaxListValue () {
        return maxListValue;
    }

    public double getMutationPct () {
        return mutationPct;
    }

    public double getCrossoverPct () {
        return crossoverPct;
    }

    public double getCrossoverExpansion () {
        return crossoverExpansion;
    }

    public int getGenerations () {
        return generations;
    }

    @Override
    public String toString () {
        return "BatchParameters{" +
               "initialPopulation=" + initialPopulation +
               ", maxTreeDepth=" + maxTreeDepth +
               ", testCases=" + testCases +
               ", maxListLength=" + maxListLength +
               ", maxListValue=" + maxListValue +
               ", mutationPct=" + mutationPct +
               ", crossoverPct=" + crossoverPct +
               ", crossoverExpansion=" + crossoverExpansion +
               ", generations=" + generations +
               '}';
    }

}
