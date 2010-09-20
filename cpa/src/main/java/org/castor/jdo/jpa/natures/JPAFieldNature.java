/*
 * Copyright 2008 Werner Guttmann
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
package org.castor.jdo.jpa.natures;

import javax.persistence.CascadeType;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.TemporalType;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;
import org.castor.jdo.jpa.info.FieldInfo;

/**
 * A {@link BaseNature} extension, that gives access to information derived from
 * field bound JPA annotations.
 * 
 * @author Peter Schmidt, Martin Kandler
 * @since 1.3
 * @see PropertyHolder
 */
public class JPAFieldNature extends BaseNature {

    /** Property Key for {@link javax.persistence.Basic#fetch()}. */
    private static final String BASIC_FETCH = "BASIC_FETCH";
    /** Property Key for {@link javax.persistence.Basic#optional()}. */
    private static final String BASIC_OPTIONAL = "BASIC_OPTIONAL";

    /** Property Key for {@link javax.persistence.Column#name()}. */
    private static final String COLUMN_NAME = "COLUMN_NAME";
    /** Property Key for {@link javax.persistence.Column#columnDefinition()}. */
    private static final String COLUMN_DEFINITION = "COLUMN_DEFINITION";
    /** Property Key for {@link javax.persistence.Column#length()}. */
    private static final String COLUMN_LENGTH = "COLUMN_LENGTH";
    /** Property Key for {@link javax.persistence.Column#insertable()}. */
    private static final String COLUMN_INSERTABLE = "COLUMN_INSERTABLE";
    /** Property Key for {@link javax.persistence.Column#nullable()}. */
    private static final String COLUMN_NULLABLE = "COLUMN_NULLABLE";
    /** Property Key for {@link javax.persistence.Column#precision()}. */
    private static final String COLUMN_PRECISION = "COLUMN_PRECISION";
    /** Property Key for {@link javax.persistence.Column#scale()}. */
    private static final String COLUMN_SCALE = "COLUMN_SCALE";
    /** Property Key for {@link javax.persistence.Column#table()}. */
    private static final String COLUMN_TABLE = "COLUMN_TABLE";
    /** Property Key for {@link javax.persistence.Column#unique()}. */
    private static final String COLUMN_UNIQUE = "COLUMN_UNIQUE";
    /** Property Key for {@link javax.persistence.Column#updatable()}. */
    private static final String COLUMN_UPDATABLE = "COLUMN_UPDATABLE";

    /** Property Key for {@link javax.persistence.Id}. */
    private static final String ID = "ID";

    /** Property Key for {@link javax.persistence.Transient}. */
    private static final String TRANSIENT = "TRANSIENT";

    /** Property Key for {@link javax.persistence.JoinTable#name()}. */
    private static final String JOINTABLE_NAME = "JOINTABLE_NAME";
    /** Property Key for {@link javax.persistence.JoinTable#catalog()}. */
    private static final String JOINTABLE_CATALOG = "JOINTABLE_CATALOG";
    /** Property Key for {@link javax.persistence.JoinTable#schema()}. */
    private static final String JOINTABLE_SCHEMA = "JOINTABLE_SCHEMA";
    /** Property Key for {@link javax.persistence.JoinTable#joinColumns()}. */
    private static final String JOINTABLE_JOINCOLUMNS = "JOINTABLE_JOINCOLUMNS";
    /** Property Key for {@link javax.persistence.JoinTable#schema(). */
    private static final String JOINTABLE_INVERSE_JOINCOLUMNS = "JOINTABLE_INVERSE_JOINCOLUMNS";

    /** Property Key for {@link javax.persistence.JoinColumn#name()}. */
    private static final String JOINCOLUMN_NAME = "JOINCOLUMN_NAME";
    /**
     * Property Key for
     * {@link javax.persistence.JoinColumn#referencedColumnName()}.
     */
    private static final String JOINCOLUMN_REFERENCEDCOLUMNNAME = "JOINCOLUMN_REFERENCEDCOLUMNNAME";
    /** Property Key for {@link javax.persistence.JoinColumn#unique()}. */
    private static final String JOINCOLUMN_UNIQUE = "JOINCOLUMN_UNIQUE";
    /** Property Key for {@link javax.persistence.JoinColumn#nullable()}. */
    private static final String JOINCOLUMN_NULLABLE = "JOINCOLUMN_NULLABLE";
    /** Property Key for {@link javax.persistence.JoinColumn#insertable()}. */
    private static final String JOINCOLUMN_INSERTABLE = "JOINCOLUMN_INSERTABLE";
    /** Property Key for {@link javax.persistence.JoinColumn#updatable()}. */
    private static final String JOINCOLUMN_UPDATABLE = "JOINCOLUMN_UPDATABLE";
    /**
     * Property Key for {@link javax.persistence.JoinColumn#columnDefinition()}.
     */
    private static final String JOINCOLUMN_COLUMNDEFINITION = "JOINCOLUMN_COLUMNDEFINITION";
    /** Property Key for {@link javax.persistence.JoinColumn#table()}. */
    private static final String JOINCOLUMN_TABLE = "JOINCOLUMN_TABLE";

    /** Property Key for {@link javax.persistence.OneToOne}. */
    private static final String ONETOONE = "ONETOONE";

    /** Property Key for {@link javax.persistence.ManyToOne}. */
    private static final String MANYTOONE = "MANYTOONE";

    /** Property Key for {@link javax.persistence.OneToMany}. */
    private static final String ONETOMANY = "ONETOMANY";

    /** Property Key for {@link javax.persistence.ManyToMany}. */
    private static final String MANYTOMANY = "MANYTOMANY";

    /**
     * Property Key for indicating that JoinTable settings of a many to many
     * field shall be copied (inverse) from the related field.
     */
    private static final String MANYTOMANY_INVERSECOPY = "MANYTOMANY_INVERSECOPY";

    /**
     * Property Key for {@link javax.persistence.OneToOne#targetEntity()},
     * {@link javax.persistence.OneToMany#targetEntity()},
     * {@link javax.persistence.ManyToOne#targetEntity()},
     * {@link javax.persistence.ManyToMany#targetEntity()}, depending on the
     * relation type.
     */
    private static final String RELATION_TARGETENTITY = "RELATION_TARGETENTITY";
    /**
     * Property Key for {@link javax.persistence.OneToOne#fetch()},
     * {@link javax.persistence.OneToMany#fetch()},
     * {@link javax.persistence.ManyToOne#fetch()},
     * {@link javax.persistence.ManyToMany#fetch()}, depending on the relation
     * type.
     */
    private static final String RELATION_LAZYFETCH = "RELATION_FETCHLAZY";

    /**
     * Property Key for {@link javax.persistence.OneToMany#mappedBy()},
     * {@link javax.persistence.ManyToMany#mappedBy()}, depending on the
     * relation type.
     */
    private static final String RELATION_MAPPEDBY = "RELATION_MAPPEDBY";

    /**
     * Property Key for the type of collection used by
     * {@link javax.persistence.OneToMany}, {@link javax.persistence.ManyToMany}
     * , depending on the relation type.
     */
    private static final String RELATION_COLLECTIONTYPE = "RELATION_COLLECTIONTYPE";

    /**
     * Property Key for {@link javax.persistence.OneToOne#optional()},
     * {@link javax.persistence.ManyToOne#optional()} , depending on the
     * relation type.
     */
    private static final String RELATION_OPTIONAL = "RELATION_OPTIONAL";

    /**
     * Property Key for {@link javax.persistence.GeneratedValue#strategy()}
     */
    private static final String GENERATEDVALUE_STRATEGY = "GENERATEDVALUE_STRATEGY";

    /**
     * Property Key for {@link javax.persistence.GeneratedValue#generator()}
     */
    private static final String GENERATEDVALUE_GENERATOR = "GENERATEDVALUE_GENERATOR";
    
    /**
     * Property Key for {@link javax.persistence.Temporal}
     */
    public static final String TEMPORAL_TYPE = "TEMPORAL_TYPE";

    /**
     * Property Key for {@link javax.persistence.Lob}
     */
    public static final String LOB = "LOB";

    /**
     * Property Key for {@link javax.persistence.Enumerated}
     */
    public static final String STRING_ENUM_TYPE = "STRING_ENUM_TYPE";

    /**
     * Property Key for {@link javax.persistence.CascadeType} array of a relation.
     */
    public static final String CASCADE_TYPES = "CASCADE_TYPES";

    /**
     * Instantiate a {@link JPAFieldNature} to access the given
     * {@link PropertyHolder}.
     * 
     * @param holder
     *            The underlying {@link PropertyHolder} (obviously a
     *            {@link org.castor.jdo.jpa.info.FieldInfo FieldInfo}).
     * 
     * @see PropertyHolder
     */
    public JPAFieldNature(final PropertyHolder holder) {
        super(holder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.nature.Nature#getId()
     */
    public final String getId() {
        return getClass().getName();
    }

    /**
     * Get the underlying {@link FieldInfo} (the {@link PropertyHolder}).
     * 
     * @return the underlying {@link FieldInfo}. If the {@link PropertyHolder}
     *         was not of type {@link FieldInfo}, null is returned.
     */
    public FieldInfo getFieldInfo() {
        PropertyHolder holder = super.getHolder();
        if (holder instanceof FieldInfo) {
            return (FieldInfo) holder;
        }
        return null;
    }

    /*
     * @Basic
     */

    /**
     * @see #BASIC_FETCH
     * @param fetch
     *            The value of {@link javax.persistence.Basic#fetch()}
     */
    public final void setBasicFetch(final javax.persistence.FetchType fetch) {
        super.setProperty(BASIC_FETCH, fetch);
    }

    /**
     * @see #BASIC_FETCH
     * @return The value of {@link javax.persistence.Basic#fetch()}
     */
    public final javax.persistence.FetchType getBasicFetch() {
        return (javax.persistence.FetchType) super.getProperty(BASIC_FETCH);
    }

    /**
     * @see #BASIC_OPTIONAL
     * @param optional
     *            The value of {@link javax.persistence.Basic#optional()}
     */
    public final void setBasicOptional(final boolean optional) {
        super.setProperty(BASIC_OPTIONAL, optional);
    }

    /**
     * @see #BASIC_OPTIONAL
     * @return True if {@link javax.persistence.Basic#optional()} was set to
     *         true on the field.
     */
    public final boolean isBasicOptional() {
        Boolean isBasicOptinoal = (Boolean) super.getProperty(BASIC_OPTIONAL);
        if (isBasicOptinoal == null) {
            return false;
        }
        return isBasicOptinoal;
    }

    /**
     * Set the name value of the @Column annotation.
     * 
     * @see #COLUMN_NAME
     * @param name
     *            The value of {@link javax.persistence.Column#name()}
     */
    public final void setColumnName(final String name) {
        super.setProperty(COLUMN_NAME, name);
    }

    /**
     * Returns the name of the @Column annotation.
     * @see #COLUMN_NAME
     * @return The value of {@link javax.persistence.Column#name()}
     */
    public final String getColumnName() {
        return (String) super.getProperty(COLUMN_NAME);
    }

    /**
     * Sets the 'columnDefinition' value of the @Column annotation.
     *  
     * @see #COLUMN_DEFINITION
     * @param columnDefinition
     *            The value of
     *            {@link javax.persistence.Column#columnDefinition()}
     */
    public final void setColumnDefinition(final String columnDefinition) {
        super.setProperty(COLUMN_DEFINITION, columnDefinition);
    }

    /**
     * Returns the 'columnDefinition' value of the @Column annotation.
     *  
     * @see #COLUMN_DEFINITION
     * @return The value of {@link javax.persistence.Column#columnDefinition()}
     */
    public final String getColumnDefinition() {
        return (String) super.getProperty(COLUMN_DEFINITION);
    }

    /**
     * @see #COLUMN_LENGTH
     * @param length
     *            The value of {@link javax.persistence.Column#length()}
     */
    public final void setColumnLength(final int length) {
        super.setProperty(COLUMN_LENGTH, length);
    }

    /**
     * @see #COLUMN_LENGTH
     * @return The value of {@link javax.persistence.Column#length()}
     */
    public final Integer getColumnLength() {
        return (Integer) super.getProperty(COLUMN_LENGTH);
    }

    /**
     * @see #COLUMN_INSERTABLE
     * @param insertable
     *            The value of {@link javax.persistence.Column#insertable()}
     */
    public final void setColumnInsertable(final boolean insertable) {
        super.setProperty(COLUMN_INSERTABLE, insertable);
    }

    /**
     * @see #COLUMN_INSERTABLE
     * @return The value of {@link javax.persistence.Column#insertable()}
     */
    public final Boolean getColumnInsertable() {
        return (Boolean) super.getProperty(COLUMN_INSERTABLE);
    }

    /**
     * @see #COLUMN_NULLABLE
     * @param nullable
     *            The value of {@link javax.persistence.Column#nullable()}
     */
    public final void setColumnNullable(final boolean nullable) {
        super.setProperty(COLUMN_NULLABLE, nullable);
    }

    /**
     * @see #COLUMN_NULLABLE
     * @return The value of {@link javax.persistence.Column#nullable()}
     */
    public final Boolean getColumnNullable() {
        return (Boolean) super.getProperty(COLUMN_NULLABLE);
    }

    /**
     * @see #COLUMN_PRECISION
     * @param precision
     *            The value of {@link javax.persistence.Column#precision()}
     */
    public final void setColumnPrecision(final int precision) {
        super.setProperty(COLUMN_PRECISION, precision);
    }

    /**
     * @see #COLUMN_PRECISION
     * @return The value of {@link javax.persistence.Column#precision()}
     */
    public final Integer getColumnPrecision() {
        return (Integer) super.getProperty(COLUMN_PRECISION);
    }

    /**
     * @see #COLUMN_SCALE
     * @param scale
     *            The value of {@link javax.persistence.Column#scale()}
     */
    public final void setColumnScale(final int scale) {
        super.setProperty(COLUMN_SCALE, scale);
    }

    /**
     * @see #COLUMN_SCALE
     * @return The value of {@link javax.persistence.Column#scale()}
     */
    public final Integer getColumnScale() {
        return (Integer) super.getProperty(COLUMN_SCALE);
    }

    /**
     * @see #COLUMN_TABLE
     * @param table
     *            The value of {@link javax.persistence.Column#table()}
     */
    public final void setColumnTable(final String table) {
        super.setProperty(COLUMN_TABLE, table);
    }

    /**
     * @see #COLUMN_TABLE
     * @return The value of {@link javax.persistence.Column#table()}
     */
    public final String getColumnTable() {
        return (String) super.getProperty(COLUMN_TABLE);
    }

    /**
     * @see #COLUMN_UNIQUE
     * @param unique
     *            The value of {@link javax.persistence.Column#unique()}
     */
    public final void setColumnUnique(final boolean unique) {
        super.setProperty(COLUMN_UNIQUE, unique);
    }

    /**
     * @see #COLUMN_UNIQUE
     * @return The value of {@link javax.persistence.Column#unique()}
     */
    public final Boolean getColumnUnique() {
        return (Boolean) super.getProperty(COLUMN_UNIQUE);
    }

    /**
     * @see #COLUMN_UPDATABLE
     * @param updatable
     *            The value of {@link javax.persistence.Column#updatable()}
     */
    public final void setColumnUpdatable(final boolean updatable) {
        super.setProperty(COLUMN_UPDATABLE, updatable);
    }

    /**
     * @see #COLUMN_UPDATABLE
     * @return The value of {@link javax.persistence.Column#updatable()}
     */
    public final Boolean getColumnUpdatable() {
        return (Boolean) super.getProperty(COLUMN_UPDATABLE);
    }

    /**
     * @see #ID
     * @param isId
     *            If {@link javax.persistence.Id} was found on that property.
     */
    public final void setId(final boolean isId) {
        super.setProperty(ID, isId);
    }

    /**
     * @see #ID
     * @return true if {@link javax.persistence.Id} was set on the property,
     *         else false.
     */
    public final boolean isId() {
        Boolean isId = (Boolean) super.getProperty(ID);
        if (isId == null) {
            return false;
        }
        return isId.booleanValue();
    }

    /**
     * @see #TRANSIENT
     * @param isTransient
     *            if {@link javax.persistence.Transient} was found on that
     *            property.
     */
    public final void setTransient(final boolean isTransient) {
        super.setProperty(TRANSIENT, isTransient);
    }

    /**
     * @see #TRANSIENT
     * @return true if {@link javax.persistence.Transient} was set on the
     *         property, else false
     */
    public final boolean isTransient() {
        Boolean isTransient = (Boolean) super.getProperty(TRANSIENT);
        if (isTransient == null) {
            return false;
        }
        return isTransient.booleanValue();
    }

    /**
     * @see #JOINTABLE_NAME
     * @param tablename
     *            The value of {@link javax.persistence.JoinTable#name()}
     */
    public void setJoinTableName(final String tablename) {
        super.setProperty(JOINTABLE_NAME, tablename);
    }

    /**
     * @see #JOINTABLE_NAME
     * @return The value of {@link javax.persistence.JoinTable#name()}
     */
    public String getJoinTableName() {
        return (String) super.getProperty(JOINTABLE_NAME);
    }

    /**
     * @see #JOINTABLE_CATALOG
     * @param catalog
     *            The value of {@link javax.persistence.JoinTable#catalog()}
     */
    public void setJoinTableCatalog(final String catalog) {
        super.setProperty(JOINTABLE_CATALOG, catalog);
    }

    /**
     * @see #JOINTABLE_CATALOG
     * @return The value of {@link javax.persistence.JoinTable#catalog()}
     */
    public String getJoinTableCatalog() {
        return (String) super.getProperty(JOINTABLE_CATALOG);
    }

    /**
     * @see #JOINTABLE_SCHEMA
     * @param schema
     *            The value of {@link javax.persistence.JoinTable#schema()}
     */
    public void setJoinTableSchema(final String schema) {
        super.setProperty(JOINTABLE_SCHEMA, schema);
    }

    /**
     * @see #TABLE_SCHEMA
     * @return The value of{@link javax.persistence.JoinTable#schema()}
     */
    public String getJoinTableSchema() {
        return (String) super.getProperty(JOINTABLE_SCHEMA);
    }

    /**
     * @see #JOINTABLE_JOINCOLUMNS
     * @param joinColumns
     *            The value of {@link javax.persistence.JoinTable#joinColumns()}
     */
    public void setJoinTableJoinColumns(final JoinColumn[] joinColumns) {
        super.setProperty(JOINTABLE_JOINCOLUMNS, joinColumns);
    }

    /**
     * @see #JOINTABLE_JOINCOLUMNS
     * @return The value of{@link javax.persistence.JoinTable#joinColumns()}
     */
    public JoinColumn[] getJoinTableJoinColumns() {
        return (JoinColumn[]) super.getProperty(JOINTABLE_JOINCOLUMNS);
    }

    /**
     * @see #JOINTABLE_INVERSE_JOINCOLUMNS
     * @param inverseJoinColumns
     *            The value of
     *            {@link javax.persistence.JoinTable#inverseJoinColumns()}
     */
    public void setJoinTableInverseJoinColumns(
            final JoinColumn[] inverseJoinColumns) {
        super.setProperty(JOINTABLE_INVERSE_JOINCOLUMNS, inverseJoinColumns);
    }

    /**
     * @see #JOINTABLE_INVERSE_JOINCOLUMNS
     * @return The value of
     *         {@link javax.persistence.JoinTable#inverseJoinColumns()}
     */
    public JoinColumn[] getJoinTableInverseJoinColumns() {
        return (JoinColumn[]) super.getProperty(JOINTABLE_INVERSE_JOINCOLUMNS);
    }

    /**
     * @see #JOINCOLUMN_NAME
     * @param name
     *            The value of {@link javax.persistence.JoinColumn#name()}.
     */
    public final void setJoinColumnName(final String name) {
        super.setProperty(JOINCOLUMN_NAME, name);
    }

    /**
     * @see #JOINCOLUMN_NAME
     * @return The value of {@link javax.persistence.JoinColumn#name()}.
     */
    public final String getJoinColumnName() {
        return (String) super.getProperty(JOINCOLUMN_NAME);
    }

    /**
     * @see #JOINCOLUMN_REFERENCEDCOLUMNNAME
     * @param referencedColumnName
     *            The value of
     *            {@link javax.persistence.JoinColumn#referencedColumnName()}.
     */
    public final void setJoinColumnReferencedColumnName(
            final String referencedColumnName) {
        super
                .setProperty(JOINCOLUMN_REFERENCEDCOLUMNNAME,
                        referencedColumnName);
    }

    /**
     * @see #JOINCOLUMN_REFERENCEDCOLUMNNAME
     * @return The value of
     *         {@link javax.persistence.JoinColumn#referencedColumnName()}.
     */
    public final String getJoinColumnReferencedColumnName() {
        return (String) super.getProperty(JOINCOLUMN_REFERENCEDCOLUMNNAME);
    }

    /**
     * @see #JOINCOLUMN_UNIQUE
     * @param unique
     *            The value of {@link javax.persistence.JoinColumn#unique()}.
     */
    public final void setJoinColumnUnique(final boolean unique) {
        super.setProperty(JOINCOLUMN_UNIQUE, unique);
    }

    /**
     * @see #JOINCOLUMN_UNIQUE
     * @return The value of {@link javax.persistence.JoinColumn#unique()}.
     */
    public final Boolean getJoinColumnUnique() {
        return (Boolean) super.getProperty(JOINCOLUMN_UNIQUE);
    }

    /**
     * @see #JOINCOLUMN_NULLABLE
     * @param nullable
     *            The value of {@link javax.persistence.JoinColumn#nullable()}.
     */
    public final void setJoinColumnNullable(final boolean nullable) {
        super.setProperty(JOINCOLUMN_NULLABLE, nullable);
    }

    /**
     * @see #JOINCOLUMN_NULLABLE
     * @return The value of {@link javax.persistence.JoinColumn#nullable()}.
     */
    public final Boolean getJoinColumnNullable() {
        return (Boolean) super.getProperty(JOINCOLUMN_NULLABLE);
    }

    /**
     * @see #JOINCOLUMN_INSERTABLE
     * @param insertable
     *            The value of {@link javax.persistence.JoinColumn#insertable()}
     *            .
     */
    public final void setJoinColumnInsertable(final boolean insertable) {
        super.setProperty(JOINCOLUMN_INSERTABLE, insertable);
    }

    /**
     * @see #JOINCOLUMN_INSERTABLE
     * @return The value of {@link javax.persistence.JoinColumn#insertable()}.
     */
    public final Boolean getJoinColumnInsertable() {
        return (Boolean) super.getProperty(JOINCOLUMN_INSERTABLE);
    }

    /**
     * @see #JOINCOLUMN_UPDATABLE
     * @param updatable
     *            The value of {@link javax.persistence.JoinColumn#updatable()}.
     */
    public final void setJoinColumnUpdatable(final boolean updatable) {
        super.setProperty(JOINCOLUMN_UPDATABLE, updatable);
    }

    /**
     * @see #JOINCOLUMN_UPDATABLE
     * @return The value of {@link javax.persistence.JoinColumn#updatable()}.
     */
    public final Boolean getJoinColumnUpdatable() {
        return (Boolean) super.getProperty(JOINCOLUMN_UPDATABLE);
    }

    /**
     * @see #JOINCOLUMN_COLUMNDEFINITION
     * @param columnDefinition
     *            The value of
     *            {@link javax.persistence.JoinColumn#columnDefinition()}.
     */
    public final void setJoinColumnColumnDefinition(
            final String columnDefinition) {
        super.setProperty(JOINCOLUMN_COLUMNDEFINITION, columnDefinition);
    }

    /**
     * @see #JOINCOLUMN_COLUMNDEFINITION
     * @return The value of
     *         {@link javax.persistence.JoinColumn#columnDefinition()}.
     */
    public final String getJoinColumnColumnDefinition() {
        return (String) super.getProperty(JOINCOLUMN_COLUMNDEFINITION);
    }

    /**
     * @see #JOINCOLUMN_TABLE
     * @param table
     *            The value of {@link javax.persistence.JoinColumn#table()}.
     */
    public final void setJoinColumnTable(final String table) {
        super.setProperty(JOINCOLUMN_TABLE, table);
    }

    /**
     * @see #JOINCOLUMN_TABLE
     * @return The value of {@link javax.persistence.JoinColumn#table()}.
     */
    public final String getJoinColumnTable() {
        return (String) super.getProperty(JOINCOLUMN_TABLE);
    }

    /**
     * @see #ONETOONE
     * @param hasOneToOne
     *            if {@link javax.persistence.OneToOne} was found on that
     *            property.
     */
    public void setOneToOne(final boolean hasOneToOne) {
        super.setProperty(ONETOONE, hasOneToOne);
    }

    /**
     * @see #ONETOONE
     * @return if {@link javax.persistence.OneToOne} was set on the property,
     *         else false.
     */
    public boolean isOneToOne() {
        Boolean isOneToOne = (Boolean) super.getProperty(ONETOONE);
        if (isOneToOne == null) {
            return false;
        }
        return isOneToOne;
    }

    /**
     * @see #MANYTOONE
     * @param hasManyToOne
     *            if {@link javax.persistence.ManyToOne} was found on that
     *            property.
     */
    public void setManyToOne(final boolean hasManyToOne) {
        super.setProperty(MANYTOONE, hasManyToOne);
    }

    /**
     * @see #MANYTOONE
     * @return if {@link javax.persistence.ManyToOne} was set on the property,
     *         else false.
     */
    public boolean isManyToOne() {
        Boolean isManyToOne = (Boolean) super.getProperty(MANYTOONE);
        if (isManyToOne == null) {
            return false;
        }
        return isManyToOne;
    }

    /**
     * @see #ONETOMANY
     * @param hasOneToMany
     *            if {@link javax.persistence.OneToMany} was found on that
     *            property.
     */
    public void setOneToMany(final boolean hasOneToMany) {
        super.setProperty(ONETOMANY, hasOneToMany);
    }

    /**
     * @see #ONETOMANY
     * @return if {@link javax.persistence.OneToMany} was set on the property,
     *         else false.
     */
    public boolean isOneToMany() {
        Boolean isOneToMany = (Boolean) super.getProperty(ONETOMANY);
        if (isOneToMany == null) {
            return false;
        }
        return isOneToMany;
    }

    /**
     * @see #MANYTOMANY
     * @param hasManyToMany
     *            if {@link javax.persistence.ManyToMany} was found on that
     *            property.
     */
    public void setManyToMany(final boolean hasManyToMany) {
        super.setProperty(MANYTOMANY, hasManyToMany);
    }

    /**
     * @see #MANYTOMANY
     * @return if {@link javax.persistence.ManyToOne} was set on the property,
     *         else false.
     */
    public boolean isManyToMany() {
        Boolean isManyToMany = (Boolean) super.getProperty(MANYTOMANY);
        if (isManyToMany == null) {
            return false;
        }
        return isManyToMany;
    }

    /**
     * @see #MANYTOMANY_INVERSECOPY
     * @param copySettingsInverse
     *            set this to true if a ManyToMany related field shall copy all
     *            {@link JoinTable} relevant information from the other side of
     *            the relation (inverting the {@link JoinColumn} settings).
     */
    public void setManyToManyInverseCopy(final boolean copySettingsInverse) {
        super.setProperty(MANYTOMANY_INVERSECOPY, copySettingsInverse);
    }

    /**
     * @see #MANYTOMANY_INVERSECOPY
     * @return if this is true a ManyToMany related field shall copy all
     *         {@link JoinTable} relevant information from the other side of the
     *         relation (inverting the {@link JoinColumn} settings).
     */
    public boolean isManyToManyInverseCopy() {
        Boolean isInverseCopy = (Boolean) super
                .getProperty(MANYTOMANY_INVERSECOPY);
        if (isInverseCopy == null) {
            return false;
        }
        return isInverseCopy;
    }

    /**
     * @see #RELATION_TARGETENTITY
     * @param targetEntity
     *            The value of {@link javax.persistence.OneToOne#targetEntity()}
     *            , {@link javax.persistence.OneToMany#targetEntity()},
     *            {@link javax.persistence.ManyToOne#targetEntity()},
     *            {@link javax.persistence.ManyToMany#targetEntity()}, depending
     *            to the relation type.
     */
    public void setRelationTargetEntity(final Class<?> targetEntity) {
        super.setProperty(RELATION_TARGETENTITY, targetEntity);
    }

    /**
     * @see #RELATION_TARGETENTITY
     * @return The value of {@link javax.persistence.OneToOne#targetEntity()},
     *         {@link javax.persistence.OneToMany#targetEntity()},
     *         {@link javax.persistence.ManyToOne#targetEntity()},
     *         {@link javax.persistence.ManyToMany#targetEntity()}, depending on
     *         the relation type. Returns null iff no relational annotation was
     *         set on the field.
     */
    public Class<?> getRelationTargetEntity() {
        return (Class<?>) super.getProperty(RELATION_TARGETENTITY);
    }

    /**
     * @see #RELATION_LAZYFETCH
     * @param lazyFetch
     *            true if {@link javax.persistence.OneToOne#fetch()} ,
     *            {@link javax.persistence.OneToMany#fetch()},
     *            {@link javax.persistence.ManyToOne#fetch()},
     *            {@link javax.persistence.ManyToMany#fetch()} is set to
     *            {@link javax.persistence.FetchType#LAZY}, depending on the
     *            relation type.
     */
    public void setRelationLazyFetch(final boolean lazyFetch) {
        super.setProperty(RELATION_LAZYFETCH, lazyFetch);
    }

    /**
     * @see #RELATION_LAZYFETCH
     * @return true if {@link javax.persistence.OneToOne#fetch()} ,
     *         {@link javax.persistence.OneToMany#fetch()},
     *         {@link javax.persistence.ManyToOne#fetch()},
     *         {@link javax.persistence.ManyToMany#fetch()} is set to
     *         {@link javax.persistence.FetchType#LAZY}, depending on the
     *         relation type.
     */
    public boolean isRelationLazyFetch() {
        Boolean lazyFetch = (Boolean) super.getProperty(RELATION_LAZYFETCH);
        if (lazyFetch == null) {
            return false;
        }
        return lazyFetch;
    }

    /**
     * @see #RELATION_MAPPEDBY
     * @param mappedBy
     *            The content of {@link javax.persistence.OneToMany#mappedBy()},
     *            {@link javax.persistence.ManyToMany#mappedBy()}, depending on
     *            the relation type.
     */
    public void setRelationMappedBy(final String mappedBy) {
        super.setProperty(RELATION_MAPPEDBY, mappedBy);
    }

    /**
     * @see #RELATION_MAPPEDBY
     * @return The content of {@link javax.persistence.OneToMany#mappedBy()},
     *         {@link javax.persistence.ManyToMany#mappedBy()}, depending on the
     *         relation type. If no (or an empty) String was set, null is
     *         returned!
     */
    public String getRelationMappedBy() {
        String mappedBy = (String) super.getProperty(RELATION_MAPPEDBY);
        if ((mappedBy == null) || (mappedBy.trim().length() == 0)) {
            return null;
        }
        return mappedBy;
    }

    /**
     * @see #RELATION_COLLECTIONTYPE
     * @return The type of Collection being used by
     *         {@link javax.persistence.OneToMany},
     *         {@link javax.persistence.ManyToMany}, depending on the relation
     *         type. Returns null iff none of the above relational annotations
     *         was set on the field.
     */
    public Class<?> getRelationCollectionType() {
        return (Class<?>) super.getProperty(RELATION_COLLECTIONTYPE);
    }

    /**
     * @see #RELATION_COLLECTIONTYPE
     * @param collectionType
     *            Set the type of Collection being used by
     *            {@link javax.persistence.OneToMany},
     *            {@link javax.persistence.ManyToMany}, depending on the
     *            relation type.
     */
    public void setRelationCollectionType(final Class<?> collectionType) {
        super.setProperty(RELATION_COLLECTIONTYPE, collectionType);
    }

    /**
     * @see #RELATION_OPTIONAL
     * @param optional
     *            Set the value of {@link javax.persistence.OneToOne#optional()}
     *            , {@link javax.persistence.ManyToOne#optional()}, depending on
     *            the relation type.
     */
    public void setRelationOptional(final boolean optional) {
        super.setProperty(RELATION_OPTIONAL, optional);
    }

    /**
     * @see #RELATION_OPTIONAL
     * @return true if {@link javax.persistence.OneToOne#optional()},
     *         {@link javax.persistence.ManyToOne#optional()} was set to true,
     *         depending on the relation type.
     */
    public boolean isRelationOptional() {
        Boolean isOptional = (Boolean) super.getProperty(RELATION_OPTIONAL);
        if (isOptional == null) {
            return false;
        }
        return isOptional;
    }

    /**
     * Set the {@link javax.persistence.GeneratedValue#strategy()}
     * @see #GENERATEDVALUE_STRATEGY
     * @param strategy
     */
    public void setGeneratedValueStrategy(GenerationType strategy) {
    	super.setProperty(GENERATEDVALUE_STRATEGY, strategy);
    }
    
    /**
     * Get the {@link javax.persistence.GeneratedValue#strategy()}
     * @see #GENERATEDVALUE_STRATEGY
     * @return strategy
     */
    public GenerationType getGeneratedValueStrategy() {
    	return (GenerationType) super.getProperty(GENERATEDVALUE_STRATEGY);
    }
    
    /**
     * Set the {@link javax.persistence.GeneratedValue#generator()}
     * @see #GENERATEDVALUE_STRATEGY
     * @param strategy
     */
    public void setGeneratedValueGenerator(String generator) {
    	super.setProperty(GENERATEDVALUE_GENERATOR, generator);
    }
    
    /**
     * Get the {@link javax.persistence.GeneratedValue#generator()}
     * @see #GENERATEDVALUE_STRATEGY
     * @return strategy
     */
    public String getGeneratedValueGenerator() {
    	return (String) super.getProperty(GENERATEDVALUE_GENERATOR);
    }
    /**
     * @see #TEMPORAL_TYPE
     * @return The {@link javax.persistence.TemporalType} of field.
     */
    public TemporalType getTemporalType() {
        return (TemporalType) super.getProperty(TEMPORAL_TYPE);
    }

    /**
     * @see #TEMPORAL_TYPE
     * @param temporalType
     *            set the {@link javax.persistence.TemporalType} of field.
     */
    public void setTemporalType(final TemporalType temporalType) {
        super.setProperty(TEMPORAL_TYPE, temporalType);
    }

    /**
     * @see #LOB
     * @return true if {@link javax.persistence.Lob} was set on the
     *         property, else false
     */
    public boolean isLob() {
        Boolean isLob = (Boolean) super.getProperty(LOB);
        return isLob != null && isLob;
    }

    /**
     * @see #LOB
     * @param isLob
     *            if {@link javax.persistence.Lob} was found on that
     *            property.
     */
    public void setLob(final boolean isLob) {
        super.setProperty(LOB, isLob);
    }

    /**
     * @see #STRING_ENUM_TYPE
     * @return true if {@link javax.persistence.Enumerated} with value
     *         {@link javax.persistence.EnumType} STRING was set on the
     *         property, else false
     */
    public boolean isStringEnumType() {
        Boolean isStringEnumType = (Boolean) super.getProperty(STRING_ENUM_TYPE);
        return isStringEnumType != null && isStringEnumType;
    }

    /**
     * @see #STRING_ENUM_TYPE
     * @param isStringEnumType
     *            if {@link javax.persistence.Enumerated} with value
     *            {@link javax.persistence.EnumType} STRING was found on that
     *            property.
     */
    public void setStringEnumType(final boolean isStringEnumType) {
        super.setProperty(STRING_ENUM_TYPE, isStringEnumType);
    }

    /**
     * @see #CASCADE_TYPES
     * @return The {@link javax.persistence.CascadeType} array of relation.
     */
    public CascadeType[] getCascadeTypes() {
        return (CascadeType[]) super.getProperty(CASCADE_TYPES);
 }

    /**
     * @see #CASCADE_TYPES
     * @param cascadeTypes
     *            set the {@link javax.persistence.CascadeType} array of relation.
     */
    public void setCascadeTypes(final CascadeType[] cascadeTypes) {
        super.setProperty(CASCADE_TYPES, cascadeTypes);
    }

}
