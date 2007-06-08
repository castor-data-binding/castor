package jdo.c1355.app;

public class Country extends MyAppObject {

	private String name;
	private String iso3Code;
	/**
	 * Retreives the value of <code>name</code> - Name of the country.
	 *
	 * @return the <code>name</code> property value.  Name of the country.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Replaces the current value of <code>name</code> - Name of the country.
	 *
	 * @param value new Name of the country.
	 */
	public void setName(String value) {
		name = value;
	}

	/**
	 * Retreives the value of <code>iso3Code</code> - ISO 3 Country Code for this country.
	 *
	 * @return the <code>iso3Code</code> property value.  ISO 3 Country Code for this country.
	 */
	public String getIso3Code() {
		return iso3Code;
	}

	/**
	 * Replaces the current value of <code>iso3Code</code> - ISO 3 Country Code for this country.
	 *
	 * @param value new ISO 3 Country Code for this country.
	 */
	public void setIso3Code(String value) {
		iso3Code = value;
	}

}
