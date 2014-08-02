package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Index implements Terminal {

    private int value;

    public Index () {
        this.value = 0;
    }

    @Override
    public List<TreeNode> getChildren () {
        return null;
    }

    @Override
    public int evaluate (List<Integer> list) {
        return value;
    }

    public int increment() {
        return value++;
    }

    public void initialize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
