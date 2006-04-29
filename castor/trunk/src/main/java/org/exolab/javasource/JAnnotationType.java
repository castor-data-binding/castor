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
 * $Id: JAnnotationType.java
 *
 * Contributors:
 * --------------
 * Andrew Fawcett (andrew.fawcett@coda.com) - Original Author
 */
package org.exolab.javasource;

import java.io.PrintWriter;

/**
 * Class describes the definition of a annotation type class
 * 
 *   JAnnotationType annotationType = new JAnnotationType("RequestForEnhancement");
 *   annotationType.addElement(new JAnnotationTypeElement("id", JType.Int));
 *   annotationType.addElement(new JAnnotationTypeElement("synopsis", new JType("String")));
 *   JAnnotationTypeElement enginer = new JAnnotationTypeElement("enginer", new JType("String"));
 *   enginer.setDefaultString("\"[unassigned]\"");
 *   annotationType.addElement(enginer);
 *   JAnnotationTypeElement date = new JAnnotationTypeElement("date", new JType("String"));
 *   date.setDefaultString("\"[unimplemented]\"");
 *   annotationType.addElement(date);
 * 
 *   Outputs
 * 
 *   public @interface RequestForEnhancement {
 *       int id();
 *       String synopsis();
 *       String enginer() default "[unassigned]";
 *       String date() default "[unimplemented]";
 *   }  
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a> 
 */
public class JAnnotationType extends JStructure
{
	/**
	 * The list of elements of this JAnnotationType
	 */
	private JNamedMap _elements = null;
	
	/**
	 * Creates a JAnnotationType of the given name
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public JAnnotationType(String name) 
		throws IllegalArgumentException 
	{
		super(name);
		_elements = new JNamedMap();
		//-- initialize default Java doc
		getJDocComment().appendComment("Annotation " + getLocalName() + ".");
	}

	/**
	 * Adds the given JMember to this JAnnotationType
	 *
	 * @param jMember, the JMember to add
	 * @exception IllegalArgumentException when the given
	 * JMember has the same name of an existing JAnnotationTypeElement
	 * or if the JMember is of an unrecognized class.
	**/
	public void addMember(JMember jMember) 
		throws IllegalArgumentException 
	{	
		if(jMember instanceof JAnnotationTypeElement)
			addElement((JAnnotationTypeElement) jMember);
		else
			throw new IllegalArgumentException("Must be a JAnnotationTypeElement.");
		
	} //-- addMember
	
	/**
	 * Adds the given JAnnotationTypeElement to this JAnnotationType
	 *
	 * @param jElement, the element to add
	 * @exception IllegalArgumentException when the given
	 * JAnnotationTypeElement has the same name of an existing JAnnotationTypeElement.
	**/	
	public void addElement(JAnnotationTypeElement jElement)
		throws IllegalArgumentException 
	{
		if (jElement == null) {
			throw new IllegalArgumentException("Class members cannot be null");
		}
        
		String name = jElement.getName();        
		if (_elements.get(name) != null) {
			String err = "duplicate name found: " + name;
			throw new IllegalArgumentException(err);
		}
		_elements.put(name, jElement);

		// if member is of a type not imported by this class
		// then add import
		JType type = jElement.getType();
		while (type.isArray()) type = type.getComponentType();
		if ( !type.isPrimitive() )
			addImport( type.getName());
	} //-- addElement
	
	/**
	 * Returns the member with the given name, or null if no member
	 * was found with the given name
	 * @param name the name of the member to return
	 * @return the member with the given name, or null if no member
	 * was found with the given name
	**/
	public JAnnotationTypeElement getElement(String name)
	{
		return (JAnnotationTypeElement)_elements.get(name);
	} //-- getElement
	
	/**
	 * Returns an array of all the JAnnotationTypeElements of this JClass
	 * @return an array of all the JAnnotationTypeElements of this JClass
	**/	
	public JAnnotationTypeElement[] getElements()
	{
		int size = _elements.size();
		JAnnotationTypeElement[] farray = new JAnnotationTypeElement[size];
		for (int i = 0; i < size; i++) {
			farray[i] = (JAnnotationTypeElement)_elements.get(i);
		}
		return farray;
	} //-- getElements

	/**
	 * Not implemented.
	 * @param jField 
	 */
	public void addField(JField jField) 
		throws IllegalArgumentException 
	{
		throw new RuntimeException("Not implemented."); 
	} //-- addField

	/**
	 * Not implemnted.
	 * @param name 
	 * @return JField
	 */
	public JField getField(String name) 
	{
		throw new RuntimeException("Not implemented."); 
	} //-- getField

	/**
	 * Not implemented
	 * @return JField[]
	 */
	public JField[] getFields() 
	{
		throw new RuntimeException("Not implemented."); 
	} //-- getFields

	/**
	 * Prints the source code for this JAnnotationType to the given JSourceWriter
	 * @param jsw the JSourceWriter to print to. [May not be null]
	 */
	public void print(JSourceWriter jsw) 
	{
		if (jsw == null) {
			throw new IllegalArgumentException("argument 'jsw' should not be null.");
		}
        
		StringBuffer buffer = new StringBuffer();

		printHeader(jsw);
		printPackageDeclaration(jsw);
        printImportDeclarations(jsw);

		//------------/
		//- Java Doc -/
		//------------/

		getJDocComment().print(jsw);

		//-- print class information
		//-- we need to add some JavaDoc API adding comments

		buffer.setLength(0);
		JModifiers modifiers = getModifiers();
		if (modifiers.isPrivate()) {
			buffer.append("private ");
		}
		else if (modifiers.isPublic()) {
			buffer.append("public ");
		}
		buffer.append("@interface ");
		buffer.append(getLocalName());
		buffer.append(' ');
		buffer.append('{');
		jsw.writeln(buffer.toString());
		
		//-- declare members
		
		buffer.setLength(0);
		jsw.writeln();
		jsw.indent();
		for (int i = 0; i < _elements.size(); i++) 
		{

			JAnnotationTypeElement jElement = (JAnnotationTypeElement)_elements.get(i);
			jElement.print(jsw);
			jsw.writeln();
		}       
		jsw.unindent();
		
		// -- close class
		
		jsw.writeln('}');
		jsw.flush();
		
	} //-- print
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args)
	{
		JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
		
		{		
			JAnnotationType annotationType = new JAnnotationType("RequestForEnhancement");
			annotationType.addElement(new JAnnotationTypeElement("id", JType.Int));
			annotationType.addElement(new JAnnotationTypeElement("synopsis", new JType("String")));
			JAnnotationTypeElement enginer = new JAnnotationTypeElement("enginer", new JType("String"));
			enginer.setDefaultString("\"[unassigned]\"");
			annotationType.addElement(enginer);
			JAnnotationTypeElement date = new JAnnotationTypeElement("date", new JType("String"));
			date.setDefaultString("\"[unimplemented]\"");
			annotationType.addElement(date);
			annotationType.print(jsw);
		}
		
		jsw.flush();
	}
}