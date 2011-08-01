/*
 * Copyright 2010 Ralf Joachim
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
package org.castor.cpa.test.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

/**
 * Execute create and drop DDL scripts of CPA test framework. 
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class CPAScriptExecutor {
    //--------------------------------------------------------------------------

    /** "cpactf" prefix of script path. */
    private static final String MODULE_PREFIX = "cpactf/";
    
    /** Path to find CREATE and DROP scripts. */
    private static final String MODULE_PATH = "src/test/ddl/";
    
    /** Constant for a "dot". */
    private static final String DOT = ".";
    
    /** Constant for the separator. */
    private static final String SEPARATOR = "/";
    
    /** Filename suffix of SQL DROP scripts. */
    private static final String DROP_FILE = "-drop";
    
    /** Filename suffix of SQL CREATE scripts. */
    private static final String CREATE_FILE = "-create";
    
    /** Extension of SQL script files.  */
    private static final String FILE_EXTENSION = ".sql";
    
    /** String for comments. */
    private static final String COMMENT = "--";
    
    /** String for default delimiter. */
    private static final String DELIMITER_DEFAULT = ";";
    
    /** String for Oracle-specific delimiter. */
    private static final String DELIMITER_ORACLE = "/";
    
    /** String for SAPDB-specific delimiter. */
    private static final String DELIMITER_SAPDB = "//";
    
    /** String for MSSQL-specific delimiter. */
    private static final String DELIMITER_MSSQL = "GO";

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(CPAScriptExecutor.class);
    
    //--------------------------------------------------------------------------

    /**
     * Execute DROP and CREATE script for given test. 
     * 
     * @param engine The database engine, that is used to run the test. 
     * @param connection The currently used connection to execute the scripts on. 
     * @param test The name of the test to execute. 
     */
    public static void execute(final DatabaseEngineType engine, final Connection connection,
            final String test) {
        String path = MODULE_PATH + test.replace(DOT, SEPARATOR) + SEPARATOR;
        if (!new File(path).exists()) {
            path = MODULE_PREFIX + path;
        }

        String dropFileName = path + engine.toString() + DROP_FILE + FILE_EXTENSION;
        File dropFile = new File(dropFileName);
        try {
            List<String> dropScripts = parse(new FileReader(dropFile), engine);
            executeDrop(dropScripts, connection);
            LOG.info("Drop script for '" + test + "' executed.");
        } catch (SQLException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Drop script for '" + test + "' failed.", ex);
            } else {
                LOG.info("Drop script for '" + test + "' failed.");
            }
        } catch (FileNotFoundException ex) {
            String msg = "Drop script for '" + test + "' not found.";
            throw new IllegalStateException(msg);
        } catch (IOException ex) {
            String msg = "Drop script for '" + test + "' could not be read.";
            throw new IllegalStateException(msg);
        }

        String createFileName = path + engine.toString() + CREATE_FILE + FILE_EXTENSION;
        File createFile = new File(createFileName);
        try {
            List<String> createScripts = parse(new FileReader(createFile), engine);
            executeCreate(createScripts, connection);
            LOG.info("Create script for '" + test + "' executed.");
        } catch (SQLException ex) {
            String msg = "Create script for '" + test + "' failed.";
            throw new IllegalStateException(msg, ex);
        } catch (FileNotFoundException ex) {
            String msg = "Create script for '" + test + "' not found.";
            throw new IllegalStateException(msg);
        } catch (IOException ex) {
            String msg = "Create script for '" + test + "' could not be read.";
            throw new IllegalStateException(msg);
        }
    }
    
    /**
     * Execute given DROP statements on given connection. 
     * 
     * @param statements A list of DROP statements to execute. 
     * @param connection The currently used connection to execute the scripts on. 
     * @throws SQLException if a database access error occurs, this method is called 
     *         on a closed Statement or the given SQL statement produces a ResultSet object
     */
    private static void executeDrop(final List<String> statements, final Connection connection)
    throws SQLException {
        SQLException firstException = null;
        
        for (Iterator<String> iter = statements.iterator(); iter.hasNext(); ) {
            String statement = iter.next();
            Statement sql = null;
            try {
                sql = connection.createStatement();
                sql.executeUpdate(statement);
            } catch (SQLException ex) {
                // just remember first exceptions on failing statements of drop script
                if (firstException == null) {
                    firstException = ex;
                }
            } finally {
                if (sql != null) {
                    sql.close();
                }
            }
        }

        if (firstException != null) { throw firstException; }
    }
    
    /**
     * Execute given CREATE statements on given connection.  
     * 
     * @param statements A list of CREATE statements to execute. 
     * @param connection The currently used connection to execute the scripts on. 
     * @throws SQLException if a database access error occurs, this method is called 
     *         on a closed Statement or the given SQL statement produces a ResultSet object
     */
    private static void executeCreate(final List<String> statements, final Connection connection)
    throws SQLException {
        Statement sql = null;
        try {
            for (Iterator<String> iter = statements.iterator(); iter.hasNext(); ) {
                String statement = iter.next();
                sql = connection.createStatement();
                sql.executeUpdate(statement);
                sql.close();
                sql = null;
            }
        } finally {
            if (sql != null) {
                sql.close();
            }
        }
    }
    
    /**
     * Read from given reader and divide input into multiple Strings. 
     * Use the database engine-specific delimiter to recognize statements. 
     * 
     * @param reader An reader on a SQL script file. 
     * @param engine The database engine, that is used to run the test. 
     * @return List of statements separated by a delimiter. 
     * @throws IOException If an I/O error occurs. 
     */
    public static List<String> parse(final Reader reader, final DatabaseEngineType engine)
    throws IOException {
        List<String> list = new ArrayList<String>();
        
        String delimiter = getStatementDelimiter(engine);

        StringBuilder builder = new StringBuilder();
        BufferedReader buffer = new BufferedReader(reader);
        String line = buffer.readLine();
        while (line != null) {
            String trim = line.trim();
            
            if ((trim.length() != 0) && !line.startsWith(COMMENT)) {
                if (trim.toUpperCase().endsWith(delimiter)) {
                    builder.append(trim.substring(0, trim.length() - delimiter.length()));
                    list.add(builder.toString());
                    
                    builder = new StringBuilder();
                } else {
                    builder.append(trim);
                    builder.append(' ');
                }
            }

            line = buffer.readLine();
        }
        
        return list;
    }
    
    /**
     * Provide the database engine-specific delimiter. 
     * 
     * @param engine The database engine to get fitting delimiter for. 
     * @return String that represents the delimiter for the given database engine. 
     */
    private static String getStatementDelimiter(final DatabaseEngineType engine) {
        if (engine == DatabaseEngineType.ORACLE) {
            return DELIMITER_ORACLE;
        } else if (engine == DatabaseEngineType.SAPDB) {
            return DELIMITER_SAPDB;
        } else if (engine == DatabaseEngineType.SQL_SERVER) {
            return DELIMITER_MSSQL;
        } else if (engine == DatabaseEngineType.SYBASE) {
            return DELIMITER_MSSQL;
        } else {
            return DELIMITER_DEFAULT;
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Hide default constructor of utility class.
     */
    private CPAScriptExecutor() { }

    //--------------------------------------------------------------------------
}
