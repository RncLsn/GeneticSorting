package genetic_sorting.structures;

import genetic_sorting.operators.TreeNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Length extends Terminal {

    @Override
    public void init () { }

    @Override
    public int evaluate (List<Integer> list, int index) {
        return list.size();
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return new Length();
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
        return "(length)";
    }
}
