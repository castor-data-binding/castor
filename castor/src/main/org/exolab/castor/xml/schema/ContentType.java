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

package org.exolab.castor.xml.schema;

/**
 * An XML Schema ContentType
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ContentType implements java.io.Serializable {


    public static final short ELEMENT_ONLY = 0;
    public static final short MIXED        = 1;
    public static final short EMPTY        = 2;
    public static final short ANY          = 3;
    public static final short SIMPLETYPE   = 4;
	//need to keep it for the dtd package
	public static final short TEXT_ONLY     = 5;

    public static final ContentType elemOnly   = new ContentType(ELEMENT_ONLY);

    public static final ContentType mixed      = new ContentType(MIXED);

    public static final ContentType empty      = new ContentType(EMPTY);

    public static final ContentType any        = new ContentType(ANY);
    
    

	//need to keep it for the dtd package
	public static final ContentType textOnly     = new ContentType(TEXT_ONLY);


    /* Private Members */

    private static final String[] _names =
    { "elementOnly", "mixed", "empty", "any", "simpleType","textOnly" };

    private short _type = ELEMENT_ONLY;
    
    /**
     * The simpleType definition that defines this content type (if any)
     */
    private SimpleType _simpleType;

    /**
     * Creates a new ContentType.
    **/
    private ContentType(short type) {
        _type = type;
    } //-- ContentType

    /**
     * Returns the type of this ContentType
     * @return the type of this ContentType
    **/
    public short getType() {
        return _type;
    } //-- getType

    /**
     * Returns the String representation of this ContentType
     * @return the String representation of this ContentType
    **/
    public String toString() {
        return _names[_type];
    } //-- toString

   /**
    * Returns the simpleType that represents that contentType. Null is returned
    * when the type of this content type is not 'simpleType'.
    * 
    * @return the simpleType that represents that contentType. Null is returned
    * when the type of this content type is not 'simpleType'.
    */
    public SimpleType getSimpleType() {
        return _simpleType;
    }
    
    /**
     * Sets the simpleType that represents that contentType.
     * This method can only be called when the ContentType is of type
     * 'simpleType'. A runtime exception is thrown if the type is different
     * than 'simpleType'
     * 
     * @param simpleType the simpleType to set
     */
    public void setSimpleType(SimpleType simpleType) {
        if (_type != SIMPLETYPE)
            throw new IllegalStateException("A simpleType cannot be set on a content type that is not of type 'SimpleType'.");
        else 
            _simpleType = simpleType;
    }
    /**
     * Creates a new ContentType based on the given String
     * @param contentType the type of the ContentType to create.
     * <BR />
     * <PRE>
     * The valid values are as follows:
     *  elemOnly, textOnly, mixed, empty, any, simpleType
     * </PRE>
     * @exception IllegalArgumentException when the given type is
     * not one of the possible valid values
    **/
    public static ContentType valueOf(String contentType)
        throws IllegalArgumentException
    {
        if (contentType.equals(_names[ELEMENT_ONLY])) return elemOnly;
        else if (contentType.equals(_names[MIXED])) return mixed;
        else if (contentType.equals(_names[EMPTY])) return empty;
        else if (contentType.equals(_names[ANY])) return any;
        else if (contentType.equals(_names[SIMPLETYPE])){
            return new ContentType(ContentType.SIMPLETYPE);
        }
        else if (contentType.equals(_names[TEXT_ONLY])) return textOnly;
        else {
            String err = contentType;
            err += " is not a valid ContentType";
            throw new IllegalArgumentException(err);
        }
    } //-- valueOf

} //-- ContentType
