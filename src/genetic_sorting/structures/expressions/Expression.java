package genetic_sorting.structures.expressions;

import genetic_sorting.operators.TreeNode;

import java.util.List;

/**
 * It is the interface for implementing classes that represents expressions of a tree program. As an
 * expression is recursive in its nature, every method call is recursively propagated to its
 * sub-expression until a terminal is reached.
 *
 * @author Alessandro Ronca
 */
public abstract class Expression extends TreeNode<Expression> implements Cloneable {

    /**
     * It must be invoked before every execution, i.e. evaluation of the root expression.
     */
    public abstract void init ();

    /**
     * It evaluates the expression, evaluating first its sub-expressions, according to a post-order
     * visit. Evaluating an expression means triggering the behaviour it was programmed for. In
     * particular it can perform some side-effect on the list, and at the end it returns a value.
     *
     * @param list  the list to be sorted.
     * @param index the current value of the index for iterating on the list.
     * @return a value according to the semantics of the specific class.
     */
    public abstract int evaluate (List<Integer> list, int index);

    /**
     * It must be invoked before using the evolved program on general inputs in order to obtain a
     * proper behaviour. <p></p> This methods, recursively propagated to all the nodes, makes the
     * expression executable. In other words it changes a program in evolution, to a program ready
     * to be executed on every kind of input.
     */
    public abstract void makeExecutable ();

    @Override
    public Object clone () throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
