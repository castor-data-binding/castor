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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.JavaNaming;

import java.util.Hashtable;

/*** 
 * A simple class used for creating class names for unnamed Groups 
 * in XML Schema. 
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a> 
 * @version $Revision$ $Date$ 
**/ 
public class GroupNaming { 

    private Hashtable _packageGroupNames = null; 

    /** 
     * Creates a new GroupNaming 
    **/ 
    public GroupNaming() { 
        _packageGroupNames = new Hashtable(); 
    } //-- GroupNaming 

    private String getGroupName(Group group, String packageName) { 
        Hashtable groupNames = (Hashtable)_packageGroupNames.get(packageName); 
        if (groupNames == null) { 
            return null; 
        } 
        return (String)groupNames.get(group); 
    } 

    private void putGroupName(Group group, String packageName, String name) { 
        Hashtable groupNames = (Hashtable)_packageGroupNames.get(packageName); 
        if (groupNames == null) { 
            groupNames = new Hashtable(); 
            _packageGroupNames.put(packageName, groupNames); 
        } 
        groupNames.put(group, name); 
    } 

    private boolean containsGroupName(String packageName, String name) { 
        Hashtable groupNames = (Hashtable)_packageGroupNames.get(packageName); 
        if (groupNames == null) { 
            return false; 
        } 
        return groupNames.contains(name); 
    } 

    /** 
     * Creates a class name for the given Group. A null value 
     * will be returned if a name cannot be created for the group. 
     * 
     * @return the class name for the given Group. 
    **/ 
    public String createClassName(Group group, String packageName) { 
        String name = group.getName(); 
        if (name != null) { 
            return JavaNaming.toJavaClassName(name); 
        } 
        else { 
            name = getGroupName(group, packageName); 
            if (name != null) return name; 

            Structure parent = group.getParent(); 
            if (parent == null) return null; 

            boolean addOrder = true; 
            switch(parent.getStructureType()) { 
                case Structure.GROUP: 
                    name = createClassName((Group)parent, packageName); 
                    break; 
                case Structure.MODELGROUP: 
                    name = ((ModelGroup)parent).getName(); 
                    name = JavaNaming.toJavaClassName(name); 
                    addOrder = false; 
                    break; 
                case Structure.COMPLEX_TYPE: 
                    name = getClassName((ComplexType)parent); 
                    addOrder = false; 
                    break; 
                default: 
                    break; 
            } 

            if (name != null) { 

                if (addOrder) { 
                    String order = group.getOrder().toString(); 
                    name += JavaNaming.toJavaClassName(order); 
                } 

                int count = 2; 
                String tmpName = name; 
                while (containsGroupName(packageName, name)) { 
                    name = tmpName + count; 
                    ++count; 
                } 
                putGroupName(group, packageName, name); 
            } 
        } 
        return name; 
    } //-- createClassName 

    /** 
     * Returns the class name for the given ComplexType 
     * 
     * @return the class name for the given ComplexType 
    **/ 
    private static String getClassName(ComplexType complexType) { 

        //-- if complexType has name, simply return it 
        String name = complexType.getName(); 
        if (name != null) { 
            return JavaNaming.toJavaClassName(name); 
        } 

        //-- otherwise get name of containing element 
        Structure parent = complexType.getParent(); 
        if (parent != null) { 
            if (parent.getStructureType() == Structure.ELEMENT) { 
                name = ((ElementDecl)parent).getName(); 
            } 
        } 
        if (name != null) { 
            name = JavaNaming.toJavaClassName(name); 
        } 
        return name; 
    } //-- getName 

} //-- class GroupNaming
