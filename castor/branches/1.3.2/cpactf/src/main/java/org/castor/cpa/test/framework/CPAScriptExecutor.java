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
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class CPAScriptExecutor {
    //--------------------------------------------------------------------------

    private static final String MODULE_PREFIX = "cpactf/";
    private static final String MODULE_PATH = "src/test/ddl/";
    private static final String DOT = ".";
    private static final String SEPARATOR = "/";
    private static final String DROP_FILE = "-drop";
    private static final String CREATE_FILE = "-create";
    private static final String FILE_EXTENSION = ".sql";
    
    private static final String COMMENT = "--";
    private static final String DELIMITER_DEFAULT = ";";
    private static final String DELIMITER_ORACLE = "/";
    private static final String DELIMITER_SAPDB = "//";
    private static final String DELIMITER_MSSQL = "GO";

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(CPAScriptExecutor.class);
    
    //--------------------------------------------------------------------------

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
