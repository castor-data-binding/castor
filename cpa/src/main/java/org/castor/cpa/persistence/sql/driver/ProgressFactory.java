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

import org.exolab.castor.jdo.drivers.GenericFactory;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * {@link org.exolab.castor.persist.spi.PersistenceFactory} for Progress RDBMS.
 * 
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2006-02-21 16:05:42 -0700 (Tue, 21 Feb 2006) $
 */
public final class ProgressFactory extends GenericFactory {
    /** Internal name of this PersistenceFactory instance. */
    public static final String FACTORY_NAME = "progress";

    /**
     * @inheritDoc
     */
    public String getFactoryName() {
        return FACTORY_NAME;
    }

    /**
     * @inheritDoc
     */
    public QueryExpression getQueryExpression() {
        return new ProgressQueryExpression(this);
    }
    
    /**
     * @inheritDoc
     */
    public String quoteName(final String name) {
        return doubleQuoteName(name);
    }
}
