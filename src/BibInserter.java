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
	protected static BibInserterSearchGUI searchGUI;
	protected static BibInserterConfigGUI configGUI;
	protected static BibShortcutGUIManager shortcuts;
	protected static BibFile file;
	
	public static void main(String[] args) {
		//GUIs
		try {
			//has to be first line, else look and feel might be different
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			configGUI = new BibInserterConfigGUI();
			searchGUI = new BibInserterSearchGUI();
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("Error: GUI wasn't set up correctly");
			System.exit(1);
		}
		
		//shortcuts
		shortcuts = BibShortcutGUIManager.create();
		
		//file, if null configGUI will do it
		if (args[0] != null) {
			initializeFile(args[0]);
		}
		
		//for testing
		Console console = System.console();
		while (true) {
			String input = console.readLine("$ ");
			if (input.contains("s")) {
				searchGUI.showGUI();
			} else if (input.contains("h")) {
				searchGUI.hideGUI();
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
	
	protected static void initializeFile(String filePath) {
		try {
			file = new BibFile(filePath);
			configGUI.updateChosenFileStatus();
			file.loadFile();
		} catch (IOException e) {
			e.printStackTrace();
			String warning = "Error: File error.";
			configGUI.updateChosenFileStatus(warning);
			System.err.println(warning);
		}
	}
}
