/*
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
 * Copyright 2000-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.validators;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.exolab.castor.util.RegExpEvaluator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * A simple abstract class used for validating types which allow the pattern
 * facet.
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$ $Date: 2004-12-11 02:13:52 -0700 (Sat, 11 Dec 2004) $
 */
public abstract class PatternValidator {
    
    protected static ResourceBundle resourceBundle;
    
    static {
        resourceBundle = ResourceBundle.getBundle("ValidationMessages", Locale
                .getDefault());
    }

    /** 
     * The regular expressions to match against. 
     */
    private LinkedList<String> _patterns = new LinkedList<String>();
    
    /** 
     * If true, object is nillable, otherwise it is not. 
     */
    private boolean _nillable = false;
    
    /** 
     * An instance of the regular expression evaluator if necessary.
     */
    private RegExpEvaluator _regex    = null;

    /**
     * Creates a new {@link PatternValidator} with no initial regular expression.
     */
    public PatternValidator() {
        super();
        
    }

    /**
     * Creates a new {@link PatternValidator} with the given initial regular expression.
     *
     * @param pattern
     *            the regular expression to validate against
     */
    public PatternValidator(final String pattern) {
        _patterns.add(pattern);
    }

    /**
     * Returns the collection of regular expression patterns.
     *
     * @return the collection of regular expression patterns.
     * @see #setPattern
     */
    public List<String> getPatterns() {
        return Collections.unmodifiableList(_patterns);
    }

    /**
     * Returns whether or not objects validated by this Validator are nillable
     * (are allowed to be null).
     *
     * @return true if null is a valid value
     */
    public boolean isNillable() {
        return _nillable;
    }

    /**
     * Returns true if a regular expression has been set for this
     * PatternValidator.
     *
     * @return true if a regular expression has been set for this
     *         PatternValidator
     */
    public boolean hasPattern() {
        return !_patterns.isEmpty();
    }

    /**
     * Sets whether or not objects validated by this Validator are allowed to be
     * null (nillable).
     *
     * @param nillable
     *            a boolean that when true indicates null values pass validation
     */
    public void setNillable(final boolean nillable) {
        _nillable = nillable;
    }

    /**
     * Sets the regular expression to validate against.
     *
     * @param pattern
     *            the regular expression to use when validating
     */
    public void addPattern(final String pattern) {
        _patterns.add(pattern);
    }

    /**
     * Clear all configured patterns.
     */
    public void clearPatterns() {
        _patterns.clear();
    }

    /**
     * Validates the given String against the regular expression pattern of this
     * PatternValidator.
     *
     * @param str
     *            the string to validate
     * @param context
     *            the validation context
     *
     * @see #setPattern
     * @throws ValidationException
     *             if the given String is not matched by the regular expression
     *             pattern
     */
    public void validate(final String str, final ValidationContext context)
                                                    throws ValidationException {
        if (_patterns.isEmpty()) {
            return;
        }

        if (context == null) {
            throw new IllegalArgumentException(resourceBundle.getString("patternValidator.error.exception"));
        }

        if (_regex == null) {
            initEvaluator(context);
        }

        // Loop over all patterns and return (success) if any one of them matches
        for (String pattern : _patterns) {
            _regex.setExpression(pattern);
            if (_regex.matches(str)) {
                return;
            }
        }

        // If we get here, all patterns failed.
        StringBuffer buff = new StringBuffer("'" + str + "'");
        if (_patterns.size() == 1) {
            buff.append(MessageFormat.format(resourceBundle
                    .getString("patternValidator.error.pattern.not.match"),
                    new Object[] { _patterns.getFirst() }));
        } else {
            StringBuffer patterns = new StringBuffer();
            for (String pattern : _patterns) {
                patterns.append("\"");
                patterns.append(pattern);
                patterns.append("\"");
            }
            
            buff.append(MessageFormat.format(resourceBundle
                    .getString("patternValidator.error.pattern.not.match.any"),
                    new Object[] { patterns.toString() }));
        }
        throw new ValidationException(buff.toString());
    }

    /**
     * Validates the given Object.
     *
     * @param object
     *            the Object to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException
     *             if the given String is not matched by the regular expression
     *             pattern
     */
    public void validate(final Object object, final ValidationContext context)
                                                    throws ValidationException {
        if (object == null) {
            if (!_nillable) {
                String err = resourceBundle.getString("patternValidator.error.cannot.validate.null.object");
                throw new ValidationException(err);
            }
            return;
        }
        validate(object.toString(), context);
    }

    /**
     * Initializes the regular expression validator.
     * @param context
     *            the ValidationContext
     */
    private void initEvaluator(final ValidationContext context) {
        _regex = context.getInternalContext().getRegExpEvaluator();
        if (_regex == null && hasPattern()) {
            throw new IllegalStateException(resourceBundle.getString("patternValidator.error.exception.noPatterns"));
        }
    }

}
