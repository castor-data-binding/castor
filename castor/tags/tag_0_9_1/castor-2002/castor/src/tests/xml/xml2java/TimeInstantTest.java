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
 *  This is a test specificly for the TimeInstant type 
 *  (there was a bug in Castor for this type).
 *  In {@link #test} an xml file containinig only one
 *  timeinstant element is unmarshalled. Then the value
 *  is checked.
 *
 * @author <a href="mailto:victoor@intalio.com">Alexandre Victoor</a>
 * @version $Revision$ $Date$
 */


public class TimeInstantTest extends TestCase {
   
    Unmarshaller unmarshaller;
    TimeInstantTestxsd    test_obj;

   
        
   public TimeInstantTest(String name) {
        super(name);
    }
    
    
    public void test() {

        try {
           
            unmarshaller = new Unmarshaller(TimeInstantTestxsd.class);
            
            test_obj = (TimeInstantTestxsd) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/timeInstant.xml" ) );
            
         
            assert("both timeInstant are not equals: "
                   + test_obj.getTimeInstant() + " and "
                   + "Mon May 31 13:20:00 PDT 1999", (((test_obj
                       .getTimeInstant()).toString())
                           .equals("Mon May 31 13:20:00 PDT 1999")));
            
            
            
            
                        
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
}