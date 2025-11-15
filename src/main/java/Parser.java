import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private final HashMap<String, Double> constants = new HashMap<>();
    private final List<String> functions = new ArrayList<>();
    private final SortedSet<String> variables = new TreeSet<>();

    public Parser(String expression) {
        this.tokens = new Tokenizer(expression).tokenize();
        constants.put("m_pi", Math.PI);
        constants.put("m_e", Math.E);
        functions.add("acos");
        functions.add("asin");
        functions.add("atan");
        functions.add("cos");
        functions.add("ln");
        functions.add("log");
        functions.add("sin");
        functions.add("sqrt");
        functions.add("tan");
    }

    public Node expression() {
        return equals();
    }

    private Node equals() {
        Node node = term();

        if (match(TokenType.EQUALS)) {
            Token t = previous();
            Node right = term();
            return new Node.EqualNode(node, t, right);
        }

        return node;
    }

    private Node term() {
        Node node = factor();

       while (match(TokenType.ADD, TokenType.SUB)) {
            Token operator = previous();
            Node right = factor();
            node = new Node.BinaryNode(node, operator, right);
        }

        return node;
    }

    private Node factor() {
        Node node = pow();

        while (match(TokenType.MUL, TokenType.DIV)) {
            Token operator = previous();
            Node right = pow();
            node = new Node.BinaryNode(node, operator, right);
        }
        return node;
    }

    private Node pow() {
        Node node = func();

        while (match(TokenType.POW)) {
            Token operator = previous();
            Node right = func();
            node = new Node.BinaryNode(node, operator, right);
        }
        return node;
    }

    private Node func() {
        Node node = unary();
        if(check(TokenType.VAR) && isFunc(peek().value())) {
            advance();
            Token token = previous();
            Node child = unary();
            return new Node.FunctionNode(token, child);
        }
        return node;
    }

    private Node unary() {
        Node node = primary();

        if(match(TokenType.MINUS, TokenType.FACT)) {
            Token t = previous();
            Node child = primary();
            return new Node.UnaryNode(child, t);
        }
        return node;
    }

    private Node primary() {
        if (match(TokenType.NUM)) return new Node.NumberNode(previous().value());

        if (match(TokenType.PAR_L)) {
            Node node = expression();
            consume(TokenType.PAR_R);
            return new Node.GroupingNode(node);
        }

        if(check(TokenType.VAR) && constants.containsKey(peek().value())) {
            advance();
            return new Node.NumberNode(constants.get(previous().value()));
        }

        if (check(TokenType.VAR) && !isFunc(peek().value())) {
            advance();
            variables.add(previous().value());
            return new Node.VariableNode(previous().value());
        }

        return null;
    }

    private boolean end() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean check(TokenType t) {
        if(end()) return false;
        return t == peek().type();
    }

    private Token advance() {
        if(!end()) current++;
        return previous();
    }

    private Token consume(TokenType type) {
        if (check(type)) return advance();
        throw new RuntimeException("Missing )");
    }

    private boolean match(TokenType... types) {
        for(TokenType t : types) {
            if(check(t)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean isFunc(String value) {
        return BinarySearch.search(functions, value) != -1;
    }

    public List<String> getVariables() {
        return new ArrayList<>(variables);
    }
}
