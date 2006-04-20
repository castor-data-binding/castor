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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.persist.cache;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.util.Messages;

/**
  * Base implementation of {@link CacheFactory}. Users interested in supplying their
  * own cache implementations might want to extend this class to provide their
  * own {@link CacheFactory} instance. 
  *
  * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
  */
public abstract class AbstractCacheFactory 
    implements CacheFactory
{
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(AbstractCacheFactory.class);
    
    /**
     * Instantiates an instance of the given class.
     * @return A Cache instance.
     * @throws CacheAcquireException Problem instantiating a cache instance.
     */
    public Cache getCache() 
        throws CacheAcquireException
    {
        Cache cache = null;

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); 
            cache = (Cache) Class.forName (getCacheClassName(), true, classLoader).newInstance();
        }
        catch (ClassNotFoundException cnfe) {
            _log.error ("Cannot find class " + getCacheClassName(), cnfe);
            throw new CacheAcquireException ( Messages.format( 
                    "jdo.transaction.unableToAcquireTransactionManager", cnfe.getMessage()), cnfe);
        }
        catch (IllegalAccessException iae) {
            _log.error ("Illegal access with class " + getCacheClassName(), iae);
            throw new CacheAcquireException( Messages.format( 
                    "jdo.transaction.unableToAcquireTransactionManager", iae.getMessage()), iae);
        } 
        catch (InstantiationException e) {
            _log.error ("Problem instantiating class " + getCacheClassName(), e);
            throw new CacheAcquireException( Messages.format( 
                    "jdo.transaction.unableToAcquireTransactionManager", e.getMessage()), e );
        }
        
        return cache;
    }

    /**
     * Returns the short alias for this factory instance.
     * @return The short alias name. 
     */
    abstract public String getName();
    
    /**
     * Returns the full class name of the underlying cache implementation.
     * @return The full cache class name. 
     */
    abstract public String getCacheClassName();
    
}
