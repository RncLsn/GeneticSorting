package genetic_sorting.operators;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.structures.Population;
import genetic_sorting.structures.expressions.Expression;
import genetic_sorting.structures.individuals.EvolvingSorting;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Alessandro Ronca
 */
public class MutualCrossover implements Operator {

    private static final int DEFAULT_MAX_TRIES                 = 100;
    private static final int DEFAULT_MAX_INDIVIDUAL_SELECTIONS = 5;
    private final Evaluation evaluation;
    private final int        maxHeight;
    private final int        maxTries;
    private final int        maxIndividualSelections;


    /**
     * @param evaluation is used for fitness proportional selection.
     * @param maxHeight
     */
    public MutualCrossover (Evaluation evaluation, int maxHeight) {
        this(evaluation, maxHeight, DEFAULT_MAX_TRIES, DEFAULT_MAX_INDIVIDUAL_SELECTIONS);
    }

    /**
     * @param maxHeight
     * @param maxTries
     * @param maxIndividualSelections
     */
    public MutualCrossover (Evaluation evaluation, int maxHeight, int maxTries,
                            int maxIndividualSelections) {
        this.evaluation = evaluation;
        this.maxHeight = maxHeight;
        this.maxTries = maxTries;
        this.maxIndividualSelections = maxIndividualSelections;
    }

    @Override
    public Collection<EvolvingSorting> operate (Population population) throws CrossoverException {
        EvolvingSorting firstIndividual = null;
        EvolvingSorting secondIndividual = null;
        try {
            Expression firstRoot;
            Expression secondRoot;
            Expression firstSelectedSubtree;
            Expression secondSelectedSubtree;
            int tries;
            int individualSelections = 0;
            do {
                if (individualSelections > maxIndividualSelections) {
                    throw new CrossoverException("Too much selections without success");
                }
                individualSelections++;

                firstIndividual
                        = (EvolvingSorting) population.fitnessProportionalSelection(evaluation)
                                                      .clone();
                secondIndividual =
                        (EvolvingSorting) population.fitnessProportionalSelection(evaluation)
                                                    .clone();

                firstRoot = firstIndividual.getRootExpression();
                secondRoot = secondIndividual.getRootExpression();

                tries = 0;
                do {
                    firstSelectedSubtree = (Expression) firstRoot.randomSubtree();
                    secondSelectedSubtree = (Expression) secondRoot.randomSubtree();
                } while (tries++ < maxTries &&
                         firstRoot.depthOf(firstSelectedSubtree) + secondSelectedSubtree.height() >
                         maxHeight ||
                         secondRoot.depthOf(secondSelectedSubtree) + firstSelectedSubtree.height() >
                         maxHeight);
            } while (tries > maxTries);

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
