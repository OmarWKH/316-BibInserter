import java.util.Map;
import java.util.Arrays;

/**
 * Current Goal: Be good enough for search function in BibInserter.
 * Does not represent a valid BibTeX entry.
 * Cannot be changed or reconstructed into plaintext. Use getRaw() for text that was used to create the entry instance.
 * @see BibFileParse
 */
public class BibEntry {
	private String type;
	private BibKey key;
	private Map<String, String> attributes;
	private String raw;
	
	public BibEntry(String type, String key, Map<String, String> attributes) {
		this.type = type;
		this.key = new BibKey(key);
		this.attributes = attributes;
	}
	
	public BibEntry(String type, String key, Map<String, String> attributes, String raw) {
		this(type, key, attributes);
		this.raw = raw;
	}
	
	public String getType() {
		return type;
	} 

	public BibKey getKey() {
		return key;
	}
		
	public String getAttributeValues() {
		return Arrays.toString(attributes.values().toArray());
	}
	
	public String getRaw() {
		return raw;
	}
	
	public String getValueOf(String tag) {
		return attributes.get(tag.toLowerCase());
	}
	
	//toString (key? type? formatted bib entry?)
}
