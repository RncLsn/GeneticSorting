package genetic_sorting.operators;

import genetic_sorting.structures.*;

import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class UniformMutation implements Operator {

    private final RandomFunctionFactory randomFunctionFactory;
    private final RandomTerminalFactory randomTerminalFactory;
    private final double                affectedPopulationPct;

    public UniformMutation (double affectedPopulationPct,
                            RandomFunctionFactory randomFunctionFactory,
                            RandomTerminalFactory randomTerminalFactory) {
        this.affectedPopulationPct = affectedPopulationPct;
        this.randomFunctionFactory = randomFunctionFactory;
        this.randomTerminalFactory = randomTerminalFactory;
    }

    @Override
    public Collection<EvolvingSorting> operate (Population population) {

        // todo apply to the whole population according to the input percentage
        List<Collection<EvolvingSorting>> partitions =
                population.randomPartition(
                        Arrays.asList(affectedPopulationPct, 1 - affectedPopulationPct));

        for (EvolvingSorting individual : partitions.get(0)) {

            TreeNode<Expression> root = individual.getRootExpression();
            TreeNode<Expression> randomNode = root.randomSubtree();
            TreeNode<Expression> father = root.fatherOf(randomNode);

            int rootHeight = root.height();
            int nodeDepth = root.depthOf(randomNode);

            Random rand = new Random();
            int maxSubtreeHeight = 1 + rand.nextInt((int) (1.15 * rootHeight - nodeDepth + 1));

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
