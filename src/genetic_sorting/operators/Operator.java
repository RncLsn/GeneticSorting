package genetic_sorting.operators;

import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.Population;

import java.util.Collection;

/**
 * @author Alessandro Ronca
 */
public interface Operator {

    Collection<EvolvingSorting> operate (Population population);
}
