package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public interface TreeNode {

    List<? extends TreeNode> getChildren ();
}
