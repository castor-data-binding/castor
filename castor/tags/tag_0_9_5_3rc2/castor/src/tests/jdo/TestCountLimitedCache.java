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
import org.exolab.castor.persist.cache.CountLimited;

import junit.framework.TestCase;

/**
 * @author Administrator
 */
public class TestCountLimitedCache extends TestCase {
	
	/**
	 * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
	 * Commons Logging</a> instance used for all logging.
	 */
	private static Log log = null;
	
	/**
	 * @param arg0
	 */
	public TestCountLimitedCache(String arg0) {
		super(arg0);
	}
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		log = LogFactory.getFactory().getInstance (TestCountLimitedCache.class);
		
	}
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCache () throws Exception {
		
		CountLimited cl = new CountLimited(3);
        String returnValue =null;
		
        printElements (cl, "empty");

		returnValue = (String) cl.put("a","#a");
        assertNull(returnValue);
        printElements (cl, "put(a,a)");
        
		cl.put("b","#b");
        assertNull(returnValue);
        printElements (cl, "put(b,b)");
        
		cl.put("c","#c");
        printElements (cl, "put(c,c)");
        assertNull(returnValue);
        
		returnValue = (String) cl.put("d","#d");
        printElements (cl, "put(d,d)");
        assertEquals (returnValue, "#a");

        returnValue = (String) cl.put("c","#c1");
        assertEquals (returnValue, "#c");
        printElements (cl, "put(c,c1)");
        
        returnValue = (String) cl.put("c","#c2");
        assertEquals (returnValue, "#c1");
        printElements (cl, "put(c,c2)");
        
        returnValue = (String) cl.put("c","#c3");
        assertEquals (returnValue, "#c2");
        printElements (cl, "put(c,c3)");

        returnValue = (String) cl.put("b","#b");
        assertEquals (returnValue, "#b");
        printElements (cl, "put(b,b)");

        returnValue = (String) cl.put("e","#e");
        assertEquals ("#b", returnValue);
        printElements (cl, "put(e,e)");

        returnValue = (String) cl.put("f","#f");
        assertEquals ("#c3", returnValue);
        printElements (cl, "put(f,f)");

        returnValue = (String) cl.remove("e");
        assertEquals ("#e", returnValue);
        printElements (cl, "remove(e,e)");

		returnValue = (String) cl.put("g","#g");
        assertEquals ("#d", returnValue);
        printElements (cl, "put(g,g)");

        returnValue = (String) cl.remove("f");
        assertEquals ("#f", returnValue);
        printElements (cl, "remove(f,f)");

        returnValue = (String) cl.remove("b");
        assertNull (returnValue);
        printElements (cl, "remove(b,b)");

        returnValue = (String) cl.remove("g");
        assertEquals ("#g", returnValue);
        printElements (cl, "remove(g,g)");

        returnValue = (String) cl.remove("x");
        assertNull (returnValue);
        printElements (cl, "remove(x,x)");

		returnValue = (String) cl.put("a","#a");
        assertNull(returnValue);
        printElements (cl, "put(a,a)");

		returnValue = (String) cl.put("b","#b");
        assertNull(returnValue);
        printElements (cl, "put(b,b)");

        returnValue = (String) cl.put("c","#c");
        assertNull(returnValue);
        printElements (cl, "put(c,c)");

		returnValue = (String) cl.put("d","#d");
        assertEquals ("#a", returnValue);
        printElements (cl, "put(d,d)");
	}

    /**
     * Helper methd to output state of the cache.
     * @param cache
     */
    private void printElements (Cache cache, String action) {
        Enumeration e = cache.elements();
        StringBuffer buffer = new StringBuffer ();
        buffer.append ("<").append(action).append(">");
        while (e.hasMoreElements()) {
            buffer.append ( e.nextElement());
            if (e.hasMoreElements())
                buffer.append ("\t");
        }
        buffer.append ("</").append(action).append(">");
        log.debug (buffer.toString());
    }
    
	
}
