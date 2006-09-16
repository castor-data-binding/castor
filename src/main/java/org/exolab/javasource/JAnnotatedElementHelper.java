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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: JAnnotatedElementHelper
 */
package org.exolab.javasource;

import java.io.PrintWriter;
import java.util.Iterator;

import org.exolab.castor.util.OrderedHashMap;

/**
 * Implements JAnnotatedElement interface on behalf of other classes in this
 * package that implement this interface.
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a>
 */
public class JAnnotatedElementHelper implements JAnnotatedElement {
    // NOTE: Removed references to LinkedHashMap as we are trying to maintain
    // backward compatibility with JDK 1.2 and 1.3.
    
    /**
     * Stores annotations associated with the source element containing this
     * helper
     */
    private OrderedHashMap _annotations;
    
    /**
     * Creates a JAnnodatedElementHelper
     */ 
    public JAnnotatedElementHelper() {
        super();
    }
    
    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #getAnnotation(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public final JAnnotation getAnnotation(final JAnnotationType annotationType) {
        if (_annotations == null) { return null; }
        return (JAnnotation) _annotations.get(annotationType.getName());
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement#getAnnotations()
     * {@inheritDoc}
     */
    public final JAnnotation[] getAnnotations() {
        if (_annotations == null) { return new JAnnotation[0]; }
        return (JAnnotation[]) _annotations.values().toArray(
                new JAnnotation[_annotations.size()]);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #isAnnotationPresent(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public final boolean isAnnotationPresent(final JAnnotationType annotationType) {
        if (_annotations != null) {
            return _annotations.containsKey(annotationType.getName());
        }
        return false;
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #addAnnotation(org.exolab.javasource.JAnnotation)
     * {@inheritDoc}
     */
    public final void addAnnotation(final JAnnotation annotation) {
        if (isAnnotationPresent(annotation.getAnnotationType())) {
            throw new IllegalArgumentException(
                    "Annotation for '" + annotation.getAnnotationType().getName()
                    + "' already added.");
        }
        String annotationType = annotation.getAnnotationType().getName();
        if (_annotations == null) { _annotations = new OrderedHashMap(); }
        _annotations.put(annotationType, annotation);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #removeAnnotation(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public final JAnnotation removeAnnotation(final JAnnotationType annotationType) {
        if (!isAnnotationPresent(annotationType)) {
            throw new IllegalArgumentException(
                    "Annotation for '" + annotationType.getName() + "' not present.");
        }
        return (JAnnotation) _annotations.remove(annotationType.getName());
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement#hasAnnotations()
     * {@inheritDoc}
     */
    public final boolean hasAnnotations() {
        if (_annotations != null) {
            return _annotations.size() > 0;
        }
        return false;
    }

    /**
     * Outputs the list of annotations maintained by this object
     * 
     * @param jsw the JSourceWriter to print the annotations to
     * @return true if at least one annotation was printed, false otherwise.
     */
    public final boolean printAnnotations(final JSourceWriter jsw) {
        boolean printed = false;
        if (_annotations != null) {
            Iterator annotations = _annotations.values().iterator();
            while (annotations.hasNext()) {
                JAnnotation annotation = (JAnnotation) annotations.next();
                annotation.print(jsw);
                jsw.writeln();
                printed = true;
            }
        }
        return printed;
    }

    /**
     * Test
     * @param args
     */
    public static void main(final String[] args) {
        JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
        
        test1(jsw);

        jsw.writeln();
        jsw.writeln();

        test2(jsw);
        
        jsw.writeln();
        jsw.writeln();

        test3(jsw);

        jsw.writeln();
        jsw.writeln();

        test4(jsw);
        
        jsw.writeln();
        jsw.writeln(); 

        test5(jsw);
        
        jsw.writeln();
        jsw.writeln();

        test6(jsw);
        
        jsw.writeln();
        jsw.writeln();

        test7(jsw);
        
        jsw.writeln();
        jsw.writeln();

        test8(jsw);
        
        jsw.writeln();
        jsw.writeln();

        test9(jsw);
        
        jsw.writeln();
        jsw.writeln();

        test10(jsw);
        
        jsw.flush();
    }
    
    public static void test1(final JSourceWriter jsw) {
        // Class annotation
        JClass lollipop1 = new JClass("Lollipop");
        JAnnotationType endorsersType1 = new JAnnotationType("org.xyz.Endorsers");
        JAnnotation endorsers1 = new JAnnotation(endorsersType1);
        endorsers1.setValue(new String[] {"\"Children\"", "\"Unscrupulous dentists\""});
        lollipop1.addAnnotation(endorsers1);
        lollipop1.print(jsw);
    }
    
    public static void test2(final JSourceWriter jsw) {
        // Class annotation (multiple)
        JAnnotationType copyrightType = new JAnnotationType("org.xyz.Copyright");
        JAnnotation copyright = new JAnnotation(copyrightType);
        copyright.setValue(
                "\"2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.\"");
        JClass lollipop2 = new JClass("Lollipop");
        JAnnotationType endorsersType2 = new JAnnotationType("org.xyz.Endorsers");
        JAnnotation endorsers2 = new JAnnotation(endorsersType2);
        endorsers2.setValue(new String[] {"\"Children\"", "\"Unscrupulous dentists\""});
        lollipop2.addAnnotation(endorsers2);
        lollipop2.addAnnotation(copyright);
        lollipop2.print(jsw);
    }
    
    public static void test3(final JSourceWriter jsw) {
        // Interface annotation
        JInterface lollipop3 = new JInterface("Lollipop");
        JAnnotationType endorsersType3 = new JAnnotationType("org.xyz.Endorsers");
        JAnnotation endorsers3 = new JAnnotation(endorsersType3);
        endorsers3.setValue(new String[] {"\"Children\"", "\"Unscrupulous dentists\""});
        lollipop3.addAnnotation(endorsers3);
        lollipop3.print(jsw);
    }
    
    public static void test4(final JSourceWriter jsw) {
        // Field annotation (JClass)
        JClass timeMachine1 = new JClass("EventProducer");
        JAnnotationType suppressWarningsType1 = new JAnnotationType(
                "org.xyz.SuppressWarnings");
        JAnnotation suppressWarnings1 = new JAnnotation(suppressWarningsType1);
        JField field1 = new JField(new JClass("DocumentHandler"), "documentHandler");
        field1.addAnnotation(suppressWarnings1);
        timeMachine1.addField(field1);
        timeMachine1.print(jsw);
    }
    
    public static void test5(final JSourceWriter jsw) {
        // Field annotation (JInterface)
        JInterface timeMachine2 = new JInterface("TimeMachine");
        JAnnotationType suppressWarningsType2 = new JAnnotationType("SuppressWarnings");
        JAnnotation suppressWarnings2 = new JAnnotation(suppressWarningsType2);
        JField field2 = new JField(new JClass("DocumentHandler"), "documentHandler");
        field2.getModifiers().setStatic(true);
        field2.getModifiers().makePublic();
        field2.addAnnotation(suppressWarnings2);
        timeMachine2.addField(field2);
        timeMachine2.print(jsw);
    }
    
    public static void test6(final JSourceWriter jsw) {
        // Method annotation (JClass)
        JClass timeMachine3 = new JClass("TimeMachine");
        JAnnotationType requestForEnhancementType3 = new JAnnotationType(
                "org.xyz.RequestForEnhancement");
        JAnnotation requestForEnhancement3 = new JAnnotation(requestForEnhancementType3);
        requestForEnhancement3.setElementValue("id", "2868724");
        requestForEnhancement3.setElementValue(
                "sysopsis", "\"Provide time-travel functionality\"");
        requestForEnhancement3.setElementValue("enginer", "\"Mr. Peabody\"");
        requestForEnhancement3.setElementValue("date", "\"4/1/2004\"");
        JMethod travelThroughTime3 = new JMethod(null, "travelThroughTime");
        travelThroughTime3.addAnnotation(requestForEnhancement3);
        travelThroughTime3.addParameter(new JParameter(new JClass("Date"), "date"));
        timeMachine3.addMethod(travelThroughTime3);
        timeMachine3.print(jsw);
    }
    
    public static void test7(final JSourceWriter jsw) {
        // Method annotation (JInterface)
        JInterface timeMachine4 = new JInterface("TimeMachine");
        JAnnotationType requestForEnhancementType4 = new JAnnotationType(
                "RequestForEnhancement");
        JAnnotation requestForEnhancement4 = new JAnnotation(requestForEnhancementType4);
        requestForEnhancement4.setElementValue("id", "2868724");
        requestForEnhancement4.setElementValue(
                "sysopsis", "\"Provide time-travel functionality\"");
        requestForEnhancement4.setElementValue("enginer", "\"Mr. Peabody\"");
        requestForEnhancement4.setElementValue("date", "\"4/1/2004\"");
        JMethodSignature travelThroughTime4 = new JMethodSignature(
                "travelThroughTime", null);
        travelThroughTime4.addAnnotation(requestForEnhancement4);
        travelThroughTime4.addParameter(new JParameter(new JClass("Date"), "date"));
        timeMachine4.addMethod(travelThroughTime4);
        timeMachine4.print(jsw);
    }

    public static void test8(final JSourceWriter jsw) {
        // Constructor annotation
        JClass lollipop4 = new JClass("Lollipop");
        JAnnotationType endorsersType4 = new JAnnotationType("Endorsers");
        JAnnotation endorsers4 = new JAnnotation(endorsersType4);
        endorsers4.setValue(new String[] {"\"Children\"", "\"Unscrupulous dentists\""});
        JConstructor constructor4 = new JConstructor(lollipop4);
        constructor4.addAnnotation(endorsers4);           
        lollipop4.addConstructor(constructor4);
        lollipop4.print(jsw);
    }

    public static void test9(final JSourceWriter jsw) {
        // Method parameter annotation (JInterface)
        JInterface timeMachine5 = new JInterface("EventProducer");
        JAnnotationType suppressWarningsType5 = new JAnnotationType(
                "org.xyz.SuppressWarnings");
        JAnnotation suppressWarnings5 = new JAnnotation(suppressWarningsType5);
        JMethodSignature travelThroughTime5 = new JMethodSignature("produceEvents", null);
        JParameter parameter51 = new JParameter(
                new JClass("DocumentHandler"), "documentHandler");
        parameter51.addAnnotation(suppressWarnings5);
        travelThroughTime5.addParameter(parameter51);
        JParameter parameter52 = new JParameter(JType.BOOLEAN, "asDocument");
        travelThroughTime5.addParameter(parameter52);
        timeMachine5.addMethod(travelThroughTime5);
        timeMachine5.print(jsw);                 
    }

    public static void test10(final JSourceWriter jsw) {
        // Constructor parameter annotation
        JClass lollipop6 = new JClass("Lollipop");
        JConstructor constructor6 = new JConstructor(lollipop6);
        JAnnotationType suppressWarningsType6 = new JAnnotationType("SuppressWarnings");
        JAnnotation suppressWarnings6 = new JAnnotation(suppressWarningsType6);
        JParameter parameter61 = new JParameter(
                new JClass("DocumentHandler"), "documentHandler");
        parameter61.addAnnotation(suppressWarnings6);
        constructor6.addParameter(parameter61);
        JParameter parameter62 = new JParameter(JType.BOOLEAN, "asDocument");
        constructor6.addParameter(parameter62);
        lollipop6.addConstructor(constructor6);
        lollipop6.print(jsw);
    }
}
