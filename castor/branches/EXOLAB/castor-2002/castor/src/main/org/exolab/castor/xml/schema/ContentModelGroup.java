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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * An XML Schema Group
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class ContentModelGroup extends ContentModelType {


    private Vector _contentModel = null;
    private ScopableResolver _resolver = null;
    
    /**
     * Creates a new ContentModelGroup.
    **/
    public ContentModelGroup() {
        _contentModel = new Vector();
        _resolver = new ScopableResolver();
    } //-- ContentModelGroup
    
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
        String key = "element:"+name;
        //-- check for naming collisions
        if (_resolver.resolve(key) != null) {
            String err = "An element declaration with the given name, ";
            err += name + ", already exists in this scope.";
            throw new SchemaException(err);
        }
        
        _resolver.addResolvable(key, elementDecl);
        
        //-- add to content model
        _contentModel.addElement(elementDecl);
        
    } //-- addElementDecl
    
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
                String err = "An element declaration with the given name, ";
                err += name + ", already exists in this scope.";
                throw new SchemaException(err);
            }
            
            _resolver.addResolvable(key, group);
        }
        
        //-- add to content model
        _contentModel.addElement(group);
    } //-- addGroup
    
    public Enumeration enumerate() {
        return _contentModel.elements();
    } //-- enumerate
} //-- ContentModelGroup