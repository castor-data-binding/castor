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
 *  This class is just useful to put the testcases from {@link SGtypes1TestCases}
 *  in a TestSuite and to make a 'one time' setUp, using a 'wrapper'. The setup
 *  method {@link SGtypes1TestCases#oneTimeSetUp SGtypes1TestCases.oneTimeSetUp()} will be executed only once.
 *
 *
 * @author <a href="mailto:victoor@intalio.com">Alexandre Victoor</a>
 * @version $Revision$ $Date$
 */



public class SGtypes1Test 

{


    
    public static Test suite() {
         TestSuite suite= new TestSuite();
         suite.addTest(new TestSuite(SGtypes1TestCases.class));
         TestSetup wrapper= new TestSetup(suite) { 
            public void setUp() { 
                SGtypes1TestCases.oneTimeSetUp();   // this setUp method will be used only once during the test process
            } 
         }; 
       return wrapper; 
    }
    

}