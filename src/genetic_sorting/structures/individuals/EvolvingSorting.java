package genetic_sorting.structures.individuals;

import genetic_sorting.structures.expressions.*;
import genetic_sorting.structures.factories.RandomFunctionFactory;
import genetic_sorting.structures.factories.RandomTerminalFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alessandro Ronca
 */
public class EvolvingSorting implements Cloneable {

    public static final  int     INITIAL_INDEX      = 0;
    private static final int     MAX_DEPTH          = 6;
    private static final Pattern EXPRESSION_START_P = Pattern.compile("\\s*\\(\\s*(?<id>\\w+)");
    private static final Pattern EXPRESSION_END_P   = Pattern.compile("\\s*\\)");
    private Expression rootExpression;

    private EvolvingSorting (Expression rootExpression) { this.rootExpression = rootExpression; }

    public static EvolvingSorting generateFull (RandomFunctionFactory functionFactory,
                                                RandomTerminalFactory terminalFactory) {
        return generateFull(functionFactory, terminalFactory, MAX_DEPTH);
    }

    public static EvolvingSorting generateFull (RandomFunctionFactory functionFactory,
                                                RandomTerminalFactory terminalFactory,
                                                int maxDepth) {
        return new EvolvingSorting(
                generateRandomExpression(maxDepth, functionFactory, terminalFactory));
    }

    public static EvolvingSorting generateFromEncoding (String encodedTree)
            throws InvalidExpressionException {
        return new EvolvingSorting(parseExpression(encodedTree));
    }

    public static EvolvingSorting generateFromEncoding (Path encodedTreeFile)
            throws IOException, InvalidExpressionException {
        String encodedTree = new String(Files.readAllBytes(encodedTreeFile));
        return generateFromEncoding(encodedTree);
    }

    public static Expression generateRandomExpression (int maxDepth,
                                                       RandomFunctionFactory functionFactory,
                                                       RandomTerminalFactory terminalFactory) {
        return auxGenerateRandomExpression(0, maxDepth, functionFactory, terminalFactory);
    }


    private static Expression auxGenerateRandomExpression (int depth,
                                                           int maxDepth,
                                                           RandomFunctionFactory functionFactory,
                                                           RandomTerminalFactory terminalFactory) {
        if (depth == maxDepth - 1) {
            return terminalFactory.makeTerminal();
        }
        Function function = functionFactory.makeFunction();
        List<Expression> args = new ArrayList<>();
        for (int i = 0; i < function.numOfArgs(); i++) {
            args.add(auxGenerateRandomExpression(depth + 1, maxDepth, functionFactory,
                                                 terminalFactory));
        }
        try {
            function.setArgs(args);
        } catch (WrongNumberOfArgsException | AlreadyInitializedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return function;
    }

    public static Expression parseExpression (String encodedTree)
            throws InvalidExpressionException {
        return auxParse(encodedTree, 0, new Index()).expression;
    }

    private static ParserPair auxParse (String encodedTree, int start, Index index)
            throws InvalidExpressionException {

        Matcher matcher = EXPRESSION_START_P.matcher(encodedTree);
        if (matcher.find(start)) {
            int end;
            ParserPair parserPair1;
            ParserPair parserPair2;
            ParserPair parserPair3;
            Expression expression;
            if (matcher.group("id").matches("\\d+")) {
                expression = new Constant(Integer.parseInt(matcher.group("id")));
                end = matcher.end();
            } else {
                switch (matcher.group("id").toLowerCase()) {
                    case "decrement":
                        parserPair1 = auxParse(encodedTree, matcher.end(), index);
                        end = parserPair1.index;
                        expression = new Decrement(parserPair1.expression);
                        break;
                    case "increment":
                        parserPair1 = auxParse(encodedTree, matcher.end(), index);
                        end = parserPair1.index;
                        expression = new Increment(parserPair1.expression);
                        break;
                    case "getbigger":
                        parserPair1 = auxParse(encodedTree, matcher.end(), index);
                        parserPair2 = auxParse(encodedTree, parserPair1.index, index);
                        end = parserPair2.index;
                        expression = new GetBigger(parserPair1.expression, parserPair2.expression);
                        break;
                    case "getsmaller":
                        parserPair1 = auxParse(encodedTree, matcher.end(), index);
                        parserPair2 = auxParse(encodedTree, parserPair1.index, index);
                        end = parserPair2.index;
                        expression = new GetSmaller(parserPair1.expression, parserPair2.expression);
                        break;
                    case "iterate":
                        Index newIndex = new Index();
                        parserPair1 = auxParse(encodedTree, matcher.end(), newIndex);
                        parserPair2 = auxParse(encodedTree, parserPair1.index, newIndex);
                        parserPair3 = auxParse(encodedTree, parserPair2.index, newIndex);
                        end = parserPair3.index;
                        expression = new Iterate(parserPair1.expression,
                                                 parserPair2.expression,
                                                 parserPair3.expression);
                        break;
                    case "subtract":
                        parserPair1 = auxParse(encodedTree, matcher.end(), index);
                        parserPair2 = auxParse(encodedTree, parserPair1.index, index);
                        end = parserPair2.index;
                        expression = new Subtract(parserPair1.expression, parserPair2.expression);
                        break;
                    case "swap":
                        parserPair1 = auxParse(encodedTree, matcher.end(), index);
                        parserPair2 = auxParse(encodedTree, parserPair1.index, index);
                        end = parserPair2.index;
                        expression = new Swap(parserPair1.expression, parserPair2.expression);
                        break;
                    case "index":
                        expression = index;
                        end = matcher.end();
                        break;
                    case "length":
                        expression = new Length();
                        end = matcher.end();
                        break;
                    default:
                        throw new InvalidExpressionException(
                                "Invalid identifier: " + matcher.group("id"));
                }
            }
            Matcher endMatcher = EXPRESSION_END_P.matcher(encodedTree);
            if (endMatcher.find(end)) {
                return new ParserPair(expression, endMatcher.end());
            } else {
                throw new InvalidExpressionException(
                        "The expression is not legal for the grammar, " +
                        "missing closing parentheses");
            }
        } else {
            throw new InvalidExpressionException("The expression is not legal for the grammar");
        }
    }

    public Expression getRootExpression () {
        return this.rootExpression;
    }

    public void setRootExpression (Expression rootExpression) {
        this.rootExpression = rootExpression;
    }

    public void trySorting (List<Integer> list) {
        rootExpression.init();
        rootExpression.evaluate(list, INITIAL_INDEX);
    }

    public void init () {
        rootExpression.init();
    }

    @Override
    public int hashCode () {
        return getClass().hashCode() + rootExpression.hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        if (!obj.getClass().equals(getClass())) {
            return false;
        }
        EvolvingSorting that = (EvolvingSorting) obj;
        return this.rootExpression.equals(that.rootExpression);
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return new EvolvingSorting((Expression) rootExpression.clone());
    }

    @Override
    public String toString () {
        return rootExpression.toString();
    }

    private static class ParserPair {

        private final Expression expression;
        private final int        index;

        private ParserPair (Expression expression, int index) {
            this.expression = expression;
            this.index = index;
        }
    }

}
