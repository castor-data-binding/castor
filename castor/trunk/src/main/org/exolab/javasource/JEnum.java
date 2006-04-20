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
 * $Id: JEnum.java
 *
 * Contributors:
 * --------------
 * Andrew Fawcett (andrew.fawcett@coda.com) - Original Author
 */
package org.exolab.javasource;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class describes the definition of a enum type class
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a> 
 */
public class JEnum extends JClass 
{
	/**
	 * The list of elements of this JEnumConstant
	 */
	private JNamedMap _constants = null;
	
	/**
	 * @param name
	 * @throws IllegalArgumentException
	 */
	protected JEnum(String name) throws IllegalArgumentException {
		super(name);
		_constants        = new JNamedMap();
		//-- initialize default Java doc
		getJDocComment().setComment("Enumeration " + getLocalName() + ".");		
	}
	
    
	/**
	 * Adds the given JMember to this JAnnotationType
	 *
	 * @param jMember, the JMember to add
	 * @exception IllegalArgumentException when the given
	 * JMember has the same name of an existing JEnumConstant
	 * or if the JMember is of an unrecognized class.
	**/
	public void addMember(JMember jMember) 
		throws IllegalArgumentException 
	{	
		if(jMember instanceof JEnumConstant)
			addConstant((JEnumConstant) jMember);
		else
			super.addMember(jMember);
	} //-- addMember

	/**
	 * Adds the given JEnumConstant to this JEnum
	 *
	 * @param jConstant, the constant to add
	 * @exception IllegalArgumentException when the given
	 * JEnumConstant has the same name of an existing JEnumConstant.
	**/	
	public void addConstant(JEnumConstant jConstant)
		throws IllegalArgumentException 
	{
		if (jConstant == null) {
			throw new IllegalArgumentException("Enum fields cannot be null");
		}
        
		String name = jConstant.getName();        
		if (_constants.get(name) != null) {
			String err = "duplicate name found: " + name;
			throw new IllegalArgumentException(err);
		}
		_constants.put(name, jConstant);
	} //-- addConstant
		
	/**
	 * Returns the member with the given name, or null if no member
	 * was found with the given name
	 * @param name the name of the member to return
	 * @return the member with the given name, or null if no member
	 * was found with the given name
	**/
	public JEnumConstant getConstant(String name)
	{
		return (JEnumConstant)_constants.get(name);
	} //-- getElement
	
	/**
	 * Returns an array of all the JEnumConstant of this JEnum
	 * @return an array of all the JEnumConstant of this JEnum
	**/	
	public JEnumConstant[] getConstants()
	{
		int size = _constants.size();
		JEnumConstant[] farray = new JEnumConstant[size];
		for (int i = 0; i < size; i++) {
			farray[i] = (JEnumConstant)_constants.get(i);
		}
		return farray;
	} //-- getConstants
	
	
	/* (non-Javadoc)
	 * @see org.exolab.javasource.JClass#setSuperClass(java.lang.String)
	 */
	public void setSuperClass(String superClass) {
		throw new RuntimeException("Enum classes are not allowed to extend other classes."); 
	}
	
	/**
	 * Prints the source code for this JEnum to the given JSourceWriter
	 * @param jsw the JSourceWriter to print to. [May not be null]
	 */
	public void print(JSourceWriter jsw) {
        if (jsw == null) {
            throw new IllegalArgumentException("argument 'jsw' should not be null.");
        }
        StringBuffer buffer = new StringBuffer();
        
        printHeader(jsw);
        printPackageDeclaration(jsw);
        
        //-- get imports from inner-classes
        Vector removeImports = null;
        if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
            removeImports = new Vector();
            for (int i = 0; i < _innerClasses.size(); i++) {
                JClass iClass = (JClass)_innerClasses.elementAt(i);
                Enumeration enumeration = iClass.getImports();
                while (enumeration.hasMoreElements()) {
                    String classname = (String)enumeration.nextElement();
                    if (!hasImport(classname)) {
                        addImport(classname);
                        removeImports.addElement(classname);
                    }
                }
            }
        }
        printImportDeclarations(jsw);
        
        //-- remove imports from inner-classes, if necessary
        if (removeImports != null) {
            for (int i = 0; i < removeImports.size(); i++) {
                removeImport((String)removeImports.elementAt(i));
            }
        }

        //------------/
        //- Java Doc -/
        //------------/

        getJDocComment().print(jsw);

        //-- print class information
        //-- we need to add some JavaDoc API adding comments

        buffer.setLength(0);

		//-- print annotations
		getAnnotatedElementHelper().printAnnotations(jsw);
				
        JModifiers modifiers = getModifiers();
        if (modifiers.isPrivate()) {
            buffer.append("private ");
        }
        else if (modifiers.isPublic()) {
            buffer.append("public ");
        }

        buffer.append("enum ");
        buffer.append(getLocalName());
        buffer.append(' ');
        if (getInterfaceCount() > 0) {
            boolean endl = false;
            if ((getInterfaceCount() > 1)) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
                endl = true;
            }
            buffer.append("implements ");
            
            Enumeration enumeration = getInterfaces();
            while (enumeration.hasMoreElements()) {
                buffer.append(enumeration.nextElement());
                if (enumeration.hasMoreElements()) buffer.append(", ");
            }
            if (endl) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
            }
            else buffer.append(' ');
        }

        buffer.append('{');
        jsw.writeln(buffer.toString());
        buffer.setLength(0);
        jsw.writeln();

        jsw.indent();

        // -- print enum constants
        
        if (_constants.size() > 0) {
      		jsw.writeln();
      		jsw.writeln("  //------------------/");
      		jsw.writeln(" //- Enum Constants -/");
  			jsw.writeln("//------------------/");
      		jsw.writeln();
  		}
  		for (int i = 0; i < _constants.size(); i++) 
  		{
  			JEnumConstant jConstant = (JEnumConstant)_constants.get(i);
  			jConstant.print(jsw);
  			if(i < _constants.size()-1)
  				jsw.write(",");
  			else
  				jsw.write(";");
  			jsw.writeln();
  		}
  		
        //-- declare members

        if (_fields.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //--------------------------/");
            jsw.writeln(" //- Class/Member Variables -/");
            jsw.writeln("//--------------------------/");
            jsw.writeln();
        }

        for (int i = 0; i < _fields.size(); i++) {

            JField jField = (JField)_fields.get(i);

            //-- print Java comment
            JDocComment comment = jField.getComment();
            if (comment != null) comment.print(jsw);

			//-- print Annotations
			jField.printAnnotations(jsw);
			
            // -- print member
            jsw.write(jField.getModifiers().toString());
            jsw.write(' ');

            JType type = jField.getType();
            String typeName = type.toString();
            //-- for esthetics use short name in some cases
            if (typeName.equals(toString())) {
                typeName = type.getLocalName();
            }
            jsw.write(typeName);
            jsw.write(' ');
            jsw.write(jField.getName());

            String init = jField.getInitString();
            if (init != null) {
                jsw.write(" = ");
                jsw.write(init);
            }

            jsw.writeln(';');
            jsw.writeln();
        }

        //----------------------/
        //- Static Initializer -/
        //----------------------/
        
        if (!_staticInitializer.isEmpty()) {
            jsw.writeln();
            jsw.writeln("static {");
            jsw.writeln(_staticInitializer.toString());
            jsw.writeln("};");
            jsw.writeln();
        }
        
        //-- print constructors
        if (_constructors.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //----------------/");
            jsw.writeln(" //- Constructors -/");
            jsw.writeln("//----------------/");
            jsw.writeln();
        }
        for (int i = 0; i < _constructors.size(); i++) {
            JConstructor jConstructor = (JConstructor)_constructors.elementAt(i);
            jConstructor.print(jsw);
            jsw.writeln();
        }

        //-- print methods
        if (_methods.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //-----------/");
            jsw.writeln(" //- Methods -/");
            jsw.writeln("//-----------/");
            jsw.writeln();
        }

        for (int i = 0; i < _methods.size(); i++) {
            JMethod jMethod = (JMethod)_methods.elementAt(i);
            jMethod.print(jsw);
            jsw.writeln();
        }

        //-- print inner-classes
        if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
            jsw.writeln();
            jsw.writeln("  //-----------------/");
            jsw.writeln(" //- Inner Classes -/");
            jsw.writeln("//-----------------/");
            jsw.writeln();
        
            for (int i = 0; i < _innerClasses.size(); i++) {
                JClass jClass = (JClass)_innerClasses.elementAt(i);
                jClass.print(jsw, true);
                jsw.writeln();
            }
            
        }
        
        jsw.unindent();
        jsw.writeln('}');
        jsw.flush();
	}
	
	/**
	 * Test drive
	 * @param args
	 */
	public static void main(String[] args)
	{
		JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
		{
			JEnum color = new JEnum("Color");
			color.addConstant(new JEnumConstant("RED"));
			color.addConstant(new JEnumConstant("GREEN"));
			color.addConstant(new JEnumConstant("BLUE"));
			color.print(jsw);
		}		
		{
			JEnum color = new JEnum("Color");
			color.addField(new JField(new JType("String"), "_rgb"));
			JConstructor jConstructor = new JConstructor(color);
			jConstructor.addParameter(new JParameter(new JType("String"), "rgb"));
			jConstructor.setSourceCode("_rgb = rgb;");
			color.addConstructor(jConstructor);
			color.addConstant(new JEnumConstant("RED", new String[] { "\"#FF0000\"" }));
			color.addConstant(new JEnumConstant("GREEN", new String[] { "\"#00FF00\"" }));
			color.addConstant(new JEnumConstant("BLUE", new String[] { "\"#0000FF\"" }));
			color.print(jsw);
		}		
		{
			JEnum operation = new JEnum("Operation");
			// PLUS
			JEnumConstant plus = new JEnumConstant("PLUS");
			JMethod jMethod = new JMethod(new JType("double"), "eval");
			jMethod.getModifiers().makePackage();
			jMethod.addParameter(new JParameter(JType.Double, "x")); 
			jMethod.addParameter(new JParameter(JType.Double, "y"));
			jMethod.setSourceCode("return x + y;");
			plus.addMethod(jMethod);
			operation.addConstant(plus);
			// MINUS
			JEnumConstant minus = new JEnumConstant("MINUS");
			jMethod = new JMethod(JType.Double, "eval");
			jMethod.getModifiers().makePackage();
			jMethod.addParameter(new JParameter(JType.Double, "x"));
			jMethod.addParameter(new JParameter(JType.Double, "y"));
			jMethod.setSourceCode("return x - y;");
			minus.addMethod(jMethod);
			operation.addConstant(minus);
			// TIMES
			JEnumConstant times = new JEnumConstant("TIMES");
			jMethod = new JMethod(JType.Double, "eval");
			jMethod.getModifiers().makePackage();
			jMethod.addParameter(new JParameter(JType.Double, "x"));
			jMethod.addParameter(new JParameter(JType.Double, "y"));
			jMethod.setSourceCode("return x * y;");
			times.addMethod(jMethod);
			operation.addConstant(times);
			// Add 
			jMethod = new JMethod(JType.Double, "eval");
			jMethod.getModifiers().makePackage();
			jMethod.addParameter(new JParameter(JType.Double, "x"));
			jMethod.addParameter(new JParameter(JType.Double, "y"));
			jMethod.getModifiers().setAbstract(true);
			operation.addMethod(jMethod);
			operation.print(jsw);
		}
		jsw.flush();
	}
}
