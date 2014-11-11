package console;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import ast.ProgramImpl;
import parse.ParserFactory;
import exceptions.SyntaxError;
import simulation.Critter;
import simulation.HexCoord;
import simulation.WorldImpl;

/** The console user interface for Assignment 5. */
public class Console {
	private Scanner scan;
	public boolean done;
	private WorldImpl world = null;

	public static void main(String[] args) {
		try{
			Console console = new Console();
		

		while (!console.done) {
			System.out
					.print("Enter a command or \"help\" for a list of commands.\n> ");
			console.handleCommand();
		}
		
		}catch(SyntaxError se){
			System.out.println(se.getMessage());
		}catch(FileNotFoundException f){
			System.out.println("file not found");
			System.out.println(f.getMessage());
		}catch (InputMismatchException e) {
			System.out.println("given file contains wrong input type");
            System.out.println(e.getMessage());
        }catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
        }
	}

	/**
	 * Processes a single console command provided by the user.
	 * 
	 * @throws SyntaxError
	 * @throws IOException
	 */
	void handleCommand() throws SyntaxError, IOException {
		String command = scan.next();
		switch (command) {
		case "new": {
			newWorld();
			break;
		}
		case "load": {
			String filename = scan.next();
			loadWorld(filename);
			break;
		}
		case "critters": {
			String filename = scan.next();
			int n = scan.nextInt();
			loadCritters(filename, n);
			break;
		}
		case "step": {
			int n = scan.nextInt();
			advanceTime(n);
			break;
		}
		case "info": {
			worldInfo();
			break;
		}
		case "hex": {
			int c = scan.nextInt();
			int r = scan.nextInt();
			hexInfo(c, r);
			break;
		}
		case "help": {
			printHelp();
			break;
		}
		case "exit": {
			done = true;
			break;
		}
		default:
			System.out.println(command + " is not a valid command.");
		}
	}

	/**
	 * Constructs a new Console capable of reading the standard input.
	 */
	public Console() {
		scan = new Scanner(System.in);
		done = false;
	}

	/**
	 * Starts new random world simulation.
	 * 
	 * @throws FileNotFoundException
	 */
	private void newWorld() throws FileNotFoundException {
		loadConstant();
		world = new WorldImpl((int) WorldConstants.getConstant("COLUMNS"),
				(int) WorldConstants.getConstant("ROWS"));
		int times = (new Random().nextInt(this.world.availableHex().size())) / 4;
		for (int i = 0; i < times; i++) {
			this.world.randomPlaceRock();
		}
	}

	/**
	 * Starts new simulation with world specified in filename.
	 * 
	 * @param filename
	 * @throws SyntaxError
	 * @throws IOException
	 */
	private void loadWorld(String filename) throws SyntaxError, IOException {
		loadConstant();
		InputStream is = new FileInputStream(filename);
		Reader r = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(r);
		String s = meaningfulString(br);
		if (!s.contains("name ")) {
			se.setMessage("World file does not contain a name");
			throw se;
		}
		String name = s.replaceFirst("name ", "");
		s = meaningfulString(br);
		if (!s.contains("size ")) {
			se.setMessage("World file does not contain size");
			throw se;
		}
		s = s.replaceFirst("size ", "");
		Scanner strscan = new Scanner(s);
		int cn = strscan.nextInt();
		int rn = strscan.nextInt();
		world = new WorldImpl(name, cn, rn);
		while (br.ready()) {
			s = meaningfulString(br);
			if (s.contains("rock ")) {
				s = s.replaceFirst("rock ", "");
				strscan = new Scanner(s);
				int rc = strscan.nextInt();
				int rr = strscan.nextInt();
				world.setRock(new HexCoord(rc, rr));
			}
			if (s.contains("critter ")) {
				s = s.replaceFirst("critter ", "");
				strscan = new Scanner(s);
				String critterfile = strscan.next();
				int cc = strscan.nextInt();
				int cr = strscan.nextInt();
				int dir = strscan.nextInt();
				Critter critter = loadCritter(critterfile);
				world.setCritter(critter, new HexCoord(cc, cr));
				world.setDirection(critter, dir);
			}
		}
		System.out.println(world.name + " has been loaded!");
		strscan.close();
	}

	private void loadConstant() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("src/console/constant.txt"));
		while (scanner.hasNext()) {
			String s = scanner.nextLine();
			Scanner ss = new Scanner(s);
			String c = ss.next();
			double d = ss.nextDouble();
			WorldConstants.addConstant(c, d);
			ss.close();
		}
		scanner.close();
	}

	private Critter loadCritter(String critterfile) throws IOException,
			SyntaxError {
		InputStream is = new FileInputStream(critterfile);
		Reader r = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(r);
		String s = meaningfulString(br);
		if (!s.contains("species: ")) {
			se.setMessage("Critter file does not contain a species name");
			throw se;
		}
		String species = s.replaceFirst("species: ", "");
		s = meaningfulString(br);
		int[] mem = new int[8];
		mem[0] = 8; // memsize
		mem[1] = 1; // defense
		mem[2] = 1; // offense
		mem[3] = 1; // size
		mem[4] = 250; // energy
		mem[5] = 0; // pass
		mem[6] = 0; // tag
		mem[7] = 0; // posture
		while (ismem(s)) {
			if (s.contains("memsize: ")) {
				s = s.replaceAll("memsize: ", "");
				int n = scanInt(s);
				if (n > 8) {
					mem[0] = n;
					int[] memTemp = Arrays.copyOf(mem, mem[0]);
					mem = memTemp;
				}
			}
			if (s.contains("defense: ")) {
				s = s.replaceAll("defense: ", "");
				int n = scanInt(s);
				if (n > 1)
					mem[1] = n;

			}
			if (s.contains("offense: ")) {
				s = s.replaceAll("offense: ", "");
				int n = scanInt(s);
				if (n > 1)
					mem[2] = n;
			}
			if (s.contains("size: ")) {
				s = s.replaceAll("size: ", "");
				int n = scanInt(s);
				if (n > 1)
					mem[3] = n;
			}
			if (s.contains("energy: ")) {
				s = s.replaceAll("energy: ", "");
				int n = scanInt(s);
				if (n > 1)
					mem[4] = n;
			}
			if (s.contains("posture: ")) {
				s = s.replaceAll("posture: ", "");
				int n = scanInt(s);
				if (n > 0 && n <= 99)
					mem[7] = n;
			}

			s = meaningfulString(br);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(s);
		while (br.ready()) {
			String t = meaningfulString(br);
			sb.append(t);
		}
		Reader reader = new StringReader(sb.toString());
		ProgramImpl p = (ProgramImpl) ParserFactory.getParser().parse(reader);
		Critter critter = new Critter(species, mem, 0, p, world);
		return critter;
	}

	private int scanInt(String s) {
		Scanner strscan = new Scanner(s);
		int i = strscan.nextInt();
		strscan.close();
		return i;
	}

	private boolean ismem(String s) {

		return s.contains("memsize: ") || s.contains("offense: ")
				|| s.contains("defense: ") || s.contains("size: ")
				|| s.contains("energy: ") || s.contains("posture: ");
	}

	/**
	 * Loads critter definition from filename and randomly places n critters
	 * with that definition into the world.
	 * 
	 * @param filename
	 * @param n
	 * @throws SyntaxError
	 * @throws IOException
	 */
	private void loadCritters(String filename, int n) throws IOException,
			SyntaxError {
		Random r = new Random();
		for (int i = 0; i < n; i++) {
			Critter critter = loadCritter(filename);
			world.setDirection(critter, r.nextInt(6));
			world.randomPlaceCritter(critter);
		}
	}

	/**
	 * Advances the world by n time steps.
	 * 
	 * @param n
	 * @throws SyntaxError
	 */
	private void advanceTime(int n) throws SyntaxError {
		world.advance(n);
	}

	/**
	 * Prints current time step, number of critters, and world map of the
	 * simulation.
	 */
	private void worldInfo() {
		world.worldInfo();
	}

	/**
	 * Prints description of the contents of hex (c,r).
	 * 
	 * @param c
	 *            column of hex
	 * @param r
	 *            row of hex
	 */
	private void hexInfo(int c, int r) {
		world.printDescription(c, r);
	}

	/**
	 * Prints a list of possible commands to the standard output.
	 */
	private void printHelp() {
		System.out.println("new: start a new simulation with a random world");
		System.out.println("load <world_file>: start a new simulation with "
				+ "the world loaded from world_file");
		System.out.println("critters <critter_file> <n>: add n critters "
				+ "defined by critter_file randomly into the world");
		System.out.println("step <n>: advance the world by n timesteps");
		System.out.println("info: print current timestep, number of critters "
				+ "living, and map of world");
		System.out.println("hex <c> <r>: print contents of hex "
				+ "at column c, row r");
		System.out.println("exit: exit the program");
	}

	protected static SyntaxError se = new SyntaxError();

	private String meaningfulString(BufferedReader br) throws IOException {
		String s = br.readLine();

		s = s.replaceAll("//(.*)", "");
		while (s == "" && br.ready()) {
			s = br.readLine();
		}
		return s;
	}

}
