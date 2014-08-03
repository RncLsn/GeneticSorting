package genetic_sorting.structures;

import genetic_sorting.operators.TreeNode;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Constant implements Terminal {

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
    public List<? extends TreeNode> getChildren () {
        return null;
    }

    @Override
    public String toString () {
        return "(" + value + ")";
    }
}
