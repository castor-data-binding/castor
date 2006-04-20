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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 */


package jdo;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.engine.SQLTypeConverters;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;

import junit.framework.TestCase;


/**
 * Unit test for SQLTypes.
 */
public class TestSQLTypes 
    extends TestCase
{
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log log = LogFactory.getFactory().getInstance(TestSQLTypes.class);
    
    /**
	 * @param arg0
	 */
	public TestSQLTypes(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

    /**
     * Simple test to convert a java.sql.Timestamp to java.util.Date
     */
    public void testTimestamp2Date()
            throws PersistenceException, MappingException {

        DateFormat format = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.SSS");
        Timestamp timeStamp = new Timestamp (new java.util.Date().getTime());
        log.debug ("time stamp = " + format.format (timeStamp));
        TypeConvertor convertor = SQLTypeConverters.getConvertor(Timestamp.class, java.util.Date.class);
        java.util.Date date = (java.util.Date) convertor.convert(timeStamp, null);
        log.debug("date = " + format.format(date));
        
        assertEquals(timeStamp.getTime(), date.getTime());
        
    }

    /**
     * Simple test to convert a java.sql.Timestamp to java.util.Date
     */
    public void testDate2Timestamp()
    throws PersistenceException, MappingException {

        DateFormat format = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.SSS");
        java.util.Date date = new java.util.Date();
        log.debug("date = " + format.format(date));
        TypeConvertor convertor = SQLTypeConverters.getConvertor(java.util.Date.class, Timestamp.class);
        Timestamp timeStamp = (Timestamp) convertor.convert(date, null);
        log.debug ("time stamp = " + format.format (timeStamp));
        
        assertEquals(timeStamp.getTime(), date.getTime());
    }

}

