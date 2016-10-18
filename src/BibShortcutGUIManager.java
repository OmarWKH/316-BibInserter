import com.tulskiy.keymaster.common.Provider;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.HotKey;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

//better design?
//consider the gui?
public class BibShortcutGUIManager {
	private static Provider provider;
	private static BibShortcutGUIManager instance;
	
	private static final String SHOW_HOTKEY = "control released B";
	private static final String HIDE_HOTKEY = "released ESCAPE";
	//..
	
	private BibShortcutGUIManager() {
		BibShortcutGUIManager.provider = Provider.getCurrentProvider(false);
		BibShortcutGUIManager.instance = this;
		
		registerShowGUIHotKey(SHOW_HOTKEY);
		registerHideGUIHotKey(HIDE_HOTKEY);
	}
	
	public static BibShortcutGUIManager create() {
		if (instance == null) {
			return new BibShortcutGUIManager();
		} else {
			return instance;
		}
	}
	
	//here too, it would be cool to use yeild
	public void registerShowGUIHotKey(String hotKey) {
		KeyStroke keyStroke = getValidKeyStroke(hotKey);
		provider.register(keyStroke, new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				/*
				try {
					SwingUtilities.invokeAndWait(() -> BibInserter.gui.showGUI());
				} catch(InterruptedException | InvocationTargetException e) {
					e.printStackTrace();
					System.exit(1);
				}
				*/
				BibInserter.gui.showGUI();
			}
		});
	}
	
	public void registerHideGUIHotKey(String hotKey) {
		KeyStroke keyStroke = getValidKeyStroke(hotKey);
		provider.register(keyStroke, new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				BibInserter.gui.hideGUI();
			}
		});
	}
	
	private static KeyStroke getValidKeyStroke(String hotKey) {
		KeyStroke keyStroke = KeyStroke.getKeyStroke(hotKey);
		if (keyStroke == null) {
			NullPointerException npe = new NullPointerException("HotKey is not valid or formatted incorrectly");
			npe.printStackTrace();
			System.exit(1);
		}
		return keyStroke;
	}
	
}
