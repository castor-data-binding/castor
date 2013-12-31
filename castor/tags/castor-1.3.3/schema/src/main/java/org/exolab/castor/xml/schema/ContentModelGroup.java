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
 * Copyright 1999, 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import java.util.Enumeration;

/**
 * An XML Schema ModelGroup.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public interface ContentModelGroup {

    /**
     * Adds the given ElementDecl to this ContentModelGroup.
     * @param elementDecl the ElementDecl to add
     * @exception SchemaException when an ElementDecl already
     * exists with the same name as the given ElementDecl
    **/
    void addElementDecl(ElementDecl elementDecl)
        throws SchemaException;

    /**
     * Removes the given ElementDecl from this ContentModelGroup.
     * @param elementDecl the ElementDecl to remove.
     * @return true if the element has been successfully removed, false otherwise.
     */
    boolean removeElementDecl(ElementDecl elementDecl);

    /**
     * Adds the given {@link Group} to this {@link ContentModelGroup}.
     * @param group the Group to add
     * @exception SchemaException when a group with the same name as the
     * specified group already exists in the current scope
    **/
    void addGroup(Group group) throws SchemaException;

    /**
     * Removes the given {@link Group} from this {@link ContentModelGroup}.
     * @param group the Group to remove.
     * @return true if the group has been successfully removed, false otherwise.
     */
    boolean removeGroup(Group group);

    /**
     * Adds the given {@link ModelGroup} definition to this {@link ContentModelGroup}.
     * @param group the ModelGroup to add
     * @exception SchemaException when a group with the same name as the
     * specified group already exists in the current scope
    **/
    void addGroup(ModelGroup group) throws SchemaException;

    /**
     * Removes the given {@link ModelGroup} definition from this {@link ContentModelGroup}.
     * @param group the {@link ModelGroup} definition to remove.
     * @return true if the group has been successfully removed, false otherwise.
     */
    boolean removeGroup(ModelGroup group);

    /**
     * Adds the given {@link Wildcard} to this {@link ContentModelGroup}.
     * @param wilcard the {@link Wildcard} to add
     * @exception SchemaException when the {@link Wildcard} is &lt;anyAttribute> and
     * not &lt;any>
     */
    void addWildcard(Wildcard wilcard) throws SchemaException;

    /**
     * Removes the given {@link Wildcard} from this {@link ContentModelGroup}.
     * @param wildcard the {@link Wildcard} to remove.
     * @return true if the given {@link Wildcard} has been successfully removed, false otherwise.
     */
    boolean removeWildcard(Wildcard wildcard);

    /**
     * Returns an enumeration of all the {@link Particle}s contained
     * within this {@link ContentModelGroup}.
     *
     * @return an enumeration of all the {@link Particle}s contained
     * within this {@link ContentModelGroup}
    **/
    Enumeration enumerate();

    /**
     * Returns the element declaration with the given name, or null if no
     * element declaration with that name exists in this 
     * {@link ContentModelGroup}.
     *
     * @param name the name of the element.
     * @return the {@link ElementDecl} with the given name, or null if no
     * ElementDecl exists in this {@link ContentModelGroup}.
    **/
    ElementDecl getElementDecl(String name);

    /**
     * Returns the maximum number of occurrences that this ContentModelGroup
     * may appear.
     * @return the maximum number of occurrences that this ContentModelGroup
     * may appear.
     * A non positive (n < 1) value indicates that the
     * value is unspecified (ie. unbounded).
    **/
    int getMaxOccurs();

    /**
     * Returns the minimum number of occurrences that this ContentModelGroup
     * must appear.
     * @return the minimum number of occurrences that this ContentModelGroup
     * must appear
     * A negative (n < 0) value indicates that the value is unspecified.
    **/
    int getMinOccurs();

    /**
     * Returns the number of particles contained within
     * this {@link ContentModelGroup}.
     *
     * @return the number of particles
    **/
    int getParticleCount();

    /**
     * Returns the {@link Particle} at the specified index.
     * @param index the index of the {@link Particle} to return
     * @return the CMParticle at the specified index
    **/
    Particle getParticle(int index);

}