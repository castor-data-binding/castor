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

import org.castor.cpa.query.Field;

/**
 * Final immutable class that implements Field.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class FieldImpl extends AbstractField {
    //--------------------------------------------------------------------------
    
    /** The parent of the field. */
    private Field _parent;
    
    /** The name of the field. */
    private String _name;
    
    //--------------------------------------------------------------------------

    /**
     * Construct field implementation.
     * 
     * @param parent The parent of the field.
     * @param name The name of the field.
     */
    public FieldImpl(final Field parent, final String name) {
        _parent = parent;
        _name = name;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Gets the parent of the field.
     * 
     * @return The parent of the field.
     */
    public Field getParent() {
        return _parent;
    }
    
    /**
     * Gets the name of the field.
     * 
     * @return The name of the field.
     */
    public String getName() {
        return _name;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */ 
    public StringBuilder toString(final StringBuilder sb) {
        if (_parent instanceof SchemaImpl) {
            sb.append(((SchemaImpl) _parent).getIdentifier());
        } else if (_parent != null) {
            _parent.toString(sb);
        } 
        sb.append('.');
        if (_name != null) {
            sb.append(_name);            
        }
        return sb;
    }

    //--------------------------------------------------------------------------
}
