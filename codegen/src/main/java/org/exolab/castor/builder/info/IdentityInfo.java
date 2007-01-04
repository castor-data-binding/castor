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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.info;

import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.builder.types.XSId;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class IdentityInfo extends FieldInfo {

    public IdentityInfo(final String name) {
        super(new XSId(), name);
        setNodeType(XMLInfo.ATTRIBUTE_TYPE);
    } // -- SGId

    public JMethod[] createAccessMethods() {
        String mname = getMethodSuffix();
        JType jType = getSchemaType().getJType();

        JMethod[] methods = new JMethod[3];
        methods[0] = makeGetMethod(mname, jType); // -- create get method
        methods[1] = makeSetMethod(mname, jType); // -- create set method
        methods[2] = makeGetReferenceIdMethod(); // -- create getReferenceId
                                                 // (from Referable Interface)

        return methods;
    } // -- createAccessMethods

    /**
     * @param mname
     * @param jType
     * @return a new JMethod that is a getter for this field
     */
    private JMethod makeGetMethod(final String mname, final JType jType) {
        JMethod method = new JMethod("get" + mname, jType,
                "the value of field '" + mname + "'.");
        JSourceCode jsc = method.getSourceCode();
        jsc.add("return this.");
        jsc.append(getName());
        jsc.append(";");
        return method;
    }

    /**
     * @param mname
     * @param jType
     * @return a new JMethod that is a setter for this field
     */
    private JMethod makeSetMethod(final String mname, final JType jType) {
        JMethod method = new JMethod("set" + mname);
        method.addParameter(new JParameter(jType, getName()));
        JSourceCode jsc = method.getSourceCode();
        jsc.add("this.");
        jsc.append(getName());
        jsc.append(" = ");
        jsc.append(getName());
        jsc.append(";");

        //-- add resolver registration
        //jsc.add("if (idResolver != null) ");
        //jsc.indent();
        //jsc.add("idResolver.addResolvable(");
        //jsc.append(getName());
        //jsc.append(", this);");
        //jsc.unindent();

        return method;
    }

    private JMethod makeGetReferenceIdMethod() {
        JMethod method = new JMethod("getReferenceId", SGTypes.STRING,
                "the reference ID");
        JSourceCode jsc = method.getSourceCode();
        jsc.add("return this.");
        jsc.append(getName());
        jsc.append(";");
        return method;
    }

}
