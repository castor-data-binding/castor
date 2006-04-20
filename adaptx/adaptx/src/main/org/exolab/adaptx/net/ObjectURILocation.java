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
package org.exolab.adaptx.net;


/**
 * An extension of URILocation, useful for dealing with in-memory
 * objects. The implementation should return null, for the 
 * getReader method if the object itself is not null.
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 */
public abstract class ObjectURILocation extends URILocation {
    
    /**
     * Returns the object at this URILocation
     */
    public abstract Object getObject();
    
} //-- ObjectURILocation
