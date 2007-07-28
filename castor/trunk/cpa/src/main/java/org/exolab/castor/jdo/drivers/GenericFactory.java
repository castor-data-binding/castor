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


import java.util.StringTokenizer;

import org.exolab.castor.jdo.engine.BaseFactory;
import org.exolab.castor.persist.spi.QueryExpression;


/**
 * {@link org.exolab.castor.persist.spi.PersistenceFactory} for generic JDBC driver.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2004-01-19 13:01:46 -0700 (Mon, 19 Jan 2004) $
 */
public class GenericFactory extends BaseFactory {
    public String getFactoryName() {
        return "generic";
    }

    public QueryExpression getQueryExpression() {
        return new JDBCQueryExpression( this );
    }

    /**
     * Determine if the given SQLException is DuplicateKeyException.
     * 
     * @return Boolean.TRUE means "yes",
     *         Boolean.FALSE means "no",
     *         null means "cannot determine"
     */
    public Boolean isDuplicateKeyException(Exception ex) {
        return null;
    }

    public String quoteName(String name) {
        return name;
    }

 	/**
 	 * Updated to handle input such as user.tablename.column
 	 *
	 * @author Andrew Ballanger, 3/15/2001
	 */
	protected final String doubleQuoteName(String name) {
		StringBuffer buffer = new StringBuffer();
		StringTokenizer tokens = new StringTokenizer(name, ".");

		// Note:
		//
		// I am assuming this method always recieves a non null,
		// and non empty string.

		buffer.append('\"');
		buffer.append(tokens.nextToken());
		while(tokens.hasMoreTokens()) {
			buffer.append("\".\"");
			buffer.append(tokens.nextToken());
		}
		buffer.append('\"');

		return buffer.toString();
	}


}


