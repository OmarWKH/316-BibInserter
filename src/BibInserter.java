//import org.jnativehook.GlobalScreen;
//import org.jnativehook.NativeHookException;
//import java.util.logging.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import java.util.List;
import java.io.Console;
/*
Design, UML, Classes, Code, Test

Structure, observer, passing|public|package/protected
Generalized, dependent
BibInserter --> All
GUI <--> Shortcuts/Input

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
	protected static BibInserterGUI gui;
	protected static BibShortcutGUIManager shortcuts;
	protected static BibFile file;
	
	//exceptions
	public static void main(String[] args) {
		try {
			//setUpGUI() has to be done first, so lock and feel works properly
			//and to work with shortcuts (in current implmentation)
			setUpGUI();
			setUpShortcuts();
		} catch (Exception e) {
			System.err.println("Error(s) in setting up GUI and shortcuts");
			e.printStackTrace();
			System.exit(1);
		}
		
		//file = BibParser.readEntries();
		
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
			file = new BibFile(args[0]);
			file.loadFile();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: Forgot to pass a file");
			System.exit(1);
		} catch (NoSuchFileException nsfe) {
			nsfe.printStackTrace();
			System.err.println("Error: File does not exist");
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
		/*try {
			parseChangedBibFile(bibFilePath);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}*/
		
		//improper loop, prompts are not shortcuts
		Console console = System.console();
		while (true) {
			String input = console.readLine("$ ");
			if (input.contains("s")) {
				gui.showGUI();
			} else if (input.contains("h")) {
				gui.hideGUI();
			}/* else if (input.contains("c")) {
				try {
					System.out.println(parseChangedBibFile(bibFilePath));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			} else if (input.contains("t")) {
				System.out.println(lastModificatoinTime);
			}*/ else if (input.contains("q")) {
				System.exit(0);
			} else if (input.contains("r")) {
				try {
					file.loadFile();
				} catch (IOException ioe) {
					System.exit(1);
				}
			} else if (input.contains("f")) {
				String query = console.readLine(" ? ");
				List<BibKey> results = file.find(query);
				System.out.println(" Found: " + results.size());
				System.out.println(results);
				boolean keys = true;
				while (keys) {
					String key = console.readLine(" k: ");
					if (key.contains("=")) {
						keys = false;
					} else {
						System.out.println(file.getEntry(key));
						//System.out.println(file.getEntry(results.get(0)));
					}
				}
			}
		}
	}
	
	public static void setUpGUI() throws UnsupportedLookAndFeelException,
											ClassNotFoundException,
											InstantiationException,
											IllegalAccessException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		gui = new BibInserterGUI();
	}
	
	//flow aint there
	public static void setUpShortcuts() {
		shortcuts = BibShortcutGUIManager.create();
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
	
	/*public static boolean parseChangedBibFile(Path filePath) throws IOException {
		boolean fileChanged = (lastModificatoinTime == null) || !lastModificatoinTime.equals(Files.getLastModifiedTime(filePath));
		if (fileChanged) {
			//read
			lastModificatoinTime = Files.getLastModifiedTime(filePath); //given no failure
		}
		return fileChanged; //not sure
	}*/
}
