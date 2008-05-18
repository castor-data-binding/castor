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
    private abstract class AbstractIdentityValue implements IdentityValue {
        public AbstractIdentityValue() { }
    }

    private class BigDecimalIdentityValue extends AbstractIdentityValue {
        public Object getValue(final ResultSet rs) throws SQLException {
            return rs.getBigDecimal(1);
        }
    }

    private class IntegerIdentityValue extends AbstractIdentityValue {
        public Object getValue(final ResultSet rs) throws SQLException {
            return new Integer(rs.getInt(1));
        }
    }

    private class LongIdentityValue extends AbstractIdentityValue {
        public Object getValue(final ResultSet rs) throws SQLException {
            return new Long(rs.getLong(1));
        }
    }

    private class StringIdentityValue extends AbstractIdentityValue {
        public Object getValue(final ResultSet rs) throws SQLException {
            return rs.getString(1);
        }
    }

    protected PersistenceFactory _factory;

    protected String _factoryName;

    protected byte _style = AFTER_INSERT;

    protected IdentityValue _identityValue = null;

    protected void checkSupportedFactory(final PersistenceFactory factory)
    throws MappingException {
        _factoryName = factory.getFactoryName();
        String supportedFactoryNames[] = getSupportedFactoryNames();
        boolean supported = false;
        for (int i = 0; i < supportedFactoryNames.length; i++) {
            if (_factoryName.equals(supportedFactoryNames[i])) {
                supported = true;
            }
        }

        if (!supported) {
            String msg = Messages.format("mapping.keyGenNotCompatible", getClass().getName(), _factoryName); 
            throw new MappingException(msg);
        }
    }

    public void supportsSqlType(final int sqlType) throws MappingException {
        if (sqlType != Types.INTEGER
                && sqlType != Types.NUMERIC
                && sqlType != Types.DECIMAL
                && sqlType != Types.BIGINT) {
            String msg = Messages.format("mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)); 
            throw new MappingException(msg);
        }
    }

    /**
     * Style of key generator: BEFORE_INSERT, DURING_INSERT or AFTER_INSERT ?
     */
    public byte getStyle() {
        return _style;
    }

    public abstract String[] getSupportedFactoryNames();

    protected void initIdentityValue(final int sqlType) {
        if (sqlType == Types.INTEGER) {
            _identityValue = new IntegerIdentityValue();
        } else if (sqlType == Types.BIGINT) {
            _identityValue = new LongIdentityValue();
        } else if ((sqlType == Types.CHAR) || (sqlType == Types.VARCHAR)) {
            _identityValue = new StringIdentityValue();
        } else {
            _identityValue = new BigDecimalIdentityValue();
        }
    }

    /**
     * Is key generated in the same connection as INSERT? Default is true.
     */
    public boolean isInSameConnection() {
        return true;
    }

    /**
     * Gives a possibility to patch the Castor-generated SQL statement
     * for INSERT (makes sense for DURING_INSERT key generators).
     */
    public String patchSQL(final String insert, final String primKeyName)
    throws MappingException {
        return insert;
    }
}
