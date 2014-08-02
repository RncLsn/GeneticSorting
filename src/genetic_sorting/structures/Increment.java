package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Increment implements Function {

    private final Expression x;

    public Increment (Expression x) {
        this.x = x;
    }

    @Override
    public int evaluate (List<Integer> list) {
        return x.evaluate(list) + 1;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x);
    }

}
