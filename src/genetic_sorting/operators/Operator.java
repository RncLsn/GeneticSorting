package genetic_sorting.operators;

import genetic_sorting.structures.Population;
import genetic_sorting.structures.individuals.EvolvingSorting;

import java.util.Collection;

/**
 * @author Alessandro Ronca
 */
public interface Operator {

    Collection<EvolvingSorting> operate (Population population) throws OperatorException;
}
