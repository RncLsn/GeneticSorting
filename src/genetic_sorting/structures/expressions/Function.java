package genetic_sorting.structures.expressions;

import java.util.List;

/**
 * A function is an expression that recursively takes other expression as parameters.
 *
 * @author Alessandro Ronca
 */
public abstract class Function extends Expression {

    /**
     * @return the number of arguments.
     */
    public abstract int numOfArgs ();

    /**
     * It allows to set the parameters of this function. The number of parameters is specified by
     * the class implementing the interface through the method <tt>numOfArgs()</tt>. <p></p> It must
     * be invoked exactly once during the building of the expression, before every evaluation.
     *
     * @param args
     * @throws WrongNumberOfArgsException
     * @throws AlreadyInitializedException
     */
    public abstract void setArgs (List<Expression> args)
            throws WrongNumberOfArgsException, AlreadyInitializedException;
}
