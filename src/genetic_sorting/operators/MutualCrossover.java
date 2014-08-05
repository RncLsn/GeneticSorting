package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.Expression;
import genetic_sorting.structures.Population;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Alessandro Ronca
 */
public class MutualCrossover implements Operator {

    private final Evaluation evaluation;
    private final int maxDepth;

    public MutualCrossover (Evaluation evaluation, int maxHeight) {
        this.evaluation = evaluation;
        this.maxDepth = maxHeight;
    }

    @Override
    public Collection<EvolvingSorting> operate (Population population) {
        EvolvingSorting firstIndividual = null;
        EvolvingSorting secondIndividual = null;
        try {
            firstIndividual
                    = (EvolvingSorting) population.fitnessProportionalSelection(evaluation).clone();
            secondIndividual = (EvolvingSorting) population.fitnessProportionalSelection(evaluation)
                                                           .clone();

            Expression firstRoot = firstIndividual.getRootExpression();
            Expression secondRoot = secondIndividual.getRootExpression();

            Expression firstSubtree;
            Expression secondSubtree;
            do {
                firstSubtree = (Expression) firstRoot.randomSubtree();
                secondSubtree = (Expression) secondRoot.randomSubtree();
            } while (firstRoot.depthOf(firstSubtree) + secondSubtree.height() > maxDepth ||
                     secondRoot.depthOf(secondSubtree) + firstSubtree.height() > maxDepth);

            TreeNode firstFather = firstRoot.fatherOf(firstSubtree);
            if (firstFather == null) {
                firstIndividual.setRootExpression(secondSubtree);
            } else {
                firstFather.replaceChild(firstSubtree, secondSubtree);
            }

            TreeNode secondFather = secondRoot.fatherOf(firstSubtree);
            if (secondFather == null) {
                secondIndividual.setRootExpression(firstSubtree);
            } else {
                secondFather.replaceChild(secondSubtree, firstSubtree);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return Arrays.asList(firstIndividual, secondIndividual);
    }
}
