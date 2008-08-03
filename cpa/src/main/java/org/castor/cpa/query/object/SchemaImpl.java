/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.object;

import org.castor.cpa.query.Schema;

/**
 * Final immutable class that implements Schema.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class SchemaImpl extends AbstractField implements Schema  {
    //--------------------------------------------------------------------------

    /** The abstract schema name. */
    private String _abstractName;
    
    /** The name of the type represented by the Schema. */
    private String _typeName;
    
    /** The type represented by the Schema. */
    private Class _type;
 
    /** The identifier of the schema. */
    private String _identifier;
    
    //--------------------------------------------------------------------------
    
    /**
     * Construct schema with given identifier. The name given can be an abstract schema name
     * or the name of the type represented by the schema. If name does not include '.' it is
     * assumed to be an abstract schema name.
     * 
     * @param name The abstract schema name or the name of the type represented by the Schema.
     * @param identifier The identifier of the schema.
     */
    public SchemaImpl(final String name, final String identifier) {
        if (name.indexOf('.') < 0) {
            _abstractName = name;
        } else {
            _typeName = name;
        }
        _identifier = identifier;
    }
    
    /**
     * Construct schema that represents given type with given identifier. 
     * 
     * @param type The type represented by the Schema.
     * @param identifier The identifier of the schema.
     */
    public SchemaImpl(final Class < ? > type, final String identifier) {
        _typeName = type.getName();
        _type = type;
        _identifier = identifier;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Gets the abstract schema name.
     * 
     * @return The abstract schema name.
     */
    public String getAbstractName() {
        return _abstractName;
    }

    /**
     * Gets the name of the type represented by the Schema.
     * 
     * @return The name of the type represented by the Schema.
     */
    public String getTypeName() {
        return _typeName;
    }

    /**
     * Gets the type represented by the Schema.
     * 
     * @return The type represented by the Schema.
     */
    public Class getType() {
        return _type;
    }

    /**
     * Gets the identifier of the schema.
     * 
     * @return The identifier of the schema.
     */
    public String getIdentifier() {
        return _identifier;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */ 
    public StringBuilder toString(final StringBuilder sb) {
        if (_abstractName != null) {
            sb.append(_abstractName);
        } else if (_typeName != null) {
            sb.append(_typeName);
        }
        sb.append(" AS ");
        if (_identifier != null) {
            sb.append(_identifier);             
        }
        return sb;
    }
    
    //--------------------------------------------------------------------------
}
