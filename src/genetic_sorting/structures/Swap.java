package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Swap implements Function {

    private final Expression x;
    private final Expression y;

    public Swap (Expression x, Expression y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x, y);
    }

    @Override
    public int evaluate (List<Integer> list) {
        int xValue = x.evaluate(list);
        int yValue = y.evaluate(list);

        if (xValue < 0 || xValue >= list.size()) {
            return 0;
        }
        if (yValue < 0 || yValue >= list.size()) {
            return 0;
        }

        int tmp = list.get(xValue);
        list.set(xValue, list.get(yValue));
        list.set(yValue, tmp);

        return xValue;
    }
}
