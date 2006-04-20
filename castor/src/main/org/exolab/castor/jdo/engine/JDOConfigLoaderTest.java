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

package org.exolab.castor.jdo.engine;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.TransactionDemarcation;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * <Add your own comment here>
 * @author me
 *
 */
public class JDOConfigLoaderTest extends TestCase {
	
	private InputSource source = null;
	private EntityResolver resolver = null;
	private ClassLoader classLoader = null;
	
	private PrintWriter writer = null;

	/**
	 * Constructor for JDOConfigLoaderTest.
	 * @param arg0
	 */
	public JDOConfigLoaderTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		source = new InputSource ("src/examples/jdo/jdo-conf.xml");
		
		writer = new PrintWriter (new FileWriter ("output.log"));
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		
		writer.flush();
		writer.close();
	}

	public void testGetDatabase() 
		throws Exception 
	{
		
		Database database = JDOConfLoader.getDatabase (source, resolver);
		writer.println (database);
	}

	public void testGetTransactionDemarcation() 
		throws Exception 
	{
		TransactionDemarcation transactionDemarcation = JDOConfLoader.getTransactionDemarcation (source, resolver);
		writer.println (transactionDemarcation);
	}

	public void testGetMapping() 
		throws Exception 
	{
		/*
        Mapping mapping = JDOConfLoader.getMapping (source, resolver);
		writer.println(mapping);
        */
	}

	public void testLoadConfigurationLocalMinimal() 
		throws Exception 
	{
		source = new InputSource ("src/examples/jdo/jdo-conf.minimal.xml");
		
		JDOConfLoader.deleteConfiguration();
		
		TransactionDemarcation demarcation = JDOConfLoader.getTransactionDemarcation (source, resolver);
		
		assertNotNull (demarcation);
		assertNull (demarcation.getTransactionManager());
		
	}

	public void testLoadConfigurationGlobalJNDI() 
		throws Exception 
	{
		source = new InputSource ("src/examples/jdo/jdo-conf.global.jndi.xml");
		
		JDOConfLoader.deleteConfiguration();
		
		TransactionDemarcation demarcation = JDOConfLoader.getTransactionDemarcation (source, resolver);
		
		assertNotNull (demarcation);
		assertEquals (demarcation.getMode(), "global");
		assertNotNull (demarcation.getTransactionManager());
		assertEquals (demarcation.getTransactionManager().getName(), "jndi");
		
	}
	
	public void testLoadConfigurationGlobalJNDIWithParams() 
		throws Exception 
	{
		source = new InputSource ("src/examples/jdo/jdo-conf.global.jndi.with-params.xml");
		
		JDOConfLoader.deleteConfiguration();
		
		TransactionDemarcation demarcation = JDOConfLoader.getTransactionDemarcation (source, resolver);
		
		assertNotNull (demarcation);
		assertEquals (demarcation.getMode(), "global");
		assertNotNull (demarcation.getTransactionManager());
		assertEquals (demarcation.getTransactionManager().getName(), "jndi");
		assertEquals (demarcation.getTransactionManager().getParamCount(), 1);
		
	}
	
	public void testLoadConfigurationMissingTransactionDemarcation() 
		throws Exception 
	{
		source = new InputSource ("src/examples/jdo/jdo-conf.missing-demarcation.xml");
		
		JDOConfLoader.deleteConfiguration();
		
		TransactionDemarcation demarcation = JDOConfLoader.getTransactionDemarcation (source, resolver);
		
		assertNull(demarcation);
		
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#run()
	 */
	public static TestSuite suite() {
		
		TestSuite suite = new TestSuite();
		
		suite.addTest (new JDOConfigLoaderTest ("testGetDatabase"));
		suite.addTest (new JDOConfigLoaderTest ("testGetTransactionDemarcation"));
		suite.addTest (new JDOConfigLoaderTest ("testGetMapping"));
		
		suite.addTest (new JDOConfigLoaderTest ("testLoadConfigurationLocalMinimal"));
		suite.addTest (new JDOConfigLoaderTest ("testLoadConfigurationMissingTransactionDemarcation"));

		suite.addTest (new JDOConfigLoaderTest ("testLoadConfigurationGlobalJNDI"));
		suite.addTest (new JDOConfigLoaderTest ("testLoadConfigurationGlobalJNDIWithParams"));
		
		return suite;
	}

}
