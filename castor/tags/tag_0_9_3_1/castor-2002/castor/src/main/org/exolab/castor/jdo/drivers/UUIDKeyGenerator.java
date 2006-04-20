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


package org.exolab.castor.jdo.drivers;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.StringTokenizer;
import java.net.InetAddress;
import java.text.DecimalFormat;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.util.Messages;

/**
 * UUID key generator.
 * @author <a href="thomas.fach@publica.de">Thomas Fach</a>
 * @version $Revision$ $Date$
 * @see UUIDGeneratorFactory
 */
public final class UUIDKeyGenerator implements KeyGenerator
{
    private final  int                _sqlType;

    private final  PersistenceFactory _factory;

    private        DecimalFormat      _df            = new DecimalFormat();

    private        String             _sHost         = null;

    private static long               _staticCounter = 0;

    /**
     * Initialize the UUID key generator.
     */
    public UUIDKeyGenerator( PersistenceFactory factory, int sqlType )
            throws MappingException
    {
        _factory = factory;
        _sqlType = sqlType;
        if(sqlType != Types.CHAR && sqlType != Types.VARCHAR && sqlType != Types.LONGVARCHAR)
          throw new MappingException( Messages.format( "mapping.keyGenSQLType",
                                     getClass().getName(), new Integer( sqlType ) ) );
    }

    /**
     * Generate a new unique key for the specified table.
     *     
     * @param conn An open connection within the given transaction
     * @param tableName The table name
     * @param primKeyName The primary key name
     * @param props A temporary replacement for Principal object
     * @return A new key
     * @throws PersistenceException An error occured talking to persistent
     *  storage
     */
    public Object generateKey( Connection conn, String tableName, String primKeyName,
            Properties props )
            throws PersistenceException
    {
        String sUUID = null;

        try
        {
          // getting IP (fixed length: 12 character)
          if(_sHost == null)
            _sHost = InetAddress.getLocalHost().getHostAddress();

          StringTokenizer st = new StringTokenizer(_sHost, ".");
          _df.applyPattern("000");
          while(st.hasMoreTokens())
          {
            if(sUUID == null)
              sUUID = _df.format(new Integer(st.nextToken()));
            else
              sUUID += _df.format(new Integer(st.nextToken()));;
          }

          // getting currentTimeMillis (fixed length: 13 character)
          _df.applyPattern("0000000000000");
          sUUID += _df.format(System.currentTimeMillis());

          // getting static counter (fixed length: 15 character)
          if(_staticCounter >= 99999) // 99999 generated keys in one timer interval? no...
            _staticCounter = 0;

          _staticCounter++;
          _df.applyPattern("00000");
          sUUID += _df.format(_staticCounter);
        }
        catch ( Exception ex )
        {
          throw new PersistenceException( Messages.format(
                    "persist.keyGenSQL", ex.toString() ), ex );
        }

        if (sUUID == null)
        {
          throw new PersistenceException( Messages.format(
                    "persist.keyGenOverflow", getClass().getName() ) );
        }

        return sUUID;
    }


    /**
     * Style of key generator: BEFORE_INSERT, DURING_INSERT or AFTER_INSERT ?
     */
    public final byte getStyle() {
        return BEFORE_INSERT;
    }


    /**
     * Gives a possibility to patch the Castor-generated SQL statement
     * for INSERT (makes sense for DURING_INSERT key generators)
     */
    public final String patchSQL( String insert, String primKeyName )
            throws MappingException {
        return insert;
    }


    /**
     * Is key generated in the same connection as INSERT?
     */
    public boolean isInSameConnection() {
        return true;
    }

}