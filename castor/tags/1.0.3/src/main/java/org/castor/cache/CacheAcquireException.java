/*
 * Copyright 2005 Werner Guttmann, Ralf Joachim
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
package org.castor.cache;

/**
 * Exception that indicates that a performance cache instance can not be acquired.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public class CacheAcquireException extends Exception {
    /** SerialVersionUID */
    private static final long serialVersionUID = 6282797357450171990L;

    /**
     * Creates an instance of CacheAcquireException
     * 
     * @param message An error message.
     */
    public CacheAcquireException (final String message) {
        super(message);
    }
    
    /**
     * Creates an instance of CacheAcquireException Exception
     * 
     * @param message An error message
     * @param e The original exception that caused the problem.
     */
    public CacheAcquireException (final String message, final Exception e) {
        super(message, e);
    }
}
