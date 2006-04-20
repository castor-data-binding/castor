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


import java.util.HashSet;
import java.util.Iterator;
import org.exolab.castor.jdo.TimeStampable;


/**
 * Test "set" collection.
 * Only for JDK 1.2
 */
public class TestSetMaster {

    private int         _id;

    private HashSet     _details;
    
    static final int    DefaultId = 3;

    public TestSetMaster()
    {
        _id = DefaultId;
        _details = new HashSet();
    }


    public void setId( int id )
    {
        _id = id;
    }


    public int getId()
    {
        return _id;
    }

    public void addDetail( TestSetDetail detail )
    {
        _details.add( detail );
        detail.setMaster( this );
    }


    public HashSet getDetails()
    {
        return _details;
    }

    public TestSetDetail createDetail()
    {
        return new TestSetDetail();
    }

    public TestSetDetail findDetail(int id)
    {
        Iterator it;
        TestSetDetail detail;

        it = _details.iterator();
        while ( it.hasNext() ) {
            detail = (TestSetDetail) it.next();
            if ( detail.getId() == id ) {
                return detail;
            }
        }
        return null;
    }

    public String toString()
    {
        Iterator it;
        TestSetDetail detail;
        StringBuffer details = new StringBuffer();

        it = _details.iterator();
        while ( it.hasNext() ) {
            detail = (TestSetDetail) it.next();
            details.append( detail );
            details.append( "," );
        }
        if ( details.length() > 0 ) {
            details.setLength( details.length() - 1 );
        }
        return _id + " { " + details + " }";
    }
}
