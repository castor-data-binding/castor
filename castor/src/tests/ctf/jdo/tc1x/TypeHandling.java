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

package ctf.jdo.tc1x;

/**
 * Test tyoe handling against test_types.
 */
public final class TypeHandling {
    public static final int DEFAULT_ID = 3;

    private long            _id;
    private java.util.Date  _date;
    private java.util.Date  _time;
    private java.util.Date  _timestamp;
    private int             _intValue;
    private boolean         _intExists = false;
    private boolean         _boolIsMethod;
    private Long            _longValue;
    private char            _charValue;
    private boolean         _boolValue;
    private float           _floatValue;
    private double          _doubleValue;
    private java.util.Date  _date2;
    private java.util.Date  _date3;
    private java.util.Date  _time2;
    private java.util.Date  _timestamp2;
    private String          _timestamp3;

    public TypeHandling() {
        long time = System.currentTimeMillis();
        // ignore milliseconds. Comment the following line out to see SAP DB bug :-(
        time = time / 1000 * 1000;
        java.util.Date date = new java.util.Date(time);
        _id = DEFAULT_ID;
        _date = date;
        _time = date;
        _charValue = 'A';
        _timestamp = date;
        _date2 = date;
        _date3 = date;
        _time2 = date;
        _timestamp2 = date;
        _timestamp3 = "2001-02-06 02:33:44.555";
    }

    public void setId(final long id) { _id = id; }
    public long getId() { return _id; }

    public void setDoubleValue(final double doubleValue) { _doubleValue = doubleValue; }
    public double getDoubleValue() { return _doubleValue; }

    public void setFloatValue(final float floatValue) { _floatValue = floatValue; }
    public float getFloatValue() { return _floatValue; }

    public void setDate(final java.util.Date date) { _date = date; }
    public java.util.Date getDate() { return _date; }

    public void setTime(final java.util.Date date) { _time = date; }
    public java.util.Date getTime() { return _time; }

    public void setTimestamp(final java.util.Date date) { _timestamp = date; }
    public java.util.Date getTimestamp() { return _timestamp; }

    public void setIntValue(final int value) {
        _intValue = value;
        _intExists = true;
    }
    public int getIntValue() { return _intValue; }
    public boolean hasIntValue() { return _intExists; }
    public void deleteIntValue() { _intExists = false; }

    public void setLongValue(final Long value) { _longValue = value; }
    public Long getLongValue() { return _longValue; }

    public void setCharValue(final char value) { _charValue = value; }
    public char getCharValue() { return _charValue; }

    public void setBoolValue(final boolean value) { _boolValue = value; }
    public boolean getBoolValue() { return _boolValue; }

    public void setDate2(final java.util.Date date) { _date2 = date; }
    public java.util.Date getDate2() { return _date2; }

    public void setDate3(final java.util.Date date) { _date3 = date; }
    public java.util.Date getDate3() { return _date3; }

    public void setTime2(final java.util.Date date) { _time2 = date; }
    public java.util.Date getTime2() { return _time2; }

    public void setTimestamp2(final java.util.Date date) { _timestamp2 = date; }
    public java.util.Date getTimestamp2() { return _timestamp2; }

    public void setTimestamp3(final String timestamp3) { _timestamp3 = timestamp3; }
    public String getTimestamp3() { return _timestamp3; }

    public String toString() {
        return "" + _id;
    }
    
    public void setBoolIsMethod(final boolean boolIsMethod) {
        _boolIsMethod = boolIsMethod;
    }
    
    public boolean isBoolIsMethod() {
        return _boolIsMethod;
    }
}
