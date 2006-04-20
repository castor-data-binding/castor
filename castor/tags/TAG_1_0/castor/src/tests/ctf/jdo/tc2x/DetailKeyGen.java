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

/**
 * Test object mapping to test_detaul used to conduct relation tests.
 */
public final class DetailKeyGen {
    public static final int DEFAULT_ID = 5;
    public static final String DEFAULT_VALUE = "group";

    private int _id;
    private String _value;
    private MasterKeyGen _master;
    private ArrayList _details2;
    private DetailKeyGen3 _detail3;

    public DetailKeyGen(final int id) {
        this();
        _id = id;
    }

    public DetailKeyGen() {
        _value = DEFAULT_VALUE;
        _details2 = new ArrayList();
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public void setMaster(final MasterKeyGen master) { _master = master; }
    public MasterKeyGen getMaster() { return _master; }

    public DetailKeyGen2 createDetail2() { return new DetailKeyGen2(); }
    public void setDetails2(final ArrayList list) { _details2 = list; }
    public ArrayList getDetails2() { return _details2; }
    public void addDetail2(final DetailKeyGen2 detail2) {
        _details2.add(detail2);
        detail2.setDetail(this);
    }
    public DetailKeyGen2 findDetail2(final int id) {
        Iterator enumeration;
        DetailKeyGen2 detail2;

        if (_details2 == null) { return null; }

        enumeration = _details2.iterator();
        while (enumeration.hasNext()) {
            detail2 = (DetailKeyGen2) enumeration.next();
            if (detail2.getId() == id) { return detail2; }
        }
        return null;
    }

    public void setDetail3(final DetailKeyGen3 detail3) {
        if (_detail3 != null) { _detail3.setDetail(null); }
        if (detail3 != null) { detail3.setDetail(this); }
        _detail3 = detail3;
    }
    public DetailKeyGen3 getDetail3() { return _detail3; }

    public String toString() {
        String details2 = "";

        if (_details2 != null) {
            for (int i = 0; i < _details2.size(); ++i) {
                if (i > 0) { details2 = details2 + ", "; }
                details2 = details2 + _details2.get(i).toString();
            }
        }
        
        return "<detail: " + _id + " / " + _value + " / "
                + (_master == null ? 0 : _master.getId()) + " / { " + details2
                + " }" + "/" + _detail3 + ">";
    }

    public int hashCode() { return _id; }

    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other != null && other instanceof DetailKeyGen) {
            return (((DetailKeyGen) other)._id == _id);
        }
        return false;
    }
}
