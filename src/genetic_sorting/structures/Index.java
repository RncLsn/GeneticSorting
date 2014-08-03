package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Index implements Terminal {

    @Override
    public List<TreeNode> getChildren () {
        return null;
    }

    @Override
    public void init () { }

    @Override
    public int evaluate (List<Integer> list, int index) {
        return index;
    }

    @Override
    public String toString() {
        return "(index)";
    }
}
