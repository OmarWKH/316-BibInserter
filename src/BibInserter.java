import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import java.util.List;
import java.io.Console;

/**
 * BibInserter's main class.
 * Initializes and holds references to GUI, ShortcutManager, and file.
 */
public class BibInserter {
	protected static BibInserterGUI gui;
	protected static BibShortcutGUIManager shortcuts;
	protected static BibFile file;
	
	public static void main(String[] args) {
		//gui
		try {
			//has to be first line, else look and feel might be different
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			gui = new BibInserterGUI();
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("Error: GUI wasn't set up correctly");
			System.exit(1);
		}
		
		//shortcuts
		shortcuts = BibShortcutGUIManager.create();
		
		//file
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
		
		//for testing
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
}
