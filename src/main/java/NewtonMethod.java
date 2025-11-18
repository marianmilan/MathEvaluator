import java.util.List;
import java.util.Scanner;

public class NewtonMethod {
    private double start = 0.7;
    private double end = 0.8;
    private double presnost = 1e-8;
    private String expression = "x - cos(x)";
    private String firstDerivation = "1 + sin(x)";
    private String secondDerivation = "cos(x)";

    private final Scanner scanner;
    private final Tokenizer tokenizer;
    private final Parser parser;
    private final Evaluator evaluator;

    public NewtonMethod(Scanner scanner, Tokenizer tokenizer, Parser parser, Evaluator evaluator) {
        this.scanner = scanner;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.evaluator = evaluator;
        calculate();
    }

    private void calculate() {
        Node functionNode = getAST(expression);
        evaluator.setVariables(parser.getVariables());
        Node firstDerivationNode = getAST(firstDerivation);
        Node secondDerivationNode = getAST(secondDerivation);

        double x = getFirstPoint(functionNode, secondDerivationNode);
        System.out.println(x);
        int iterations = 0;
        double y;
        do {
            //System.out.printf("%.9f\n", x);
            y = x;
            x = calculateX(functionNode, firstDerivationNode, y);
            iterations++;
        } while(Math.abs(x - y) > presnost);
        System.out.printf("Vysledok je %.8f ziskali sme ho po %d iteraciach.\n", x, iterations);
    }

    private double getFirstPoint(Node function, Node secondDerivation) {
        double x = evalFunction(function, start) * evalFunction(secondDerivation, start);
        double y = evalFunction(function, end) * evalFunction(secondDerivation, end);
        return x > 0 ? start : end;
    }

    private Node getAST(String expression) {
        tokenizer.setExpression(expression);
        parser.setTokens(tokenizer.tokenize());
        return parser.expression();
    }

    private double calculateX(Node first, Node second, double x) {
        double a = evalFunction(first, x);
        double b = evalFunction(second, x);
        return x - a / b;
    }

    private double evalFunction(Node head, double x) {
       evaluator.setHead(head);
       return evaluator.evaluateExpression(List.of(x));
    }
}


