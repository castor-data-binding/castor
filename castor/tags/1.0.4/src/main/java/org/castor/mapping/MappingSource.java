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

import org.exolab.castor.util.DTDResolver;
import org.xml.sax.InputSource;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class MappingSource {
    /** The input source. */
    private final InputSource _source;
    
    /** The type of the mapping source. */
    private final String _type;
    
    /** The entity resolver to use. May be null. */
    private final DTDResolver _resolver;

    public MappingSource(final InputSource source, final String type, final DTDResolver resolver) {
        _source = source;
        _type = type;
        _resolver = resolver;
    }
    
    public InputSource getSource() { return _source; }
    
    public String getType() { return _type; }
    
    public DTDResolver getResolver() { return _resolver; }
}
