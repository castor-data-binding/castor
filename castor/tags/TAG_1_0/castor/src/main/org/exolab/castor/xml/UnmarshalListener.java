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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 *
 * Date         Author             Changes
 * 09-13-2002  Paul Christmann   Initial Revision
 */

package org.exolab.castor.xml;


/**
 * An interface to allow external "listening" to objects when
 * they are being unmarshalled for various tracking purposes and
 * potential modification. An implementation of
 * this interface may be registered with the Unmarshaller.
 * <p>
 * The UnmarshalListener interface does <em>not</em> report on
 * native data types that are unmarshalled.
 *
 * @author <a href="mailto:paul@priorartisans.com">Paul Christmann</a>
 * @author <a href="mailto:kvsico@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
**/
public interface UnmarshalListener {

    /**
     * This method is called when an object has just been initialized by the
     * Unmarshaller.
     *
     * @param object the Object that was initialized.
    **/
    public void initialized (Object object);

    /**
     * This method is called once the attributes have been processed.
     * It indicates that the the fields of the given object corresponding
     * to attributes in the XML document have been set.
     *
     * @param object the Object the object being unmarshalled.
     */
    public void attributesProcessed(Object object);

    /**
     * This method is called after a child object
     * has been added during the unmarshalling.  This
     * method will be called after {@link unmarshalled}
     * has been called for the child.
     *
     * @param fieldName the Name of the field the child is being added to.
     * @param target the Object being unmarshalled.
     * @param child the Object that was just added.
    **/
    public void fieldAdded (String fieldName, Object parent, Object child);

    /**
     * This method is called after an object
     * has been completely unmarshalled, including
     * all of its children (if any).
     *
     * @param object the Object that was unmarshalled.
    **/
    public void unmarshalled (Object object);

} //-- UnmarshalListener
