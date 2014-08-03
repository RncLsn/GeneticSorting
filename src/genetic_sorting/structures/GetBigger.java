package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class GetBigger implements Function {

    private static final int NUM_ARGS = 2;

    private Expression x;
    private Expression y;

    public GetBigger () {}

    public GetBigger (Expression x, Expression y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x, y);
    }

    @Override
    public void init () { }

    @Override
    public int evaluate (List<Integer> list, int index) {
        int xValue = x.evaluate(list, index);
        int yValue = y.evaluate(list, index);

        if (xValue < 0 || xValue >= list.size()) {
            return 0;
        }
        if (yValue < 0 || yValue >= list.size()) {
            return 0;
        }

        return (list.get(xValue) > list.get(yValue)) ? xValue : yValue;
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
    }

    @Override
    public String toString () {
        return "(getbigger " + x + " " + y + ")";
    }
}
