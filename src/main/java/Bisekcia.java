import java.util.List;
import java.util.Scanner;

public class Bisekcia {
    private double start = 3.0;
    private double end = 5.0;
    private double presnost = 1e-10;
    private String expression = "sin(t) + cos(sqrt(3) * t)";

    private Scanner scanner;
    private final Tokenizer tokenizer;
    private final Parser parser;
    private final Evaluator evaluator;

    public Bisekcia(Scanner scanner, Tokenizer tokenizer, Parser parser, Evaluator evaluator) {
        this.scanner = scanner;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.evaluator = evaluator;

        init();
        calculate();
    }

    private Double calculate() {
        tokenizer.setExpression(expression);
        parser.setTokens(tokenizer.tokenize());
        evaluator.setHead(parser.expression());
        evaluator.setVariables(parser.getVariables());

        int iterations = 0;
        double x = 0;

        if (evalFunction(start) * evalFunction(end) > 0) {
            System.out.println("Na tomto intervale nepretina x-ovu os.");
            System.out.println("Zadat novy interval? [y]");
            if (!scanner.nextLine().equalsIgnoreCase("y")) return null;
            setNewInterval();
        }

        while((end - start) > presnost) {
            x = 0.5 * (start + end);
            iterations++;
            if (evalFunction(x) == 0.0) return x;

            if (evalFunction(start) * evalFunction(x) < 0) {
                end = x;
            }
            else {
                start = x;
            }
        }
        System.out.printf("Vysledok je %.8f ziskali sme ho po %d iteraciach.\n", x, iterations);
        return x;
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

    private void setNewInterval() {
        System.out.println("Zadajte zaciatok intervalu: ");
        this.start = Double.parseDouble(scanner.nextLine());
        System.out.println("Zadajte koniec intervalu: ");
        this.end = Double.parseDouble(scanner.nextLine());
    }

    private double evalFunction(double x) {
        return evaluator.evaluateExpression(List.of(x));
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
