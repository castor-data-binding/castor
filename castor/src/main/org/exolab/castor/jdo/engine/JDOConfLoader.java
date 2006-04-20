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

package org.exolab.castor.jdo.engine;

import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Mapping;
import org.exolab.castor.jdo.conf.TransactionDemarcation;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.DTDResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Helper class to load the Castor JDO configuration from its configuration file.
 * @author <a href="mailto:werner.guttmann@gmx.net">Werner Guttmann</a>
 */
public class JDOConfLoader {
    
    private static boolean _loaded = false;
    private static JdoConf _jdoConf = null;
    
    public static synchronized void loadConfiguration (InputSource source, EntityResolver resolver) 
        throws MappingException 
    {
        
        if (!_loaded) {
            
            Unmarshaller       unmarshaller;

            unmarshaller = new Unmarshaller (JdoConf.class);
            try {
                
                if (resolver == null)
                    unmarshaller.setEntityResolver (new DTDResolver());
                else
                    unmarshaller.setEntityResolver (new DTDResolver (resolver));
                    
                _jdoConf = (JdoConf) unmarshaller.unmarshal (source);
            }
            catch (MarshalException e) {
                throw new MappingException (e); 
            }
            catch (ValidationException e) {
                throw new MappingException (e);
            }
            
            _loaded = true; 
        }
    }
    
    public static Database getDatabase (InputSource source, EntityResolver resolver) 
        throws MappingException 
    {
        loadConfiguration (source, resolver);
        return _jdoConf.getDatabase();
    }
    
    public static TransactionDemarcation getTransactionDemarcation (InputSource source, EntityResolver resolver)
        throws MappingException 
    {
        loadConfiguration  (source, resolver);
        return _jdoConf.getTransactionDemarcation();
    }
    
    public static Mapping[] getMapping (InputSource source, EntityResolver resolver) 
        throws MappingException 
    {
        loadConfiguration  (source, resolver);
        return _jdoConf.getDatabase().getMapping();
    }
    
    /**
     * Deletes JDO configuration. 
     */
    public static synchronized void deleteConfiguration () {
        _loaded = false;
    }
    
    /**
     * Indicates whether JDO configuration has loaded successfully.
     * @return True if JDO configuration is loaded.
     */
    public static synchronized boolean isLoaded() {
        return _loaded;
    }

}
