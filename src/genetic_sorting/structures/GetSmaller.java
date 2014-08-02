package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class GetSmaller implements Function {

    private Expression x;
    private Expression y;

    public GetSmaller (Expression x, Expression y) {
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

        return (list.get(xValue) < list.get(yValue)) ? xValue : yValue;
    }

}
