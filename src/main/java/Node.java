public abstract class Node {
    public abstract Node simplify();

    static class BinaryNode extends Node {
         Node left;
         Token token;
         Node right;

        public BinaryNode(Node left, Token token, Node right) {
            this.left = left;
            this.token = token;
            this.right = right;
        }

        @Override
        public Node simplify() {
            Node leftNode = left.simplify();
            Node rightNode = right.simplify();

            if (token.type() == TokenType.POW && leftNode instanceof VariableNode vn) {
                if (rightNode instanceof NumberNode nn) {
                   return new VariableNode(vn.coefficient, vn.name, nn.value);
                }
            }
            if (leftNode instanceof NumberNode nn && rightNode instanceof VariableNode vn) {
                return new VariableNode(nn.value, vn.name, vn.exponent);
            }
            return new BinaryNode(leftNode, token, rightNode);
        }
    }

    static class UnaryNode extends Node {
        Node child;
        Token token;

        public UnaryNode(Node child, Token token) {
            this.child = child;
            this.token = token;
        }

        @Override
        public Node simplify() {
            Node node = child.simplify();
            if (token.type() == TokenType.MINUS) {
                if (node instanceof NumberNode nn) {
                    return new NumberNode(-nn.value);
                }
                if (node instanceof VariableNode vn) {
                    return new VariableNode(-vn.coefficient, vn.name, vn.exponent);
                }
            }
            return new UnaryNode(node, token);
        }
    }

    static class NumberNode extends Node {
        double value;

        public NumberNode(String value) {
            this.value = Double.parseDouble(value);
        }

        public NumberNode (Double value) {
            this.value = value;
        }

        @Override
        public Node simplify() {
            return this;
        }
    }

    static class GroupingNode extends Node {
        Node child;

        public GroupingNode(Node child) {
            this.child = child;
        }

        @Override
        public Node simplify() {
            return child.simplify();
        }
    }

    static class FunctionNode extends Node {
        String functionName;
        Node child;

        public FunctionNode(String functionName, Node child) {
            this.functionName = functionName;
            this.child = child;
        }

        @Override
        public Node simplify() {
            return new FunctionNode(functionName, child.simplify());
        }
    }

    static class VariableNode extends Node {
        double coefficient;
        String name;
        double exponent;

        public VariableNode(double coefficient, String name, double exponent) {
            this.coefficient = coefficient;
            this.name = name;
            this.exponent = exponent;
        }

        @Override
        public Node simplify() {
            return this;
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
        public Node simplify() {
            return new EqualNode(left.simplify(), operator, right.simplify());
        }
    }
}
