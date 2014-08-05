package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public abstract class Function extends Expression {

    abstract int numOfArgs ();
    abstract void setArgs (List<Expression> args)
            throws WrongNumberOfArgsException, AlreadyInitializedException;
}
