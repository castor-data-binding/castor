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
 * QueryExpression for Progress RDBMS.
 * 
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2004-09-08 03:37:54 -0600 (Wed, 08 Sep 2004) $
 */
public final class ProgressQueryExpression extends JDBCQueryExpression {

    /**
     * Creates an instance of this class.
     * 
     * @param factory The persistence factory to be assigned against.
     */
    public ProgressQueryExpression(final PersistenceFactory factory) {
        super(factory);
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.persist.spi.QueryExpression#getStatement(boolean)
     */
    public String getStatement(final boolean lock) {
        StringBuffer sql;

        sql = getStandardStatement(lock, false);

//        // support for LIMIT clause
//        if (_limit != null) {
//            // TODO[WG]: investigate whether Progress has support for LIMIT clauses
//            // sql.append(_limit);
//        }

//        if (lock) {
//            // TODO[WG]: investigate whether Progress has support for ROW level locking
//            // sql.append(" FOR UPDATE");
//        }

        return sql.toString();
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.persist.spi.QueryExpression#isLimitClauseSupported()
     */
    public boolean isLimitClauseSupported() {
        return false;
    }

}
