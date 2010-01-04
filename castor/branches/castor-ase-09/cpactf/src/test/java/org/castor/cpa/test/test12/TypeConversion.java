/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.test.test12;

import java.math.BigDecimal;

import org.junit.Ignore;

/**
 * Test type conversion.
 */
@Ignore
public final class TypeConversion {
    public static final int    DEFAULT_ID = 4;

    private int _id;
    private boolean _boolInt;
    private boolean _boolIntMinus;
    private boolean _boolBigdec;
    private boolean _boolBigdecMinus;
    private byte _byteInt;
    private short _shortInt;
    private long _longInt;
    private double _doubleInt;
    private float _floatInt;
    private int _intBigdec;
    private float _floatBigdec;
    private double _doubleBigdec;
    private int _intString;
    private long _longString;
    private BigDecimal _bigdecString;
    private float _floatString;
    private double _doubleString;

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

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
