package genetic_sorting.operators;

import genetic_sorting.util.Balance;
import genetic_sorting.util.MyCollections;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author Alessandro Ronca
 */
public abstract class TreeNode<T> implements Iterable<TreeNode<T>> {

    public abstract T getElement ();

    public boolean isLeaf () {
        return getChildren() == null || getChildren().size() == 0;
    }

    public abstract List<? extends TreeNode<T>> getChildren ();

    public TreeNode<T> randomSubtree () {
        return MyCollections.randomSelection(this);
    }

    public TreeNode<T> randomSubtree (double leafProbability) {
        return MyCollections.weightedRandomSelection(this, new TreeNodeBalance<T>(leafProbability));
    }

    public int height () {
        if (getChildren().isEmpty()) {
            return 0;
        }

        int maxDepth = 0;
        for (TreeNode<T> child : getChildren()) {
            maxDepth = Math.max(maxDepth, child.height());
        }
        return maxDepth + 1;
    }

    /**
     * @param treeNode
     * @return -1 if the tree doesn't contain the node, the depth otherwise.
     */
    public int depthOf (TreeNode<T> treeNode) {
        int depthOf = auxDepthOf(treeNode, 0);
        if (depthOf == 0 && !equals(treeNode)) {
            return -1;
        } else {
            return depthOf;
        }
    }

    public int auxDepthOf (TreeNode<T> treeNode, int depth) {
        if (equals(treeNode)) {
            return depth;
        }
        int depthOf = 0;
        for (TreeNode<T> child : getChildren()) {
            depthOf = Math.max(depthOf, child.auxDepthOf(treeNode, depth + 1));
        }
        return depthOf;
    }

    public int size () {
        if (getChildren().isEmpty()) {
            return 1;
        }

        int size = 0;
        for (TreeNode<T> child : getChildren()) {
            size += child.size();
        }
        return size + 1;
    }

    public TreeNode<T> fatherOf (TreeNode node) {
        for (TreeNode<T> candidateFather : this) {
            if (candidateFather.getChildren().contains(node)) {
                return candidateFather;
            }
        }
        return null;
    }

    public abstract boolean replaceChild (T oldChild, T newChild);

    @Override
    public Iterator<TreeNode<T>> iterator () {
        return new TreeIterator<>(this);
    }

    private static class TreeNodeBalance<V> implements Balance<TreeNode<V>> {

        private final double leafProbability;

        public TreeNodeBalance (double leafProbability) {
            this.leafProbability = leafProbability;
        }

        @Override
        public double weigh (TreeNode<V> tTreeNode) {
            if (tTreeNode.isLeaf()) {
                return leafProbability;
            } else {
                return 1 - leafProbability;
            }
        }
    }

    private class TreeIterator<E> implements Iterator<TreeNode<E>> {

        private final Stack<TreeNode<E>> stack;

        private TreeNode<E> next;

        public TreeIterator (TreeNode<E> root) {
            stack = new Stack<>();
            stack.push(root);
        }

        @Override
        public boolean hasNext () {
            if (next != null) {
                return true;
            }
            if (stack.isEmpty()) {
                return false;
            }

            next = stack.pop();
            for (TreeNode<E> children : next.getChildren()) {
                stack.push(children);
            }
            return true;
        }

        @Override
        public TreeNode<E> next () {
            if (hasNext()) {
                TreeNode<E> tmpNext = next;
                next = null;
                return tmpNext;
            }
            return null;
        }

        @Override
        public void remove () {
            throw new UnsupportedOperationException();
        }
    }
}
