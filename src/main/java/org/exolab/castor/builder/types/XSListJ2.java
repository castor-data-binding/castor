/**
 * Copyright 2000 (C) Intalio Inc. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 1. Redistributions of source code must retain copyright statements
 * and notices. Redistributions must also contain a copy of this
 * document.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. The name "ExoLab" must not be used to endorse or promote products
 * derived from this Software without prior written permission of
 * Intalio Inc. For written permission, please contact info@exolab.org.
 * 4. Products derived from this Software may not be called "Castor"
 * nor may "Castor" appear in their names without prior written
 * permission of Intalio Inc. Exolab, Castor and Intalio are
 * trademarks of Intalio Inc.
 * 5. Due credit should be given to the ExoLab Project
 * (http://www.exolab.org/).
 * THIS SOFTWARE IS PROVIDED BY INTALIO AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL INTALIO OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.exolab.castor.builder.types;


import org.exolab.javasource.*;


/**
 * A list type for Java 2 collection that adapts the Castor preset list type.
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
**/
public class XSListJ2 extends XSList {


    int maxSize = -1; //-- undefined
    int minSize = 0;

    XSType contentType = null;

    /**
     * The JType represented by this XSType
    **/
    private JType jType = null;

    public XSListJ2(XSType contentType, String collectionType, final boolean useJava50) {
        super(contentType, useJava50);
        this.contentType = contentType;
        if (collectionType.equalsIgnoreCase("arraylist")) {
            this.jType = new JCollectionType("java.util.List", "java.util.ArrayList", contentType.getJType(), useJava50);
        } else if (collectionType.equalsIgnoreCase("collection")) {
            this.jType = new JCollectionType("java.util.Collection", "java.util.LinkedList", contentType.getJType(), useJava50);
        } else if (collectionType.equalsIgnoreCase("set")) {
            this.jType = new JCollectionType("java.util.Set", "java.util.HashSet", contentType.getJType(), useJava50);
        } else if (collectionType.equalsIgnoreCase("sortedset")) {
            this.jType = new JCollectionType("java.util.SortedSet", "java.util.TreeSet", contentType.getJType(), useJava50);
        }
    } //-- XSListJ2


    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jType;
    }

    public int getMinimumSize() {
        return minSize;
    } //-- getMinimumSize

    public int getMaximumSize() {
        return maxSize;
    } //-- getMaximumSize

    public XSType getContentType() {
        return contentType;
    }

    public void setMaximumSize(int size) {
        maxSize = size;
    } //-- setMaximumSize

    public void setMinimumSize(int size) {
        minSize = size;
    } //-- setMinimumSize

} //-- XSListJ2

