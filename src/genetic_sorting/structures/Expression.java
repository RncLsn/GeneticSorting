package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public interface Expression extends TreeNode {

    void init();
    int evaluate (List<Integer> list, int index);

}
