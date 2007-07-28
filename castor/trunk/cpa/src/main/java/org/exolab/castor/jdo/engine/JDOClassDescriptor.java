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

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;

/**
 * JDO class descriptors. Extends {@link ClassDescriptor} to include the table name and
 * other SQL-related information. All fields are of type {@link JDOFieldDescriptor},
 * identity field is not included in the returned field list, and contained fields are
 * flattened out for efficiency (thus all fields are directly accessible).
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public class JDOClassDescriptor extends ClassDescriptorImpl {
    //-----------------------------------------------------------------------------------

    /** The name of the SQL table. */
    private String  _tableName;

    /** The access mode specified for this class. */
    private AccessMode _accessMode = AccessMode.Shared;

    /** The properties defining cache type and parameters. */
    private final Properties _cacheParams = new Properties();
    
    /** Associated named queries, keyed by their name. */
    private final Map _namedQueries = new HashMap();
    
    /** The key generator specified for this class. */
    private KeyGeneratorDescriptor _keyGenDesc;

    //-----------------------------------------------------------------------------------

    public JDOClassDescriptor() {
        super();
    }
    
    //-----------------------------------------------------------------------------------

    protected void setTableName(final String tableName) {
        _tableName = tableName;
    }
    
    /**
     * Returns the table name to which this object maps.
     *
     * @return Table name
     */
    public String getTableName() {
        return _tableName;
    }
    
    protected void setAccessMode(final AccessMode accessMode) {
        _accessMode = accessMode;
    }
    
    public AccessMode getAccessMode() {
        return _accessMode;
    }
    
    protected void addCacheParam(final String key, final String value) {
        _cacheParams.put(key, value);
    }

    public Properties getCacheParams() {
        return _cacheParams;
    }

    protected void addNamedQuery(final String name, final String query) {
        _namedQueries.put(name, query);
    }

    /**
     * Get map of named query strings associated with their names.
     * 
     * @return Map of named query strings associated with their names.
     */
    public Map getNamedQueries() {
        return _namedQueries;
    }
    
    /**
     * Set key generator specified for this class.
     * 
     * @param keyGenDesc Key generator descriptor.
     */
    protected void setKeyGeneratorDescriptor(final KeyGeneratorDescriptor keyGenDesc) {
        _keyGenDesc = keyGenDesc;
    }

    /**
     * Get key generator specified for this class.
     *
     * @return Key generator descriptor.
     */
    public KeyGeneratorDescriptor getKeyGeneratorDescriptor() {
        return _keyGenDesc;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Returns a JDOFieldDescriptor for the field with the name passed. <code>null</code>
     * if named field does not exist.
     *
     * @param name Name of the field to return.
     * @return Field if it exists, otherwise <code>null</code>.
     */
    public JDOFieldDescriptor getField(final String name) {
        FieldDescriptor[] fields = getFields();
        for (int i = 0 ; i < fields.length ; ++i) {
            FieldDescriptor field = fields[i];
            if ((field instanceof JDOFieldDescriptor)
                    && (field.getFieldName().equals(name))) {
                
                return (JDOFieldDescriptor) field;
            }
        }
        
        FieldDescriptor[] identities = getIdentities();
        for (int i = 0 ; i < identities.length ; ++i) {
            FieldDescriptor field = identities[i];
            if ((field instanceof JDOFieldDescriptor)
                    && (field.getFieldName().equals(name))) {
                
                return (JDOFieldDescriptor) field;
            }
        }

        return null;
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return getJavaClass().getName() + " AS " + _tableName;
    }

    //-----------------------------------------------------------------------------------
}
