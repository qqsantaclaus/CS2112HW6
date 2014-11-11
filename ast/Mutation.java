package ast;


/**
 * A mutation to the AST
 */
public interface Mutation {
    /**
     * Compares the type of this mutation to {@code m}
     * 
     * @param m
     *            The mutation to compare with
     * @return Whether this mutation is the same type as {@code m}
     */
    boolean equals(Mutation m);
    /**
     * Apply this mutation to the given node.
     * @param n the node to be mutated.
     * @param p the parent node of {@code n};
     * @param root the root node of the entire AST tree.
     * @return true if the mutation is successful.
     */
    boolean getMutated(Node n, Node p, Node root);
}
