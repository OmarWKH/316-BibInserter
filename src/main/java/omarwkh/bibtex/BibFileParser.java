package omarwkh.bibtex;

import java.util.HashMap;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

/**
 * Limited BibTex parser.
 * Goal: Serve BibInserter (search, read, insert)
 * Known limitations:
 * - Ignores # operator
 * - Ignores @string, @preamble, @comment
 * - Output can not be used to create a file with the same structure as the original
 * - Accepts illegal characters and invalid syntax as long as it does not interfere with parsing
 */
public class BibFileParser {
	/**
	 * Unescaped: {@code @(?<type>\w+)\s*\{\s*(?<key>\S+)\s*,(?<attributes>.*?)(?=\}\s*@)+ }
	 * <br>
	 * Captures type, key, and attributes. Stopping at ""} @". Has to be used with Pattern.DOTALL to support multiple lines.
	 * <br>
	 * Matched block will be missing the last closing brace.
	 */
	public static final String TYPE_KEY_ATTRIBUTES_PATTERN_LOOKAHEAD = "@(?<type>\\w+)\\s*\\{\\s*(?<key>\\S+)\\s*,(?<attributes>.*?)(?=\\}\\s*@)+";
	/**
	 * Unescaped: {@code @(?<type>\w+)\s*\{\s*(?<key>\S+)\s*,(?<attributes>[\s\S]*)\} }
	 * <br>
	 * Captures type, key, and attributes. Used for the last remaining entry.
	 */
	public static final String TYPE_KEY_ATTRIBUTES_PATTERN_SINGLE = "@(?<type>\\w+)\\s*\\{\\s*(?<key>\\S+)\\s*,(?<attributes>[\\s\\S]*)\\}";
	/**
	 * Unescaped: {@code (?<tag>\w+)\s*\=\s*[\{|"]?(?<value>.+?)[\}|"]?(?=,\s*\w+\s*\=)+ }
	 * <br>
	 * Captures tag and value. Stopping at ", nextTag =". Has to be used with Pattern.DOTALL to support multiple lines.
	 */
	public static final String TAG_VALUE_LOOKAHEAD = "(?<tag>\\w+)\\s*\\=\\s*[\\{|\"]?(?<value>.+?)[\\}|\"]?(?=,\\s*\\w+\\s*\\=)+";
	/**
	 * Unescaped: {@code (?<tag>\w+)\s*\=\s*[\{|"]?(?<value>.+?)[\}|"] }
	 * <br>
	 * Captures tag and values. Used for the last remaining attribute. Has to be used with Pattern.DOTALL to support multiple lines.
	 */
	public static final String TAG_VALUE_SINGLE = "(?<tag>\\w+)\\s*\\=\\s*[\\{|\"]?(?<value>.+?)[\\}|\"]";
	private static final int TYPE = 1, KEY = 2, ATTRIBUTES = 3;
	private BibFileParser() {}
	
	/**
	 * Extracts BibTeX entries from a .bib file into a HashMap of (BibKey, BibEntry) pairs.
	 * The method reads file content into a String then looks for matches for TYPE_KEY_ATTRIBUTES_PATTERN_LOOKAHEAD (see above) and calls parseEntry() on each match to extract attributes.
	 * When no more matches are present the method attempts to find the last possible entry by matching TYPE_KEY_ATTRIBUTES_PATTERN_SINGLE.
	 * Parsed entries are added to the returned HashMap.
	 * @param filePath Path to .bib file.
	 * @return HashMap of (BibKey, BibEntry) pairs found in the .bib file.
	 * @throws IOException If readFileContent(Path) throws it
	 */
	public static HashMap<BibKey, BibEntry> parseFile(Path filePath) throws IOException {
		String content = BibFileParser.readFileContent(filePath);
		HashMap<BibKey, BibEntry> entries = new HashMap<>();
		
		Pattern entryPattern = Pattern.compile(TYPE_KEY_ATTRIBUTES_PATTERN_LOOKAHEAD, Pattern.DOTALL);
		Matcher matcher = entryPattern.matcher(content);
		int end = 0;
		while (matcher.find()) {
			end = matcher.end();
			BibEntry entry = parseEntry(matcher.toMatchResult());
			entries.put(entry.getKey(), entry);
		}
		
		Pattern lastPattern = Pattern.compile(TYPE_KEY_ATTRIBUTES_PATTERN_SINGLE, Pattern.DOTALL);
		matcher = lastPattern.matcher(content.substring(end));
		if (matcher.find()) {
			BibEntry entry = parseEntry(matcher.toMatchResult());
			entries.put(entry.getKey(), entry);
		}
		
		return entries;
	}
	
	/**
	 * This is mainly called by parseFile() to extract attributes from each found entry into a BibEntry object.
	 * The method looks for matches for TAG_VALUE_LOOKAHEAD in the passed entry and stores each found (tag, value) pair.
	 * When no more matches are present the method attempts to find the last possible attribute by matching TAG_VALUE_SINGLE.
	 * The type, key, and extracted attributes are returned as a BibEntry object.
	 * @param result An entry found in the file.
	 * @return BibEntry object containing the extracted type, key, attributes, and the raw String used for extraction.
	 */
	private static BibEntry parseEntry(MatchResult result) {
		String raw = result.group();
		String type = result.group(TYPE);
		String key = result.group(KEY);
		String rawAttributes = result.group(ATTRIBUTES);
		HashMap<String, String> attributes = new HashMap<>();
		
		Pattern attributesPattern = Pattern.compile(TAG_VALUE_LOOKAHEAD, Pattern.DOTALL);
		Matcher matcher = attributesPattern.matcher(rawAttributes);
		int end = 0;
		while (matcher.find()) {
			end = matcher.end();
			attributes.put(matcher.group("tag"), matcher.group("value"));
		}
		
		Pattern lastPattern = Pattern.compile(TAG_VALUE_SINGLE, Pattern.DOTALL);
		matcher = lastPattern.matcher(rawAttributes.substring(end));
		if (matcher.find()) {
			attributes.put(matcher.group("tag"), matcher.group("value"));
			String value = matcher.group("value");
			attributes.put(matcher.group("tag").toLowerCase(), value);
		}
		
		return new BibEntry(type, key, attributes, raw);
	}
	
	/**
	 * Reads whole file content into one String. Uses default platform encoding which might not be ideal in all cases.
	 * @param path File path
	 * @return String File content as String
	 * @throws IOException If thrown by Files.readAllBytes(Path)
	 */
	private static String readFileContent(Path path) throws IOException {
		byte[] fileBytes = Files.readAllBytes(path);
		return new String(fileBytes);
	}
}
