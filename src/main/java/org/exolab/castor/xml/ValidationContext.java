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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;

import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;

import java.util.HashSet;

/**
 * A class which can be used to hold validation information, used
 * by the TypeValidator interface.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class ValidationContext {
    
    /**
     * The Castor configuration
     */
    private Configuration _config = null;
    
    /**
     * A flag to indicate fail-fast validation. When true
     * The first error encounted will cause a Validation
     * Exception, when false, the validator should
     * attempt to validate as much as possible collecting
     * as many errors as possible before throwing
     * a validation exception.
     */
    private boolean _failFast = true;
    
    /**
     * The ClassDescriptorResolver to be used for loading
     * and resolving ClassDescriptors during validation
     */
    private ClassDescriptorResolver _resolver = null;
    
    /**
     * List of objects marked as validated
     */
    private HashSet _validated = null;
    
    
    /**
     * Creates a new ValidationContext
     */
    public ValidationContext() {
        super();
    } //-- ValidationContext
    
    
    /**
     * Returns the Configuration to use during validation.
     *
     * @return the Configuration to use. Will never be null.
     */
     public Configuration getConfiguration() {
        if (_config == null) {
            _config = LocalConfiguration.getInstance();
        } 
        return _config;
     } //-- getConfiguration
     
    /**
     * Returns the ClassDescriptorResolver to use during validation.
     *
     * @return the ClassDescriptorResolver to use. May be null.
     */
    public ClassDescriptorResolver getResolver() {
        return _resolver;
    } //-- setResolver
     
    /**
     * Returns true if the validation process should fail
     * upon first error encountered, otherwise the validation
     * processs will attempt to validate as much as possible
     * (even after the first error is encountered) and collect as
     * many errors before either returning (no errors) or
     * throwing a validationException containing the list of
     * errors.
     *
     * <b>NOTE: DISABLEING OF FAIL-FAST IS NOT YET ENABLED.</b>
     *
     * @return true if fail-fast processing is enabled.
     */
    public boolean isFailFast() {
        return _failFast;
    } //-- isFailFast
    
    /**
     * Sets the Configuration used during validation
     *
     * @param config the Configuration to use
     */
    public void setConfiguration(Configuration config) {
        _config = config;
    }

    /**
     * Sets the fail-fast flag. Fail-fast is enabled by default.
     * When fail-fast is enabled (default or by setting the
     * flag to true) the validation process will throw an 
     * exception upon the first error encountered.
     * When fail-fast is disabled (by setting the flag to false)
     * the validation processs will attempt to validate even
     * after the first error is encountered and collect as
     * many errors before either returning (no errors) or
     * throwing a validationException containing the list of
     * errors.
     *
     * <b>NOTE: DISABLEING FAIL-FAST IS NOT YET ENABLED.</b>
     *
     * @param failFast a boolean that when true enables fail-fast
     * validation, otherwise the validator will attempt to validate
     * as much as it can reporting as many errors as possible before
     * returning.
     */
    public void setFailFast(boolean failFast) {
        _failFast = failFast;
    } //-- setFailFast
    
    /**
     * Sets the ClassDescriptorResolver to use during validation.
     *
     * @param resolver the ClassDescriptorResolver to use.
     */
    public void setResolver(ClassDescriptorResolver resolver) {
        _resolver = resolver;
    } //-- setResolver

    protected boolean isValidated(Object obj) {
    	if (_validated != null) {
    		return _validated.contains(obj);
        }
        return false;
    }
    
    protected void addValidated(Object obj) {
    	if (_validated == null) {
            _validated = new HashSet();
        }
        _validated.add(obj);
    }
    
    protected void removeValidated(Object obj) {
        if (_validated != null) {
            _validated.remove(obj);
        }
    }
    
    
} //-- ValidationContext