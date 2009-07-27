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
 * $Id: MutatingListener.java 5951 2006-05-30 22:18:48Z bsnyder $
 *
 */

/**
 * Simple implementation of MarshalListener and UnmarshalListener
 * that does nothing to the elements being marshaled/unmarshalled.
 * It merely stores them on a list for later reporting.
 */
public class MutatingListener extends SimpleListener {

    /** MarshalListener */
    public boolean preMarshal (Object o) {

        modifyObject(o);
        super.preMarshal(o);

        // Just for the hell of it, don't marshal a C.
        if ( o instanceof C ) return false;

        return true;
    }

    /** MarshalListener */
    public void postMarshal (Object o) {
        super.postMarshal(o);
    }

    /** UnmarshalListener */
    public void initialized (Object o) {
        modifyObject(o);
        super.initialized(o);
    }

    /** UnmarshalListener */
    public void fieldAdded (String fieldName, Object parent, Object child) {
        modifyObject(parent);
        modifyObject(child);
        super.fieldAdded(fieldName, parent, child);
    }

    /** UnmarshalListener */
    public void unmarshalled (Object o) {
        modifyObject(o);
        super.unmarshalled(o);
    }
    
    /** UnmarshalListener */
    public void attributesProcessed(Object o) {
        log(o, "Attributes Processed");
    }

    private void modifyObject (Object o) {
        if ( o instanceof BaseClass ) {
            BaseClass base = (BaseClass)o;
            base.setNumMods( base.getNumMods()+1 );
            if ( base instanceof B ) {
                base.setElement(base.getElement() + ":X");
            } else {
                base.setAttribute(base.getAttribute() + ":X");
            }
        }
    }

}

