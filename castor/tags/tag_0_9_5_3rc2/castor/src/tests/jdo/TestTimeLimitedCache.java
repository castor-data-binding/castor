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
 * $Id: LRU.java
 */

package jdo;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.persist.cache.Cache;
import org.exolab.castor.persist.cache.TimeLimited;

import junit.framework.TestCase;

/**
 * @author <a href="werner DOT guttmann AT gmx DOT com">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class TestTimeLimitedCache 
    extends TestCase 
{
	
	/**
	 * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
	 * Commons Logging</a> instance used for all logging.
	 */
	private static Log log = null;
	
	/**
	 * Constructor for TimeLimitedTest.
	 * @param arg0
	 */
	public TestTimeLimitedCache(String arg0) {
		super(arg0);
	}
	
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		log = LogFactory.getFactory().getInstance (TestTimeLimitedCache.class);
		
	}
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCache () throws Exception {
		TimeLimited cl = new TestTimeLimited(5);
		log.debug( "<empty>" );
		
		printElements (cl);
		
		log.debug( "</empty>" );

		log.debug( "<put(a,a)>" );
		cl.put("a","#a");
		printElements (cl);
		Thread.sleep(1000);
		log.debug( "\n</put(a,a)>" );


		log.debug( "<put[a,b,c,d,e,f,g,h]>" );
		cl.put("a","#a"); 
		log.debug( "put a" );
		Thread.sleep(10);

		printElements (cl);
		
		cl.put("b","#b"); 
		log.debug( "put b" );
		Thread.sleep(100);

		printElements (cl);
		
		cl.put("c","#c"); 
		log.debug( "put c" );
		Thread.sleep(1000);

		printElements (cl);
		
		cl.put("d","#d"); 
		log.debug( "put d" );
		Thread.sleep(500);
		
		printElements (cl);

		cl.put("e","#e"); log.debug( "put e" );
		Thread.sleep(1000);
		cl.put("f","#f"); log.debug( "put f" );
		Thread.sleep(1000);
		cl.put("g","#g"); log.debug( "put g" );
		Thread.sleep(1000);
		cl.put("h","#h"); log.debug( "put h" );
		Thread.sleep(500);
		
		printElements (cl);

		for (int i=0; i<10; i++ ) {
			Thread.sleep(1000);
			printElements (cl);
		}

		log.debug( "\n</put[a,b,c,d,e,f,g,h]>" );

	}
	
	private void printElements (Cache cache) {
		Enumeration e = cache.elements();
		StringBuffer buffer = new StringBuffer ();
		while (e.hasMoreElements()) {
			buffer.append ( e.nextElement() + "\t" );
		}
		log.debug (buffer.toString());
	}

	
	/**
	 * Test is an inner class for whitebox testing.
	 */
	 class TestTimeLimited extends TimeLimited {
	 	
	 	public TestTimeLimited(int count) {
	 		super(count);
	 	}
	 	
	 	protected void dispose(Object o) {
	 		Enumeration e = this.elements();
	 		StringBuffer buffer = new StringBuffer();
	 		log.debug ("disposing: " + o + "\n");
	 		log.debug ("list after disposed: " );
	 		while ( e.hasMoreElements() ) {
	 			buffer.append (e.nextElement() + "\t" );
	 		}
	 		
	 		log.debug (buffer.toString());
	 		
	 		super.dispose(o);
	 	}
	 }
}
