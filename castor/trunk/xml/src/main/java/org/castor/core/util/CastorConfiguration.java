/*
 * Copyright 2007 Ralf Joachim
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
 *
 * $Id: Configuration.java 6907 2007-03-28 21:24:52Z rjoachim $
 */
package org.castor.core.util;


/**
 * Castor configuration holding user properties.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public final class CastorConfiguration extends Configuration {
    //--------------------------------------------------------------------------

    /** Name of common Castor configuration file. */
    private static final String FILENAME = "castor.properties";

    //--------------------------------------------------------------------------

    /**
     * Construct a configuration with given parent. Application and domain class loaders will be
     * initialized to the ones of the parent. 
     * <br/>
     * Note: This constructor is not intended for public use. Use one of the newInstance() methods
     * instead.
     * 
     * @param parent Parent configuration.
     */
    public CastorConfiguration(final Configuration parent) {
        super(parent);
        loadUserProperties(FILENAME);
    }
    
    //--------------------------------------------------------------------------
}
