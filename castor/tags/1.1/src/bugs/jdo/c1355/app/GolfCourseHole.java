package jdo.c1355.app;

public class GolfCourseHole extends MyAppObject {

	protected GolfCourseTees courseTees = new GolfCourseTees();
	protected Integer number;
	protected String name;
	protected Integer par;
	protected Integer yardage;

	public GolfCourseTees getCourseTees() {
		return courseTees;
	}

	public void setCourseTees(GolfCourseTees value) {
		courseTees = value;
	}

	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer value) {
		number = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}

	public Integer getPar() {
		return par;
	}
	public void setPar(Integer value) {
		par = value;
	}

	public Integer getYardage() {
		return yardage;
	}
	public void setYardage(Integer value) {
		yardage = value;
	}
}
