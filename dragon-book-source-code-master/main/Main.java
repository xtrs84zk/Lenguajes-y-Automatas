package main;

import lexer.Lexer;
import parser.Parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer();
        Parser parse = new Parser(lex);
        parse.program();
        System.out.write('\n');
    }
}
