package genetic_sorting.operators;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public interface TreeNode {

    List<? extends TreeNode> getChildren ();
}
