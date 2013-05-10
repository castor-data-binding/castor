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
package org.castor.cpa.query.object.parameter;

/**
 * Final immutable class that represents a named parameter.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class NamedParameter extends AbstractParameter {
    //--------------------------------------------------------------------------

    /** Name of the named parameter. */
    private final String _name;

    //--------------------------------------------------------------------------

    /**
     * Construct named parameter with given name.
     * 
     * @param name Name of the named parameter.
     */
    public NamedParameter(final String name) {
        if (name == null) { throw new NullPointerException(); }
        _name = name;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Get the name of the named parameter.
     *
     * @return Name of the named parameter.
     */
    public String getName() {
        return _name;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        return sb.append(':').append(_name);
    }

    //--------------------------------------------------------------------------
}
