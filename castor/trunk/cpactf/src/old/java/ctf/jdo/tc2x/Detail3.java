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
 */
package ctf.jdo.tc2x;

/**
 * Test object mapping to test_detail2 used to conduct relation tests.
 */
public final class Detail3 {
    public static final int DEFAULT_ID = 100;
    public static final String DEFAULT_VALUE = "value";

    private int _id;
    private String _value;
    private Detail _detail;

    public Detail3(final int id) {
        _id = id;
        _value = DEFAULT_VALUE;
    }

    public Detail3() {
        _value = DEFAULT_VALUE;
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public void setDetail(final Detail detail) { _detail = detail; }
    public Detail getDetail() { return _detail; }

    public String toString() {
        return _id + " / " + _value + " / "
                + (_detail == null ? 0 : _detail.getId());
    }

    public int hashCode() { return _id; }

    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other != null && other instanceof Detail3) {
            return (((Detail3) other)._id == _id);
        }
        return false;
    }
}
