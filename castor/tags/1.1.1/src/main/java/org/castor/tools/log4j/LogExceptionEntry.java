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
 * A <code>LogExceptionEntry</code> holds the information belongs to a related exception
 * of a <code>LoggingEvent</code>.
 *
 * @author  <a href="mailto:holger.west@syscon-informatics.de">Holger West</a>
 */
public final class LogExceptionEntry {
    // -----------------------------------------------------------------------------------
    
    /** The identity of this object. */
    private Integer     _id;
    
    /** The stack trace of this exception. */
    private String      _stackTrace;
    
    /** Reference to the <code>LogEntry</code> this <code>LogExceptionEntry</code>
     *  belongs to. */
    private LogEntry    _entry;
    
    // -----------------------------------------------------------------------------------

    /** 
     * Get the identity of this object. 
     *
     * @return The identity of this object.
     */
    public Integer getId() {
        return _id;
    }
    
    /** 
     * Set the identity of this object. 
     *
     * @param id The identity of this object.
     */
    public void setId(final Integer id) {
        _id = id;
    }
    
    /** 
     * Get the stack trace of the exception. 
     * 
     * @return Stack trace of the exception.
     */
    public String getStackTrace() {
        return _stackTrace;
    }
    
    /** 
     * Set the stack trace of this exception.
     * 
     * @param stackTrace Stack trace of this exception.
     */
    public void setStackTrace(final String stackTrace) {
        _stackTrace = stackTrace;
    }
    
    /** 
     * Get the <code>LogEntry</code> this <code>LogExceptionEntry</code> belongs to.
     * 
     * @return <code>LogEntry</code> this <code>LogExceptionEntry</code> belongs to.
     */
    public LogEntry getEntry() {
        return _entry;
    }
    
    /** 
     * Set the <code>LogEntry</code> this <code>LogExceptionEntry</code> belongs to.
     * 
     * @param entry <code>LogEntry</code> this <code>LogExceptionEntry</code> belongs to.
     */
    public void setEntry(final LogEntry entry) {
        _entry = entry;
    }

    // -----------------------------------------------------------------------------------
}
