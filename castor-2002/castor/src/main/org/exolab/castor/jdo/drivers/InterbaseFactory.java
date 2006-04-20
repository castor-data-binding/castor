

package org.exolab.castor.jdo.drivers;


import java.sql.SQLException;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.spi.PersistenceQuery;


/**
 * {@link PersistenceFactory} for Interbase driver.
 *
 */
public final class InterbaseFactory
    extends GenericFactory
{


    public String getFactoryName()
    {
        return "interbase";
    }


    public QueryExpression getQueryExpression()
    {
        return new InterbaseQueryExpression( this );
    }

/*
    public Boolean isDuplicateKeyException( Exception except )
    {
        if ( except instanceof SQLException )
            return ( (SQLException) except ).getErrorCode() == 335544349 ? Boolean.TRUE : Boolean.FALSE;
        return null;
    }
*/
}


