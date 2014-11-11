package ast;

/**
 * A factory that produces the static Mutation objects corresponding to each
 * mutation
 */
class MutationFactory {
    static Mutation getRemove() {
        return new Remove();
    }

    static Mutation getSwap() {
    	return new Swap();
    }

    static Mutation getReplace() {
    	return new Replace();
    }

    static Mutation getTransform() {
    	return new Transform();
    }

    static Mutation getInsert() {
    	return new Insert();
    }

    static Mutation getDuplicate() {
    	return new Duplicate();
    }
}
