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

package ctf.jdo.tc1x;

import java.math.BigDecimal;

/**
 * Test type conversion.
 */
public final class TypeConversion {
    public static final int    DEFAULT_ID = 4;

    private int         _id;
    private boolean     _boolByte;
    private boolean     _boolShort;
    private boolean     _boolShortMinus;
    private boolean     _boolInt;
    private boolean     _boolIntMinus;
    private boolean     _boolBigdec;
    private boolean     _boolBigdecMinus;
    private byte        _byteInt;
    private short       _shortInt;
    private long        _longInt;
    private double      _doubleInt;
    private float       _floatInt;
    private byte        _byteBigdec;
    private short       _shortBigdec;
    private int         _intBigdec;
    private float       _floatBigdec;
    private double      _doubleBigdec;
    private short       _shortString;
    private byte        _byteString;
    private int         _intString;
    private long        _longString;
    private BigDecimal  _bigdecString;
    private float       _floatString;
    private double      _doubleString;

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setBoolByte(final boolean boolByte) { _boolByte = boolByte; }
    public boolean getBoolByte() { return _boolByte; }

    public void setBoolShort(final boolean boolShort) { _boolShort = boolShort; }
    public boolean getBoolShort() { return _boolShort; }

    public void setBoolShortMinus(final boolean boolShortMinus) {
        _boolShortMinus = boolShortMinus;
    }
    public boolean getBoolShortMinus() { return _boolShortMinus; }

    public void setBoolInt(final boolean boolInt) { _boolInt = boolInt; }
    public boolean getBoolInt() { return _boolInt; }

    public void setBoolIntMinus(final boolean boolIntMinus) {
        _boolIntMinus = boolIntMinus;
    }
    public boolean getBoolIntMinus() { return _boolIntMinus; }

    public void setBoolBigdec(final boolean boolBigdec) {
        _boolBigdec = boolBigdec;
    }
    public boolean getBoolBigdec() { return _boolBigdec; }

    public void setBoolBigdecMinus(final boolean boolBigdecMinus) {
        _boolBigdecMinus = boolBigdecMinus;
    }
    public boolean getBoolBigdecMinus() { return _boolBigdecMinus; }

    public void setByteInt(final byte byteInt) { _byteInt = byteInt; }
    public byte getByteInt() { return _byteInt; }

    public void setShortInt(final short shortInt) { _shortInt = shortInt; }
    public short getShortInt() { return _shortInt; }

    public void setLongInt(final long longInt) { _longInt = longInt; }
    public long getLongInt() { return _longInt; }

    public void setDoubleInt(final double doubleInt) { _doubleInt = doubleInt; }
    public double getDoubleInt() { return _doubleInt; }

    public void setFloatInt(final float floatInt) { _floatInt = floatInt; }
    public float getFloatInt() { return _floatInt; }

    public void setByteBigdec(final byte byteBigdec) { _byteBigdec = byteBigdec; }
    public byte getByteBigdec() { return _byteBigdec; }

    public void setShortBigdec(final short shortBigdec) { _shortBigdec = shortBigdec; }
    public short getShortBigdec() { return _shortBigdec; }

    public void setIntBigdec(final int intBigdec) { _intBigdec = intBigdec; }
    public int getIntBigdec() { return _intBigdec; }

    public void setFloatBigdec(final float floatBigdec) {
        _floatBigdec = floatBigdec;
    }
    public float getFloatBigdec() { return _floatBigdec; }

    public void setDoubleBigdec(final double doubleBigdec) {
        _doubleBigdec = doubleBigdec;
    }
    public double getDoubleBigdec() { return _doubleBigdec; }

    public void setShortString(final short shortString) {
        _shortString = shortString;
    }
    public short getShortString() { return _shortString; }

    public void setByteString(final byte byteString) { _byteString = byteString; }
    public byte getByteString() { return _byteString; }

    public void setIntString(final int intString) { _intString = intString; }
    public int getIntString() { return _intString; }

    public void setLongString(final long longString) { _longString = longString; }
    public long getLongString() { return _longString; }

    public void setBigdecString(final BigDecimal bigdecString) {
        _bigdecString = bigdecString;
    }
    public BigDecimal getBigdecString() { return _bigdecString; }

    public void setFloatString(final float floatString) {
        _floatString = floatString;
    }
    public float getFloatString() { return _floatString; }

    public void setDoubleString(final double doubleString) {
        _doubleString = doubleString;
    }
    public double getDoubleString() { return _doubleString; }
}
