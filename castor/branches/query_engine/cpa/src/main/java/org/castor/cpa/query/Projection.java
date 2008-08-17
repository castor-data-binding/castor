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
package org.castor.cpa.query;

/**
 * Interface for Projection of query objects.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public interface Projection extends Field {
    //--------------------------------------------------------------------------

    /**
     * Append full string representation of projection to the given <code>StringBuilder</code>.
     * For projection toString() method returns alias only. To get full projection string
     * one has to call toFullString() method.
     *
     * @param sb StringBuilder to append the string representation of this object to.
     * @return Same instance of StringBuilder given as parameter.
     */
    StringBuilder toFullString(StringBuilder sb);

    /**
     * Append full string representation of projection to the given <code>StringBuilder</code>.
     * For projection toString() method returns alias only. To get full projection string
     * one has to call toFullString() method.
     *
     * @return String with full representation of projection.
     */
    String toFullString();

    //--------------------------------------------------------------------------
}
