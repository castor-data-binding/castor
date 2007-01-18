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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;

/**
 * A class which can be used to hold validation information, used
 * by the TypeValidator interface.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-10-06 02:10:17 -0600 (Wed, 06 Oct 2004) $
 */
public class ValidationContext {

    /** Logger for debugging and error information. */
    private static final Log LOG = LogFactory.getLog(ValidationContext.class);

    /** The Castor configuration. */
    private Configuration              _config           = null;
    /** A flag to indicate fail-fast validation. When true, the first error
     * encounted will cause a Validation Exception. When false, the validator
     * should attempt to validate as much as possible, collecting as many errors
     * as possible before throwing a validation exception. */
    private boolean                    _failFast         = true;
    /** The ClassDescriptorResolver to be used for loading and resolving
     * ClassDescriptors during validation. */
    private XMLClassDescriptorResolver _resolver         = null;
    /** The List of objects marked as validated. */
    private final Set                  _validated        = new HashSet();
    /** The Set of already encountered IDs (of type &lt;xsd:ID>). */
    private final Set                  _ids              = new HashSet();
    /** The Set of temporary unreseolved IDREFS. */
    private final Set                  _unresolvedIdrefs = new HashSet();

    /**
     * Creates a new ValidationContext.
     */
    public ValidationContext() {
        super();
    }

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
     }

    /**
     * Returns the ClassDescriptorResolver to use during validation.
     *
     * @return the ClassDescriptorResolver to use. May be null.
     */
    public XMLClassDescriptorResolver getResolver() {
        return _resolver;
    }

    /**
     * Returns true if the validation process should fail upon first error
     * encountered, otherwise the validation processs will attempt to validate
     * as much as possible (even after the first error is encountered) and
     * collect as many errors before either returning (no errors) or throwing a
     * validationException containing the list of errors.
     * <p>
     * <b>NOTE: DISABLING OF FAIL-FAST IS NOT YET ENABLED.</b>
     *
     * @return true if fail-fast processing is enabled.
     */
    public boolean isFailFast() {
        return _failFast;
    }

    /**
     * Sets the Configuration used during validation.
     *
     * @param config the Configuration to use
     */
    public void setConfiguration(final Configuration config) {
        _config = config;
    }

    /**
     * Sets the fail-fast flag. Fail-fast is enabled by default. When fail-fast
     * is enabled (default or by setting the flag to true) the validation
     * process will throw an exception upon the first error encountered. When
     * fail-fast is disabled (by setting the flag to false) the validation
     * processs will attempt to validate even after the first error is
     * encountered and collect as many errors before either returning (no
     * errors) or throwing a validationException containing the list of errors.
     * <p>
     * <b>NOTE: DISABLING FAIL-FAST IS NOT YET ENABLED.</b>
     *
     * @param failFast a boolean that when true enables fail-fast validation,
     *        otherwise the validator will attempt to validate as much as it can
     *        reporting as many errors as possible before returning.
     */
    public void setFailFast(final boolean failFast) {
        _failFast = failFast;
    }

    /**
     * Sets the ClassDescriptorResolver to use during validation.
     *
     * @param resolver the ClassDescriptorResolver to use.
     */
    public void setResolver(final XMLClassDescriptorResolver resolver) {
        _resolver = resolver;
    }

    /**
     * Checks whether an object has already been validated.
     * @param object The object for which the check should be performed
     * @return True if the object specified has already been validated.
     */
    public boolean isValidated(final Object object) { /* FIXME: Should be protected */
        LOG.trace("Called isValidated(" + object + ")");
        return _validated.contains(object);
    }

    /**
     * Adds the specified object to the cache of already validated objects.
     * @param object Object about to be validated.
     */
    public void addValidated(final Object object) { /* FIXME: Should be protected */
        LOG.trace("Called addValidated(" + object + ")");
        _validated.add(object);
    }

    /**
     * Removes the specified object from the cache of already validated objects.
     * @param object The object to be removed from the cache.
     */
    public void removeValidated(final Object object) { /* FIXME: Should be protected */
        LOG.trace("Called removeValidated(" + object + ")");
        _validated.remove(object);
    }

    /**
     * Adds current ID (as seen during (un)marshalling) to the ID cache. If this
     * ID was previously added to the Set of unresolved IDs, then remove it from
     * that Set.
     *
     * @param id The current ID
     * @throws ValidationException If an ID is used more than once.
     * @see #getUnresolvedIdRefs()
     */
    public void addID(final String id) throws ValidationException {
        if (!_ids.contains(id)) {
            _ids.add(id);
            _unresolvedIdrefs.remove(id);
        } else {
            throw new ValidationException ("ID " + id + " is already used within current document.");
        }
    }

    /**
     * Checks an ID Reference, returning true if the provided ID is known. If
     * the provided ID is not known, it is added to the Set of unresolved ID
     * references.  Note that if this ID is later found, it will be removed
     * from this Set.
     *
     * @param id The ID to check.
     * @return true if the provided ID is known.
     * @see #getUnresolvedIdRefs()
     */
    public boolean checkIdRef(final String id) {
        if (!_ids.contains(id)) {
            _unresolvedIdrefs.add(id);
            return false;
        }
        return true;
    }

    /**
     * Returns the Set of unresolved ID references. The Set returns is not
     * modifiable, but is available to be used to list all unresolved
     * references.
     *
     * @return the Set of unresolved ID references.
     */
    public Set getUnresolvedIdRefs() {
        return Collections.unmodifiableSet(_unresolvedIdrefs);
    }

    /**
     * Life-cycle method for proper 'shutdown operations'.
     */
    public void cleanup() {
        _ids.clear();
        _validated.clear();
        _unresolvedIdrefs.clear();
    }

}
