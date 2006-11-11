package jdo.c1355.app;

/**
 * Represents a single city in the system.
 */
public class City extends MyAppObject {

	private String name;
	private StateProv state = new StateProv();
    
	/**
	 * Retreives the value of <code>name</code> - Name of the city.
	 *
	 * @return the <code>name</code> property value.  Name of the city.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Replaces the current value of <code>name</code> - Name of the city.
	 *
	 * @param value new Name of the city.
	 */
	public void setName(String value) {
		name = value;
	}

	/**
	 * Retreives the value of <code>state</code> -
	 *
	 * @return the <code>state</code> property value.
	 */
	public StateProv getState() {
		return state;
	}

	/**
	 * Replaces the current value of <code>state</code> -
	 *
	 * @param value new
	 */
	public void setState(StateProv value) {
		state = value;
	}

}

