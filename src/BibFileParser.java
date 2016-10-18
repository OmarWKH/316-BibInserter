//parse given bib file into BibItems
/**
Limited BibTex parser.
Goal: Serve BibInserter (search, read, insert String)
Known limitations:
- Ignores # operator
- Ignores @string, @preamble, @comment
- Output can not be used to create a file with the same structure as the original
- Accepts illegal characters and invalid syntax as long as it does not interfere with parsing
*/

import java.util.HashMap;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

public class BibFileParser {
	/*
	  Unescaped (in order below):
	   @(?<type>\w+)\s*\{\s*(?<key>\S+)\s*,(?<attributes>.*?)(?=\}\s*@)+
	   @(?<type>\w+)\s*\{\s*(?<key>\S+)\s*,(?<attributes>[\s\S]*)\}
	   (?<tag>\w+)\s*\=\s*[\{|"]?(?<value>.+?)[\}|"]?(?=,\s*\w+\s*\=)+
	   (?<tag>\w+)\s*\=\s*[\{|"]?(?<value>.+?)[\}|"]
	*/
	private static final String TYPE_KEY_ATTRIBUTES_PATTERN_LOOKAHEAD = "@(?<type>\\w+)\\s*\\{\\s*(?<key>\\S+)\\s*,(?<attributes>.*?)(?=\\}\\s*@)+";
	private static final String TYPE_KEY_ATTRIBUTES_PATTERN_SINGLE = "@(?<type>\\w+)\\s*\\{\\s*(?<key>\\S+)\\s*,(?<attributes>[\\s\\S]*)\\}";
	private static final String TAG_VALUE_LOOKAHEAD = "(?<tag>\\w+)\\s*\\=\\s*[\\{|\"]?(?<value>.+?)[\\}|\"]?(?=,\\s*\\w+\\s*\\=)+";
	private static final String TAG_VALUE_SINGLE = "(?<tag>\\w+)\\s*\\=\\s*[\\{|\"]?(?<value>.+?)[\\}|\"]";
	private static final int TYPE = 1, KEY = 2, ATTRIBUTES = 3;
	private BibFileParser() {}
	
	//raw will be missing closing brace except for last entry
	public static HashMap<BibKey, BibEntry> parseFile(Path filePath) throws IOException {
		String content = BibFileParser.readFileContent(filePath);
		HashMap<BibKey, BibEntry> entries = new HashMap<>();
		
		//wouldn't it be neat to use yeild from ruby here?
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
	
	//uses default platform encoding
	private static String readFileContent(Path path) throws IOException {
		byte[] fileBytes = Files.readAllBytes(path);
		return new String(fileBytes);
	}
}
