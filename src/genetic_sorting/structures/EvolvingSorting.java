package genetic_sorting.structures;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alessandro Ronca
 */
public class EvolvingSorting {

    private Expression rootExpression;

    public Expression getRootExpression () {
        return this.rootExpression;
    }

    public void trySorting (List<Integer> list) {
        rootExpression.evaluate(list);
    }

    public EvolvingSorting (String encodedTree) throws InvalidExpressionException {
        rootExpression = parse(encodedTree, 0, new Index()).expression;
    }

    private static final Pattern EXPRESSION_START_P = Pattern.compile("\\s*\\(\\s*(?<id>\\w+)");
    private static final Pattern EXPRESSION_END_P   = Pattern.compile("\\s*\\)");

    private ParserPair parse (String encodedTree, int start, Index index)
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
                        parserPair1 = parse(encodedTree, matcher.end(), index);
                        end = parserPair1.index;
                        expression = new Decrement(parserPair1.expression);
                        break;
                    case "increment":
                        parserPair1 = parse(encodedTree, matcher.end(), index);
                        end = parserPair1.index;
                        expression = new Increment(parserPair1.expression);
                        break;
                    case "getbigger":
                        parserPair1 = parse(encodedTree, matcher.end(), index);
                        parserPair2 = parse(encodedTree, parserPair1.index, index);
                        end = parserPair2.index;
                        expression = new GetBigger(parserPair1.expression, parserPair2.expression);
                        break;
                    case "getsmaller":
                        parserPair1 = parse(encodedTree, matcher.end(), index);
                        parserPair2 = parse(encodedTree, parserPair1.index, index);
                        end = parserPair2.index;
                        expression = new GetSmaller(parserPair1.expression, parserPair2.expression);
                        break;
                    case "iterate":
                        Index newIndex = new Index();
                        parserPair1 = parse(encodedTree, matcher.end(), newIndex);
                        parserPair2 = parse(encodedTree, parserPair1.index, newIndex);
                        parserPair3 = parse(encodedTree, parserPair2.index, newIndex);
                        end = parserPair3.index;
                        expression =
                                new Iterate((parserPair1.expression.getClass() == Index.class) ?
                                            index : parserPair1.expression,
                                            (parserPair2.expression.getClass() == Index.class) ?
                                                index : parserPair2.expression,
                                            parserPair3.expression,
                                            newIndex);
                        break;
                    case "subtract":
                        parserPair1 = parse(encodedTree, matcher.end(), index);
                        parserPair2 = parse(encodedTree, parserPair1.index, index);
                        end = parserPair2.index;
                        expression = new Subtract(parserPair1.expression, parserPair2.expression);
                        break;
                    case "swap":
                        parserPair1 = parse(encodedTree, matcher.end(), index);
                        parserPair2 = parse(encodedTree, parserPair1.index, index);
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

    @Override
    public String toString () {
        // todo
        return "";
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
