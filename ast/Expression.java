package ast;

import interpret.InterpreterImpl;

/**
 * A critter program expression that has an integer value.
 */
public interface Expression extends Node {
	
	/**
	 * Return the int value represented by expression.
	 * @return int value of expression
	 */
	int accept(InterpreterImpl i);
}
