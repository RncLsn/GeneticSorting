package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Subtract implements Function {

    private final Expression x;
    private final Expression y;

    public Subtract (Expression x, Expression y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int evaluate (List<Integer> list) {
        return x.evaluate(list) - y.evaluate(list);
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x, y);
    }

}
