import java.util.List;

public class MatrixCreator {
    private final double[][] matrix;
    private final List<String> variables;
    private final int lastIndex;

    public MatrixCreator(List<String> variables) {
        this.variables = variables;
        this.matrix = new double[variables.size()][variables.size() + 1];
        this.lastIndex = variables.size();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void calcMatrix(Node... head) {
        int length = head.length;
        for (int i = 0; i < length; i++) {
            if (head[i] instanceof Node.EqualNode en) {
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
        if (node instanceof Node.BinaryNode bn) {
            if (!(bn.left instanceof Node.VariableNode x)) {
                addToMatrix(bn.left, row);
            } else {
                int variableIndex = getVariableIndex(x.name);
                matrix[row][variableIndex] = x.coefficient;
            }
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
}
