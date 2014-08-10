package genetic_sorting.structures.expressions;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public abstract class Function extends Expression {

    public abstract int numOfArgs ();
    public abstract void setArgs (List<Expression> args)
            throws WrongNumberOfArgsException, AlreadyInitializedException;
}
