/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test02;

import java.sql.Connection;
import java.sql.Statement;

import org.exolab.castor.jdo.JDOManager;

public final class ConcurrentUpdateThread extends Thread {
    private JDOManager _jdo;
    
    public ConcurrentUpdateThread(final JDOManager jdo) {
        super();

        _jdo = jdo;
    }
    
    public void run() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = _jdo.getConnectionFactory().createConnection();
            stmt = conn.createStatement();
            stmt.execute("UPDATE test02_sample SET value1='" + TestConcurrent.JDBC_VALUE + "' "
                    + "WHERE id=" + Sample.DEFAULT_ID);
            stmt.close();
            conn.close();
        } catch (Exception ex) {
        } finally {
            try {
                if (stmt != null) { stmt.close(); }
                if (conn != null) { conn.close(); }
            } catch (Exception ex) {
            }
        }
    }
}
