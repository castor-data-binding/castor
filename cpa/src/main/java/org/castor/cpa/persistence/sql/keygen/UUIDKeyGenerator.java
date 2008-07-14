/*
 * Copyright 2008 Thomas Fach, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.persistence.sql.keygen;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * UUID key generator.
 * 
 * @see UUIDKeyGeneratorFactory
 * @author <a href="mailto:thomas DOT fach AT publica DOT de">Thomas Fach</a>
 * @author <a href="mailto:bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public final class UUIDKeyGenerator implements KeyGenerator {
    //-----------------------------------------------------------------------------------

    private static final DecimalFormat IP_FORMAT = new DecimalFormat("000");

    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("0000000000000");

    private static final DecimalFormat COUNTER_FORMAT = new DecimalFormat("00000");
    
    private static final long COUNTER_MAX = 99999;

    private static long _staticCounter = 0;
    
    private String _hostAddress;

    //-----------------------------------------------------------------------------------

    /**
     * Initialize the UUID key generator.
     */
    public UUIDKeyGenerator(final PersistenceFactory factory, final int sqlType)
    throws MappingException {
        supportsSqlType(sqlType);
        initHostAddress();
        
    }

    private void initHostAddress() throws MappingException {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            
            StringBuffer sb = new StringBuffer();
            StringTokenizer st = new StringTokenizer(host, ".");
            while (st.hasMoreTokens()) {
                sb.append(IP_FORMAT.format(new Integer(st.nextToken())));
            }

            _hostAddress = sb.toString();
        } catch (Exception ex) {
            throw new MappingException(Messages.format(
                    "persist.keyGenSQL", getClass().getName(), ex.toString()), ex);
        }
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object generateKey(final Connection conn, final String tableName,
            final String primKeyName, final Properties props) throws PersistenceException {
        StringBuffer sb = new StringBuffer();
        
        // getting IP (fixed length: 12 character)
        sb.append(_hostAddress);
        
        // getting currentTimeMillis (fixed length: 13 character)
        sb.append(TIME_FORMAT.format(System.currentTimeMillis()));
        
        // getting static counter (fixed length: 5 character)
        if (_staticCounter >= COUNTER_MAX) { _staticCounter = 0; }
        _staticCounter++;
        sb.append(COUNTER_FORMAT.format(_staticCounter));

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void supportsSqlType(final int sqlType) throws MappingException {
        if ((sqlType != Types.CHAR)
                && (sqlType != Types.VARCHAR)
                && (sqlType != Types.LONGVARCHAR)) {
          throw new MappingException(Messages.format("mapping.keyGenSQLType",
                  getClass().getName(), new Integer(sqlType)));
        }
    }

    /**
     * {@inheritDoc}
     */
    public byte getStyle() {
        return BEFORE_INSERT;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInSameConnection() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String patchSQL(final String insert, final String primKeyName) {
        return insert;
    }

    //-----------------------------------------------------------------------------------
}