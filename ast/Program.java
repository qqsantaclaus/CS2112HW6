package ast;


import interpret.InterpreterImpl;


import exceptions.SyntaxError;

/**
 * An abstraction of a critter program.
 */
public interface Program extends Node {
    /**
     * Mutates this program with a single mutation
     * @return The root of the mutated AST
     * @throws SyntaxError 
     */
    Program mutate() throws SyntaxError;

    /**
     * Mutates {@code nodeAt(index)} (and not its children) with mutation
     * {@code m}.
     * @param index The index of the node to mutate
     * @param m The mutation to perform on the node
     * @return The node resulting from the mutation, or {@code null}
     *         if the mutation cannot be performed
     * @throws SyntaxError 
     */
    Node mutate(int index, Mutation m) throws SyntaxError;
    /**
     * Find the eariliest executable rule in rule set.
     * Return null if there's no rule executable.
     * @return the list of commands for the executable rule.
     */

	Rule accept(InterpreterImpl i);

	
}
