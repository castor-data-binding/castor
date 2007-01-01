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
 */
package org.exolab.javasource;

import java.io.PrintWriter;
import java.lang.reflect.Array;

import org.exolab.castor.util.OrderedHashMap;

/**
 * JAnnotation represents a single annotation against a code element. The
 * methods described on the JAnnotatedElement interface are used to associate
 * JAnnotation's with various other objects in this package describing Java code
 * elements.
 * <p>
 * The print method outputs annotations in various forms (as described in the
 * Java Language Specification Third Edition) based on the methods called.
 * <p>
 * For "Marker Annotation", construct with the appropriate JAnnotationType.
 * <pre>
 *   JAnnotationType preliminaryType = new JAnnotationType("Preliminary");
 *   JAnnotation preliminary = new JAnnotation(preliminaryType);
 * </pre>
 * outputs
 * <pre>
 *   &#064;Preliminary()
 * </pre>
 * For "Single Element Annotation", construct as above and call the
 * setValue(value) method to set the value of the "value" element of the
 * annotation type.
 * <pre>
 *   JAnnotationType copyrightType = new JAnnotationType("Copyright");
 *   JAnnotation copyright = new JAnnotation(copyrightType);
 *   copyright.setValue("\"2002 Yoyodyne Systems, Inc., All rights reserved.\"");
 * </pre>
 * outputs
 * <pre>
 *   &#064;Copyright("2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.")
 * </pre>
 * For "Normal Annotation," construct as above then call the appropriate
 * setValue methods that accept an "elementName" parameter.
 * <pre>
 *   JAnnotationType requestType = new JAnnotationType("RequestForEnhancement");
 *   JAnnotation request = new JAnnotation(requestType);
 *   request.setElementValue("id", "2868724");
 *   request.setElementValue("synopsis", "\"Provide time-travel functionality\"");
 *   request.setElementValue("engineer", "\"Mr. Peabody\"");
 *   request.setElementValue("date", "\"4/1/2004\"");
 * </pre>
 * outputs
 * <pre>
 *   &#064;RequestForEnhancement(
 *       id       = 2868724,
 *       sysopsis = "Provide time-travel functionality",
 *       engineer = "Mr. Peabody",
 *       date     = "4/1/2004")
 * </pre>
 * "Complex" annotations are also supported via the various setValue methods
 * that take a JAnnotation object.
 * <pre>
 *   JAnnotationType nameType = new JAnnotationType("Name");
 *   JAnnotationType authorType = new JAnnotationType("Author");
 *   JAnnotation author = new JAnnotation(authorType);
 *   JAnnotation name = new JAnnotation(nameType);
 *   name.setElementValue("first", "\"Joe\"");
 *   name.setElementValue("last", "\"Hacker\"");
 *   author.setValue(name);
 * </pre>
 * outputs
 * <pre>
 *   &#064;Author(&#064;Name(
 *       first = "Joe",
 *       last  = "Hacker"))
 * </pre>
 * Finally annotation elements whose types are arrays are supported via the
 * setValue methods that take arrays:
 * <pre>
 *   JAnnotationType endorsersType = new JAnnotationType("Endorsers");
 *   JAnnotation endorsers = new JAnnotation(endorsersType);
 *   endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});
 * </pre>
 * outputs
 * <pre>
 *   &#064;Endorsers(
 *       {
 *           "Children",
 *           "Unscrupulous dentists"
 *       })
 * </pre>
 * Note: Conditional element values are not currently supported. However the
 * setValue methods taking String values can be used to output this construct
 * literally if desired.
 *
 * @author <a href="mailto:andrew DOT fawcett AT coda DOTcom">Andrew Fawcett</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class JAnnotation {
    /**
     * Annotation type referenced by this annotation.
     */
    private JAnnotationType _annotationType;

    /**
     * Element values associated with this JAnnotation, contains String,
     * String[], JAnnotation and JAnnotation[] objects.
     */
    private OrderedHashMap _elementValues = new OrderedHashMap();

    /**
     * Name of a single element.
     */
    public static final String VALUE = "value";

    /**
     * Constructs a JAnnotation for the given annotation type.
     *
     * @param annotationType annotation type
     */
    public JAnnotation(final JAnnotationType annotationType) {
        _annotationType = annotationType;
    }

    /**
     * Returns the JAnnotationType associated with this JAnnotation.
     *
     * @return the JAnnotationType associated with this JAnnotation.
     */
    public JAnnotationType getAnnotationType() {
        return _annotationType;
    } //-- getAnnotationType

    /**
     * Sets the "value" annotation element value.
     *
     * @param stringValue literal String value
     */
    public void setValue(final String stringValue) {
        _elementValues.put(VALUE, stringValue);
    } //-- setValue

    /**
     * Sets the "value" annotation element value as a list.
     *
     * @param stringValue Array of literal String values
     */
    public void setValue(final String[] stringValue) {
        _elementValues.put(VALUE, stringValue);
    } //-- setValue

    /**
     * Sets the "value" annotation element value as an annotation.
     *
     * @param annotationValue JAnnotation to be used as this JAnnotation's value
     */
    public void setValue(final JAnnotation annotationValue) {
        _elementValues.put(VALUE, annotationValue);
    } //-- setValue

    /**
     * Sets the "value" annotation element value as a list of annotation values.
     *
     * @param annotationValues array of JAnnotations to be used as this
     *            JAnnotation's value
     */
    public void setValue(final JAnnotation[] annotationValues) {
        _elementValues.put(VALUE, annotationValues);
    } //-- setValue

    /**
     * Adds an annotation element name=value pair.
     *
     * @param elementName name of this annotation element
     * @param stringValue value of this annotation element
     */
    public void setElementValue(final String elementName, final String stringValue) {
        _elementValues.put(elementName, stringValue);
    } //-- setElementValue

    /**
     * Adds an annotation element name=list pair.
     *
     * @param elementName name of this annotation element
     * @param stringValues string array value of this annotation element
     */
    public void setElementValue(final String elementName, final String[] stringValues) {
        _elementValues.put(elementName, stringValues);
    } //-- setElementValue

    /**
     * Adds an annotation element name=annotation pair.
     *
     * @param elementName name of this annotation element
     * @param annotationValue annotation to be used as the value
     */
    public void setElementValue(final String elementName,
            final JAnnotation annotationValue) {
        _elementValues.put(elementName, annotationValue);
    } //-- setElementValue

    /**
     * Adds an annotation element name=array of annotations.
     *
     * @param elementName name of this annotation element
     * @param annotationValues array of annotations to be used as the value
     */
    public void setElementValue(final String elementName,
            final JAnnotation[] annotationValues) {
        _elementValues.put(elementName, annotationValues);
    } //-- setElementValue

    /**
     * Returns the annotation element value when it is a String.
     *
     * @return the annotation element value.
     */
    public String getValue() {
        Object elementValue = getElementValueObject(VALUE);
        if (elementValue instanceof String) { return (String) elementValue; }
        throw new IllegalStateException("'value' element is not of type String.");
    } //-- getValue

    /**
     * Returns the annotation element value when it is an annotation.
     *
     * @return the annotation element value when it is an annotation.
     */
    public JAnnotation getValueAnnotation() {
        Object elementValue = getElementValueObject(VALUE);
        if (elementValue instanceof JAnnotation) { return (JAnnotation) elementValue; }
        throw new IllegalStateException("'value' element is not of type JAnnotation.");
    } //-- getValueAnnotation

    /**
     * For the provided element name, returns the annotation element value when
     * it is a String.
     *
     * @param elementName element to return the value of
     * @return the annotation element value.
     */
    public String getElementValue(final String elementName) {
        Object elementValue = getElementValueObject(elementName);
        if (elementValue instanceof String) { return (String) elementValue; }
        throw new IllegalStateException(
                "'" + elementName + "' element is not of type String.");
    } //-- getElementValue

    /**
     * For the provided element name, returns the annotation element value when
     * it is an array of String.
     *
     * @param elementName element to return the value of
     * @return the annotation element value.
     */
    public String[] getElementValueList(final String elementName) {
        Object elementValue = getElementValueObject(elementName);
        if (elementValue instanceof String[]) { return (String[]) elementValue; }
        throw new IllegalStateException(
                "'" + elementName + "' element is not of type String[].");
    } //-- getElementValueList

    /**
     * Returns the given annotation element value as Object, typically used if
     * the value type is not known. This will either be a String or JAnnotation
     * or an array of String or an array of JAnnotation.
     *
     * @param elementName element to return the value of
     * @return annotation element value as Object
     */
    public Object getElementValueObject(final String elementName) {
        return _elementValues.get(elementName);
    } //-- getElementValueObject

    /**
     * For the provided element name, returns the annotation element value when
     * it is a JAnnotation.
     *
     * @param elementName element to return the value of
     * @return the annotation element value.
     */
    public JAnnotation getElementValueAnnotation(final String elementName) {
        Object elementValue = getElementValueObject(elementName);
        if (elementValue instanceof JAnnotation) { return (JAnnotation) elementValue; }
        throw new IllegalStateException(
                "'" + elementName + "' element is not of type JAnnotation.");
    } //-- getElementValueAnnotation

    /**
     * For the provided element name, returns the annotation element value when
     * it is an array of JAnnotation.
     *
     * @param elementName element to return the value of
     * @return the annotation element value.
     */
    public JAnnotation[] getElementValueAnnotationList(final String elementName) {
        Object elementValue = getElementValueObject(elementName);
        if (elementValue instanceof JAnnotation[]) {
            return (JAnnotation[]) elementValue;
        }
        throw new IllegalStateException(
                "'" + elementName + "' element is not of type JAnnotation[].");
    } //-- getElementValueAnnotationList

    /**
     * Returns the names of the elements set by this annotation.
     *
     * @return array of element names.
     */
    public String[] getElementNames() {
        return (String[]) _elementValues.keySet().toArray(
                new String[_elementValues.size()]);
    } //-- getElementNames

    /**
     * Prints the source code for this JAnnotation to the given JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to. Must not be null.
     */
    public void print(final JSourceWriter jsw) {
        jsw.write("@");
        jsw.write(_annotationType.getLocalName());
        jsw.write("(");
        // Single element annotation?
        String[] elementNames = getElementNames();
        if (elementNames.length == 1 && elementNames[0].equals(VALUE)) {
            // Just output value
            printElementValue(jsw, getElementValueObject(VALUE));
        } else if (elementNames.length > 0) {
            // Max element name length?
            int maxLength = 0;
            for (int i = 0; i < elementNames.length; i++) {
                int elementNameLength = elementNames[i].length();
                if (elementNameLength > maxLength) { maxLength = elementNameLength; }
            }
            // Output element name and values
            jsw.writeln();
            jsw.indent();
            for (int i = 0; i < elementNames.length; i++) {
                int elementNameLength = elementNames[i].length();
                // Output element name with padding
                jsw.write(elementNames[i]);
                for (int p = 0; p < maxLength - elementNameLength; p++) {
                    jsw.write(" ");
                }
                // Assignment operator
                jsw.write(" = ");
                // Value
                printElementValue(jsw, getElementValueObject(elementNames[i]));
                if (i < elementNames.length - 1) {
                    jsw.write(",");
                    jsw.writeln();
                }
            }
            jsw.unindent();
        }
        jsw.write(")");
    } //-- print

    /**
     * Prints annotation element value according to its type: String, String[],
     * JAnnotation or JAnnotation[].
     *
     * @param jsw the JSourceWriter to print to. Must not be null.
     * @param elementValue element value to print
     */
    private void printElementValue(final JSourceWriter jsw, final Object elementValue) {
        // String?
        if (elementValue instanceof String) {
            jsw.write((String) elementValue);
            return;
        } else if (elementValue instanceof JAnnotation) {
            JAnnotation annotation = (JAnnotation) elementValue;
            annotation.print(jsw);
            return;
        } else if (elementValue.getClass().isArray()) {
            // Short hand for single item list
            int listLength = Array.getLength(elementValue);
            if (listLength == 1) {
                printElementValue(jsw, Array.get(elementValue, 0));
                return;
            }
            // Output list items
            jsw.indent();
            jsw.writeln();
            jsw.write("{");
            jsw.writeln();
            jsw.indent();
            for (int i = 0; i < listLength; i++) {
                printElementValue(jsw, Array.get(elementValue, i));
                if (i < listLength - 1) { jsw.write(","); }
                jsw.writeln();
            }
            jsw.unindent();
            jsw.write("}");
            jsw.unindent();
            return;
        }
        throw new IllegalArgumentException("'" + elementValue + "' was not expected.");
    } //-- printElementValue

    /**
     * Test.
     * @param args command-line arguments.
     */
    public static void main(final String[] args) {
        JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));

        // Normal annotation
        test1(jsw);

        jsw.writeln();
        jsw.writeln();

        // Marker annotation
        test2(jsw);

        jsw.writeln();
        jsw.writeln();

        // Single element annotation
        test3(jsw);

        jsw.writeln();
        jsw.writeln();

        // Single element annotation with array list
        test4(jsw);

        jsw.writeln();
        jsw.writeln();

        // Single element annotation with array list (single item)
        test5(jsw);

        jsw.writeln();
        jsw.writeln();

        // Complex annotation (single element)
        test6(jsw);

        jsw.writeln();
        jsw.writeln();

        // Complex annotation (multi element)
        test7(jsw);

        jsw.writeln();
        jsw.writeln();

        // Complex annotation (multi element, annotation element list)
        test8(jsw);

        jsw.flush();
    }

    private static void test1(final JSourceWriter jsw) {
        JAnnotationType requestForEnhancementType1 = new JAnnotationType("RequestForEnhancement");
        JAnnotation requestForEnhancement1 = new JAnnotation(requestForEnhancementType1);
        requestForEnhancement1.setElementValue("id", "2868724");
        requestForEnhancement1.setElementValue("synopsis", "\"Provide time-travel functionality\"");
        requestForEnhancement1.setElementValue("engineer", "\"Mr. Peabody\"");
        requestForEnhancement1.setElementValue("date", "\"4/1/2004\"");
        requestForEnhancement1.print(jsw);
    }

    private static void test2(final JSourceWriter jsw) {
        JAnnotationType webMethodType2 = new JAnnotationType("WebMethod");
        JAnnotation webMethod2 = new JAnnotation(webMethodType2);
        webMethod2.print(jsw);
    }

    private static void test3(final JSourceWriter jsw) {
        JAnnotationType copyrightType3 = new JAnnotationType("Copyright");
        JAnnotation copyright3 = new JAnnotation(copyrightType3);
        copyright3.setValue("\"2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.\"");
        copyright3.print(jsw);
    }

    private static void test4(final JSourceWriter jsw) {
        JAnnotationType endorsersType4 = new JAnnotationType("Endorsers");
        JAnnotation endorsers4 = new JAnnotation(endorsersType4);
        endorsers4.setValue(new String[] {"\"Children\"", "\"Unscrupulous dentists\""});
        endorsers4.print(jsw);
    }

    private static void test5(final JSourceWriter jsw) {
        JAnnotationType endorsersType5 = new JAnnotationType("Endorsers");
        JAnnotation endorsers5 = new JAnnotation(endorsersType5);
        endorsers5.setValue(new String[] {"\"Epicurus\"" });
        endorsers5.print(jsw);
    }

    private static void test6(final JSourceWriter jsw) {
        JAnnotationType nameType6 = new JAnnotationType("Name");
        JAnnotationType authorType6 = new JAnnotationType("Author");
        JAnnotation author6 = new JAnnotation(authorType6);
        JAnnotation name6 = new JAnnotation(nameType6);
        name6.setElementValue("first", "\"Joe\"");
        name6.setElementValue("last", "\"Hacker\"");
        author6.setValue(name6);
        author6.print(jsw);
    }

    private static void test7(final JSourceWriter jsw) {
        JAnnotationType nameType7 = new JAnnotationType("Name");
        JAnnotationType authorType7 = new JAnnotationType("Author");
        JAnnotation author7 = new JAnnotation(authorType7);
        JAnnotation name7 = new JAnnotation(nameType7);
        name7.setElementValue("first", "\"Joe\"");
        name7.setElementValue("last", "\"Hacker\"");
        author7.setElementValue("name", name7);
        author7.setElementValue("rating", "Rating.GOOD");
        author7.print(jsw);
    }

    private static void test8(final JSourceWriter jsw) {
        JAnnotationType nameType8 = new JAnnotationType("Name");
        JAnnotationType authorType8 = new JAnnotationType("Author");
        JAnnotation author8 = new JAnnotation(authorType8);
        JAnnotation name81 = new JAnnotation(nameType8);
        name81.setElementValue("first", "\"Joe\"");
        name81.setElementValue("last", "\"Hacker\"");
        JAnnotation name82 = new JAnnotation(nameType8);
        name82.setElementValue("first", "\"Joe\"");
        name82.setElementValue("last", "\"Blogs\"");
        author8.setElementValue("name", new JAnnotation[] {name81, name82 });
        author8.setElementValue("rating", "Rating.GOOD");
        author8.print(jsw);
    }

}
