/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test20;

/**
 * The base class of all test object for key generators for non-numeric key columns.
 */
public abstract class AbstractKeyGenObjectString {
    public static final String DEFAULT_ATTR = "attr";
    public static final String DEFAULT_EXT = "ext";

    private String _id;
    private String _attr;
    private String _ext;

    public AbstractKeyGenObjectString() {
        _attr = DEFAULT_ATTR;
        _ext = DEFAULT_EXT;
    }

    public final void setId(final String id) { _id = id; }
    public final String getId() { return _id; }

    public final void setAttr(final String attr) { _attr = attr; }
    public final String getAttr() { return _attr; }

    public final void setExt(final String ext) { _ext = ext; }
    public final String getExt() { return _ext; }

    public String toString() {
        String str = ((_id == null) ? "null" : _id);
        str = str + " / " + _attr + " / " + _ext;
        return str;
    }
}
