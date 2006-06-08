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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 */


package org.exolab.castor.persist;

import org.castor.util.Messages;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
   
    
public class PersistenceInfoGroup {
    LockEngine[] engines;
    
    public PersistenceInfoGroup( LockEngine[] engines ) {
        this.engines = engines;
    }
    
    public ClassMolder getClassMolder(final Class type) throws ClassNotPersistenceCapableException {
        ClassMolder molder = null;
        int i = 0;
        while ( (i<engines.length) && (molder == null) ) {
            molder = engines[i++].getClassMolderWithDependent( type );
        }
        
        if (molder == null) {
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", type.getName()) );
        } else if (molder.isDependent()) {
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classIsDependent", type.getName(), molder.getDepends().getName()) );
        }
        
        return molder;
    }
    
    public LockEngine getLockEngine() {
        if ( engines != null && engines.length >= 1 )
            return engines[0];
        return null;
    }
}
