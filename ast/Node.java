package ast;



import java.util.ArrayList;

/**
 * A node in the abstract syntax tree of a program.
 */
public interface Node {
	
    /**
     * The number of nodes in the AST rooted at this node, including the current
     * one.
     * 
     * @return The size of the AST rooted at this node
     */
    int size();

    /**
     * Returns the node at {@code index} in the AST rooted at this node. Indices
     * are defined such that:<br>
     * 1. Indices are in the range {@code [0, size())}<br>
     * 2. {@code this.nodeAt(0) == this} for all nodes in the AST
     * 
     * @param index
     *            The index of the node to retrieve
     * @return The node at {@code index}
     */
    Node nodeAt(int index);

    /**
     * Appends the program represented by this node prettily to the given
     * StringBuilder.
     * 
     * @param sb
     *            The StringBuilder to which the program will be appended
     */
    void prettyPrint(StringBuilder sb);

    /**
     * Returns the pretty-print of the abstract syntax subtree
     * rooted at this node.
     * @return The pretty-print of the AST rooted at this node.
     */
    
    @Override
    String toString();
    
    /**
     * Make a copy of the subtree rooted at current node
     * @return the new copy of current node
     */
    Node copy();
    /**
     * Returns the entire array of non-null child nodes
     * @return an array list of child nodes. It's empty array list if the node has no child.
     */
    ArrayList<Node> getChildren();
    /**
     * Check whether node {@code n} can be a possible child of current node
     * @param n node to be checked
     * @return true if node {@code n} is the right type.
     */
    boolean isPossibleChild(Node n);
    /**
     * Remove child node {@code n} and its descendants from current node if valid.
     * Requires the subtree rooted at current node is still valid after this removal.
     * @param n the node to be removed
     * @return true if the removal is successful.
     */
    boolean removeChild(Node n);
    /**
     * Replace the subtree rooted at Node {@code c} with a subtree rooted at {@code n}.
     * Requires: {@code c} is a child node of current node; the subtree rooted at current node
     * remains valid after replacing.
     * @param c the root of the original subtree.
     * @param n the root of new tree to replace {@code c}.
     * @return true if the replacement is successful.
     */
    boolean replaceChild(Node c, Node n);
}
