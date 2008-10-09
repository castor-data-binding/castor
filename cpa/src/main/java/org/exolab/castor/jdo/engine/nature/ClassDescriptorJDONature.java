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
package org.exolab.castor.jdo.engine.nature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.jdo.engine.KeyGeneratorDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;

/**
 * JDO-specific nature for {@link ClassDescriptor}.<br/><br/>
 * 
 * Augments {@link ClassDescriptor} to include persistence-specific data such as 
 * e.g. the table name, cache parameter, key generators, access mode and other 
 * SQL-related information.<br/><br/>
 * 
 * To access persistence-specific data of a {@link ClassDescriptor}, use the 
 * following code fragment to ...
 * 
 * <ol>
 *   <li>check for this nature</li>
 *   <li>apply this nature to the {@link ClassDescriptor} in question.</li>
 *   <li>access e.g. the table name.</li>
 * </ol>
 * 
 * <i>Sample - Looking up table name</i>
 * 
 * <pre>
 *    ClassDescriptor classDescriptor = ...;                                               
 *    ...
 *    if (classDescriptor.hasNature(ClassDescriptorJDONature.class.getName()) {              1)
 *       ClassDescriptorJDONature nature = new ClassDescriptorJDONature(classDescriptor);    2)
 *       ...
 *       String tableName = nature.getTableName();                                           3)
 *    }
 * </pre>
 *  
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.2.1
 */
public class ClassDescriptorJDONature extends BaseNature {
    
    /**
     * Nature property name for table name.
     */
    private static final String TABLE_NAME = "tableName";
    
    /**
     * Nature property name for table name.
     */
    private static final String ACCESS_MODE = "accessMode";

    /**
     * Nature property name for key generator descriptor.
     */
    private static final String KEY_GENERATOR_DESCRIPTOR = "keyGeneratorDescriptor";

    /**
     * Nature property name for cache parameters.
     */
    private static final String CACHE_PARAMETERS = "cacheParameters";

    /**
     * Nature property name for named queries.
     */
    private static final String NAMED_QUERIES = "namedQueries";

    /**
     * Nature property name for {@link ClassDescriptor}s that extend this {@link ClassDescriptor}.
     */
    private static final String EXTENDED = "extended";

    /**
     * Creates an instance of {@link ClassDescriptorJDONature}.
     * @param holder The {@link PropertyHolder} to 'view upon'.
     */
    public ClassDescriptorJDONature(final PropertyHolder holder) {
        super(holder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }
    
    public void setTableName(final String tableName) {
        setProperty(TABLE_NAME, tableName);
    }
    
    /**
     * Returns the table name to which this object maps.
     *
     * @return Table name
     */
    public String getTableName() {
        return (String) getProperty(TABLE_NAME);
    }


    /**
     * Sets the access mode to which this {@link ClassDescriptor} maps.
     * @param accessMode The access mode to be used.
     */
    public void setAccessMode(final AccessMode accessMode) {
        setProperty(ACCESS_MODE, accessMode);
    }
    
    /**
     * Returns the access mode to which this object maps.
     *
     * @return Access mode
     */
    public AccessMode getAccessMode() {
        return (AccessMode) getProperty(ACCESS_MODE);
    }

    /**
     * Set key generator specified for this class.
     * 
     * @param keyGenDesc Key generator descriptor.
     */
    public void setKeyGeneratorDescriptor(final KeyGeneratorDescriptor keyGenDesc) {
        setProperty(KEY_GENERATOR_DESCRIPTOR, keyGenDesc);
    }

    /**
     * Get key generator specified for this class.
     *
     * @return Key generator descriptor.
     */
    public KeyGeneratorDescriptor getKeyGeneratorDescriptor() {
        return (KeyGeneratorDescriptor) getProperty(KEY_GENERATOR_DESCRIPTOR);
    }

    /**
     * Adds a cache parameter to this {@link ClassDescriptor}.
     * @param key The cache parameter key.
     * @param value The cache parameter value.
     */
    public void addCacheParam(final String key, final String value) {
        Properties cacheParameters = (Properties) getProperty(CACHE_PARAMETERS);
        if (cacheParameters == null) {
            cacheParameters = new Properties();
        }
        cacheParameters.put(key, value);
        setProperty(CACHE_PARAMETERS, cacheParameters);
    }
    
    /**
     * Returns the cache parameters defined for this {@link ClassDescriptor}.
     * @return the defined cache parameters
     */
    public Properties getCacheParams() {
        return (Properties) getProperty(CACHE_PARAMETERS);
    }

    /**
     * Adds a named query to this {@link ClassDescriptor}.
     * @param name The name of the named query.
     * @param query The query string
     */
    public void addNamedQuery(final String name, final String query) {
        Map namedQueries = (Map) getProperty(NAMED_QUERIES);
        if (namedQueries == null) {
            namedQueries = new HashMap();
        }
        namedQueries.put(name, query);
        setProperty(NAMED_QUERIES, namedQueries);
   }

    /**
     * Get map of named query strings associated with their names.
     * 
     * @return Map of named query strings associated with their names.
     */
    public Map getNamedQueries() {
        return getPropertyAsMap(NAMED_QUERIES);
    }

    /**
     * Returns the {@link FieldDescriptor} for the given name.
     * @param name A field name.
     * @return The associated {@link FieldDescriptor}.
     */
    public FieldDescriptor getField(final String name) {
        FieldDescriptor[] fields = ((ClassDescriptor) getHolder()).getFields();
        for (int i = 0; i < fields.length; ++i) {
            FieldDescriptor field = fields[i];
            if ((field.hasNature(FieldDescriptorJDONature.class.getName()))
                    && (field.getFieldName().equals(name))) {
                return field;
            }
        }
        
        FieldDescriptor[] identities = ((ClassDescriptorImpl) getHolder()).getIdentities();
        for (int i = 0; i < identities.length; ++i) {
            FieldDescriptor field = identities[i];
            if ((field.hasNature(FieldDescriptorJDONature.class.getName()))
                    && (field.getFieldName().equals(name))) {
                return field;
            }
        }

        return null;
    }

    /**
     * Adds a {@link ClassDescriptor} that extends this class.
     * @param classDesc A {@link ClassDescriptor} that extends this class.
     */
    public void addExtended(final ClassDescriptor classDesc) {
        List extendeds = getPropertyAsList(EXTENDED);
        if (extendeds == null) {
            extendeds = new ArrayList();
        }
        extendeds.add(classDesc);
        setProperty(EXTENDED, extendeds);
    }
    

    /**
     * Returns a collection of {@link ClassDescriptor}s that extend this class (descriptor).
     * @return A collection of {@link ClassDescriptor}s that extend this class.
     */
    public Collection getExtended() {
        return getPropertyAsList(EXTENDED);
    }

}
