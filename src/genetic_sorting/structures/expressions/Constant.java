package genetic_sorting.structures.expressions;

import genetic_sorting.operators.TreeNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Constant extends Terminal {

    // todo

    private int value;

    public Constant (int value) {
        this.value = value;
    }

    @Override
    public void init () { }

    @Override
    public int evaluate (List<Integer> list, int index) {
        return value;
    }

    @Override
    public void makeExecutable () {

    }

    @Override
    public Expression getElement () {
        return this;
    }

    @Override
    public List<? extends TreeNode<Expression>> getChildren () {
        return Collections.emptyList();
    }

    @Override
    public boolean replaceChild (Expression oldChild, Expression newChild) {
        return false;
    }

    @Override
    public String toString () {
        return "(" + value + ")";
    }

}
