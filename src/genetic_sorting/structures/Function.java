package genetic_sorting.structures;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public interface Function extends Expression {
    int numOfArgs();
    void setArgs(List<Expression> args)
            throws WrongNumberOfArgsException, AlreadyInitializedException;
}
