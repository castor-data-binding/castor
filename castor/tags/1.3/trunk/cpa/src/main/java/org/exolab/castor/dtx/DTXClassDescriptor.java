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
 * $Id$
 */

package org.exolab.castor.dtx;

import java.util.HashMap;
import java.util.Set;

import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;

/* This is more of a ClassHolder or a
 * PleaseStoreThisClassInfoForMeThing.  It's useful for going quickly
 * between column names in a result set and fields in a class.
 * 
 * The methods are kind of self-explanatory -- they map column names
 * to FieldMappings. The attributes, simple elements, contained
 * objects and text are stored separately for easy access.
 *
 * @author <a href="0@intalio.com">Evan Prodromou</a> 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */

/* package-level class */

public class DTXClassDescriptor {

    private ClassMapping _clsMapping = null;
    private HashMap _attrs = null;
    private HashMap _simpleElements = null;
    private HashMap _contained = null;
    private FieldMapping _text = null;
    private String _textCol = null;

    DTXClassDescriptor(final ClassMapping clsMapping) {
        _clsMapping = clsMapping;
        _attrs = new HashMap();
        _simpleElements = new HashMap();
        _contained = new HashMap();
    }

    void addAttr(final String columnName, final FieldMapping fieldMapping) {
        _attrs.put(columnName, fieldMapping);
    }

    void addSimpleElement(final String columnName, final FieldMapping fieldMapping) {
        _simpleElements.put(columnName, fieldMapping);
    }

    void addContained(final String columnName, final ClassMapping contained) {
        _contained.put(columnName, contained);
    }

    FieldMapping getAttr(final String columnName) {
        return (FieldMapping) _attrs.get(columnName);
    }

    FieldMapping getSimpleElement(final String columnName) {
        return (FieldMapping) _simpleElements.get(columnName);
    }

    String[] getAttrCols() {
        Set keys = _attrs.keySet();
        return (String[]) keys.toArray(new String[keys.size()]);
    }

    String[] getSimpleElementCols() {
        Set keys = _simpleElements.keySet();
        return (String[]) keys.toArray(new String[keys.size()]);
    }

    ClassMapping getClassMapping() {
        return _clsMapping;
    }

    String getTextCol() {
        return _textCol;
    }

    FieldMapping getText() {
        return _text;
    }

    void setTextCol(final String textCol, final FieldMapping text) {
        _text = text;
        _textCol = textCol;
    }
}
