import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LeastSquaresMethod {
    private List<Double> xCoordinates = List.of(-3.0, -1.0, 1.0, 3.0);
    private List<Double> yCoordinates = List.of(2.0, 5.0, 3.0, 4.0);
    private double[][] matrix;

    Scanner scanner;
    MatrixCreator creator;

    public LeastSquaresMethod(Scanner scanner, MatrixCreator creator) {
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

    private void calculate() {
        createMatrix();
        matrix = creator.tryToMakeDominant(matrix);
        matrix = creator.editMatrix(matrix);
        double[] vector = new GaussSeidelMethod(matrix, 1e-4).calculate();
        vector[0] = (double)Math.round(vector[0]*1000d) / 1000d;
        vector[1] = (double)Math.round(vector[1]*1000d) / 1000d;

        System.out.printf("\uD835\uDF11∗(\uD835\uDC65) = %.3f + %.3fx", vector[0], vector[1]);
    }

    private void createMatrix() {
        this.matrix = new double[2][3];
        double xCor = calculateSumOfCordinatesX();
        matrix[0][0] = xCoordinates.size();
        matrix[0][1] = xCor;
        matrix[0][2] = calculateSumOfCordinatesY();
        matrix[1][0] = xCor;
        matrix[1][1] = calculateSumOfCordinatesXSquared();
        matrix[1][2] = calculateSumOfCordinatesXMultipliedWithY();
    }

    private double calculateSumOfCordinatesX() {
        double sum = 0;
        for (double v : xCoordinates) {
            sum = sum + v;
        }
        return sum;
    }

    private double calculateSumOfCordinatesY() {
        double sum = 0;
        for (double v : yCoordinates) {
            sum = sum + v;
        }
        return sum;
    }

    private double calculateSumOfCordinatesXSquared() {
        double sum = 0;
        for (double v : xCoordinates) {
            sum = sum + (v * v);
        }
        return sum;
    }

    private double calculateSumOfCordinatesXMultipliedWithY() {
        double sum = 0;
        int length = xCoordinates.size();
        for (int i = 0; i < length; i++) {
            sum = sum + xCoordinates.get(i) * yCoordinates.get(i);
        }
        return sum;
    }
}
