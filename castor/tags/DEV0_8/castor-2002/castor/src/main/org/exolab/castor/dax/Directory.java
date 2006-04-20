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


/**
 * An object representing a directory. All objects are persisted
 * within that directory based on their RDNs. Operations can be
 * atomic or transactional. When performing an operation in the
 * context of a transaction, all changes to objects are persisted
 * when the transaction commits, if the transaction rolls back all
 * objects return to their previous state.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public interface Directory
{


    /**
     * Returns the DN of this directory. Each directory has a root
     * distinguished name, all objects persisted in that directory
     * have a relative DN within that directory.
     *
     * @return The directory's DN
     */
    public String getDN();


    /**
     * Constructs and returns a new search object. The returned object
     * will be used to perform the search.
     *
     * @param filter The search expression
     * @return A new search object
     * @throws InvalidSearchException An invalid search expression
     *  or query parameter
     * @throws DirectoryException Directory access failed
     * @see Search
     */
    public Search createSearch( String expr )
        throws InvalidSearchException, DirectoryException;


    /**
     * Read an object with the specified identity. If the object is
     * found, it is returned to the application. If this method is
     * called in the context of a transaction, the object will be
     * persistent and any modifications to the object will be
     * persisted when the transaction commits.
     *
     * @param rdn The object's relative DN in this directory
     * @throws DirectoryException Directory access failed
     */
    public Object read( Object rdn )
        throws DirectoryException;


    /**
     * Creates a new object in this directory. The object must not
     * have been read, stored or deleted in this directory in the
     * same transaction, otherwise an exception will be thrown. If
     * an object with the same rdn already exists, an exception will
     * be thrown. If this method is called in the context of a
     * transaction, any modifications to the object after it has been
     * created will be persisted when the transaction commits.
     *
     * @param obj The object to create
     * @throws DuplicateRDNException An object with the same RDN
     *  already exists in this directory
     * @throws DirectoryException Directory access failed
     */
    public void create( Object obj )
        throws DuplicateRDNException, DirectoryException;


    /**
     * Deletes an object from this directory. If this method is called
     * in the context of a transaction, the actual deletion will only
     * occur when the transaction commits. The object must have been
     * read or stored in this directory, however this method will
     * succeed even if the object has already been removed from the
     * directory by some other operation.
     *
     * @param obj The object to delete
     * @throws DirectoryException Directory access failed
     */
    public void delete( Object obj )
        throws DirectoryException;


    /**
     * Starts a transaction against this directory. Objects retrieved
     * or stored within the context of a tranasaction are considered
     * persistent. When the transaction commits, all changes made to
     * these objects during the transaction will be persisted to the
     * directory.
     * <p>
     * This method cannot be called if the directory has been
     * obtained from an application server using the JNDI environment
     * naming context. In such environments, the application server
     * is responsible for beginning, committing and rolling back the
     * transaction.
     * <p>
     * Directories do not support locking, therefore transactions do
     * not prevent concurrent access or guarantee integrity. They are
     * provided for the benefit of the application developer and for
     * supporting the JTA development model.
     *
     * @throws DirectoryException Directory access failed
     */
    public void begin()
        throws DirectoryException;


    /**
     * Commits a transaction against this directory. Objects retrieved
     * or stored within the context of a tranasaction are considered
     * persistent. When the transaction commits, all changes made to
     * these objects during the transaction will be persisted to the
     * directory.
     * <p>
     * If an error occured while attempting to commit the
     * transaction, the transaction will be rolledback before
     * returning an exception to the application.
     * <p>
     * This method cannot be called if the directory has been
     * obtained from an application server using the JNDI environment
     * naming context. In such environments, the application server
     * is responsible for beginning, committing and rolling back the
     * transaction.
     *
     * @throws DirectoryException Directory access failed
     */
    public void commit()
        throws DirectoryException;


    /**
     * Commits a transaction against this directory. Objects retrieved
     * or stored within the context of a tranasaction are considered
     * persistent.
     * <p>
     * This method cannot be called if the directory has been
     * obtained from an application server using the JNDI environment
     * naming context. In such environments, the application server
     * is responsible for beginning, committing and rolling back the
     * transaction.
     *
     * @throws DirectoryException Directory access failed
     */
    public void rollback()
        throws DirectoryException;


    /**
     * Returns true if the object is persistent. An object is
     * persistent if it has been retrieved or stored in this directory
     * within the context of a transaction.
     *
     * @param obj The object
     * @return True if persistent in this transaction
     */
    public boolean isPersistent( Object obj );


    /**
     * Close this directory object. If this directory is in the
     * context of a transaction, the transaction will be rolled back.
     *
     * @throws DirectoryException Directory access failed
     */
    public void close()
        throws DirectoryException;


}

