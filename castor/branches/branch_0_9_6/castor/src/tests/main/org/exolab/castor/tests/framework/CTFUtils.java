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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 *
 */

package org.exolab.castor.tests.framework;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.exolab.adaptx.xslt.dom.XPNReader;
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xml.XMLDiff;

//-- Java imports
import java.io.StringReader;


/**
 * This class contains utility methods needed by the CTF.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */

public class CTFUtils {

   /**
    * The Java primitives
    */
   public static final String BOOLEAN   = "boolean";
   public static final String BYTE      = "byte";
   public static final String CHARACTER = "character";
   public static final String DOUBLE    = "double";
   public static final String FLOAT     = "float";
   public static final String INT       = "int";
   public static final String LONG      = "long";
   public static final String SHORT     = "short";
   public static final String STRING    = "String";
   
   /**
	* Loads the given XML file as an XPathNode
    *
	* @param url the filename or URL of the XML file to load
    */
    public static XPathNode loadXPN(String url) throws java.io.IOException {

	    XPathNode node = null;
	    XPNReader reader = new XPNReader(url);
	    reader.setSaveLocation(true);
	    node = reader.read();
        return node;
    } //-- loadXPN

   /**
    * Compares two XML documents located at 2 given URL.
    * @param document1 the URL of the first XML document.
    * @param document2 the URL of the second XML document.
    * @return an int indicating the number of differences or 0 if both documents are
    * 'XML equivalent'.
    */
    public static int compare(String document1, String document2) throws java.io.IOException {
        XPathNode node1 = loadXPN(document1);
        XPNReader reader = new XPNReader(document2);
        XPathNode node2 = reader.read();
	    XMLDiff diff = new XMLDiff();
	    int result = diff.compare(node1, document1, node2, "In-Memory-Result");
        return result;

    }
    
    /**
     * Returns the class associated with the 
     * given name.
     * 
     * @param name the fully qualified name of the class to return.
     * Primitives are handled through their name and not their class name.
     * For instance 'boolean' should be used instead of 'java.lang.Boolean.TYPE'.
     * 
     * @return the class associated with given name.
     *
     */
    public static Class getClass(String name, ClassLoader loader) throws ClassNotFoundException {
        if (name == null)
            throw new IllegalArgumentException("Name shouldn't be null.");
        
        if (name.equals(BOOLEAN))
            return (Boolean.TYPE);
        else if (name.equals(BYTE))
            return (Byte.TYPE);
        else if (name.equals(CHARACTER))
            return (Character.TYPE);
        else if (name.equals(DOUBLE))
            return (Double.TYPE);
        else if (name.equals(FLOAT))
            return (Float.TYPE);  
        else if (name.equals(INT))
            return (Integer.TYPE);
        else if (name.equals(LONG))
            return (Long.TYPE);
        else if (name.equals(SHORT))
            return (Short.TYPE);  
        else
            return loader.loadClass(name);
    
    }

    
   /**
    * Converts the given value to a java representation that
    * corresponds to the given type.
    * 
    * @param value the value to be converted
    * @param type a string representation of the java type.
    * @param loader an optional ClassLoader used in case we need to
    * use the Unmarshaller to retrieve a complex java object.
    * @return an java object that corresponds to the given value converted
    * to a java type according to the type passed as parameter.
    */
   public static Object instantiateObject(String type, String value, ClassLoader loader) 
       throws ClassNotFoundException, MarshalException
   {
       if (type.equals(STRING)) {
           return value;
       }
       else if (type.equals(String.class.getName())) {
           return value;
       }
       else if (type.equals(BOOLEAN) || type.equals(Boolean.class.getName())) {
          return new Boolean(value);
       }
       else if (type.equals(BYTE) || type.equals(Byte.class.getName())) {
          return new Byte(value);
       } 
       else if (type.equals(CHARACTER) || type.equals(Character.class.getName())) {
           return new Character(value.charAt(0));
       }
       else if (type.equals(DOUBLE) || type.equals(Double.class.getName())) {
           return new Double(value);
       }
       else if (type.equals(FLOAT) || type.equals(Float.class.getName())) {
           return new Float(value);
       }
       else if (type.equals(INT) || type.equals(Integer.class.getName())) {
            return new Integer(value);
       }
       else if (type.equals(LONG) || type.equals(Long.class.getName())) {
           return new Long(value);
       }
       else if (type.equals(SHORT) || type.equals(Short.class.getName())) {
           return new Short(value);
       }
       //-- Else we let the unmarshaller to get us the class
       else {
          try {
              //-- check arbitrary class:
              Class clazz = loader.loadClass(type);
              Unmarshaller unm = new Unmarshaller(clazz);
              return unm.unmarshal(new StringReader(value));
          } catch (ValidationException e) {
              //--this can't happen, just log it
              e.printStackTrace();
          }
       }
       return null;
   }
}