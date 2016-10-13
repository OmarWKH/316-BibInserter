import java.util.HashMap;

//unchangable BibEntry
public class BibEntry {
	String type;
	String key;
	HashMap<String, String> attributes;
	//type
	//key
	//hashmap of tags
	
	//BibEntry(type, key, tags)
	//BibEntry(bib entry as string) --parser's job? yes
	public BibEntry(String type, String key, HashMap<String, String> attributes) {
		this.type = type;
		this.key = key;
		this.attributes = attributes;
	}
	
	//getattribute(tag)/getvalue(tag) --ignore case--not found
	//gettype
	//getkey
	
	//toString (key? type? formatted bib entry?)
}
