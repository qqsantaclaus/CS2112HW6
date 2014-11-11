package ast;

import interpret.InterpreterImpl;

/**
 * An interface representing a Boolean condition in a critter program.
 *
 */
public interface Condition extends Node {
	/**
	 * Return the boolean value represented by condition.
	 * @return boolean value of condition
	 */
	boolean accept(InterpreterImpl i);
}
