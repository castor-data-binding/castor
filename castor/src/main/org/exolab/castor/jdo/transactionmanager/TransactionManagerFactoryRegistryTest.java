package org.exolab.castor.jdo.transactionmanager;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Properties;

import junit.framework.TestCase;

import org.exolab.castor.util.Logger;

/*
 * JUnit test case for unit testing TransactionManagerFactoryRegistry.
 * @author <a href="werner.guttmann@gmx.net">Werner Guttmann</a>
 *
 */
public class TransactionManagerFactoryRegistryTest 
	extends TestCase 
{

	private PrintWriter writer = null;

	/**
	 * Constructor for TransactionManagerFactoryRegistryTest.
	 * @param arg0
	 */
	public TransactionManagerFactoryRegistryTest(String arg0) {
		super(arg0);
	}

	public void testGetTransactionManagerFactory() 
		throws Exception 
	{
		TransactionManagerFactory localFactory =
			TransactionManagerFactoryRegistry.getTransactionManagerFactory ("local");

		assertEquals("equals", "local", localFactory.getName());
					
		TransactionManagerFactory jndiFactoryNoParams =
			TransactionManagerFactoryRegistry.getTransactionManagerFactory ("jndi");

		assertEquals("equals", "jndi", jndiFactoryNoParams.getName());
		assertNull("params == null", jndiFactoryNoParams.getParams());
			
		TransactionManagerFactory jndiFactory =
			TransactionManagerFactoryRegistry.getTransactionManagerFactory ("jndi");

		assertEquals("factory name == ", "jndi", jndiFactory.getName());
		
		Collection factories = TransactionManagerFactoryRegistry.getTransactionManagerFactories();
		
		assertNotNull ("At least one transaction manager factory", factories);
		// assertEquals ("2 transaction manager factories", 2, factories.size());
		
		String[] factoryNames = TransactionManagerFactoryRegistry.getTransactionManagerFactoryNames();
		for (int i = 0; i < factoryNames.length; i++) {
			writer.println (factoryNames[i]);
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		writer = new Logger( System.out ).setPrefix( "test" );
		
		Properties params = new Properties ();
		params.put ("jndiENC", "comp:java/transactionManager");
	}

}
