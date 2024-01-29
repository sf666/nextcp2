package nextcp.db.service;

public class KeyValuePair {

	public String key = "";
	public String value = "";

	public KeyValuePair() {
	}

	public KeyValuePair(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValuePair [key=" + key + ", value=" + value + "]";
	}

}
