package genetic_sorting.structures;

import genetic_sorting.structures.factories.RandomFunctionFactory;
import genetic_sorting.structures.factories.RandomTerminalFactory;
import genetic_sorting.structures.individuals.EvolvingSorting;
import genetic_sorting.structures.individuals.InvalidExpressionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * @author Alessandro Ronca
 */
class DebugTest {

    public static void main (String[] args) {
//        test1();
        test2();

    }

    private static void test2 () {
        HashSet<EvolvingSorting> population = new HashSet<>();
        RandomFunctionFactory functionFactory = new RandomFunctionFactory();
        RandomTerminalFactory terminalFactory = new RandomTerminalFactory();
        for (int i = 0; i < 20; i++) {
            population.add(EvolvingSorting.generateFull(functionFactory, terminalFactory, 3));
        }
        for (EvolvingSorting sorting : population) {
            System.out.println(sorting);
        }
    }

    public static void test1 () {
        try (BufferedReader reader =
                     Files.newBufferedReader(
                             Paths.get("Resources/SortingTree.txt"), Charset.defaultCharset())) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String encodedTree = stringBuilder.toString();
            EvolvingSorting evolvingSorting = EvolvingSorting.generateFromEncoding(encodedTree);

            ArrayList<Integer> list = new ArrayList<>();
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                list.add(rand.nextInt(100));
            }
            System.out.println(list);

            evolvingSorting.init();
            evolvingSorting.trySorting(list);
            System.out.println(list);
            System.out.println(evolvingSorting);
        } catch (IOException | InvalidExpressionException e) {
            e.printStackTrace();
        }
    }
}
