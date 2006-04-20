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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import java.lang.reflect.*;

/**
 * A class used to obtain information about how to handle Object Marshalling
 * @author <a href="kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SimpleMarshalDescriptor extends MarshalDescriptor {
        
    //-------------------/
    //- Private Members -/
    //-------------------/

    private static final Object[] emptyArgs = new Object[0];
    
    /**
     * The type of MarshalDescriptor that this MarshalDescriptor represents
    **/
    private DescriptorType dType = DescriptorType.element;
    
    /**
     * The MarshalInfo describing the Class type of the Member
    **/
    MarshalInfo marshalInfo = null;
    
    /**
     * The read method to obtain the Member being described by this
     * descriptor
    **/
    private Method readMethod = null;
        
    /**
     * The write method to set the Member being described by this
     * descriptor
    **/
    private Method writeMethod = null;

    private Method createMethod = null;
    
    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new SimpleMarshalDescriptor with the given class
     * type and programmatic name. An XML name will be created
     * automatically from the programmatic name.
     * @param type the Class type of the described field
     * @param name the programmatic name of the field
    **/
    public SimpleMarshalDescriptor(Class type, String name) {
        super(type, name, MarshalHelper.toXMLName(name));
    } //-- SimpleMarshalDescriptor
    
    /**
     * Creates a new SimpleMarshalDescriptor with the given class
     * type and names
     * @param type the Class type of the described field
     * @param name the programmatic name of the field
     * @param xmlName the XML name of the field
    **/
    public SimpleMarshalDescriptor(Class type, String name, String xmlName) {
        super(type, name, xmlName);
    } //-- SimpleMarshalDescriptor
    
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Returns the DescriptorType for this MarshalDescriptor
     * @return the DescriptorType for this MarshalDescriptor
    **/
    public DescriptorType getDescriptorType() {
        return dType;
    } //-- getDescriptorType
    
    /**
     * Returns the MarshalInfo class for the member described by
     * this descriptor. This can be null if the MarshalInfo should
     * be obtained automatically.
     * @return the MarshalInfo for the Class of the member described by this
     * descriptor. This can be null if the MarshalInfo should be
     * obtained automatically.
    **/
    public MarshalInfo getMarshalInfo() {
        return marshalInfo;
    } //-- getMarshalInfo
    
    /**
     * Returns the value of the field associated with this
     * descriptor from the given target object.
     * @param target the object to get the value from
     * @return the value of the field associated with this
     * descriptor from the given target object.
    **/
    public Object getValue(Object target) 
        throws java.lang.reflect.InvocationTargetException,
               java.lang.IllegalAccessException
    {
        
        Object val = null;
        if (readMethod != null) {
            val = readMethod.invoke(target,emptyArgs);
        }
        return val;
    } //-- getValue
        
    /**
     * Sets the value of the field associated with this descriptor.
     * @param target the object in which to set the value
     * @param value the value of the field 
    **/
    public void setValue(Object target, Object value)
        throws java.lang.reflect.InvocationTargetException,
               java.lang.IllegalAccessException
    {
        if (writeMethod != null) {
            Object[] params = new Object[1];
            params[0] = value;
            writeMethod.invoke(target, params);
        }
    } //-- setValue
    
    
    /**
     * Sets the MarshalDescriptorType type for this MarshalDescriptor
     * @param descriptorType the MarshalDescriptorType that should be
     * used for this MarshalDescriptor.
    **/
    public void setDescriptorType(DescriptorType descriptorType) {
        this.dType = descriptorType;
    } //-- setDescriptorType
    
    /**
     * Sets the MarshalInfo class for the member described by
     * this descriptor. This can be a null value.
     * @param marshalInfo the MarshalInfo class which describes the
     * Class type of the member described by this descriptor
    **/
    public void setMarshalInfo(MarshalInfo marshalInfo) {
        this.marshalInfo = marshalInfo;
    } //-- setMarshalInfo
    
    /**
     * Sets the method used to obtain the member described by this Descriptor
     * @param readMethod the method used to obtain the described member
    **/
    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }
    
    /**
     * Sets the method used to set the member described by this Descriptor
     * @param writeMethod the method used to set the described member
    **/
    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public void setCreateMethod(Method createMethod) {
        this.createMethod = createMethod;
    } //-- setCreateMethod

    public Method getCreateMethod() {
        return createMethod;
    } //-- getCreateMethod

} //-- SimpleMarshalDescriptor



