/*
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

import org.castor.xmlctf.CastorTestable;
import org.castor.xmlctf.RandomHelper;
import org.castor.xmlctf.CompareHelper;

public class XandZ implements CastorTestable {

    private X      _x;
    private X      _z;
    private String _w;
    private int    _y;
    private RandS  _rs;

    public void setRandS(RandS rs) {
        _rs = rs;
    }

    public RandS getRandS() {
        return _rs;
    }

    public void setX(X x) {
        _x = x;
    }

    public X getX() {
        return _x;
    }

    public void setZ(X z) {
        _z = z;
    }

    public X getZ() {
        return _z;
    }

    public void setY(int y) {
        _y = y;
    }

    public int getY() {
        return _y;
    }

    public void setW(String w) {
        _w = w;
    }

    public String getW() {
        return _w;
    }

    // --- CastorTestable ------------------------

    public boolean equals(Object object) {
        if ( ! (object instanceof XandZ)) {
            return false;
        }

        XandZ xz = (XandZ)object;

        if (!CompareHelper.equals(this.getRandS(), xz.getRandS())) return false;
        if (!CompareHelper.equals(this.getX(), xz.getX())) return false;
        if (!CompareHelper.equals(this.getZ(), xz.getZ())) return false;
        if (this.getY() != xz.getY()) return false;
        if (!CompareHelper.equals(this.getW(), xz.getW())) return false;

        return true;
    }

    public String dumpFields() {
        return  "[RandS=" + ((getRandS() == null)? "null" : ((CastorTestable)getRandS()).dumpFields()) + "]\n" +
                "[X=" + ((getX() == null)? "null" : ((CastorTestable)getX()).dumpFields()) + "]\n" +
                "[Z=" + ((getZ() == null)? "null" : ((CastorTestable)getZ()).dumpFields()) + "]\n" +
                "[Y=" + getY() + "]\n" +
                "[W=" + ((getW() == null)? "null" : getW()) + "]\n";
    }

    public void randomizeFields() throws InstantiationException, IllegalAccessException {
        // Not Implemented
    }

}
