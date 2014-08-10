package genetic_sorting.operators;

import genetic_sorting.structures.Population;
import genetic_sorting.structures.expressions.Expression;
import genetic_sorting.structures.factories.RandomFunctionFactory;
import genetic_sorting.structures.factories.RandomTerminalFactory;
import genetic_sorting.structures.individuals.EvolvingSorting;

import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class UniformMutation implements Operator {

    private static final double DEFAULT_MAX_EXPANSION    = 1.15;
    private static final double DEFAULT_LEAF_PROBABILITY = 0.1;

    private final RandomFunctionFactory randomFunctionFactory;
    private final RandomTerminalFactory randomTerminalFactory;
    private final double                affectedPopulationPct;
    private final double                maxExpansion;
    private final double                leafProbability;

    public UniformMutation (double affectedPopulationPct,
                            RandomFunctionFactory randomFunctionFactory,
                            RandomTerminalFactory randomTerminalFactory) {
        this(affectedPopulationPct, randomFunctionFactory, randomTerminalFactory,
             DEFAULT_MAX_EXPANSION, DEFAULT_LEAF_PROBABILITY);
    }

    public UniformMutation (double affectedPopulationPct,
                            RandomFunctionFactory randomFunctionFactory,
                            RandomTerminalFactory randomTerminalFactory,
                            double maxExpansion, double leafProbability) {
        this.affectedPopulationPct = affectedPopulationPct;
        this.randomFunctionFactory = randomFunctionFactory;
        this.randomTerminalFactory = randomTerminalFactory;
        this.maxExpansion = maxExpansion;
        this.leafProbability = leafProbability;
    }

    @Override
    public Collection<EvolvingSorting> operate (Population population) {

        List<Collection<EvolvingSorting>> partitions = null;
        try {
            partitions = ((Population) population.clone()).randomPartition(
                    Arrays.asList(affectedPopulationPct, 1 - affectedPopulationPct));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        for (EvolvingSorting individual : partitions.get(0)) {

            TreeNode<Expression> root = individual.getRootExpression();
//            TreeNode<Expression> randomNode = root.randomSubtree();
            TreeNode<Expression> randomNode = root.randomSubtree(leafProbability); // todo test
            TreeNode<Expression> father = root.fatherOf(randomNode);

            int rootHeight = root.height();
            int nodeDepth = root.depthOf(randomNode);

            Random rand = new Random();
            int maxSubtreeHeight = 1 +
                                   rand.nextInt((int) (maxExpansion * rootHeight - nodeDepth + 1));

            Expression newSubtree = EvolvingSorting.generateRandomExpression(maxSubtreeHeight,
                                                                             randomFunctionFactory,
                                                                             randomTerminalFactory);
            if (father == null) {
                individual.setRootExpression(newSubtree);
            } else {
                father.replaceChild(randomNode.getElement(), newSubtree);
            }
        }

        // merge partitions
        Collection<EvolvingSorting> allPopulation = new ArrayList<>();
        allPopulation.addAll(partitions.get(0));
        allPopulation.addAll(partitions.get(1));

        return allPopulation;
    }
}
