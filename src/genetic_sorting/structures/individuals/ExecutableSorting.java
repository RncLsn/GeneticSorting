package genetic_sorting.structures.individuals;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class ExecutableSorting {

    private EvolvingSorting evolvingSorting;

    public ExecutableSorting (EvolvingSorting evolvingSorting) {
        try {
            this.evolvingSorting = (EvolvingSorting) evolvingSorting.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.evolvingSorting.getRootExpression().makeExecutable();
    }

    public void execute (List<Integer> list) {
        evolvingSorting.trySorting(list);
    }
}
