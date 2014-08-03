package genetic_sorting.operators;

import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.Population;

import java.util.Collection;

/**
 * @author Alessandro Ronca
 */
public class UniformMutation implements Operator {

    @Override
    public Collection<EvolvingSorting> operate (Population population) {
        EvolvingSorting individual = population.randomSelection();
        return null;
    }
}
