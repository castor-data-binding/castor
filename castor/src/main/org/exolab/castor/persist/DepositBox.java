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
 * Id: DepositBox.java
 */

package org.exolab.castor.persist;


/**
 * DepositBox is an interface for {@link ClassMolder} to access the dirty 
 * checking cache of an object. 
 * <p>
 * Checking for accessMode will be done to each access of the DepositBox. 
 * Only if a transaction has write access may <tt>setObject()</tt> or 
 * {@link java.lang.IllegalArgumentException} will be thrown. 
 * <p>
 * Only if a transaction with read or write access may call
 * <tt>getObject()</tt>. Multiple transactions may own read access of the
 * same DespositBox at the same time. Only one transaction may own write access
 * at any given time.
 * <p>
 * Every time <tt>setObject()</tt> is called, the internal timestamp will be updated.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public interface DepositBox {

    /** 
     *  Set an object into the DespositBox, only a transaction
     *  has the write lock may call it method or IllegalArgumentException
     *  will be thrown.
     *
     *  @param  tx the transaction in action
     *  @param  object to be store into deposit box
     *  @throw  IllegalArgumentException if tx doesn't own the box
     *
     */
    public void setObject( TransactionContext tx, Object object );

    /** 
     *  Get the object from the DespositBox, only the transaction
     *  have the read or write lock can get it.
     *
     *  @param  tx the transaction in action
     *  @return An object instance.
     *  @throw  IllegalArgumentException if tx doesn't own the box
     */
    public Object getObject( TransactionContext tx ); 

    /**
     * Get the time of the most recent call on setObject(Object)}.
     * @return The timestamp of this object.
     */
    public long getTimeStamp();
}