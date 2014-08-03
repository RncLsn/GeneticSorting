package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Length implements Terminal {

    @Override
    public List<TreeNode> getChildren () {
        return null;
    }

    @Override
    public void init () { }

    @Override
    public int evaluate (List<Integer> list, int index) {
        return list.size();
    }

    @Override
    public String toString() {
        return "(length)";
    }
}
