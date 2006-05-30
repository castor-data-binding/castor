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


package org.exolab.castor.jdo.keygen;


import java.util.Properties;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.KeyGeneratorFactory;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * SEQUENCE key generator factory.
 * The short name of this key generator is "SEQUENCE".
 * It uses Oracle/PostrgeSQL SEQUENCEs
 * There are two optional parameters for this key generator:
 * 1) name is "sequence" and the default value is "{0}_seq";
 * 2) name is "returning", values: "true"/"false", default is "false".
 * The latter parameter should be used only with Oracle8i, "true" value 
 * turns on more efficient RETURNING syntax.
 * It is possible to use naming patterns like this for obtaining
 * SEQUENCE name by table name. This gives the possibility to use
 * one global key generator declaration rather than one per table.
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date: 2005-06-01 06:08:22 -0600 (Wed, 01 Jun 2005) $
 * @see SequenceKeyGenerator
 */
public final class SequenceKeyGeneratorFactory implements KeyGeneratorFactory
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
        return new SequenceKeyGenerator( factory, params, sqlType );
    }

    /**
     * The short name of this key generator is "SEQUENCE"
     */
    public String getName() {
        return "SEQUENCE";
    }
}
