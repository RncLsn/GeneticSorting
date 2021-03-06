package genetic_sorting.structures.expressions;

import genetic_sorting.operators.TreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * It represents the expression "(increment e)". It evaluates to the value of its argument plus 1.
 *
 * @author Alessandro Ronca
 */
public class Increment extends Function {

    private static final int NUM_ARGS = 1;
    private Expression x;

    public Increment () {
    }

    public Increment (Expression x) {
        this.x = x;
    }

    @Override
    public void init () {
        x.init();
    }

    @Override
    public int evaluate (List<Integer> list, int index) {
        return x.evaluate(list, index) + 1;
    }

    @Override
    public void makeExecutable () {
        x.makeExecutable();
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return new Increment((Expression) x.clone());
    }

    @Override
    public Expression getElement () {
        return this;
    }

    @Override
    public List<? extends TreeNode<Expression>> getChildren () {
        return Arrays.asList(x);
    }

    @Override
    public boolean replaceChild (Expression oldChild, Expression newChild) {
        if (x.equals(oldChild)) {
            x = newChild;
            return true;
        }
        return false;
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
