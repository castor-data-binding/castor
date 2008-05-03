

package org.exolab.castor.jdo.drivers;


import org.exolab.castor.persist.spi.QueryExpression;


/**
 * {@link org.exolab.castor.persist.spi.PersistenceFactory} for Interbase driver.
 *
 */
public final class InterbaseFactory extends GenericFactory {
    public static final String FACTORY_NAME = "interbase";

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
        return new InterbaseQueryExpression(this);
    }
}


