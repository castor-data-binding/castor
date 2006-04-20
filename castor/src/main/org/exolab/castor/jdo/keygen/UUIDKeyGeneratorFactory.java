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
 */

/**
 * ----------------------------------------------
 * UUIDKeyGenerator
 * ----------------------------------------------
 * Developed by Publica Data-Service GmbH
 *
 * Team "New Projects" are:
 * Joerg Sailer, Martin Goettler, Andreas Grimme,
 * Thorsten Prix, Norbert Fuss, Thomas Fach
 * 
 * Address:
 * Publica Data-Service GmbH
 * Steinerne Furt 72
 * 86167 Augsburg
 * Germany
 *
 * Internet: http://www.publica.de
 *
 * "live long and in prosper"
 * ----------------------------------------------
**/


package org.exolab.castor.jdo.keygen;


import java.util.Properties;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.KeyGeneratorFactory;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * UUID key generator factory.
 * The short name of this key generator is "UUID".
 * It uses the following alrorithm: 
 * The uuid is a combination of the IP address, the current
 * time in milliseconds since 1970 and a static counter.
 * The complete key consists of a 30 character fixed length string.
 * Brief statement:
 * The ip only exists once during runtime of castor, the
 * current time in milliseconds (updated every 55 mills) is
 * in combination to the ip pretty unique. considering a static 
 * counter will be used a database-wide unique key will be created.
 * 
 * @author <a href="thomas.fach@publica.de">Thomas Fach</a>
 * @version $Revision$ $Date$
 * @see UUIDKeyGenerator
 */
public final class UUIDKeyGeneratorFactory implements KeyGeneratorFactory
{
    /**
     * Produce the key generator.
     * @factory Helper object for obtaining database-specific QuerySyntax.
     * @params Parameters for key generator.
     */
    public KeyGenerator getKeyGenerator( PersistenceFactory factory,
            Properties params, int sqlType )
            throws MappingException
    {
        return new UUIDKeyGenerator( factory, sqlType );
    }

    /**
     * The short name of this key generator is "UUID"
     */
    public String getName() {
        return "UUID";
    }
}

