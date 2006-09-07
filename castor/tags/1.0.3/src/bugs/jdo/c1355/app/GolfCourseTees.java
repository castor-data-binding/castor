package jdo.c1355.app;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single set of golf course tees. These may be entered into the system by a registered
        user.
 */
public class GolfCourseTees extends MyAppObject {

	protected GolfCourse course = new GolfCourse();
	protected String name;
	protected String color;
	protected Integer mensSlope;
	protected Double mensRating;
	protected Integer womensSlope;
	protected Double womensRating;
	protected Integer yardage;
	//Marked as transient because the castor persistent collection is not serializeable
	protected transient Collection holes = new ArrayList();

	public GolfCourse getCourse() {
		return course;
	}
	public void setCourse(GolfCourse value) {
		course = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String value) {
		color = value;
	}

	public Integer getMensSlope() {
		return mensSlope;
	}

	public void setMensSlope(Integer value) {
		mensSlope = value;
	}

	public Double getMensRating() {
		return mensRating;
	}
	public void setMensRating(Double value) {
		mensRating = value;
	}

	public Integer getWomensSlope() {
		return womensSlope;
	}
	public void setWomensSlope(Integer value) {
		womensSlope = value;
	}

	public Double getWomensRating() {
		return womensRating;
	}
	public void setWomensRating(Double value) {
		womensRating = value;
	}

	public Integer getYardage() {
		return yardage;
	}
	public void setYardage(Integer value) {
		yardage = value;
	}

	public Collection getHoles() {
		return holes;
	}
	public void setHoles(Collection value) {
		holes = value;
	}
}

