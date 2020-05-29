package main;

import java.util.regex.Pattern;

public enum TokenType {
    NUMBER("[0-9]+"),
    ID("[_a-zA-Z]+"),
    ADD("\\+"),
    SUB("-"),
    MUL("\\*"),
    DIV("/"),
    LPAR("\\("),
    RPAR("\\)"),
    SPACE("[ \t\r\n]+");

    final Pattern pattern;

    TokenType(String regexp) {
        pattern = Pattern.compile(regexp);
    }
}
