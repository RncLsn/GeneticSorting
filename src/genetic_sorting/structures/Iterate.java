package genetic_sorting.structures;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class Iterate implements Function {

    private static final int ITERATIONS_PER_CALL = 200;
    private static final int ITERATIONS_PER_TEST = 2000;

    private int iterationsPerTest = 0;

    private final Expression x;
    private final Expression y;
    private final Expression z;
    private final Index      index;

    public Iterate (Expression x, Expression y, Expression z, Index index) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.index = index;
    }

    @Override
    public List<? extends TreeNode> getChildren () {
        return Arrays.asList(x, y, z);
    }

    @Override
    public int evaluate (List<Integer> list) {

        int start = x.evaluate(list);
        int end = y.evaluate(list);

        index.initialize(start);
        for (int i = 0;
             index.getValue() < end && index.getValue() < list.size() &&
             i < ITERATIONS_PER_CALL &&
             iterationsPerTest < ITERATIONS_PER_TEST;
             index.increment(), i++, iterationsPerTest++) {
            z.evaluate(list);
        }

        return Math.min(end, list.size());
    }
}
