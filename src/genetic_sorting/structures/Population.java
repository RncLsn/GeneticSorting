package genetic_sorting.structures;

import genetic_sorting.evaluation.Evaluation;
import genetic_sorting.operators.RandomOperatorsFactory;
import genetic_sorting.util.Balance;
import genetic_sorting.util.MyArrays;
import genetic_sorting.util.MyCollections;

import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class Population {

    private static final int MAX_DEPTH = 6;
    Collection<EvolvingSorting> individuals;

    private Population () {
        individuals = new ArrayList<>();
    }

    public Population (int size, RandomFunctionFactory functionFactory,
                       RandomTerminalFactory terminalFactory) {
        this(size, MAX_DEPTH, functionFactory, terminalFactory);
    }

    public Population (int size, int maxDepth,
                       RandomFunctionFactory functionFactory,
                       RandomTerminalFactory terminalFactory) {
        individuals = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            individuals.add(
                    EvolvingSorting.generateFull(functionFactory, terminalFactory, maxDepth));
        }
    }

    public EvolvingSorting fitnessProportionalSelection (Evaluation evaluation) {
        return MyCollections.weightedRandomSelection(individuals,
                                                     new EvolvingSortingBalance(evaluation));
    }

    public EvolvingSorting randomSelection () {
        Random rand = new Random();
//        while (true) {
//            for (EvolvingSorting individual : individuals) {
//                double p = 1.0 / individuals.size();
//                if (rand.nextDouble() < p) {
//                    return individual;
//                }
//            }
//        }
        int position = rand.nextInt(individuals.size());
        for (EvolvingSorting individual : individuals) {
            if (position == 0) {
                return individual;
            }
            position--;
        }

        // it never happens
        return null;
    }

    public Set<EvolvingSorting> getCorrectIndividuals (Evaluation evaluation) {
        HashSet<EvolvingSorting> correctIndividuals = new HashSet<>();
        for (EvolvingSorting individual : individuals) {
            if (evaluation.isCorrect(individual)) {
                correctIndividuals.add(individual);
            }
        }
        return correctIndividuals;
    }

    public Population evolve (RandomOperatorsFactory operatorsFactory) {
        Population nextGeneration = new Population();
        while (nextGeneration.size() < this.size()) {
            Collection<EvolvingSorting> newIndividuals =
                    operatorsFactory.makeOperator().operate(this);
            nextGeneration.individuals.addAll(newIndividuals);
        }
        return nextGeneration;
    }

    public int size () {
        return individuals.size();
    }

    public List<Collection<EvolvingSorting>> randomPartition (List<Double> probabilities) {
        ArrayList<Collection<EvolvingSorting>> populationList = new ArrayList<>();
        for (int i = 0; i < probabilities.size(); i++) {
            populationList.add(new ArrayList<EvolvingSorting>());
        }

        for (EvolvingSorting individual : individuals) {
            int randomIndex =
                    MyArrays.indexWeightedRandomSelection(probabilities, new DoubleBalance());
            populationList.get(randomIndex).add(individual);

        }

        return populationList;
    }

    private static class DoubleBalance implements Balance<Double> {

        @Override
        public double weigh (Double aDouble) {
            return aDouble;
        }
    }
}
