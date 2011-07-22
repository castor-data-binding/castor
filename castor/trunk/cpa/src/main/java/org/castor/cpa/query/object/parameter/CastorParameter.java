/*
 * Copyright 2011 Ralf Joachim, Johannes Venzke
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
package org.castor.cpa.query.object.parameter;

/**
 * Final immutable class that represents a Castor Parameter.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="johannes DOT venzke AT revival DOT de">Johannes Venzke</a>
 * @version $Revision: $
 */
public final class CastorParameter extends AbstractParameter {
    //--------------------------------------------------------------------------

    /** Position of the Castor Parameter. */
    private final int _position;
    
    /** Type of the Castor Parameter.  */
    private final String _type;

    //--------------------------------------------------------------------------

    /**
     * Construct Castor Parameter with given position.
     * 
     * @param position Position of the Castor Parameter.
     */
    public CastorParameter(final int position) {
        this(position, null);
    }
    
    /**
     * Construct Castor Parameter with given position and type.
     * 
     * @param position Position of the Castor Parameter.
     * @param type The type of the Castor Parameter. 
     */
    public CastorParameter(final int position, final String type) {
        _position = position;
        _type = type;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Get the position of the Castor Parameter.
     *
     * @return Position of the Castor Parameter.
     */
    public int getPosition() {
        return _position;
    }
    
    /**
     * Get the type of the Castor Parameter.
     *
     * @return Type of the Castor Parameter.
     */
    public String getType() {
        return _type;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append('$');
        if (_type != null) {
            sb.append('(' + _type + ')');
        }
        return sb.append(_position);
    }

    //--------------------------------------------------------------------------
}
