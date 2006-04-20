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


package org.exolab.castor.dax.engine;


import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Hashtable;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPModification;
import netscape.ldap.LDAPModificationSet;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPReferralException;
import netscape.ldap.LDAPv2;
import netscape.ldap.LDAPDN;
import netscape.ldap.util.DN;
import netscape.ldap.util.RDN;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.FatalPersistenceException;
import org.exolab.castor.persist.PersistenceExceptionImpl;
import org.exolab.castor.persist.DuplicateIdentityExceptionImpl;
import org.exolab.castor.persist.ObjectNotFoundExceptionImpl;
import org.exolab.castor.persist.ObjectDeletedExceptionImpl;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;



/**
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class MozillaEngine
    implements Persistence
{


    private DAXClassDescriptor    _clsDesc;


    private DAXFieldDescriptor[]  _fields;


    private DAXFieldDescriptor[] _dnFields;


    private String               _dnFieldName;

 
    private FieldHandler         _attrField;


    private String               _rootDN;


    private int                  _firstField;


    public MozillaEngine( DAXClassDescriptor clsDesc, String rootDN )
        throws MappingException
    {
        FieldDescriptor[] fields;
        
        _clsDesc = clsDesc;
        _firstField = 0;
        /*
        _attrField = _clsDesc.getAttributeSetField();
        */
        fields = _clsDesc.getFields();
        _fields = new DAXFieldDescriptor[ fields.length ];
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ] instanceof DAXFieldDescriptor )
                _fields[ i ] = (DAXFieldDescriptor) fields[ i ];
        }
        _dnFieldName = ( (DAXFieldDescriptor) _clsDesc.getIdentity() ).getLdapName();
        _rootDN = rootDN;
    }


    public Object create( Object conn, Object[] fields, Object identity )
        throws DuplicateIdentityException, PersistenceException
    {
        LDAPAttributeSet   ldapSet;
        String             dn;
        DAXFieldDescriptor field;
        boolean            exists;
        
        dn = getDN( identity );
        try {
            ldapSet = new LDAPAttributeSet();
            for ( int i = 0 ; i < _fields.length ; ++i )
                ldapSet.add( getAttribute( _fields[ i ].getLdapName(), fields[ _firstField + i ] ) );

            // XXX
            // Also need to create all the attributes in the attrSet
            DAXClassDescriptor clsDesc;
            
            clsDesc = _clsDesc;
            while ( clsDesc != null ) {
                ldapSet.add( new LDAPAttribute( "objectclass", clsDesc.getLdapClass() ) );
                clsDesc = (DAXClassDescriptor) clsDesc.getExtends();
            }
            ( (LDAPConnection) conn ).add( new LDAPEntry( dn, ldapSet ) );
            return identity;
        } catch ( LDAPException except ) {
            if ( except.getLDAPResultCode() == LDAPException.ENTRY_ALREADY_EXISTS )
                throw new DuplicateIdentityExceptionImpl( _clsDesc.getJavaClass(), identity );
            if ( except.getLDAPResultCode() == LDAPException.SERVER_DOWN || 
                 except.getLDAPResultCode() == LDAPException.CONNECT_ERROR )
                throw new FatalPersistenceException( except );
            throw new PersistenceExceptionImpl( except );
        }
    }


    public Object load( Object conn, Object[] fields, Object identity,
                        AccessMode accessMode )
        throws ObjectNotFoundException, PersistenceException
    {
        LDAPAttributeSet   ldapSet;
        LDAPAttribute      ldapAttr;
        LDAPEntry          entry;
        String             dn;
        DAXFieldDescriptor field;
        Enumeration        enum;
        
        dn = getDN( identity );
        try {
            entry = ( (LDAPConnection) conn ).read( dn );
            if ( entry == null )
                throw new ObjectNotFoundExceptionImpl( _clsDesc.getJavaClass(), identity );
        } catch ( LDAPException except ) {
            if ( except.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
                throw new ObjectNotFoundExceptionImpl( _clsDesc.getJavaClass(), identity );
            if ( except.getLDAPResultCode() == LDAPException.SERVER_DOWN || 
                 except.getLDAPResultCode() == LDAPException.CONNECT_ERROR )
                throw new FatalPersistenceException( except );
            throw new PersistenceExceptionImpl( except );
        }
        
        ldapSet = entry.getAttributeSet();

        for ( int i = 0 ; i < _fields.length ; ++i ) {
            fields[ _firstField + i ] = getValue( ldapSet.getAttribute( _fields[ i ].getLdapName() ),
                                                  _fields[ i ].getFieldType() );
            ldapSet.remove( _fields[ i ].getLdapName() );
        }

        for ( int i = 0 ; i < ldapSet.size() ; ++i ) {
            ldapAttr = ldapSet.elementAt( i );
            if ( ldapAttr.getName().equals( "objectclass" ) ) {
                
                String[] classes;
                boolean  match;
                
                /*
                  classes = ldapAttr.getStringValueArray();
                  match = false;
                  for ( int j = 0 ; j < classes.length ; ++j ) {
                  if ( classes[ j ].equals( _clsDesc.getLdapClass() ) ) {
                  match = true;
                  break;
                  }
                  }
                  if ( ! match ) {
                  throw new IllegalStateException( "LDAP entry does not match object class " +
                  _clsDesc.getLdapClass() );
                  }
                */
                
            } else if ( _attrField != null ) {
                /* XXX Should work with field directly
                Hashtable     attrSet;
                
                attrSet = (Hashtable) _attrField.getValue( obj );
                if ( attrSet == null ) {
                    attrSet = new Hashtable();
                    _attrField.setValue( obj, attrSet );
                }
                attrSet.put( ldapAttr.getName(), ldapAttr.getStringValueArray() );
                */
            }
        }
        return null;
    }


    public Object store( Object conn, Object[] fields, Object identity,
                         Object[] original, Object stamp )
        throws ObjectModifiedException, ObjectDeletedException, PersistenceException
    {
        LDAPModificationSet modifs;
        String              dn;
        LDAPEntry           entry;
        LDAPAttributeSet    ldapSet;
        boolean             exists;
        
        dn = getDN( identity );
        try {
            entry = ( (LDAPConnection) conn ).read( dn );
            if ( entry == null )
                throw new ObjectDeletedExceptionImpl( _clsDesc.getJavaClass(), identity );
        } catch ( LDAPException except ) {
            if ( except.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
                throw new ObjectDeletedExceptionImpl( _clsDesc.getJavaClass(), identity );
            if ( except.getLDAPResultCode() == LDAPException.SERVER_DOWN || 
                 except.getLDAPResultCode() == LDAPException.CONNECT_ERROR )
                throw new FatalPersistenceException( except );
            throw new PersistenceExceptionImpl( except );
        }
        ldapSet = entry.getAttributeSet();
        
        modifs = new LDAPModificationSet();
        for ( int i = 0 ; i < _fields.length ; ++i ) {
            LDAPAttribute attr;

            exists = ( ldapSet.getAttribute( _fields[ i ].getLdapName() ) != null );
            attr = getAttribute( _fields[ i ].getLdapName(), fields[ _firstField + i ] );
            if ( exists )
                ldapSet.remove( _fields[ i ].getLdapName() );
            if ( attr == null ) {
                if ( exists )
                    modifs.add( LDAPModification.DELETE, new LDAPAttribute( _fields[ i ].getLdapName() ) );
            } else {
                modifs.add( ( exists ? LDAPModification.REPLACE : LDAPModification.ADD ), attr );
            }
        }

        if ( _attrField != null ) {
            /*
            Hashtable    attrSet;
            Enumeration  attrs;
            String       name;
            Object       value;
            
            attrSet = (Hashtable) _attrField.getValue( obj );
            attrs = attrSet.keys();
            while ( attrs.hasMoreElements() ) {
                name = (String) attrs.nextElement();
                exists = ( ldapSet.getAttribute( name ) != null );
                if ( exists )
                    ldapSet.remove( name );
                value = attrSet.get( name.toString() );
                if ( value instanceof String[] )
                    modifs.add( ( exists ? LDAPModification.REPLACE : LDAPModification.ADD ),
                                new LDAPAttribute( name, (String[]) value ) );
                else
                    modifs.add( ( exists ? LDAPModification.REPLACE : LDAPModification.ADD ),
                                new LDAPAttribute( name, value.toString() ) );
            }
            */
        }

        Enumeration enum;
        
        enum = ldapSet.getAttributes();
        while ( enum.hasMoreElements() ) {
            LDAPAttribute ldapAttr;
            
            ldapAttr = (LDAPAttribute) enum.nextElement();
            if ( ! ldapAttr.getName().equals( "objectclass" ) ) {
                modifs.add( LDAPModification.DELETE, ldapAttr );
            }
        }
        try {
            ( (LDAPConnection) conn ).modify( dn, modifs );
            return null;
        } catch ( LDAPException except ) {
            if ( except.getLDAPResultCode() == LDAPException.SERVER_DOWN || 
                 except.getLDAPResultCode() == LDAPException.CONNECT_ERROR )
                throw new FatalPersistenceException( except );
            throw new PersistenceExceptionImpl( except );
        }
    }
    
    
    public void delete( Object conn, Object identity )
        throws PersistenceException
    {
        String dn;
        
        dn = getDN( identity );
        try {
            ( (LDAPConnection) conn ).delete( dn );
        } catch ( LDAPException except ) {
            if ( except.getLDAPResultCode() == LDAPException.SERVER_DOWN || 
                 except.getLDAPResultCode() == LDAPException.CONNECT_ERROR )
                throw new FatalPersistenceException( except );
            throw new PersistenceExceptionImpl( except );
        }
    }


    public void writeLock( Object conn, Object identity )
        throws ObjectDeletedException, PersistenceException
    {
    }
    
    
    public PersistenceQuery createQuery( QueryExpression query, Class[] types )
        throws QueryException
    {
        return new MozillaQuery( query.getStatement( false ), types );
    }


    private String getDN( Object identity )
    {
        return getDN( identity, true );
    }


    private String getDN( Object identity, boolean withRootDN )
    {
        StringBuffer dn;
        boolean      first;
        
        dn = new StringBuffer();
        if ( _dnFields != null ) {
            first = true;
            for ( int i = _dnFields.length ; i-- > 0 ; ) {
                if ( first )
                    first = false;
                else
                    dn.append( ',' );
                dn.append( _dnFields[ i ].getLdapName() ).append( '=' );
                dn.append( _dnFields[ i ].getHandler().getValue( identity ).toString() );
            }
        } else {
            dn.append( _dnFieldName );
            dn.append( '=' ).append( identity.toString() );
        }
        if ( withRootDN && _rootDN != null )
            dn.append( ',' ).append( _rootDN );
        return dn.toString();
    }


    private Object getIdentityFromDN( String dn )
    {
        String[] rdns;
        
        if ( _rootDN == null )
            rdns = LDAPDN.explodeDN( dn, true );
        else
            rdns = LDAPDN.explodeRDN( dn.substring( 0, dn.length() - _rootDN.length() - 1 ), true );

        if ( _dnFields != null ) {
            Object identity;
            
            identity = Types.newInstance( _clsDesc.getIdentity().getFieldType() );
            for ( int i = _dnFields.length ; i-- > 0 ; )
                _dnFields[ i ].getHandler().setValue( identity, rdns[ i ] );
            return identity;
        } else {
            return rdns[ 0 ];
        }
    }


    private LDAPAttribute getAttribute( String ldapName, Object value )
    {
        if ( value == null ) {
            return null;
        } else if ( value instanceof byte[] ) {
            return new LDAPAttribute( ldapName, (byte[]) value );
        } else if ( value instanceof String[] ) {
            return  new LDAPAttribute( ldapName, (String[]) value );
        } else if ( value instanceof String ) {
            return new LDAPAttribute( ldapName, (String) value );
        } else if ( value instanceof char[] ) {
            return new LDAPAttribute( ldapName, new String( (char[]) value ) );
        } else if ( Types.isSimpleType( value.getClass() ) ) {
            // Simple objects are stored as String
            return new LDAPAttribute( ldapName, value.toString() );
        } else {
            return null;
            // Complex objects are serialized
            /*
            try {
                return new LDAPAttribute( ldapName, Serializer.serialize( value ) );
            } catch ( IOException except ) {
                throw new IllegalArgumentException( "Cannot serialize field value of type " +
                                                    value.getClass().getName() );
            }
            */
        }
    }


    private Object getValue( LDAPAttribute attr, Class fieldType )
    {
        if ( attr == null )
            return null;
        if ( fieldType == byte[].class ) {
            byte[][] values;
            int      count;
            byte[]   bytes;
            
            // Pretty much takes an array of bytes and stuffs it
            // into a single array of bytes
            values = attr.getByteValueArray();
            if ( values.length == 0 )
                return null;
            else if ( values.length == 1 )
                return values[ 0 ];
            else {
                count = 0;
                for ( int i = 0 ; i < values.length ; ++i )
                    count += values[ i ].length;
                bytes = new byte[ count ];
                count = 0;
                for ( int i = 0 ; i < values.length ; ++i ) {
                    for ( int j = 0 ; j < values.length ; ++j )
                        bytes[ count + j ] = values[ i ][ j ];
                    count += values[ i ].length;
                }
                return values;
            }
        } else if ( fieldType == String[].class ) {
            return attr.getStringValueArray();
        } else {
            // Type conversion comes here
            String[]  values;
            
            values = attr.getStringValueArray();
            if ( values.length == 0 )
                return null;
            else if ( values.length == 1 )
                return values[ 0 ];
            else
                // Need to assemble all strings together
                return values[ 0 ];
        }
    }



    class MozillaQuery
        implements PersistenceQuery
    {
        
        private final Class[]     _paramTypes;
        
        
        private Object[]          _paramValues;
        
        
        private LDAPEntry         _lastResult;
        
        
        private LDAPSearchResults _results;
        
        
        private final String[]    _query;
        
        
        MozillaQuery( String query, Class[] types )
        {
            StringTokenizer token;
            
            if ( types == null )
                throw new IllegalArgumentException( "Argument 'types' is null" );
            _paramTypes = types;
            _paramValues = new Object[ _paramTypes.length ];
            if ( query == null )
                throw new IllegalArgumentException( "Argument 'query' is null" );
            token = new StringTokenizer( query, "\0" );
            _query = new String[ token.countTokens() ];
            for ( int i = 0 ; i < _query.length ; ++i )
                _query[ i ] = token.nextToken();
            if ( _query.length != _paramTypes.length + 1 )
                throw new IllegalArgumentException( "Argument 'query' and 'paramTypes' do not match in number of parameters" );
        }
        
        
        public int getParameterCount()
        {
            return _paramTypes.length;
        }
        
        
        public Class getParameterType( int index )
        {
            return _paramTypes[ index ];
        }
        
        
        public void setParameter( int index, Object value )
        {
            if ( value != null && _paramTypes[ index ] != null )
                if ( ! _paramTypes[ index ].isAssignableFrom( value.getClass() ) )
                    throw new IllegalArgumentException( "Parameter " + index + " must be of type " +
                                                        _paramTypes[ index ].getName() + " instead recieved type " +
                                                        value.getClass().getName() );
            _paramValues[ index ] = value;
        }
        
        
        public Class getResultType()
        {
            return _clsDesc.getJavaClass();
        }
        
        
        public void execute( Object conn, AccessMode accessMode )
            throws QueryException, PersistenceException
        {
            try {
                StringBuffer filter;
                
                filter = new StringBuffer();
                for ( int i = 0 ; i < _query.length - 1 ; ++i ) {
                    filter.append( _query[ i ] );
                    if ( _paramValues[ i ] != null )
                        filter.append( _paramValues[ i ].toString() );
                }
                filter.append( _query[ _query.length - 1 ] );
                _lastResult = null;
                _paramValues = new Object[ _paramTypes.length ];
                _results = ( (LDAPConnection) conn ).search( _rootDN, LDAPv2.SCOPE_ONE, filter.toString(), null, false );
            } catch ( LDAPException except ) {
                if ( except.getLDAPResultCode() == LDAPException.SERVER_DOWN || 
                     except.getLDAPResultCode() == LDAPException.CONNECT_ERROR )
                    throw new FatalPersistenceException( except );
                throw new PersistenceExceptionImpl( except );
            }
        }


        public void close()
        {
            _results = null;
            _lastResult = null;
        }
        
        
        public Object nextIdentity( Object identity )
            throws PersistenceException
        {
            _lastResult = null;
            if ( _results.hasMoreElements() ) {
                Object result;
                
                result = _results.nextElement();
                if ( result instanceof LDAPEntry ) {
                    _lastResult = (LDAPEntry) result;
                    return getIdentityFromDN( _lastResult.getDN() );
                } else if ( result instanceof LDAPReferralException ) {
                    // No support for referrals in this release
                    throw new PersistenceExceptionImpl( (LDAPReferralException) result );
                } else if ( result instanceof LDAPException ) {
                    throw new PersistenceExceptionImpl( (LDAPException) result );
                }
            }
            return null;
        }
        
        
        public Object fetch( Object[] fields, Object identity )
            throws ObjectNotFoundException, PersistenceException
        {
            LDAPAttributeSet   ldapSet;
            LDAPAttribute      ldapAttr;
            DAXFieldDescriptor field;
            Enumeration        enum;
            
            if ( _lastResult == null )
                throw new PersistenceExceptionImpl( "Internal error: fetch called without an identity returned from getIdentity/nextIdentity" );
            
            ldapSet = _lastResult.getAttributeSet();

            for ( int i = 0 ; i < _fields.length ; ++i ) {
                fields[ _firstField + i ] = getValue( ldapSet.getAttribute( _fields[ i ].getLdapName() ),
                                                      _fields[ i ].getFieldType() );
                ldapSet.remove( _fields[ i ].getLdapName() );
            }
            _lastResult = null;
            return null;
        }
        
    }


}

