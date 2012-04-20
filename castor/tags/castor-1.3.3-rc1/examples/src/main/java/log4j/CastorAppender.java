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
package log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.QueryResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>CastorAppender</code> provides sending log events to a database.
 *
 * <p>Each append call adds the <code>LoggingEvent</code> to an <code>ArrayList</code>
 * buffer. When the buffer is filled each log event is saved to the database.
 *
 * <b>DatabaseName</b>, <b>BufferSize</b>, <b>ColumnWidthClass</b>,
 * <b>ColumnWidthThread</b>, <b>ColumnWidthMessage</b>, <b>ColumnWidthStackTrace</b> and
 * <b>DuplicateCount</b> are configurable options in the standard log4j ways.
 *
 * @author  <a href="mailto:holger.west@syscon-informatics.de">Holger West</a>
 */
public final class CastorAppender extends AppenderSkeleton {
    // -----------------------------------------------------------------------------------
    
    /** Default column width for the class column. */
    private static final int COLUMNWIDTHCLASS = 100;
    
    /** Default column width for the thread column. */
    private static final int COLUMNWIDTHTHREAD = 100;
    
    /** Default column width for the message column. */
    private static final int COLUMNWIDTHMESSAGE = 1000;
    
    /** Default column width for the message column. If this value is greater than 4000
     *  and using an oracle database, as minimum the 10g driver is necessary. */
    private static final int COLUMNWIDTHSTACKTRACE = 20000;

    /** Should duplicate entries be replaced with the newest one and count the number of
     *  occurrence or should all records be saved independent. If set to false, all
     *  records are saved independent. */
    private static final boolean DUPLICATECOUNT = false;

    /** List holding all registered <code>CastorAppenders</code>. */
    private static List _elements = new ArrayList();

    /** Default size of LoggingEvent buffer before writting to the database. */
    private int _bufferSize = 1;

    /** ArrayList holding the buffer of Logging Events. */
    private ArrayList _buffer;

    /** Helper object for clearing out the buffer. */
    private ArrayList _removes;
  
    /** The database is opened the first time it is needed and then held open until the
     *  appender is closed. */
    private Database _database;
    
    /** A prepared statement to identify a possible existing entry with the same values.
     *  It is only used if 'duplicateCount' is enabled. */
    private Query _qry;

    /** The name of the database to be used by castor. This <b>must</b> be specified in
     *  the log4j configuration. */
    private String _databaseName;
    
    /** Column width for the class information. */
    private int _columnWidthClass = COLUMNWIDTHCLASS;
    
    /** Column width for the thread information. */
    private int _columnWidthThread = COLUMNWIDTHTHREAD;
    
    /** Column width for the message information. */
    private int _columnWidthMessage = COLUMNWIDTHMESSAGE;
  
    /** Column width for the stack trace information. */
    private int _columnWidthStackTrace = COLUMNWIDTHSTACKTRACE;
    
    /** Replace duplicate entries and count the occurrence? This can be very slow when
     *  saving to the database. */
    private boolean _duplicateCount = DUPLICATECOUNT;
    
    // -----------------------------------------------------------------------------------

    /**
     * Add a new <code>CastorAppender</code> to static list.
     * 
     * @param appender The <code>CastorAppender</code> to be added.
     */
    private static synchronized void addAppender(final CastorAppender appender) {
        _elements.add(appender);
    }
    
    /**
     * Remove a <code>CastorAppender</code> from static list.
     * 
     * @param appender The <code>CastorAppender</code> to be removed.
     */
    private static synchronized void removeAppender(final CastorAppender appender) {
        _elements.remove(appender);
    }
    
    /**
     * Get an array holding all registered <code>CastorAppender</code>.
     * 
     * @return An array holding all registered <code>CastorAppender</code>.
     */
    private static synchronized CastorAppender[] getAppenders() {
        CastorAppender[] appenders = new CastorAppender[_elements.size()];
        return (CastorAppender[]) _elements.toArray(appenders);
    }
    
    /**
     * When the program has ended all logger instances are destroyed. To save all data
     * which are still in the buffer, this method must be called. It saves all data from
     * all registered <code>CastorAppender</code>.
     * <br/>
     * As an alternative <code>org.apache.log4j.LogManager.shutdown()</code> can be
     * called.
     */
    public static void flush() {
        CastorAppender[] appenders = getAppenders();
        if (appenders.length > 0) {
            for (int i = 0; i < appenders.length; i++) {
                appenders[i].flushBuffer();
            }
        }
    }

    // -----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public CastorAppender() {
        super();
        addAppender(this);
        _database = null;
        _buffer = new ArrayList(_bufferSize);
        _removes = new ArrayList(_bufferSize);
    }
    
    /** Closes the appender before disposal. */
    public void finalize() {
        close();
    }

    /**
     * Closes the appender, flushing the buffer first then closing the query and database
     * if it is still open.
     */
    public void close() {
        flushBuffer();
      
        if (_database != null) {
            try {
                _qry.close();
                _database.close();
            } catch (Exception e) {
                errorHandler.error("Error closing database.", e, ErrorCode.CLOSE_FAILURE);
            }
        }
        this.closed = true;
        removeAppender(this);
    }

    // -----------------------------------------------------------------------------------

    /**
     * Adds the event to the buffer.  When full the buffer is flushed.
     * 
     * @param event The event to be logged.
     */
    public synchronized void append(final LoggingEvent event) {
        _buffer.add(event);
        
        if (_buffer.size() >= _bufferSize) {
            flushBuffer();
        }
    }

    /**
     * Loops through the buffer of <code>LoggingEvents</code> and store them into the
     * database. If a statement fails the <code>LoggingEvent</code> stays in the buffer!
     */
    private synchronized void flushBuffer() {
        _removes.ensureCapacity(_buffer.size());
    
        Database db = getDatabase();
        try {
            for (Iterator i = _buffer.iterator(); i.hasNext();) {
                LoggingEvent logEvent = (LoggingEvent) i.next();
                execute(logEvent);
                _removes.add(logEvent);
            }

            db.commit();
            
            _buffer.removeAll(_removes);
            _removes.clear();
        } catch (Exception e) {
            errorHandler.error("Error flush buffer.", e, ErrorCode.GENERIC_FAILURE);
        }
    }

    /**
     * Initialize the database and create the query. If the database is already
     * initialized, only return the database. In both cases a transaction is started. 
     * 
     * @return The initialized database.
     */
    private Database getDatabase() {
        if (_database == null) {
            try {
                _database = JDOManager.createInstance(_databaseName).getDatabase();
                _database.begin();
                String oql = "select o from " + LogEntry.class.getName() 
                + " o where o.className = $1 and"
                + " o.level = $2 and"
                + " o.message = $3";
                _qry = _database.getOQLQuery(oql);
            } catch (Exception e) {
                errorHandler.error("Error get database.", e, ErrorCode.GENERIC_FAILURE);
            }
        } else {
            try {
                _database.begin();
            } catch (Exception e) {
                errorHandler.error(
                        "Cannot begin a transaction.", e, ErrorCode.GENERIC_FAILURE);
            }
        }
        return _database;
    }
  
    /**
     * Save the given <code>LoggingEvent</code> to the database. If 'duplicateCount' is
     * enabled, a possible earlier entry is updated. Events with exceptions are stored
     * ever.
     * 
     * @param event The <code>LoggingEvent</code> to be saved.
     */
    private void execute(final LoggingEvent event) {
        LogEntry entry;
        if (event.getMessage() instanceof LogEntry) {
            entry = (LogEntry) event.getMessage();
        } else if (event.getMessage() != null) {
            String message = event.getMessage().toString();
            message = clipLength(message, _columnWidthMessage);
            entry = new LogEntry(message);
        } else {
            entry = new LogEntry();
        }
        
        String clazz = event.getLoggerName();
        clazz = clipLength(clazz, _columnWidthClass);
        entry.setClassName(clazz);

        String thread = event.getThreadName();
        thread = clipLength(thread, _columnWidthThread);
        entry.setThread(thread);

        entry.setLevel(event.getLevel().toString());
        entry.setTimestamp(new Date(event.timeStamp));
        
        //-----------------------------------------------------------------
        
        boolean hasException = (event.getThrowableInformation() != null);
        
        if (hasException) {
            if (_columnWidthStackTrace > 0) {
                LogExceptionEntry exceptionEntry = new LogExceptionEntry();
                
                String temp = "";
                String[] stackTrace = event.getThrowableStrRep();
                int stackSize = stackTrace.length;
                for (int i = 0; i < stackSize; i++) {
                    temp = temp.concat(stackTrace[i] + "\n");
                }
                
                temp = clipLength(temp, _columnWidthStackTrace);
                
                exceptionEntry.setStackTrace(temp);
                exceptionEntry.setEntry(entry);
                entry.setException(exceptionEntry);
            }
        }
        
        //-----------------------------------------------------------------

        try {
            if (!hasException && _duplicateCount) {
                _qry.bind(entry.getClassName());
                _qry.bind(entry.getLevel());
                _qry.bind(entry.getMessage());
                
                    QueryResults rst = _qry.execute();
                    if (rst.hasMore()) {
                        LogEntry x = (LogEntry) rst.next();
                        x.setTimestamp(entry.getTimestamp());
                        x.setThread(entry.getThread());
                        x.setCount(new Integer(x.getCount().intValue() + 1));
                    } else {
                        entry.setCount(new Integer(1));
                        _database.create(entry);
                    }
                    rst.close();
            } else {
                entry.setCount(new Integer(1));
                _database.create(entry);
            }
        } catch (Exception e) {
            errorHandler.error("Cannot save the object.", e, ErrorCode.FLUSH_FAILURE);
        }
    }
    
    /**
     * Clip a string to ensure the length. If the string is longer, the rear part is
     * clipped.
     * 
     * @param value The string to cut.
     * @param maxLength The maximum length of this value.
     * @return The clipped String.
     */
    private String clipLength(final String value, final int maxLength) {
        if (value.length() > maxLength) {
            return value.substring(0, maxLength);
        } 
        return value;
    }

    /**
     * CastorAppender don't requires a layout.
     * 
     * @return <code>true</code> if this appender require a layout, otherwise
     *         <code>false</code>.
     * */
    public boolean requiresLayout() {
        return false;
    }

    // -----------------------------------------------------------------------------------

    /**
     * Set the size of the buffer.
     * 
     * @param newBufferSize New size of the buffer.
     */
    public void setBufferSize(final int newBufferSize) {
        _bufferSize = newBufferSize;
        _buffer.ensureCapacity(_bufferSize);
        _removes.ensureCapacity(_bufferSize);
    }

    /**
     * Get the size of the buffer.
     * 
     * @return The size of the buffer.
     */
    public int getBufferSize() {
        return _bufferSize;
    }
    
    /**
     * Set the name of the database.
     * 
     * @param name Name of the database.
     */
    public void setDatabaseName(final String name) {
        _databaseName = name;
    }
    
    /**
     * Get the name of the database.
     * 
     * @return Name of the database.
     */
    public String getDatabaseName() {
        return _databaseName;
    }
    
    /**
     * Set the column width for class information.
     * 
     * @param columWidth The column width for class information.
     */
    public void setColumnWidthClass(final int columWidth) {
        _columnWidthClass = columWidth;
    }
    
    /**
     * Get the column width for class information.
     * 
     * @return The column width for class information.
     */
    public int getColumnWidthClass() {
        return _columnWidthClass;
    }

    /**
     * Set the column width for thread information.
     * 
     * @param columWidth The column width for thread information.
     */
    public void setColumnWidthThread(final int columWidth) {
        _columnWidthThread = columWidth;
    }
    
    /**
     * Get the column width for tread information.
     * 
     * @return The column width for thread information.
     */
    public int getColumnWidthThread() {
        return _columnWidthThread;
    }
    
    /**
     * Set the column width for message information.
     * 
     * @param columWidth The column width for message information.
     */
    public void setColumnWidthMessage(final int columWidth) {
        _columnWidthMessage = columWidth;
    }
    
    /**
     * Get the column width for message information.
     * 
     * @return The column width for message information.
     */
    public int getColumnWidthMessage() {
        return _columnWidthMessage;
    }

    /**
     * Set the column width for stack trace information.
     * 
     * @param columWidth The column width for stack trace information.
     */
    public void setColumnWidthStackTrace(final int columWidth) {
        _columnWidthStackTrace = columWidth;
    }
    
    /**
     * Get the column width for stack trace information.
     * 
     * @return The column width for stack trace information.
     */
    public int getColumnWidthStackTrace() {
        return _columnWidthStackTrace;
    }

    /**
     * Set duplicate count.
     * 
     * @param duplicateCount Should duplicate count be enabled?
     */
    public void setDuplicateCount(final String duplicateCount) {
        String temp = duplicateCount.toLowerCase();
        if ("true".equals(temp)) {
            _duplicateCount = true;
        } else {
            _duplicateCount = false;
        }
    }
    
    /**
     * Is duplicate count enabled?
     * 
     * @return <code>true</code> if duplicate count is enabled, otherwise
     *         <code>false</code>.
     */
    public String getDuplicateCount() {
        return new Boolean(_duplicateCount).toString();
    }

    // -----------------------------------------------------------------------------------
}
