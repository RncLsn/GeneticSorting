package genetic_sorting.structures.expressions;

import genetic_sorting.operators.TreeNode;

import java.util.Collections;
import java.util.List;

/**
 * This expression is a terminal. It evaluates to the value of the looping index of its closest
 * <tt>Iterate</tt> ancestor. If there isn't an <tt>Iterate</tt> ancestor then the value is 0.
 *
 * @author Alessandro Ronca
 */
public class Index extends Terminal {

    @Override
    public void init () { }

    @Override
    public int evaluate (List<Integer> list, int index) {
        return index;
    }

    @Override
    public void makeExecutable () {

    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return new Index();
    }

    @Override
    public Expression getElement () {
        return this;
    }

    @Override
    public List<TreeNode<Expression>> getChildren () {
        return Collections.emptyList();
    }

    @Override
    public boolean replaceChild (Expression oldChild, Expression newChild) {
        return false;
    }

    @Override
    public String toString () {
        return "(index)";
    }

}
