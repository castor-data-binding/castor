/*
 * (C) Copyright Keith Visco 2003  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */

package org.exolab.adaptx.jaxp.transform.sax;

//-- Adaptx
import org.exolab.adaptx.jaxp.transform.TemplatesImpl;
import org.exolab.adaptx.xslt.util.StylesheetHandler;
import org.exolab.adaptx.net.impl.URILocationImpl;

//-- JAXP
import javax.xml.transform.Templates;
import javax.xml.transform.sax.TemplatesHandler;

/**
 * An implemenation of the JAXP TemplatesHandler interface
 *
 * <p>see javax.xml.transform.sax.TemplatesHandler 
 * for more information</p>
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class TemplatesHandlerImpl extends StylesheetHandler
    implements TemplatesHandler
{

    
    /**
     * The systemId for the templates instance
     */
    private String _systemId = null;
    
    /**
     * Creates a new TemplatesHandlerImpl
     */
    public TemplatesHandlerImpl() {
        super();
    }

    /**
     * When a TemplatesHandler object is used as a ContentHandler or DocumentHandler
     * for the parsing of transformation instructions, it creates a Templates object, 
     * which the caller can get once the SAX events have been completed.
     * 
     * @return The Templates object that was created during
     * the SAX event process, or null if no Templates object has
     * been created.
     */
    public Templates getTemplates() {
        return new TemplatesImpl(getStylesheet());
    } //-- getTemplates
    
    /**
     * Set the base ID (URI or system ID) for the Templates object
     * created by this builder.  This must be set in order to
     * resolve relative URIs in the stylesheet.  This must be 
     * called before the startDocument event.
     * 
     * @param baseID Base URI for this stylesheet.
     */
    public void setSystemId(String systemId) {
        if (systemId == null) {
            String error = "The argument 'systemId' must not be null.";
            throw new IllegalArgumentException(error);
        }
        _systemId = systemId;
        setURILocation(new URILocationImpl(systemId));
    } //-- setSystemId
  
    /**
     * Get the base ID (URI or system ID) from where relative 
     * URLs will be resolved.
     * @return The systemID that was set with {@link #setSystemId}.
     */
    public String getSystemId() {
        return _systemId;
    } //-- getSystemId
    
} //-- TemplatesHandlerImpl
