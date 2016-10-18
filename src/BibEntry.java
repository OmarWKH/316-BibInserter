import java.util.Map;
import java.util.Arrays;

//unchangable BibEntry
/*
Does not represent a valid BibTeX entry.
Goal: Be good enough for search function in BibInserter.
See BibFileParser
*/
public class BibEntry {
	private String type;
	private BibKey key;
	//does not force proper format, ok since only reading, would be irresponsible if writing
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
