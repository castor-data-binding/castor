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


import java.util.Vector;
import java.util.Enumeration;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import org.exolab.castor.dsml.Consumer;
import org.exolab.castor.dsml.ImportEventListener;
import org.exolab.castor.dsml.Importer;
import org.exolab.castor.dsml.ImportDescriptor;
import org.exolab.castor.dsml.ImportExportException;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public class JNDIImporter
    extends Importer
{


    private DirContext _ctx;


    public JNDIImporter( DirContext ctx )
    {
	_ctx = ctx;
    }


    protected Consumer createConsumer()
    {
	return new JNDIConsumer();
    }


    public void importEntry( SearchResult result, int policy )
	throws NamingException
    {
	Attributes        existing;
	Vector            modifs;
    Attributes        attrSet;
	Attribute         attr;
	NamingEnumeration enumeration;

	if ( result.getAttributes().size() == 0 ) {

	    if ( ( policy & ImportDescriptor.Policy.DeleteEmpty ) != 0 ) {
		try {
		    _ctx.lookup( result.getName() );
		    _ctx.unbind( result.getName() );
		    notify( result.getName(), ImportEventListener.Deleted );
		} catch ( NameNotFoundException except ) {
		    // Object does not exist, was not removed, ignore.
		    notify( result.getName(), ImportEventListener.Ignored );
		}
	    } else {
		notify( result.getName(), ImportEventListener.Ignored );
	    }

	} else {

	    try {
		existing = _ctx.getAttributes( result.getName() );

		modifs = new Vector();
		attrSet = result.getAttributes();
		enumeration = attrSet.getAll();
		while ( enumeration.hasMore() ) {
		    attr = (Attribute) enumeration.next();
		    if ( existing.get( attr.getID() ) != null ) {
                        if ( ( policy & ImportDescriptor.Policy.NewAttrOnly ) == 0 ) {
                            if ( attr.size() > 0 ) {
                                modifs.addElement( new ModificationItem( DirContext.REPLACE_ATTRIBUTE, attr ) );
                            } else {
                                modifs.addElement( new ModificationItem( DirContext.REMOVE_ATTRIBUTE, attr ) );
                            }
                        }
                    } else {
                        if ( ( policy & ImportDescriptor.Policy.UpdateOnly ) == 0 ) {
                            if ( attr.size() > 0 ) {
                                modifs.addElement( new ModificationItem( DirContext.ADD_ATTRIBUTE, attr ) );
                            }
                        }
                    }
		}
		if ( ( policy & ImportDescriptor.Policy.ReplaceAttr ) != 0 ) {
		    enumeration = existing.getAll();
		    while ( enumeration.hasMore() ) {
			attr = (Attribute) enumeration.next();
			if ( attrSet.get( attr.getID() ) == null ) {
			    modifs.addElement( new ModificationItem( DirContext.REMOVE_ATTRIBUTE, attr ) );
			}
		    }
		}
		if ( modifs.size() > 0 ) {
		    ModificationItem[] array;

		    array = new ModificationItem[ modifs.size() ];
		    modifs.copyInto( array );
		    _ctx.modifyAttributes( result.getName(), array );
		    notify( result.getName(), ImportEventListener.Refreshed );
		} else {
		    notify( result.getName(), ImportEventListener.Ignored );
		}
	    } catch ( NameNotFoundException except ) {
		// Object does not exist, we create a new one.
		if ( ( policy & ImportDescriptor.Policy.RefreshOnly ) == 0 ) {
		    _ctx.bind( result.getName(), null, result.getAttributes() );
		    notify( result.getName(), ImportEventListener.Created );
		} else {
		    notify( result.getName(), ImportEventListener.Ignored );
		}
	    }
	}
    }


    public void importEntries( NamingEnumeration results )
	throws NamingException
    {
	SearchResult result;

	if ( getImportDescriptor() == null )
	    setImportDescriptor( new ImportDescriptor() );
	while ( results.hasMore() ) {
	    result = (SearchResult) results.next();
	    importEntry( result, getImportDescriptor().getPolicy( result.getName() ) );
	}
    }
    

    public void importEntries( Enumeration results )
	throws ImportExportException
    {
	SearchResult result;

	if ( getImportDescriptor() == null )
	    setImportDescriptor( new ImportDescriptor() );
	try {
	    while ( results.hasMoreElements() ) {
		result = (SearchResult) results.nextElement();
		importEntry( result, getImportDescriptor().getPolicy( result.getName() ) );
	    }
	} catch ( NamingException except ) {
	    throw new ImportExportException( except );
	}
    }


}
