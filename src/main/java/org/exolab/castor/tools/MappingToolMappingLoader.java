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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: MappingTool.java 6026 2006-06-28 07:24:40Z wguttmn $
 */
package org.exolab.castor.tools;

import java.lang.reflect.Array;

import org.castor.mapping.BindingType;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.xml.JavaNaming;

/**
 * Extend mapping loader to give us access to the findAccessor method.
 */
public final class MappingToolMappingLoader extends AbstractMappingLoader {
    private static final String GET = "get";
    private static final String SET = "set";
    private static final String ADD = "add";
    
    MappingToolMappingLoader() { super(null); }
    
    public BindingType getBindingType() { return null; }

    /**
     * Returns true if the get method returns an array.
     * This method is used for greater compatability with
     * generated descriptors.
     *
     * @return if get method returns an array.
    **/
    boolean returnsArray(final Class clazz, final String fieldName, final Class type) {
        try {
            Class array = null;
            if (type.isArray()) {
                array = type;
            } else {
                array = Array.newInstance(type, 0).getClass();
            }
            //-- getMethod
            String prefix = JavaNaming.toJavaClassName(fieldName);
            String method = GET + prefix;
            boolean isGet = true;
            if (findAccessor(clazz, method, array, isGet) != null) {
                return true;
            }
        } catch(Exception ex) {
            // nothing to do
        }
        return false;
    }
    
    boolean canFindAccessors(final Class clazz, final String fieldName, final Class type) {
        try {
            //-- getMethod
            String prefix = JavaNaming.toJavaClassName(fieldName);
            String method = GET + prefix;
            boolean isGet = true;
            if (findAccessor(clazz, method, type, isGet) != null) {
                return true;
            }
                
            //-- setMethod and/or addMethod
            isGet = false;
            method = SET + prefix;
            if (findAccessor(clazz, method, type, isGet) != null) {
                return true;
            }
            method = ADD + prefix;
            if (findAccessor(clazz, method, type, isGet) != null) {
                return true;                
            }
        } catch(Exception ex) {
            // nothing to do
        }
        return false;
    }
}
