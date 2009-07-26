package org.castor.cpa.persistence.sql.driver;

import java.sql.Types;

import org.exolab.castor.persist.spi.QueryExpression;

/**
 * {@link org.exolab.castor.persist.spi.PersistenceFactory} for Interbase driver.
 *
 */
public final class InterbaseFactory extends GenericFactory {
    //-----------------------------------------------------------------------------------

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
    
    @Override
    public boolean isKeyGeneratorSequenceSupported(final boolean returning, final boolean trigger) {
        return !returning;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isKeyGeneratorSequenceTypeSupported(final int type) {
        if (type == Types.INTEGER) { return true; }
        if (type == Types.DECIMAL) { return true; }
        if (type == Types.NUMERIC) { return true; }
        if (type == Types.BIGINT) { return true; }
        if (type == Types.CHAR) { return true; }
        if (type == Types.VARCHAR) { return true; }

        return false;
    }
    
    //-----------------------------------------------------------------------------------
}


