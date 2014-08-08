package genetic_sorting.interpreters;

import genetic_sorting.structures.EvolvingSorting;
import genetic_sorting.structures.InvalidExpressionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Alessandro Ronca
 */
public class SortingTreeInterpreter {

//    static String encodedSorting
//            = "(swap (swap (iterate (swap (getbigger (getbigger (index) (index)) (index)) (swap
// (length) (length))) (increment (subtract (length) (index))) (iterate (iterate (index) (swap
// (increment (getbigger (length) (length))) (getbigger (subtract (getbigger (getbigger (subtract
// (length) (length)) (getbigger (index) (index))) (index)) (subtract (subtract (swap (index)
// (index)) (getbigger (length) (index))) (getbigger (subtract (length) (index)) (getbigger
// (index) (length))))) (iterate (iterate (getbigger (getbigger (index) (index)) (increment
// (index))) (swap (getbigger (index) (index)) (getbigger (length) (length))) (increment
// (subtract (index) (index)))) (getbigger (index) (swap (index) (getbigger (index) (index))))
// (iterate (getbigger (increment (length)) (decrement (index))) (getbigger (decrement (length))
// (decrement (length))) (swap (increment (index)) (iterate (index) (length) (length)))))))
// (length)) (increment (length)) (swap (increment (length)) (increment (length))))) (getbigger
// (getbigger (index) (decrement (getbigger (increment (index)) (increment (length))))) (index)))
// (increment (length)))";

    public static void main (String[] args) throws InvalidExpressionException {

        Scanner scanner = new Scanner(System.in);

        EvolvingSorting sorting = EvolvingSorting.generateFromEncoding(scanner.nextLine());

        List<Integer> list = new ArrayList<>();
        String listStr = scanner.nextLine();
        Scanner listScanner = new Scanner(listStr);
        while (listScanner.hasNext()) {
            list.add(listScanner.nextInt());
        }

        sorting.trySorting(list);

        System.out.println(list);

//        EvolvingSorting sorting = EvolvingSorting.generateFromEncoding(encodedSorting);

//        List<Integer> list = Arrays.asList(6, 4, 13, 8, 19, 4, 19);

//        System.out.println(list);
//        sorting.trySorting(list);
//        System.out.println(list);
    }
}
