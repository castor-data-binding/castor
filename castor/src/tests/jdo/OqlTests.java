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


import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;


/**
 * A suite of general tests for the Castor JDO OQL engine. This test case may
 * grow out of control trying to cover all the supported QL syntax, 
 * especially in the new impl. If/when this happens, we'll just split it up 
 * into multiple test cases. 
 *
 * @author <a href="mailto:ferret@frii.com">Bruce Snyder</a>
 */
public class OqlTests extends CastorTestCase {

    private JDOCategory    _category;

    private Database       _db;

    private OQLQuery       _oql;

    private QueryResults   _results;

    public OqlTests( TestHarness category ) 
    {
        super( category, "TC61", "OQL-supported syntax" );
        _category = (JDOCategory) category;
    }

    public void setUp()
            throws PersistenceException 
    {
        _db = _category.getDatabase( verbose );
    }

    /*
     * @todo Populate the database
     */
    public void runTest()
       throws PersistenceException, Exception 
    {
        // The following statement only prints in -verbose mode
        stream.println( "Not yet implemented" );

        // Populate the database here rather than in setUp(). The setUp()
        // method is run once before each test (and tearDown() is run once
        // after each test and I don't think that we want to populate/truncate
        // the database for each test. We're just selecting the data we're not
        // manipulating it. 

        populateDatabase();
    }

    /*
     * This method will truncate everything from the database and then
     * repopulate it. It needs to be generic enough to work across databases
     * so I would prefer to use straight JDBC calls. 
     */
    public void populateDatabase()
    {}

    /*
     * Test many different variations of the basic SELECT statement.
     */
    public void testBasicSelect()
    {}

    /*
     * Test the ORDER BY clause.
     */
    public void testOrderBy()
    {}

    /*
     * Test the GROUP BY clause.
     */
    public void testGroupBy()
    {}

    /*
     * Test the sub-select support
     * 
     * @todo Implement the sub-select support in the new OQL engine
     */
    public void testBasicSubSelect()
    {}

    /*
     * @see The <a href="http://www.castor.org/nested-attr.html">Nested Attributes</a> doc
     */
    public void testSelectWithNestedAttrs()
    {}

    /*
     * Test the LIMIT clause
     *
     * @todo How should we implement database specific implemenations?
     */
    public void testSelectWithLimitClause()
    {}

    /*
     * Test SQL functions (e.g. count(), max(), first(), last(), avg(), etc.).
     */
    public void testSelectWithFunctions()
    {}

    /*
     * @todo Truncate the database
     */
    public void tearDown()
        throws PersistenceException 
    {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}
