package genetic_sorting.structures.expressions;


import genetic_sorting.operators.TreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Iterate extends Function {

    public static final  int MAX_ITERATIONS_PER_CALL = 200;
    public static final  int MAX_ITERATIONS_PER_TEST = 2000;
    private static final int NUM_ARGS                = 3;

    private int iterationsPerTest = 0;

    private Expression x;
    private Expression y;
    private Expression z;
    private boolean    executable;

    public Iterate () {}

    public Iterate (Expression x, Expression y, Expression z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.executable = false;
    }

    @Override
    public void init () {
        iterationsPerTest = 0;
        x.init();
        y.init();
        z.init();
    }

    @Override
    public int evaluate (List<Integer> list, int index) {

        int start = x.evaluate(list, index);
        int end = y.evaluate(list, index);

        // todo test start
        for (int i = start;
             i < end && i < list.size() &&
             (executable ||
              ((i - start) < MAX_ITERATIONS_PER_CALL &&
               iterationsPerTest < MAX_ITERATIONS_PER_TEST));
             i++, iterationsPerTest++) {
            z.evaluate(list, i);
        }

        return Math.min(end, list.size());
    }

    @Override
    public void makeExecutable () {
        this.executable = true;
        x.makeExecutable();
        y.makeExecutable();
        z.makeExecutable();
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return new Iterate((Expression) x.clone(), (Expression) y.clone(), (Expression) z.clone());
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
        z = args.get(2);
    }

    @Override
    public Expression getElement () {
        return this;
    }

    @Override
    public List<? extends TreeNode<Expression>> getChildren () {
        return Arrays.asList(x, y, z);
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
        if (z.equals(oldChild)) {
            z = newChild;
            return true;
        }
        return false;
    }

    @Override
    public String toString () {
        return "(iterate " + x + " " + y + " " + z + ")";
    }

}
