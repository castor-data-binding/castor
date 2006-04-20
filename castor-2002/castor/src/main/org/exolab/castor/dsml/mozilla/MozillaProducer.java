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


package org.exolab.castor.dsml.mozilla;


import java.util.Enumeration;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPSchema;
import netscape.ldap.LDAPAttributeSchema;
import netscape.ldap.LDAPObjectClassSchema;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPv2;
import netscape.ldap.LDAPSearchConstraints;
import org.exolab.castor.dsml.Producer;
import org.exolab.castor.util.MimeBase64Encoder;
import org.exolab.castor.dsml.XML;


/**
 *
 *
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class MozillaProducer
    extends Producer
{


    public MozillaProducer( DocumentHandler docHandler, boolean namespace  )
    {
	super( docHandler, namespace );
    }


    public void produce( LDAPEntry entry )
	throws SAXException
    {
	AttributeListImpl attrList;
	LDAPAttributeSet  attrSet;
	LDAPAttribute     attr;
	Enumeration       enum;
	Enumeration       values;
	byte[]            value;

	leaveSchema();
	enterDirectory();

	// dsml:entry dn
	attrList = new AttributeListImpl();
	attrList.addAttribute( XML.Entries.Attributes.DN, "CDATA", entry.getDN() );
	// dsml:entry
	_docHandler.startElement( prefix( XML.Entries.Elements.Entry ), attrList );
	
	attrSet = entry.getAttributeSet();
	if ( attrSet != null ) {
	    
	    attr = attrSet.getAttribute( "objectclass" );
	    if ( attr != null ) {
		// dsml:objectclass
		attrList = new AttributeListImpl();
		_docHandler.startElement( prefix( XML.Entries.Elements.ObjectClass ), attrList );
		values = attr.getStringValues();
		while ( values.hasMoreElements() ) {
		    char[] chars;
		    
		    // dsml:oc-value
		    chars = ( (String) values.nextElement() ).toCharArray();
		    attrList = new AttributeListImpl();
		    _docHandler.startElement( prefix( XML.Entries.Elements.OCValue ), attrList );
		    _docHandler.characters( chars, 0, chars.length );
		    _docHandler.endElement( prefix( XML.Entries.Elements.OCValue ) );
		}
		_docHandler.endElement( prefix( XML.Entries.Elements.ObjectClass ) );
	    }
	    
	    enum = attrSet.getAttributes();
	    while ( enum.hasMoreElements() ) {
		// dsml:attr
		attr = (LDAPAttribute) enum.nextElement();
		if ( attr.getName().equals( "objectclass" ) )
		    continue;
		attrList = new AttributeListImpl();
		attrList.addAttribute( XML.Entries.Attributes.Name, "CDATA", attr.getName() );
		_docHandler.startElement( prefix( XML.Entries.Elements.Attribute ), attrList );
		
		values = attr.getByteValues();
		while ( values.hasMoreElements() ) {
		    char[] chars;
		    int    i;

		    // dsml:value
		    value = (byte[]) values.nextElement();
		    attrList = new AttributeListImpl();
		    if ( value == null ) {
			chars = new char[ 0 ];
		    } else {
			// XXX We have no way of knowing if the attribute is
			//     string or binary, so we do this stupid check
			//     to determine and print it as ASCII text or
			//     base 64 encoding.
			//     (note: OpenLDAP does not provide the attributes
			//     schema as one would hope)
			for ( i = 0 ; i < value.length ; ++i ) {
			    if ( value[ i ] < 0x20 || value[ i ] == 0x7f )
				break;
			}
			if ( i == value.length ) {
			    chars = new char[ value.length ];
			    for ( i = 0 ; i < value.length ; ++i )
				chars[ i ] = (char) value[ i ];
			} else {
			    MimeBase64Encoder encoder;
			    
			    encoder = new MimeBase64Encoder();
			    encoder.translate( value );
			    chars = encoder.getCharArray();
			    attrList.addAttribute( XML.Entries.Attributes.Encoding, "NMTOKEN",
						   XML.Entries.Attributes.Encodings.Base64 );
			}
		    }

		    _docHandler.startElement( prefix( XML.Entries.Elements.Value ), attrList );
		    _docHandler.characters( chars, 0, chars.length );
		    _docHandler.endElement( prefix( XML.Entries.Elements.Value ) );
		}
		_docHandler.endElement( prefix( XML.Entries.Elements.Attribute ) );
	    }
	}
	_docHandler.endElement( prefix( XML.Entries.Elements.Entry ) );
    }
    

    public void produce( Enumeration entries )
	throws SAXException
    {
	while ( entries.hasMoreElements() ) {
	    produce( (LDAPEntry) entries.nextElement() );
	}
    }


    public void produce( LDAPSearchResults entries )
	throws SAXException
    {
	while ( entries.hasMoreElements() ) {
	    produce( (LDAPEntry) entries.nextElement() );
	}
    }


    public void produce( LDAPSchema schema )
	throws SAXException
    {
	Enumeration       enum;

	enum = schema.getObjectClasses();
	while ( enum.hasMoreElements() ) {
	    produce( (LDAPObjectClassSchema) enum.nextElement() );
	}
	enum = schema.getAttributes();
	while ( enum.hasMoreElements() ) {
	    produce( (LDAPAttributeSchema) enum.nextElement() );
	}
    }


    public void produce( LDAPObjectClassSchema schema )
	throws SAXException
    {
	AttributeListImpl attrList;
	String            superiors[];
	String            superior;
	int               i;
	Enumeration       enum;

	leaveDirectory();
	enterSchema();
	
	attrList = new AttributeListImpl();
	// dsml:class id
	attrList.addAttribute( XML.Schema.Attributes.Id, "ID", schema.getName() );
	// dsml:class superior
	superiors = schema.getSuperiors();
	superior = null;
	for ( i = 0 ; i < superiors.length ; ++i ) {
	    if ( i == 0 )
		superior = superiors[ i ];
	    else
		superior = superior + "," + superiors[ i ];
	}
	if ( i > 0 )
	    attrList.addAttribute( XML.Schema.Attributes.Superior, "CDATA", superior );
	// dsml:class obsolete
	attrList.addAttribute( XML.Schema.Attributes.Obsolete, null,
			       schema.isObsolete() ? "true" : "false" );
	// dsml:class type
	switch ( schema.getType() ) {
	case LDAPObjectClassSchema.STRUCTURAL:
	    attrList.addAttribute( XML.Schema.Attributes.Type, null,
				   XML.Schema.Attributes.Types.Structural );
	    break;
	case LDAPObjectClassSchema.ABSTRACT:
	    attrList.addAttribute( XML.Schema.Attributes.Type, null,
				   XML.Schema.Attributes.Types.Abstract );
	    break;
	case LDAPObjectClassSchema.AUXILIARY:
	    attrList.addAttribute( XML.Schema.Attributes.Type, null,
				   XML.Schema.Attributes.Types.Auxiliary );
	    break;
	}

	// dsml:class
	_docHandler.startElement( prefix( XML.Schema.Elements.Class ), attrList );

	// dsml:class name
	if ( schema.getName() != null ) {
	    attrList = new AttributeListImpl();
	    _docHandler.startElement( prefix( XML.Schema.Elements.Name ), attrList );
	    _docHandler.characters( schema.getName().toCharArray(), 0,
				    schema.getName().length() );
	    _docHandler.endElement( prefix( XML.Schema.Elements.Name ) );
	}
	// dsml:class description
	if ( schema.getDescription() != null ) {
	    attrList = new AttributeListImpl();
	    _docHandler.startElement( prefix( XML.Schema.Elements.Description ), attrList );
	    _docHandler.characters( schema.getDescription().toCharArray(), 0,
				    schema.getDescription().length() );
	    _docHandler.endElement( prefix( XML.Schema.Elements.Description ) );
	}
	// dsml:class object-identifier
	if ( schema.getID() != null ) {
	    attrList = new AttributeListImpl();
	    _docHandler.startElement( prefix( XML.Schema.Elements.OID ), attrList );
	    _docHandler.characters( schema.getID().toCharArray(), 0,
				    schema.getID().length() );
	    _docHandler.endElement( prefix( XML.Schema.Elements.OID ) );
	}

	// dsml:class attribute required=false
	enum = schema.getOptionalAttributes();
	while ( enum.hasMoreElements() ) {
	    attrList = new AttributeListImpl();
	    attrList.addAttribute( XML.Schema.Attributes.Ref, "CDATA",
				   "#" + (String) enum.nextElement() );
	    attrList.addAttribute( XML.Schema.Attributes.Required, null, "false" );
	    _docHandler.startElement( prefix( XML.Schema.Elements.Attribute) , attrList );
	    _docHandler.endElement( prefix( XML.Schema.Elements.Attribute ) );
	}
	// dsml:class attribute required=true
	enum = schema.getRequiredAttributes();
	while ( enum.hasMoreElements() ) {
	    attrList = new AttributeListImpl();
	    attrList.addAttribute( XML.Schema.Attributes.Ref, "CDATA",
				   "#" + (String) enum.nextElement() );
	    attrList.addAttribute( XML.Schema.Attributes.Required, null, "true" );
	    _docHandler.startElement( prefix( XML.Schema.Elements.Attribute) , attrList );
	    _docHandler.endElement( prefix( XML.Schema.Elements.Attribute ) );
	}

	_docHandler.endElement( prefix( XML.Schema.Elements.Class ) );
    }


    public void produce( LDAPAttributeSchema schema )
	throws SAXException
    {
	AttributeListImpl attrList;

	leaveDirectory();
	enterSchema();
 
	attrList = new AttributeListImpl();
	// dsml:attribute id
	attrList.addAttribute( XML.Schema.Attributes.Id, "ID", schema.getName() );
	// dsml:attribute superior
	if ( schema.getSuperior() != null ) {
	    attrList.addAttribute( XML.Schema.Attributes.Superior, "CDATA", "#" + schema.getSuperior() );
	}
	// dsml:attribute obsolete
	attrList.addAttribute( XML.Schema.Attributes.Obsolete, null,
			       schema.isObsolete() ? "true" : "false" );
	// dsml:attribute single-value
	attrList.addAttribute( XML.Schema.Attributes.SingleValue, null,
			       schema.isSingleValued() ? "true" : "false" );
	// dsml:attribute user-modification
	// XXX

	// dsml:attribute
	_docHandler.startElement( prefix( XML.Schema.Elements.AttributeType) , attrList );

	// dsml:attribute name
	if ( schema.getName() != null ) {
	    attrList = new AttributeListImpl();
	    _docHandler.startElement( prefix( XML.Schema.Elements.Name ), attrList );
	    _docHandler.characters( schema.getName().toCharArray(), 0,
				    schema.getName().length() );
	    _docHandler.endElement( prefix( XML.Schema.Elements.Name ) );
	}
	// dsml:attribute description
	if ( schema.getDescription() != null ) {
	    attrList = new AttributeListImpl();
	    _docHandler.startElement( prefix( XML.Schema.Elements.Description ), attrList );
	    _docHandler.characters( schema.getDescription().toCharArray(), 0,
				    schema.getDescription().length() );
	    _docHandler.endElement( prefix( XML.Schema.Elements.Description ) );
	}
	// dsml:attribute object-identifier
	if ( schema.getID() != null ) {
	    attrList = new AttributeListImpl();
	    _docHandler.startElement( prefix( XML.Schema.Elements.OID ), attrList );
	    _docHandler.characters( schema.getID().toCharArray(), 0,
				    schema.getID().length() );
	    _docHandler.endElement( prefix( XML.Schema.Elements.OID ) );
	}
	// dsml:attribute syntax
	if ( schema.getSyntaxString() != null ) {
	    attrList = new AttributeListImpl();
	    _docHandler.startElement( prefix( XML.Schema.Elements.Syntax ), attrList );
	    _docHandler.characters( schema.getSyntaxString().toCharArray(), 0,
				    schema.getSyntaxString().length() );
	    _docHandler.endElement( prefix( XML.Schema.Elements.Syntax ) );
	}

	// dsml:attribute equality
	// XXX
	// dsml:attribute ordering
	// XXX
	// dsml:attribute substring
	// XXX

	_docHandler.endElement( prefix( XML.Schema.Elements.AttributeType ) );
    }


}
