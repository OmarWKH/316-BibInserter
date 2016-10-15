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
	  //Unescaped: @(?<type>\w+)\s*\{\s*(?<key>\S+)\s*,(?<attributes>.*?)(?=\}\s*@)+
	  //Unescaped: @(?<type>\w+)\s*\{\s*(?<key>\S+)\s*,(?<attributes>[\s\S]*)\}
	  //Unescaped: (?<tag>\w+)\s*\=\s*[\{|"]?(?<value>.+?)[\}|"]?(?=,\s*\w+\s*\=)+
	  //Unescaped: (?<tag>\w+)\s*\=\s*(?<value>.+)
	*/
	private static final String TYPE_KEY_ATTRIBUTES_PATTERN_LOOKAHEAD = "@(?<type>\\w+)\\s*\\{\\s*(?<key>\\S+)\\s*,(?<attributes>.*?)(?=\\}\\s*@)+";
	private static final String TYPE_KEY_ATTRIBUTES_PATTERN_SINGLE = "@(?<type>\\w+)\\s*\\{\\s*(?<key>\\S+)\\s*,(?<attributes>[\\s\\S]*)\\}";
	private static final String TAG_VALUE_LOOKAHEAD = "(?<tag>\\w+)\\s*\\=\\s*[\\{|\"]?(?<value>.+?)[\\}|\"]?(?=,\\s*\\w+\\s*\\=)+";
	private static final String TAG_VALUE_SINGLE = "(?<tag>\\w+)\\s*\\=\\s*(?<value>.+)";
	private static final int TYPE = 1, KEY = 2, ATTRIBUTES = 3;
	private BibFileParser() {}
	
	public static HashMap<BibKey, BibEntry> parseFile(BibFile file) throws IOException {
		String content = BibFileParser.readFile(file.getPath());
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
		
		Pattern lastPattern = Pattern.compile(TYPE_KEY_ATTRIBUTES_PATTERN_SINGLE);
		matcher = lastPattern.matcher(content.substring(end));
		if (matcher.find()) {
			//System.console().readLine("ReadIt:");
			BibEntry entry = parseEntry(matcher.toMatchResult());
			entries.put(entry.getKey(), entry);
		}
		
		return entries;
	}
	
	//raw will be missing } except for last item
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
		
		Pattern lastPattern = Pattern.compile(TAG_VALUE_SINGLE);
		matcher = lastPattern.matcher(rawAttributes.substring(end));
		if (matcher.find()) {
			attributes.put(matcher.group("tag"), matcher.group("value"));
			String value = matcher.group("value");
			switch (value.charAt(0)) {
				case '{':
				case '"':
					value = value.substring(1, value.length());
					break;
			}
			attributes.put(matcher.group("tag"), value);
		}
		
		return new BibEntry(type, key, attributes, raw);
	}
	
	//uses default platform encoding
	private static String readFile(Path path) throws IOException {
		byte[] fileBytes = Files.readAllBytes(path);
		return new String(fileBytes);
	}
}
