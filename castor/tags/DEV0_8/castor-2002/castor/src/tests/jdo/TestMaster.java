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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.util.Vector;
import java.util.Enumeration;


/**
 * Test object mapping to test_master used to conduct relation tests.
 */
public class TestMaster
{


    private int        _id;


    private String     _value;


    private TestGroup  _group;


    private Vector     _details;


    static final int       DefaultId = 3;


    static final String    DefaultValue = "master";


    public TestMaster()
    {
        _id = DefaultId;
        _value = DefaultValue;
        _details = new Vector();
    }


    public void setId( int id )
    {
        _id = id;
    }


    public int getId()
    {
        return _id;
    }


    public void setValue( String value )
    {
        _value = value;
    }


    public String getValue()
    {
        return _value;
    }


    public void setGroup( TestGroup group )
    {
        _group = group;
    }


    public TestGroup getGroup()
    {
        return _group;
    }


    public TestGroup createGroup()
    {
        return new TestGroup();
    }


    public void setDetail( TestDetail detail )
    {
        _details.addElement( detail );
    }

    public Enumeration getDetail()
    {
        return _details.elements();
    }


    public TestDetail createDetail()
    {
        return new TestDetail();
    }


    public String toString()
    {
        String details = "";

        for ( int i = 0 ; i < _details.size() ; ++i ) {
            if ( i > 0 )
                details = details + ", ";
            details = details + _details.elementAt( i ).toString();
        }
        return _id + " / " + _value + " (" + _group + ") { " + details + " }";
    }


}
