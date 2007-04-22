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
package org.castor.xmlctf;

/**
 * Every root object in an object model used in a tests has to implement this
 * interface as well as to override the equals() method. It can be useful that
 * all the objects in an object model used for tests implement this interface.
 *
 * @author <a href="mailto:gignoux@intalio.com">Sebastien Gignoux</a>
 * @version $Revision$ $Date: 2003-10-15 09:17:49 -0600 (Wed, 15 Oct 2003) $
 */
public interface CastorTestable {

    /**
     * Returns a recursive dump of the contents of the object's fields in a user
     * readable format. This is used to retrieve the state of the object if
     * castor fails to marshal the object for any reason.
     * <p>
     * We don't rely on the {@link java.lang.Object#toString()} function as
     * might be implemented with another semantic.
     *
     * @return a recursive dump of the contents of the object's fields in a user
     *         readable format.
     */
    public String dumpFields();

    /**
     * Randomizes the contents of the fields of this instance. This is used to
     * create an instance of the object model whithout having to unmarshal
     * anything.
     *
     * @throws InstantiationException if this method is called for an Interface
     *             or abstract class
     * @throws IllegalAccessException if this method is not accessible
     */
    public void randomizeFields() throws InstantiationException, IllegalAccessException;

}
