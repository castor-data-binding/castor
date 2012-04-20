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
 * An implementation of an XML Schema content model group.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a> 
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
 */
class ContentModelGroupImpl implements ContentModelGroup , java.io.Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -2477271185972337873L;

    /**
     * Collection holding all {@link Particle}s of this content model group.
     */
    private Vector<Particle> _contentModel = new Vector<Particle>();
    
    private transient ScopableResolver _resolver = new ScopableResolver();

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addWildcard(org.exolab.castor.xml.schema.Wildcard)
     */
    public void addWildcard(final Wildcard wildcard) throws SchemaException {
        if (wildcard.isAttributeWildcard()) {
            throw new SchemaException("only <any> should be add in a group.");
        }
        _contentModel.addElement(wildcard);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addElementDecl(org.exolab.castor.xml.schema.ElementDecl)
     */
    public void addElementDecl(final ElementDecl elementDecl)
        throws SchemaException {

        if (elementDecl == null) {
            return;
        }

        String name = elementDecl.getName();
        
        if (!elementDecl.isReference()) {
            String key = "element:" + name;
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

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeElementDecl(org.exolab.castor.xml.schema.ElementDecl)
     */
    public boolean removeElementDecl(final ElementDecl elementDecl) {
        if (elementDecl == null) {
            return false;
        }
        int position = _contentModel.indexOf(elementDecl);
        if (position >= 0) {
            _contentModel.removeElementAt(position);
            if (!elementDecl.isReference()) {
                String key = "element:" + elementDecl.getName();
                _resolver.removeResolvable(key);
            }
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addGroup(org.exolab.castor.xml.schema.Group)
     */
    public void addGroup(final Group group) throws SchemaException {
        if (group == null) {
            return;
        }

        String name = group.getName();
        if (name != null) {
            String key = "group:" + name;
            // -- check for naming collisions
            if (_resolver.resolve(key) != null) {
                String err = "A group definition with the given name, ";
                err += name + ", already exists in this scope.";
                throw new SchemaException(err);
            }

            _resolver.addResolvable(key, group);
        }

        // -- add to content model
        _contentModel.addElement(group);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeGroup(org.exolab.castor.xml.schema.Group)
     */
    public boolean removeGroup(final Group group) {
        if (group == null) {
            return false;
        }
        int position = _contentModel.indexOf(group);
        if (position >= 0) {
            String name = group.getName();
            if (name != null) {
                String key = "group:" + name;
                _resolver.removeResolvable(key);
            }
            _contentModel.removeElementAt(position);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addGroup(org.exolab.castor.xml.schema.ModelGroup)
     */
    public void addGroup(final ModelGroup group) throws SchemaException {
        if (group == null) {
            return;
        }

        String name = group.getName();
        if ((name != null) && (!group.isReference())) {
            String key = "group:" + name;
            // -- check for naming collisions
            if (_resolver.resolve(key) != null) {
                String err = "An element declaration with the given name, ";
                err += name + ", already exists in this scope.";
                throw new SchemaException(err);
            }

            _resolver.addResolvable(key, group);
        }

        // -- add to content model
        _contentModel.addElement(group);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeGroup(org.exolab.castor.xml.schema.ModelGroup)
     */
    public boolean removeGroup(final ModelGroup group) {
        if (group == null) {
            return false;
        }
        int position = _contentModel.indexOf(group);
        if (position >= 0) {
            String name = group.getName();
            if ((name != null) && (!group.isReference())) {
                String key = "group:" + name;
                _resolver.removeResolvable(key);
            }
            _contentModel.removeElementAt(position);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeWildcard(org.exolab.castor.xml.schema.Wildcard)
     */
    public boolean removeWildcard(final Wildcard wildcard) {
        if (wildcard == null) {
            return false;
        }
        int position = _contentModel.indexOf(wildcard);
        if (position >= 0) {
            _contentModel.removeElementAt(position);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#enumerate()
     */
    public Enumeration<Particle> enumerate() {
        return _contentModel.elements();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getElementDecl(java.lang.String)
     */
    public ElementDecl getElementDecl(final String name) {
        if (name == null) {
            return null;
        }
        ElementDecl result = null;
        if (_resolver != null) {
            String key = "element:" + name;
            result = (ElementDecl) _resolver.resolve(key);
            // resolver is always not null (initialized in the constructor)
            // but it may not contain the element (in case of a complexType)
            if (result != null) {
                return result;
            }
        }
        for (int i = 0; i < _contentModel.size(); i++) {
            Particle particle = _contentModel.elementAt(i);
            switch (particle.getStructureType()) {
            case Structure.ELEMENT:
                ElementDecl e = (ElementDecl) particle;
                if (name.equals(e.getName())) {
                    result = e;
                }
                break;
            case Structure.GROUP:
            case Structure.MODELGROUP:
                result = ((ContentModelGroup) particle).getElementDecl(name);
                break;
            default:
                break;
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getMaxOccurs()
     */
    public int getMaxOccurs() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getMinOccurs()
     */
    public int getMinOccurs() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getParticle(int)
     */
    public Particle getParticle(final int index) {
        return _contentModel.elementAt(index);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getParticleCount()
     */
    public int getParticleCount() {
        return _contentModel.size();
    }

}