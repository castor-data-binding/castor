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
 *
 */

import org.castor.xmlctf.CastorTestable;
import org.castor.xmlctf.RandomHelper;
import org.castor.xmlctf.CompareHelper;

public class RandS implements CastorTestable {

    private R      _r;
    private R      _s;
    private int    _t;
    private String _u;

    public void setT(int t) {
        _t = t;
    }

    public int getT() {
        return _t;
    }

    public void setU(String u) {
        _u = u;
    }

    public String getU() {
        return _u;
    }

    public void setR(R r) {
        _r = r;
    }

    public R getR() {
        return _r;
    }

    public void setS(R s) {
        _s = s;
    }

    public R getS() {
        return _s;
    }

    // --- CastorTestable ------------------------

    public boolean equals(Object object) {
        if ( ! (object instanceof RandS)) {
            return false;
        }

        RandS rs = (RandS)object;

        if (!CompareHelper.equals(this.getR(), rs.getR())) return false;
        if (!CompareHelper.equals(this.getS(), rs.getS())) return false;
        if (this.getT() != rs.getT()) return false;
        if (!CompareHelper.equals(this.getU(), rs.getU())) return false;

        return true;
    }

    public String dumpFields() {
        return "[R="
               + ((getR() == null) ? "null" : ((CastorTestable) getR()).dumpFields())
               + "]\n"
               + "[S="
               + ((getS() == null) ? "null" : ((CastorTestable) getS()).dumpFields())
               + "]\n"
               + "[T=" + getT() + "]\n"
               + "[U="
               + ((getU() == null) ? "null" : getU()) + "]\n";
    }

    public void randomizeFields() throws InstantiationException, IllegalAccessException {
        // Not Implemented
    }

}
