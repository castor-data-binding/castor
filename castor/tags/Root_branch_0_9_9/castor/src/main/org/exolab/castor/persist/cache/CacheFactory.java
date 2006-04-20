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
 * $Id: CacheFactory.java,v 1.1.1.1 2003/03/03 07:08:25 kvisco Exp
 * $
 */


package org.exolab.castor.persist.cache;

/**
 * A factory for instantiating Cache implementations. To provide an implementation 
 * for a specific cache type, please implement this interface.
 * 
 * When providing your own cache instance as explained in the JavaDocs for this 
 * package, please make sure that you provide valid values for the <b>name</b> and
 * <b>className</b> properties.  
 *
 * @author <a href=" mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Id$
 */
public interface CacheFactory
{

    /**
     * Instantiates an instance of the given class.
	 * @param classLoader A ClassLoader instance.
     * @return A Cache instance.
     * @throws CacheAcquireException Problem instantiating a cache instance.
     */
    Cache getCache (ClassLoader classLoader) 
    	throws CacheAcquireException;

    /**
     * Returns the short alias for this factory instance.
     * @return The short alias name. 
     */
    String getName();

    /**
     * Returns the full class name of the underlying cache implementation.
     * @return The full cache class name. 
     */
    String getCacheClassName();
    
}
