/*
This is just a string. It's made a class in case that changes.
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
