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

package org.exolab.castor.builder;

import org.exolab.javasource.*;

/**
 * Helper class for creating Schema based source code
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class SGHelper {
    
       
    /**
     * Creates a get access method for the given member
     * @param member the JMember to create the get access method for
     * @return the JMethod representing the get access method
    **/
    protected static JMethod createGetMethod(JMember member) {
        
        String methodName = "get"+ member.getName().substring(1);
        
        JMethod method = new JMethod(member.getType(), methodName);
        
        JSourceCode jsc = method.getSourceCode();
        jsc.add("return this.");
        jsc.append(member.getName());
        jsc.append(";");
        
        return method;
        
    } //-- createGetMethod

    
    /**
     * Creates a set access method for the given member
     * @param member the JMember to create the set access method for
     * @return the JMethod representing the set access method
    **/
    protected static JMethod createSetMethod(JMember member) {
        
        String methodName = "set"+ member.getName().substring(1);
        
        JMethod method = new JMethod(null, methodName);
        JParameter param = new JParameter(member.getType(), member.getName());
        method.addParameter(param);
        
        JSourceCode jsc = method.getSourceCode();
        jsc.add("this.");
        jsc.append(member.getName());
        jsc.append(" = ");
        jsc.append(param.getName());
        jsc.append(";");
        
        return method;
        
    } //-- createSetMethod
    
}