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


package org.exolab.castor.dsml.jndi;


import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.SAXException;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import org.exolab.castor.dsml.XML;
import org.exolab.castor.util.MimeBase64Decoder;
import org.exolab.castor.util.Messages;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
class JNDIEntryConsumer
    extends HandlerBase
{


    private String            _entryDN;


    private Attributes       _attrSet;


    private Attribute        _attr;


    private StringBuffer     _value;


    private MimeBase64Decoder  _decoder;


    private Vector           _entries = new Vector();


    JNDIEntryConsumer()
    {
    }


    public Enumeration getSearchResults()
    {
	return _entries.elements();
    }


    public void startElement( String tagName, AttributeList attr )
	throws SAXException
    {
	if ( tagName.equals( XML.Entries.Element ) ) {
	    // Do nothing
	} else if ( tagName.equals( XML.Entries.Elements.Entry ) ) {
	    if ( _attrSet != null )
		throw new SAXException( Messages.format( "dsml.openingTagNotRecognized", tagName ) );
	    _attrSet = new BasicAttributes();
	    _entryDN = attr.getValue( XML.Entries.Attributes.DN );
	} else if ( tagName.equals( XML.Entries.Elements.ObjectClass ) ) {
	    if ( _attrSet == null || _attr != null )
		throw new SAXException( Messages.format( "dsml.openingTagNotRecognized", tagName ) );
	    _attr = new BasicAttribute( "objectclass" );
	} else if ( tagName.equals( XML.Entries.Elements.Attribute ) ) {
	    if ( _attrSet == null || _attr != null )
		throw new SAXException( Messages.format( "dsml.openingTagNotRecognized", tagName ) );
	    _attr = new BasicAttribute( attr.getValue( XML.Entries.Attributes.Name ) );
	} else if ( tagName.equals( XML.Entries.Elements.Value ) ||
		    tagName.equals( XML.Entries.Elements.OCValue ) ) {
	    if ( _attrSet == null || _attr == null || _value != null )
		throw new SAXException( Messages.format( "dsml.openingTagNotRecognized", tagName ) );
	    if ( XML.Entries.Attributes.Encodings.Base64.equals(
		     attr.getValue( XML.Entries.Attributes.Encoding ) ) ) {
		_decoder = new MimeBase64Decoder();
	    } else {
		_value = new StringBuffer();
	    }
	} else {
	    throw new SAXException( Messages.format( "dsml.openingTagNotRecognized", tagName ) );
	}
    }
	    

    public void endElement( String tagName )
	throws SAXException
    {
	if ( tagName.equals( XML.Entries.Element ) ) {
	    if ( _attrSet != null )
		throw new SAXException( Messages.format( "dsml.closingTagNotRecognized", tagName ) );
	} else if ( tagName.equals( XML.Entries.Elements.Entry ) ) {
	    if ( _attrSet == null || _attr != null )
		throw new SAXException( Messages.format( "dsml.closingTagNotRecognized", tagName ) );
	    _entries.addElement( new SearchResult( _entryDN, null, _attrSet ) );
	    _entryDN = null;
	    _attrSet = null;
	} else if ( tagName.equals( XML.Entries.Elements.ObjectClass ) ||
		    tagName.equals( XML.Entries.Elements.Attribute ) ) {
	    if ( _attrSet == null || _attr == null || _value != null )
		throw new SAXException( Messages.format( "dsml.closingTagNotRecognized", tagName ) );
	    _attrSet.put( _attr );
	    _attr = null;
	} else if ( tagName.equals( XML.Entries.Elements.Value ) ||
		    tagName.equals( XML.Entries.Elements.OCValue ) ) {
	    if ( _attrSet == null || _attr == null || ( _value == null && _decoder == null ) )
		throw new SAXException( Messages.format( "dsml.closingTagNotRecognized", tagName ) );
	    if ( _decoder != null ) {
		_attr.add( _decoder.getByteArray() );
		_decoder = null;
	    } else {
		_attr.add( _value.toString() );
		_value = null;
	    }
	} else {
	    throw new SAXException( Messages.format( "dsml.closingTagNotRecognized", tagName ) );
	}
    }


    public void characters( char[] chars, int offset, int length )
    {
	if ( _decoder != null ) {
	    _decoder.translate( chars, offset, length );
	} else if ( _value != null ) {
	    _value.append( chars, offset, length );
	}
    }


}
