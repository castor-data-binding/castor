/*
 * Copyright 2010 Werner Guttmann
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
package org.castor.cpa.jpa.info;

/**
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 */
public abstract class JPAKeyGeneratorDescriptor {
    private String _name;
    private int _allocationSize;
    private int _initialValue;
    
    public final String getName() {
        return _name;
    }

    public final void setName(final String name) {
        _name = name;
    }

    public final int getAllocationSize() {
        return _allocationSize;
    }

    public final void setAllocationSize(final int allocationSize) {
        _allocationSize = allocationSize;
    }

    public final int getInitialValue() {
        return _initialValue;
    }

    public final void setInitialValue(final int initialValue) {
        _initialValue = initialValue;
    }
}
