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
        
        // Class annotation
        test1(jsw);

        jsw.writeln();
        jsw.writeln();

        // Class annotation (multiple)
        test2(jsw);
        
        jsw.writeln();
        jsw.writeln();

        // Interface annotation
        test3(jsw);

        jsw.writeln();
        jsw.writeln();

        // Field annotation (JClass)
        test4(jsw);
        
        jsw.writeln();
        jsw.writeln(); 

        // Field annotation (JInterface)
        test5(jsw);
        
        jsw.writeln();
        jsw.writeln();

        // Method annotation (JClass)
        test6(jsw);
        
        jsw.writeln();
        jsw.writeln();

        // Method annotation (JInterface)
        test7(jsw);
        
        jsw.writeln();
        jsw.writeln();

        // Constructor annotation
        test8(jsw);
        
        jsw.writeln();
        jsw.writeln();

        // Method parameter annotation (JInterface)
        test9(jsw);
        
        jsw.writeln();
        jsw.writeln();

        // Constructor parameter annotation
        test10(jsw);
        
        jsw.flush();
    }
    
    private static void test1(final JSourceWriter jsw) {
        JAnnotation endorsers = makeTestEndorserAnnotation();
        JClass lollipop = new JClass("Lollipop");
        lollipop.addAnnotation(endorsers);
        lollipop.print(jsw);
    }

    private static void test2(final JSourceWriter jsw) {
        JAnnotation copyright = makeTestCopyrightAnnotation();
        JAnnotation endorsers = makeTestEndorserAnnotation();
        JClass lollipop = new JClass("Lollipop");
        lollipop.addAnnotation(endorsers);
        lollipop.addAnnotation(copyright);
        lollipop.print(jsw);
    }

    private static void test3(final JSourceWriter jsw) {
        JAnnotation endorsers = makeTestEndorserAnnotation();
        JInterface lollipop = new JInterface("Lollipop");
        lollipop.addAnnotation(endorsers);
        lollipop.print(jsw);
    }
    
    private static void test4(final JSourceWriter jsw) {
        JAnnotationType suppressWarningsType = new JAnnotationType("org.xyz.SuppressWarnings");
        JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
        JField field = new JField(new JClass("DocumentHandler"), "documentHandler");
        field.addAnnotation(suppressWarnings);
        JClass timeMachine = new JClass("EventProducer");
        timeMachine.addField(field);
        timeMachine.print(jsw);
    }
    
    private static void test5(final JSourceWriter jsw) {
        JAnnotationType suppressWarningsType = new JAnnotationType("SuppressWarnings");
        JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
        JField field = new JField(new JClass("DocumentHandler"), "documentHandler");
        field.getModifiers().setStatic(true);
        field.getModifiers().makePublic();
        field.addAnnotation(suppressWarnings);
        JInterface timeMachine = new JInterface("TimeMachine");
        timeMachine.addField(field);
        timeMachine.print(jsw);
    }
    
    private static void test6(final JSourceWriter jsw) {
        JAnnotation requestForEnhancement = makeTestRFEAnnotation();
        JMethod travelThroughTime = new JMethod(null, "travelThroughTime");
        travelThroughTime.addAnnotation(requestForEnhancement);
        travelThroughTime.addParameter(new JParameter(new JClass("Date"), "date"));
        JClass timeMachine = new JClass("TimeMachine");
        timeMachine.addMethod(travelThroughTime);
        timeMachine.print(jsw);
    }

    private static void test7(final JSourceWriter jsw) {
        JAnnotation requestForEnhancement = makeTestRFEAnnotation();
        JMethodSignature travelThroughTime = new JMethodSignature("travelThroughTime", null);
        travelThroughTime.addAnnotation(requestForEnhancement);
        travelThroughTime.addParameter(new JParameter(new JClass("Date"), "date"));
        JInterface timeMachine = new JInterface("TimeMachine");
        timeMachine.addMethod(travelThroughTime);
        timeMachine.print(jsw);
    }

    private static void test8(final JSourceWriter jsw) {
        JAnnotation endorsers = makeTestEndorserAnnotation();
        JClass lollipop = new JClass("Lollipop");
        JConstructor constructor = new JConstructor(lollipop);
        constructor.addAnnotation(endorsers);           
        lollipop.addConstructor(constructor);
        lollipop.print(jsw);
    }

    private static void test9(final JSourceWriter jsw) {
        JAnnotationType suppressWarningsType = new JAnnotationType("org.xyz.SuppressWarnings");
        JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
        JMethodSignature travelThroughTime = new JMethodSignature("produceEvents", null);
        JParameter parameter1 = new JParameter(new JClass("DocumentHandler"), "documentHandler");
        parameter1.addAnnotation(suppressWarnings);
        travelThroughTime.addParameter(parameter1);
        JParameter parameter2 = new JParameter(JType.BOOLEAN, "asDocument");
        travelThroughTime.addParameter(parameter2);
        JInterface timeMachine = new JInterface("EventProducer");
        timeMachine.addMethod(travelThroughTime);
        timeMachine.print(jsw);                 
    }

    private static void test10(final JSourceWriter jsw) {
        JAnnotationType suppressWarningsType = new JAnnotationType("SuppressWarnings");
        JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
        JParameter parameter1 = new JParameter(new JClass("DocumentHandler"), "documentHandler");
        JParameter parameter2 = new JParameter(JType.BOOLEAN, "asDocument");
        parameter1.addAnnotation(suppressWarnings);
        JClass lollipop = new JClass("Lollipop");
        JConstructor constructor = new JConstructor(lollipop);
        constructor.addParameter(parameter1);
        constructor.addParameter(parameter2);
        lollipop.addConstructor(constructor);
        lollipop.print(jsw);
    }

    private static JAnnotation makeTestCopyrightAnnotation() {
        JAnnotationType copyrightType = new JAnnotationType("org.xyz.Copyright");
        JAnnotation copyright = new JAnnotation(copyrightType);
        copyright.setValue("\"2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.\"");
        return copyright;
    }
    
    private static JAnnotation makeTestEndorserAnnotation() {
        JAnnotationType endorsersType = new JAnnotationType("org.xyz.Endorsers");
        JAnnotation endorsers = new JAnnotation(endorsersType);
        endorsers.setValue(new String[] {"\"Children\"", "\"Unscrupulous dentists\""});
        return endorsers;
    }

    private static JAnnotation makeTestRFEAnnotation() {
        JAnnotationType requestForEnhancementType = new JAnnotationType("org.xyz.RequestForEnhancement");
        JAnnotation requestForEnhancement = new JAnnotation(requestForEnhancementType);
        requestForEnhancement.setElementValue("id", "2868724");
        requestForEnhancement.setElementValue("synopsis", "\"Provide time-travel functionality\"");
        requestForEnhancement.setElementValue("engineer", "\"Mr. Peabody\"");
        requestForEnhancement.setElementValue("date", "\"4/1/2004\"");
        return requestForEnhancement;
    }

}
