/*
 * Copyright 2006 Ralf Joachim
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
package org.exolab.castor.xml.util;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.NodeType;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2005-12-06 14:55:28 -0700 (Tue, 06 Dec 2005) $
 */
public class XMLContainerElementFieldDescriptor extends XMLFieldDescriptorImpl {

    public XMLContainerElementFieldDescriptor(final XMLFieldDescriptorImpl fieldDesc,
            final NodeType primitiveNodeType) throws MappingException {
        super(fieldDesc, fieldDesc.getXMLName(), fieldDesc.getNodeType(), primitiveNodeType);

        setFieldType(org.exolab.castor.xml.util.ContainerElement.class);

        setNameSpaceURI(fieldDesc.getNameSpaceURI());
        setNameSpacePrefix(fieldDesc.getNameSpacePrefix());
//        setQNamePrefix(fieldDesc.getQNamePrefix());
    }
}
