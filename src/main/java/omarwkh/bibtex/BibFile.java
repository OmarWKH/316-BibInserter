package omarwkh.bibtex;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import java.util.Map;
import java.util.Vector;

/**
 * Represents a .bib file. Stores entries in a Map and allows searching through them by key or attribute value.
 * Usage: Create an instance by passing file path. Call loadFile() to store entries. If file wasn't changed since last load operation, it won't be loaded.
 * A file is changed if its last modification time was changed since last load operation.
 */
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
	
	/**
	 * Loads the file entries into a Map of (BibKey, BibEntry) pairs if the file changed since the last load operation (or was never loaded).
	 * @see omarwkh.bibtex.BibFileParser
	 * @throws IOException If wasChanged() or markAsRead() threw it.
	 */
	public void loadFile() throws IOException {
		if (wasChanged()) {
			bibEntries = BibFileParser.parseFile(this.getPath());
			markAsRead(); //given no failure?
		}
	}
	
	//supposed to do more
	/**
	 * Makes sure the file exists.
	 * @throws NoSuchFileException If file does not exist.
	 */
	public void validateFile() throws NoSuchFileException {
		if (Files.notExists(bibFilePath)) {
			throw new NoSuchFileException(bibFilePath.toString());
		}
	}
	
	/**
	 * @return True if file was changed since it was marked as read or if it was never read. False otherwise.
	 * @throws IOException if Files.getLastModifiedTime() throws it
	 */
	public boolean wasChanged() throws IOException {
		return (lastModificationTime == null) || !lastModificationTime.equals(Files.getLastModifiedTime(bibFilePath));
	}

	/**
	 * Stores the file's last modification time.
	 * @throws IOException if Files.getLastModifiedTime() throws it
	 */
	private void markAsRead() throws IOException {
		lastModificationTime = Files.getLastModifiedTime(bibFilePath);
	}
	
	/**
	 * Searches through the list of loaded entries. A match is found if either key or attribute values contain the given string (using String.contains, so every string contains the empty string).
	 * @param query The string to search for
	 * @return A Vector of BibKeys whose entries contain the given string in the key or the attribute values. All the entries if the given string is empty.
	 */
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
	
	/**
	 * @param key BibKey for the entry
	 * @return The entry with the given BibKey. Null if no such entry exists.
	 */
	public BibEntry getEntry(BibKey key) {
		return bibEntries.get(key);
	}
	
	/**
	 * Creates a BibKey objects and calls getEntry(BibKey)
	 * @param key The string representing the BibKey for the entry
	 * @return The entry if found. Null if not.
	 */
	public BibEntry getEntry(String key) {
		return getEntry(new BibKey(key));
	}
	
	public Path getPath() {
		return bibFilePath;
	}
	
	/**
	 * @return Number of loaded entries.
	 */
	public int getCount() {
		return bibEntries.size();
	}
}
