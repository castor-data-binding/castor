/**
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


package xml.xml2java;

import java.io.*;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import junit.framework.*;
import junit.extensions.*;

/**
 *  Here we try to test the behavior of Castor when the unmarshaller 
 *  has to deal with min/max Occurs attributes. For each situation 
 *  there is a corresponding schema.
 *
 * @author <a href="mailto:victoor@intalio.com">Alexandre Victoor</a>
 * @version $Revision$ $Date$
 */

 public class OccursTest extends TestCase {
   
    Unmarshaller unmarshaller;
    Object    test_obj;

   
        
   public OccursTest(String name) {
        super(name);
    }
    
    
    /**
    *   test minOccurs when the tag is not present in the XML file
    */
    public void testMinOccurs()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinOccurs.class);
        test_obj = (MinOccurs) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minOccurs.xml" ) );
    }
    catch (org.exolab.castor.xml.ValidationException excep ) {
        
        // this is expected
        return;
    }
    catch (org.exolab.castor.xml.MarshalException excep ) {
        
        // this is expected
        return;
        
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
        
    fail("Should have raised an exception");
     
    }

    /**
    *   test minOccurs when the tag is present but not enough times
    */
    public void testMinOccurs2()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinOccurs2.class);
        test_obj = (MinOccurs2) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minOccurs2.xml" ) );
    }
    catch (org.exolab.castor.xml.ValidationException excep ) {
        
        // this is expected
        return;
    }
    catch (org.exolab.castor.xml.MarshalException excep ) {
        
        // this is expected
        return;
        
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    
    fail("Should have raised an exception");
    
    }

 /*
    public void testMinOccurs3()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinOccurs2.class);
        test_obj = (MinOccurs2) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minOccurs3.xml" ) );
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    }
 */
    /**
     *   test where no exception should be raised
     */
    public void testMinOccurs4()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinOccurs2.class);
        test_obj = (MinOccurs2) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minOccurs4.xml" ) );
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    }   

    /**
    *    test where no exception should be raised involving the unbound value for the maxoccur attribute
    */
    public void testMinOccurs5()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinOccurs3.class);
        test_obj = (MinOccurs3) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minOccurs5.xml" ) );
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    }   


    /**
    *    test where there are too much tags
    */
    public void testMaxOccurs()
    {
    try
    {
        unmarshaller = new Unmarshaller(MaxOccurs.class);
        test_obj = (MaxOccurs) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/maxOccurs.xml" ) );
    }
    catch (org.exolab.castor.xml.ValidationException excep ) {
        
        // this is expected
        return;
    }
    catch (org.exolab.castor.xml.MarshalException excep ) {
        
        // this is expected
        return;
        
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    
    fail("Should have raised an exception");
    }

    /**
    *    test where no exception should be raised
    */
    public void testMaxOccurs2()
    {
    try
    {
        unmarshaller = new Unmarshaller(MaxOccurs2.class);
        test_obj = (MaxOccurs2) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/maxOccurs2.xml" ) );
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    }


    /** 
    *   test where no exception should be raised using both minOccurs and maxOccurs
    */
    public void testMinMaxOccurs()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinMaxOccurs.class);
        test_obj = (MinMaxOccurs) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minmaxOccurs.xml" ) );
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    /**
    *    maxOccurs attribute not respected
    */
    public void testMinMaxOccurs2()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinMaxOccurs.class);
        test_obj = (MinMaxOccurs) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minmaxOccurs2.xml" ) );
    }
    catch (org.exolab.castor.xml.ValidationException excep ) {
        
        // this is expected
        return;
    }
    catch (org.exolab.castor.xml.MarshalException excep ) {
        
        // this is expected
        return;
        
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    
    /**
    *    minOccurs attribute not respected
    */
    public void testMinMaxOccurs3()
    {
    try
    {
        unmarshaller = new Unmarshaller(MinMaxOccurs.class);
        test_obj = (MinMaxOccurs) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/minmaxOccurs3.xml" ) );
    }
    catch (org.exolab.castor.xml.ValidationException excep ) {
        
        // this is expected
        return;
    }
    catch (org.exolab.castor.xml.MarshalException excep ) {
        
        // this is expected
        return;
        
    }
    catch (Exception excep) {
            fail("error: " + excep);
        }
    }


}