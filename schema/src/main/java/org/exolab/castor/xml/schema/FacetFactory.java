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
 */
package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.schema.facets.MaxExclusive;
import org.exolab.castor.xml.schema.facets.MaxInclusive;
import org.exolab.castor.xml.schema.facets.MinExclusive;
import org.exolab.castor.xml.schema.facets.MinInclusive;

/**
 * A factory to create instances of {@link Facet}s.
 *
 * @see Facet
 * @author <a href="mailto:sergei.ivanov@mail.ru">Sergei Ivanov</a>
 * @version $Revision: 6465 $ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public final class FacetFactory {

    /**
     * A private constructor to disallow direct instantiation.
     */
    private FacetFactory() { }

    /**
     * The singleton instance of the factory.
     */
    private static final FacetFactory INSTANCE = new FacetFactory();

    /**
     * Returns the singleton instance of the facet factory.
     * @return factory instance
     */
    public static FacetFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a new instance of a facet.
     * @param name facet name (implies facet type)
     * @param value value of the facet
     * @return a new facet instance
     */
    public Facet createFacet(final String name, final String value) {
        Facet facet = null;
        if (Facet.MIN_EXCLUSIVE.equals(name)) {
            facet = new MinExclusive(value);
        } else if (Facet.MIN_INCLUSIVE.equals(name)) {
            facet = new MinInclusive(value);
        } else if (Facet.MAX_INCLUSIVE.equals(name)) {
            facet = new MaxInclusive(value);
        } else if (Facet.MAX_EXCLUSIVE.equals(name)) {
            facet = new MaxExclusive(value);
        } else {
            facet = new Facet(name, value);
        }
//        facet.setOwningType(parent);
        return facet;
    }
}