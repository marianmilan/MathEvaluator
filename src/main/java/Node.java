package node;

public enum NodeType { BINARY, NUMBER, VARIABLE, GROUPED_VARIABLE, UNARY, FUNCTION, GROUPED, EQUAL}
public abstract class Node {
    public abstract void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction);
    public abstract NodeType getType();
    public String getName() {return null;}
    public Token getToken() {return null;}
    public Double getValue() {return null;}


    static class BinaryNode extends Node {
        final Node left;
        final Token token;
        final Node right;

        public BinaryNode(Node left, Token token, Node right) {
            this.left = left;
            this.token = token;
            this.right = right;
        }

        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {
            if (token.type() == TokenType.MUL) {

            }
        }

        @Override
        public NodeType getType() {
            return NodeType.BINARY;
        }

        public Token getToken() { return token; }
    }

    static class UnaryNode extends Node {
        final Node child;
        final Token token;

        public UnaryNode(Node child, Token token) {
            this.child = child;
            this.token = token;
        }
        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {

        }

        @Override
        public NodeType getType() {
            return NodeType.UNARY;
        }

        @Override
        public Token getToken() { return token; }
    }

    static class NumberNode extends Node {
        final double value;

        public NumberNode(String value) {
            this.value = Double.parseDouble(value);
        }

        public NumberNode (Double value) {
            this.value = value;
        }

        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {

        }

        @Override
        public NodeType getType() {
            return NodeType.NUMBER;
        }

        @Override
        public Double getValue() {
            return value;
        }
    }

    static class GroupingNode extends Node {
        Node child;

        public GroupingNode(Node child) {
            this.child = child;
        }

        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {}

        @Override
        public NodeType getType() {
            return NodeType.GROUPED;
        }
    }

    static class FunctionNode extends Node {
        Token token;
        Node child;

        public FunctionNode(Token token, Node child) {
            this.token = token;
            this.child = child;
        }

        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {}

        @Override
        public NodeType getType() {
            return NodeType.FUNCTION;
        }

        @Override
        public Token getToken() { return token; }
    }

    static class VariableNode extends Node {
        String name;

        public VariableNode(String name) {
            this.name = name;
        }

        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {

        }

        @Override
        public NodeType getType() {
            return NodeType.VARIABLE;
        }

        public String getName() {
            return name;
        }
    }

    static class GroupedVariable extends Node {
        double coefficient;
        String name;
        double exponent;

        public GroupedVariable(double coefficient, String name, double exponent) {
            this.coefficient = coefficient;
            this.name = name;
            this.exponent = exponent;
        }

        public String getName() {
            return name;
        }

        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {

        }

        @Override
        public NodeType getType() {
            return NodeType.GROUPED_VARIABLE;
        }
    }

    static class EqualNode extends Node {
        Node left;
        Token operator;
        Node right;

        public EqualNode(Node left, Token operator, Node right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public void addToMatrix(double[][] matrix, int row, int col, boolean isSubtraction) {

        }

        @Override
        public NodeType getType() {
            return NodeType.EQUAL;
        }

        @Override
        public Token getToken() { return operator; }
    }
}
