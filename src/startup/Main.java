package startup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
	public static void main(String[] args) {
		try {
			System.setErr(new PrintStream(new File("logs.txt")));
			System.setOut(new PrintStream(new File("logs.txt")));
		} catch (FileNotFoundException e) {System.exit(1);}
		new Camera();
	}
}
