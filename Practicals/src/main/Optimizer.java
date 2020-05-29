package main;

import main.ast.BinOpNode;
import main.ast.ExprNode;
import main.ast.NumberNode;
import main.ast.VarNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Optimizer {

    private static ExprNode foldConstants(ExprNode node) {
        if (node instanceof NumberNode || node instanceof VarNode) {
            return node;

        } else if (node instanceof BinOpNode) {
            BinOpNode binOpNode = (BinOpNode) node;
            Token token = binOpNode.op;
            TokenType op = token.type;

            ExprNode left = foldConstants(binOpNode.left);
            ExprNode right = foldConstants(binOpNode.right);

            PotentialConst leftConst = new PotentialConst(left);
            PotentialConst rightConst = new PotentialConst(right);

            if (leftConst.isConstant() && rightConst.isConstant()) {
                switch (op) {
                    case ADD: return leftConst.addOther(rightConst, token.pos);
                    case SUB: return leftConst.subtractOther(rightConst, token.pos);
                    case MUL: return leftConst.multiplyByOther(rightConst, token.pos);
                    case DIV: return leftConst.divideByOther(rightConst, token.pos);
                    default: throw new RuntimeException("Should not happen!");
                }

            } else if (leftConst.isConstant())  {
                if (op==TokenType.ADD && leftConst.isEqualTo(0)) return right;
                if (op==TokenType.MUL && leftConst.isEqualTo(1)) return right;

            } else if (rightConst.isConstant()) {
                if (op==TokenType.ADD && rightConst.isEqualTo(0)) return left;
                if (op==TokenType.SUB && rightConst.isEqualTo(1)) return left;
                if (op==TokenType.MUL && rightConst.isEqualTo(1)) return left;
                if (op==TokenType.DIV && rightConst.isEqualTo(1)) return left;
            }
            return new BinOpNode(binOpNode.op, left, right);

        } else {
            throw new RuntimeException("Should not happen!");
        }
    }

    private static StringBuilder OptimizePeepholes(String instructs) {
        StringBuilder returnSb = new StringBuilder();

        String rx = "(\\s*)push (.+)\\s*pop (.+)"; //group 1 = whitespace + "\n", group 2 = first instruct, group 3 = second instruct
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(instructs);

        while (m.find()) {
            if (m.groupCount()==3) {
                String repString = m.group(1) + "mov " + m.group(3) + ", " + m.group(2);
                m.appendReplacement(returnSb, repString);
            }
        }
        m.appendTail(returnSb);

        return returnSb;
    }

    public static void main(String[] args) {
        String text = "1 + 1 + 1 + 1 + x";

        Lexer l = new Lexer(text);
        List<Token> tokens = l.lex();
        tokens.removeIf(t -> t.type == TokenType.SPACE);

        Parser p = new Parser(tokens);
        ExprNode node = p.parseExpression();
        ExprNode optimizedNode = foldConstants(node);

        Compiler compiler = new Compiler();
        compiler.compile32(optimizedNode);

        StringBuilder optimizedInstructs = OptimizePeepholes(compiler.getInstructs().toString());

        Utils.printToConsole(optimizedInstructs);

        try {
            Utils.writeToFile("program.asm", optimizedInstructs);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write program to file");
        }
    }
}
