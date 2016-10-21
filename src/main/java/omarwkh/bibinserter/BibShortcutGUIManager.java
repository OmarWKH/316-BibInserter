package omarwkh.bibinserter;

import com.tulskiy.keymaster.common.Provider;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.HotKey;

import javax.swing.KeyStroke;

/**
 * Manages shortcuts that don't belong to GUI components.
 * Manages showGUI() and hideGUI()
 */
public class BibShortcutGUIManager {
	private static Provider provider;
	private static BibShortcutGUIManager instance;
	
	private static final String SHOW_HOTKEY = "control shift released B";
	private static final String HIDE_HOTKEY = "released ESCAPE";
	
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
	
	//a way to not repeat the code in *Hide*?
	public void registerShowGUIHotKey(String hotKey) {
		KeyStroke keyStroke = getValidKeyStroke(hotKey);
		provider.register(keyStroke, new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				BibInserter.searchGUI.showGUI();
			}
		});
	}
	
	public void registerHideGUIHotKey(String hotKey) {
		KeyStroke keyStroke = getValidKeyStroke(hotKey);
		provider.register(keyStroke, new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				BibInserter.searchGUI.hideGUI();
			}
		});
	}
	
	/**
	 * Makes sure the given hotkey string represents a valid KeyStroke.
	 * Terminates the application if the hotkey is not valid (could be handled better if new hotkey features are implemented).
	 * @param hotKey The hotky string to be validated.
	 * @return The KeyStroke represented by the given strong if it is valid.
	 */
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
