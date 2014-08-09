package genetic_sorting.interpreters;

import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.InvalidExpressionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Alessandro Ronca
 */
public class SortingTreeInterpreter {

    public static void main (String[] args) throws InvalidExpressionException {

        if (args.length < 1) {
            System.out.println("usage: SortingTreeInterpreter <sortingTree-file>");
        }

        try (BufferedReader reader = Files
                .newBufferedReader(Paths.get(args[0]), Charset.defaultCharset())) {

            // load program
            String encodedTree = "";
            String line;
            while ((line = reader.readLine()) != null) {
                encodedTree += line;
                encodedTree += "\n";
            }
            EvolvingSorting sorting = EvolvingSorting.generateFromEncoding(encodedTree);

            // load input list
            Scanner scanner = new Scanner(System.in);
            List<Integer> list = new ArrayList<>();
            String listStr = scanner.nextLine();
            Scanner listScanner = new Scanner(listStr);
            while (listScanner.hasNext()) {
                list.add(listScanner.nextInt());
            }

            // execute
            sorting.trySorting(list);

            // output
            System.out.println(list);

        } catch (IOException e) {
            System.out.println("file " + args[0] + "cannot be opened");
            System.exit(1);
        }

    }
}
