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


package jdo;


import java.math.BigDecimal;


/**
 * Test tyoe handling against test_types.
 */
public class TestTypes
{


    private long           _id;


    private java.util.Date _date;


    private java.util.Date _time;


    private java.util.Date _timestamp;


    private int            _intValue;


    private boolean        _intExists = false;


    private Long           _longValue;


    private char           _charValue;


    private boolean        _boolValue;


    private double         _doubleValue;


    private java.util.Date _date2;


    private java.util.Date _time2;


    private java.util.Date _timestamp2;


    static final int       DefaultId = 3;


    public TestTypes()
    {
        _id = DefaultId;
        _date = new java.util.Date();
        _time = new java.util.Date();
        _timestamp = new java.util.Date();
        _date2 = new java.util.Date();
        _time2 = new java.util.Date();
        _timestamp2 = new java.util.Date();
        _doubleValue = 0.1;
    }


    public void setId( long id )
    {
        _id = id;
    }


    public long getId()
    {
        return _id;
    }


    public void setDate( java.util.Date date )
    {
        _date = date;
    }


    public java.util.Date getDate()
    {
        return _date;
    }


    public void setTime( java.util.Date date )
    {
        _time = date;
    }


    public java.util.Date getTime()
    {
        return _time;
    }


    public void setTimestamp( java.util.Date date )
    {
        _timestamp = date;
    }


    public java.util.Date getTimestamp()
    {
        return _timestamp;
    }


    public void setIntValue( int value )
    {
        _intValue = value;
        _intExists = true;
    }


    public int getIntValue()
    {
        return _intValue;
    }


    public boolean hasIntValue()
    {
        return _intExists;
    }


    public void deleteIntValue()
    {
        _intExists = false;
    }


    public void setLongValue( Long value )
    {
        _longValue = value;
    }


    public Long getLongValue()
    {
        return _longValue;
    }


    public char getCharValue()
    {
        return _charValue;
    }


    public void setCharValue( char value )
    {
        _charValue = value;
    }


    public double getDoubleValue()
    {
        return _doubleValue;
    }


    public void setDoubleValue( double value )
    {
        _doubleValue = value;
    }


    public boolean getBoolValue()
    {
        return _boolValue;
    }


    public void setBoolValue( boolean value )
    {
        _boolValue = value;
    }


    public void setDate2( java.util.Date date )
    {
        _date2 = date;
    }


    public java.util.Date getDate2()
    {
        return _date2;
    }


    public void setTime2( java.util.Date date )
    {
        _time2 = date;
    }


    public java.util.Date getTime2()
    {
        return _time2;
    }


    public void setTimestamp2( java.util.Date date )
    {
        _timestamp2 = date;
    }


    public java.util.Date getTimestamp2()
    {
        return _timestamp2;
    }


    public String toString()
    {
        return "" + _id;
    }


}
