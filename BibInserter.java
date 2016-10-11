import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.io.IOException;

public class BibInserter {
	private static Path bibFilePath;
	private static FileTime lastModificatoinTime;
	private static BibInserterGUI gui;
	
	//can't/won't handle UIManager exceptions
	public static void main(String[] args) throws UnsupportedLookAndFeelException,
													ClassNotFoundException,
													InstantiationException,
													IllegalAccessException,
													IOException {
		setUpGUI(); //has to be done first, so lock and feel works properly
		
		//testing passed argument
			//pass file name in cd, proper
			//pass file path relative to cd, proper
			//pass absolute file path, proper
		//System.out.println(file.toAbsolutePath());
		//change time when reading again (so do this on read probably)
			//either have method on parser that takes FileTime
			//or wrap file parser in a method below
		bibFilePath = Paths.get(args[0]);
		parseChangedBibFile(bibFilePath);
		
		//improper loop, prompts are not shortcuts
		while (true) {
			System.out.print("$ ");
			String input = System.console().readLine();
			if (input.contains("s")) {
				gui.showGUI();
			} else if (input.contains("h")) {
				gui.hideGUI();
			} else if (input.contains("c")) {
				System.out.println(parseChangedBibFile(bibFilePath));
			} else if (input.contains("t")) {
				System.out.println(lastModificatoinTime);
			}
		}
	}
	
	public static void setUpGUI() throws UnsupportedLookAndFeelException,
											ClassNotFoundException,
											InstantiationException,
											IllegalAccessException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		gui = new BibInserterGUI();
	}
	
	public static boolean parseChangedBibFile(Path filePath) throws IOException {
		boolean fileChanged = (lastModificatoinTime == null) || !lastModificatoinTime.equals(Files.getLastModifiedTime(filePath));
		if (fileChanged) {
			//read
			lastModificatoinTime = Files.getLastModifiedTime(filePath); //given no failure
		}
		return fileChanged; //not sure
	}
}
