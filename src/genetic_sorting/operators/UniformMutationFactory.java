package genetic_sorting.operators;

import genetic_sorting.structures.RandomFunctionFactory;
import genetic_sorting.structures.RandomTerminalFactory;

/**
 * @author Alessandro Ronca
 */
public class UniformMutationFactory implements RandomOperatorsFactory {

    private final double                pctPopulation;
    private final RandomFunctionFactory randomFunctionFactory;
    private final RandomTerminalFactory randomTerminalFactory;

    public UniformMutationFactory (double pctPopulation,
                                   RandomFunctionFactory randomFunctionFactory,
                                   RandomTerminalFactory randomTerminalFactory) {
        this.pctPopulation = pctPopulation;
        this.randomFunctionFactory = randomFunctionFactory;
        this.randomTerminalFactory = randomTerminalFactory;
    }

    @Override
    public Operator makeOperator () {
        return new UniformMutation(pctPopulation, randomFunctionFactory, randomTerminalFactory);
    }
}
