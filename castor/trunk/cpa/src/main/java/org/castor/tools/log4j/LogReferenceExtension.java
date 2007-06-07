/*
 * Copyright 2006 Holger West, Ralf Joachim
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
package org.castor.tools.log4j;

/**
 * A <code>LogReferenceExtension</code> for saving a reference to another object. Use in
 * that way e.g.: <code>LOG.warn(object)</code>. 
 * 
 * <p> To write your own extension, the following requirements has to be fulfilled: 
 * 1. The customized class must extend the <code>LogEntry</code> class.
 * 2. A table in the database must be created. 
 * 3. The castor mapping file must be customized for the new class. As base the existing
 *    one can be used.
 *
 * @author  <a href="mailto:holger.west@syscon-informatics.de">Holger West</a>
 */
public final class LogReferenceExtension extends LogEntry {
    // -----------------------------------------------------------------------------------
    
    /** The type this object references to. */
    private String      _type;
    
    /** The value this object references to. */
    private String      _value;

    // -----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public LogReferenceExtension() { super(); }
    
    /**
     * Construct a new <code>LogReferenceExtension</code> with the given message, type
     * and value.
     * 
     * @param message The message to set for this object.
     * @param type The type this oject references to.
     * @param value The value this object references to.
     */
    public LogReferenceExtension(
            final String message, final String type, final String value) {
        
        super(message);
        _type = type;
        _value = value;
    }
    
    // -----------------------------------------------------------------------------------

    /** 
     * Get the type this object references to. 
     *
     * @return The type this object references to.
     */
    public String getType() {
        return _type;
    }

    /** 
     * Set the type this object references to. 
     *
     * @param type The type this object references to.
     */
    public void setType(final String type) {
        _type = type;
    }
    
    /** 
     * Get the value this object references to. 
     *
     * @return The value this object references to.
     */
    public String getValue() {
        return _value;
    }
    
    /** 
     * Set the value this object references to. 
     *
     * @param value The value this object references to.
     */
    public void setValue(final String value) {
        _value = value;
    }

    // -----------------------------------------------------------------------------------
}
