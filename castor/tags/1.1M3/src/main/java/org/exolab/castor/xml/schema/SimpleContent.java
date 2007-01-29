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
 * $Id$
 */

package org.exolab.castor.xml.schema;

/**
 * An extension of the ContentType to support simple content
 * extension and restriction for complexTypes.
 * 
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class SimpleContent extends ContentType implements java.io.Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 6406889524623062413L;

    /**
     * The associated parent Schema 
     */
    private Schema _schema = null;
    
    /**
     * The simpleType definition that defines this content type (if any)
     */
    private SimpleType _simpleType;

    /**
     * The name of the simpleType, used when the simpleType
     * has not yet been read or added to the parent Schema
     */
    private String _typeName = null;
    
    /**
     * Creates a new SimpleContent
     */
    public SimpleContent() {
        super(ContentType.SIMPLE);
    } //-- ContentType

    /**
     * Creates a new SimpleContent using the given SimpleContent.
     * This constructor will copy from the given SimpleContent.
     * 
     *
     * @param content the SimpleContent to copy from
     */
    public SimpleContent(SimpleContent content) {
        super(ContentType.SIMPLE);
        if (content != null) {
            _simpleType = content._simpleType;
            _typeName = content._typeName;
            _schema = content._schema;
        }
    } //-- ContentType
    
    /**
     * Creates a new SimpleContent
     *
     * @param simpleType the simpleType of this SimpleContent
     */
    public SimpleContent(SimpleType simpleType) {
        super(ContentType.SIMPLE);
        _simpleType = simpleType;
    } //-- SimpleContent

    /**
     * Creates a new SimpleContent
     *
     * @param schema the associated parent Schema
     * @param typeName the name of the base simpleType, or 
     * complexType (must also be a SimpleContent).
     *
     */
    public SimpleContent(Schema schema, String typeName) {
        super(ContentType.SIMPLE);
        if (schema == null) {
            String err = "The argument 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        if (typeName == null) {
            String err = "The argument 'typeName' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _schema = schema;
        _typeName = typeName;        
    } //-- SimpleContent

    /**
     * Creates a  copy of this SimpleContent
     *
     * @return the new SimpleContent which is a copy of this SimpleContent
     */
    public SimpleContent copy() {
        return new SimpleContent(this);
    } //-- copy
    
   /**
    * Returns the simpleType that represents that contentType. This may
    * be null, if no simpleType has been set.
    * 
    * @return the simpleType that represents that contentType. 
    */
    public SimpleType getSimpleType() {
        if ((_simpleType == null) && (_typeName != null)) {
            XMLType base = _schema.getType(_typeName);
            if (base != null) {
                if (base.isSimpleType()) {
                    _simpleType = (SimpleType)base;
                }
                else {
                    ComplexType complexType = (ComplexType)base;
                    if (complexType.isSimpleContent()) {
                        SimpleContent sc = (SimpleContent)complexType.getContentType();
                        _simpleType = sc.getSimpleType();
                    }
                    else {
                        //-- Report error
                        String error = "The base ComplexType '" + _typeName + "' ";
                        error += "must be a simpleContent.";
                        throw new IllegalStateException(error);
                    }
                }
            }
        }
        return _simpleType;
    } //-- getSimpleType
    
    /**
     * Returns the name of the associated type for this SimpleContent
     *
     * @return the associated type name for this SimpleContent.
     */
    public String getTypeName() {
        if (_simpleType != null) {
            return _simpleType.getName();
        }
        return _typeName;
    } //-- getTypeName
    
    
    /**
     * Sets the simpleType that represents that contentType.
     * 
     * @param simpleType the simpleType to set
     */
    public void setSimpleType(SimpleType simpleType) {
        _simpleType = simpleType;
    } //-- setSimpleType
    

} //-- SimpleContent
