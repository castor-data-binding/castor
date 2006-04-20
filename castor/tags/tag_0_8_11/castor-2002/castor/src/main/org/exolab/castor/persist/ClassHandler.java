/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist;


import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Vector;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import org.exolab.castor.jdo.engine.JDOCallback;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.util.Messages;


/**
 * The class handler is an efficient mechanism for dealing with objects
 * and used exclusively by the {@link CacheEngine}. Each class is
 * represented as a set of fields and relations, including those of the
 * parent class. The handler provides methods for copying objects into
 * and from the cached copy, checking validity and modification, and
 * getting/setting the object's identity.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see ClassDescriptor
 */
public final class ClassHandler
{


    /**
     * Indicates the object has not been modified in memory since it
     * was loaded and need not be stored.
     */
    public static final short Unmodified = 0;


    /**
     * Indicates the object has been modified in memory since it was
     * loaded, but all modified fields are marked as ignore dirty ignore,
     * the object should be stored but not write lock is required.
     */
    public static final short Modified = 1;


    /**
     * Indicates the object has been modified in memory since it was
     * loaded, at least one of the modified fields is marked as dirty
     * check, the object must be write locked before it can be stored.
     */
    public static final short LockRequired = 2;


    /**
     * The class descriptor.
     */
    private final ClassDescriptor  _clsDesc;


    /**
     * The handler of the parent class.
     */
    private ClassHandler      _extends;


    /**
     * Information about all the fields in this class.
     */
    private FieldInfo[]       _fields;


    /**
     * Relation handlers for all the fields in this class.
     */
    private RelationHandler[] _relations;


    /**
     * The identity field.
     */
    private FieldInfo         _identity;


    /**
     * The field handler for obtaining the identity through a relation.
     */
    private FieldHandler      _relIdentity;


    /**
     * The call back interceptor for this class.
     */
    private CallbackInterceptor  _callback;



    /**
     * Constructs a new handler. The handler cannot be used until it
     * is normalized.
     */
    ClassHandler( ClassDescriptor clsDesc )
    {
        _clsDesc = clsDesc;
        if ( Persistent.class.isAssignableFrom( _clsDesc.getJavaClass() ) )
            _callback = new JDOCallback();
    }


    /**
     * Used by {@link #CacheEngine} to normalize the handler. Since
     * an handler reference each other including circular references,
     * an handler is created, registered with the cache engine and
     * then normalized. In the process of normalization the other
     * relations are processed.
     */
    void normalize( CacheEngine cache )
        throws MappingException
    {
        Vector            rels;
        Vector            fields;

        if ( _clsDesc.getExtends() != null ) {
            _extends = new ClassHandler( _clsDesc.getExtends() );
            _extends.normalize( cache );
        }

        if ( _clsDesc.getIdentity() != null )
            _identity = new FieldInfo( _clsDesc.getIdentity(), null );
        rels = new Vector();
        fields = new Vector();
        addFields( cache, _clsDesc, fields, rels );

        _fields = new FieldInfo[ fields.size() ];
        fields.copyInto( _fields );
        _relations = new RelationHandler[ rels.size() ];
        rels.copyInto( _relations );
    }


    /**
     * Used by <tt>normalize</tt> to add fields to this handler.
     * Recursive function that flattens the field of this class
     * and the parent class into one set.
     */
    private void addFields( CacheEngine cache, ClassDescriptor clsDesc,
                Vector fields, Vector rels )
        throws MappingException
    {
        FieldDescriptor[] descs;

        if ( clsDesc.getExtends() != null ) {
            // [Bill Reynolds] Make sure ClassHandler has been installed for extends.
            cache.addClassHandler( clsDesc.getExtends().getJavaClass() );
            addFields( cache, clsDesc.getExtends(), fields, rels );
        }

        descs = clsDesc.getFields();
        for ( int i = 0 ; i < descs.length ; ++i ) {
            ClassHandler clsHandler;

            clsHandler = cache.addClassHandler( descs[ i ].getFieldType() );
            if ( clsHandler == null ) {
                fields.addElement( new FieldInfo( descs[ i ], null ) );
                rels.addElement( null );
            } else {
                FieldDescriptor[] relFields;
                RelationHandler   relHandler;
                boolean           attached = false;

                relFields = clsHandler._clsDesc.getFields();
                for ( int j = 0 ; j < relFields.length ; ++j )
                    if ( relFields[ j ].getFieldType() == clsDesc.getJavaClass() ) {
                        attached = true;
                        break;
                    }
                relHandler = new RelationHandler( descs[ i ], clsHandler, attached );
                fields.addElement( new FieldInfo( descs[ i ], relHandler ) );
                rels.addElement( relHandler );
            }
        }
    }


    /**
     * Returns the Java class.
     */
    public Class getJavaClass()
    {
        return _clsDesc.getJavaClass();
    }


    /**
     * Returns the callback interceptor for this class.
     */
    public CallbackInterceptor getCallback()
    {
        return _callback;
    }


    /**
     * Constructs a new object of this class. Does not generate any
     * exceptions, since object creation has been proven to work when
     * creating descriptor from mapping.
     *
     * @return A new instance of this class
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this descriptor, or the descriptor
     *  is not compatiable with the Java object
     */
    public Object newInstance()
        throws IllegalStateException
    {
        return Types.newInstance( _clsDesc.getJavaClass() );
    }


    /**
     * Returns the identity of the object.
     *
     * @param object The object
     * @return The identity
     */
    public Object getIdentity( Object object )
    {
        if ( _relIdentity == null )
            return _identity.handler.getValue( object );
        else {
            // Get the related object and it's identity;
            object = _identity.handler.getValue( object );
            return _relIdentity.getValue( object );
        }
    }


    /**
     * Sets the identity of the object.
     *
     * @param object The object
     * @param identity The identity
     */
    void setIdentity( Object object, Object identity )
    {
        _identity.handler.setValue( object, identity );
    }


    /**
     * Returns the descriptor of this class.
     */
    ClassDescriptor getDescriptor()
    {
        return _clsDesc;
    }


    /**
     * Returns the class handler which this handler extends.
     */
    ClassHandler getExtends()
    {
        return _extends;
    }


    /**
     * Returns an array of all the fields in this class.
     *
     * @return Array of zero or more field handlers
     */
    FieldHandler[] getFields()
    {
        FieldHandler[] fields;

        fields = new FieldHandler[ _fields.length ];
        for ( int i = 0 ; i < _fields.length ; ++i )
            fields[ i ] = _fields[ i ].handler;
        return fields;
    }


    /**
     * Returns an array of all the relations in this class.
     * This array is the same size as the array returned
     * from {@link #getFields} and has the order of fields.
     * Fields that do not represent relations are simply null.
     *
     * @return Array of zero or more relation handlers
     *  including nulls for non-relation fields
     */
    RelationHandler[] getRelations()
    {
        return _relations;
    }


    /**
     * Create a new field set for holding a cached copy.
     * Returns an array capable of holding all the fields
     * in the object.
     *
     * @return A new field set
     */
    Object[] newFieldSet()
    {
    return new Object[ _fields.length ];
    }


    /**
     * Returns the suitable access mode. If <tt>txMode</tt> is null,
     * return the access mode defined for the object. Otherwise, the
     * following rules apply (in that order):
     * <ul>
     * <li>If the class is defined as read/only the access mode is
     *  read/only
     * <li>If the transaction is defined as read/only the access mode
     *  is read/only
     * <li>If the class is defined as locked the access mode is
     *  exclusive
     * <li>If the transaction is defined as locked the access mode
     *  is exclusive
     * <li>If the class is defined as exclusive the access mode is
     *  exclusive
     * <li>If the transaction is defined as exclusive the access mode
     *  is exclusive
     * <li>The transaction mode is used
     * </ul>
     *
     * @param txMode The transaction mode, or null
     * @return The suitable access mode
     */
    AccessMode getAccessMode( AccessMode txMode )
    {
        AccessMode clsMode;

        if ( txMode == null )
            return _clsDesc.getAccessMode();
        clsMode = _clsDesc.getAccessMode();
        if ( clsMode == AccessMode.ReadOnly || txMode == AccessMode.ReadOnly )
            return AccessMode.ReadOnly;
        if ( clsMode == AccessMode.DbLocked || txMode == AccessMode.DbLocked )
            return AccessMode.DbLocked;
        if ( clsMode == AccessMode.Exclusive || txMode == AccessMode.Exclusive )
            return AccessMode.Exclusive;
        return txMode;
    }


    /**
     * Copy values from the source object to the target object. Will
     * copy all the fields, and related objects that are specified in
     * the object mapping.
     * <p>
     * The fetch context determines how related fields are copied.
     * When copying into the cache, no fetch context is necessary
     * and the cache will only contain a copy of the relation using
     * the identity field. When copying from the cache, the fetch
     * context is required and will be used to obtain an object
     * matching the cached identity field.
     *
     * @param source The source object
     * @param target The target object
     * @param ctx The fetch context, or null
     * @throws PersistenceException An error fetching the related object
     */
    void copyInto( Object[] fields, Object target, FetchContext ctx )
        throws PersistenceException
    {
        for ( int i = 0 ; i < _fields.length ; ++i ) {
            if ( _fields[ i ].relation == null )
                _fields[ i ].handler.setValue( target, copyValue( _fields[ i ], fields[ i ] ) );
            else if ( ctx != null ) {
                Object relSource;

                if ( fields[ i ] == null )
                    _fields[ i ].relation.setRelated( target, null );
                else if ( _fields[ i ].multi ) {
                    Vector vector;

                    vector = (Vector) fields[ i ];
                    for ( int j = 0 ; j < vector.size() ; ++j ) {
                        Object object;

                        object = ctx.load( _fields[ i ].relation.getRelatedHandler(), vector.elementAt( j ) );
                        if ( object == null )
                            throw new ObjectNotFoundExceptionImpl( _fields[ i ].relation.getRelatedClass(),
                                                                   vector.elementAt( j ) );
                         _fields[ i ].relation.setRelated( target, object );
                    }
                } else {
                    Object relTarget;

                    //[oleg] ObjectNotFoundException here 1) leads to incorrect behavior;
                    // 2) doesn't give the possibility to load the object which can be result of
                    // legal Castor operation of removing of the related object
                    try {
                        relTarget = ctx.load( _fields[ i ].relation.getRelatedHandler(), fields[ i ] );
                    } catch (ObjectNotFoundException ex) {
                        relTarget = null;
                    }
                    if ( relTarget == null )
                        fields[ i ] = null;
                    //    throw new ObjectNotFoundExceptionImpl( _fields[ i ].relation.getRelatedClass(),
                    //                                           fields[ i ] );
                    _fields[ i ].relation.setRelated( target, relTarget );
                }
            }
        }
    }


    /**
     * Copies the contents of the object into the field array. The
     * fields are copied in their mapping order, with relations
     * represented by their identity value and collections using a
     * <tt>Vector</tt>.
     *
     * @param source The source object
     * @param field The target set of fields
     */
    void copyInto( Object source, Object[] fields )
    {
        for ( int i = 0 ; i < _fields.length ; ++i ) {
            if ( _fields[ i ].relation == null )
                fields[ i ] = copyValue( _fields[ i ], _fields[ i ].handler.getValue( source ) );
            else if ( _fields[ i ].multi ) {
                Vector      vector;
                Enumeration enum;

                vector = new Vector();
                enum = (Enumeration) _fields[ i ].relation.getRelated( source );
                if ( enum != null ) {
                    Object related;

                    while ( enum.hasMoreElements() ) {
                        related = enum.nextElement();
                        if ( related != null )
                            vector.addElement( copyValue( _fields[ i ].relation.getRelatedHandler()._identity,
                                                          _fields[ i ].relation.getIdentity( related ) ) );
                    }
                }
                fields[ i ] = vector;
            } else {
                Object related;

                related = _fields[ i ].relation.getRelated( source );
                if ( related == null )
                    fields[ i ] = null;
                else
                    fields[ i ] = copyValue( _fields[ i ].relation.getRelatedHandler()._identity,
                                             _fields[ i ].relation.getIdentity( related ) );
            }
        }
    }


    /**
     * Copies the contents of the object into another object. 
     * Used by LongTransactionSupport.
     *
     * @param source The source object
     * @param target The target object
     */
    public void copyObject(Object source, Object target) 
        throws PersistenceException
    {
        if ( source == target ) 
            return;
        for ( int i = 0 ; i < _fields.length ; ++i ) {
            if ( _fields[ i ].relation == null )
                _fields[ i ].handler.setValue( target, _fields[ i ].handler.getValue( source ) );
            else if ( _fields[ i ].multi ) {
                Vector       vector;
                Enumeration  enum;
                ClassHandler relHandler;
                Object[]     idents;
                Object       related;
                Object       origRelated;
                Object       relIdentity;
                int          foundIndex;

                vector = new Vector();
                enum = (Enumeration) _fields[ i ].relation.getRelated( target );
                if ( enum != null ) 
                    while ( enum.hasMoreElements() ) 
                        vector.addElement( enum.nextElement() );
                _fields[ i ].handler.resetValue( target );
                enum = (Enumeration) _fields[ i ].relation.getRelated( source );
                if ( enum == null ) 
                    continue;        
                relHandler = _fields[ i ].relation.getRelatedHandler();
                idents = new Object[ vector.size() ];
                for ( int j = 0; j < idents.length; j++ ) {
                    idents[ j ] = relHandler.getIdentity( vector.elementAt( j ) );
                }
                while ( enum.hasMoreElements() ) {
                    related = enum.nextElement();
                    if ( related == null ) 
                        continue;        

                    // search for backward references equal to the source 
                    // and set it to target 
                    for ( int j = 0; j < relHandler._relations.length; j++ ) {
                        if ( relHandler._relations[ j ] == null ) 
                            continue;
                        if ( source == relHandler._relations[ j ].getRelated( related ) )
                            relHandler._relations[ j ].setRelated( related, target );
                    }

                    relIdentity = relHandler.getIdentity( related );
                    if ( relIdentity == null ) 
                        continue;        
                    foundIndex = -1;
                    for ( int j = 0; j < idents.length; j++ ) {
                        if ( relIdentity.equals( idents[ j ] ) ) {
                            foundIndex = j;
                            break;
                        }
                    }
                    if ( foundIndex < 0 ) 
                        _fields[ i ].relation.setRelated( target, related );
                    else {
                        origRelated = vector.elementAt( foundIndex );
                        if ( origRelated != related ) 
                            relHandler.copyObject( related, origRelated );
                        _fields[ i ].relation.setRelated( target, origRelated );
                    }
                }
            } else if ( !_fields[ i ].relation.isAttached() ) {
                ClassHandler relHandler;
                Object       related;
                Object       origRelated;
                Object       relIdentity;

                relHandler = _fields[ i ].relation.getRelatedHandler();
                related = _fields[ i ].relation.getRelated( source );
                relIdentity = null;
                if ( related != null )
                    relIdentity = relHandler.getIdentity( related );
                if ( relIdentity == null )
                    _fields[ i ].relation.setRelated( target, null );
                else {
                    origRelated = _fields[ i ].relation.getRelated( target );
                    if ( origRelated == null) 
                        _fields[ i ].relation.setRelated( target, related );
                    else if ( origRelated != related ) 
                        relHandler.copyObject( related, origRelated );
                }
            }
        }
    }

    /**
     * Searches for the object with the same identity as the passed copy has
     * among objects participating in the given transaction and fills it 
     * by values from the copy
     * Used by LongTransactionSupport.
     *
     * @param Object source The copy
     * @param TransactionContext tx The transaction
     * @param PersistenceEngine engine The persistence engine
     * @return The filled object participating in the transaction or the same 
     *         object if not found.
     */
    public Object fillFromCopy( Object source, TransactionContext tx, 
                                PersistenceEngine engine )
        throws PersistenceException
    {
        TransactionContext.ObjectEntry entry;
        Object target;

        entry = tx.getObjectEntry( engine, new OID( this, getIdentity( source ) ) );
        if ( entry == null )
            target = source;  // The record will be created
        else {
            target = entry.object;
            if ( target == source ) // Already processed
                return source;
        }
        for ( int i = 0 ; i < _fields.length ; i++ ) {
            if ( _fields[ i ].relation == null ) 
                _fields[ i ].handler.setValue( target, _fields[ i ].handler.getValue( source ) );
            else if ( _fields[ i ].multi ) {
                Vector       vector;
                Enumeration  enum;
                ClassHandler relHandler;
                Object       related;

                vector = new Vector();
                enum = (Enumeration) _fields[ i ].relation.getRelated( source );
                if ( enum != null ) 
                    while ( enum.hasMoreElements() ) 
                        vector.addElement( enum.nextElement() );
                relHandler = _fields[ i ].relation.getRelatedHandler();
                _fields[ i ].handler.resetValue( target );
                enum = vector.elements();
                while ( enum.hasMoreElements() ) {
                    related = enum.nextElement();
                    if ( related != null ) 
                        _fields[ i ].relation.setRelated( target, 
                                relHandler.fillFromCopy( related, tx, engine ) );
                }
            } else {
                ClassHandler relHandler;
                Object       related;

                relHandler = _fields[ i ].relation.getRelatedHandler();
                related = _fields[ i ].relation.getRelated( source );
                if ( related == null )
                    _fields[ i ].relation.setRelated( target, null );
                else {
                    entry = tx.getObjectEntry( engine, 
                            new OID( relHandler, relHandler.getIdentity( related ) ) );
                    if ( entry != null ) {
                        related = entry.object;
                    }
                    _fields[ i ].relation.setRelated( target, related );
                }
            }
        }
        return target;
    }            

    
    /**
     * Used by {@link #copyField(Object,Object)} to copy a single field.
     */
    private Object copyValue( FieldInfo field, Object source )
    {
        // Immutable objects are copied verbatim. Cloneable objects are
        // cloned, all other fields must be serializable and are
        // serialized.
        if ( field.immutable )
            return source;
        else {
            try {
                ByteArrayOutputStream ba;
                ObjectOutputStream    os;
                ObjectInputStream     is;

                ba = new ByteArrayOutputStream();
                os = new ObjectOutputStream( ba );
                os.writeObject( source );
                os.flush();
                is = new ObjectInputStream( new ByteArrayInputStream( ba.toByteArray() ) );
                return is.readObject();
            } catch ( IOException except ) {
                throw new IllegalStateException( Messages.format( "mapping.schemaNotSerializable",
                                                                  field.fieldType.getName(), except.getMessage() ) );
            } catch ( ClassNotFoundException except ) {
                throw new IllegalStateException( Messages.format( "mapping.schemaNotSerializable",
                                                                  field.fieldType.getName(), except.getMessage() ) );
            }
        }
    }


    /**
     * Determines if the object has been modified from its original
     * value. Returns the modification status as {@link #Unmodified},
     * {@link #Modified} or {@link #LockRequired}. This method does
     * not check whether the identity has been modified.
     *
     * @param object The object
     * @param cached The cached copy
     * @return The modified status
     */
    short isModified( Object object, Object[] original )
    {
        short modified = Unmodified;

        for ( int i = 0 ; i < _fields.length ; ++i ) {
            if ( isModified( _fields[ i ], object, original[ i ] ) ) {
                if ( _fields[ i ].dirty )
                    return LockRequired;
                else if ( modified == Unmodified )
                    modified = Modified;
            }
        }
        return modified;
    }


    /**
     * Used by {@link #isModified(Object,Object[])} to check a single field.
     */
    private boolean isModified( FieldInfo field, Object object, Object original )
    {
        if ( ! field.multi ) {
            Object value;

        // Modified if field has value but original is null,
        // original has value but field is null, or the value
        // of boths does not pass equality test.
        value = field.handler.getValue( object );
        if ( value == null )
        return ( original != null );
        if ( field.relation != null )
            value = field.relation.getIdentity( value );
            if ( value == null )
                return ( original != null );
            else if (value instanceof BigDecimal) 
                return ( original == null || ( (BigDecimal) value ).compareTo( original ) != 0 );
            else 
                return ! value.equals( original );
        } else {
            // The field will always return an enumeration, the original
            // might be null. If the original is null and there are elements
            // in the collection, then the field has been modified. If not,
            // look at all the element in the collection, if any of these does
            // not appear in the vector (based on identity), the field is
            // modified. As the enumeration is traversed the elements are
            // counted to determine whether the vector might have additional
            // elements.
            Enumeration enum;
            int         count;
        Object      relIdentity;

            enum = (Enumeration) field.handler.getValue( object );
            if ( enum.hasMoreElements() && original == null )
                return true;
            for ( count = 0 ;  enum.hasMoreElements() ; ++count ) {
        relIdentity = field.relation.getIdentity( enum.nextElement() );
                if ( ! ( (Vector) original ).contains( relIdentity ) )
                    return true;
        }
            return ( count != ( (Vector) original ).size() );
        }
    }


    /**
     * Checks the object validity. Returns successfully if the object
     * can be stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The object is invalid, a required is
     *  null, or any other validity violation
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler
     *  is not compatiable with the Java object
     */
    void checkValidity( Object object )
        throws ValidityException, IllegalStateException
    {
        // XXX Temporary hack until we find proper home for validity checks
        if ( _clsDesc instanceof ClassDescriptorImpl )
            ( (ClassDescriptorImpl) _clsDesc ).checkValidity( object );
    }


    /**
     * Sets all the fields of the object to null. This method is called
     * on objects that have been deleted during the transaction.
     *
     * @param object The object to reset
     */
    void setFieldsNull( Object object )
    {
        for ( int i = 0 ; i < _fields.length ; ++i )
            _fields[ i ].handler.resetValue( object );
    }


    /**
     * Release lock for all dependent objects in the cache
     *
     * @param fields The fields of the object from the cache
     * @param tx The transaction context
     * @param cache The cache engine
     * @throws PersistenceException An error forgetting the dependent objects
     */
    void releaseDependent( Object[] fields, TransactionContext tx, CacheEngine cache )
    {
        for ( int i = 0 ; i < _fields.length ; ++i ) {
            if ( _fields[ i ].relation != null && _fields[ i ].multi && fields[ i ] != null ) {
                Enumeration enum;
                ClassHandler handler;

                handler = _fields[ i ].relation.getRelatedHandler();
                for ( enum = ((Vector) fields[ i ]).elements(); enum.hasMoreElements(); ) {
                    cache.releaseLockWithDependent( tx, new OID( handler, enum.nextElement() ) );
                }
            }
        }
    }

    /**
     * Apply type convertor to convert the identity from field type to sql type.
     */
    public Object convertIdentity( Object identity ) {
        return ( (FieldHandlerImpl) _identity.handler).convertFrom( identity );        
    }


    public String toString()
    {
        return _clsDesc.toString();
    }


    /**
     * Holds information about a field in the class that can be
     * used to handle that field.
     */
    final static class FieldInfo
    {

    /**
     * The field handler.
     */
        final FieldHandler      handler;

    /**
     * The field type.
     */
        final Class             fieldType;

    /**
     * True if the field is an immutable type.
     */
        final boolean           immutable;

    /**
     * The relation handler if the field is a relation.
     */
        final RelationHandler   relation;

        /**
         * True if the field is multi valued (collection).
         */
        final boolean           multi;

        /**
         * True if the field requires dirty checking.
         */
        final boolean           dirty;

        FieldInfo( FieldDescriptor fieldDesc, RelationHandler relation )
        {
            this.fieldType = fieldDesc.getFieldType();
        this.handler = fieldDesc.getHandler();
            this.immutable = fieldDesc.isImmutable();
            this.relation = relation;
            this.multi = fieldDesc.isMultivalued();
            if ( fieldDesc instanceof JDOFieldDescriptor )
                this.dirty = ( (JDOFieldDescriptor) fieldDesc ).isDirtyCheck();
            else
                this.dirty = true;
        }

        public String toString()
        {
            return handler.toString();
        }

    }


}





