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
 * Class implements JAnnotatedElement interface on behalf of other classes 
 *   in this package that implement this interface.
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a> 
 */
public class JAnnotatedElementHelper
	implements JAnnotatedElement 
{
    
    // NOTE: Removed references to LinkedHashMap as we are trying to maintain
    // backward compatibility with JDK 1.2 and 1.3.
    
	/**
	 * Stores annotations associated with the source element containing this helper
	 */
	private OrderedHashMap _annotations;
    
	/**
	 * Creates a JAnnodatedElementHelper
	 */	
	public JAnnotatedElementHelper()
	{
        super();
	}
	
	/* (non-Javadoc)
	 * @see org.exolab.javasource.JAnnotatedElement#getAnnotation(org.exolab.javasource.JAnnotationType)
	 */
	public JAnnotation getAnnotation(JAnnotationType annotationType) {
        if (_annotations == null) return null;
		return (JAnnotation) _annotations.get(annotationType.getName());
	}

	/* (non-Javadoc)
	 * @see org.exolab.javasource.JAnnotatedElement#getAnnotations()
	 */
	public JAnnotation[] getAnnotations() {
        if (_annotations == null) 
            return new JAnnotation[0];
        
		return (JAnnotation[]) _annotations.values().toArray(new JAnnotation[_annotations.size()]);
	}

	/* (non-Javadoc)
	 * @see org.exolab.javasource.JAnnotatedElement#isAnnotationPresent(org.exolab.javasource.JAnnotation)
	 */
	public boolean isAnnotationPresent(JAnnotationType annotationType) {
        if (_annotations != null) {
        	return _annotations.containsKey(annotationType.getName());
        }
        return false;
	}

	/* (non-Javadoc)
	 * @see org.exolab.javasource.JAnnotatedElement#addAnnotation(org.exolab.javasource.JAnnotation)
	 */
	public void addAnnotation(JAnnotation annotation) {
		if(isAnnotationPresent(annotation.getAnnotationType()))
			throw new IllegalArgumentException("Annotation for '"+annotation.getAnnotationType().getName()+"' already added.");
		String annotationType = annotation.getAnnotationType().getName();
        if (_annotations == null) {
        	_annotations = new OrderedHashMap();
        }
        _annotations.put(annotationType, annotation);
	}

	/* (non-Javadoc)
	 * @see org.exolab.javasource.JAnnotatedElement#removeAnnotation(org.exolab.javasource.JAnnotation)
	 */
	public JAnnotation removeAnnotation(JAnnotationType annotationType) {
		if(isAnnotationPresent(annotationType)==false)
			throw new IllegalArgumentException("Annotation for '"+annotationType.getName()+"' not present.");
		return (JAnnotation) _annotations.remove(annotationType.getName());
	}

	/* (non-Javadoc)
	 * @see org.exolab.javasource.JAnnotatedElement#hasAnnotations()
	 */
	public boolean hasAnnotations() {
        if (_annotations != null) {
        	return _annotations.size()>0;
        }
        return false;
	}
	
	/**
	 * Outputs the list of annotations maintained by this object
	 * @param jsw
	 */
	public boolean printAnnotations(JSourceWriter jsw)
	{
		boolean printed = false;
        if (_annotations != null) {
    		Iterator annotations = _annotations.values().iterator();
    		while(annotations.hasNext())
    		{
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
	public static void main(String[] args)
	{
		JSourceWriter jsw = new JSourceWriter(new PrintWriter(System.out));
		
		{
			// Class annotation
			JClass lollipop = new JClass("Lollipop");
			JAnnotationType endorsersType = new JAnnotationType("org.xyz.Endorsers");
			JAnnotation endorsers = new JAnnotation(endorsersType);
			endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});			
			lollipop.addAnnotation(endorsers);
			lollipop.print(jsw);
		}

		jsw.writeln();
		jsw.writeln();
		
		{
			// Class annotation (multiple)
			JAnnotationType copyrightType = new JAnnotationType("org.xyz.Copyright");
			JAnnotation copyright = new JAnnotation(copyrightType);
			copyright.setValue("\"2002 Yoyodyne Propulsion Systems, Inc., All rights reserved.\"");
			JClass lollipop = new JClass("Lollipop");
			JAnnotationType endorsersType = new JAnnotationType("org.xyz.Endorsers");
			JAnnotation endorsers = new JAnnotation(endorsersType);
			endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});			
			lollipop.addAnnotation(endorsers);
			lollipop.addAnnotation(copyright);
			lollipop.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		{
			// Interface annotation
			JInterface lollipop = new JInterface("Lollipop");
			JAnnotationType endorsersType = new JAnnotationType("org.xyz.Endorsers");
			JAnnotation endorsers = new JAnnotation(endorsersType);
			endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});			
			lollipop.addAnnotation(endorsers);
			lollipop.print(jsw);
		}

		jsw.writeln();
		jsw.writeln();
		
		{	
			// Field annotation (JClass)
			JClass timeMachine = new JClass("EventProducer");
			JAnnotationType suppressWarningsType = new JAnnotationType("org.xyz.SuppressWarnings");
			JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
			JField field = new JField(new JClass("DocumentHandler"), "documentHandler");
			field.addAnnotation(suppressWarnings);
			timeMachine.addField(field);
			timeMachine.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln(); 
		
		{
			// Field annotation (JInterface)
			JInterface timeMachine = new JInterface("TimeMachine");
			JAnnotationType suppressWarningsType = new JAnnotationType("SuppressWarnings");
			JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
			JField field = new JField(new JClass("DocumentHandler"), "documentHandler");
			field.getModifiers().setStatic(true);
			field.getModifiers().makePublic();
			field.addAnnotation(suppressWarnings);
			timeMachine.addField(field);
			timeMachine.print(jsw);
		}		
		
		jsw.writeln();
		jsw.writeln();
	
		{
			// Method annotation (JClass)
			JClass timeMachine = new JClass("TimeMachine");
			JAnnotationType requestForEnhancementType = new JAnnotationType("org.xyz.RequestForEnhancement");
			JAnnotation requestForEnhancement = new JAnnotation(requestForEnhancementType);
			requestForEnhancement.setElementValue("id", "2868724");
			requestForEnhancement.setElementValue("sysopsis", "\"Provide time-travel functionality\"");
			requestForEnhancement.setElementValue("enginer", "\"Mr. Peabody\"");
			requestForEnhancement.setElementValue("date", "\"4/1/2004\"");
			JMethod travelThroughTime = new JMethod(null, "travelThroughTime");
			travelThroughTime.addAnnotation(requestForEnhancement);
			travelThroughTime.addParameter(new JParameter(new JClass("Date"), "date"));
			timeMachine.addMethod(travelThroughTime);
			timeMachine.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		{
			// Method annotation (JInterface)
			JInterface timeMachine = new JInterface("TimeMachine");
			JAnnotationType requestForEnhancementType = new JAnnotationType("RequestForEnhancement");
			JAnnotation requestForEnhancement = new JAnnotation(requestForEnhancementType);
			requestForEnhancement.setElementValue("id", "2868724");
			requestForEnhancement.setElementValue("sysopsis", "\"Provide time-travel functionality\"");
			requestForEnhancement.setElementValue("enginer", "\"Mr. Peabody\"");
			requestForEnhancement.setElementValue("date", "\"4/1/2004\"");
			JMethodSignature travelThroughTime = new JMethodSignature("travelThroughTime", null);
			travelThroughTime.addAnnotation(requestForEnhancement);
			travelThroughTime.addParameter(new JParameter(new JClass("Date"), "date"));
			timeMachine.addMethod(travelThroughTime);
			timeMachine.print(jsw);
		}		
		
		jsw.writeln();
		jsw.writeln();
				
		{
			// Constructor annotation
			JClass lollipop = new JClass("Lollipop");
			JAnnotationType endorsersType = new JAnnotationType("Endorsers");
			JAnnotation endorsers = new JAnnotation(endorsersType);
			endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});
			JConstructor constructor = new JConstructor(lollipop);
			constructor.addAnnotation(endorsers);			
			lollipop.addConstructor(constructor);
			lollipop.print(jsw);
		}
		
		jsw.writeln();
		jsw.writeln();
		
		{
			// Method parameter annotation (JInterface)
			JInterface timeMachine = new JInterface("EventProducer");
			JAnnotationType suppressWarningsType = new JAnnotationType("org.xyz.SuppressWarnings");
			JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
			JMethodSignature travelThroughTime = new JMethodSignature("produceEvents", null);
			JParameter parameter = new JParameter(new JClass("DocumentHandler"), "documentHandler");
			parameter.addAnnotation(suppressWarnings);
			travelThroughTime.addParameter(parameter);
			JParameter parameter1 = new JParameter(JType.Boolean, "asDocument");
			travelThroughTime.addParameter(parameter1);
			timeMachine.addMethod(travelThroughTime);
			timeMachine.print(jsw);					
		}
		
		jsw.writeln();
		jsw.writeln();
				
		{
			// Constructor parameter annotation
			JClass lollipop = new JClass("Lollipop");
			JConstructor constructor = new JConstructor(lollipop);
			JAnnotationType suppressWarningsType = new JAnnotationType("SuppressWarnings");
			JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
			JParameter parameter = new JParameter(new JClass("DocumentHandler"), "documentHandler");
			parameter.addAnnotation(suppressWarnings);
			constructor.addParameter(parameter);
			JParameter parameter1 = new JParameter(JType.Boolean, "asDocument");
			constructor.addParameter(parameter1);
			lollipop.addConstructor(constructor);
			lollipop.print(jsw);
		}
		
		jsw.flush();
	}
}
