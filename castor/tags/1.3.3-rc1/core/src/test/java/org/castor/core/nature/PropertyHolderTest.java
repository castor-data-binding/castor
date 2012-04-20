/*
 * Copyright 2008 Lukas Lang
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
 *
 */
package org.castor.core.nature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;

/**
 * Provides a basic {@link PropertyHolder} with HashMap implementation..
 * 
 * @author Lukas Lang
 * 
 */
@Ignore
public class PropertyHolderTest implements PropertyHolder
{
    /**
     * Added Natures.
     */
    private List natures = new ArrayList();
    /**
     * Properties.
     */
    private Map properties = new HashMap();
    

    public Object getProperty(final String name) {
        return properties.get(name);
    }

    public void setProperty(final String name, final Object value) {
        properties.put(name, value);
    }

    public void addNature(String nature) {
        this.natures.add(nature);
    }

    public boolean hasNature(final String nature) {
        return natures.contains(nature);
    }

}
