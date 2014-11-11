package parse;

import java.io.Reader;

import exceptions.SyntaxError;
import ast.Program;

/**
 * An interface for parsing a critter program.
 *
 */
public interface Parser {

    /**
     * Parses a program in the given file.
     * @param r A reader to read the program
     * @return The parsed program, or null if the program contains a syntax error.
     * @throws SyntaxError 
     */
    Program parse(Reader r) throws SyntaxError;
}
