import java.util.List;

public class Evaluator {
    private final Node head;
    private final List<String> variables;
    private List<Double> values;

    public Evaluator(Node head, List<String> variables) {
        this.head = head;
        this.variables = variables;
    }

    public double evaluateExpression(List<Double> values) {
        this.values = values;
        return evaluate(head);
    }

    private double evaluate (Node node) {
        double a, b;
        if (node instanceof Node.NumberNode nn) return nn.value;
        if (node instanceof Node.VariableNode vn) {
            return getVariableValue(vn.value);
        }
        if (node instanceof Node.GroupingNode gn) return evaluate(gn.child);
        if (node instanceof Node.BinaryNode bn) {
            a = evaluate(bn.left);
            b = evaluate(bn.right);
            return calculate(a, b, bn.token.type());
        }
        if (node instanceof Node.UnaryNode un) {
            if (un.child != null) {
                a = evaluate(un.child);
                if (un.token.type() == TokenType.MINUS) return negate(a);
                return factorial(a);
            } else throw new RuntimeException("Bad use of unary operator");
        }
        if (node instanceof Node.FunctionNode fn) {
            if (fn.child != null) {
                a = evaluate(fn.child);
                return applyFunction(fn.token.value(), a);
            }
            else throw new RuntimeException("Not valid function use!");
        }
        return 0;
    }

    private double getVariableValue(String name) {
        int index = BinarySearch.search(variables, name);
        return values.get(index);
    }

    private double factorial(double a) {
        int x = (int) a;
        int sum = 1;
        for (int i = 2; i <= x; i++) {
            sum = sum * i;
        }
        return sum;
    }

    private double negate(double a) {
        return -a;
    }

    private double calculate(double a, double b, TokenType operator) {
        return switch (operator) {
            case ADD -> a + b;
            case SUB -> a - b;
            case MUL -> a * b;
            case DIV -> a / b;
            case POW -> Math.pow(a, b);
            default -> throw new RuntimeException("Invalid operator");
        };
    }

    private double applyFunction(String value, double x) {
        return switch (value) {
            case "acos" -> Math.acos(x);
            case "asin" -> Math.asin(x);
            case "atan" -> Math.atan(x);
            case "cos" -> Math.cos(x);
            case "ln" -> Math.log(x);
            case "log" -> Math.log10(x);
            case "sin" -> Math.sin(x);
            case "sqrt" -> Math.sqrt(x);
            case "tan" -> Math.tan(x);
            default -> throw new RuntimeException("Not a valid function");
        };
    }
}
