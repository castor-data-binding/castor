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
package org.exolab.castor.persist.spi;

import java.util.Properties;

import org.exolab.castor.mapping.MappingException;

/**
 * Interface for a key generator factory. The key generator factory
 * is used for producing key generators for concrete databases with
 * given parameters
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public interface KeyGeneratorFactory {
    
    /**
     * Produce the key generator.
     * 
     * @param factory Helper object for obtaining database-specific QuerySyntax.
     * @param params Parameters for key generator.
     * @param sqlType The SQL type of the primary key, the generated identities must have
     *        the corresponding Java type, e.g. java.sql.Types.INTEGER corresponds to
     *        java.lang.Integer, java.sql.Types.NUMERIC corresponds to java.lang.BigDecimal.
     * @return A {@link KeyGenerator} instance.
     * @throws MappingException If there's a problem resolving the mapping information. 
     */
    KeyGenerator getKeyGenerator(PersistenceFactory factory, Properties params, int sqlType)
    throws MappingException;

    /**
     * Get the short name of the key generator. It is used to reference
     * key generators in a mapping configuration file.
     * If several key generators of the same type are used for the same
     * database, then they are referenced by aliases.
     * @return Name of the {@link KeyGenerator} used to identify key generator (types).
     */
    String getName();
}
