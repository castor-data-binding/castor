package jdo.tc166;

import java.io.Serializable;

public class Lazy1to1Child implements Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -5819064191424599043L;
    
    private Integer id;
	private String description;
	
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append ("<").append(getClass().getName()).append(">")
			.append ("<id>")
			.append (getId())
			.append ("</id>")
			.append ("<description>")
			.append (getDescription())
			.append ("</description>")
			.append ("</").append(getClass().getName()).append(">");
			
		return buffer.toString();
	}
}
