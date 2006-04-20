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


import java.util.List;
import java.util.Arrays;

/**
 * This class is used internally by SQLEngine to hold information about loaded Relations
 * in bounds of one transaction.
 *
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
final class LoadedRelation {

    /**
     * The path consisting of one-to-many relations used for loading.
     */
    final SQLRelationInfo[] oneToManyPath;

    /**
     * The identity on the "one" side of the first relation in the path.
     */
    final Object identity;

    /**
     * The name of the table on the "many" side of the last relation in the path.
     */
    final String manyTable;

    /**
     * The list of identities on th "many" side of the last relation in the path.
     */
    final List list;

    LoadedRelation(SQLRelationInfo[] oneToManyPath, Object identity, List list) {
        this.oneToManyPath = oneToManyPath;
        this.manyTable = oneToManyPath[oneToManyPath.length - 1].manyTable;
        this.identity = identity;
        this.list = list;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(256);

        sb.append(identity);
        for (int i = 0; i < oneToManyPath.length; i++) {
            sb.append('/');
            sb.append(oneToManyPath[i]);
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        LoadedRelation lr;

        lr = (LoadedRelation) obj;
        return (identity.equals(lr.identity) && Arrays.equals(oneToManyPath, lr.oneToManyPath));
    }

    public int hashCode() {
        return identity.hashCode();
    }
}
