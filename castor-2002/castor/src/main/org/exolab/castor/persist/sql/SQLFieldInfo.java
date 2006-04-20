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


package org.exolab.castor.persist.sql;

import java.sql.Types;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.persist.EntityFieldInfo;
import org.exolab.castor.persist.types.SQLTypes;
import org.exolab.castor.util.Messages;

/**
 * This class hold SQL-specific information and is used internally by the bridge layer.
 * It aggregates EntityFieldInfo.
 *
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
public final class SQLFieldInfo
{
    /**
     * The info for the value field;
     */
    public final EntityFieldInfo info;

    public final int[] sqlType;

    public final TypeConvertor[] javaToSql;

    public final TypeConvertor[] sqlToJava;

    public SQLFieldInfo(EntityFieldInfo info) throws PersistenceException {
        int len = info.fieldNames.length;

        this.info = info;
        sqlType = new int[len];
        javaToSql = new TypeConvertor[len];
        sqlToJava = new TypeConvertor[len];
        for (int i = 0; i < len; i++) {
            sqlType[i] = SQLTypes.getSQLType(info.declaredType[i]);
            if (info.expectedType[i] != info.declaredType[i]) {
                try {
                    javaToSql[i] = SQLTypes.getConvertor(info.expectedType[i], info.declaredType[i]);
                    sqlToJava[i] = SQLTypes.getConvertor(info.declaredType[i], info.expectedType[i]);
                } catch (Exception except) {
                    throw new PersistenceException(Messages.format("persist.nested", except), except);
                }
            }
        }
    }

    public String toString() {
        return info.toString();
    }

    public boolean equals(Object obj) {
        return info.equals(obj);
    }

    public int hashCode() {
        return info.hashCode();
    }
}
