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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: JAnnotation.java
 *
 * Contributors:
 * --------------
 * Andrew Fawcett (andrew.fawcett@coda.com) - Original Author
 */
package org.exolab.javasource;

import java.io.PrintWriter;
import java.lang.reflect.Array;

import org.exolab.castor.util.OrderedHashMap;

/**
 * JAnnotation represents a single annotation against a code element. The methods 
 *   described on the JAnnotatedElement interface are used to associate JAnnotation's
 *   with various other objects in this package describing Java code elements.
 *  
 *   The print method outputs annotations in various forms (as described in the 
 *   Java Language Specificaiton Third Edition) based on the methods called. 
 * 
 *   For "Marker Annotation", construct with the approprite JAnnotationType.
 * 
 *   JAnnotationType preliminaryType = new JAnnotationType("Preliminary");
 *   JAnnotation preliminary = new JAnnotation(preliminaryType);
 * 
 *   Outputs
 * 
 *   @Preliminary()
 * 
 *   For "Single Element Annotation", construct as above and call the setValue(value) 
 *   method to set the value of the "value" element of the annotation type.
 * 
 *   JAnnotationType copyrightType = new JAnnotationType("Copyright");
 *   JAnnotation copyright = new JAnnotation(copyrightType);
 *   copyright.setValue("\"2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.\"");
 * 
 *   Outputs
 * 
 *   @Copyright("2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.")
 * 
 *   For "Normal Annotation", construct as above then call the approprite setValue
 *   methods that accept an "elementName" parameter.
 * 
 *	 JAnnotationType requestForEnhancementType = new JAnnotationType("RequestForEnhancement");
 *   JAnnotation requestForEnhancement = new JAnnotation(requestForEnhancementType);
 *   requestForEnhancement.setElementValue("id", "2868724");
 *   requestForEnhancement.setElementValue("sysopsis", "\"Provide time-travel functionality\"");
 *   requestForEnhancement.setElementValue("enginer", "\"Mr. Peabody\"");
 *   requestForEnhancement.setElementValue("date", "\"4/1/2004\"");		
 * 
 *   Outputs
 * 
 *   @RequestForEnhancement(
 *       id       = 2868724,
 *       sysopsis = "Provide time-travel functionality",
 *       enginer  = "Mr. Peabody",
 *       date     = "4/1/2004")
 * 
 * 	 "Complex" annotations are also supported via the various setValue methods 
 *   that take a JAnnotation object. 
 * 
 *   JAnnotationType nameType = new JAnnotationType("Name");
 *   JAnnotationType authorType = new JAnnotationType("Author");
 *   JAnnotation author = new JAnnotation(authorType);
 *   JAnnotation name = new JAnnotation(nameType);
 *   name.setElementValue("first", "\"Joe\"");
 *   name.setElementValue("last", "\"Hacker\"");
 *   author.setValue(name);
 * 
 *   Outputs
 * 
 *   @Author(@Name(
 *       first = "Joe",
 *       last  = "Hacker"))
 * 
 *   Finally annotation elements who's types are arrays are supported via the 
 *   setValue methods that take arrays.
 *
 *   JAnnotationType endorsersType = new JAnnotationType("Endorsers");
 *   JAnnotation endorsers = new JAnnotation(endorsersType);
 *   endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});
 *    			
 *   Outputs
 * 
 *   @Endorsers(
 *       {
 *           "Children",
 *           "Unscrupulous dentists"
 *       })
 * 
 *   Note: Conditional element values are not currently supported. However the
 *         setValue methods taking String values can be used to output this construct 
 *         literally if desired.
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a> 
 */
public class JAnnotation
{
	/**
	 * Annotation type referenced by this annotation
	 */
	private JAnnotationType _annotationType;
	
	/**
	 * Element values associated with this JAnnotation, contains String, String[],
	 *   JAnnotation and JAnnotation[] objects.
	 */
	private OrderedHashMap _elementValues = new OrderedHashMap();
	
	/**
	 * Name of a single element 
	 */
	public static final String value = "value";
	
	/**
	 * Constructs a JAnnotation for the given annotation type
	 * @param annotationType
	 */
	public JAnnotation(JAnnotationType annotationType)
	{
		_annotationType = annotationType;
	}
	
	/**
	 * Returns the JAnnotationType associated with this JAnnotation
	 * @return
	 */
	public JAnnotationType getAnnotationType()
	{
		return _annotationType;
	} //-- getAnnotationType
	
	/**
	 * Sets the "value" annotation element value
	 * @param stringValue
	 */
	public void setValue(String stringValue)
	{
		_elementValues.put(value, stringValue);
	} //-- setValue
	
	/**
	 * Sets the "value" annotation element value as a list
	 * @param stringValue
	 */
	public void setValue(String[] stringValue)
	{
		_elementValues.put(value, stringValue);
	} //-- setValue
	
	/**
	 * Sets the "value" annotation element value as an annotation
	 * @param annotationValue
	 */
	public void setValue(JAnnotation annotationValue)
	{
		_elementValues.put(value, annotationValue);
	} //-- setValue
	
	/**
	 * Sets the "value" annotation element value as a list of annotation values
	 * @param annotationValue
	 */
	public void setValue(JAnnotation[] annotationValues)
	{
		_elementValues.put(value, annotationValues);
	} //-- setValue
	
	/**
	 * Sets the given annotation element value
	 * @param elementName
	 * @param stringValue
	 */
	public void setElementValue(String elementName, String stringValue)
	{
		_elementValues.put(elementName, stringValue);		
	} //-- setElementValue
	
	/**
	 * Sets the given annotation element value as a list
	 * @param elementName
	 * @param stringValue
	 */
	public void setElementValue(String elementName, String[] stringValues)
	{
		_elementValues.put(elementName, stringValues);		
	} //-- setElementValue
	
	/**
	 * Sets the given annotation element value as an annotation
	 * @param elementName
	 * @param annotationValue
	 */
	public void setElementValue(String elementName, JAnnotation annotationValue)
	{
		_elementValues.put(elementName, annotationValue);				
	} //-- setElementValue
	
	/**
	 * Sets the given annotation element values as a list of annotation values
	 * @param elementName
	 * @param annotationValue
	 */
	public void setElementValue(String elementName, JAnnotation[] annotationValues)
	{
		_elementValues.put(elementName, annotationValues);				
	} //-- setElementValue
	
	/**
	 * Returns the "value" annotation element value
	 * @return
     * @exception IllegalStateException when the element value is not a String
	 */
	public String getValue()
	{
		Object elementValue = getElementValueObject(value);
		if(elementValue instanceof String)
			return (String) elementValue;
		throw new IllegalStateException("'value' element is not of type String.");
	} //-- getValue
	
	/**
	 * Returns the "value" annotation element value as an JAnnotation
	 * @return
     * @exception IllegalStateException when the element value is not a JAnnotation
	 */
	public JAnnotation getValueAnnotation()
		throws IllegalStateException	
	{
		Object elementValue = getElementValueObject(value);
		if(elementValue instanceof JAnnotation)
			return (JAnnotation) elementValue;
		throw new IllegalStateException("'value' element is not of type JAnnotation.");
	} //-- getValueAnnotation

	/**
	 * Returns the given annotation element value
	 * @param elementName
	 * @return
     * @exception IllegalArgumentException when the element value is not a String
	 */
	public String getElementValue(String elementName)
		throws IllegalStateException	
	{
		Object elementValue = getElementValueObject(elementName);
		if(elementValue instanceof String)
			return (String) elementValue;
		throw new IllegalStateException("'"+elementName+"' element is not of type String.");
	} //-- getElementValue

	/**
	 * Returns the given annotation element value as a list
	 * @param elementName
	 * @return
     * @exception IllegalArgumentException when the element value is not a String[]
	 */	
	public String[] getElementValueList(String elementName)
		throws IllegalStateException	
	{
		Object elementValue = getElementValueObject(elementName);
		if(elementValue instanceof String[])
			return (String[]) elementValue;
		throw new IllegalStateException("'"+elementName+"' element is not of type String[].");
	} //-- getElementValueList
	
	/**
	 * Returns the given annotation element value as Object, typically used
	 *   if the value type is not known. This will either be a String or JAnnotation
	 *   as single value or as an array.
	 * @param elementName
	 * @return
	 */
	public Object getElementValueObject(String elementName)
	{
		return _elementValues.get(elementName);
	} //-- getElementValueObject
	
	/**
	 * Returns the given annotation element value as a JAnnotation
	 * @param elementValue
	 * @return
     * @exception IllegalArgumentException when the element value is not a JAnnotation
	 */
	public JAnnotation getElementValueAnnotation(String elementName)
		throws IllegalStateException	
	{
		Object elementValue = getElementValueObject(elementName);
		if(elementValue instanceof JAnnotation)
			return (JAnnotation) elementValue;
		throw new IllegalStateException("'"+elementName+"' element is not of type JAnnotation.");
	} //-- getElementValueAnnotation
	
	/**
	 * Returns the given annotation element value as a JAnnotation list
	 * @param elementValue
	 * @return
     * @exception IllegalArgumentException when the element value is not a JAnnotation[]
	 */
	public JAnnotation[] getElementValueAnnotationList(String elementName)
		throws IllegalStateException		
	{
		Object elementValue = getElementValueObject(elementName);
		if(elementValue instanceof JAnnotation[])
			return (JAnnotation[]) elementValue;
		throw new IllegalStateException("'"+elementName+"' element is not of type JAnnotation[].");
	} //-- getElementValueAnnotationList
	
	/**
	 * Returns the names of the elements set by this annotation
	 * @return
	 */
	public String[] getElementNames()
	{
		return (String[]) _elementValues.keySet().toArray(new String[] {});
	} //-- getElementNames
	
	/**
	 * Prints the source code for this JAnnotation to the given JSourceWriter
	 * @param jsw the JSourceWriter to print to. [May not be null]
	 */
	public void print(JSourceWriter jsw) 
	{
		jsw.write("@");
		jsw.write(_annotationType.getLocalName());
		jsw.write("(");
		// Single element annotation?
		String[] elementNames = getElementNames();
		if(elementNames.length==1 && elementNames[0].equals(value))
		{
			// Just output value
			printElementValue(jsw, getElementValueObject(value));
		}		
		else if(elementNames.length>0)
		{
			// Max element name length?
			int maxLength = 0;
			for(int i=0; i<elementNames.length; i++)
			{
				int elementNameLength = elementNames[i].length();
				if(elementNameLength>maxLength)
					maxLength = elementNameLength;
			}
			// Output element name and values
			jsw.writeln();
			jsw.indent();
			for(int i=0; i<elementNames.length; i++)
			{				
				int elementNameLength = elementNames[i].length();
				// Output element name with padding
				jsw.write(elementNames[i]);
				for(int p=0; p<maxLength-elementNameLength; p++)
					jsw.write(" ");
				// Assignment operator
				jsw.write(" = ");
				// Value
				printElementValue(jsw, getElementValueObject(elementNames[i]));
				if(i<elementNames.length-1)
				{
					jsw.write(",");
					jsw.writeln();
				}
			}
			jsw.unindent();
		}
		jsw.write(")");
	} //-- print

	/**
	 * Output annotation element value according to its type, String, String[], JAnnotation or JAnnotation[]
	 * @param jsw
	 * @param elementValue
	 * @throws IllegalArgumentException
	 */	
	private void printElementValue(JSourceWriter jsw, Object elementValue)
		throws IllegalArgumentException		
	{
		// String?
		if(elementValue instanceof String)
		{
			jsw.write((String) elementValue);
			return;
		}
		// JAnnotation?
		else if(elementValue instanceof JAnnotation)
		{
			JAnnotation annotation = (JAnnotation) elementValue;
			annotation.print(jsw);
			return;
		}		
		// List (of either String or JAnnotation)?
		else if(elementValue.getClass().isArray())
		{
			// Short hand for single item list
			int listLength = Array.getLength(elementValue);
			if(listLength==1)
			{
				printElementValue(jsw, Array.get(elementValue, 0));
				return;			
			}
			// Output list items
			jsw.indent();
			jsw.writeln();
			jsw.write("{");
			jsw.writeln();
			jsw.indent();
			for(int i=0; i<listLength; i++)
			{
				printElementValue(jsw, Array.get(elementValue, i));
				if(i<listLength-1)
					jsw.write(",");
				jsw.writeln();
			}
			jsw.unindent();
			jsw.write("}");
			jsw.unindent();
			return;
		}
		throw new IllegalArgumentException("'"+elementValue+"' was not expected.");
	} //-- printElementValue
	
	/**
	 * Test 
	 * @param args
	 */
	public static void main(String[] args)
	{
		JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
		
		// Normal annotation
		{
			JAnnotationType requestForEnhancementType = new JAnnotationType("RequestForEnhancement");
			JAnnotation requestForEnhancement = new JAnnotation(requestForEnhancementType);
			requestForEnhancement.setElementValue("id", "2868724");
			requestForEnhancement.setElementValue("sysopsis", "\"Provide time-travel functionality\"");
			requestForEnhancement.setElementValue("enginer", "\"Mr. Peabody\"");
			requestForEnhancement.setElementValue("date", "\"4/1/2004\"");		
			requestForEnhancement.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		// Markup annotation
		{
			JAnnotationType webMethodType = new JAnnotationType("WebMethod");
			JAnnotation webMethod = new JAnnotation(webMethodType);
			webMethod.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		// Single element annotation
		{
			JAnnotationType copyrightType = new JAnnotationType("Copyright");
			JAnnotation copyright = new JAnnotation(copyrightType);
			copyright.setValue("\"2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.\"");
			copyright.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		// Single element annotation with array list
		{
			JAnnotationType endorsersType = new JAnnotationType("Endorsers");
			JAnnotation endorsers = new JAnnotation(endorsersType);
			endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});
			endorsers.print(jsw);
		}

		jsw.writeln();
		jsw.writeln();
		
		// Single element annotation with array list (single item)
		{
			JAnnotationType endorsersType = new JAnnotationType("Endorsers");
			JAnnotation endorsers = new JAnnotation(endorsersType);
			endorsers.setValue(new String[] { "\"Epicurus\"" });
			endorsers.print(jsw);
		}
				
		jsw.writeln();
		jsw.writeln();
		
		// Complex annotation (single element)
		{
			JAnnotationType nameType = new JAnnotationType("Name");
			JAnnotationType authorType = new JAnnotationType("Author");
			JAnnotation author = new JAnnotation(authorType);
			JAnnotation name = new JAnnotation(nameType);
			name.setElementValue("first", "\"Joe\"");
			name.setElementValue("last", "\"Hacker\"");
			author.setValue(name);
			author.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		// Complex annotation (multi element)
		{
			JAnnotationType nameType = new JAnnotationType("Name");
			JAnnotationType authorType = new JAnnotationType("Author");
			JAnnotation author = new JAnnotation(authorType);
			JAnnotation name = new JAnnotation(nameType);
			name.setElementValue("first", "\"Joe\"");
			name.setElementValue("last", "\"Hacker\"");
			author.setElementValue("name", name);
			author.setElementValue("rating", "Rating.GOOD");
			author.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		// Complex annotation (multi element, annotation element list)
		{
			JAnnotationType nameType = new JAnnotationType("Name");
			JAnnotationType authorType = new JAnnotationType("Author");
			JAnnotation author = new JAnnotation(authorType);
			JAnnotation name1 = new JAnnotation(nameType);
			name1.setElementValue("first", "\"Joe\"");
			name1.setElementValue("last", "\"Hacker\"");
			JAnnotation name2 = new JAnnotation(nameType);
			name2.setElementValue("first", "\"Joe\"");
			name2.setElementValue("last", "\"Blogs\"");
			author.setElementValue("name", new JAnnotation[] { name1, name2 });
			author.setElementValue("rating", "Rating.GOOD");
			author.print(jsw);
		}
		
		jsw.flush();
	}
}
