/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

/**
 * Class FieldMapping.
 * 
 * @version $Revision$ $Date$
 */
public class FieldMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _type.
     */
    private java.lang.String _type;

    /**
     * Field _required.
     */
    private boolean _required = false;

    /**
     * keeps track of state for field: _required
     */
    private boolean _has_required;

    /**
     * Field _transient.
     */
    private boolean _transient = false;

    /**
     * keeps track of state for field: _transient
     */
    private boolean _has_transient;

    /**
     * Field _direct.
     */
    private boolean _direct = false;

    /**
     * keeps track of state for field: _direct
     */
    private boolean _has_direct;

    /**
     * Field _lazy.
     */
    private boolean _lazy = false;

    /**
     * keeps track of state for field: _lazy
     */
    private boolean _has_lazy;

    /**
     * Field _nillable.
     */
    private boolean _nillable = false;

    /**
     * keeps track of state for field: _nillable
     */
    private boolean _has_nillable;

    /**
     * Field _container.
     */
    private boolean _container;

    /**
     * keeps track of state for field: _container
     */
    private boolean _has_container;

    /**
     * Field _getMethod.
     */
    private java.lang.String _getMethod;

    /**
     * Field _hasMethod.
     */
    private java.lang.String _hasMethod;

    /**
     * Field _setMethod.
     */
    private java.lang.String _setMethod;

    /**
     * Field _createMethod.
     */
    private java.lang.String _createMethod;

    /**
     * Field _handler.
     */
    private java.lang.String _handler;

    /**
     * Field _collection.
     */
    private org.exolab.castor.mapping.xml.types.FieldMappingCollectionType _collection;

    /**
     * Field _comparator.
     */
    private java.lang.String _comparator;

    /**
     * Field _identity.
     */
    private boolean _identity = false;

    /**
     * keeps track of state for field: _identity
     */
    private boolean _has_identity;

    /**
     * Field _description.
     */
    private java.lang.String _description;

    /**
     * Field _sql.
     */
    private org.exolab.castor.mapping.xml.Sql _sql;

    /**
     * The 'bind-xml' element is used for specifying XML specific
     * databinding
     *  properties and behavior for a specific field. 'bind-xml'
     * may only appear
     *  as a child of a 'field' element.
     *  
     */
    private org.exolab.castor.mapping.xml.BindXml _bindXml;

    /**
     * Field _ldap.
     */
    private org.exolab.castor.mapping.xml.Ldap _ldap;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldMapping() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteContainer(
    ) {
        this._has_container= false;
    }

    /**
     */
    public void deleteDirect(
    ) {
        this._has_direct= false;
    }

    /**
     */
    public void deleteIdentity(
    ) {
        this._has_identity= false;
    }

    /**
     */
    public void deleteLazy(
    ) {
        this._has_lazy= false;
    }

    /**
     */
    public void deleteNillable(
    ) {
        this._has_nillable= false;
    }

    /**
     */
    public void deleteRequired(
    ) {
        this._has_required= false;
    }

    /**
     */
    public void deleteTransient(
    ) {
        this._has_transient= false;
    }

    /**
     * Returns the value of field 'bindXml'. The field 'bindXml'
     * has the following description: The 'bind-xml' element is
     * used for specifying XML specific databinding
     *  properties and behavior for a specific field. 'bind-xml'
     * may only appear
     *  as a child of a 'field' element.
     *  
     * 
     * @return the value of field 'BindXml'.
     */
    public org.exolab.castor.mapping.xml.BindXml getBindXml(
    ) {
        return this._bindXml;
    }

    /**
     * Returns the value of field 'collection'.
     * 
     * @return the value of field 'Collection'.
     */
    public org.exolab.castor.mapping.xml.types.FieldMappingCollectionType getCollection(
    ) {
        return this._collection;
    }

    /**
     * Returns the value of field 'comparator'.
     * 
     * @return the value of field 'Comparator'.
     */
    public java.lang.String getComparator(
    ) {
        return this._comparator;
    }

    /**
     * Returns the value of field 'container'.
     * 
     * @return the value of field 'Container'.
     */
    public boolean getContainer(
    ) {
        return this._container;
    }

    /**
     * Returns the value of field 'createMethod'.
     * 
     * @return the value of field 'CreateMethod'.
     */
    public java.lang.String getCreateMethod(
    ) {
        return this._createMethod;
    }

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'Description'.
     */
    public java.lang.String getDescription(
    ) {
        return this._description;
    }

    /**
     * Returns the value of field 'direct'.
     * 
     * @return the value of field 'Direct'.
     */
    public boolean getDirect(
    ) {
        return this._direct;
    }

    /**
     * Returns the value of field 'getMethod'.
     * 
     * @return the value of field 'GetMethod'.
     */
    public java.lang.String getGetMethod(
    ) {
        return this._getMethod;
    }

    /**
     * Returns the value of field 'handler'.
     * 
     * @return the value of field 'Handler'.
     */
    public java.lang.String getHandler(
    ) {
        return this._handler;
    }

    /**
     * Returns the value of field 'hasMethod'.
     * 
     * @return the value of field 'HasMethod'.
     */
    public java.lang.String getHasMethod(
    ) {
        return this._hasMethod;
    }

    /**
     * Returns the value of field 'identity'.
     * 
     * @return the value of field 'Identity'.
     */
    public boolean getIdentity(
    ) {
        return this._identity;
    }

    /**
     * Returns the value of field 'lazy'.
     * 
     * @return the value of field 'Lazy'.
     */
    public boolean getLazy(
    ) {
        return this._lazy;
    }

    /**
     * Returns the value of field 'ldap'.
     * 
     * @return the value of field 'Ldap'.
     */
    public org.exolab.castor.mapping.xml.Ldap getLdap(
    ) {
        return this._ldap;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'nillable'.
     * 
     * @return the value of field 'Nillable'.
     */
    public boolean getNillable(
    ) {
        return this._nillable;
    }

    /**
     * Returns the value of field 'required'.
     * 
     * @return the value of field 'Required'.
     */
    public boolean getRequired(
    ) {
        return this._required;
    }

    /**
     * Returns the value of field 'setMethod'.
     * 
     * @return the value of field 'SetMethod'.
     */
    public java.lang.String getSetMethod(
    ) {
        return this._setMethod;
    }

    /**
     * Returns the value of field 'sql'.
     * 
     * @return the value of field 'Sql'.
     */
    public org.exolab.castor.mapping.xml.Sql getSql(
    ) {
        return this._sql;
    }

    /**
     * Returns the value of field 'transient'.
     * 
     * @return the value of field 'Transient'.
     */
    public boolean getTransient(
    ) {
        return this._transient;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public java.lang.String getType(
    ) {
        return this._type;
    }

    /**
     * Method hasContainer.
     * 
     * @return true if at least one Container has been added
     */
    public boolean hasContainer(
    ) {
        return this._has_container;
    }

    /**
     * Method hasDirect.
     * 
     * @return true if at least one Direct has been added
     */
    public boolean hasDirect(
    ) {
        return this._has_direct;
    }

    /**
     * Method hasIdentity.
     * 
     * @return true if at least one Identity has been added
     */
    public boolean hasIdentity(
    ) {
        return this._has_identity;
    }

    /**
     * Method hasLazy.
     * 
     * @return true if at least one Lazy has been added
     */
    public boolean hasLazy(
    ) {
        return this._has_lazy;
    }

    /**
     * Method hasNillable.
     * 
     * @return true if at least one Nillable has been added
     */
    public boolean hasNillable(
    ) {
        return this._has_nillable;
    }

    /**
     * Method hasRequired.
     * 
     * @return true if at least one Required has been added
     */
    public boolean hasRequired(
    ) {
        return this._has_required;
    }

    /**
     * Method hasTransient.
     * 
     * @return true if at least one Transient has been added
     */
    public boolean hasTransient(
    ) {
        return this._has_transient;
    }

    /**
     * Returns the value of field 'container'.
     * 
     * @return the value of field 'Container'.
     */
    public boolean isContainer(
    ) {
        return this._container;
    }

    /**
     * Returns the value of field 'direct'.
     * 
     * @return the value of field 'Direct'.
     */
    public boolean isDirect(
    ) {
        return this._direct;
    }

    /**
     * Returns the value of field 'identity'.
     * 
     * @return the value of field 'Identity'.
     */
    public boolean isIdentity(
    ) {
        return this._identity;
    }

    /**
     * Returns the value of field 'lazy'.
     * 
     * @return the value of field 'Lazy'.
     */
    public boolean isLazy(
    ) {
        return this._lazy;
    }

    /**
     * Returns the value of field 'nillable'.
     * 
     * @return the value of field 'Nillable'.
     */
    public boolean isNillable(
    ) {
        return this._nillable;
    }

    /**
     * Returns the value of field 'required'.
     * 
     * @return the value of field 'Required'.
     */
    public boolean isRequired(
    ) {
        return this._required;
    }

    /**
     * Returns the value of field 'transient'.
     * 
     * @return the value of field 'Transient'.
     */
    public boolean isTransient(
    ) {
        return this._transient;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'bindXml'. The field 'bindXml' has
     * the following description: The 'bind-xml' element is used
     * for specifying XML specific databinding
     *  properties and behavior for a specific field. 'bind-xml'
     * may only appear
     *  as a child of a 'field' element.
     *  
     * 
     * @param bindXml the value of field 'bindXml'.
     */
    public void setBindXml(
            final org.exolab.castor.mapping.xml.BindXml bindXml) {
        this._bindXml = bindXml;
    }

    /**
     * Sets the value of field 'collection'.
     * 
     * @param collection the value of field 'collection'.
     */
    public void setCollection(
            final org.exolab.castor.mapping.xml.types.FieldMappingCollectionType collection) {
        this._collection = collection;
    }

    /**
     * Sets the value of field 'comparator'.
     * 
     * @param comparator the value of field 'comparator'.
     */
    public void setComparator(
            final java.lang.String comparator) {
        this._comparator = comparator;
    }

    /**
     * Sets the value of field 'container'.
     * 
     * @param container the value of field 'container'.
     */
    public void setContainer(
            final boolean container) {
        this._container = container;
        this._has_container = true;
    }

    /**
     * Sets the value of field 'createMethod'.
     * 
     * @param createMethod the value of field 'createMethod'.
     */
    public void setCreateMethod(
            final java.lang.String createMethod) {
        this._createMethod = createMethod;
    }

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(
            final java.lang.String description) {
        this._description = description;
    }

    /**
     * Sets the value of field 'direct'.
     * 
     * @param direct the value of field 'direct'.
     */
    public void setDirect(
            final boolean direct) {
        this._direct = direct;
        this._has_direct = true;
    }

    /**
     * Sets the value of field 'getMethod'.
     * 
     * @param getMethod the value of field 'getMethod'.
     */
    public void setGetMethod(
            final java.lang.String getMethod) {
        this._getMethod = getMethod;
    }

    /**
     * Sets the value of field 'handler'.
     * 
     * @param handler the value of field 'handler'.
     */
    public void setHandler(
            final java.lang.String handler) {
        this._handler = handler;
    }

    /**
     * Sets the value of field 'hasMethod'.
     * 
     * @param hasMethod the value of field 'hasMethod'.
     */
    public void setHasMethod(
            final java.lang.String hasMethod) {
        this._hasMethod = hasMethod;
    }

    /**
     * Sets the value of field 'identity'.
     * 
     * @param identity the value of field 'identity'.
     */
    public void setIdentity(
            final boolean identity) {
        this._identity = identity;
        this._has_identity = true;
    }

    /**
     * Sets the value of field 'lazy'.
     * 
     * @param lazy the value of field 'lazy'.
     */
    public void setLazy(
            final boolean lazy) {
        this._lazy = lazy;
        this._has_lazy = true;
    }

    /**
     * Sets the value of field 'ldap'.
     * 
     * @param ldap the value of field 'ldap'.
     */
    public void setLdap(
            final org.exolab.castor.mapping.xml.Ldap ldap) {
        this._ldap = ldap;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'nillable'.
     * 
     * @param nillable the value of field 'nillable'.
     */
    public void setNillable(
            final boolean nillable) {
        this._nillable = nillable;
        this._has_nillable = true;
    }

    /**
     * Sets the value of field 'required'.
     * 
     * @param required the value of field 'required'.
     */
    public void setRequired(
            final boolean required) {
        this._required = required;
        this._has_required = true;
    }

    /**
     * Sets the value of field 'setMethod'.
     * 
     * @param setMethod the value of field 'setMethod'.
     */
    public void setSetMethod(
            final java.lang.String setMethod) {
        this._setMethod = setMethod;
    }

    /**
     * Sets the value of field 'sql'.
     * 
     * @param sql the value of field 'sql'.
     */
    public void setSql(
            final org.exolab.castor.mapping.xml.Sql sql) {
        this._sql = sql;
    }

    /**
     * Sets the value of field 'transient'.
     * 
     * @param _transient
     * @param transient the value of field 'transient'.
     */
    public void setTransient(
            final boolean _transient) {
        this._transient = _transient;
        this._has_transient = true;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final java.lang.String type) {
        this._type = type;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.mapping.xml.FieldMapping
     */
    public static org.exolab.castor.mapping.xml.FieldMapping unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.mapping.xml.FieldMapping) org.exolab.castor.xml.Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.FieldMapping.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
