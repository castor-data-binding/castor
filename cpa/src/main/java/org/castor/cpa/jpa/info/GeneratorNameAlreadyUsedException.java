/*
 * Copyright 2010 Werner Guttmann
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
package org.castor.cpa.jpa.info;

/**
 * Signals that a key generator name is used more than once.
 * 
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public class GeneratorNameAlreadyUsedException extends Exception {

    /** Serial version UID. */
    private static final long serialVersionUID = 1721448018301715685L;

    /**
     * Creates an instance of this exception.
     */
    public GeneratorNameAlreadyUsedException() {
        super();
    }
    
    /**
     * Creates an instance of this exception.
     * 
     * @param message The actual error message.
     */
    public GeneratorNameAlreadyUsedException(final String message) {
        super(message);
    }
}
