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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 *
 */

import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.UnmarshalListener;
import org.castor.xmlctf.CastorTestable;

import java.util.Vector;

/**
 * Simple implementation of MarshalListener and UnmarshalListener
 * that does nothing to the elements being marshaled/unmarshalled.
 * It merely stores them on a list for later reporting.
 */
public class SimpleListener implements MarshalListener, UnmarshalListener, CastorTestable {

    private Vector _calls;

    public SimpleListener () {
        _calls = new Vector();
    }

    protected void log (String msg) {
        _calls.add(new LogEntry(msg));
    }

    protected void log (Object o, String msg) {
        if ( !(o instanceof LogEntry) )
            log(msg + " (" + o + "/" + o.getClass().getName() + ")");
    }

    protected void log (String field, Object parent, Object child) {
        if ( parent instanceof LogEntry || child instanceof LogEntry ) return;

        String msg = "Child " +
            child + "/" + child.getClass().getName() + " added to " +
            parent + "/" + parent.getClass().getName() + " " + field;
        log(msg);
    }

    /** MarshalListener */
    public boolean preMarshal (Object o) {
        log(o, "PreMarshal");
        return true;
    }

    /** MarshalListener */
    public void postMarshal (Object o) {
        log(o, "PostMarshal");
    }

    /** UnmarshalListener */
    public void initialized (Object o) {
        log(o, "Initialized");
    }

    /** UnmarshalListener */
    public void fieldAdded (String fieldName, Object parent, Object child) {
        log(fieldName, parent, child);
    }

    /** UnmarshalListener */
    public void unmarshalled (Object o) {
        log(o, "Unmarshalled");
    }

    /** UnmarshalListener */
    public void attributesProcessed(Object o) {
        log(o, "Attributes Processed");
    }

    /** mapping.xml */
    public Vector getCalls () {
        return _calls;
    }

    /** mapping.xml */
    public void setCalls (Vector calls) {
        _calls = calls;
    }

    /** CastorTestable */
    public boolean equals (Object o) {
        if ( !(o instanceof SimpleListener) ) {
            return false;
        }

        SimpleListener l = (SimpleListener)o;
        if ( _calls.size() != l.getCalls().size() ) return false;

        return toString().equals(l.toString());
    }

    /** CastorTestable */
    public String dumpFields() {
        StringBuffer buf = new StringBuffer();
        buf.append("<" + getClass().getName() + ">");
        for (int i=0;i<_calls.size(); i++) {
            buf.append("<call number=\"" + (i+1) + "\">" +
                       _calls.get(i).toString() + "</call>");
        }
        buf.append("</" + getClass().getName() + "/>");
        return buf.toString();
    }

    /** CastorTestable */
    public void randomizeFields() throws InstantiationException, IllegalAccessException {
        // Not Implemented
    }

}
