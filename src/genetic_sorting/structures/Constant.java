package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Constant implements Terminal {

    private final int value;

    public Constant (int value) {
        this.value = value;
    }

    @Override
    public int evaluate (List<Integer> list) {
        return value;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return null;
    }
}
