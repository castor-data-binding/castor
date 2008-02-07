/*
 * Copyright 2008 Joachim Grueneis
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
package org.castor.xml;

/**
 * This class is responsible to adapt from new (1.2) UnmarshalListener interface
 * to calls into the old interface.<br/>
 * The old interface has been marked as deprecated but will be supported for
 * some Castor releases to come. When the deprecated interface will be removed
 * also this adapter implementation is useless and should be removed.
 * 
 * @author Joachim Grueneis, jgrueneis AT codehaus DOT org
 * @version $Revision$
 */
public class UnmarshalListenerAdapter implements UnmarshalListener {
    /** Old style listener. */
    private org.exolab.castor.xml.UnmarshalListener _oldListener;
    
    /**
     * Empty default constructor.
     */
    public UnmarshalListenerAdapter() {
        super();
    }
    
    /**
     * To set an 'old style' unmarshal listener to receive the callback calls.
     * @param exolabListener the 'old style' unmarshal listener
     */
    public void setOldListener(org.exolab.castor.xml.UnmarshalListener exolabListener) {
        _oldListener = exolabListener;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.xml.UnmarshalListener#attributesProcessed(java.lang.Object, java.lang.Object)
     */
    public void attributesProcessed(final Object target, final Object parent) {
        if (_oldListener != null) {
            _oldListener.attributesProcessed(target);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {
        if (_oldListener != null) {
            _oldListener.fieldAdded(fieldName, parent, child);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.xml.UnmarshalListener#initialized(java.lang.Object, java.lang.Object)
     */
    public void initialized(Object target, Object parent) {
        if (_oldListener != null) {
            _oldListener.initialized(target);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.xml.UnmarshalListener#unmarshalled(java.lang.Object, java.lang.Object)
     */
    public void unmarshalled(Object target, Object parent) {
        if (_oldListener != null) {
            _oldListener.unmarshalled(target);
        }
    }
    
    
}
