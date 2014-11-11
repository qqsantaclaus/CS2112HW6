package interpret;

import simulation.Critter;
import ast.Condition;
import ast.Expression;
import ast.Program;

/**
 * An interface for interpreting a critter program.
 */
public interface Interpreter {
    /**
     * Execute program {@code p} until either the maximum number of rules per
     * turn is reached or some rule whose command contains an action is
     * executed.
     * @param p
     * @return a result containing the action to be performed;
     * the action may be null if the maximum number of rules
     * per turn was exceeded.
     */
    Outcome interpret(Program p, Critter critter);

    /**
     * Evaluate the given condition.
     * @param c
     * @return a boolean that results from evaluating c.
     */
    boolean eval(Condition c);

    /**
     * Evaluate the given expression.
     * @param e
     * @return an integer that results from evaluating e.
     */
    int eval(Expression e);
}
