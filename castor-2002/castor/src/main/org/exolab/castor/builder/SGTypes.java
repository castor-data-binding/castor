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

package org.exolab.castor.builder;

import org.exolab.castor.xml.JavaXMLNaming;
import org.exolab.javasource.*;


/**
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SGTypes {
    
    //-----------------------/
    //- org.xml.sax Objects -/
    //-----------------------/
    
    public static final JClass SAXException 
        = new JClass("org.xml.sax.SAXException");
        
    
    //---------------------------/
    //- org.exolab.castor types -/
    //---------------------------/
    
    public static final JClass UnmarshalException = 
        new JClass("org.exolab.castor.xml.UnmarshalException");
        
    public static final JClass MarshalException =
        new JClass("org.exolab.castor.xml.MarshalException");
        
    public static final JClass ValidationException = 
        new JClass("org.exolab.castor.xml.ValidationException");
        
    //---------------/
    //- JDK Objects -/
    //---------------/
    
    //-- java.lang
    public static final JClass IllegalStateException
        = new JClass("java.lang.IllegalStateException");
        
    public static final JClass IndexOutOfBoundsException
        = new JClass("java.lang.IndexOutOfBoundsException");
        
    public static final JClass InstantiationException 
        = new JClass("java.lang.InstantiationException");
        
        
    public static final JClass Class         = new JClass("java.lang.Class");
    public static final JClass Object        = new JClass("java.lang.Object");
    public static final JClass String        = new JClass("java.lang.String");
    public static final JClass StringBuffer  = new JClass("java.lang.StringBuffer");
    
    //-- java.io
    public static final JClass FileReader    = new JClass("java.io.FileReader");
    public static final JClass FileWriter    = new JClass("java.io.FileWriter");
    public static final JClass IOException   = new JClass("java.io.IOException");
    public static final JClass PrintWriter   = new JClass("java.io.PrintWriter");
    public static final JClass Reader        = new JClass("java.io.Reader");
    public static final JClass Writer        = new JClass("java.io.Writer");
    
    //-- java.util
    public static final JClass Enumeration   = new JClass("java.util.Enumeration");
    public static final JClass Hashtable     = new JClass("java.util.Hashtable");
    public static final JClass Stack         = new JClass("java.util.Stack");
    public static final JClass Vector        = new JClass("java.util.Vector");
    
    
} //-- SGTypes
