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
    private final int        maxDepth;

    /**
     * @param evaluation is used for fitness proportional selection.
     * @param maxHeight
     */
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
            secondIndividual =
                    (EvolvingSorting) population.fitnessProportionalSelection(evaluation).clone();

//            System.out.println("selected individuals");
//            System.out.println(firstIndividual);
//            System.out.println(secondIndividual);

            Expression firstRoot = firstIndividual.getRootExpression();
            Expression secondRoot = secondIndividual.getRootExpression();

            Expression firstSelectedSubtree;
            Expression secondSelectedSubtree;
            do {
                firstSelectedSubtree = (Expression) firstRoot.randomSubtree();
                secondSelectedSubtree = (Expression) secondRoot.randomSubtree();
            } while (firstRoot.depthOf(firstSelectedSubtree) + secondSelectedSubtree.height() >
                     maxDepth ||
                     secondRoot.depthOf(secondSelectedSubtree) + firstSelectedSubtree.height() >
                     maxDepth);

//            System.out.println("selected subtrees");
//            System.out.println(firstSelectedSubtree);
//            System.out.println(secondSelectedSubtree);

            TreeNode firstFather = firstRoot.fatherOf(firstSelectedSubtree);
            if (firstFather == null) {
                firstIndividual.setRootExpression(secondSelectedSubtree);
            } else {
                firstFather.replaceChild(firstSelectedSubtree, secondSelectedSubtree);
            }

            TreeNode secondFather = secondRoot.fatherOf(secondSelectedSubtree);
            if (secondFather == null) {
                secondIndividual.setRootExpression(firstSelectedSubtree);
            } else {
                secondFather.replaceChild(secondSelectedSubtree, firstSelectedSubtree);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return Arrays.asList(firstIndividual, secondIndividual);
    }
}
