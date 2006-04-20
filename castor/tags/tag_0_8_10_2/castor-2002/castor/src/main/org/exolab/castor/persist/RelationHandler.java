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


package org.exolab.castor.persist;


import java.util.Enumeration;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.CollectionHandler;


/**
 * Used by {@link ClassHandler} to represent a relation field. The
 * handler is used to simplify access to the related object, it's
 * identity, or a collection of related objects.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class RelationHandler
{


    /**
     * The handler of the relation field.
     */
    private final FieldHandler      _handler;


    /**
     * True if the relation is multi valued (a collection).
     */
    private final boolean           _multi;


    /**
     * The handler of the related class.
     */
    private final ClassHandler      _relHandler;


    /**
     * True if an attached relation.
     */
    private final boolean           _attached;


    /**
     * Constructor used by {@link ClassHandler}.
     */
    RelationHandler( FieldDescriptor fieldDesc, ClassHandler relHandler, boolean attached )
    {
	_handler = fieldDesc.getHandler();
        _relHandler = relHandler;
        _attached = attached;
        _multi = fieldDesc.isMultivalued();
    }


    /**
     * Returns true if the relation is a many relation. Both one-many
     * and many-many relations deal with a collection of related
     * objects.
     *
     * @return True if a many relation
     */
    public boolean isMulti()
    {
        return _multi;
    }


    /**
     * Returns true if the relation is attached. In an attached
     * relation the related object is deleted when the primary object
     * is deleted.
     *
     * @return True if an attached relation
     */
    public boolean isAttached()
    {
        return _attached;
    }


    /**
     * Returns the related object.
     *
     * @param object The object
     * @return The related object
     */
    public Object getRelated( Object object )
    {
        return _handler.getValue( object );
    }


    /**
     * Sets the related object.
     *
     * @param object The object
     * @param related The related object
     */
    public void setRelated( Object object, Object related )
    {
        _handler.setValue( object, related );
    }


    /**
     * Returns the identity of this relation. Acts on the related
     * object and returns it's identity.
     *
     * @param object The related object
     * @return The identity of the relation
     */
    public Object getIdentity( Object object )
    {
        return _relHandler.getIdentity( object );
    }


    /**
     * Returns the Java class of the related object, which this
     * relation field references.
     *
     * @return The related Java class
     */
    public Class getRelatedClass()
    {
        return _relHandler.getJavaClass();
    }


    /**
     * Returns the class handler of the related object, which this
     * relation field references.
     *
     * @return The related class handler
     */
    public ClassHandler getRelatedHandler()
    {
        return _relHandler;
    }


    public String toString()
    {
        return "Relation " + _handler + ( _multi ? " multi-valued" : ""  ) + ( _attached ? " attached" : "" ) ;
    }


}


