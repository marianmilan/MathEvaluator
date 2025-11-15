public abstract class Node {
    static class BinaryNode extends Node {
        final Node left;
        final Token token;
        final Node right;

        public BinaryNode(Node left, Token token,  Node right) {
            this.left = left;
            this.token = token;
            this.right = right;
        }
    }

    static class UnaryNode extends Node {
        final Node child;
        final Token token;

        public UnaryNode(Node child, Token token) {
            this.child = child;
            this.token = token;
        }
    }

    static class NumberNode extends Node {
        final double value;

        public NumberNode(String value) {
            this.value = Double.parseDouble(value);
        }

        public NumberNode (Double value) {
            this.value = value;
        }
    }

    static class GroupingNode extends Node {
        Node child;

        public GroupingNode(Node child) {
            this.child = child;
        }
    }

    static class FunctionNode extends Node {
        Token token;
        Node child;

        public FunctionNode(Token token, Node child) {
            this.token = token;
            this.child = child;
        }
    }

    static class VariableNode extends Node {
        String value;

        public VariableNode(String value) {
            this.value = value;
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
    }
}
