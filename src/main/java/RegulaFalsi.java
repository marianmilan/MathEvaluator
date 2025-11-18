import java.util.List;
import java.util.Scanner;

public class RegulaFalsi {
    private double start = 3;
    private double end = 5;
    private double presnost = 1e-10;
    private String expression = "sin(t) + cos(sqrt(3) * t)";

    private final Scanner scanner;
    private final Tokenizer tokenizer;
    private final Parser parser;
    private final Evaluator evaluator;

    public RegulaFalsi(Scanner scanner, Tokenizer tokenizer, Parser parser, Evaluator evaluator) {
        this.scanner = scanner;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.evaluator = evaluator;

        init();
        calculate();
    }

    private void init() {
        byte choice;
        do {
            System.out.println("Pouzit ukazku [1] alebo zadat vlastne hodnoty [2]: ");
            choice = Byte.parseByte(scanner.nextLine());
        } while (choice != 1 && choice != 2);

        if (choice == 1) return;
        setFunction();
        setNewInterval();
        setPresnost();
    }

    private double calculate() {
        tokenizer.setExpression(expression);
        parser.setTokens(tokenizer.tokenize());
        evaluator.setHead(parser.expression());
        evaluator.setVariables(parser.getVariables());

        double x = start;
        double y;
        int iterations = 0;
        do {
            iterations++;
            y = x;
            x = caluclateX(start, end);
            if (evalFunction(x) == 0) return x;
            else if (evalFunction(start) * evalFunction(x) < 0) end = x;
            else if (evalFunction(end) * evalFunction(x) < 0) start = x;
            else throw new RuntimeException("Nie je koren na intervale.");
        } while(Math.abs(x - y) > presnost);
        System.out.printf("Vysledok je %.8f ziskali sme ho po %d iteraciach.\n", x, iterations);
        return x;
    }

    private double caluclateX(double a, double b) {
        double evalA = evalFunction(a);
        double evalB = evalFunction(b);
        return (a*evalB - b*evalA) / (evalB-evalA);
    }

    private double evalFunction(double x) {
        return evaluator.evaluateExpression(List.of(x));
    }


    private void setNewInterval() {
        System.out.println("Zadajte zaciatok intervalu: ");
        this.start = Double.parseDouble(scanner.nextLine());
        System.out.println("Zadajte koniec intervalu: ");
        this.end = Double.parseDouble(scanner.nextLine());
    }

    private void setFunction() {
        System.out.println("Zadajte funkciu: ");
        this.expression = scanner.nextLine();
    }

    private void setPresnost() {
        System.out.println("Zadajte presnost vo formate 1eX: ");
        this.presnost = Double.parseDouble(scanner.nextLine());
    }
}
