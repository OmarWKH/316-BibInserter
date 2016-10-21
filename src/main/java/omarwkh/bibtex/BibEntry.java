package omarwkh.bibtex;

import java.util.Map;
import java.util.Arrays;

/**
 * Current Goal: Be good enough for search function in BibInserter.
 * Does not represent a valid bibtex entry.
 * Cannot be changed or reconstructed into plaintext. Use getRaw() for text that was used to create the entry instance.
 * @see omarwkh.bibtex.BibFileParser
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
	
	/**
	 * @param type Entry type.
	 * @param key Entry key.
	 * @param attributes Map of (tag, value) string pairs.
	 * @param raw String used to create the entry.
	 */
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
	
	/**
	 * @return Values of all attributes.
	 */
	public String getAttributeValues() {
		return Arrays.toString(attributes.values().toArray());
	}
	
	/**
	 * @return Raw string used to create the entry. Null if raw string was not given upon construction.
	 */
	public String getRaw() {
		return raw;
	}
	
	/**
	 * @param tag The attribute tag/field name you want to retrive the value of.
	 * @return The value of the tag. Null if no value for the tag exists.
	 */
	public String getValueOf(String tag) {
		return attributes.get(tag.toLowerCase());
	}
	
	//toString (key? type? formatted bib entry?)
}
