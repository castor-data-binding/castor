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
