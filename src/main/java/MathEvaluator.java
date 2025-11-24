import java.util.Scanner;

public class MathEvaluator {
    private static String[] metody = {"Babylonska metoda", "Bisekcia", "Regula Falsi", "Metoda dotycnic",
                                "Jacobiho metoda", "Gauss-Seidelova metoda", "Lagrangeov interpolacny polynom",
                                "Metoda najmensich stvorcov", "Obdlznikova metoda"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        MatrixCreator creator = new MatrixCreator();
        MatrixNorm norm = new MatrixNorm();

        loop:
        while(true) {
            String index = getMethodIndex(scanner);
            switch (index) {
                case "0" -> new BabylonskaMetoda(scanner);
                case "1" -> new Bisekcia(scanner, tokenizer, parser, evaluator);
                case "2" -> new RegulaFalsi(scanner, tokenizer, parser, evaluator);
                case "3" -> new NewtonMethod(scanner, tokenizer, parser, evaluator);
                case "4" -> new JacobiMethod(scanner, tokenizer, parser, creator, norm);
                case "5" -> new GaussSeidelMethod(scanner, tokenizer, parser, creator, norm);
                case "6" -> new InterpolatingPolynomial(scanner, creator);
                case "7" -> new LeastSquaresMethod(scanner, creator);
                case "10" -> System.out.println();
            }
            System.out.println("\nPre pokracovanie stlacte [y].");
            if(!scanner.nextLine().equalsIgnoreCase("y")) { break loop; }
        }

        scanner.close();
    }

    private static String getMethodIndex(Scanner scanner) {
        System.out.println("Vyberte metodu:");
        byte index = 0;
        for(String s : metody) {
            System.out.printf("[%d] - %s\n", index++, s);
        }
        System.out.println("[x] - Exit");

        System.out.print("\nVyberte metodu, ktoru chcete pouzit: ");
        return scanner.nextLine();
    }
}
