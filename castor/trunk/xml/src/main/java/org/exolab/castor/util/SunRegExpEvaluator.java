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
package org.exolab.castor.util;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * An implementation of {@link RegExpEvaluator} that uses the Java Regular
 * Expression library.
 * 
 * @author <a href="mailto:george76@hotmail.com">George Varghese</a>
 * @since 1.3.2
 **/
public class SunRegExpEvaluator implements RegExpEvaluator {

    /**
     * The Regular expression
     **/
    private Pattern _pattern;

    /**
     * Sets the regular expression to match against during a call to #matches
     * 
     * @param rexpr
     *            the regular expression
     **/
    public void setExpression(String rexpr) {

        if (rexpr != null) {
            try {
                // -- patch and compile expression
                _pattern = Pattern.compile(rexpr);
            } catch (PatternSyntaxException ex) {
                String err = "RegExp Syntax error: ";
                err += ex.getMessage();
                err += " ; error occured with the following "
                        + "regular expression: " + rexpr;

                IllegalArgumentException iae = new IllegalArgumentException(err);
                iae.initCause(ex);
                throw iae;
            }
        }
    }

    /**
     * Returns true if the given String is matched by the regular expression of
     * this RegExpEvaluator
     * 
     * @param value
     *            the String to check the production of
     * @return true if the given string matches the regular expression of this
     *         RegExpEvaluator
     * @see #setExpression
     **/
    public boolean matches(String value) {
        if (_pattern != null) {
            return _pattern.matcher(value).matches();
        }
        return true;
    }

}

