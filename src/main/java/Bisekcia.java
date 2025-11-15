import java.util.List;

public class Bisekcia {
    private final Evaluator evaluator;
    private double start;
    private double end;
    private double presnost;

    public Bisekcia(Evaluator evaluator, double start, double end, double presnost) {
        this.evaluator = evaluator;
        this.start = start;
        this.end = end;
        this.presnost = presnost;
    }

    public double calculate() {
        int iterations = 0;
        double x = 0;
        while((end - start) > presnost) {
            x = 0.5 * (start + end);
            iterations++;
            if (evalVar(x) == 0) return x;
            if (evalVar(start) * evalVar(x) < 0) {
                end = x;
            }
            else if (evalVar(end) * evalVar(x) < 0) {
                start = x;
            }
            else {
                throw new RuntimeException("Na tomto intervale nepretina xovu os ");
            }
        }
        System.out.printf("Vysledok je %.8f ziskali sme ho po %d iteraciach.\n", x, iterations);
        return x;
    }

    private double evalVar(double x) {
        return evaluator.evaluateExpression(List.of(x));
    }
}
