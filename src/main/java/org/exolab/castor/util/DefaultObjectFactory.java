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
 * Copyright 2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 *
 * Date         Author          Changes
 * 04/21/2003   Keith Visco     Created
 */
 
package org.exolab.castor.util;

import java.lang.reflect.Constructor;

/** 
 * The default implementation of ObjectFactory used for 
 * creating class instances
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 * @see ObjectFactory
 */
public class DefaultObjectFactory implements ObjectFactory {
    
    /**
     * Creates a default instance of the given class.
     *
     * @param type the Class to create an instance of
     * @return the new instance of the given class
     */
    public Object createInstance(Class type) 
        throws IllegalAccessException, InstantiationException
    {
        if (java.util.Date.class.isAssignableFrom(type)) {
            return handleDates(type);
        }
        return type.newInstance();
    } //-- createInstance
    
    /**
     * Creates a default instance of the given class.
     *
     * @param type the Class to create an instance of
     * @param args the array of arguments to pass to the Class constructor
     * @return the new instance of the given class
     */
    public Object createInstance(Class type, Object[] args) 
        throws IllegalAccessException, InstantiationException
    {
        if ((args == null) || (args.length == 0)) {
            if (java.util.Date.class.isAssignableFrom(type)) {
                return handleDates(type);
            }
            return type.newInstance();
        }
        return createInstance(type, null, args);
    } //-- createInstance
    
    /**
     * Creates a default instance of the given class.
     *
     * @param type the Class to create an instance of
     * @param argTypes the Class types for each argument, used
     * to find the correct constructor
     * @param args the array of arguments to pass to the Class constructor
     * @return the new instance of the given class
     */
    public Object createInstance(Class type, Class[] argTypes, Object[] args)
        throws IllegalAccessException, InstantiationException
    {
        if ((args == null) || (args.length == 0)) {
            if (java.util.Date.class.isAssignableFrom(type)) {
                return handleDates(type);
            }
            return type.newInstance();
        }
        
        if (argTypes == null) {
            argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    argTypes[i] = args[i].getClass();
                }
                else {
                    String err = "null arguments to constructor not accepted " +
                        "if the 'argTypes' array is null.";
                    throw new IllegalStateException(getClass().getName() + ": " + err);
                }
            }
        }
        else if (argTypes.length != args.length) {
            String err = "The argument type array must be the same length as argument value array.";
            throw new IllegalArgumentException(getClass().getName() + ": " + err);
        }
        
        try {
            Constructor cons = type.getConstructor(argTypes);
            return cons.newInstance(args);
        }
        catch(java.lang.NoSuchMethodException nsmx) {
            String err = "unable to find matching public constructor for class: " + type.getName();
            err += " with argument types: ";
            for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) err += ", ";
                err += argTypes[i].getName();
            }
            throw new InstantiationException(err);
        }
        catch(java.lang.reflect.InvocationTargetException ite) {
            // storing causal exception using Java 1.4 method
            IllegalStateException ise = new IllegalStateException(ite.getMessage());
            ise.initCause(ite.getCause());
            throw ise;
        }
            
    } //-- createInstance
    
    
    /**
     * Special Date Handling
     */
    private Object handleDates(Class type) 
        throws IllegalAccessException, InstantiationException
    {
        
        java.util.Date date = new java.util.Date();
        
        if (java.util.Date.class == type) {
            return date;
        }
        else if (java.sql.Date.class == type) {
            long time = date.getTime();
            return new java.sql.Date(time);
        }
        else if (java.sql.Time.class == type) {
            long time = date.getTime();
            return new java.sql.Time(time);
        }
        else if (java.sql.Timestamp.class == type) {
            long time = date.getTime();
            return new java.sql.Timestamp(time);
        }
        else {
            return type.newInstance();
        }
        
    } //-- handleDates
    
} //-- DefaultObjectFactory