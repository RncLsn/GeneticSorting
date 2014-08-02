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
    public int evaluate (List<Integer> list) {
        return list.size();
    }

}
