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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping;



/**
 * Represents a Mapped Object. This Class allows for more
 * control over the key used in Maps and Hashtables.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public class MapItem {
    
    /**
     * The key
     */
    private Object _key = null;

    /**
     * The value 
     */
    private Object _value = null;
    
    /**
     * Creates a new empty MapItem.
     */
    public MapItem() {
        super();
    } //-- MapItem
    
    /**
     * Creates a new MapItem with the given key and value.
     *
     * @param key the key Object for this MapItem.
     * @param value the value for this MapItem.
     */
    public MapItem(Object key, Object value) {
        _key = key;
        _value = value;
    } //-- MapItem
    
    /**
     * Returns the key Object for this MapItem, or null
     * if no key has been specified.
     *
     * @return the key Object for this MapItem.
     */
    public Object getKey() {
        return _key;
    } //-- getKey

    /**
     * Returns the value Object for this MapItem, or null
     * if no value has yet been specified.
     *
     * @return the value Object for this MapItem.
     */
    public Object getValue() {
        return _value;
    } //-- getValue
    
    /**
     * Sets the key for this MapItem. 
     *
     * @param key the key Object for this MapItem.
     */    
    public void setKey(Object key) {
        _key = key;
    } //-- setKey
    
    /**
     * Sets the value for this MapItem. 
     *
     * @param value the value Object for this MapItem.
     */    
    public void setValue(Object value) {
        _value = value;
    } //-- setValue
    
} //-- MapItem



