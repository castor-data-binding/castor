package ctf.jdo.bugs.test1355.model;

/**
 * Represents a single country's state or province in the system.
 */
public class StateProv extends MyAppObject {

	private String code;
	private String name;
	private Country country = new Country();

	public String getCode() {
		return code;
	}
	public void setCode(String value) {
		code = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}

	public Country getCountry() {
		return country;
	}
	public void setCountry(Country value) {
		country = value;
	}

}

