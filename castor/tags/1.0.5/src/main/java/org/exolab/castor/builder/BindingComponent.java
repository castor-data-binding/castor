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
 * Copyright 2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.XSType;

/**
 * This interface is the abstraction of any type of source that can interact
 * with the Source Code Generator. From the Source Code Generator point of view,
 * the source document used to generate Java source code is totally transparent
 * and is not exposed.
 * <p>
 * Specific implementation of that class will represent the source document
 * used. For instance when generating source code from an XML Schema, the source
 * generator will interact with an
 * {@link org.exolab.castor.builder.binding.XMLBindingComponent} whereas when
 * generating source code from an UML model object model, the source generator
 * will interact with an UMLBindingComponent (This is obviously just an example,
 * no UML Object Model has been as of today integrated in Castor).
 * <p>
 * A binding component can be of three different types:
 * <ul>
 *   <li>MEMBER: this type of BindingComponent will represent a java class
 *       member.</li>
 *   <li>INTERFACE: this type of BindingComponent will represent a java
 *       interface.</li>
 *   <li>CLASS: this type of BindingComponent will represent a java class.</li>
 * </ul>
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public interface BindingComponent {

    //--Constants to represent the different types.
    short INTERFACE = 0;
    short CLASS     = 1;
    short MEMBER    = 2;

    /**
     * Returns true if the given Object is equal to this instance of
     * BindingComponent.
     *
     * @param object the object to compare to this instance
     * @return true if the given Object is equal to this instance of
     *         BindingComponent.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    boolean equals(Object object);

    /**
     * Returns the name of collection type such as 'arraylist' in which we will
     * store the different occurrences of the java member generated to represent
     * that BindingComponent.
     *
     * @return a string that represents the collection type such as 'arraylist'
     *         name in which we will store the different occurrences of the java
     *         member generated to represent that BindingComponent.
     */
    String getCollectionType();

    /**
     * Returns the name of a super class for the current BindingComponent. Null
     * is returned if this BindingComponent is not meant to be mapped to a java
     * class.
     *
     * @return the name of a super class for the current BindingComponent. Null
     *         is returned if this BindingComponent is not meant to be mapped to
     *         a java class.
     */
    String getExtends();

    /**
     * Returns an array of the different interface names implemented by the
     * class that will represent the current BindingComponent. Null is returned
     * if this BindingComponent is not meant to be mapped to a java class.
     *
     * @return an array of the different interface names implemented by the
     *         class that will represent the current BindingComponent. Null is
     *         returned if this BindingComponent is not meant to be mapped to a
     *         java class.
     */
    String[] getImplements();

    /**
     * Returns a valid Java Class Name corresponding to this BindingComponent.
     * This name is not qualified, this is only a local Java class name.
     *
     * @return a valid Java Class Name corresponding to this BindingComponent.
     *         This name is not qualified, this is only a local Java class name.
     * @see #getQualifiedName
     */
    String getJavaClassName();

    /**
     * Returns a valid Java Member Name corresponding to this BindingComponent.
     * This name is not qualified, this is only a local Java Member name.
     *
     * @return a valid Java Member Name corresponding to this BindingComponent.
     *         This name is not qualified, this is only a local Java member
     *         name.
     * @see #getQualifiedName
     */
    String getJavaMemberName();

    /**
     * Returns the java package associated with this BindingComponent.
     *
     * @return the java package associated with this BindingComponent.
     */
    String getJavaPackage();

    /**
     * Returns the XSType that corresponds to the Java type chosen to represent
     * this BindingComponent. An XSType is an abstraction of a Java type used in
     * the Source Generator. It wraps a JType as well as the necessary methods
     * to convert to/from String.
     *
     * @return an XSType that corresponds to the Java type chosen to represent
     *         this BindingComponent.
     */
    XSType getJavaType();

    /**
     * Returns the lower bound of the collection that is generated from this
     * BindingComponent. The lower bound is a positive integer.
     *
     * @return an int representing the lower bound of the collection generated
     *         from this BindingComponent.
     */
    int getLowerBound();

    /**
     * Returns a fully qualified java class name. This name corresponds to the
     * class name that will be generated from this BindingComponent.
     *
     * @return a fully qualified java class name. This name corresponds to the
     *         class name corresponding to this BindingComponent.
     */
    String getQualifiedName();

    /**
     * Returns the type of this component binding. A component binding can be of
     * three different types:
     * <ul>
     *   <li>Interface: it represents the binding to a java interface.</li>
     *   <li>Class: it represents the binding to a java class.</li>
     *   <li>Member: it represents the binding to a java class member.</li>
     * </ul>
     * -1 is returned if the component binding is null.
     *
     * @return the type of this component binding.
     */
    short getType();

    /**
     * Returns the upper bound of the collection that is generated from this
     * BindingComponent. The upper bound is a positive integer. -1 is returned
     * to indicate that the upper bound is unbounded.
     *
     * @return an int representing the lower bound of the collection generated
     *         from this BindingComponent. -1 is returned to indicate that the
     *         upper bound is uinbounded.
     */
    int getUpperBound();

    /**
     * Returns the default value of the member generated from this binding
     * component. The value is returned as its string representation.
     *
     * @return a string representation of default value for the member generated
     *         from this binding component.
     */
    String getValue();

    /**
     * Returns the fully qualified name of the Validator to use.
     *
     * @return the fully qualified name of the Validator to use.
     */
    String getValidator();

    /**
     * Returns the fully qualified name of the XMLFieldHandler to use. This
     * handler will be used when generating ClassDescriptors meant to be used in
     * the marshalling framework.
     *
     * @return the fully qualified name of the XMLFieldHandler to use.
     */
    String getXMLFieldHandler();

    /**
     * Returns true if bound properties must be generated for the class that
     * will represent the current BindingComponent.
     *
     * @return true if bound properties must be generated for the class the
     *         class that will represent the current BindingComponent.
     */
    boolean hasBoundProperties();

    /**
     * Returns true if equal method must be generated for the class that will
     * represent the current BindingComponent.
     *
     * @return true if equal method must be generated for the class the class
     *         that will represent the current BindingComponent.
     */
    boolean hasEquals();

    /**
     * Returns the hashCode value for this object.
     *
     * @return the hashcode value for this object.
     * @see java.lang.Object#hashCode()
     */
    int hashCode();

    /**
     * Returns true if the class generated from the current BindingComponent
     * will be abstract.
     *
     * @return true if the class generated from the current BindingComponent
     *         will be abstract.
     */
    boolean isAbstract();

    /**
     * Returns true if the class generated from the current BindingComponent
     * will be final.
     *
     * @return true if the class generated from the current BindingComponent
     *         will be final.
     */
    boolean isFinal();

    /**
     * Returns true if the member represented by that BindingComponent is to be
     * represented by an Object wrapper. For instance an int will be represented
     * by a java Integer if the property is set to true.
     *
     * @return true if the member represented by that BindingComponent is to be
     *         represented by an Object wrapper.
     */
    boolean useWrapper();

}
