/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.mapping;

import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.xml.IDResolver;

/**
 * An IDResolver to allow us to resolve ClassMappings from included Mapping files.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class MappingUnmarshallIDResolver implements IDResolver {
    private MappingRoot _mapping = null;

    public MappingUnmarshallIDResolver() { super(); }

    public void setMapping(final MappingRoot mapping) { _mapping = mapping; }

    /**
     * Returns the Object whose id matches the given IDREF, or null if no Object was
     * found.
     * 
     * @param idref the IDREF to resolve.
     * @return the Object whose id matches the given IDREF.
     * @see org.exolab.castor.xml.IDResolver#resolve(java.lang.String)
     */
    public Object resolve(final String idref) {
        if (_mapping == null) { return null; }
        for (int i = 0; i < _mapping.getClassMappingCount(); i++) {
            ClassMapping clsMap = _mapping.getClassMapping(i);
            if (idref.equals(clsMap.getName())) { return clsMap; }
        }
        return null;
    }
}
