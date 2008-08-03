/*
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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder;

import org.exolab.javasource.JClass;
import org.exolab.javasource.JCollectionType;
import org.exolab.javasource.JType;

/**
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-10-10 06:35:52 -0600 (Mon, 10 Oct 2005) $
 */
public class SGTypes {
    
    /**
     * Represents a {@link JClass} instance of type 'org.exolab.castor.xml.MarshalException'.
     */
    public static final JClass MARSHAL_EXCEPTION =
        new JClass("org.exolab.castor.xml.MarshalException");

    /**
     * Represents a {@link JClass} instance of type 
     * 'org.exolab.castor.xml.ValidationException'.
     */
    public static final JClass VALIDATION_EXCEPTION =
        new JClass("org.exolab.castor.xml.ValidationException");

    /**
     * Represents a {@link JClass} instance of type 'java.lang.IndexOutOfBoundsException'.
     */
    public static final JClass INDEX_OUT_OF_BOUNDS_EXCEPTION =
        new JClass("java.lang.IndexOutOfBoundsException");

    /**
     * Represents a {@link JClass} instance of type 'java.lang.Class'.
     */
    public static final JClass CLASS = new JClass("java.lang.Class");

    /**
     * Represents a {@link JClass} instance of type 'java.lang.Object'.
     */
    public static final JClass OBJECT = new JClass("java.lang.Object");
    
    /**
     * Represents a {@link JClass} instance of type 'java.lang.String'.
     */
    public static final JClass STRING = new JClass("java.lang.String");

    /**
     * Represents a {@link JClass} instance of type 'java.io.IOException'.
     */
    public static final JClass IO_EXCEPTION = new JClass("java.io.IOException");

    /**
     * Represents a {@link JClass} instance of type 'java.io.Reader'.
     */
    public static final JClass READER = new JClass("java.io.Reader");
    
    /**
     * Represents a {@link JClass} instance of type 'java.lang.Writer'.
     */
    public static final JClass WRITER = new JClass("java.io.Writer");

    /**
     * Represents a {@link JClass} instance of type 'java.beans.PropertyChangeSupport'.
     */
    public static final JClass PROPERTY_CHANGE_SUPPORT =
        new JClass("java.beans.PropertyChangeSupport");

    /**
     * Factory method for creating a {@link JCollectionType} instance representing 
     * an enumeration.
     * @param jType The content type of the collection.
     * @param usejava50 Whether Java 5.0 is the target JVM. 
     * @return {@link JCollectionType} instance representing an enumeration
     */
    public static final JType createEnumeration(final JType jType, final boolean usejava50) {
        return new JCollectionType("java.util.Enumeration", jType, usejava50);
    }

    /**
     * Factory method for creating a {@link JCollectionType} instance representing 
     * an {@link Iterator} instance.
     * @param jType The content type of the collection.
     * @param usejava50 Whether Java 5.0 is the target JVM. 
     * @return {@link JCollectionType} instance representing an {@link Iterator}
     */
    public static final JType createIterator(final JType jType, final boolean usejava50) {
        return new JCollectionType("java.util.Iterator", jType, usejava50);
    }

    /**
     * Factory method for creating a {@link JCollectionType} instance representing 
     * an {@link Hashtable} instance.
     * @param useJava50 Whether Java 5.0 is the target JVM. 
     * @return {@link JCollectionType} instance representing a {@link Hashtable}
     */
    public static final JType createHashtable(final boolean useJava50) {
        if (useJava50) {
            return new JClass("java.util.Hashtable<Object,Object>");
        }
        return new JClass("java.util.Hashtable");
    }
}
