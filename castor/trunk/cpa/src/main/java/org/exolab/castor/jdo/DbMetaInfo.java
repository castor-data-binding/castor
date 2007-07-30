/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.jdo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Database meta information encapsulation
 * This is currently used to get the database version out of a
 * JDBC database connection and enable comparing against some
 * required version string.  
 * 
 * @author Martin Fuchs <martin-fuchs AT gmx DOT net></a>
 * @version $Revision$
 */
public final class DbMetaInfo {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging </a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(DbMetaInfo.class);

    private DatabaseMetaData _connInfo;
    private Connection _conn = null;
    private String _dbVersion = null;

    /**
     * 
     * @param conn JDBC connection
     */
    public DbMetaInfo(final Connection conn) {
        _conn = conn;
    }

    /**
     * delayed initialization function to avoid unnecessary metadata queries
     * @todo This algorithm should also be tested against other databases
     *       than Oracle, however it's currently only used for this DB type.   
     */
    private void init() {
        if (_connInfo == null) {
            try {
                _connInfo = _conn.getMetaData();

                String dbProdVer = _connInfo.getDatabaseProductVersion();
            
                 // find the first numeric word in the version string 
                int i = 0;
                for ( ;; ) {
                     int n = dbProdVer.indexOf(' ', i);
        
                     String word = (n != -1) ? dbProdVer.substring(i, n) : dbProdVer.substring(i);
        
                     if (Character.isDigit(word.charAt(0))) {
                         _dbVersion = word;
                         break;
                     }

                    if (n == -1) {
                        break;
                    }

                    i = n + 1;
                }
            } catch (SQLException e) {
                _dbVersion = "";
                _log.error(e);
            }
        }
    }

    /**
     * return the version string for the current database conection
     * @return database version string
     */
    public String getDbVersion() {
        init();

        return _dbVersion;
    }

    /**
     * compare the actual database version
     * with the given required version string
     * @param version
     * @return -1 -> lower  0 -> equal  1 -> higher
     */
    public int compareDbVersion(final String version) {
        init();

        return compareVersionStrings(_dbVersion, version);
    }

    static int compareVersionStrings(final String v1, final String v2) {
        int p1 = 0;
        int p2 = 0;

        for ( ;; ) {
            int n1 = v1.indexOf('.', p1);
            int n2 = v2.indexOf('.', p2);

            String s1 = (n1 != -1) ? v1.substring(p1, n1) : v1.substring(p1);
            String s2 = (n2 != -1) ? v2.substring(p2, n2) : v2.substring(p2);
            
            int x1 = (s1.length() > 0) ? Integer.parseInt(s1) : 0;
            int x2 = (s2.length() > 0) ? Integer.parseInt(s2) : 0;

            if (x1 < x2) {
                return -1;
            } else if (x1 > x2) {
                return 1;
            }

            if ((n1 == -1) && (n2 == -1)) {
                return 0;
            }

            p1 = (n1 != -1) ? n1 + 1 : v1.length();
            p2 = (n2 != -1) ? n2 + 1 : v2.length();
        }
    }
}