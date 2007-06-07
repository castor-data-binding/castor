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

import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Configuration.Property;

/**
 * PersistenceFactory for SQL Server.
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2004-01-19 13:01:46 -0700 (Mon, 19 Jan
 *          2004) $
 */
public final class SQLServerFactory extends SybaseFactory {

    public String getFactoryName() {
        return "sql-server";
    }

    public QueryExpression getQueryExpression() {
        LocalConfiguration configuration = LocalConfiguration.getInstance();
        boolean useNewSyntaxForSQLServer = Boolean.valueOf(
                configuration.getProperty(Property.SqlServerAnsiCompliant,
                        "false")).booleanValue();

        if (useNewSyntaxForSQLServer) {
            return new JDBCQueryExpression(this);
        }

        return new SQLServerQueryExpression(this);
    }

    /**
     * SQL Server doesn't support setNull for "WHERE fld=?".
     */
    public boolean supportsSetNullInWhere() {
        return false;
    }
}
