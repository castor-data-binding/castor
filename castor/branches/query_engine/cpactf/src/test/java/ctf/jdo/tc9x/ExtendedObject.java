/*
 * Copyright 2005 Werner Guttmann
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
package ctf.jdo.tc9x;

/**
 * @author nstuart
 */
public final class ExtendedObject extends BaseObject {
    private String _description2;

    private DependentObject _dependent;

    public String getDescription2() {
        return _description2;
    }

    public void setDescription2(final String description2) {
        _description2 = description2;
    }

    public DependentObject getDependent() {
        return _dependent;
    }

    public void setDependent(final DependentObject dependent) {
        _dependent = dependent;
    }
}
