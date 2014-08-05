package genetic_sorting.structures;

import genetic_sorting.operators.TreeNode;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public abstract class Expression extends TreeNode<Expression> implements Cloneable {

    abstract void init ();
    abstract int evaluate (List<Integer> list, int index);

    @Override
    public Object clone () throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
