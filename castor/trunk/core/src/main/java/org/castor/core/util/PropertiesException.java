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

import org.castor.core.exceptions.CastorRuntimeException;

/**
 * PropertiesException is an unchecked exception thrown when properties can not be loaded
 * or if configuration property can't be converted to the requested type.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public final class PropertiesException extends CastorRuntimeException {
    /** SerialVersionUID */
    private static final long serialVersionUID = 4446761026170253291L;

    /**
     * Constructs a new PropertiesException without a message. The cause is not initialized
     * but may subsequently be initialized by a call to initCause(Throwable).
     */
    public PropertiesException() {
        super();
    }
    
    /**
     * Constructs a new PropertiesException with the specified detail message. The cause is
     * not initialized but may subsequently be initialized by a call to initCause(Throwable).
     * 
     * @param message The detail message.
     */
    public PropertiesException(final String message) {
        super(message);
    }

    /**
     * Constructs a new PropertiesException with the specified cause and the detail message
     * of the cause. This constructor is useful for exceptions that are wrappers for others.
     * 
     * @param cause The cause.
     */
    public PropertiesException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new PropertiesException with the specified detail message and cause.
     * 
     * @param message The detail message.
     * @param cause The cause.
     */
    public PropertiesException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
