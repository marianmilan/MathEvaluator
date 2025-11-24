import java.util.Arrays;
import java.util.List;

public class MatrixCreator {
    private int lastIndex;
    private double[][] matrix;
    private List<String> variables;

    public double[][] getMatrixFromExpressions(int size, List<Node> trees, List<String> variables) {
        this.lastIndex = size;
        this.matrix = new double[size][size+1];
        this.variables = variables;
        calcMatrix(trees);
        tryToMakeDominant(matrix);
        return editMatrix(matrix);
    }

    public double[][] getMatrixFromVectors(List<Double> x, List<Double> y) {
        int xSize = x.size();
        int ySize = y.size() + 1;
        this.matrix = new double[xSize][ySize];
        for (int i = 0; i < xSize; i++) {
            matrix[i][ySize - 1] = y.get(i);
            for (int j = 0; j < xSize; j++) {
                matrix[i][j]  = Math.pow(x.get(i), j);
            }
        }
        System.out.println(Arrays.deepToString(matrix));
        tryToMakeDominant(matrix);
        return editMatrix(matrix);
    }

    public double[][] tryToMakeDominant(double[][] matrix) {
        int length = matrix.length;
        for (int i = matrix.length - 1; i >= 0; i--) {
            int bestRow = i;
            double bestValue = Math.abs(matrix[i][i]);
            for (int j = i; j >= 0; j--) {
                double value = Math.abs(matrix[i][j]);
                if (value > bestValue) {
                    bestValue = value;
                    bestRow = j;
                }
            }
            if (bestRow != i) {
                changeRows(bestRow, i);
            }
        }
        return matrix;
    }

    public double[][] editMatrix(double[][] matrix) {
        int colIndex;
        int size = matrix.length;
        double[][] matrixA  = new double[size][size];
        for (int i = 0; i < matrix.length; i++) {
            colIndex = 0;
            for (int j = 0; j <= matrix.length; j++) {
                if (i == j) continue;
                if (j < matrix.length) {
                    matrixA[i][colIndex] = -(matrix[i][j] / matrix[i][i]);
                } else {
                    matrixA[i][colIndex] = matrix[i][j] / matrix[i][i];
                }
                colIndex++;
            }
        }
        return matrixA;
    }

    private void calcMatrix(List<Node> trees) {
        int length = trees.size();
        for (int i = 0; i < length; i++) {
            if (trees.get(i) instanceof Node.EqualNode en) {
                addToMatrix(en.left, i);
                Node node = en.right;
                if (node instanceof Node.NumberNode nn) {
                    matrix[i][lastIndex] = nn.value;
                }
            } else {
                throw new RuntimeException("Not valid linear expression");
            }
        }
    }

    private void addToMatrix(Node node, int row) {
        if (node instanceof Node.VariableNode vn) {
            int variableIndex = getVariableIndex(vn.name);
            matrix[row][variableIndex] = vn.coefficient;
        }
        if (node instanceof Node.BinaryNode bn) {
            addToMatrix(bn.left, row);
            if (!(bn.right instanceof Node.VariableNode x)) {
                addToMatrix(bn.right, row);
            } else {
                int variableIndex = getVariableIndex(x.name);
                boolean subtraction = bn.token.type() == TokenType.SUB;
                double value = subtraction ? -x.coefficient : x.coefficient;
                matrix[row][variableIndex] = value;
            }
        }
    }

    private int getVariableIndex(String value) {
        return BinarySearch.search(variables, value);
    }

    private void changeRows(int i, int j) {
        for (int x = 0; x <= matrix.length; x++) {
            double temp = matrix[i][x];
            matrix[i][x] = matrix[j][x];
            matrix[j][x] = temp;
        }
    }
}
