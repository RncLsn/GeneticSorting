package genetic_sorting.structures;

import genetic_sorting.operators.TreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Increment implements Function {

    private static final int NUM_ARGS = 1;
    private Expression x;

    public Increment () {}

    public Increment (Expression x) {
        this.x = x;
    }

    @Override
    public void init () { }

    @Override
    public int evaluate (List<Integer> list, int index) {
        return x.evaluate(list, index) + 1;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x);
    }

    @Override
    public int numOfArgs () {
        return NUM_ARGS;
    }

    @Override
    public void setArgs (List<Expression> args)
            throws AlreadyInitializedException, WrongNumberOfArgsException {
        if (x != null) {
            throw new AlreadyInitializedException();
        }
        if (args.size() != NUM_ARGS) {
            throw new WrongNumberOfArgsException(args.size(), NUM_ARGS);
        }
        x = args.get(0);
    }

    @Override
    public String toString () {
        return "(increment " + x + ")";
    }

}
