/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.cpa.persistence.sql.driver;

import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * QueryExpression implementation for Borland's Pointbase.
 *
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-12 15:13:08 -0600 (Wed, 12 Apr 2006) $
 * @since 1.0M2
 */
public final class PointbaseQueryExpression extends JDBCQueryExpression {
    /**
     * Craetes an instance of this class.
     * 
     * @param factory PersistenceFactory instance.
     */
    public PointbaseQueryExpression(final PersistenceFactory factory) {
        super(factory);
    }

    /**
     * {@inheritDoc}
     */
    public String getStatement(final boolean lock) {
        return getStandardStatement(lock, false).toString();
    }
}


