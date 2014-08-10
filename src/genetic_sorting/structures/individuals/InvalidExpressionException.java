package genetic_sorting.structures.individuals;

/**
 * @author Alessandro Ronca
 */
public class InvalidExpressionException extends Exception {

    public InvalidExpressionException () { }

    public InvalidExpressionException (String msg) {
        super(msg);
    }
}
