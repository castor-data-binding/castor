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
package org.exolab.castor.jdo.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.castor.cache.Cache;
import org.castor.cache.simple.CountLimited;
import org.castor.cache.simple.TimeLimited;
import org.castor.util.Messages;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.xml.CacheTypeMapping;
import org.exolab.castor.mapping.xml.Param;

/**
 * JDO class descriptors. Extends {@link ClassDescriptor} to include the
 * table name and other SQL-related information. All fields are of
 * type {@link JDOFieldDescriptor}, identity field is not included in the
 * returned field list, and contained fields are flattened out for
 * efficiency (thus all fields are directly accessible).
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public class JDOClassDescriptor extends ClassDescriptorImpl {
    /** The name of the SQL table. */
    private final String  _tableName;

    /** The names of columns that the identity consists of. */
    private final String[] _idnames;

    /** The key generator specified for this class. */
    private final KeyGeneratorDescriptor _keyGenDesc;

    /** The properties defining cache type and parameters. */
    private final Properties _cacheParams = new Properties();
    
    /**
     * Associated named queries, keyed by their name 
     */
    private Map _namedQueries = new HashMap();
    
    public JDOClassDescriptor(final ClassDescriptor clsDesc,
            final KeyGeneratorDescriptor keyGenDesc)
    throws MappingException {
        super((ClassDescriptorImpl) clsDesc);
        
        _tableName = getMapping().getMapTo().getTable();
        if (_tableName == null) {
            throw new IllegalArgumentException("Argument 'tableName' is null");
        }
        
        if (getIdentity() == null) {
            throw new MappingException("mapping.noIdentity", getJavaClass().getName());
        }
        
        if (!(getIdentity() instanceof JDOFieldDescriptor)) {
            throw new IllegalArgumentException("Identity field must be of type JDOFieldDescriptor");
        }
        
        if ((getExtends() != null) && !(getExtends() instanceof JDOClassDescriptor)) {
            throw new IllegalArgumentException( "Extended class does not have a JDO descriptor" );
        }
        
        _idnames = new String[_identities.length];
        for (int i = 0; i < _idnames.length; i++) {
        	String[] sqlNames = ((JDOFieldDescriptor) _identities[i]).getSQLName();
        	if (sqlNames == null) {
        		throw new MappingException("mapping.noSqlName", _identities[i].getFieldName(), getJavaClass().getName());
        	}
        	_idnames[i] = sqlNames[0];
        }

        _keyGenDesc = keyGenDesc;

        CacheTypeMapping cacheMapping = getMapping().getCacheTypeMapping();
        if (cacheMapping != null) {
            String capacity = Integer.toString(cacheMapping.getCapacity());
            _cacheParams.put(CountLimited.PARAM_CAPACITY, capacity);
            _cacheParams.put(TimeLimited.PARAM_TTL, capacity);
            
            Param[] params = cacheMapping.getParam();
            for (int i = 0; i < params.length; i++) {
                _cacheParams.put(params[i].getName(), params[i].getValue());
            }

            String debug = Boolean.toString(cacheMapping.getDebug());
            _cacheParams.put(Cache.PARAM_DEBUG, debug);

            String type = cacheMapping.getType();
            if ((TimeStampable.class.isAssignableFrom(getJavaClass()))
                    && (type != null) && (type.equalsIgnoreCase("none"))) {
                throw new MappingException(Messages.format("persist.wrongCacheTypeSpecified", getMapping().getName()));
            }
            _cacheParams.put(Cache.PARAM_TYPE, type);
        }
        
        _cacheParams.put(Cache.PARAM_NAME, getMapping().getName());
    }

    /**
     * Returns the table name to which this object maps.
     *
     * @return Table name
     */
    public String getTableName() {
        return _tableName;
    }

    public Properties getCacheParams() {
        return _cacheParams;
    }

    /**
     * Returns a JDOFieldDescriptor for the field with the name passed.  Null
     * if named field does not exist.
     *
     * @param name The name of the field to return
     * @return The field if it exists, otherwise null.
     */
    public JDOFieldDescriptor getField(String name) {
        JDOFieldDescriptor field = null;
        for (int i = 0 ; i < _fields.length ; ++i) {
            if ((_fields[i] instanceof JDOFieldDescriptor)
                    && (_fields[i].getFieldName().equals(name))) {
                
                field = (JDOFieldDescriptor) _fields[i];
                break;
            }
        }
        
        if (field == null) {
            for (int i = 0 ; i < _identities.length ; ++i) {
                if ((_identities[i] instanceof JDOFieldDescriptor)
                        && (_identities[i].getFieldName().equals(name))) {
                    
                    field = (JDOFieldDescriptor) _identities[i];
                }
            }
        }

        return field;
    }

    /**
     * Returns the key generator specified for this class.
     *
     * @return The key generator descriptor
     */
    public KeyGeneratorDescriptor getKeyGeneratorDescriptor() {
        return _keyGenDesc;
    }

    /**
     * @return The names of columns that the identity consists of.
     */
    public String[] getIdentityColumnNames() {
        return _idnames;
    }

    public String toString() {
        return super.toString() + " AS " + _tableName;
    }

    /**
     * Returns the OQL statement from a named query instance associated with the given name
     * @param name Name of the named query
     * @return the OQL statement from a named query instance associated with the given name  
     */
    public String getNamedQuery(String name) {
        String namedQuery = (String) _namedQueries.get(name);
        return namedQuery;
    }
    
    /**
     * Adds a new named query for the given name for future usage (through Database.getNamedQuery()).
     * @param name Name of the named query.
     * @param namedQuery Named query to be associated with the given name
     * @throws QueryException If there's already a named query for the given name
     */
    public void addNamedQuery(final String name, final String namedQuery) throws QueryException {
        if (_namedQueries.containsKey(name)) {
            throw new QueryException ("Duplicate entry for named query " + name);
        }
        _namedQueries.put(name, namedQuery);
    }
}
