package parse;

import java.io.Reader;

import ast.BinaryCondition;
import ast.BinaryCondition.Operator;
import ast.BinaryExpr;
import ast.BinaryRelation;
import ast.Condition;
import ast.Expression;
import ast.FunctionNode;
import ast.MessageNode;
import ast.Program;
import ast.ProgramImpl;
import ast.Rule;
import ast.UnaryExpr;
import ast.UpdateNode;
import ast.VariableExpr;
import ast.VariableExpr.Message;
import ast.Number;
import exceptions.SyntaxError;

class ParserImpl implements Parser {
	
	/**
     * Parses a program in the given file.
     * @param r A reader to read the program
     * @return The parsed program, or null if the program contains a syntax error.
     * @throws SyntaxError 
     */
    @Override
    public Program parse(Reader r)  {
    	Program p=null;
    	try{
    		p= parseProgram(new Tokenizer(r));
    	}catch(SyntaxError e){
    		System.err.println("Syntax error "+e.getMessage());
    	}
		return p;
    }

    /** Parses a program from the stream of tokens provided by the Tokenizer,
     *  consuming tokens representing the program. All following methods with
     *  a name "parseX" have the same spec except that they parse syntactic form
     *  X.
     *  @return the created AST
     *  @throws SyntaxError if there the input tokens have invalid syntax 
     */
    public static Program parseProgram(Tokenizer t) throws SyntaxError {
    	ProgramImpl program=new ProgramImpl();   
    	while(t.hasNext() && !t.peekType(TokenType.EOF)){
    		program.addRule(parseRule(t));
    		consume(t,TokenType.SEMICOLON);
    	}
        return program;
    }

    /**
     * Parses a rule from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the rule.
     * @param t the given tokenizer
     * @return the created Rule node
     * @throws SyntaxError if there 
     */
    public static Rule parseRule(Tokenizer t) throws SyntaxError {
    	Rule rule=new Rule(parseCondition(t));
    	consume(t,TokenType.ARR);

    	while(t.hasNext() && (t.peekType(TokenType.MEM)||t.peek().getType().getCategory().equals(TokenCategory.MEMSUGAR))){
    		rule.addCommand(parseUpdate(t));
    	}
    	if(t.hasNext() && !t.peekType(TokenType.SEMICOLON)){
    		switch(t.peek().getType()){
    			case TAG:
    				rule.addCommand(parseFunction(t));
    				break;
    			case SERVE:	 //function
    				rule.addCommand(parseFunction(t));
    				break;
    			default:
    				rule.addCommand(parseMessage(t));
    		}	
    	}
       return rule;
    }

    /**
     * Parse a condition from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the Condition
     * @param t the given tokenizer
     * @return the created Condition node
     * @throws SyntaxError if there
     */
    public static Condition parseCondition(Tokenizer t) throws SyntaxError {
    	Condition c=parseConjunction(t);
    	while(t.peekType(TokenType.OR)){ //while contains "or"
    		consume(t,TokenType.OR);
    		c=new BinaryCondition(c,Operator.OR,parseConjunction(t));
    	}
    	return c;
    }
    
    /**
     * Parse an Expression from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the Expression
     * @param t the given tokenizer
     * @return the created Expression node
     * @throws SyntaxError if there
     */
    public static Expression parseExpression(Tokenizer t) throws SyntaxError {
    	Expression l=parseTerm(t);    	
		while(t.peek().isAddOp()){
			ast.BinaryExpr.Operator op = null;
			switch(t.peek().getType()){
			
			case PLUS:
				op=BinaryExpr.Operator.PLUS;
				consume(t,TokenType.PLUS);
				break;
			case MINUS:
				op=BinaryExpr.Operator.MINUS;
				consume(t,TokenType.MINUS);
				break;
    		default:
    			error("Unexpected term operator at line"+t.peek().lineNumber());
			}
			l=new BinaryExpr(l,op,parseTerm(t));
		}
		return l;
    }

    /**
     * Parse a term from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the term
     * @param t the given tokenizer
     * @return the created Expression node
     * @throws SyntaxError if there
     */
    public static Expression parseTerm(Tokenizer t) throws SyntaxError {
    	Expression l=parseFactor(t);   	
    	while(t.peek().isMulOp()){
    		ast.BinaryExpr.Operator op=null;
    		switch(t.peek().getType()){
    			case MUL:
    				op=BinaryExpr.Operator.TIMES;
    				consume(t,TokenType.MUL);
    				break;
    			case DIV: 
    				op=BinaryExpr.Operator.DIVIDE;
    				consume(t,TokenType.DIV);
    				break;
    			case MOD: 
    				op=BinaryExpr.Operator.MOD;
    				consume(t,TokenType.MOD);
    				break;
    			default:
    				error("Unexpected factor operator at line"+t.peek().lineNumber());
    		}
    		l=new BinaryExpr(l,op,parseFactor(t));
    	}
    	return l;
    }

    /**
     * Parse a factor from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the factor
     * @param t the given tokenizer
     * @return the created Expression node
     * @throws SyntaxError if there
     */
    public static Expression parseFactor(Tokenizer t) throws SyntaxError { 
    	switch(t.peek().getType()){    	
    	case NUM: 
    		Number num= new Number(t.next().toNumToken().getValue());  
    		return num;
    	case LPAREN:
    		consume(t,TokenType.LPAREN);
    		Expression be=parseExpression(t);
    		consume(t,TokenType.RPAREN);
    		return be;    	
    	case MINUS:
    		consume(t,TokenType.MINUS);
    		ast.UnaryExpr.Operator op = UnaryExpr.Operator.NEGATE;
    		return new UnaryExpr(op,parseFactor(t));
    	case AHEAD :
    		consume(t,TokenType.AHEAD);
    		consume(t,TokenType.LBRACKET);
    		VariableExpr varExpr=new VariableExpr(Message.ahead,parseExpression(t));
    		 consume(t,TokenType.RBRACKET);
    		 return varExpr;
    	case MEM:
    		consume(t,TokenType.MEM);
    		consume(t,TokenType.LBRACKET);
    		varExpr=new VariableExpr(Message.mem,parseExpression(t));
    		consume(t,TokenType.RBRACKET);
    		return varExpr;
    	case NEARBY:
    		consume(t,TokenType.NEARBY);
    		consume(t,TokenType.LBRACKET);
    		varExpr=new VariableExpr(Message.nearby,parseExpression(t));
    		consume(t,TokenType.RBRACKET);
    		return varExpr;
    	case RANDOM:
    		consume(t,TokenType.RANDOM);
    		consume(t,TokenType.LBRACKET);
    		varExpr=new VariableExpr(Message.random,parseExpression(t));
    		consume(t,TokenType.RBRACKET);
    		return varExpr;
    	case SMELL:
    		consume(t,TokenType.SMELL);
    		return new VariableExpr(Message.smell,null);
		default:
			return new VariableExpr(Message.mem,getSugar(t));
    	}   	  	
    }

    // add more as necessary...
    /**
     * Parse a conjunction from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the conjunction
     * @param t the given tokenizer
     * @return the created BinaryCondition node
     * @throws SyntaxError if there
     */
    public static Condition parseConjunction(Tokenizer t) throws SyntaxError{
    	Condition l= parseRelation(t);
    	while(t.peekType(TokenType.AND)){
    		consume(t,TokenType.AND);
    		l=new BinaryCondition(l,Operator.AND,parseRelation(t));
    	}
		return  l;
    }
    
    /**
     * Parse a Relation from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the Relation
     * @param t the given tokenizer
     * @return the created BinaryRelation node
     * @throws SyntaxError if there
     */
    public static Condition parseRelation(Tokenizer t) throws SyntaxError{
    	Expression l=null;
    	ast.BinaryRelation.Operator op=null;
    	if(t.peekType(TokenType.LBRACE)){
    		consume(t,TokenType.LBRACE);
    		Condition br= parseCondition(t);
    		consume(t,TokenType.RBRACE);
    		return br;
    	}else{
    		l=parseExpression(t);
    		switch(t.peek().getType()){
    			case LT: 
    				op=BinaryRelation.Operator.SMALLER;
    				consume(t,TokenType.LT);
    				break;
    			case LE: 
    				op=BinaryRelation.Operator.SMALLEREQ;
    				consume(t,TokenType.LE);
    				break;
    			case EQ: 
    				op=BinaryRelation.Operator.EQUAL;
    				consume(t,TokenType.EQ);
    				break;
    			case GE: 
    				op=BinaryRelation.Operator.BIGGEREQ;
    				consume(t,TokenType.GE);
    				break;
    			case GT: 
    				op=BinaryRelation.Operator.BIGGER;
    				consume(t,TokenType.GT);
    				break;
    			case NE: 
    				op=BinaryRelation.Operator.NOTEQUAL;
    				consume(t,TokenType.NE);
    				break;
    			default:
    				error("Unexpected relation operator"+t.peek().getType());
    		}
    		return new BinaryRelation(l,op,parseExpression(t));
    	}
    }
    
    /**
     * Parse an Update sentence from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the Update
     * @param t the given tokenizer
     * @return the created Update node
     * @throws SyntaxError if there
     */
    public static UpdateNode parseUpdate(Tokenizer t) throws SyntaxError{
    	if(t.peekType(TokenType.MEM)){
    		consume(t,TokenType.MEM);
    		consume(t,TokenType.LBRACKET);
    		Expression index=parseExpression(t);
    		consume(t,TokenType.RBRACKET);
    		consume(t,TokenType.ASSIGN);
    		Expression assign=parseExpression(t);
    		return new UpdateNode(index,assign);
    	}else{
    		Expression index=getSugar(t);
    		consume(t,TokenType.ASSIGN);
    		Expression assign=parseExpression(t);
    		return new UpdateNode(index,assign);
    	}
    }
    
    /**
     * Parse a Message from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the Message
     * @param t the given tokenizer
     * @return the created Message node
     * @throws SyntaxError if there
     */
    public static MessageNode parseMessage(Tokenizer t) throws SyntaxError{
    	switch(t.peek().getType()){
    	case WAIT: 
    		consume(t,TokenType.WAIT);
    		return new MessageNode(MessageNode.Message.wait);
    	case FORWARD: 
    		consume(t,TokenType.FORWARD);
    		return new MessageNode(MessageNode.Message.forward);
    	case BACKWARD: 
    		consume(t,TokenType.BACKWARD);
    		return new MessageNode(MessageNode.Message.backward);
    	case LEFT: 
    		consume(t,TokenType.LEFT);
    		return new MessageNode(MessageNode.Message.left);
    	case RIGHT: 
    		consume(t,TokenType.RIGHT);
    		return new MessageNode(MessageNode.Message.right);
    	case EAT: 
    		consume(t,TokenType.EAT);
    		return new MessageNode(MessageNode.Message.eat);
    	case ATTACK: 
    		consume(t,TokenType.ATTACK);
    		return new MessageNode(MessageNode.Message.attack);
    	case GROW: 
    		consume(t,TokenType.GROW);
    		return new MessageNode(MessageNode.Message.grow);
    	case BUD: 
    		consume(t,TokenType.BUD);
    		return new MessageNode(MessageNode.Message.bud);
    	case MATE: 
    		consume(t,TokenType.MATE);
    		return new MessageNode(MessageNode.Message.mate);
		default: 
			se.setMessage("Unexpected action at line"+t.peek().lineNumber());
			throw se;
    	}
    }
    
    /**
     * Parse a Function from the stream of tokens provided by the Tokenizer
     * consuming tokens representing the Function
     * @param t the given tokenizer
     * @return the created Function node
     * @throws SyntaxError if there
     */
    public static FunctionNode parseFunction(Tokenizer t) throws SyntaxError{
    	switch(t.peek().getType()){
    	case TAG:
    		consume(t,TokenType.TAG);
    		consume(t,TokenType.LBRACKET);
    		Expression e=parseExpression(t);
    		consume(t,TokenType.RBRACKET);
    		return new FunctionNode(FunctionNode.Message.tag,e);
    	
    	case SERVE:
    		consume(t,TokenType.SERVE);
    		consume(t,TokenType.LBRACKET);
    		Expression ex=parseExpression(t);
    		consume(t,TokenType.RBRACKET);
    		return new FunctionNode(FunctionNode.Message.serve,ex);    	
    	
		default:
			se.setMessage("Unexpected action"+t.peek().getType());
			throw se;
    	}
    }
    
    /**
     * transform syntatic sugar provided by the Tokenizer into mem[expr], expr is an integer between 0 and 7
     * @param t the given tokenizer
     * @return an Expression that is a Number node, the int value is between 0 and 7
     * @throws SyntaxError
     */
    private static Expression getSugar(Tokenizer t) throws SyntaxError{
    	switch(t.next().getType()){
    	case ABV_MEMSIZE:
    		return new Number(0);
    	case ABV_DEFENSE:
    		return new Number(1);
    	case ABV_OFFENSE:
    		return new Number(2);
    	case ABV_SIZE:
    		return new Number(3);
    	case ABV_ENERGY:
    		return new Number(4);
    	case ABV_PASS:
    		return new Number(5);
    	case ABV_TAG:
    		return new Number(6);
    	case ABV_POSTURE:
    		return new Number(7);
		default:
			se.setMessage("Unexpected syntactic sugar " + t.next().getType() + " for " + TokenType.MEM);
			throw se;   		
    	}
    }
   
    /**
     * Consumes a token of the expected type.
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
    	if(t.peekType(tt)){
    		t.next();
    	}
    	else{
    		error("Expected " + tt + " at line " + t.next().lineNumber());
    	}
    }
    
    protected static SyntaxError se = new SyntaxError();

    protected static void error(String msg) throws SyntaxError {
        se.setMessage(msg);
        throw se;
    }
}
