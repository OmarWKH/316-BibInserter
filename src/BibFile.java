import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import java.util.Map;
import java.util.Vector;

public class BibFile {
	private Path bibFilePath;
	private FileTime lastModificationTime;
	private Map<BibKey, BibEntry> bibEntries;
	
	public BibFile(Path filePath) throws NoSuchFileException {
		this.bibFilePath = filePath;
		this.validateFile();
	}
	
	public BibFile(String filePath) throws NoSuchFileException {
		this(Paths.get(filePath));
	}
	
	public void loadFile() throws IOException {
		if (wasChanged()) {
			bibEntries = BibFileParser.parseFile(this.getPath());
			markAsRead(); //given no failure?
		}
	}
	//supposed to do more
	public void validateFile() throws NoSuchFileException {
		if (Files.notExists(bibFilePath)) {
			throw new NoSuchFileException(bibFilePath.toString());
		}
	}
	
	public boolean wasChanged() throws IOException {
		return (lastModificationTime == null) || !lastModificationTime.equals(Files.getLastModifiedTime(bibFilePath));
	}

	private void markAsRead() throws IOException {
		lastModificationTime = Files.getLastModifiedTime(bibFilePath);
	}
	
	//looks through key and attribute values, uses String.contains ignoring case
	//therefore, if query is empty string, all entries would be returned
	public Vector<BibKey> find(String query) {
		
		Vector<BibKey> results = new Vector<BibKey>(bibEntries.size());
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
	
	public Path getPath() {
		return bibFilePath;
	}
	
	public int getCount() {
		return bibEntries.size();
	}
}
