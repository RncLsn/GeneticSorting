package genetic_sorting.batch_gp;

import genetic_sorting.structures.individuals.EvolvingSorting;

import java.util.Set;

/**
 * @author Alessandro Ronca
 */
public class BatchGPResult {

    private final int                  generations;
    private final Set<EvolvingSorting> correctIndividuals;

    public BatchGPResult (Set<EvolvingSorting> correctIndividuals, int generations) {
        this.generations = generations;
        this.correctIndividuals = correctIndividuals;
    }

    public int getGenerations () {
        return generations;
    }

    public Set<EvolvingSorting> getCorrectIndividuals () {
        return correctIndividuals;
    }
}