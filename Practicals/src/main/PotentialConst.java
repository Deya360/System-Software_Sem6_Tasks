package main;

import main.ast.ExprNode;
import main.ast.NumberNode;

public class PotentialConst {
    ExprNode node;

    public PotentialConst(ExprNode node) {
        this.node = node;
    }

    public boolean isConstant() {
        return node instanceof NumberNode;
    }

    public int getValue() {
        if (isConstant()) return Integer.parseInt(((NumberNode)node).number.text);
        else throw new RuntimeException("getValue() should only be called if node is known to be a constant");
    }

    public boolean isEqualTo(int other) {
        if (isConstant()) return getValue() == other;
        else throw new RuntimeException("isEqualTo() should only be called if node is known to be a constant");
    }

    private void throwIfNotConst(PotentialConst other) {
        if (!isConstant() || !other.isConstant())
        throw new RuntimeException(
                Thread.currentThread().getStackTrace()[1].getMethodName() +
                " should only be called if node is known to be a constant");
    }

    public NumberNode addOther(PotentialConst other, int tokenPos) {
        throwIfNotConst(other);
        int res = getValue() + other.getValue();
        return new NumberNode(new Token(TokenType.NUMBER, String.valueOf(res), tokenPos));
    }

    public NumberNode subtractOther(PotentialConst other, int tokenPos) {
        throwIfNotConst(other);
        int res = getValue() - other.getValue();
        return new NumberNode(new Token(TokenType.NUMBER, String.valueOf(res), tokenPos));
    }

    public NumberNode multiplyByOther(PotentialConst other, int tokenPos) {
        throwIfNotConst(other);
        int res = getValue() * other.getValue();
        return new NumberNode(new Token(TokenType.NUMBER, String.valueOf(res), tokenPos));
    }

    public NumberNode divideByOther(PotentialConst other, int tokenPos) {
        throwIfNotConst(other);
        int res = getValue() / other.getValue();
        return new NumberNode(new Token(TokenType.NUMBER, String.valueOf(res), tokenPos));
    }
}
