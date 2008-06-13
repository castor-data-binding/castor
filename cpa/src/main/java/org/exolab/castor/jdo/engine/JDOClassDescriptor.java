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
package org.exolab.castor.jdo.engine;

import java.util.Map;
import java.util.Properties;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;

/**
 * JDO class descriptor.
 * 
 * Extends and augments {@link ClassDescriptor} to include persistence-specific data such as 
 * e.g. the table name and other SQL-related information. 
 *
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7099 $ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 * @since 1.2.1
 */
public interface JDOClassDescriptor extends ClassDescriptor {

    /**
     * Returns the table name to which this object maps.
     *
     * @return Table name
     */
    String getTableName();

    /**
     * Return the access mode specified for this class. 
     * @return the access mode specified for this class
     */
    AccessMode getAccessMode();

    /**
     * {@link Properties} defining cache type and parameters.
     * @return Cache-related properties for this class.
     */
    Properties getCacheParams();

    /**
     * Get map of named query strings associated with their names.
     * 
     * @return Map of named query strings associated with their names.
     */
    Map getNamedQueries();

    /**
     * Get key generator specified for this class.
     *
     * @return Key generator descriptor.
     */
    KeyGeneratorDescriptor getKeyGeneratorDescriptor();
    
    /**
     * Returns the identity fields ({@link FieldDescriptor}) of this class.
     * @return the identity fields of this class.
     */
    FieldDescriptor[] getIdentities();
    

}