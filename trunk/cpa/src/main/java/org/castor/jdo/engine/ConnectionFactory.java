/*
 * Copyright 2005 Werner Guttmann, Ralf Joachim
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
package org.castor.jdo.engine;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for creation of new JDBC Connection instances.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-07-24 14:37:16 -0600 (Sun, 24 Jul 2005) $
 * @since 0.9.9
 */
public interface ConnectionFactory {
    /**
     * Creates a new JDBC Connection instance.
     * 
     * @return A JDBC Connection.
     * @throws SQLException If the JDBC connection cannot be created.
     */
    Connection createConnection () throws SQLException;
}
