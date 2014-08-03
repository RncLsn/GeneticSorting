package genetic_sorting.structures;

import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class RandomTerminalFactory {

    public Terminal makeTerminal() {
        Random rand = new Random();
        int r = rand.nextInt(2);
        switch (r) {
            case 0:
                return new Index();
            case 1:
                return new Length();
            default:
                return null;
        }
    }
}
