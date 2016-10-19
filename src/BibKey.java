/**
 * Just a String currently. Could be expanded to verify a valid key.
 * Uses hashCode() and equals() are based on the String field (key). Such that two BibKey instances with the same key field would be equal with the same hash code.
 */
public class BibKey {
	private String key;
	
	public BibKey(String key) {
		this.key = key;
	}
	
	public String asString() {
		return key;
	}
	
	@Override
	public String toString() {
		return key;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	@Override
	public boolean equals(Object that) {
		if (that == null || !(that instanceof BibKey)) {
			return false;
		} else {
			BibKey thatKey = (BibKey)that;
			return this.key.equals(thatKey.asString());
		}
	}
}
