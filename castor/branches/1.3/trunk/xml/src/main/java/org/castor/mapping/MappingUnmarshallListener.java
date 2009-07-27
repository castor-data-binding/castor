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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.xml.UnmarshalListener;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.xml.Include;
import org.exolab.castor.util.DTDResolver;

/**
 * An UnmarshalListener to handle mapping includes.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class MappingUnmarshallListener implements UnmarshalListener {
    
    private static final Log LOG = LogFactory.getLog(MappingUnmarshallListener.class);
    private final MappingUnmarshaller _unmarshaller;
    
    private final Mapping _mapping;
    
    /** The entity resolver to use. May be null. */
    private final DTDResolver _resolver;

    
    public MappingUnmarshallListener(final MappingUnmarshaller unmarshaller,
                                     final Mapping mapping,
                                     final DTDResolver resolver) {
        _unmarshaller = unmarshaller;
        _mapping = mapping;
        _resolver = resolver;
    }
    
    /**
     * Not used for includes processing.
     * 
     * @see org.castor.xml.UnmarshalListener#initialized(java.lang.Object)
     * {@inheritDoc}
     */
    public void initialized(final Object target, final Object parent) { }

    /**
     * Not used for includes processing.
     * 
     * @see org.castor.xml.UnmarshalListener#attributesProcessed(java.lang.Object)
     * {@inheritDoc}
     */
    public void attributesProcessed(final Object target, final Object parent) { }

    /**
     * Not used for includes processing.
     * 
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String,
     *      java.lang.Object, java.lang.Object)
     * {@inheritDoc}
     */
    public void fieldAdded(final String fieldName,
                           final Object parent, final Object child) { }

    /**
     * This method is called after an object has been completely unmarshalled, including
     * all of its children (if any).
     *
     * @param object the Object that was unmarshalled.
     * @see org.castor.xml.UnmarshalListener#unmarshalled(java.lang.Object)
     * {@inheritDoc}
     */
    public void unmarshalled(final Object target, final Object parent) {
        if (target instanceof Include) {
            Include include = (Include) target;
            try {
                _unmarshaller.loadMappingInternal(_mapping, _resolver, include.getHref());
            } catch (Exception ex) {
                LOG.warn("Problem", ex);
                //-- ignore error, it'll get reported
                //-- later when we re-process the
                //-- includes of the parent Mapping in
                //-- loadMappingInternal
            }
        }
    }
}
