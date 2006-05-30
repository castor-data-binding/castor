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


import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;
import org.castor.util.Base64Encoder;
import org.exolab.castor.dsml.XML;
import org.exolab.castor.dsml.Producer;
import org.exolab.castor.dsml.ImportExportException;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2005-08-05 13:58:36 -0600 (Fri, 05 Aug 2005) $
 */
public class JNDIProducer
    extends Producer
{


    public JNDIProducer( DocumentHandler docHandler, boolean namespace  )
    {
        super( docHandler, namespace );
    }


    public void produce( String name, Attributes attrs )
        throws SAXException, NamingException
    {
        AttributeListImpl  attrList;
        Attribute          attr;
        NamingEnumeration  enumeration;
        NamingEnumeration  values;
        Object             value;

        leaveSchema();
        enterDirectory();

        // dsml:entry dn
        attrList = new AttributeListImpl();
        attrList.addAttribute( XML.Entries.Attributes.DN, "CDATA", name );
        // dsml:entry
        _docHandler.startElement( prefix( XML.Entries.Elements.Entry ), attrList );

        if ( attrs != null ) {
            attr = attrs.get( "objectclass" );
            if ( attr != null ) {
                // dsml:objectclass
                attrList = new AttributeListImpl();
                _docHandler.startElement( prefix( XML.Entries.Elements.ObjectClass ),
                                          attrList );
                values = attr.getAll();
                while ( values.hasMore() ) {
                    char[] chars;

                    // dsml:oc-value
                    value = values.next();
                    if ( value != null )
                        chars = value.toString().toCharArray();
                    else
                        chars = new char[ 0 ];
                    attrList = new AttributeListImpl();
                    _docHandler.startElement( prefix( XML.Entries.Elements.OCValue ), attrList );
                    _docHandler.characters( chars, 0, chars.length );
                    _docHandler.endElement( prefix( XML.Entries.Elements.OCValue ) );
                }
                _docHandler.endElement( prefix( XML.Entries.Elements.ObjectClass ) );
            }

            enumeration = attrs.getAll();
            while ( enumeration.hasMore() ) {
                // dsml:attr
                attr = (Attribute) enumeration.next();
                if ( attr.getID().equals( "objectclass" ) )
                    continue;
                attrList = new AttributeListImpl();
                attrList.addAttribute( XML.Entries.Attributes.Name, "CDATA", attr.getID() );
                _docHandler.startElement( prefix( XML.Entries.Elements.Attribute ), attrList );

                values = attr.getAll();
                while ( values.hasMore() ) {
                    char[] chars = null;
                    byte[] bytes = null;

                    attrList = new AttributeListImpl();

                    // dsml:value
                    value = values.next();
                    if ( value == null ) {
                        chars = new char[ 0 ];
                    } else if ( value instanceof String ) {
                        chars = ( (String) value ).toCharArray();
                    } else if (value instanceof byte[]) {
                        bytes = (byte[])value;
                    } else {
                        chars = value.toString().toCharArray();
                    }
                    if (chars != null) {
                        boolean encode = false;
                        boolean wchar = false;
                        int i = 0;
                        while (i < chars.length && !wchar) {
                            char c = chars[i++];
                            if (c >= '\u0100')
                                    encode = wchar = true;
                            else if (c >= '\u0080' || (c < ' ' && c != '\n' && c != '\t'))
                                    encode = true;
                        }
                        if (encode) {
                            if (wchar) {
                                bytes = new byte[chars.length << 1];
                                int j = 0;
                                // bigendian
                                for (i = 0; i < chars.length; i++) {
                                        bytes[j++] = (byte) (chars[i] >> 8);
                                        bytes[j++] = (byte) (0xff & chars[i]);
                                }
                            } else {
                                bytes = new byte[chars.length];
                                for (i = 0; i < chars.length; i++) {
                                        bytes[i] = (byte)chars[i];
                                }
                            }
                        }
                    }

                    if (bytes != null) {
                        chars = Base64Encoder.encode(bytes);
                        attrList.addAttribute( XML.Entries.Attributes.Encoding, "NMTOKEN", 
                                               XML.Entries.Attributes.Encodings.Base64 );
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


    public void produce( SearchResult result )
        throws SAXException
    {
        try {
            produce( result.getName(), result.getAttributes() );
        } catch ( NamingException except ) {
            throw new SAXException( except.toString() );
        }
    }


    public void produce( NamingEnumeration results )
        throws ImportExportException, SAXException
    {
        SearchResult result;

        try {
            while ( results.hasMore() ) {
                result = (SearchResult) results.next();
                produce( result.getName(), result.getAttributes() );
            }
        } catch ( NamingException except ) {
            throw new ImportExportException( except );
        }
    }


}


