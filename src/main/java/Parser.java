import java.util.*;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;
    private final HashMap<String, Double> constants = new HashMap<>();
    private final List<String> functions = new ArrayList<>();
    private final SortedSet<String> variables = new TreeSet<>();

    public Parser() {
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

    public void setTokens(List<Token> tokens) {
        this.currentTokenIndex = 0;
        this.tokens = tokens;
    }

    public Node expression() {

        return equals().simplify();
    }

    private Node equals() {
        Node node = term();

        if (match(TokenType.EQUALS)) {
            Token t = previousToken();
            Node right = term();
            return new Node.EqualNode(node, t, right);
        }

        return node;
    }

    private Node term() {
        Node node = factor();

       while (match(TokenType.ADD, TokenType.SUB)) {
            Token operator = previousToken();
            Node right = factor();
            node = new Node.BinaryNode(node, operator, right);
        }

        return node;
    }

    private Node factor() {
        Node node = pow();

        while (match(TokenType.MUL, TokenType.DIV)) {
            Token operator = previousToken();
            Node right = pow();
            node = new Node.BinaryNode(node, operator, right);
        }
        return node;
    }

    private Node pow() {
        Node node = func();

        while (match(TokenType.POW)) {
            Token operator = previousToken();
            Node right = func();
            node = new Node.BinaryNode(node, operator, right);
        }
        return node;
    }

    private Node func() {
        Node node = unary();
        if(check(TokenType.VAR) && isFunc(currentToken().value())) {
            advance();
            String functionName = previousToken().value();
            Node child = unary();
            return new Node.FunctionNode(functionName, child);
        }
        return node;
    }

    private Node unary() {
        Node node = primary();

        if(match(TokenType.MINUS, TokenType.FACT)) {
            Token t = previousToken();
            Node child = primary();
            return new Node.UnaryNode(child, t);
        }
        return node;
    }

    private Node primary() {
        if (match(TokenType.NUM)) return new Node.NumberNode(previousToken().value());
        else if (match(TokenType.PAR_L)) {
            Node node = expression();
            consume(TokenType.PAR_R);
            return new Node.GroupingNode(node);
        }
        else if(check(TokenType.VAR) && constants.containsKey(currentToken().value())) {
            advance();
            return new Node.NumberNode(constants.get(previousToken().value()));
        }
        else if (check(TokenType.VAR) && !isFunc(currentToken().value())) {
            advance();
            variables.add(previousToken().value());
            return new Node.VariableNode(1, previousToken().value(), 1);
        } else return null;
    }

    private boolean end() {
        return currentToken().type() == TokenType.EOF;
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private Token previousToken() {
        return tokens.get(currentTokenIndex - 1);
    }

    private boolean check(TokenType t) {
        if(end()) return false;
        return t == currentToken().type();
    }

    private Token advance() {
        if(!end()) currentTokenIndex++;
        return previousToken();
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
