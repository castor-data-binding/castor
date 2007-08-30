/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder.types;

/**
 * Helper class to facilitate creation of XML schema collection types.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 *
 */
public class XSCollectionFactory {

    /**
     * Factory method to create an XS collection type, i.e. an instance of {@link XSListType}.
     * @param collectionName The name of the Java collection type,e.g. 'java.util.Collection'
     * @param contentType
     *            the content type of the collection, ie. the type of objects
     *            that the collection will contain
     * @param useJava50
     *            true if source code is supposed to be generated for Java 5
     * @return An {@link XSListType} instance.
     * @see XSListType
     */
    public static XSListType createCollection (final String collectionName,
            final XSType contentType, 
            final boolean useJava50) {
        XSListType collection = null;
        if (contentType instanceof XSIdRef) {
            collection = new XSIdRefs(collectionName, useJava50);
        } else if (contentType instanceof XSNMToken) {
            collection = new XSNMTokens(collectionName, useJava50);
        } else {
            collection = new XSList(collectionName, contentType, useJava50);
        }
        return collection;
    }
}
