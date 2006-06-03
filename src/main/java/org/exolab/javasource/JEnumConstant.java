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
import java.util.Vector;

/**
 * Class describes the definition of a enum constant
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a> 
 */
public class JEnumConstant extends JAnnotatedElementHelper 
	implements JMember
{
	private String _name;
	private String[] _arguments;
	private JDocComment _comment;
    private Vector _methods = null;
	
	/**
	 * Constructs a JEnumConstant with a given name
	 * @param name
	 */
	public JEnumConstant(String name)
	{
		this(name, null);
	}
	
	/**
	 * Constructs a JEnumConstant with a given name
	 * @param name
	 * @param arguments
	 */
	public JEnumConstant(String name, String[] arguments)
	{
		setName(name);
        _methods = new Vector();		
		_comment = new JDocComment();
		_comment.appendComment("Constant " + name);
		_arguments = arguments;
	}
	
	/**
	 * Returns the modifiers for this JEnumConstant
	 * @return the modifiers for this JEnumConstant     
	**/	
	public JModifiers getModifiers() 
	{
		throw new RuntimeException("Not implemented."); 
	} //-- getModifiers
	
	/**
	 * Sets the arguments specified by this constant
	 * @param args
	 */
	public void setArguments(String[] args)
	{
		_arguments = args;
	} //-- setArguments
	
	/**
	 * Returns the arguments used by this constant
	 * @return
	 */
	public String[] getArguments()
	{
		return _arguments;
	} // -- getArguments

    /**
     * Adds the given JMethod to this JClass
     *
     * @param jMethod, the JMethod to add
     * @exception IllegalArgumentException when the given
     * JMethod has the same name of an existing JMethod.
    **/
    public void addMethod(JMethod jMethod) {
         addMethod(jMethod, true);
    }
    
    /**
     * Adds the given JMethod to this JClass
     *
     * @param jMethod, the JMethod to add
     * @param importReturnType true if we add the importReturnType to
     * the class import lists. It could be useful to set it to false when 
     * all types are fully qualified.
     * @exception IllegalArgumentException when the given
     * JMethod has the same name of an existing JMethod.
    **/
    public void addMethod(JMethod jMethod, boolean importReturnType)
        throws IllegalArgumentException
    {
        if (jMethod == null) {
            throw new IllegalArgumentException("Class methods cannot be null");
        }

        //-- check method name and signatures *add later*

        //-- keep method list sorted for esthetics when printing
        //-- START SORT :-)
        boolean added = false;
        JModifiers modifiers = jMethod.getModifiers();

        if (modifiers.isAbstract()) {
            getModifiers().setAbstract(true);
        }

        for (int i = 0; i < _methods.size(); i++) {
            JMethod tmp = (JMethod) _methods.elementAt(i);
            //-- first compare modifiers
            if (tmp.getModifiers().isPrivate()) {
                if (!modifiers.isPrivate()) {
                    _methods.insertElementAt(jMethod, i);
                    added = true;
                    break;
                }
            }
            //-- compare names
            if (jMethod.getName().compareTo(tmp.getName()) < 0) {
                    _methods.insertElementAt(jMethod, i);
                    added = true;
                    break;
            }
        }
        //-- END SORT
        if (!added) _methods.addElement(jMethod);
    } //-- addMethod

    /**
     * Adds the given array of JMethods to this JClass
     *
     * @param jMethods, the JMethod[] to add
     * @exception IllegalArgumentException when any of the given
     * JMethods has the same name of an existing JMethod.
    **/
    public void addMethods(JMethod[] jMethods)
        throws IllegalArgumentException
    {
        for (int i = 0; i < jMethods.length; i++)
            addMethod(jMethods[i]);
    } //-- addMethods
    
    /**
     * Returns an array of all the JMethods of this JClass
     *
     * @return an array of all the JMethods of this JClass
     */
    public JMethod[] getMethods() {
        int size = _methods.size();
        JMethod[] marray = new JMethod[size];

        for (int i = 0; i < _methods.size(); i++) {
            marray[i] = (JMethod)_methods.elementAt(i);
        }
        return marray;
    } //-- getMethods

    /**
     * Returns the first occurance of the method with the
     * given name, starting from the specified index.
     *
     * @param name the name of the method to look for
     * @param startIndex the starting index to begin the search
     * @return the method if found, otherwise null.
     */
    public JMethod getMethod(String name, int startIndex) {
        for (int i = startIndex; i < _methods.size(); i++) {
            JMethod jMethod = (JMethod)_methods.elementAt(i);
            if (jMethod.getName().equals(name)) return jMethod;
        }
        return null;
    } //-- getMethod

    /**
     * Returns the JMethod located at the specified index
     *
     * @param index the index of the JMethod to return.
     * @return the JMethod 
     */
    public JMethod getMethod(int index) {
        return (JMethod)_methods.elementAt(index);
    } //-- getMethod    
    
	/**
	 * Sets the name of this JEnumConstant
	 * @param name the name of this JEnumConstant
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
	 * Returns the name of this JEnumConstant
	 * @return the name of this JEnumConstant
	**/
	public String getName() 
	{
		return _name;
	} //-- getName
	
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
	 * Outputs the enum constant
	 * @param jsw
	 */
	public void print(JSourceWriter jsw)
	{
		//-- print comments
		if(_comment!=null)
			_comment.print(jsw);
		//-- print annotation
		if(printAnnotations(jsw))
			jsw.writeln();
		//-- print name
		jsw.write(_name);
		//-- print arguments
		if(_arguments!=null && _arguments.length>0)
		{
			jsw.write("(");
			for(int a=0; a<_arguments.length; a++)
			{
				jsw.write(_arguments[a]);
				if(a<_arguments.length-1)
					jsw.write(", ");
			}
			jsw.write(")");
		}
        //-- print methods
        if (_methods.size() > 0) {
        	jsw.write(" {");
        	jsw.writeln();
        	jsw.indent();
	        for (int i = 0; i < _methods.size(); i++) {
	            JMethod jMethod = (JMethod)_methods.elementAt(i);
	            jMethod.print(jsw);
	            if(i<_methods.size()-1)
	            	jsw.writeln();
	        }
	        jsw.unindent();
	        jsw.write("}");
        }		        
	}
	
	/**
	 * Test drive
	 * @param args
	 */
	public static void main(String[] args)
	{
		JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
		{
			JEnumConstant constant = new JEnumConstant("PENNY");
			constant.print(jsw);
			jsw.writeln();
		}
		{
			JEnumConstant constant = new JEnumConstant("PENNY");
			constant.setArguments(new String[] { "1" });
			constant.print(jsw);
			jsw.writeln();
		}
		{
			JEnumConstant constant = new JEnumConstant("PENNY");
			constant.setArguments(new String[] { "1", "\"Penny\"" });
			constant.print(jsw);
			jsw.writeln();
		}
		{
			JEnumConstant constant = new JEnumConstant("PENNY");
			constant.setArguments(new String[] { "1", "\"Penny\"" });
			JMethod jMethod = new JMethod(new JType("String"), "color");
			jMethod.setSourceCode("return \"Copper\";");
			constant.addMethod(jMethod);
			constant.print(jsw);
			jsw.writeln();
		}
		{
			JEnumConstant constant = new JEnumConstant("PENNY");
			constant.setArguments(new String[] { "1", "\"Penny\"" });
			JMethod jMethod = new JMethod(new JType("String"), "color");
			jMethod.setSourceCode("return \"Copper\";");
			constant.addMethod(jMethod);
			jMethod = new JMethod(new JType("int"), "weight");
			jMethod.setSourceCode("return 1;");
			constant.addMethod(jMethod);
			constant.print(jsw);
			jsw.writeln();
		}
		jsw.flush();
	}
}