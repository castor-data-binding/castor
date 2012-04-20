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
 * $Id$
 */

package org.exolab.castor.dsml;

/**
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public abstract class XML {
    public static class Namespace {
        public static final String URI = "http://www.dsml.org/DSML";
        public static final String PREFIX = "dsml";
        public static final String ROOT = "dsml";
    }

    public static class Schema {
        public static final String ELEMENT = "directory-schema";

        public static class Elements {
            public static final String NAME = "name";
            public static final String DESCRIPTION = "description";
            public static final String OID = "object-identifier";
            public static final String CLASS = "class";
            public static final String ATTRIBUTE = "attribute";
            public static final String ATTRIBUTE_TYPE = "attribute-type";
            public static final String SYNTAX = "syntax";
            public static final String EQUALITY = "equality";
            public static final String ORDERING = "ordering";
            public static final String SUBSTRING = "substring";
        }

        public static class Attributes {
            public static final String ID = "id";
            public static final String SUPERIOR = "superior";
            public static final String OBSOLETE = "obsolete";
            public static final String TYPE = "type";
            public static final String SINGLE_VALUE = "single-value";
            public static final String USER_MODIFICATION = "user-modification";
            public static final String REF = "ref";
            public static final String REQUIRED = "required";

            public static class Types {
                public static final String STRUCTURAL = "structural";
                public static final String ABSTRACT = "abstract";
                public static final String AUXILIARY = "auxiliary";
            }
        }
    }

    public static class Entries {
        public static final String ELEMENT = "directory-entries";
        
        public static class Elements {
            public static final String ENTRY = "entry";
            public static final String OBJECT_CLASS = "objectclass";
            public static final String OBJECT_CLASS_VALUE = "oc-value";
            public static final String ATTRIBUTE = "attr";
            public static final String VALUE = "value";
        }

        public static class Attributes {
            public static final String DN = "dn";
            public static final String NAME = "name";
            public static final String REF = "ref";
            public static final String ENCODING = "encoding";

            public static class Encodings {
                public static final String BASE64 = "base64";
            }
        }
    }
}
