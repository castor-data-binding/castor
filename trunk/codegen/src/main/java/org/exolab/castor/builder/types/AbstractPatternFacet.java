/*
 * Copyright 2006-2007 Keith Visco, Edward Kuns
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.castor.builder.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JSourceCode;

/**
 * A base class for types which support the pattern facet.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:edward DOT kuns AT aspect DOT com">Edward Kuns</a>
 * @version $Revision: 6678 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 * @since 1.1
 */
public abstract class AbstractPatternFacet extends XSType {
    //--------------------------------------------------------------------------

    /** The list of pattern facets. */
    private List _patterns = new LinkedList();

    //--------------------------------------------------------------------------

    /**
     * Adds a pattern branch for this XSType. To successfully pass the pattern
     * facets, only one branch needs to pass.
     * 
     * @param pattern The regular expression for this XSType.
     */
    public final void addPattern(final String pattern) {
        _patterns.add(pattern);
    }
    
    /**
     * Get list of pattern facets.
     * 
     * @return List of pattern facets.
     */
    public final List getPatterns() {
        return _patterns;
    }

    //--------------------------------------------------------------------------

    /**
     * Transfer given facet if it is a pattern.
     *
     * @param facet The facet to transfer.
     */
    protected final void addPatternFacet(final Facet facet) {
        if (Facet.PATTERN.equals(facet.getName())) { addPattern(facet.getValue()); }
    }

    /**
     * Generate the source code for pattern facet validation.
     *
     * @param jsc The JSourceCode to fill in.
     * @param validatorName The name of the TypeValidator that the patterns should be added to.
     */
    protected final void codePatternFacet(final JSourceCode jsc, final String validatorName) {
        for (Iterator i = _patterns.iterator(); i.hasNext(); ) {
            jsc.add("{0}.addPattern(\"{1}\");", validatorName, escapePattern((String) i.next()));
        }
    }

    /**
     * Escapes special characters in the given String so that it can be printed correctly.
     *
     * @param str The String to escape.
     * @return The escaped String, or null if the given String was null.
     */
    private static String escapePattern(final String str) {
        if (str == null) { return str; }

        //-- make sure we have characters to escape
        if (str.indexOf('\\') < 0 && str.indexOf('\"') < 0) { return str; }

        StringBuffer sb = new StringBuffer();

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '\\') { sb.append(ch); }
            if (ch == '\"') { sb.append('\\'); }
            sb.append(ch);
        }
        
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}
