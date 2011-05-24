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
package org.castor.cpa.test.test98;

import org.junit.Ignore;

@Ignore
public final class Container {
    private String _id;
    private Base _reference;

    public String getId() { return _id; }
    public void setId(final String id) { _id = id; }

    public Base getReference() { return _reference; }
    public void setReference(final Base reference) { _reference = reference; }

    public String toString() {
        return super.toString()
            + "Container: id =" + _id + ",reference=[" + _reference + "]";
    }
}
