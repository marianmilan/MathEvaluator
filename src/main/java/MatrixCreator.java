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
                findAtomicNodes(en.left, i);
                Node node = en.right;
                if (node instanceof Node.NumberNode nn) {
                    matrix[i][lastIndex] = nn.value;
                }
            }
        }
    }

    private void findAtomicNodes(Node node, int row) {
        if (node instanceof Node.BinaryNode bn) {
            if (checkAtomicNode(bn.left)) {
                addRowToMatrix(bn.left, row, false);
            }
            else {
                findAtomicNodes(bn.left, row);
            }

            if (bn.token.type() == TokenType.SUB) {
                addRowToMatrix(bn.right, row, true);
            } else {
                addRowToMatrix(bn.right, row, false);
            }
        }
    }

    private boolean checkAtomicNode(Node node) {
        return switch (node) {
            case Node.UnaryNode un -> true;
            case Node.VariableNode vn -> true;
            case Node.BinaryNode bn when bn.token.value().equals("*") -> true;
            default -> false;
        };
    }

    private void addRowToMatrix(Node node, int row, boolean isSubtraction) {
        double value;
        int index;
        if (node instanceof Node.BinaryNode bn) {
            if (bn.right instanceof Node.VariableNode vn) {
                value = getValue(bn.left, isSubtraction);
                index = getVariableIndex(vn.value);
                matrix[row][index] = value;
            }
        }
        else if (node instanceof Node.VariableNode vn) {
            value = getValue(node, isSubtraction);
            index = getVariableIndex(vn.value);
            matrix[row][index] = value;
        }
        else if (node instanceof Node.UnaryNode un) {
            if (un.child instanceof Node.VariableNode vn) {
                index = getVariableIndex(vn.value);
                matrix[row][index] = getValue(node, isSubtraction);
            }
        }
    }

    private double getValue(Node node, boolean subtraction) {
        double value;
        if (node instanceof Node.UnaryNode un && un.token.type() == TokenType.MINUS) {
            value = -getValue(un.child, subtraction);
        }
        else if (node instanceof Node.NumberNode nn) {
            value = nn.value;
        }
        else value = 1.0;
        return subtraction ? -value : value;
    }

    private int getVariableIndex(String value) {
        return BinarySearch.search(variables, value);
    }
}
