import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class JacobiMethod {
    private List<String> expressions= List.of(
            "7a+b+2c+3d=18",
            "-2a+10b-3c+4d=23",
            "3a+4b-12c+d=15",
                    "3c-8d=25");
    private double presnost = 1e-6;
    private double[][] matrix;
    private double[] vector = new double[expressions.size()];
    private double iterations;

    Scanner scanner;
    Tokenizer tokenizer;
    Parser parser;
    MatrixCreator matrixCreator;
    MatrixNorm norm;

    public JacobiMethod(double[][] matrix, double presnost) {
        this.matrix = matrix;
        this.vector = new double[matrix.length];
        this.presnost = presnost;
        this.norm = new MatrixNorm();
    }

    public JacobiMethod(Scanner scanner, Tokenizer tokenizer, Parser parser, MatrixCreator creator, MatrixNorm norm) {
        this.scanner = scanner;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.matrixCreator= creator;
        this.norm = norm;
        init();
        getMatrix();
        vector = calculate();
        System.out.println("x" + iterations + " = " + Arrays.toString(vector));
    }

    private void init() {
        byte choice;
        do {
            System.out.println("Pouzit ukazku [1] alebo zadat vlastne hodnoty [2]: ");
            choice = Byte.parseByte(scanner.nextLine());
        } while (choice != 1 && choice != 2);

        if (choice == 1) return;

        expressions = new ArrayList<>();
        System.out.println("Kolko rovnic: ");
        int count = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < count; i++) {
            System.out.printf("rovnica %d:\n", i+1);
            expressions.add(scanner.nextLine());
        }
        System.out.println("Zadajte pociatocnu aproximaciu (cisla oddelte ciarkou)");
        String s = scanner.nextLine();
        String[] parts = s.split(",");
        if (parts.length != count) throw new RuntimeException("Nespravne zadany vektor");
        int index = 0;
        this.vector = new double[count];
        for (String part : parts) {
            this.vector[index] = Double.parseDouble(part);
            index++;
        }
        System.out.println("Zadajte pozadovanu presnost vo formate 1eX");
        this.presnost = Double.parseDouble(scanner.nextLine());
    }

    private void getMatrix() {
        List<Node> trees = new ArrayList<>();
        List<String> variables = new ArrayList<>();
        for (String s : expressions) {
            tokenizer.setExpression(s);
            parser.setTokens(tokenizer.tokenize());
            trees.add(parser.expression());
            System.out.println(parser.getVariables());
            List<String> currentVariables = parser.getVariables();
            if (variables.size() < currentVariables.size()) variables = currentVariables;
        }
        this.matrix = matrixCreator.getMatrixFromExpressions(variables.size(), trees, variables);
    }

    public double[] calculate() {
        double[] newVector = vector;
        double x;
        this.iterations = 0;
        do {
            iterations++;
            vector = newVector;
            newVector = getNewVector();
            x = getAccuracy(newVector);
        } while (x > presnost);
        return newVector;
    }

    private double getAccuracy(double[] newVector) {
        double[] x = new double[newVector.length];
        for (int i = 0; i < vector.length; i++) {
            x[i] = newVector[i] - vector[i];
        }
        return norm.getRowNorm(x);
    }

    private double[] getNewVector() {
        int length = matrix.length;
        int colIndex;
        double value;
        double[] newVector = new double[vector.length];
        for (int i = 0; i < length; i++) {
            colIndex = 0;
            value = 0;
            for (int j = 0; j < vector.length; j++) {
                if (i == j) continue;
                value += matrix[i][colIndex] * vector[j];
                colIndex++;
            }
            newVector[i] = value + matrix[i][colIndex];
        }
        return newVector;
    }
}
