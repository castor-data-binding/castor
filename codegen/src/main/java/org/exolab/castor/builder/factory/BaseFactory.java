/*
 * Copyright 2005 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.builder.factory;

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.GroupNaming;
import org.exolab.castor.builder.SourceGenerator;

/**
 * This class defines a base type for the source generator code factory classes.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6287 $ $Date: $
 */
public class BaseFactory {

    /** The BuilderConfiguration instance, for callbacks to obtain certain
     *  configured properties. */
    private final BuilderConfiguration _config;
    
    /** The type factory. */
    private final FieldInfoFactory _infoFactory;
    
    /** A GroupNaming helper class used to named anonymous groups. */
    private GroupNaming _groupNaming = null;
    
    /**
     * The calling instance of {@link SourceGenerator}.
     */
    private SourceGenerator _sourceGenerator;

    /**
     * Creates an instance of this class.
     * @param config XML code generator configuration
     * @param infoFactory the FieldInfoFactory to use
     * @param groupNaming Group naming scheme to be used.
     * @param sourceGenerator The calling source generator.
     */
    public BaseFactory(final BuilderConfiguration config,
            final FieldInfoFactory infoFactory, 
            final GroupNaming groupNaming,
            final SourceGenerator sourceGenerator) {
        if (config == null) {
            String err = "The 'BuilderConfiguration' argument must not be null.";
            throw new IllegalArgumentException(err);
        }
        _config = config;

        if (infoFactory == null) {
            this._infoFactory = new FieldInfoFactory();
        } else {
            this._infoFactory = infoFactory;
        }
        _groupNaming = groupNaming;
        _sourceGenerator = sourceGenerator;
    }
    
    /**
     * Get BuilderConfiguration instance, for callbacks to obtain certain configured properties.
     * 
     * @return BuilderConfiguration instance.
     */
    protected final BuilderConfiguration getConfig() {
        return _config;
    }
    
    /**
     * Get type factory.
     * 
     * @return Type factory.
     */
    protected final FieldInfoFactory getInfoFactory() {
        return _infoFactory;
    }
    
    /**
     * Normalizes the given string for use in comments.
     *
     * @param value
     *            the String to normalize
     * @return the given string, normalized, for use in comments.
     */
    protected final String normalize(final String value) {
        if (value == null) {
            return null;
        }

        char[]  chars    = value.toCharArray();
        char[]  newChars = new char[chars.length * 2];
        int     count    = 0;
        int     i        = 0;
        boolean skip     = false;

        while (i < chars.length) {
            char ch = chars[i++];

            if ((ch == ' ') || (ch == '\t')) {
                if ((!skip) && (count != 0)) {
                    newChars[count++] = ' ';
                }
                skip = true;
            } else if (ch == '*') {
                if (i < chars.length && chars[i] == '/') {
                    newChars[count++] = ch;
                    newChars[count++] = '\\';
                }
            } else {
                if (count == 0) {
                    //-- ignore new lines only if count == 0
                    if ((ch == '\r') || (ch == '\n')) {
                        continue;
                    }
                }
                newChars[count++] = ch;
                skip = false;
            }
        }
        return new String(newChars, 0, count);
    }

    /**
     * Returns the group naming helper class for naming nested anonymous groups.
     * 
     * @return the group naming helper class for naming nested anonymous groups.
     */
    public final GroupNaming getGroupNaming() {
        return _groupNaming;
    }

    /**
     * Sets the group naming helper class for naming nested anonymous groups.
     * @param groupNaming the group naming helper class for naming nested anonymous groups.
     */
    public final void setGroupNaming(final GroupNaming groupNaming) {
        _groupNaming = groupNaming;
    }

    /**
     * Returns the calling {@link SourceGenerator} instance.
     * @return the calling source generator 
     */
    protected SourceGenerator getSourceGenerator() {
        return _sourceGenerator;
    }

}
