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
package ctf.jdo.tc8x;

import java.io.Serializable;

public final class Lazy1to1Parent implements Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -414529759627602071L;
    
    private Integer _id;
    private String _description;
    private Lazy1to1Child _child;
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }

    public String getDescription() { return _description; }
    public void setDescription(final String description) { _description = description; }

    public Lazy1to1Child getChild() { return _child; }
    public void setChild(final Lazy1to1Child child) { _child = child; }

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
        
        if (_child != null) {  buffer.append (_child); }

        buffer.append ("</").append(className).append(">");
        
        return buffer.toString();
    }
}
