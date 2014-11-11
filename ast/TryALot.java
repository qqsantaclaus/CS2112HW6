package ast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import parse.Parser;
import parse.ParserFactory;
import exceptions.SyntaxError;

public class TryALot {

	public static void main(String[] args) throws FileNotFoundException, SyntaxError {
		InputStream is=new FileInputStream("test/test/ValidTest1.txt");
		Reader r=new InputStreamReader(is);
		for (int i=0; i<1500; i++){
			Parser ps=ParserFactory.getParser();
			String s = "";
			Program pg=ps.parse(r);
			if (pg==null){;
				System.out.println("syntax!");
			}
			else {
				s=pg.mutate().toString();
			}
			r=new StringReader(s);
			System.out.println(i+"th ");
			System.out.println(s);
		}

	}

}
