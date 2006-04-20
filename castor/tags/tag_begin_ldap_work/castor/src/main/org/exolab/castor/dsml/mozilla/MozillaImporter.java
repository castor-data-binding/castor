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


package org.exolab.castor.dsml.mozilla;


import java.util.Hashtable;
import java.util.Enumeration;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPModification;
import netscape.ldap.LDAPModificationSet;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPDN;
import org.exolab.castor.dsml.XML;
import org.exolab.castor.dsml.Consumer;
import org.exolab.castor.dsml.ImportEventListener;
import org.exolab.castor.dsml.Importer;
import org.exolab.castor.dsml.ImportDescriptor;
import org.exolab.castor.dsml.ImportExportException;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class MozillaImporter
    extends Importer
{


    private LDAPConnection _conn;


    public MozillaImporter( LDAPConnection conn )
    {
	_conn = conn;
    }


    protected Consumer createConsumer()
    {
	return new MozillaConsumer();
    }


    public void importEntry( LDAPEntry entry, int policy )
	throws LDAPException
    {
	LDAPEntry           existing;
	LDAPModificationSet modifs;
	LDAPAttributeSet    attrSet;
	LDAPAttribute       attr;
	int                 i;
	Enumeration         enumeration;

	if ( entry.getAttributeSet() == null ||
	     entry.getAttributeSet().size() == 0 ) {

	    if ( ( policy & ImportDescriptor.Policy.DeleteEmpty ) != 0 ) {
		try {
		    _conn.read( entry.getDN() );
		    _conn.delete( entry.getDN() );
		    notify( entry.getDN(), ImportEventListener.Deleted );
		} catch ( LDAPException except ) {
		    // Object does not exist, was not removed, ignore.
		    // Anything else, we must complain.
		    if ( except.getLDAPResultCode() != LDAPException.NO_SUCH_OBJECT )
			throw except;
		    notify( entry.getDN(), ImportEventListener.Ignored );
		}
	    } else {
		notify( entry.getDN(), ImportEventListener.Ignored );
	    }

	} else {

	    try {
		existing = _conn.read( entry.getDN() );

		modifs = new LDAPModificationSet();
		attrSet = entry.getAttributeSet();
		for ( i = 0 ; i < attrSet.size() ; ++i ) {
                    attr = attrSet.elementAt( i );
                    if ( existing.getAttributeSet().getAttribute( attr.getName() ) != null ) { 
                        if ( ( policy & ImportDescriptor.Policy.NewAttrOnly ) == 0 ) {
                            if ( attr.size() > 0 ) {
                                modifs.add( LDAPModification.REPLACE, attr );
                            } else {
                                modifs.add( LDAPModification.DELETE, attr );
                            }
                        }
                    } else {
                        if ( ( policy & ImportDescriptor.Policy.UpdateOnly ) == 0 ) {
                            if ( attr.size() > 0 ) {
                                modifs.add( LDAPModification.ADD, attr );
                            }
                        }
                    }
                }
                if ( ( policy & ImportDescriptor.Policy.ReplaceAttr ) != 0 ) {
		    enumeration = existing.getAttributeSet().getAttributes();
		    while ( enumeration.hasMoreElements() ) {
			attr = (LDAPAttribute) enumeration.nextElement();
			if ( entry.getAttribute( attr.getName() ) == null ) {
			    modifs.add( LDAPModification.DELETE, attr );
			}
		    }
		}
		if ( modifs.size() > 0 ) {
		    _conn.modify( entry.getDN(), modifs );
		    notify( entry.getDN(), ImportEventListener.Refreshed );
		} else {
		    notify( entry.getDN(), ImportEventListener.Ignored );
		}
	    } catch ( LDAPException except ) {
		// Object does not exist, we create a new one.
		// Anything else, we must complain.
		if ( except.getLDAPResultCode() != LDAPException.NO_SUCH_OBJECT )
		    throw except;
		if ( ( policy & ImportDescriptor.Policy.RefreshOnly ) == 0 ) {
		    _conn.add( entry );
		    notify( entry.getDN(), ImportEventListener.Created );
		} else {
		    notify( entry.getDN(), ImportEventListener.Ignored );
		}
	    }
	}
    }


    public void importEntries( Enumeration entries )
	throws ImportExportException
    {
	LDAPEntry entry;

	if ( getImportDescriptor() == null )
	    setImportDescriptor( new ImportDescriptor() );
	try {
	    while ( entries.hasMoreElements() ) {
		entry = (LDAPEntry) entries.nextElement();
		importEntry( entry, getImportDescriptor().getPolicy( entry.getDN() ) );
	    }
	} catch ( LDAPException except ) {
	    throw new ImportExportException( except );
	}
    }


}
