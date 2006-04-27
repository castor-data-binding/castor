/*
 * Copyright 2005 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jdo.tc166;

import java.io.Serializable;

public class Lazy1to1Parent implements Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -414529759627602071L;
    
    private Integer id;
	private String description;
    private Lazy1to1Child child;
	
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

    public Lazy1to1Child getChild() { return child; }
    public void setChild(Lazy1to1Child child) { this.child = child; }

	public String toString() {
		StringBuffer buffer = new StringBuffer();
        String className = getClass().getName();
		buffer.append ("<").append(className).append(">")
			.append ("<id>")
			.append (getId())
			.append ("</id>")
			.append ("<description>")
			.append (getDescription())
			.append ("</description>");
		
		if (child != null) {  buffer.append (child); }

		buffer.append ("</").append(className).append(">");
        
		return buffer.toString();
	}
}
