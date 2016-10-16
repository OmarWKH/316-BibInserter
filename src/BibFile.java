import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class BibFile {
	private Path bibFilePath;
	private FileTime lastModificatoinTime;
	private Map<BibKey, BibEntry> bibEntries; //will be listed on search, mean anything?
	
	public BibFile(Path filePath) throws NoSuchFileException {
		this.bibFilePath = filePath;
		this.validateFile();
	}
	
	public BibFile(String filePath) throws NoSuchFileException {
		this(Paths.get(filePath));
	}
	
	public void loadFile() throws IOException {
		if (wasChanged()) {
			bibEntries = BibFileParser.parseFile(this);
			markAsRead(); //given no failure //--modularize change?
		}
	}
	
	//looks through key and attribute values
	//if empty query
	public List<BibKey> find(String query) {
		List<BibKey> results = new ArrayList<BibKey>(bibEntries.size());
		query = query.toLowerCase();
		
		for (BibEntry entry : bibEntries.values()) {
			boolean match = entry.getKey().asString().toLowerCase().contains(query) ||
							entry.getAttributeValues().toLowerCase().contains(query);
			if (match) {
				results.add(entry.getKey());
			}
		}
		
		return results;
	}
	
	//returns null if not found
	public BibEntry getEntry(BibKey key) {
		return bibEntries.get(key);
	}
	
	public BibEntry getEntry(String key) {
		return getEntry(new BibKey(key));
	}
	
	//supposed to do more
	public void validateFile() throws NoSuchFileException {
		if (Files.notExists(bibFilePath)) {
			throw new NoSuchFileException(bibFilePath.toString());
		}
	}
	
	private void markAsRead() throws IOException {
		lastModificatoinTime = Files.getLastModifiedTime(bibFilePath);
	}
	
	public boolean wasChanged() throws IOException {
		return (lastModificatoinTime == null) || !lastModificatoinTime.equals(Files.getLastModifiedTime(bibFilePath));
	}

	public Path getPath() {
		return bibFilePath;
	}
	
	public int getCount() {
		return bibEntries.size();
	}
}
