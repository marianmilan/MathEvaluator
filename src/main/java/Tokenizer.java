import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final List<Token> tokens;
    private final String expression;
    private final int length;
    private byte position;
    private byte parentheses;
    private byte equalSign;

    public Tokenizer(String expression) {
        this.expression = convertExpression(expression);
        this.tokens = new ArrayList<>();
        this.length = expression.length();
        this.position = 0;
        this.parentheses = 0;
        this.equalSign = 0;
    }

    public List<Token> tokenize() {
        int len = expression.length();
        TokenType prev = null;

        while(position < len) {
            char c = expression.charAt(position);
            if (Character.isWhitespace(c)) {position++; continue;}

            if (Character.isDigit(c) || c == '.') { tokens.add(createNumToken()); position--; }
            if (Character.isAlphabetic(c)) { tokens.add(createAlphaToken()); position--; }
            if (c == '-') tokens.add(createMinusToken());
            if (c == '(') tokens.add(createBracketToken());
            if (c == ')') tokens.add(createBracketToken());
            if (c == '+') tokens.add(new Token(TokenType.ADD, "+"));
            if (c == '*') tokens.add(new Token(TokenType.MUL, "*"));
            if (c == '/') tokens.add(new Token(TokenType.DIV, "/"));
            if (c == '^') tokens.add(new Token(TokenType.POW, "^"));
            if (c == '!') tokens.add(new Token(TokenType.FACT, "!"));
            if (c == '=') {
                equalSign++;
                tokens.add(new Token(TokenType.EQUALS, "="));
            }
            position++;
        }

        if (parentheses != 0) {
            String message = parentheses > 0 ? "Missing )" : "Missing '('";
            throw new RuntimeException(message);
        }

        if (equalSign > 1) throw new RuntimeException("Multiple equal signs");

        checkOperators();
        tokens.add(new Token(TokenType.EOF, null));
        swapFactorial();
        return tokens;
    }

    private Token createNumToken() {
        int start = position;
        boolean dot = false;
        while(position < length) {
            char c = expression.charAt(position);
            if (c == '.') {
                if(dot) throw new RuntimeException("Multiple dots in number");
                dot = true;
            }
            else if (!Character.isDigit(c)) break;
            position++;
        }
        return new Token(TokenType.NUM, expression.substring(start, position));
    }

    private Token createAlphaToken() {
        int start = position;
        while(position < length) {
            char c = expression.charAt(position);
            if (!Character.isAlphabetic(c) && c != '_') break;
            position++;
        }
        if(!tokens.isEmpty() && tokens.getLast().type() == TokenType.NUM)
            tokens.add(new Token(TokenType.MUL, "*"));
        return new Token(TokenType.VAR, expression.substring(start, position));
    }

    private Token createMinusToken() {
        if(tokens.isEmpty()) return new Token(TokenType.MINUS, "-");
        TokenType prev = tokens.getLast().type();
        if (isOperator(prev) || prev == TokenType.PAR_L) return new Token(TokenType.MINUS, "-");
        return new Token(TokenType.SUB, "-");
    }

    private Token createBracketToken() {
        char c = expression.charAt(position);

        if (c == '(') {
            if (!tokens.isEmpty() && tokens.getLast().type() == TokenType.NUM) {
                tokens.add(new Token(TokenType.MUL, "*"));
            }
            parentheses++;
            return new Token(TokenType.PAR_L, "(");
        } else {
            parentheses--;
            return new Token(TokenType.PAR_R, ")");
        }
    }

    private boolean isOperator(TokenType t) {
        return switch (t) {
            case ADD, SUB, DIV, MUL, POW -> true;
            default -> false;
        };
    }

    private String convertExpression(String s) {
        String res = s.replaceAll("[{\\[]", "(");
        res = res.replaceAll("[]}]", ")");
        res = res.replaceAll("[,]", ".");
        return res.toLowerCase();
    }

    private void checkOperators() {
        Token prev = null;
        for (Token curr : tokens) {
            if (prev != null && isOperator(prev.type()) && isOperator(curr.type()))
                throw new RuntimeException("Invalid use of operators");
            prev = curr;
        }
    }

    private void swapFactorial() {
        byte i = 0;
        while (tokens.get(i).type() != TokenType.EOF) {
            if (tokens.getFirst().type() == TokenType.FACT) throw new RuntimeException("Bad use of factorial operator");
            if (tokens.get(i).type() == TokenType.FACT) {
                Token current = tokens.get(i);
                Token prev = tokens.get(i - 1);
                tokens.set(i, prev);
                tokens.set(i - 1, current);
            }
            i++;
        }
    }
}
