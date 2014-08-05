package genetic_sorting.structures;

import genetic_sorting.operators.TreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class GetSmaller extends Function {

    private static final int NUM_ARGS = 2;

    private Expression x;
    private Expression y;

    public GetSmaller () {}

    public GetSmaller (Expression x, Expression y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void init () {
        x.init();
        y.init();
    }

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

        return (list.get(xValue) < list.get(yValue)) ? xValue : yValue;
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return new GetBigger((Expression) x.clone(), (Expression) y.clone());
    }

    @Override
    public int numOfArgs () {
        return NUM_ARGS;
    }

    @Override
    public void setArgs (List<Expression> args)
            throws AlreadyInitializedException, WrongNumberOfArgsException {
        if (x != null || y != null) {
            throw new AlreadyInitializedException();
        }
        if (args.size() != NUM_ARGS) {
            throw new WrongNumberOfArgsException(args.size(), NUM_ARGS);
        }
        x = args.get(0);
        y = args.get(1);
    }

    @Override
    public Expression getElement () {
        return this;
    }

    @Override
    public List<? extends TreeNode<Expression>> getChildren () {
        return Arrays.asList(x, y);
    }

    @Override
    public boolean replaceChild (Expression oldChild, Expression newChild) {
        if (x.equals(oldChild)) {
            x = newChild;
            return true;
        }
        if (y.equals(oldChild)) {
            y = newChild;
            return true;
        }
        return false;
    }

    @Override
    public String toString () {
        return "(getsmaller " + x + " " + y + ")";
    }

}
