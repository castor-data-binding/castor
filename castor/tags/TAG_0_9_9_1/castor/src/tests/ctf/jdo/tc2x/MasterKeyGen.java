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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc2x;

import java.util.ArrayList;
import java.util.Iterator;

import org.exolab.castor.jdo.TimeStampable;

/**
 * Test object mapping to test_master used to conduct relation tests.
 */
public final class MasterKeyGen implements TimeStampable {
    public static final int DEFAULT_ID = 3;
    public static final String DEFAULT_VALUE = "master";

    private int _id;
    private String _value;
    private Group _group;
    private ArrayList _details;
    private long _timeStamp;

    public MasterKeyGen() {
        _id = DEFAULT_ID;
        _value = DEFAULT_VALUE;
        _group = null;
        _details = new ArrayList();
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public Group createGroup() { return new Group(); }
    public void setGroup(final Group group) { _group = group; }
    public Group getGroup() { return _group; }

    public DetailKeyGen createDetail() { return new DetailKeyGen(); }
    public ArrayList getDetails() { return _details; }
    public void setDetails(final ArrayList array) { _details = array; }
    public void addDetail(final DetailKeyGen detail) {
        _details.add(detail);
        detail.setMaster(this);
    }
    public DetailKeyGen findDetail(final int id) {
        Iterator enumeration;
        DetailKeyGen detail;

        enumeration = _details.iterator();
        while (enumeration.hasNext()) {
            detail = (DetailKeyGen) enumeration.next();
            if (detail.getId() == id) { return detail; }
        }
        return null;
    }

    public String toString() {
        String details = "";

        for (int i = 0; i < _details.size(); ++i) {
            if (i > 0) { details = details + ", "; }
            details = details + _details.get(i).toString();
        }
        return _id + " / " + _value + " (" + _group + ") { " + details + " }";
    }

    public void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
    public long jdoGetTimeStamp() { return _timeStamp; }
}
