package org.exolab.castor.jdo.drivers;

import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;

/**
  *  Persistence factory for InstantDB database (<a href="http://instantdb.enhydra.org/">http://instantdb.enhydra.org/</a> ). 
  *  <p>
  *  Example <code>database.xml</code> file for JDO
  *  <br>
  *  <pre>
  *   &lt;database name="test" engine="instantdb" &gt;
  *       &lt;driver class-name="org.enhydra.instantdb.jdbc.idbDriver" 
  *               url="jdbc:idb:C:\\castor-0.8.8\\db\\test\\test.prp"&gt;
  *         &lt;param name="user" value="" /&gt;
  *         &lt;param name="password" value="" /&gt;
  *       &lt;/driver&gt;
  *       &lt;mapping href="mapping.xml" /&gt;
  *    &lt;/database&gt;
  *  </pre> 
  *
  *  @author <a href="mailto:bozyurt@san.rr.com">I. Burak Ozyurt</a>
  *  @version 1.0
  */

public class InstantDBFactory extends GenericFactory {
    public static final String FACTORY_NAME = "instantdb";

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
        return new InstantDBQueryExpression(this);
    }

    /**
     * @inheritDoc
     */
    public Boolean isDuplicateKeyException(final Exception except) {
        return null;
    }

    /**
     * Needed to process OQL queries of "CALL" type (using stored procedure
     * call). This feature is specific for JDO.
     * 
     * @param call Stored procedure call (without "{call")
     * @param paramTypes The types of the query parameters
     * @param javaClass The Java class of the query results
     * @param fields The field names
     * @param sqlTypes The field SQL types
     * @return null if this feature is not supported.
     */
    public PersistenceQuery getCallQuery(final String call, final Class[] paramTypes, final Class javaClass, final String[] fields, final int[] sqlTypes) {
        // stored procedures are not supported by Instant DB
        return null;
    }

    /**
     * For NUMERIC type ResultSet.getObject() returns Double instead of BigDecimal for InstantDB.
     * <br/>
     * @inheritDoc
     */
    public Class adjustSqlType(final Class sqlType) {
        if (sqlType == java.math.BigDecimal.class) {
            return java.lang.Double.class;
        }
        return sqlType;
    }
}
