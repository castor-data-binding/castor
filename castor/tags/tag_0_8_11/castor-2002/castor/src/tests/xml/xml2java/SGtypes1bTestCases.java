package xml.xml2java;



import java.io.*;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import junit.framework.*;
import junit.extensions.*;


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
                   + " and " + "-2147483648",
                   (test_obj.getInt()==(-2147483648)));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }


 
    public void testShort() {

        try {
            assert("both short are not equals: " + test_obj.getShort()
                   + " and " + "-32768",
                   (test_obj.getShort()==(-32768)));
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

      
    public void testPositiveInteger() {

        try {
            assert("both positiveInteger are not equals: " + test_obj.getPositiveInteger()
                   + " and " + "1",
                   (test_obj.getPositiveInteger()==1));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
    
    public void testNonNegativeInteger() {

        try {
            assert("both nonNegativeInteger are not equals: " + test_obj.getNonNegativeInteger()
                   + " and " + "1",
                   (test_obj.getNonNegativeInteger()==1));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
}