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
 * $Id: JAnnotatedElement.java
 *
 * Contributors:
 * --------------
 * Andrew Fawcett (andrew.fawcett@coda.com) - Original Author
 */
package org.exolab.javasource;

/**
 * Interface defines methods for manipuling annotations held against various
 *   program code elements, such as classes, fields, methods etc. This interface 
 *   is simalar to the java.lang.reflect.AnnotatedElement. Accept that it also 
 *   allows modifications of associated annotations. It is implemented by the 
 *   classes within this package that represent applicable code elements. 
 * 
 *   Adding class annotations
 * 
 *   JClass lollipop = new JClass("Lollipop");
 *   JAnnotationType endorsersType = new JAnnotationType("Endorsers");
 *   JAnnotation endorsers = new JAnnotation(endorsersType);
 *   endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});			
 *   lollipop.addAnnotation(endorsers);
 * 
 *   Outputs
 * 
 *   @Endorsers(
 *       {
 *           "Children",
 *           "Unscrupulous dentists"
 *       })
 *   public class Lollipop {
 *   } 
 * 
 *   Adding method annotations
 * 
 *   JClass timeMachine = new JClass("TimeMachine");
 *   JAnnotationType requestForEnhancementType = new JAnnotationType("RequestForEnhancement");
 *   JAnnotation requestForEnhancement = new JAnnotation(requestForEnhancementType);
 *   requestForEnhancement.setElementValue("id", "2868724");
 *   requestForEnhancement.setElementValue("sysopsis", "\"Provide time-travel functionality\"");
 *   requestForEnhancement.setElementValue("enginer", "\"Mr. Peabody\"");
 *   requestForEnhancement.setElementValue("date", "\"4/1/2004\"");
 *   JMethod travelThroughTime = new JMethod(null, "travelThroughTime");
 *   travelThroughTime.addAnnotation(requestForEnhancement);
 *   travelThroughTime.addParameter(new JParameter(new JClass("Date"), "date"));
 *   timeMachine.addMethod(travelThroughTime);
 * 
 *   Outputs
 *  
 *   @RequestForEnhancement(
 *       id       = 2868724,
 *       sysopsis = "Provide time-travel functionality",
 *       enginer  = "Mr. Peabody",
 *       date     = "4/1/2004")
 *   public void travelThroughTime(Date date)
 *   {
 *   }  
 * 
 *   Adding field annotations
 * 
 *   JClass timeMachine = new JClass("EventProducer");
 *   JAnnotationType suppressWarningsType = new JAnnotationType("SuppressWarnings");
 *   JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
 *   JField field = new JField(new JClass("DocumentHandler"), "documentHandler");
 *   field.addAnnotation(suppressWarnings);
 *   timeMachine.addField(field);
 * 
 *   Outputs
 * 
 *   @SuppressWarnings()
 *   private DocumentHandler documentHandler;
 * 
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a> 
 */
public interface JAnnotatedElement 
{
	/**
	 * Retrieves a JAnnotation for the given JAnnotationType, returns null 
	 *   if no annotation has been set.
	 * @param annotationType
	 * @return A JAnnotation for the given JAnnotationType
	 */
	public JAnnotation getAnnotation(JAnnotationType annotationType);
	/**
	 * Returns a list of JAnnotation's already set on this source element
	 * @return A list of all JAnnotations associated with this source element
	 */
	public JAnnotation[] getAnnotations();
	/**
	 * Returns true if a JAnnotation exists for the given JAnnotationType
	 * @param annotationType
	 * @return True if a JAnnotation has been added for the given JAnnotationType
	 */
	public boolean isAnnotationPresent(JAnnotationType annotationType);
	/**
	 * Adds a JAnnotation to this source element. An IllegalArgumentException
	 *   is thrown if one already exists for the associated JAnnotationType.
	 * @param annotation
	 */
	public void addAnnotation(JAnnotation annotation)
		throws IllegalArgumentException;
	/**
	 * Removes the JAnnotation from this source element for the given JAnnotationType,
	 *   throws a IllegalArgumentException if no JAnnotation has been added.
	 * @param annotationType
	 * @return The JAnnotation that was associated with this source element
	 */
	public JAnnotation removeAnnotation(JAnnotationType annotationType)
		throws IllegalArgumentException;
	/**
	 * Returns true if annotations have been added to this source element
	 * @return Returns true if annotations have been added to this source element
	 */
	public boolean hasAnnotations();
}