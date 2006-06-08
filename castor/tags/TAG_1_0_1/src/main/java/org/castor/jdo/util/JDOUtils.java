/*
 * Copyright 2005 Stein M. Hugubakken, Ralf Joachim
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
package org.castor.jdo.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;

import org.exolab.castor.jdo.Database;

/**
 * Common static methods for Castor JDO
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @author <a href="mailto:dulci AT start DOT no">Stein M. Hugubakken</a>
 * @version $Revision$ $Date: 2006-05-24 08:49:08 -0600 (Wed, 24 May 2006) $
 * @since 0.9.9.1
 */
public final class JDOUtils {
    //-------------------------------------------------------------------------

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging </a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(JDOUtils.class);

    //-------------------------------------------------------------------------

    /**
     * Closes the Connection without throwing SQLException. A warning is added
     * to the log if SQLException is thrown.
     * 
     * @param conn The Connection to close
     */
    public static void closeConnection(final Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) { conn.close(); }
            } catch (SQLException e) {
                LOG.warn(Messages.message("persist.connClosingFailed"), e);
            }
        }
    }

    /**
     * Closes the ResultSet without throwing SQLException. A warning is added to
     * the log if SQLException is thrown.
     * 
     * @param rs The ResultSet to close
     */
    public static void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.warn(Messages.message("persist.rsClosingFailed"), e);
            }
        }
    }

    /**
     * Closes the Statement without throwing SQLException. A warning is added to
     * the log if SQLException is thrown.
     * 
     * @param stmt The Statement to close
     */
    public static void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.warn(Messages.message("persist.stClosingFailed"), e);
            }
        }
    }

    /**
     * Closes the Database without throwing exceptions. An active transaction will
     * silently rolled back. Warnings are added to the log if any exception occures.
     *
     * @param db The database to close.
     */
    public static void closeDatabase(final Database db) {
        if ((db != null) && !db.isClosed()) {
            synchronized (db) {
                if (db.isActive()) {
                    // if transction is still active it will be rolled back.
                    try {
                        db.rollback();
                    } catch (Exception e) {
                        // this should never happen but anyway we log it
                        LOG.warn("Failed to rollback an active transaction.", e);
                    }
                } 
                
                if (!db.isClosed()) {
                    // database is not closed but should be commited or rolled back now,
                    // so we can close it now. 
                    try {
                        db.close();
                    } catch (Exception e) {
                        // this should never happen but anyway we log it
                        LOG.warn("Failed to close an open database.", e);
                    }
                }
            }
        }
    }

    //-------------------------------------------------------------------------

    /**
     * Hide default constructor of utility classes.
     */
    private JDOUtils() { }

    //-------------------------------------------------------------------------
}
