package genetic_sorting.structures.expressions;

import genetic_sorting.operators.TreeNode;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public abstract class Expression extends TreeNode<Expression> implements Cloneable {

    public abstract void init ();
    public abstract int evaluate (List<Integer> list, int index);
    public abstract void makeExecutable ();

    @Override
    public Object clone () throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
