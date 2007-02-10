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
package org.castor.jdo.drivers;

import org.exolab.castor.jdo.drivers.GenericFactory;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * {@link org.exolab.castor.persist.spi.PersistenceFactory} implementation for 
 * Borland's Pointbase driver.
 *
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-12 15:13:08 -0600 (Wed, 12 Apr 2006) $
 * @since 1.0M2
 */
public final class PointbaseFactory extends GenericFactory {
    /**
     * {@inheritDoc}
     * @see org.exolab.castor.persist.spi.PersistenceFactory#getFactoryName()
     */
    public String getFactoryName() {
        return "pointbase";
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.persist.spi.PersistenceFactory#getQueryExpression()
     */
    public QueryExpression getQueryExpression() {
        return new PointbaseQueryExpression(this);
    }
}


