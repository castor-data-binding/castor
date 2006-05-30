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
 *
 * $Id$
 */

package org.exolab.castor.xml;

import org.xml.sax.ContentHandler;

/**
 * A interface which abstracts anything which can produce SAX 2 events.
 * This allows any EventProducer to be used with the Unmarshaller.
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-02-22 09:05:40 -0700 (Wed, 22 Feb 2006) $
 * @since 1.0M3
**/
public interface SAX2EventProducer {
    
    /**
     * Sets the SAX2 ContentHandler to send SAX 2 events to
     */
    public void setContentHandler(ContentHandler contentHandler);

    /**
     * Signals to start producing events.
     */
    public void start() throws org.xml.sax.SAXException;
    
}
