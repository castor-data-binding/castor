/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2003-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.descriptors;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * The default class descriptor for Enumerations that are passed in as the
 * root-level class, this is really only useful for marshaling, as Enumerations
 * are immutable.
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-12-16 22:49:25 -0700 (Thu, 16 Dec 2004) $
 */
public class EnumerationDescriptor extends XMLClassDescriptorImpl {

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new EnumerationDescriptor.
     */
    public EnumerationDescriptor() {
        super(java.util.Enumeration.class);
        //-- create element descriptor

        XMLFieldDescriptorImpl desc = new XMLFieldDescriptorImpl(Object.class,
                "_elements", null, NodeType.Element);

        FieldHandler handler = (new XMLFieldHandler() {

            /**
             * {@inheritDoc}
             */
            public Object getValue(final Object object) throws IllegalStateException {
                return object;
            }

            /**
             * {@inheritDoc}
             */
            public void setValue(final Object object, final Object value)
                throws IllegalStateException, IllegalArgumentException {
                //-- do nothing since enumerations are immutable.
            }

            /**
             * {@inheritDoc}
             */
            public Object newInstance(final Object parent) {
                //-- not used
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(true);

        addFieldDescriptor(desc);
    } //-- EnumerationDescriptor()

} //-- EnumerationDescriptor
