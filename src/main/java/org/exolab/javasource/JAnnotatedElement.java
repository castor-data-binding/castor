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
 * Defines methods for manipulating annotations held against various
 * program code elements, such as classes, fields, methods etc. This interface
 * is similar to the java.lang.reflect.AnnotatedElement except that it also
 * allows modifications of associated annotations. It is implemented by the
 * classes within this package that represent applicable code elements.
 * <p>
 * Adding the class annotations
 * <pre>
 *   JClass lollipop = new JClass("Lollipop");
 *   JAnnotationType endorsersType = new JAnnotationType("Endorsers");
 *   JAnnotation endorsers = new JAnnotation(endorsersType);
 *   endorsers.setValue(new String[] { "\"Children\"", "\"Unscrupulous dentists\""});
 *   lollipop.addAnnotation(endorsers);
 * </pre>
 * outputs
 * <pre>
 *   &#064;Endorsers(
 *       {
 *           "Children",
 *           "Unscrupulous dentists"
 *       })
 *   public class Lollipop {
 *   } 
 * </pre>
 * Adding the method annotations
 * <pre>
 *   JClass timeMachine = new JClass("TimeMachine");
 *   JAnnotationType requestType = new JAnnotationType("RequestForEnhancement");
 *   JAnnotation request = new JAnnotation(requestType);
 *   request.setElementValue("id", "2868724");
 *   request.setElementValue("synopsis", "\"Provide time-travel functionality\"");
 *   request.setElementValue("engineer", "\"Mr. Peabody\"");
 *   request.setElementValue("date", "\"4/1/2004\"");
 *   JMethod travelThroughTime = new JMethod(null, "travelThroughTime");
 *   travelThroughTime.addAnnotation(request);
 *   travelThroughTime.addParameter(new JParameter(new JClass("Date"), "date"));
 *   timeMachine.addMethod(travelThroughTime);
 * </pre>
 * outputs
 * <pre>
 *   &#064;RequestForEnhancement(
 *       id       = 2868724,
 *       synopsis = "Provide time-travel functionality",
 *       engineer = "Mr. Peabody",
 *       date     = "4/1/2004")
 *   public void travelThroughTime(Date date)
 *   {
 *   }  
 * </pre>
 * Adding the field annotations
 * <pre>
 *   JClass timeMachine = new JClass("EventProducer");
 *   JAnnotationType suppressWarningsType = new JAnnotationType("SuppressWarnings");
 *   JAnnotation suppressWarnings = new JAnnotation(suppressWarningsType);
 *   JField field = new JField(new JClass("DocumentHandler"), "documentHandler");
 *   field.addAnnotation(suppressWarnings);
 *   timeMachine.addField(field);
 * </pre>
 * outputs
 * <pre>
 *   &#064;SuppressWarnings()
 *   private DocumentHandler documentHandler;
 * </pre>
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a>
 */
public interface JAnnotatedElement {
    /**
     * Retrieves a JAnnotation for the given JAnnotationType, returns null if no
     * annotation has been set.
     * 
     * @param annotationType
     * @return A JAnnotation for the given JAnnotationType
     */
    JAnnotation getAnnotation(JAnnotationType annotationType);
    
    /**
     * Returns a list of JAnnotation's already set on this source element
     * 
     * @return A list of all JAnnotations associated with this source element
     */
    JAnnotation[] getAnnotations();
    
    /**
     * Returns true if a JAnnotation exists for the given JAnnotationType
     * 
     * @param annotationType
     * @return True if a JAnnotation has been added for the given
     *         JAnnotationType
     */
    boolean isAnnotationPresent(JAnnotationType annotationType);
    
    /**
     * Adds a JAnnotation to this source element. An IllegalArgumentException is
     * thrown if one already exists for the associated JAnnotationType.
     * 
     * @param annotation a JAnnotation to add to this source element
     */
    void addAnnotation(JAnnotation annotation);
    
    /**
     * Removes the JAnnotation from this source element for the given
     * JAnnotationType. An IllegalArgumentException is thrown if the provided
     * JAnnotation isn't present.
     * 
     * @param annotationType Annotation type to remove
     * @return The JAnnotation that was associated with this source element
     */
    JAnnotation removeAnnotation(JAnnotationType annotationType);
    
    /**
     * Returns true if this source element has any annotations
     * 
     * @return Returns true if this source element has any annotations
     */
    boolean hasAnnotations();
}
