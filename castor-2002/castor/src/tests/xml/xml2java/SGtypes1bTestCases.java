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
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import junit.framework.*;
import junit.extensions.*;

/**
 *  This class is used to test the unmarshaller and the types that
 *  Castor should support. In the setup, {@link #oneTimeSetUp oneTimeSetUp()}, an xml file,
 *  testtype1b.xml, is unmarshalled. For all the types present in this
 *  file, there is a corresponding method, where the value unmarshalled 
 *  is compared to what is expected.
 *  This class is similar to {@link SGtypes1TestCases} expect that the 
 *  values are different, extrema if they are about numeric types.
 *  <br>
 *  This class needs {@link SGtypes1bTest} in order to be run
 *  <br>
 * @author <a href="mailto:victoor@intalio.com">Alexandre Victoor</a>
 * @version $Revision$ $Date$
 */

 public class SGtypes1bTestCases extends TestCase {
   
    static Unmarshaller unmarshaller;
    static Testtype1    test_obj;

   
        
   public SGtypes1bTestCases(String name) {
        super(name);
    }
    
    
    public static void oneTimeSetUp() {

        try {
           
            unmarshaller = new Unmarshaller(Testtype1.class);
            /*
            test_obj     =
                (Testtype1) unmarshaller
                    .unmarshal(new InputStreamReader(getClass()
                        .getResourceAsStream("testtype1.xml")));
            */
            
            test_obj = (Testtype1) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/testtype1b.xml" ) );
                        
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
  
    
    
    public void testString() {

        try {
            assert("both strings are not equals: " + test_obj.getString()
                   + " and " + "A test text",
                   (test_obj.getString()).equals("A test text"));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }



    public void testDate() {

        try {
            assert("both date are not equals: " + test_obj.getDate()
                   + " and " + "1999-07-22",
                   (((test_obj.getDate()).toString()).equals("1999-07-22")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testTime() {

        try {
            assert("both time are not equals: " + test_obj.getTime()
                   + " and " + "00:00:00.0",
                   (((test_obj.getTime()).toString()).equals("00:00:00.0")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testTimeDuration() {

        try {
            assert("both timeDuration are not equals: "
                   + test_obj.getTimeDuration() + " and " + "P1Y2M3DT10H30M",
                   (((test_obj.getTimeDuration()).toString())
                       .equals("P1Y2M3DT10H30M")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testRecurringDuration() {

        try {
            assert("both recurringDuration are not equals: "
                   + test_obj.getRecurringDuration() + " and "
                   + "2001-12-22T16:30:00.0", (((test_obj
                       .getRecurringDuration()).toString())
                           .equals("2001-12-22T16:30:00.0")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testTimeInstant() {

        try {
            assert("both timeInstant are not equals: "
                   + test_obj.getTimeInstant() + " and "
                   + "Thu May 31 13:20:00 PDT 2001", (((test_obj
                       .getTimeInstant()).toString())
                           .equals("Thu May 31 13:20:00 PDT 2001")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testTimePeriod() {

        try {
            assert("both timePeriod are not equals: "
                   + test_obj.getTimePeriod() + " and "
                   + "2001-12-22T16:30:00.0", (((test_obj.getTimePeriod())
                       .toString()).equals("2001-12-22T16:30:00.0")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testMonth() {

        try {
            assert("both month are not equals: " + test_obj.getMonth()
                   + " and " + "1999-07",
                   (((test_obj.getMonth()).toString()).equals("1999-07")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testYear() {

        try {
            assert("both year are not equals: " + test_obj.getYear()
                   + " and " + "1999",
                   (((test_obj.getYear()).toString()).equals("1999")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testCentury() {

        try {
            assert("both century are not equals: " + test_obj.getCentury()
                   + " and " + "21",
                   (((test_obj.getCentury()).toString()).equals("21")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testInt() {

        try {
            assert("both int are not equals: " + test_obj.getInt()
                   + " and " + "2147483647",
                   (test_obj.getInt()==(2147483647)));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testLong() {

        try {
            assert("both long are not equals: " + test_obj.getLong()
                   + " and " + "9223372036854775807",
                   (test_obj.getLong()==Long.MAX_VALUE));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
 
    public void testFloat() {

        try {
            assert("both float are not equals: " + test_obj.getFloat()
                   + " and " + "3.4028235E38",
                   (test_obj.getFloat()==(float)(3.4028235E38)));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
 
    public void testShort() {

        try {
            assert("both short are not equals: " + test_obj.getShort()
                   + " and " + "32767",
                   (test_obj.getShort()==(32767)));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testDouble() {

        try {
            assert("both double are not equals: " + test_obj.getDouble()
                   + " and " + "1.7976931348623157E308",
                   (test_obj.getDouble()==1.7976931348623157E308));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    
    public void testBoolean() {

        try {
            assert("both boolean are not equals: " + test_obj.getBoolean()
                   + " and " + "false",
                   !(test_obj.getBoolean()));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testInteger() {

        try {
            assert("both int are not equals: " + test_obj.getInteger()
                   + " and 2147483647", (test_obj.getInteger() == 2147483647));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
      
    public void testPositiveInteger() {

        try {
            assert("both positiveInteger are not equals: " + test_obj.getPositiveInteger()
                   + " and " + "2147483647",
                   (test_obj.getPositiveInteger()==2147483647));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
    
    public void testNonNegativeInteger() {

        try {
            assert("both nonNegativeInteger are not equals: " + test_obj.getNonNegativeInteger()
                   + " and " + "2147483647",
                   (test_obj.getNonNegativeInteger()==2147483647));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
    
        public void testNegativeInteger() {

        try {
            assert("both negativeInteger are not equals: " + test_obj.getNegativeInteger()
                   + " and " + "-2147483648",
                   (test_obj.getNegativeInteger()==-2147483648));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
      
    public void testNonPositiveInteger() {

        try {
            assert("both nonPositiveInteger are not equals: " + test_obj.getNonPositiveInteger()
                   + " and " + "-2147483648",
                   (test_obj.getNonPositiveInteger()==-2147483648));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
}