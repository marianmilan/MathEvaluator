public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("sin(t) + cos(sqrt(3) * t)");
        Evaluator evaluator = new Evaluator(parser.expression(), parser.getVariables());
        Bisekcia bisekcia = new Bisekcia(evaluator, 3, 5, 1e-5);
        bisekcia.calculate();
        RegulaFalsi falsi = new RegulaFalsi(evaluator, 3, 5, 1e-5);
        falsi.calculate();
    }
}
