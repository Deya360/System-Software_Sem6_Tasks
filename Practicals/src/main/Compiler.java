package main;

import main.ast.BinOpNode;
import main.ast.ExprNode;
import main.ast.NumberNode;
import main.ast.VarNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Compiler {
    private final String SP = "    ";
    private StringBuilder sb = new StringBuilder();

    private void append(String s) {
        sb.append(s).append("\n");
    }

    private LinkedHashSet<String> fetchVars(ExprNode node) {
        LinkedHashSet<String> usedVars = new LinkedHashSet<>();
        if (node instanceof BinOpNode) {
            BinOpNode binOp = (BinOpNode) node;
            usedVars.addAll(fetchVars(binOp.left));
            usedVars.addAll(fetchVars(binOp.right));

        } else if (node instanceof VarNode) {
            usedVars.add(((VarNode) node).id.text);
        }
        return usedVars;
    }

    private void compileExpr(ExprNode node) {
        if (node instanceof NumberNode) {
            NumberNode num = (NumberNode) node;
            append("push dword " + num.number.text);

        } else if (node instanceof BinOpNode) {
            BinOpNode binOp = (BinOpNode) node;
            compileExpr(binOp.left);
            compileExpr(binOp.right);
            append(SP + "pop ebx");
            append(SP + "pop eax");
            switch (binOp.op.type) {
                case ADD:
                    append(SP + "add eax, ebx");
                    break;
                case SUB:
                    append(SP + "sub eax, ebx");
                    break;
                case MUL:
                    append(SP + "imul eax, ebx");
                    break;
                case DIV:
                    append(SP + "mov edx, 0");
                    append(SP + "idiv ebx");
                    break;
            }
            append(SP + "push eax");

        } else if (node instanceof VarNode) {
            append(SP + "push dword [" + ((VarNode) node).id + "]");

        } else {
            throw new RuntimeException("Should not happen!");
        }
    }


    private void compile32(ExprNode node) {
        //section .text
        append("section .text");
        append(SP + "global _main");
        append(SP + "extern _printf");
        append(SP + "extern _scanf");
        append("");

        //main
        LinkedHashSet<String> usedVars = fetchVars(node);

        append("_main:");
        for (String var : usedVars) {
            append(SP + "push " + var + "@prompt");
            append(SP + "call _printf");
            append(SP + "pop ebx");

            append(SP + "push " + var);
            append(SP + "push scanf_format");
            append(SP + "call _scanf");
            append(SP + "pop ebx");
            append(SP + "pop ebx");
        }

        compileExpr(node);
        append(SP + "push message");
        append(SP + "call _printf");
        append(SP + "pop ebx");
        append(SP + "pop ebx");
        append(SP + "ret");
        append("");
        //append("message:");

        //section .rdata
        append("section .rdata");
        append("message: db 'Result is %d', 10, 0");
        append("scanf_format: db '%d', 0");

        for (String var : usedVars) {
            append(var + "@prompt: db 'Input " + var + ": ', 0");
        }
        append("");

        //section .bss
        append("section .bss");
        for (String var : usedVars) {
            append(var + ": resd 1");
        }
    }

    private void writeToFile(String path) throws IOException {
        try (Writer writer =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write(sb.toString());
        }
    }

    private void printToConsole() {
        System.out.println(sb.toString());
    }


    public static void main(String[] args) {
        String text = "x + 20 * (3 + y)";

        Lexer l = new Lexer(text);
        List<Token> tokens = l.lex();
        tokens.removeIf(t -> t.type == TokenType.SPACE);

        Parser p = new Parser(tokens);
        ExprNode node = p.parseExpression();

        Compiler compiler = new Compiler();
        compiler.compile32(node);

        compiler.printToConsole();

        try {
            compiler.writeToFile("program.asm");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write program to file");
        }

    }
}