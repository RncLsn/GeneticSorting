package genetic_sorting.operators;

/**
 * @author Alessandro Ronca
 */
public class UniformMutationFactory implements RandomOperatorsFactory {

    @Override
    public Operator makeOperator () {
        return new UniformMutation();
    }
}
