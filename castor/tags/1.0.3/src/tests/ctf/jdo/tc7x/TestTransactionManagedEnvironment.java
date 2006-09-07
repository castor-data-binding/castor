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
*/

/*
* Created 29th April 2004
* Author: Patrick van Kann (patrick.vankann@fortune-cookie.com)
*/
package ctf.jdo.tc7x;

import harness.CastorTestCase;
import harness.TestHarness;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import jdo.JDOJ2EECategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.mockejb.jndi.MockContextFactory;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class TestTransactionManagedEnvironment extends CastorTestCase {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            TestTransactionManagedEnvironment.class);

    private JDOJ2EECategory _category;

    /** Database instance used in this test case. */
    private Database _db;
     
    /** Initial jndi context to bind to/retrieve from UserTransaction instance */ 
    private Context _context;
    
    /**
     * Creates an instance of this test case.
     * @param name Name of this test case.
     */
    public TestTransactionManagedEnvironment(final String name) {
        super (name);
    }
    
    /**
     * Craetes an instance of this class.
     * @param category - this should be a fully configured 
     * JDOJ2EECategory that features all of the J2EE
     * resources.
     */
    public TestTransactionManagedEnvironment(final TestHarness category) {
        super(category, "TC59", "Test Transaction Managed Environment");
        _category = (JDOJ2EECategory) category;
    }

    public void setUp() throws Exception {
        super.setUp();
        
        /*
        JDOJ2EECategory uses MockEJB to 
        register resources in JNDI.
        We need to create a Context to allow 
        this test class to access these resources
        */
        
        //set the MockContext as the initial context factory
        MockContextFactory.setAsInitial();
        // create the initial context 
        _context = new InitialContext();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Calls the individual tests embedded in this test case
     * @throws Exception A general exception.
     */
    public void runTest() throws Exception {
        testOQL();
    }

    public void testOQL() throws Exception {
        OQLQuery            oql;
        QueryResults        enumeration;
        UserTransaction     ut;
        
        try {   
            //obtain the UserTransaction from JNDI
            //this has been created in the JDOJ2EECategory
            ut = (UserTransaction) _context.lookup("java:/UserTransaction");
            //begin the transaction
            ut.begin();
            //get database - this should be bound to the transaction above
            _db = _category.getDatabase(verbose);
            //execute some test OQL
            oql = _db.getOQLQuery(
                    "SELECT master FROM " + Master.class.getName() + " master");    
            enumeration = oql.execute();
            while (enumeration.hasMore()) { enumeration.next(); }
            //commit the transaction
            ut.commit();
        } catch (TransactionNotInProgressException e) {
            LOG.error (e.getClass().getName(), e);
            //if this exception occurs, the JDO
            //failed to correctly aquire the transaction 
            //from the TransactionManager.
            fail("Transaction not aquired");
        } finally {
            //try and close the database
            try {
                _db.close();
            } catch (Exception e) {
                throw new Exception("Couldn't close database: " + e.getMessage());
            }
        }
    }
}
