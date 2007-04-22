/*
 * Created on 19.09.2006 by holger
 *
 * (c) Copyright 2006, Syscon Ingenieurbüro für Mess- und Datentechnik GmbH.
 * All Rights Reserved.
 */

package org.castor.tools.log4j;

import java.util.Date;

/**
 * A <code>LogEntry</code> holds all information of a <code>LoggingEvent</code> which has
 * to be saved to the database.
 *
 * @author  <a href="mailto:holger.west@syscon-informatics.de">Holger West</a>
 */
public class LogEntry {
    // -----------------------------------------------------------------------------------
    
    /** The identity of this object. */
    private Integer _id;

    /** Timestamp when the log entry was created. */
    private Date _timestamp;

    /** Class name where the log entry was created. */
    private String _className;

    /** The logging level. */
    private String _level;

    /** Thread on which the log entry was created. */
    private String _thread;

    /** The logging message. */
    private String _message;

    /** Counter of the occurence of a <code>LogEntry</code>. */
    private Integer _count;

    /** Reference to a related exception. */
    private LogExceptionEntry _exception;

    // -----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public LogEntry() { }
    
    /**
     * Construct a new <code>LogEntry</code> with the given message.
     * 
     * @param message The message to set for this <code>LogEntry</code>.
     */
    public LogEntry(final String message) { _message = message; }
    
    // -----------------------------------------------------------------------------------

    /** 
     * Get the identity of this object. 
     *
     * @return The identity of this object.
     */
    public final Integer getId() {
        return _id;
    }

    /** 
     * Set the identity of this object. 
     *
     * @param id The identity of this object.
     */
    public final void setId(final Integer id) {
        _id = id;
    }

    /** 
     * Get the timestamp when the <code>LoggingEvent</code> was created. 
     * 
     * @return Timestamp when the <code>LoggingEvent</code> was created. 
     */
    public final Date getTimestamp() {
        return _timestamp;
    }

    /** 
     * Set the timestamp when the <code>LoggingEvent</code> was created. 
     * 
     * @param timestamp Timestamp when the <code>LoggingEvent</code> was created. 
     */
    public final void setTimestamp(final Date timestamp) {
        _timestamp = timestamp;
    }
    
    /** 
     * Get the class name where the <code>LoggingEvent</code> was created. 
     * 
     * @return Class name where the <code>LoggingEvent</code> was created.
     */
    public final String getClassName() {
        return _className;
    }
    
    /** 
     * Set the class name where the <code>LoggingEvent</code> was created. 
     * 
     * @param className Class name where the <code>LoggingEvent</code> was created.
     */
    public final void setClassName(final String className) {
        _className = className;
    }
    
    /** 
     * Get the logging level. 
     * 
     * @return Logging level.
     */
    public final String getLevel() {
        return _level;
    }
    
    /** 
     * Set the logging level.
     * 
     * @param level Logging level.
     */
    public final void setLevel(final String level) {
        _level = level;
    }
    
    /** 
     * Get the thread on which the <code>LoggingEvent</code> was created. 
     * 
     * @return Thread on which the <code>LoggingEvent</code> was created.
     */
    public final String getThread() {
        return _thread;
    }
    
    /** 
     * Set the thread on which the <code>LoggingEvent</code> was created. 
     * 
     * @param thread Thread on which the <code>LoggingEvent</code> was created.
     */
    public final void setThread(final String thread) {
        _thread = thread;
    }
    
    /** 
     * Get the logging message. 
     * 
     * @return Logging message.
     */
    public final String getMessage() {
        return _message;
    }
    
    /** 
     * Set the logging message.
     * 
     * @param message Logging message.
     */
    public final void setMessage(final String message) {
        _message = message;
    }
    
    /** 
     * Get the count of occurences of the <code>LoggingEvent</code>. 
     * 
     * @return Count of occurences of the <code>LoggingEvent</code>.
     */
    public final Integer getCount() {
        return _count;
    }
    
    /** 
     * Set the count of occurences of the <code>LoggingEvent</code>. 
     * 
     * @param count Count of occurences of the <code>LoggingEvent</code>.
     */
    public final void setCount(final Integer count) {
        _count = count;
    }
    
    /** 
     * Get a reference to a related exception. 
     *
     * @return A reference to a related exception.
     */
    public final LogExceptionEntry getException() {
        return _exception;
    }
    
    /** 
     * Set a reference to a related exception. 
     *
     * @param exception A reference to a related exception.
     */
    public final void setException(final LogExceptionEntry exception) {
        _exception = exception;
    }

    // -----------------------------------------------------------------------------------
}
