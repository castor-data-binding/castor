package xml.xml2java;



import java.io.*;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import junit.framework.*;
import junit.extensions.*;


 public class SGtypes1TestCases extends TestCase {
   
    static Unmarshaller unmarshaller;
    static Testtype1    test_obj;

   
        
   public SGtypes1TestCases(String name) {
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
            
            test_obj = (Testtype1) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/testtype1.xml" ) );
                        
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

    public void testInteger() {

        try {
            assert("both int are not equals: " + test_obj.getInteger()
                   + " and 1234", (test_obj.getInteger() == 1234));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testDate() {

        try {
            assert("both date are not equals: " + test_obj.getDate()
                   + " and " + "2000-12-22",
                   (((test_obj.getDate()).toString()).equals("2000-12-22")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testTime() {

        try {
            assert("both time are not equals: " + test_obj.getTime()
                   + " and " + "16:30:00.0",
                   (((test_obj.getTime()).toString()).equals("16:30:00.0")));
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
                   + "2000-12-22T16:30:00.0", (((test_obj
                       .getRecurringDuration()).toString())
                           .equals("2000-12-22T16:30:00.0")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testTimeInstant() {

        try {
            assert("both timeInstant are not equals: "
                   + test_obj.getTimeInstant() + " and "
                   + "Mon May 31 13:20:00 PDT 1999", (((test_obj
                       .getTimeInstant()).toString())
                           .equals("Mon May 31 13:20:00 PDT 1999")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testTimePeriod() {

        try {
            assert("both timePeriod are not equals: "
                   + test_obj.getTimePeriod() + " and "
                   + "2000-12-22T16:30:00.0", (((test_obj.getTimePeriod())
                       .toString()).equals("2000-12-22T16:30:00.0")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testMonth() {

        try {
            assert("both month are not equals: " + test_obj.getMonth()
                   + " and " + "2000-12",
                   (((test_obj.getMonth()).toString()).equals("2000-12")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testYear() {

        try {
            assert("both year are not equals: " + test_obj.getYear()
                   + " and " + "2000",
                   (((test_obj.getYear()).toString()).equals("2000")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

    public void testCentury() {

        try {
            assert("both century are not equals: " + test_obj.getCentury()
                   + " and " + "20",
                   (((test_obj.getCentury()).toString()).equals("20")));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testInt() {

        try {
            assert("both int are not equals: " + test_obj.getInt()
                   + " and " + "1234",
                   (test_obj.getInt()==1234));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testLong() {

        try {
            assert("both long are not equals: " + test_obj.getLong()
                   + " and " + "1234",
                   (test_obj.getLong()==1234));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testShort() {

        try {
            assert("both short are not equals: " + test_obj.getShort()
                   + " and " + "125",
                   (test_obj.getShort()==125));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testBoolean() {

        try {
            assert("both boolean are not equals: " + test_obj.getBoolean()
                   + " and " + "true",
                   (test_obj.getBoolean()));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testFloat() {

        try {
            assert("both float are not equals: " + test_obj.getFloat()
                   + " and " + "123.456",
                   (test_obj.getFloat()==(float)(123.456)));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testDouble() {

        try {
            assert("both double are not equals: " + test_obj.getDouble()
                   + " and " + "123456789",
                   (test_obj.getDouble()==123456789));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }

   public void testDecimal() {

        try {
            assert("both decimal are not equals: " + test_obj.getDecimal()
                   + " and " + "1234.56789",
                   ((test_obj.getDecimal()).equals(new java.math.BigDecimal("1234.56789"))));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
      
    public void testPositiveInteger() {

        try {
            assert("both positiveInteger are not equals: " + test_obj.getPositiveInteger()
                   + " and " + "1234.56789",
                   (test_obj.getPositiveInteger()==12));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
    
    public void testNegativeInteger() {

        try {
            assert("both negativeInteger are not equals: " + test_obj.getNegativeInteger()
                   + " and " + "-7",
                   (test_obj.getNegativeInteger()==-7));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
      
    public void testNonPositiveInteger() {

        try {
            assert("both nonPositiveInteger are not equals: " + test_obj.getNonPositiveInteger()
                   + " and " + "1234.56789",
                   (test_obj.getNonPositiveInteger()==-12));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
    public void testNonNegativeInteger() {

        try {
            assert("both nonNegativeInteger are not equals: " + test_obj.getNonNegativeInteger()
                   + " and " + "-7",
                   (test_obj.getNonNegativeInteger()==7));
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }  
}