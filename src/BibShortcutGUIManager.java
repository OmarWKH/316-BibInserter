import com.tulskiy.keymaster.common.Provider;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.HotKey;

import javax.swing.KeyStroke;

//better design?
//consider the gui?
public class BibShortcutGUIManager {
	private BibInserterGUI gui;
	private static Provider provider;
	private static BibShortcutGUIManager instance;
	
	private static final String SHOW_HOTKEY = "control released B";
	private static final String HIDE_HOTKEY = "released ESCAPE";
	//..
	
	private BibShortcutGUIManager(BibInserterGUI gui) {
		this.gui = gui;
		BibShortcutGUIManager.provider = Provider.getCurrentProvider(false);
		BibShortcutGUIManager.instance = this;
		
		registerShowGUIHotKey(SHOW_HOTKEY);
		registerHideGUIHotKey(HIDE_HOTKEY);
	}
	
	public void registerShowGUIHotKey(String hotKey) {
		KeyStroke keyStroke = getValidKeyStroke(hotKey);
		provider.register(keyStroke, new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				gui.showGUI();
			}
		});
	}
	
	public void registerHideGUIHotKey(String hotKey) {
		KeyStroke keyStroke = getValidKeyStroke(hotKey);
		provider.register(keyStroke, new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				gui.hideGUI();
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
	
	public static BibShortcutGUIManager create(BibInserterGUI gui) {
		if (instance == null) {
			return new BibShortcutGUIManager(gui);
		} else {
			return instance;
		}
	}
}
