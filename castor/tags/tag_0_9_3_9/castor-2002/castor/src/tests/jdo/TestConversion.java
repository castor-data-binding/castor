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


package jdo;


import java.math.BigDecimal;
import java.sql.Clob;
import java.io.InputStream;


/**
 * Test type conversion.
 */
public class TestConversion
{

    static final int    DefaultId = 4;

    private int         id;

    private boolean     boolByte;

    private boolean     boolShort;

    private boolean     boolInt;

    private boolean     boolBigdec;

    private byte        byteInt;

    private short       shortInt;

    private long        longInt;

    private double      doubleInt;

    private float       floatInt;

    private byte        byteBigdec;

    private short       shortBigdec;

    private int         intBigdec;

    private float       floatBigdec;

    private double      doubleBigdec;

    private short       shortString;

    private byte        byteString;

    private int         intString;

    private long        longString;

    private BigDecimal  bigdecString;

    private float       floatString;

    private double      doubleString;


    public TestConversion()
    {
    }


    public void setId( int id )
    {
        this.id = id;
    }


    public int getId()
    {
        return id;
    }

    public void setBoolByte( boolean boolByte ) {
        this.boolByte = boolByte;
    }

    public boolean getBoolByte() {
        return boolByte;
    }

    public void setBoolShort( boolean boolShort ) {
        this.boolShort = boolShort;
    }

    public boolean getBoolShort() {
        return boolShort;
    }

    public void setBoolInt( boolean boolInt ) {
        this.boolInt = boolInt;
    }

    public boolean getBoolInt() {
        return boolInt;
    }

    public void setBoolBigdec( boolean boolBigdec ) {
        this.boolBigdec = boolBigdec;
    }

    public boolean getBoolBigdec() {
        return boolBigdec;
    }

    public void setByteInt( byte byteInt ) {
        this.byteInt = byteInt;
    }

    public byte getByteInt() {
        return byteInt;
    }

    public void setShortInt( short shortInt ) {
        this.shortInt = shortInt;
    }

    public short getShortInt() {
        return shortInt;
    }

    public void setLongInt( long longInt ) {
        this.longInt = longInt;
    }

    public long getLongInt() {
        return longInt;
    }

    public void setDoubleInt( double doubleInt ) {
        this.doubleInt = doubleInt;
    }

    public double getDoubleInt() {
        return doubleInt;
    }

    public void setFloatInt( float floatInt ) {
        this.floatInt = floatInt;
    }

    public float getFloatInt() {
        return floatInt;
    }

    public void setByteBigdec( byte byteBigdec ) {
        this.byteBigdec = byteBigdec;
    }

    public byte getByteBigdec() {
        return byteBigdec;
    }

    public void setShortBigdec( short shortBigdec ) {
        this.shortBigdec = shortBigdec;
    }

    public short getShortBigdec() {
        return shortBigdec;
    }

    public void setIntBigdec( int intBigdec ) {
        this.intBigdec = intBigdec;
    }

    public int getIntBigdec() {
        return intBigdec;
    }

    public void setFloatBigdec( float floatBigdec ) {
        this.floatBigdec = floatBigdec;
    }

    public float getFloatBigdec() {
        return floatBigdec;
    }

    public void setDoubleBigdec( double doubleBigdec ) {
        this.doubleBigdec = doubleBigdec;
    }

    public double getDoubleBigdec() {
        return doubleBigdec;
    }

    public void setShortString( short shortString ) {
        this.shortString = shortString;
    }

    public short getShortString() {
        return shortString;
    }

    public void setByteString( byte byteString ) {
        this.byteString = byteString;
    }

    public byte getByteString() {
        return byteString;
    }

    public void setIntString( int intString ) {
        this.intString = intString;
    }

    public int getIntString() {
        return intString;
    }

    public void setLongString( long longString ) {
        this.longString = longString;
    }

    public long getLongString() {
        return longString;
    }

    public void setBigdecString( BigDecimal bigdecString ) {
        this.bigdecString = bigdecString;
    }

    public BigDecimal getBigdecString() {
        return bigdecString;
    }

    public void setFloatString( float floatString ) {
        this.floatString = floatString;
    }

    public float getFloatString() {
        return floatString;
    }

    public void setDoubleString( double doubleString ) {
        this.doubleString = doubleString;
    }

    public double getDoubleString() {
        return doubleString;
    }
}
