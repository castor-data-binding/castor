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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist.cache;


import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.util.LocalConfiguration;


/**
 * Registry for {@link CacheFactory} implementations
 * obtained from the Castor properties file and used by the
 * JDO mapping configuration file.
 * 
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Id$
 */
public final class CacheRegistry {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(CacheRegistry.class);
    
    /**
     * Property listing all available {@link Cache} implementations 
     * (<tt>org.exolab.castor.jdo.cacheImplementations</tt>).
     */
    private static final String CACHE_IMPLEMENTATION_PROPERTY= 
        "org.exolab.castor.jdo.cacheFactories";

    /**
     * Default cache type to be used.
     */
    private static final String DEFAULT_CACHE_TYPE = "count-limited";
    
    /**
     * Default cache capacity (when not specified).
     */
    private static final int DEFAULT_CAPACITY = 30;
    
    /**
     * Association between {@link Cache} implementation name and cache implementation.
     */
    private static Hashtable  _cacheFactories;

    /**
     * Returns a {@link CacheFactory} with the specified name.
     * The factory class names are loaded from the Castor properties
     * file. Returns null if the named factory is not supported.
     *
     * @param cacheType Cache type identifier
     * @param capacity Cache capacity.
     * @param className Class name.
	 * @param classLoader A ClassLoader instance.
     * @return A {@link Cache} instance, null if no cache type with this name exists.
     * @throws CacheAcquireException A cache of the type specified can not be acquired.
     */
    public static Cache getCache (String cacheType, int capacity, String className, ClassLoader classLoader) 
    	throws CacheAcquireException
    {
        load();
        
        Cache cache = null;
        
        // still assume that a user does not have to specify a cache type 
        // in the mapping file. for such a case, we still set the default 
        // value to be "count-limited" and the default capacity to 0. 
        if (cacheType == null || cacheType == "") {
        	cacheType = DEFAULT_CACHE_TYPE;
        	capacity = DEFAULT_CAPACITY;
        }
        
        // if no capacity is specified, set it to default capacity.
        if (capacity == 0) {
            capacity = DEFAULT_CAPACITY;
        }
        
        CacheFactory cacheFactory = (CacheFactory) _cacheFactories.get (cacheType);
        
        if (cacheFactory != null) {
            cache = cacheFactory.getCache(classLoader);
            cache.setCacheType (cacheType);
            cache.setCapacity (capacity);
            cache.setClassName (className);
        }
        
        if (_log.isDebugEnabled()) {
            _log.debug ("Successfully instantiated '" + cache.getCacheType() + 
                        "' cache instance [" + cache.getCapacity() + "/" + cache.getClassName() + "]");
        }
        
        return cache;
    }


    /**
     * Returns the names of all the configured {@link Cache} instances as 
     * a String array. The names can be used to obtain a {@link Cache} from 
     * {@link #getCache}.
     *
     * @return Names of the configured {@link Cache} implementations.
     */
    public static String[] getCacheFactories()
    {
        String[]    names;
        Enumeration enumeration;

        load();
        names = new String[ _cacheFactories.size() ];
        enumeration = _cacheFactories.keys();
        for ( int i = 0 ; i < names.length ; ++i ) {
            names[ i ] = (String) enumeration.nextElement();
        }
        return names;
    }

    /**
     * Returns the names of all the configured {@link Cache} instances as a 
     * Collection. The names can be used to obtain a {@link Cache} from 
     * {@link #getCache}.
     *
     * @return Names of the configured {@link Cache} implementations.
     */
	public static Collection getCaches() {
		load();
		return Collections.unmodifiableCollection(_cacheFactories.keySet());
	}

    /**
     * Load the {@link CacheFactory} implementations from the 
     * properties file, if not loaded before.
     */
    private static synchronized void load()
    {
        if ( _cacheFactories == null ) {
            String                    prop;
            StringTokenizer           tokenizer;
            CacheFactory cacheFactory = null;
            _cacheFactories = new Hashtable();
            
            prop = LocalConfiguration.getInstance().getProperty(CACHE_IMPLEMENTATION_PROPERTY, "");
            tokenizer = new StringTokenizer( prop, ", " );
            while (tokenizer.hasMoreTokens()) {
                prop = tokenizer.nextToken();
                try {
                	Class cls = CacheRegistry.class.getClassLoader().loadClass(prop);
                	cacheFactory = (CacheFactory) cls.newInstance();
                	_cacheFactories.put(cacheFactory.getName(), cacheFactory);
                } 
                catch (Exception except) {
                    _log.error ("Problem instantiating cache implementation.", except);
                }
            }
        }
    }

}
