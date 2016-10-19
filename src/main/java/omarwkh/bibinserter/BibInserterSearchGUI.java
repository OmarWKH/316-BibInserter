package omarwkh.bibinserter;
import omarwkh.bibtex.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import java.util.Vector;

import java.awt.*;
import java.awt.datatransfer.*;

/**
 * BibInserterSearchGUI lists BibEntries that match the search field and allows inserting information of the selected entry via hotkeys.
 * Can be shown/hidden.
 */
public class BibInserterSearchGUI extends JFrame {
	private JPanel centerPanel;
    private JList<BibKey> entriesList;
    private JLabel hotkeysLabel;
    private JScrollPane rawEntryPanel;
    private JScrollPane entriesListPanel;
    private JTextArea rawEntry;
    private JTextField searchField;
    private JLabel statusLabel;
	private String hotkeysText = "Insert: BibKey(Enter), Title(F1), Author(F2), title-author-year(F3)";
	
	public BibInserterSearchGUI() {
		searchField = new JTextField();
		hotkeysLabel = new JLabel();
		centerPanel = new JPanel();
		entriesListPanel = new JScrollPane();
		entriesList = new JList<>();
		entriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rawEntryPanel = new JScrollPane();
		rawEntry = new JTextArea();
		statusLabel = new JLabel();
		
		setTitle("BibInserter Search");
		
		//search on change
		searchField.getDocument().addDocumentListener(
			new DocumentListener() {
				public void changedUpdate(DocumentEvent de) {
					search();
				}
				
				public void insertUpdate(DocumentEvent de) {
					changedUpdate(de);
				}
				
				public void removeUpdate(DocumentEvent de) {
					changedUpdate(de);
				}
			}
		);
		
		//fill on selection
		entriesList.addListSelectionListener(e -> {
			BibKey key = entriesList.getSelectedValue();
			rawEntry.setText(key==null ? "" : BibInserter.file.getEntry(key).getRaw());
		});
		
		//insertion hotkeys
		bindHotkeys();
		hotkeysLabel.setText(hotkeysText);

		//netbeans layout stuff
		configureLayout();

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
	}
	
	public void showGUI() {
		if (!isVisible()) {
			searchField.requestFocusInWindow();
			searchField.setText("");
			search();
			this.setVisible(true);
			this.toFront();
		}
	}
	
	public void hideGUI() {
		if (isVisible()) {
			this.setVisible(false);
		}
	}
	
	private void bindHotkeys() {
		registerInsertAction(KeyStroke.getKeyStroke("ENTER"), "insert_bibkey", new String[]{"key"});
		registerInsertAction(KeyStroke.getKeyStroke("F1"), "insert_title", new String[]{"title"});
		registerInsertAction(KeyStroke.getKeyStroke("F2"), "insert_author", new String[]{"author"});
		registerInsertAction(KeyStroke.getKeyStroke("F3"), "insert_title_author_year", new String[]{"title", "author", "year"});
		
		registerListTraversal();
	}
	
	private void registerInsertAction(KeyStroke stroke, String actionName, String[] toInsert) {
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, actionName);
		getRootPane().getActionMap().put(actionName, new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				if (!entriesList.isSelectionEmpty()) {
					hideGUI();
					
					BibKey key = entriesList.getSelectedValue();
					StringBuilder builder = new StringBuilder();
					String delimiter = "";
					for (String attribute : toInsert) {
						builder.append(delimiter);
						switch (attribute) {
							case "key": builder.append(key.asString()); break;
							case "type": builder.append(BibInserter.file.getEntry(key).getType()); break;
							default: builder.append(BibInserter.file.getEntry(key).getValueOf(attribute));
						}
						delimiter = "-";
					}
					
					insertByClipboard(builder.toString());
				}
			}
		});
	}
	
	//overrides user's clipboard content
	private void insertByClipboard(String toInsert) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection owner = new StringSelection("BibInserter"); //that it?
		
		try {
			clipboard.setContents(new StringSelection(toInsert), owner);
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			System.out.println("Error: Clipboard stuff");
		}
		
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException awte) {
			awte.printStackTrace();
			System.out.println("Error: Automatic insertion via Robot is not supported on this OS.");
			System.exit(1);
		}
	}
	
	private void registerListTraversal() {
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "list_up");
		getRootPane().getActionMap().put("list_up", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				int selected = entriesList.getSelectedIndex();
				if (selected != -1) {
					entriesList.setSelectedIndex(selected-1);
				}
			}
		});
		
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "list_down");
		getRootPane().getActionMap().put("list_down", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				int selected = entriesList.getSelectedIndex();
				entriesList.setSelectedIndex(selected+1);
			}
		});
	}
	
	private void search() {
		if (BibInserter.file == null) {
			centerPanel.setBorder(BorderFactory.createTitledBorder("Matched entries: 0"));
			statusLabel.setText("Bib File: none | Bib Entries: 0");
		} else {
			String statusWarning = "";
			try {
				BibInserter.file.loadFile();
			} catch (java.io.IOException ioe) {
				ioe.printStackTrace();
				statusWarning = "Error: File loading error. ";
				System.err.println(statusWarning);
			}

			String searchTerm;
			try {
				searchTerm = searchField.getText();
			} catch (NullPointerException npe) {
				searchTerm = "";
			}
			Vector<BibKey> results = BibInserter.file.find(searchTerm);
			entriesList.setListData(results);
			
			centerPanel.setBorder(BorderFactory.createTitledBorder("Matched entries: " + entriesList.getModel().getSize()));
			statusLabel.setText(statusWarning + "Bib File: " + BibInserter.file.getPath().toAbsolutePath().toString() +
								" | Bib Entries: " + BibInserter.file.getCount());
			
			entriesList.setSelectedIndex(0);
		}
		
	}

	//magic
	private void configureLayout() {
		entriesListPanel.setViewportView(entriesList);

		rawEntry.setEditable(false);
		rawEntry.setColumns(20);
		rawEntry.setRows(5);
		rawEntryPanel.setViewportView(rawEntry);

		GroupLayout centerPanelLayout = new GroupLayout(centerPanel);
		centerPanel.setLayout(centerPanelLayout);
		centerPanelLayout.setHorizontalGroup(
			centerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(centerPanelLayout.createSequentialGroup()
				.addComponent(entriesListPanel, GroupLayout.PREFERRED_SIZE, 398, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(rawEntryPanel))
		);
		centerPanelLayout.setVerticalGroup(
			centerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(entriesListPanel, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
			.addComponent(rawEntryPanel)
		);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addComponent(searchField, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
				.addGap(18, 18, 18)
				.addComponent(hotkeysLabel))
			.addComponent(centerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(statusLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(hotkeysLabel))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(centerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(statusLabel))
		);
	}
}
