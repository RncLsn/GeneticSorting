package genetic_sorting.structures;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Alessandro Ronca
 */
public class Test {

    public static void main (String[] args) {
        try(BufferedReader reader =
                    Files.newBufferedReader(
                            Paths.get("Resources/SortingTree.txt"), Charset.defaultCharset())) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String encodedTree = stringBuilder.toString();
            EvolvingSorting evolvingSorting = new EvolvingSorting(encodedTree);

            ArrayList<Integer> list = new ArrayList<>();
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                list.add(rand.nextInt(100));
            }
            System.out.println(list);

            evolvingSorting.trySorting(list);
            System.out.println(list);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
        }
    }
}
