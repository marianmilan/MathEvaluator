import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixCreator {
    private final double[][] matrix;
    private final List<String> variables;
    private final int lastIndex;
    private Map<String, Double> map;

    public MatrixCreator(List<String> variables) {
        this.variables = variables;
        this.matrix = new double[variables.size()][variables.size() + 1];
        this.map = new HashMap<>();
        this.lastIndex = variables.size();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void calcMatrix(Node... head) {
        int length = head.length;
        for (int i = 0; i < length; i++) {
            if (head[i] instanceof Node.EqualNode en) {
                createMatrixRow(en.left, i);
                System.out.println(map);
                map.clear();
                Node node = en.right;
                if (node instanceof Node.NumberNode nn) {
                    matrix[i][lastIndex] = nn.value;
                }
            }
        }
    }

    private void createMatrixRow(Node node, int row) {
        findAtomicNodes(node);
        for (String s : map.keySet()) {
            int index = getVariableIndex(s);
            matrix[row][index] = map.get(s);
        }
    }

    private void findAtomicNodes(Node node) {
        if (node instanceof Node.BinaryNode bn) {
            if (checkAtomicNode(bn.left)) {
                addToMap(bn.left, false);
            }
            else {
                findAtomicNodes(bn.left);
            }

            if (bn.token.type() == TokenType.SUB) {
                addToMap(bn.right, true);
            } else {
                addToMap(bn.right, false);
            }
        }
    }

    private boolean checkAtomicNode(Node node) {
        return switch (node) {
            case Node.UnaryNode unaryNode -> true;
            case Node.VariableNode variableNode -> true;
            case Node.BinaryNode bn when bn.token.value().equals("*") -> true;
            default -> false;
        };
    }

    private void addToMap(Node node, boolean isSubtraction) {
        if (node instanceof Node.BinaryNode bn) {
            if (bn.right instanceof Node.VariableNode vn) {
                double value = isSubtraction ? -getValue(bn.left) : getValue(bn.left);
                map.put(vn.value, value);
            }
        }
        if (node instanceof Node.VariableNode vn) {
            double value = isSubtraction ? -getValue(node) : getValue(node);
            map.put(vn.value, value);
        }
        if (node instanceof Node.UnaryNode un) {
            if (un.child instanceof Node.VariableNode vn) {
                map.put(vn.value, getValue(node));
            }
        }
    }

    private double getValue(Node node) {
        if (node instanceof Node.UnaryNode un && un.token.type() == TokenType.MINUS) {
            return -getValue(un.child);
        }
        else if (node instanceof Node.NumberNode nn) {
            return nn.value;
        }
        else return 1.0;
    }

    private int getVariableIndex(String value) {
        return BinarySearch.search(variables, value);
    }
}
