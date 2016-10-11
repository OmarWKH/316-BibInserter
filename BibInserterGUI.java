import javax.swing.JFrame;
//import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;

public class BibInserterGUI {
	JFrame frame = new JFrame();
	JTextField textField1 = new JTextField("textField1");
	JTextField textField2 = new JTextField("textField2");
	JComponent[] components = {textField1, textField2};
	
	public BibInserterGUI() {
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		for (JComponent component : components) {
			frame.add(component);
		}
		
		frame.pack();
		textField2.requestFocusInWindow();
	}
	
	//1.same, show/hide
	//2.new each time
	//destroy on lose focus, esc, input hotkey, close
	  //Close, nope
	  //Hide, current, see below for worries
	  //Dispose, keep as is, release resources, but may terminate
	  //do nothing, handle in method
	// worries: made focused on showGUI()? already shown/hidden, not reset
	public void showGUI() {
		frame.setVisible(true);
		frame.toFront();
	}
	
	public void hideGUI() {
		//with dispose(), focus is not on textField
		frame.setVisible(false);
	}
}
