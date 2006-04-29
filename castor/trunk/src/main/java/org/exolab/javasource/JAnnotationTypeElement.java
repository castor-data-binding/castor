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
 * $Id: JAnnotationTypeElement.java
 *
 * Contributors:
 * --------------
 * Andrew Fawcett (andrew.fawcett@coda.com) - Original Author
 */
package org.exolab.javasource;

import java.io.PrintWriter;

/**
 * A class that holds information about a given annotation type element
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a>
 */
public class JAnnotationTypeElement
	implements JMember 
{
	private String _name;
	private JDocComment _comment;
	private JType _type;
	private JModifiers _modifiers;
	private String _default;
	
	/**
	 * Constructs a JAnnotationTypeElement with a given name and type
	 * @param name
	 * @param type
	 */
	public JAnnotationTypeElement(String name, JType type)
	{
		setName(name);
		_type = type;
		_modifiers = new JModifiers();
		_comment = new JDocComment();
		_comment.appendComment("Element " + name);		
	}
	
	/**
	 * Returns the modifiers for this JAnnotationTypeElement
	 * @return the modifiers for this JAnnotationTypeElement     
	**/	
	public JModifiers getModifiers() 
	{
		return _modifiers;
	} //-- getModifiers

	/**
	 * Sets the name of this JAnnotationTypeElement
	 * @param name the name of this JAnnotationTypeElement
	 * @exception IllegalArgumentException when the
	 * name is not a valid Java member name
	**/
	public void setName(String name) throws 
		IllegalArgumentException
	{
		if (!JNaming.isValidJavaIdentifier(name)) {
			String err = "'" + name + "' is ";
			if (JNaming.isKeyword(name))
				err += "a reserved word and may not be used as "
					+ " a field name.";
			else 
				err += "not a valid Java identifier.";
			throw new IllegalArgumentException(err);
		}
		_name = name;
	} //-- setName

	/**
	 * Returns the name of this JAnnotationTypeElement
	 * @return the name of this JAnnotationTypeElement
	**/
	public String getName() 
	{
		return _name;
	} //-- getName
	
	/**
	 * Returns the JType represting the type of this JAnnotationTypeElement
	 * @return the JClass represting the type of this JAnnotationTypeElement
	**/	
	public JType getType()
	{
		return _type;
	} //-- getType
	

	/**
	 * Gets the initialization string for this JAnnotationTypeElement
	 * @return Gets the initialization string for this JAnnotationTypeElement
	 */	
	public String getDefaultString()
	{
		return _default;
	} //-- getDefaultString
	
	/**
	 * Sets the initialization string for this JAnnotationTypeElement;
	 * Allows some flexibility in declaring default values.
	 * @param defaultString the default string for this member.
	**/	
	public void setDefaultString(String defaultString)
	{
		_default = defaultString;
	} //-- setDefaultString

	/**
	 * Sets the comment describing this member. 
	 * @param comment the JDocComment for this member
	**/
	public void setComment(JDocComment comment) {
		this._comment = comment;
	} //-- setComment

	/**
	 * Sets the comment describing this member. 
	 * @param comment the JDocComment for this member
	**/
	public void setComment(String comment) {
		if (this._comment == null) {
			this._comment = new JDocComment();
		}
		this._comment.setComment(comment);
	} //-- setComment

	/**
	 * Returns the comment describing this member. 
	 * @return the comment describing this member, or
	 * null if no comment has been set.
	**/
	public JDocComment getComment() {
		return this._comment;
	} //-- getComment
	
	/**
	 * Outputs the annotation type element
	 * @param jsw
	 */
	public void print(JSourceWriter jsw)
	{
		if(_comment!=null)
			_comment.print(jsw);
		jsw.write(_type.toString());
		jsw.write(" ");
		jsw.write(_name);
		jsw.write("()");
		if(_default!=null)
		{
			jsw.write(" default ");
			jsw.write(_default);
		}
		jsw.write(";");
	}
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args)
	{
		JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
		
		// Simple
		{
			JAnnotationTypeElement annotationTypeElement = new JAnnotationTypeElement("synopsis", new JType("String"));
			annotationTypeElement.print(jsw);	
		}
		
		jsw.writeln();
		jsw.writeln();

		// Simple with default
		{
			JAnnotationTypeElement annotationTypeElement = new JAnnotationTypeElement("synopsis", new JType("String"));
			annotationTypeElement.setDefaultString("\"Good book\"");
			annotationTypeElement.print(jsw);	
		}
		
		jsw.flush();
	}
}
