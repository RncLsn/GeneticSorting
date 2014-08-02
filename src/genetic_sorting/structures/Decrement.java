package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Decrement implements Function {

    private Expression x;

    public Decrement (Expression x) {
        this.x = x;
    }

    @Override
    public int evaluate (List<Integer> list) {
        return x.evaluate(list) - 1;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x);
    }

}
