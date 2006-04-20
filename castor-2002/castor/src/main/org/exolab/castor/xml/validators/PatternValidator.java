/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.castor.xml.validators;
 
import org.exolab.castor.xml.*;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.RegExpEvaluator;
 
/**
 * A simple abstract class used for validating types
 * which allow the pattern facet
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class PatternValidator {
    
    /**
     * The regular expression
    **/
    private String _pattern = null;
    
    /**
     * An instance of the regular expression evaluator
     * if necessary
    **/
    private RegExpEvaluator _regex = null;    
    
    
    /**
     * Creates a new PatternValidator with no default
     * regular expression
    **/
    public PatternValidator() {
        super();
    } //-- PatternValidator
    
    /**
     * Creates a new PatternValidator with the given
     * regular expresion
     *
     * @param pattern the regular expression to validate against
    **/
    public PatternValidator(String pattern) {
        _pattern = pattern;
    } //-- PatternValidator
    
    /**
     * Returns the regular expression pattern for this PatternValidator,
     * or null if no pattern has been set.
     *
     * @return the regular expression pattern
     * @see #setPattern
    **/
    public String getPattern() {
        return _pattern;
    } //-- getPattern
    
    /**
     * Returns true if a regular expression has been set
     * for this PatternValidator
     *
     * @return true if a regular expression has been set
     * for this PatternValidator
    **/
    public boolean hasPattern() {
        return (_pattern != null);
    } //-- hasPattern
    
    /**
     * Sets the regular expression to validate against
     * @param pattern the regular expression to use when validating
    **/
    public void setPattern(String pattern) {
        _pattern = pattern;
        if (_regex != null) 
            _regex.setExpression(_pattern);
    } //-- setPattern
    
    /**
     * Validates the given String against the regular expression pattern
     * of this PatternValidator. 
     * @see #setPattern
     * @exception ValidationException if the given String is not
     * matched by the regular expression pattern
    **/
    public void validate(String str)
        throws ValidationException 
    {
        if (_pattern != null) {
            if (_regex == null)
                initEvaluator();
            if (!_regex.matches(str)) {
                String err = "objects of this type must match the " +
                    " following regular expression: " + _pattern;
                throw new ValidationException(err);
            }
        }
    } //-- validate
    
    /**
     * Validates the given Object
     * @param object the Object to validate
    **/
    public void validate(Object object)
        throws ValidationException 
    {
        if (object == null) {
            String err = "PatternValidator cannot validate a null object.";
            throw new ValidationException(err);
        }
        
        validate(object.toString());
        
    } //-- validate
    
    /**
     * Initializes the regular expression validator
    **/
    private void initEvaluator() {
        _regex = Configuration.getRegExpEvaluator();
        if (_regex == null)
            _regex = new DefaultRegExpEvaluator();
        _regex.setExpression(_pattern);
    } //-- initRegExpValidator
    
    /**
     * A simple implementation of a regular expression validator
     * which always returns false. 
     
     * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
     * @version $Revision$ $Date$
    **/
    class DefaultRegExpEvaluator 
        implements RegExpEvaluator
    {
        
        /**
         * Creates a new DefaultRegExpValidator
        **/
        DefaultRegExpEvaluator() {
            super();
        } //-- DefaultRegExpEvalutator
        
        /**
         * Sets the regular expression to match against during
         * a call to #matches
         *
         * @param rexpr the regular expression
        **/
        public void setExpression(String rexpr) {
            //-- nothing to do...we don't care since
            //-- match will always evaluate to false
        } //-- setExpression
    
        /**
         * Returns true if the given String is matched by the 
         * regular expression of this RegExpEvaluator
         *
         * @param value the String to check the production of
         * @return true if the given string matches the regular
         * expression of this RegExpEvaluator
         * @see #setExpression
        **/
        public boolean matches(String value)
        {
            return false;
        } //-- matches
        
    } //-- DefaultRegExpEvaluator
    
} //-- PatternValidator
 
 
