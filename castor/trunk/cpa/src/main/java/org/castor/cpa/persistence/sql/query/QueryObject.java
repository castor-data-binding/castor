/*
 * Copyright 2009 Ralf Joachim, Ahmad Hassan
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
package org.castor.cpa.persistence.sql.query;

/**
 * Abstract base class for all query objects. 
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public abstract class QueryObject {
    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }
    
    /**
     * Append a string representation of the object to the given StringBuilder. In general,
     * this toString method appends a string that "textually represents" this object. The
     * result should be a string in valid SQL syntax. It is required that all subclasses
     * override this method.
     * 
     * @param sb StringBuilder to append the string representation of the object to.
     */
    public abstract void toString(StringBuilder sb);
    
    //-----------------------------------------------------------------------------------    
}
