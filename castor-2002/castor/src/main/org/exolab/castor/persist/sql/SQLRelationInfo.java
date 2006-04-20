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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.persist.EntityInfo;
import org.exolab.castor.persist.EntityFieldInfo;
import org.exolab.castor.persist.types.SQLTypes;

/**
 * Relation info.
 * It follows the Factory pattern and caches instances.
 *
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
public final class SQLRelationInfo
{

    private static HashMap _instances = new HashMap();

    public final SQLEntityInfo oneInfo;

    public final String manyTable;

    public final EntityFieldInfo manyForeignKey;

    /**
     * fldInfo The field representing the "one" side of the relation.
     */
    private SQLRelationInfo(EntityFieldInfo fldInfo) throws PersistenceException {
        oneInfo = SQLEntityInfo.getInstance(fldInfo.entityClass);
        manyTable = fldInfo.relatedEntityClass.entityClass;
        manyForeignKey = fldInfo.relatedForeignKey;
    }

    public static SQLRelationInfo getInstance(EntityFieldInfo fldInfo) throws PersistenceException {
        SQLRelationInfo res;

        res = (SQLRelationInfo) _instances.get(fldInfo);
        if (res == null) {
            res = new SQLRelationInfo(fldInfo);
        }
        return res;
    }

    public String toString() {
        return oneInfo + ":" + manyForeignKey;
    }

    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof SQLRelationInfo) &&
                manyForeignKey.equals(((SQLRelationInfo) obj).manyForeignKey);
    }

    public int hashCode() {
        return manyForeignKey.hashCode();
    }
}
