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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: Lazy.java,
 */
package org.castor.persist.proxy;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.spi.Identity;

/**
 * <tt>Lazy</tt> is a place holder interface to indicate that a data object or
 * a Collection in a data object is an Lazy instance. In other words, 
 * instances of the objects are not retrieved from the persistence 
 * store at load time, but will be materialized when the instance 
 * is actually needed/accessed.
 * 
 * <p>Implementations of this class need to provide a writeReplace() 
 * methor required during Java serialization.</p>
 *
 * <p>
 * @author <a href="mailto:werner DOT guttmann @ gmx DOT net">Werner Guttmann</a>
 *
 */
public interface LazyCGLIB extends Serializable {
    
    /** 
     * Implementation of writeReplace specific to lazy loading.
     * @return The real object.
     * @throws ObjectStreamException If the replace activity failed.
     */
    Object writeReplace () throws ObjectStreamException; 
    
    /**
     * Returns the Class instance for the class to be intercepted.
     * @return Class instance for the intercepted class.
     */
    Class interceptedClass();
    
    /**
     * Returns the identity of the object (class) intercepted.
     * @return identity of the object (class) intercepted.
     */
    Identity interceptedIdentity();
    
    /**
     * Returns the ClassMolder of the object intercepted.
     * @return ClassMolder of the object intercepted.
     */
    ClassMolder interceptedClassMolder();
    
    /**
     * @return true if the object has been materialized; otherwise, returns false.
     */
    Boolean interceptedHasMaterialized();
}
