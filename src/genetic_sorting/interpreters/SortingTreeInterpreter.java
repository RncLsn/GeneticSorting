package genetic_sorting.interpreters;

import genetic_sorting.structures.individuals.EvolvingSorting;
import genetic_sorting.structures.individuals.ExecutableSorting;
import genetic_sorting.structures.individuals.InvalidExpressionException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Alessandro Ronca
 */
class SortingTreeInterpreter {

    public static void main (String[] args) {

        if (args.length < 1) {
            System.out.println("usage: SortingTreeInterpreter <sortingTree-file>");
        }

        try {
            // load program
            EvolvingSorting sorting = EvolvingSorting.generateFromEncoding(Paths.get(args[0]));

            // load input list
            List<Integer> list = new ArrayList<>();

            Scanner scanner = new Scanner(System.in);
            String listStr = scanner.nextLine();
            Scanner listScanner = new Scanner(listStr);
            while (listScanner.hasNext()) {
                list.add(listScanner.nextInt());
            }

            // execute
            new ExecutableSorting(sorting).execute(list);

            // output
            System.out.println(list);

        } catch (IOException e) {
            System.out.println("file " + args[0] + "cannot be opened");
            System.exit(1);
        } catch (InvalidExpressionException e) {
            System.out.println("file " + args[0] + "doesn't contain a valid program");
        }
    }
}
