package org.castor.cpa.persistence.sql.keygen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.castor.util.Messages;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * @author <a href="mailto:dulci@start.no">Stein M. Hugubakken</a>
 */
public abstract class AbstractKeyGenerator implements KeyGenerator {
    private class IntegerSqlTypeHandler implements SqlTypeHandler {
        public Object getValue(final ResultSet rs) throws SQLException {
            return new Integer(rs.getInt(1));
        }
    }

    private class LongSqlTypeHandler implements SqlTypeHandler {
        public Object getValue(final ResultSet rs) throws SQLException {
            return new Long(rs.getLong(1));
        }
    }

    private class BigDecimalSqlTypeHandler implements SqlTypeHandler {
        public Object getValue(final ResultSet rs) throws SQLException {
            return rs.getBigDecimal(1);
        }
    }

    private class StringSqlTypeHandler implements SqlTypeHandler {
        public Object getValue(final ResultSet rs) throws SQLException {
            return rs.getString(1);
        }
    }

    protected PersistenceFactory _factory;

    protected String _factoryName;

    private SqlTypeHandler _sqlTypeHandler;

    private byte _style;

    protected void checkSupportedFactory(final PersistenceFactory factory)
    throws MappingException {
        _factoryName = factory.getFactoryName();
        String[] supportedFactoryNames = getSupportedFactoryNames();
        boolean supported = false;
        for (int i = 0; i < supportedFactoryNames.length; i++) {
            if (_factoryName.equals(supportedFactoryNames[i])) {
                supported = true;
            }
        }

        if (!supported) {
            String msg = Messages.format("mapping.keyGenNotCompatible",
                    getClass().getName(), _factoryName); 
            throw new MappingException(msg);
        }
    }

    protected abstract String[] getSupportedFactoryNames();

    protected void initSqlTypeHandler(final int sqlType) {
        if (sqlType == Types.INTEGER) {
            _sqlTypeHandler = new IntegerSqlTypeHandler();
        } else if (sqlType == Types.BIGINT) {
            _sqlTypeHandler = new LongSqlTypeHandler();
        } else if ((sqlType == Types.CHAR) || (sqlType == Types.VARCHAR)) {
            _sqlTypeHandler = new StringSqlTypeHandler();
        } else {
            _sqlTypeHandler = new BigDecimalSqlTypeHandler();
        }
    }
    
    protected SqlTypeHandler getSqlTypeHandler() {
        return _sqlTypeHandler;
    }

    /**
     * {@inheritDoc}
     */
    public void supportsSqlType(final int sqlType) throws MappingException {
        if (sqlType != Types.INTEGER
                && sqlType != Types.NUMERIC
                && sqlType != Types.DECIMAL
                && sqlType != Types.BIGINT) {
            String msg = Messages.format("mapping.keyGenSQLType",
                    getClass().getName(), new Integer(sqlType)); 
            throw new MappingException(msg);
        }
    }

    protected final void setStyle(final byte style) {
        _style = style;
    }

    /**
     * {@inheritDoc}
     */
    public final byte getStyle() {
        return _style;
    }

    /**
     * {@inheritDoc}
     */
    public String patchSQL(final String insert, final String primKeyName)
    throws MappingException {
        return insert;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInSameConnection() {
        return true;
    }
}
