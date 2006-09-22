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
 * Describes the definition of a annotation type class
 * 
 * <pre>
 *   JAnnotationType type = new JAnnotationType("RequestForEnhancement");
 *   type.addElement(new JAnnotationTypeElement("id", JType.Int));
 *   type.addElement(new JAnnotationTypeElement("synopsis", new JType("String")));
 *   JAnnotationTypeElement engineer;
 *   engineer = new JAnnotationTypeElement("engineer", new JType("String"));
 *   engineer.setDefaultString("\"[unassigned]\"");
 *   type.addElement(engineer);
 *   JAnnotationTypeElement date;
 *   date = new JAnnotationTypeElement("date", new JType("String"));
 *   date.setDefaultString("\"[unimplemented]\"");
 *   type.addElement(date);
 * </pre>
 * 
 *  outputs
 *
 * <pre>
 *   public &#064;interface RequestForEnhancement {
 *       int id();
 *       String synopsis();
 *       String engineer() default "[unassigned]";
 *       String date() default "[unimplemented]";
 *   }  
 * </pre>
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a> 
 */
public final class JAnnotationType extends JStructure {
    /**
     * The list of elements of this JAnnotationType
     */
    private JNamedMap _elements = null;
    
    /**
     * Creates a JAnnotationType of the given name
     * 
     * @param name Annotation name
     * @throws IllegalArgumentException
     */
    public JAnnotationType(final String name) {
        super(name);
        _elements = new JNamedMap();
        //-- initialize default Java doc
        getJDocComment().appendComment("Annotation " + getLocalName() + ".");
    }

    /**
     * Adds the given JMember to this JAnnotationType
     * 
     * @param jMember the JMember to add
     */
    public void addMember(final JMember jMember) {
        if (! (jMember instanceof JAnnotationTypeElement)) {
            throw new IllegalArgumentException("Must be a JAnnotationTypeElement.");
        }
        addElement((JAnnotationTypeElement) jMember);
    } //-- addMember
    
    /**
     * Adds the given JAnnotationTypeElement to this JAnnotationType
     * 
     * @param jElement the element to add
     */
    public void addElement(final JAnnotationTypeElement jElement) {
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
        while (type instanceof JArrayType) {
            type = ((JArrayType) type).getComponentType();
        }
        if (!type.isPrimitive()) { 
            addImport(type.getName()); 
        }
    } //-- addElement
    
    /**
     * Returns the member with the given name, or null if no member was found
     * with the given name
     * 
     * @param name the name of the member to return
     * @return the member with the given name, or null if no member was found
     *         with the given name
     */
    public JAnnotationTypeElement getElement(final String name) {
        return (JAnnotationTypeElement) _elements.get(name);
    } //-- getElement
    
    /**
     * Returns an Array containing all our JAnnotationTypeElements
     * 
     * @return an Array containing all our JAnnotationTypeElements
     */
    public JAnnotationTypeElement[] getElements() {
        int size = _elements.size();
        JAnnotationTypeElement[] farray = new JAnnotationTypeElement[size];
        for (int i = 0; i < size; i++) {
            farray[i] = (JAnnotationTypeElement) _elements.get(i);
        }
        return farray;
    } //-- getElements

    /**
     * Not implemented. Always throws a RuntimeException.
     * 
     * @param jField not used
     * @see org.exolab.javasource.JStructure#addField
     */
    public void addField(final JField jField) {
        throw new RuntimeException("Not implemented."); 
    } //-- addField

    /**
     * Not implemented. Always throws a RuntimeException.
     * 
     * @param name not used
     * @return nothing is ever returned
     * @see org.exolab.javasource.JStructure#getField
     */
    public JField getField(final String name) {
        throw new RuntimeException("Not implemented."); 
    } //-- getField

    /**
     * Not implemented. Always throws a RuntimeException.
     * 
     * @return nothing is ever returned
     */
    public JField[] getFields() {
        throw new RuntimeException("Not implemented."); 
    } //-- getFields

    /**
     * Prints the source code for this JAnnotationType to the given
     * JSourceWriter
     * 
     * @param jsw the JSourceWriter to print to. Must not be null.
     */
    public void print(final JSourceWriter jsw) {
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
        } else if (modifiers.isPublic()) {
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
        for (int i = 0; i < _elements.size(); i++) {
            JAnnotationTypeElement jElement = (JAnnotationTypeElement) _elements.get(i);
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
    public static void main(final String[] args) {
        JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
        
        JAnnotationType annotationType = new JAnnotationType("RequestForEnhancement");
        annotationType.addElement(new JAnnotationTypeElement("id", JType.INT));
        annotationType.addElement(new JAnnotationTypeElement("synopsis", new JType("String")));
        JAnnotationTypeElement engineer = new JAnnotationTypeElement("engineer", new JType("String"));
        engineer.setDefaultString("\"[unassigned]\"");
        annotationType.addElement(engineer);
        JAnnotationTypeElement date = new JAnnotationTypeElement("date", new JType("String"));
        date.setDefaultString("\"[unimplemented]\"");
        annotationType.addElement(date);
        annotationType.print(jsw);
        
        jsw.flush();
    }

}
