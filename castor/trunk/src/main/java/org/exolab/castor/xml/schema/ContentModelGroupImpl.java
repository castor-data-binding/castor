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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import java.util.Enumeration;
import java.util.Vector;

/**
 * An implementation of an XML Schema ContentModelGroup
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a> 
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
 */
class ContentModelGroupImpl implements ContentModelGroup , java.io.Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -2477271185972337873L;

    private Vector _contentModel = null;
    private transient ScopableResolver _resolver = null;

    /**
     * Creates a new ContentModelGroup.
    **/
    public ContentModelGroupImpl() {
        _contentModel = new Vector();
        _resolver = new ScopableResolver();
    } //-- ContentModelGroup

    /**
     * Adds a wildcard to this contentModelGroup
     * @param WildCard the wildcard to add
     * @exception SchemaException thrown when the wildcard
     * is an <anyAttribute> element
     */
     public void addWildcard(Wildcard wildcard)
          throws SchemaException
    {
         if (wildcard.isAttributeWildcard())
            throw new SchemaException("only <any> should be add in a group.");
        _contentModel.addElement(wildcard);
     }

    /**
     * Adds the given ElementDecl to this ContentModelGroup
     * @param elementDecl the ElementDecl to add
     * @exception SchemaException when an ElementDecl already
     * exists with the same name as the given ElementDecl
    **/
    public void addElementDecl(ElementDecl elementDecl)
        throws SchemaException
    {

        if (elementDecl == null) return;

        String name = elementDecl.getName();
        
        if (!elementDecl.isReference()) {
            String key = "element:"+name;
            //-- check for naming collisions
            if (_resolver.resolve(key) != null) {
                String err = "An element declaration with the given name, ";
                err += name + ", already exists in this scope.";
                throw new SchemaException(err);
            }
            _resolver.addResolvable(key, elementDecl);
        }

        //-- add to content model
        _contentModel.addElement(elementDecl);

    } //-- addElementDecl

    /**
     * Removes the given ElementDecl from this ContentModelGroup.
     * @param elementDecl the ElementDecl to remove.
     * @return true if the element has been successfully removed, false otherwise.
     */
     public boolean removeElementDecl(ElementDecl elementDecl) {
        if (elementDecl == null)
            return false;
        int position = _contentModel.indexOf(elementDecl);
	    if (position >= 0) {
	        _contentModel.removeElementAt(position);
	        if (!elementDecl.isReference()) {
	            String key = "element:"+elementDecl.getName();
                _resolver.removeResolvable(key);
            }
            return true;
	   }
       return false;
     }


    /**
     * Adds the given Group to this ContentModelGroup
     * @param group the Group to add
     * @exception SchemaException when a group with the same name as the
     * specified group already exists in the current scope
    **/
    public void addGroup(Group group)
        throws SchemaException
    {
        if (group == null) return;

        String name = group.getName();
        if (name != null) {
            String key = "group:"+name;
            //-- check for naming collisions
            if (_resolver.resolve(key) != null) {
                String err = "A group definition with the given name, ";
                err += name + ", already exists in this scope.";
                throw new SchemaException(err);
            }

            _resolver.addResolvable(key, group);
        }

        //-- add to content model
        _contentModel.addElement(group);
    } //-- addGroup

    /**
     * Removes the given Group from this ContentModelGroup.
     * @param group the Group to remove.
     * @return true if the group has been successfully removed, false otherwise.
     */
     public boolean removeGroup(Group group) {
       if (group == null)
            return false;
        int position = _contentModel.indexOf(group);
        if (position >= 0) {
	         String name = group.getName();
             if (name != null) {
                 String key = "group:"+name;
                 _resolver.removeResolvable(key);
             }
            _contentModel.removeElementAt(position);
             return true;
	   }
       return false;
     }


    /**
     * Adds the given ModelGroup Definition to this ContentModelGroup
     * @param group the ModelGroup to add
     * @exception SchemaException when a modelgroup with the same name as the
     * specified group already exists in the current scope
    **/
    public void addGroup(ModelGroup group)
        throws SchemaException
    {
        if (group == null) return;

        String name = group.getName();
        if ((name != null) && (!group.isReference())) {
            String key = "group:"+name;
            //-- check for naming collisions
            if (_resolver.resolve(key) != null) {
                String err = "An element declaration with the given name, ";
                err += name + ", already exists in this scope.";
                throw new SchemaException(err);
            }

            _resolver.addResolvable(key, group);
        }

        //-- add to content model
        _contentModel.addElement(group);
    } //-- addGroup


    /**
     * Removes the given ModelGroup Definition from this ContentModelGroup.
     * @param group the ModelGroup Definition to remove.
     * @return true if the group has been successfully removed, false otherwise.
     */
     public boolean removeGroup(ModelGroup group){
       if (group == null)
            return false;
        int position = _contentModel.indexOf(group);
        if (position >= 0) {
            String name = group.getName();
            if ((name != null) && (!group.isReference())) {
                String key = "group:"+name;
                _resolver.removeResolvable(key);
            }
	        _contentModel.removeElementAt(position);
	        return true;
	   }
       return false;
     }

    /**
     * Removes the given Wildcard from this Group.
     *
     * @param wilcard the Wildcard to remove.
     * @return true if the wildcard has been successfully removed, false otherwise.
     */
    public boolean removeWildcard(Wildcard wildcard) {
         if (wildcard == null)
            return false;
        int position = _contentModel.indexOf(wildcard);
        if (position >= 0) {
	        _contentModel.removeElementAt(position);
	        return true;
	    }
        return false;
    }


    /**
     * Returns an enumeration of all the Particles contained
     * within this ContentModelGroup
     *
     * @return an enumeration of all the Particels contained
     * within this ContentModelGroup
    **/
    public Enumeration enumerate() {
        return _contentModel.elements();
    } //-- enumerate

    /**
     * Returns the element declaration with the given name, or null if no
     * element declaration with that name exists in this ContentModelGroup.
     *
     * @param name the name of the element.
     * @return the ElementDecl with the given name, or null if no
     * ElementDecl exists in this ContentModelGroup.
    **/
    public ElementDecl getElementDecl(String name) {
        if (name == null) return null;
        ElementDecl result = null;
        if (_resolver != null) {
            String key = "element:"+name;
            result =  (ElementDecl) _resolver.resolve(key);
            //resolver is always not null (initialized in the constructor)
            //but it may not contain the element (in case of a complexType)
            if (result != null)
                return result;
        }
        for (int i = 0; i < _contentModel.size(); i++) {
            Particle p = (Particle) _contentModel.elementAt(i);
            switch (p.getStructureType()) {
                case Structure.ELEMENT:
                    ElementDecl e = (ElementDecl)p;
                    if (name.equals(e.getName())) {
                        result = e;
                    }
                    break;
                case Structure.GROUP:
                case Structure.MODELGROUP:
                    result = ((ContentModelGroup)p).getElementDecl(name);
                    break;
                default:
                    break;
            }
            if (result!=null) break;
        }
        return result;
    } //-- getElementDecl

    /**
     * Returns the maximum number of occurances that this ContentModelGroup
     * may appear
     * @return the maximum number of occurances that this ContentModelGroup
     * may appear.
     * A non positive (n < 1) value indicates that the
     * value is unspecified (ie. unbounded).
    **/
    public int getMaxOccurs() {
        return 1;
    }

    /**
     * Returns the minimum number of occurances that this ContentModelGroup
     * must appear
     * @return the minimum number of occurances that this ContentModelGroup
     * must appear
     * A negative (n < 0) value indicates that the value is unspecified.
    **/
    public int getMinOccurs() {
        return 1;
    }

    /**
     * Returns the Particle at the specified index
     * @param index the index of the particle to return
     * @returns the CMParticle at the specified index
    **/
    public Particle getParticle(int index) {
        return (Particle) _contentModel.elementAt(index);
    } //-- getParticle

    /**
     * Returns the number of particles contained within
     * this ContentModelGroup
     *
     * @return the number of particles
    **/
    public int getParticleCount() {
        return _contentModel.size();
    } //-- getParticleCount


} //-- ContentModelGroup