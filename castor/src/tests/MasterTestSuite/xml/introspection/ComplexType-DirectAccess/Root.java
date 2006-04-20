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

import org.exolab.castor.tests.framework.CastorTestable;
import org.exolab.castor.tests.framework.RandomHelper;

public class Root implements CastorTestable {

    public String Name;

    public Root RootData;
    public Data Data;

    public Root() { }

    public Root(String name) {
        Name = name;
    }

    // --- CastorTestable ------------------------
    public boolean equals(Object object) {

        if ( ! (object instanceof Root))
            return false;

        Root root = (Root)object;

        boolean result = true;

        if ((this.Name != null) && (root.Name != null))
            result &= (this.Name.equals(root.Name));
        else
            result &= (this.Name == null) && (root.Name == null);
        
        if ((this.RootData != null) && (root.RootData != null))
            result &= (this.RootData.equals(root.RootData));
        else
            result &= (this.RootData == null) && (root.RootData == null);

        if ((this.Data != null) && (root.Data != null))
            result &= (this.Data.equals(root.Data));
        else
            result &= (this.Data == null) && (root.Data == null);

        return result;
    }

    public void randomizeFields() 
        throws InstantiationException, IllegalAccessException {

        Name = RandomHelper.getRandom(Name, String.class);

        if (RandomHelper.flip(0.6)) {
            RootData = new Root();
            ((CastorTestable)RootData).randomizeFields();
        }
        else
            RootData = null;

        if (RandomHelper.flip(0.7)) {
            Data = new Data();
            ((CastorTestable)Data).randomizeFields();
        }
        else
            Data = null;

    }

    public String dumpFields() {
        String dump = new String();

        dump += "[Name=" + Name + "]\n";
        dump += "[RootData=" + ((RootData!=null)?((CastorTestable)RootData).dumpFields():"null") + "]\n";
        dump += "[Data=" + ((Data!=null)?((CastorTestable)Data).dumpFields():"null") + "]\n";

        return dump;
    }
}
