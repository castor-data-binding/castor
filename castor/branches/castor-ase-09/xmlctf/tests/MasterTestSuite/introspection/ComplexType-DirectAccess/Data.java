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
 */

import org.castor.xmlctf.CastorTestable;
import org.castor.xmlctf.RandomHelper;

public class Data implements CastorTestable {
    private static final int MAX_DEPTH = 30;

    public String     name;
    public Root       rootInfo;
    private final int _depth;

    public Data() {
        this._depth = 0;
    }

    public Data(int depth) {
        this._depth = depth + 1;
    }

    public Data(String name) {
        this.name   = name;
        this._depth = 0;
    }

    // --- CastorTestable ------------------------
    public boolean equals(Object object) {
        if ( ! (object instanceof Data)) {
            return false;
        }

        Data data = (Data)object;

        boolean result = true;

        if (this.name != null && data.name != null) {
            result &= (this.name.equals(data.name));
        } else {
            result &= (this.name == null && data.name == null);
        }

        if (this.rootInfo != null && data.rootInfo != null) {
            result &= (this.rootInfo.equals(data.rootInfo));
        } else {
            result &= (this.rootInfo == null && data.rootInfo == null);
        }

        return result;
    }

    public void randomizeFields() throws InstantiationException, IllegalAccessException {
        name = RandomHelper.getRandom(name, String.class);

        if (_depth < MAX_DEPTH && RandomHelper.flip(0.6)) {
            rootInfo = new Root(_depth);
            ((CastorTestable)rootInfo).randomizeFields();
        } else {
            rootInfo = null;
        }
    }

    public String dumpFields() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Name=" + name + "]\n");
        buffer.append("[RootInfo=");
        if (rootInfo == null) {
            buffer.append("null");
        } else {
            buffer.append(rootInfo.dumpFields());
        }
        buffer.append("]\n");
        return buffer.toString();
    }

}
