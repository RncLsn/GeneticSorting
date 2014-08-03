package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Iterate implements Function {

    private static final int ITERATIONS_PER_CALL = 200;
    private static final int ITERATIONS_PER_TEST = 2000;
    private static final int NUM_ARGS            = 3;

    private int iterationsPerTest = 0;

    private Expression x;
    private Expression y;
    private Expression z;

    public Iterate () {}

    public Iterate (Expression x, Expression y, Expression z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x, y, z);
    }

    @Override
    public void init () {
        iterationsPerTest = 0;
    }

    @Override
    public int evaluate (List<Integer> list, int index) {

        int start = x.evaluate(list, index);
        int end = y.evaluate(list, index);

        for (int i = 0;
             i < end && i < list.size() &&
             i < ITERATIONS_PER_CALL && iterationsPerTest < ITERATIONS_PER_TEST;
             i++, iterationsPerTest++) {
            z.evaluate(list, i);
        }

        return Math.min(end, list.size());
    }

    @Override
    public int numOfArgs () {
        return NUM_ARGS;
    }

    @Override
    public void setArgs (List<Expression> args)
            throws AlreadyInitializedException, WrongNumberOfArgsException {
        if(x != null || y != null) throw new AlreadyInitializedException();
        if(args.size() != NUM_ARGS) throw new WrongNumberOfArgsException(args.size(), NUM_ARGS);
        x = args.get(0);
        y = args.get(1);
        z = args.get(2);
    }

    @Override
    public String toString () {
        return "(iterate " + x + " " + y + " " + z + ")";
    }
}
