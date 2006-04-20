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


package org.exolab.castor.jdo.drivers;


import java.util.Properties;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.KeyGeneratorFactory;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * HIGH/LOW key generator factory.
 * The short name of this key generator is "HIGH/LOW".
 * It uses the following alrorithm: a special sequence table must be in 
 * the database with keeps the maximum key values. The name of the 
 * sequence table is a mandatory parameter of the key generator, 
 * the parameter name is "table".
 * The name of the primary key column of the sequence table and 
 * the name of the column in which maximum values are stored are 
 * mandatory parameters with the names "key-column" and "value-column",
 * respectively. The key column contains table names, so it must be of
 * a character type (char or varchar). The value column contains primary key
 * values, it must be of a numeric type (numeric or int).
 * Key generator reads the maximum value X for the given table, 
 * writes the new value (X + N) to the sequence table and during next N 
 * calls returns values X + 1, ..., X + N without database access.
 * Number N (which sometimes is called "grab size") is an optional
 * parameter of the key generator, the parameter name is "grab-size",
 * default value is "1".
 * For example, if you want to obtain HIGH/LOW key generator with  
 * 3 digits in the LOW part of the key, you should set "grab-size" to "1000".
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 * @see HighLowGenerator
 */
public final class HighLowKeyGeneratorFactory implements KeyGeneratorFactory
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
        return new HighLowKeyGenerator( factory, params, sqlType );
    }

    /**
     * The short name of this key generator is "HIGH/LOW"
     */
    public String getName() {
        return "HIGH/LOW";
    }
}

