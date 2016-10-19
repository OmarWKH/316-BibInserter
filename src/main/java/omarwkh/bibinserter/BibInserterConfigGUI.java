package omarwkh.bibinserter;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;

public class BibInserterConfigGUI extends JFrame {
	private JButton chooseFile;
	protected JLabel chosenFileState;
	private JTextField hideShortcut;
	private JLabel hideShortcutLabel;
	private JTextField insertAuthorShortcut;
	private JLabel insertAuthorShortcutLabel;
	private JTextField insertKeyShortcut;
	private JLabel insertKeyShortcutLabel;
	private JLabel insertLabel;
	private JTextField insertTitleAuthorYearShortcut;
	private JLabel insertTitleAuthorYearShortcutLabel;
	private JTextField insertTitleShortcut;
	private JLabel insertTitleShortcutLabel;
	private JTextField showShortcut;
	private JLabel showShortcutLabel;
	private Box.Filler filler1;
	
	public BibInserterConfigGUI() {
		chosenFileState = new JLabel();
		chooseFile = new JButton();
		showShortcutLabel = new JLabel();
		insertLabel = new JLabel();
		insertKeyShortcutLabel = new JLabel();
		insertAuthorShortcutLabel = new JLabel();
		insertTitleShortcutLabel = new JLabel();
		insertTitleAuthorYearShortcutLabel = new JLabel();
		showShortcut = new JTextField();
		insertTitleAuthorYearShortcut = new JTextField();
		insertAuthorShortcut = new JTextField();
		insertTitleShortcut = new JTextField();
		insertKeyShortcut = new JTextField();
		hideShortcut = new JTextField();
		hideShortcutLabel = new JLabel();
		filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("BibInserter");
		
		updateChosenFileStatus();

		chooseFile.setText("Open");
		chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chooseFileActionPerformed(evt);
			}
		});

		showShortcutLabel.setText("Show Search Window:");
		showShortcut.setText("Ctrl+Shift+B");
		showShortcut.setFocusable(false);

		hideShortcutLabel.setText("Hide Search Window:");
		hideShortcut.setText("Escape");
		hideShortcut.setFocusable(false);
		
		insertLabel.setText("Insert");

		insertKeyShortcutLabel.setText("Key:");
		insertKeyShortcut.setText("Enter");
		insertKeyShortcut.setFocusable(false);

		insertAuthorShortcutLabel.setText("Author:");
		insertAuthorShortcut.setText("F2");
		insertAuthorShortcut.setFocusable(false);

		insertTitleShortcutLabel.setText("Title:");
		insertTitleShortcut.setText("F1");
		insertTitleShortcut.setFocusable(false);

		insertTitleAuthorYearShortcutLabel.setText("Title-Author-Year:");
		insertTitleAuthorYearShortcut.setText("F3");
		insertTitleAuthorYearShortcut.setFocusable(false);

		setUpLayout();
		pack();
		setVisible(true);
	}

	//netbeans layout stuff                         
	private void setUpLayout() {
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(showShortcutLabel)
					.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(insertKeyShortcutLabel)
							.addComponent(insertTitleShortcutLabel)
							.addComponent(insertAuthorShortcutLabel)
							.addComponent(insertTitleAuthorYearShortcutLabel)))
					.addComponent(hideShortcutLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(showShortcut)
					.addComponent(insertKeyShortcut)
					.addComponent(insertTitleShortcut)
					.addComponent(insertAuthorShortcut)
					.addComponent(insertTitleAuthorYearShortcut)
					.addComponent(hideShortcut, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE))
				.addContainerGap())
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(insertLabel)
					.addGroup(layout.createSequentialGroup()
						.addGap(185, 185, 185)
						.addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
				.addGap(0, 0, Short.MAX_VALUE))
			.addGroup(layout.createSequentialGroup()
				.addComponent(chosenFileState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(chooseFile))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
					.addComponent(chooseFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(chosenFileState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
				.addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(showShortcutLabel)
					.addComponent(showShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(hideShortcutLabel)
					.addComponent(hideShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(insertLabel)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(insertKeyShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(insertKeyShortcutLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(insertTitleShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(insertTitleShortcutLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(insertAuthorShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(insertAuthorShortcutLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(insertTitleAuthorYearShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(insertTitleAuthorYearShortcutLabel)))
		);
	}
	
	private void chooseFileActionPerformed(ActionEvent evt) {                                           
		JFileChooser chooser = new JFileChooser(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"bibtex Database", "bib");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String filePath = chooser.getSelectedFile().getAbsolutePath();
			BibInserter.initializeFile(filePath);
			updateChosenFileStatus();
		}
	}
	
	public void updateChosenFileStatus() {
		updateChosenFileStatus("");
	}
	
	public void updateChosenFileStatus(String status) {
		if (BibInserter.file != null) {
			chosenFileState.setText("<html>"+BibInserter.file.getPath().toAbsolutePath()+"</html>");
		} else {
			chosenFileState.setText("No file chosen. " + status);
		}
	}
}
