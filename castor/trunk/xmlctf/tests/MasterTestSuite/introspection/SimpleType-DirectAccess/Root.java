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

import java.math.BigDecimal;
import java.util.Date;

import org.castor.xmlctf.CastorTestable;
import org.castor.xmlctf.RandomHelper;

public class Root implements CastorTestable {

    public String     type_string;
    public int        type_int;
    public long       type_long;
    public boolean    type_boolean;
    public double     type_double;
    public float      type_float;
    public BigDecimal type_bigDecimal;
    public byte       type_byte;
    public Date       type_date;
    public char       type_char;

    // private byte[]   type_bytes; ####################

    // --- CastorTestable ------------------------

    public boolean equals(Object object) {
        if ( ! (object instanceof Root)) {
            return false;
        }

        Root root = (Root)object;

        boolean result = true;

        if ((type_string != null) && (root.type_string != null)) {
            result &= (type_string.equals(root.type_string));
        } else {
            result &= (type_string == null) && (root.type_string == null);
        }

        result &= (type_int     == root.type_int);
        result &= (type_long    == root.type_long);
        result &= (type_boolean == root.type_boolean);
        result &= (type_double  == root.type_double);
        result &= (type_float   == root.type_float);

        if ((type_bigDecimal != null) && (root.type_bigDecimal != null)) {
            result &= (type_bigDecimal.equals(root.type_bigDecimal));
        } else {
            result &= (type_bigDecimal == null) && (root.type_bigDecimal == null);
        }

        result &= (type_byte == root.type_byte);

        if ((type_date != null) && (root.type_date != null)) {
            result &= (type_date.equals(root.type_date));
        } else {
            result &= (type_date == null) && (root.type_date == null);
        }

        result &= (type_char == root.type_char);

        return result;
    }

    public void randomizeFields() throws InstantiationException, IllegalAccessException {
        type_string  = RandomHelper.getRandom(type_string, String.class);
        type_int     = RandomHelper.getRandom(type_int);
        type_long    = RandomHelper.getRandom(type_long);
        type_boolean = RandomHelper.getRandom(type_boolean);
        type_double  = RandomHelper.getRandom(type_double);
        type_float   = RandomHelper.getRandom(type_float);
        // type_bigDecimal = RandomHelper.getRandom(type_bigDecimal, BigDecimal.class); // ##############
        type_byte    = RandomHelper.getRandom(type_byte);
        // type_date    = RandomHelper.getRandom(type_date, date.class); // ##############
        type_char    = RandomHelper.getRandom(type_char);
    }

    public String dumpFields() {
        String dump = new String();

        dump += "[type_string=" + type_string + "]\n";
        dump += "[type_int=" + type_int + "]\n";
        dump += "[type_long=" + type_long + "]\n";
        dump += "[type_boolean=" + type_boolean + "]\n";
        dump += "[type_double=" + type_double + "]\n";
        dump += "[type_float=" + type_float + "]\n";
        dump += "[type_bigDecimal=" + type_bigDecimal + "]\n";
        dump += "[type_byte=" + type_byte + "]\n";
        dump += "[type_date=" + type_date + "]\n";
        dump += "[type_char=" + type_char + "]\n";

        return dump;
    }

}
