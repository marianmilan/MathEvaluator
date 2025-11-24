import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InterpolatingPolynomial {
    private List<Double> xCoordinates = List.of(-2.0, -1.0, 0.0, 2.0);
    private List<Double> yCoordinates = List.of(-2.0, 1.0, 2.0, 1.0);
    private double[][] matrix;
    private double[] vector;

    private Scanner scanner;
    private MatrixCreator creator;

    public InterpolatingPolynomial(Scanner scanner, MatrixCreator creator) {
        this.scanner = scanner;
        this.creator = creator;
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

        xCoordinates = new ArrayList<>();
        yCoordinates = new ArrayList<>();
        System.out.println("Zadajte x-ove suradnice bodov oddelene ciarkou.");
        String xCords = scanner.nextLine();
        System.out.println("Zadajte y-ove suradnice bodov oddelene ciarkou.");
        String yCords = scanner.nextLine();
        if (xCords.length() != yCords.length()) throw new RuntimeException("Msi byt rovnaky pocet x a y bodov.");
        String[] xParts = xCords.split(",");
        String[] yParts = yCords.split(",");
        int length = xParts.length;
        for (int i = 0; i < length; i++) {
            xCoordinates.add(Double.parseDouble(xParts[i]));
            yCoordinates.add(Double.parseDouble(yParts[i]));
        }
    }

    public void calculate() {
        matrix = creator.getMatrixFromVectors(xCoordinates, yCoordinates);
        System.out.println(Arrays.deepToString(matrix));
        JacobiMethod method = new JacobiMethod(matrix, 1e-6);
        vector = method.calculate();
        printAnswer();
    }

    private void printAnswer() {
        String[] exponents = {
                "⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹"
        };

        int index = 0;
        System.out.print("\np(x) = ");
        for (double v : vector) {
            v = (double)Math.round(v*1000d) / 1000d;
            if (index == 0 && v != 0) {
                System.out.printf("%.3f", v);
            } else if (v != 0) {
                if (v < 0) {
                    System.out.printf(" - %.3fx%s", Math.abs(v), exponents[index]);
                } else {
                    System.out.printf(" + %.3fx%s", v, exponents[index]);
                }
            }
            index++;
        }
    }
}
