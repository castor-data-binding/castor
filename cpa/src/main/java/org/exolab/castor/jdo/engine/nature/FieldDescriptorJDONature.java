package org.exolab.castor.jdo.engine.nature;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.mapping.TypeConvertor;

public class FieldDescriptorJDONature extends BaseNature {

    /**
     * Nature property name for sql name.
     */
    private static final String SQL_NAME = "sqlName";
    /**
     * Nature property name for sql many key.
     */
    private static final String MANY_KEY = "manyKey";
    /**
     * Nature property name for sql many table.
     */
    private static final String MANY_TABLE = "manyTable";
    /**
     * Nature property name for sql read only.
     */
    private static final String READ_ONLY = "readOnly";
    /**
     * Nature property name for sql dirty check.
     */
    private static final String DIRTY_CHECK = "dirtyCheck";
    /**
     * Nature property name for sql type convertor.
     */
    private static final String TYPE_CONVERTOR = "typeConvertor";
    /**
     * Nature property name for sql types.
     */
    private static final String SQL_TYPE = "sqlType";

    /**
     * Creates an instance of {@link FieldDescriptorJDONature}.
     * @param holder The {@link PropertyHolder} to 'view upon'.
     */
    public FieldDescriptorJDONature(final PropertyHolder holder) {
        super(holder);
    }

    /**
     * @inheritDoc
     * @see org.castor.core.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }

    /**
     * Returns the SQL (column) name of the field.
     * @return The SQL (column) name.
     */
    public String[] getSQLName() {
        return (String[]) getProperty(SQL_NAME);
    }
    
    /**
     * Sets the SQL (column) name of the field.
     * @param sqlName The SQL (column) name.
     */
    public void setSQLName(final String[] sqlName) {
        setProperty(SQL_NAME, sqlName);
    }

    /**
     * Returns the SQL many key of the field.
     * 
     * @return The SQL many key.
     */
    public String[] getManyKey() {
        return (String[]) getProperty(MANY_KEY);
    }

    /**
     * Sets the SQL many key of the field.
     * 
     * @param manyKey The SQL (many key.
     */
    public void setManyKey(final String[] manyKey) {
        setProperty(MANY_KEY, manyKey);
    }

    /**
     * Returns the SQL many table of the field.
     * 
     * @return The SQL many table.
     */
    public String getManyTable() {
        return (String) getProperty(MANY_TABLE);
    }

    /**
     * Sets the SQL many table of the field.
     * 
     * @param manyTable The SQL many table.
     */
    public void setManyTable(final String manyTable) {
        setProperty(MANY_TABLE, manyTable);
    }

    /**
     * Returns true if field access is read only.
     * 
     * @return True if field access is read-only.
     */
    public boolean isReadonly() {
        return getBooleanPropertyDefaultFalse(READ_ONLY);
    }
    
    public void setReadOnly(final boolean readOnly) {
        setProperty(READ_ONLY, new Boolean(readOnly));
    }

    /**
     * Returns true if dirty checking is required for this field.
     * @return True if dirty checking required
     */
    public boolean isDirtyCheck() {
        return getBooleanPropertyDefaultFalse(DIRTY_CHECK);
    }

    public void setDirtyCheck(final boolean dirtyCheck) {
        setProperty(DIRTY_CHECK, new Boolean(dirtyCheck));
    }
    
    /**
     * TODO improve
     * Returns the convertor from the field type to an external type.
     * @return Convertor from field type
     */
    public TypeConvertor getConvertor() {
        return (TypeConvertor) getProperty(TYPE_CONVERTOR);
    }

    public void setTypeConvertor(final TypeConvertor typeConvertor) {
        setProperty(TYPE_CONVERTOR, typeConvertor);
    }

    /**
     * Returns the SQL type of this field.
     * @return The SQL type of this field
     */
    public int[] getSQLType() {
        return (int[]) getProperty(SQL_TYPE);
    }

    /**
     * Sets the SQL type of this field.
     * @param sqlType The SQL type of this field.
     */
    public void setSQLType(final int[] sqlType) {
        setProperty(SQL_TYPE, sqlType);
    }

}


