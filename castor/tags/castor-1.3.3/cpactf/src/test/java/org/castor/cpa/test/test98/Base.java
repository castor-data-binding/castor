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
public class Base {
    private String _id;
    private String _color;

    public final String getId() { return _id; }
    public final void setId(final String id) { _id = id; }

    public final String getColor() { return _color; }
    public final void setColor(final String color) { _color = color; }

    public String toString() {
        return super.toString() + " Base:  id=" + _id + ",color=" + _color;
    }
}
