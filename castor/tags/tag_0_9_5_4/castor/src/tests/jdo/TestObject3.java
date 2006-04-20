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


/**
 * Test object mapping to test_table used to conduct all the tests.
 */
public class TestObject3
{


    private long   _id;


    private String _value1;


    private String _value2;


    private long   _number;


    static final long      DefaultId = 3;


    static final String    DefaultValue1 = "one";


    static final String    DefaultValue2 = "two";


    static final long      DefaultNumber = 5;


    public TestObject3()
    {
        _id = DefaultId;
        _value1 = DefaultValue1;
        _value2 = DefaultValue2;
        _number = DefaultNumber;
    }


    public void setId( long id )
    {
        _id = id;
    }


    public long getId()
    {
        return _id;
    }


    public void setValue1( String value1 )
    {
        _value1 = value1;
    }


    public String getValue1()
    {
        return _value1;
    }


    public void setValue2( String value2 )
    {
        _value2 = value2;
    }


    public String getValue2()
    {
        return _value2;
    }


    public long getNumber()
    {
        return _number;
    }


    public void setNumber( long number )
    {
        _number = number;
    }


    public String toString()
    {
        return _id + " / " + _value1 + " / " + _value2 + " / " + _number;
    }


}
