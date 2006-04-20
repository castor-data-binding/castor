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


package org.exolab.castor.dax;


import javax.transaction.xa.XAResource;


/**
 * An XADirectory object provides support for distributed transactions
 * and connection pooling. An XADirectory may be enlisted in a
 * distributed transaction by means of the XAResource object. The
 * directory connection may be pooled using the directory event
 * listeners.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see Directory
 * @see XADirectorySource
 * @see DirectoryEventListener
 */
public interface XADirectory
{


    /**
     * Returns an XA resource for this directory connection.
     *
     * @return The XA resource
     * @throws DirectoryException Directory access failed
     */
    public XAResource getXAResource()
        throws DirectoryException;


    /**
     * Returns a handle to the actual connection. The object returned
     * is a temporary handle used by the application to access the
     * directory through a pooled connection.
     *
     * @return An open connection to the directory
     * @throws DirectoryException Directory access failed
     */
    public Directory getDirectory()
        throws DirectoryException;


    /**
     * Closes the actual connection. This object and the actual
     * connection are no longer useable.
     */
    public void close()
        throws DirectoryException;
    

    /**
     * Adds an event listener.
     */
    public void addDirectoryEventListener( DirectoryEventListener listener );


    /**
     * Removes an event listener.
     */
    public void removeDirectoryEventListener( DirectoryEventListener listener );


}

