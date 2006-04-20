/**
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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;
import org.exolab.castor.util.List;

import java.util.Enumeration;

/**
 * A list for maintaining facets
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class FacetList implements java.io.Serializable {

    /**
     * The list of facets
    **/
    private List facets    = null;

    /**
     * Creates a new FacetList
    **/
    public FacetList() {
        facets = new List();
    } //-- FacetList

    /**
     * Adds the given Facet to this list
     * @param facet the facet to add
    **/
    public void add(Facet facet) {
        if (facet != null) 
            facets.add(facet);
    } //-- add

    /**
     * Adds the facets from the given list into this FacetList
     *
     * @param facetList the FacetList to copy from
     */
    public void add(FacetList facetList) {
        if (facetList == null) return;
        for (int i = 0; i < facetList.facets.size(); i++)
            facets.add(facetList.facets.get(i));
    } //-- add
    
    
    /**
     * Returns the facet at the given index
     * @param index the index of the Facet to return
    **/
    public Facet get(int index) {
        return (Facet)facets.get(index);
    } //-- get

    /**
     * Removes the given Facet from this list
     * @param facet the Facet to remove
    **/
    public void remove(Facet facet) {
        facets.remove(facet);
    } //-- remove

    /**
     * Returns the number of Facets in this list
     * @return the number of Facets in this list
    **/
    public int size() {
        return facets.size();
    } //-- size

    /**
     * Returns an Enumeration of the Facets in this list
     * @return an Enumeration of the Facets in this list
    **/
    public Enumeration enumerate() {
        return new FacetListEnumerator(this);
    } //-- enumerate

    /**
     * Returns the facet of the list with with the given name.
     * In case of an ENUMERATION the first facet is returned.
     * If none of the name of the facets of that list
     * correspond to the given name, null is returned.
     * 
     * @param name the facet name to look for.
     *
     * @return The facet of the list with the given name
     */
    public Facet contains(String name) {
        if (name == null)
            return null;
        Enumeration enum = enumerate();
        while (enum.hasMoreElements()) {
            Facet temp = (Facet)enum.nextElement();
            if (temp.getName().equals(name))
                return temp;
        }
        return null;
    }

} //-- FacetList

