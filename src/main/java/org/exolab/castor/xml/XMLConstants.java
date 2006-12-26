/*
 * Copyright 2006 Edward Kuns
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml;

/**
 * Constants used by all of Castor-XML are defined here.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
public interface XMLConstants {
    /** Descriptors are placed into this special package relative to the generated source. */
    String DESCRIPTOR_PACKAGE  = "descriptors";

    /** This suffix is added to a class name to make its descriptor. */
    String DESCRIPTOR_SUFFIX   = "Descriptor";

    /** Whitespace preserve (keep exactly as-is). */
    String WHITESPACE_PRESERVE = "preserve";

    /** Whitespace replace (replace each whitespace with a blank). */
    String WHITESPACE_REPLACE  = "replace";

    /** Whitespace collapse (replace each whitespace with a blank, remove leading
     * and trailing whitespace, collapse consecutive blanks into exactly one blank). */
    String WHITESPACE_COLLAPSE = "collapse";

    /** XML name type NCName. */
    short NAME_TYPE_NCNAME  = 0;

    /** XML name type NMTOKEN. */
    short NAME_TYPE_NMTOKEN = 1;

    /** XML name type CDATA. */
    short NAME_TYPE_CDATA   = 2;
}
