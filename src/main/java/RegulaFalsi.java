import java.util.List;

public class RegulaFalsi {
    private double start;
    private double end;
    private double presnost;
    private final Evaluator evaluator;

    public RegulaFalsi(Evaluator evaluator, double start, double end, double presnost) {
        this.evaluator = evaluator;
        this.start = start;
        this.end = end;
        this.presnost = presnost;
    }

    public double calculate() {
        double x = start;
        double y;
        int iterations = 0;
        do {
            iterations++;
            y = x;
            x = caluclateX(start, end);
            if (evalFunc(x) == 0) return x;
            else if (evalFunc(start) * evalFunc(x) < 0) end = x;
            else if (evalFunc(end) * evalFunc(x) < 0) start = x;
            else throw new RuntimeException("Nie je koren na intervale.");
        } while(Math.abs(x - y) > presnost);
        System.out.printf("Vysledok je %.8f ziskali sme ho po %d iteraciach.\n", x, iterations);
        return x;
    }

    private double evalFunc(double x) {
        return evaluator.evaluateExpression(List.of(x));
    }

    private double caluclateX(double a, double b) {
        double evalA = evalFunc(a);
        double evalB = evalFunc(b);
        return (a*evalB - b*evalA) / (evalB-evalA);
    }
}
