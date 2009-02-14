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
package org.castor.cpa.persistence.sql.driver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.AbstractProperties;
import org.castor.core.util.Messages;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.persistence.sql.keygen.KeyGenerator;
import org.castor.cpa.persistence.sql.keygen.KeyGeneratorFactory;
import org.castor.cpa.persistence.sql.keygen.KeyGeneratorFactoryRegistry;
import org.exolab.castor.jdo.engine.KeyGeneratorDescriptor;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.PersistenceQuery;

/**
 * {@link org.exolab.castor.persist.spi.PersistenceFactory} for generic JDBC driver.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public abstract class BaseFactory implements PersistenceFactory {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(BaseFactory.class);
    
    /** Key generator registry. */
    private final KeyGeneratorFactoryRegistry _keyGeneratorFactoryRegistry;
    
    /** Maps class descriptor to persistence engines. */
    private final Map<ClassDescriptor, KeyGenerator> _classDescriptorToKeyGenerator =
        new HashMap<ClassDescriptor, KeyGenerator>();

    /** Maps class descriptor to persistence engines. */
    private final Map<ClassDescriptor, Persistence> _classDescriptorToPersistence =
        new HashMap<ClassDescriptor, Persistence>();

    public BaseFactory() {
        AbstractProperties properties = CPAProperties.getInstance();
        _keyGeneratorFactoryRegistry = new KeyGeneratorFactoryRegistry(properties);
    }
    
    /**
     * {@inheritDoc}
     */
    public KeyGenerator getKeyGenerator(final ClassDescriptor clsDesc)
    throws MappingException {
        KeyGenerator keyGen = _classDescriptorToKeyGenerator.get(clsDesc);
        if (keyGen == null) {
            KeyGeneratorDescriptor keyGenDesc = 
                new ClassDescriptorJDONature(clsDesc).getKeyGeneratorDescriptor();
            
            if ((clsDesc.getExtends() == null) && (keyGenDesc != null)) {
                String keyGenFactoryName = keyGenDesc.getKeyGeneratorFactoryName();
                KeyGeneratorFactory keyGenFactory =
                    _keyGeneratorFactoryRegistry.getKeyGeneratorFactory(keyGenFactoryName);

                if (keyGenFactory == null) {
                    String msg = Messages.format("mapping.noKeyGen", keyGenFactoryName);
                    LOG.warn(msg);
                    throw new MappingException(msg);
                }

                FieldDescriptor fldDesc = clsDesc.getIdentity();
                int[] sqlTypes = new FieldDescriptorJDONature(fldDesc).getSQLType();
                int sqlType = (sqlTypes == null) ? 0 : sqlTypes[0];
                
                Properties keyGenParams = keyGenDesc.getParams();
                keyGen = keyGenFactory.getKeyGenerator(this, keyGenParams, sqlType);
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Key generator " + keyGenFactoryName
                            + " has been instantiated, parameters: " + keyGenParams);
                }
                
                // Does the key generator support the sql type specified in the mapping?
                keyGen.supportsSqlType(sqlType);
            }
            
            _classDescriptorToKeyGenerator.put(clsDesc, keyGen);
        }
        
        return keyGen;
    }
    
    /**
     * {@inheritDoc}
     */
    public Persistence getPersistence(final ClassDescriptor clsDesc) {
        if (!(clsDesc.hasNature(ClassDescriptorJDONature.class.getName()))) { 
            return null; 
        }
        
        try {
            Persistence sqlEngine = _classDescriptorToPersistence.get(clsDesc);
            if (sqlEngine == null) {
                sqlEngine = new SQLEngine(clsDesc, this);
                _classDescriptorToPersistence.put(clsDesc, sqlEngine);
            }
            return sqlEngine;
        } catch (MappingException except) {
            LOG.fatal(Messages.format("jdo.fatalException", except));
            return null;
        }
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
    public PersistenceQuery getCallQuery(final String call, final Class[] paramTypes,
            final Class javaClass, final String[] fields, final int[] sqlTypes) {
        return null;
    }


    /**
     * Some databases has some problems with some SQL types.
     * Usually it is enough to merely replace one SQL type by another.
     * 
     * @param sqlType The correspondent Java class for the SQL type in mapping.xml
     * @return The correspondent Java class for the SQL type that should be used instead.
     */
    public Class adjustSqlType(final Class sqlType) {
        return sqlType;
    }

    /**
     * Many databases don't support setNull for "WHERE fld=?" and require "WHERE fld IS NULL".
     */
    public boolean supportsSetNullInWhere() {
        return false;
    }
}


