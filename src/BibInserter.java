//import org.jnativehook.GlobalScreen;
//import org.jnativehook.NativeHookException;
//import java.util.logging.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.io.IOException;

import java.util.HashMap;

/*
Design, UML, Classes, Code, Test

Parsing
Searching
Input (below?)
GUI
*/

/* Hotkey/Hotsting libraries
Packaging
	Hotkay
	*Hotstring
*/

public class BibInserter {
	private static Path bibFilePath;
	private static FileTime lastModificatoinTime;
	private static BibInserterGUI gui;
	private static BibShortcutGUIManager shortcuts;
	private static HashMap<String, BibEntry> BibEntries; //will be listed on search, mean anything?
	
	//exceptions
	public static void main(String[] args) {
		try {
			//setUpGUI() has to be done first, so lock and feel works properly
			//and to work with shortcuts (in current implmentation)
			setUpShortcuts(setUpGUI());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		/*//testing passed argument
			//pass file name in cd, proper
			//pass file path relative to cd, proper
			//pass absolute file path, proper
		//System.out.println(file.toAbsolutePath());
		//change time when reading again (so do this on read probably)
			//either have method on parser that takes FileTime
			//or wrap file parser in a method below
		*/
		try {
			bibFilePath = Paths.get(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: Forgot to pass a file");
			System.exit(1);
		}
		
		try {
			parseChangedBibFile(bibFilePath);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
		//improper loop, prompts are not shortcuts
		while (true) {
			System.out.print("$ ");
			String input = System.console().readLine();
			if (input.contains("s")) {
				gui.showGUI();
			} else if (input.contains("h")) {
				gui.hideGUI();
			} else if (input.contains("c")) {
				try {
					System.out.println(parseChangedBibFile(bibFilePath));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			} else if (input.contains("t")) {
				System.out.println(lastModificatoinTime);
			} else if (input.contains("q")) {
				System.exit(0);
			}
		}
	}
	
	public static BibInserterGUI setUpGUI() throws UnsupportedLookAndFeelException,
											ClassNotFoundException,
											InstantiationException,
											IllegalAccessException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		gui = new BibInserterGUI();
		return gui;
	}
	
	//flow aint there
	public static void setUpShortcuts(BibInserterGUI gui) {
		shortcuts = BibShortcutGUIManager.create(gui);
	}
	/*
	public static void setUpShortcuts(BibInserterGUI gui) throws NativeHookException {
	if (!GlobalScreen.isNativeHookRegistered()) {
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.WARNING);
			
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new BibShortcutManager(gui));
		}
	}
	*/
	
	public static boolean parseChangedBibFile(Path filePath) throws IOException {
		boolean fileChanged = (lastModificatoinTime == null) || !lastModificatoinTime.equals(Files.getLastModifiedTime(filePath));
		if (fileChanged) {
			//read
			lastModificatoinTime = Files.getLastModifiedTime(filePath); //given no failure
		}
		return fileChanged; //not sure
	}
}
