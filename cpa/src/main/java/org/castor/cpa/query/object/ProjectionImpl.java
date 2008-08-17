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
import org.castor.cpa.query.Projection;

/**
 * Final immutable class that a projection of a select query.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class ProjectionImpl extends AbstractField implements Projection {
    //--------------------------------------------------------------------------
    
    /** The field or schema of the projection. */
    private final Field _field;
    
    /** The alias of the projection. */
    private final String _alias;
    
    //--------------------------------------------------------------------------

    /**
     * Construct projection with given field.
     * 
     * @param field The field or schema of the projection.
     */
    public ProjectionImpl(final Field field) {
        if (field == null) { throw new NullPointerException(); }
        _field = field;
        _alias = null;
    }
    
    /**
     * Construct projection with given field and alias.

     * @param field The field or schema of the projection.
     * @param alias The alias of the projection.
     */
    public ProjectionImpl(final Field field, final String alias) {
        if (field == null) { throw new NullPointerException(); }
        _field = field;
        _alias = alias;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Gets the field or schema of the projection.
     * 
     * @return The field of the projection.
     */
    public Field getField() {
        return _field;
    }
    
    /**
     * Gets the alias of the projection.
     * 
     * @return The alias of the projection.
     */
    public String getAlias() {
        return _alias;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */ 
    public StringBuilder toString(final StringBuilder sb) {
        if (_alias != null) {
            sb.append(_alias);            
        } else {
            _field.toString(sb);
        }
        return sb;
    }

    /**
     * {@inheritDoc}
     */ 
    public StringBuilder toFullString(final StringBuilder sb) {
        _field.toString(sb); 
        if (_alias != null) {
            sb.append(" AS ");
            sb.append(_alias);            
        }
        return sb;
    }

    /**
     * {@inheritDoc}
     */ 
    public String toFullString() {
        return toFullString(new StringBuilder()).toString();
    }

    //--------------------------------------------------------------------------
}
