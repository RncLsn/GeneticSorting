package genetic_sorting.structures;

/**
 * @author Alessandro Ronca
 */
public class WrongNumberOfArgsException extends Exception {

    public WrongNumberOfArgsException(int passed, int taken) {
        super("Passed " + passed + " args instead of " + taken);
    }
}
