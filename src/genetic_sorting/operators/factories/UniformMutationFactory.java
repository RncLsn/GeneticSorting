package genetic_sorting.operators.factories;

import genetic_sorting.operators.Operator;
import genetic_sorting.operators.UniformMutation;
import genetic_sorting.structures.factories.RandomFunctionFactory;
import genetic_sorting.structures.factories.RandomTerminalFactory;

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
