package ast;

public abstract class NodeImpl implements Node {

	@Override
	public String toString(){
		 StringBuilder sb=new StringBuilder();
		 prettyPrint(sb);
		 return sb.toString();
	}

}