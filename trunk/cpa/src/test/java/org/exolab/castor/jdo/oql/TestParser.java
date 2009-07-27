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
 */
package org.exolab.castor.jdo.oql;

import java.util.Hashtable;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * Test class for {@link Parser}. 
 *
 * @author  <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date: 2006-04-29 05:45:43 -0600 (Sat, 29 Apr 2006) $
 */
public final class TestParser extends TestCase {
    public static final int NODE_TYPES = 1;
    public static final int NODE_VALUES = 2;
    
    private static final String[] QUERIES = new String[] {
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT t FROM jdo.Master t",
        "SELECT types FROM jdo.TestTypes types WHERE id = $(integer)1",
        "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyGroup object WHERE id = $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE id = $1",
        "SELECT master FROM jdo.TestMaster master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT master FROM jdo.TestMaster master WHERE master.details.value1=$1",
        "SELECT m FROM jdo.TestMaster m WHERE m.details.details2.value1=$1",
        "SELECT master FROM jdo.TestMaster master WHERE master.group=$1",
        "SELECT master FROM jdo.TestMaster master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT master FROM jdo.TestMasterKeyGen master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT master FROM jdo.TestMasterKeyGen master WHERE master.details.value1=$1",
        "SELECT m FROM jdo.TestMasterKeyGen m WHERE m.details.details2.value1=$1",
        "SELECT master FROM jdo.TestMasterKeyGen master WHERE master.group=$1",
        "SELECT master FROM jdo.TestMasterKeyGen master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT t FROM jdo.TestNestedFields t WHERE id = $1",
        "SELECT t FROM jdo.TestNestedFields t WHERE nested2.nested3.value2 = $1",
        "SELECT t FROM jdo.TestOqlExtends t",
        "SELECT t FROM jdo.TestGroup t",
        "SELECT t FROM jdo.TestOqlExtends2 t",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT t FROM jdo.TestPersistent t",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT t FROM jdo.TestOqlExtends t WHERE t.group.id=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT t FROM jdo.TestPersistent t WHERE t.creationTime<=$1",
        "SELECT t FROM jdo.TestOqlExtends t WHERE t.list.id=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT object FROM jdo.TestMaxObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestMaxExtends ext WHERE id = $1",
        "SELECT object FROM jdo.TestHighLowObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestHighLowExtends ext WHERE id = $1",
        "SELECT object FROM jdo.TestUuidObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestUuidExtends ext WHERE id = $1",
        "SELECT object FROM jdo.TestIdentityObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestIdentityExtends ext WHERE id = $1",
        "SELECT p FROM jdo.TestPersistent p WHERE id=$1",
        "SELECT g FROM jdo.TestGroup g",
        "SELECT r FROM jdo.TestPersistRelated r",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestObjectTimeStampable p WHERE id=$1",
        "SELECT p FROM jdo.TestObjectTimeStampable p WHERE id=$1",
        "select obj from jdo.TestObject2 obj",
        "select obj from jdo.TestObject2 obj",
        "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1"
    };
    
    private static Hashtable _tokenTypes = new Hashtable();
    
    static {
        _tokenTypes.put(new Integer(TokenType.END_OF_QUERY), "END_OF_QUERY");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_SELECT), "KEYWORD_SELECT");
        _tokenTypes.put(new Integer(TokenType.IDENTIFIER), "IDENTIFIER");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_AS), "KEYWORD_AS");
        _tokenTypes.put(new Integer(TokenType.COLON), "COLON");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_FROM), "KEYWORD_FROM");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_IN), "KEYWORD_IN");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_WHERE), "KEYWORD_WHERE");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_OR), "KEYWORD_OR");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_AND), "KEYWORD_AND");
        _tokenTypes.put(new Integer(TokenType.EQUAL), "EQUAL");
        _tokenTypes.put(new Integer(TokenType.NOT_EQUAL), "NOT_EQUAL");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_LIKE), "KEYWORD_LIKE");
        _tokenTypes.put(new Integer(TokenType.LT), "LT");
        _tokenTypes.put(new Integer(TokenType.LTE), "LTE");
        _tokenTypes.put(new Integer(TokenType.GT), "GT");
        _tokenTypes.put(new Integer(TokenType.GTE), "GTE");
        _tokenTypes.put(new Integer(TokenType.PLUS), "PLUS");
        _tokenTypes.put(new Integer(TokenType.MINUS), "MINUS");
        _tokenTypes.put(new Integer(TokenType.CONCAT), "CONCAT");
        _tokenTypes.put(new Integer(TokenType.TIMES), "TIMES");
        _tokenTypes.put(new Integer(TokenType.DIVIDE), "DIVIDE");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_MOD), "KEYWORD_MOD");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_ABS), "KEYWORD_ABS");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_NOT), "KEYWORD_NOT");
        _tokenTypes.put(new Integer(TokenType.LPAREN), "LPAREN");
        _tokenTypes.put(new Integer(TokenType.RPAREN), "RPAREN");
        _tokenTypes.put(new Integer(TokenType.DOLLAR), "DOLLAR");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_NIL), "KEYWORD_NIL");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_UNDEFINED), "KEYWORD_UNDEFINED");
        _tokenTypes.put(new Integer(TokenType.BOOLEAN_LITERAL), "BOOLEAN_LITERAL");
        _tokenTypes.put(new Integer(TokenType.LONG_LITERAL), "LONG_LITERAL");
        _tokenTypes.put(new Integer(TokenType.DOUBLE_LITERAL), "DOUBLE_LITERAL");
        _tokenTypes.put(new Integer(TokenType.CHAR_LITERAL), "CHAR_LITERAL");
        _tokenTypes.put(new Integer(TokenType.STRING_LITERAL), "STRING_LITERAL");
        _tokenTypes.put(new Integer(TokenType.DATE_LITERAL), "DATE_LITERAL");
        _tokenTypes.put(new Integer(TokenType.TIME_LITERAL), "TIME_LITERAL");
        _tokenTypes.put(new Integer(TokenType.TIMESTAMP_LITERAL), "TIMESTAMP_LITERAL");
        _tokenTypes.put(new Integer(TokenType.KEYWORD_BETWEEN), "KEYWORD_BETWEEN");
    }
    
    public void testParser() throws Exception {
        try {
            String query = "select o from Product o where o.xyz=$1 order by o.id";
            Lexer lexer = new Lexer(query);

            Parser parser = new Parser(lexer);
            ParseTreeNode theTree = parser.getParseTree();
            System.out.println(treeToString(theTree, NODE_TYPES));
            System.out.println(treeToString(theTree, NODE_VALUES));
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    public void testParserPerformance() throws Exception {
        try {
            long start = System.currentTimeMillis();
            
            for (int i = 0; i < QUERIES.length; i++) {
                Lexer lexer = new Lexer(QUERIES[i]);
                Parser parser = new Parser(lexer);
                parser.getParseTree();
            }
            
            long stop = System.currentTimeMillis();
            
            System.out.println("Parsed " + QUERIES.length + " queries in "
                    + (stop - start) + " milliseconds");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    /**
     * Returns a string representation of the tree using lisp tree notation.  
     * (A, B, C, D) means a root a with 
     * children B, C, and D.  (A, (B, C, D), E) means A with a child B who has 
     * children C and D, and another child E (of A).
     *
     * @param theTree the Tree to convert to a string
     * @param printWhat should be one of the static members NODE_TYPES or 
     *        NODE_VALUES to tell the method what to write in the string.
     * @return a string as described above.
     */
    public String treeToString(final ParseTreeNode theTree, final int printWhat) {
        String retVal = "";
        
        Token curToken = theTree.getToken();
        
        if (printWhat == NODE_TYPES) {
            retVal = (String) _tokenTypes.get(new Integer(curToken.getTokenType()));
        } else {
            retVal = curToken.getTokenValue();
        }
        
        if (!theTree.isLeaf()) {
            retVal = "( " + retVal;
            for (Iterator iter = theTree.children(); iter.hasNext();) {
                retVal = retVal + " , "
                       + treeToString((ParseTreeNode) iter.next(), printWhat);
            }
            retVal = retVal + " )";
        }
        
        return retVal;
    }
}
