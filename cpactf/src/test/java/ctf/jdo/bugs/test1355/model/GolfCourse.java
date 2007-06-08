package ctf.jdo.bugs.test1355.model;

import java.util.ArrayList;
import java.util.Collection;

public class GolfCourse extends MyAppObject {

	private String name;
	private Integer holes;
    
	//Marked as transient because the castor persistent collection is not serializeable
	private transient Collection tees = new ArrayList();

	private City city = new City();
    
	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}

	public Integer getHoles() {
		return holes;
	}
	public void setHoles(Integer value) {
		holes = value;
	}

	public Collection getTees() {
		return tees;
	}
	public void setTees(Collection value) {
    	tees = value;
	}

	public City getCity() {
		return city;
	}
	public void setCity(City value) {
		city = value;
	}

}

