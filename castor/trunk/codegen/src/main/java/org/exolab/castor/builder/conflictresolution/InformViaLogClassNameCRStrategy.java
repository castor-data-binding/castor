/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder.conflictresolution;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.builder.SGStateInfo;
import org.exolab.castor.builder.binding.XPathHelper;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.nature.XMLInfoNature;
import org.exolab.castor.util.dialog.ConsoleDialog;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.javasource.JClass;

/**
 * An implementation of {@link ClassNameCRStrategy} that
 * reports any conflict notifications to a log file, basically
 * stating what classes have been overwriten as a result of such
 * a name conflict.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 */
public final class InformViaLogClassNameCRStrategy
extends BaseClassNameCRStrategy implements ClassNameCRStrategy {

    /** Logger instance used for logging naming conflicts. */
    private static final Log LOG = LogFactory.getLog(InformViaLogClassNameCRStrategy.class);
    /** Name of this strategy. */
    public static final String NAME = "informViaLog";

    /**
     * Creates an instance of this name conflict resolution strategy, that will
     * emit warnings to the user as part of a standard logging aproach.
     */
    public InformViaLogClassNameCRStrategy() {
        // Nothing to do.
    }

    /**
     * Handle a class name conflict between newClassInfo and conflict.
     *
     * @param state SourceGeneration state
     * @param newClassInfo ClassInfo for the new class
     * @param conflict JClass for the existing class
     * @return the provided source generator state, as modified by the strategy
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy
     *      #dealWithClassNameConflict(org.exolab.castor.builder.SGStateInfo,
     *      org.exolab.castor.builder.info.ClassInfo,
     *      org.exolab.javasource.JClass)
     */
    public SGStateInfo dealWithClassNameConflict(final SGStateInfo state,
            final ClassInfo newClassInfo, final JClass conflict) {
        // If the ClassInfo are equal, we can just return
        ClassInfo oldClassInfo = state.resolve(conflict);
        if (oldClassInfo == newClassInfo) {
            return state;
        }

        // Find the Schema structures that are conflicting
        Annotated a1 = null;
        Annotated a2 = null;

        // Loop until we exhaust the Enumeration or until we have found both
        Enumeration enumeration = state.keys();
        while (enumeration.hasMoreElements() && (a1 == null || a2 == null)) {
            Object key = enumeration.nextElement();
            if (!(key instanceof Annotated)) {
                continue;
            }

            ClassInfo cInfo = state.resolve(key);
            if (newClassInfo == cInfo) {
                a1 = (Annotated) key;
            } else if (oldClassInfo == cInfo) {
                a2 = (Annotated) key;
            }
        }

        String annotated1XPath;
        String annotated2XPath;
        
        StringBuilder message = new StringBuilder();
        message.append("Warning: A class name generation conflict has occured between ");
        
        if (a1 != null) {
            message.append(SchemaNames.getStructureName(a1));
            message.append(" '");
            if (a1 instanceof ElementDecl) {
                ElementDecl element = (ElementDecl) a1;
                annotated1XPath = XPathHelper.getSchemaLocation(a1) + "[/complexType:" 
                + element.getType().getName() + "]";
                message.append(annotated1XPath);
            } else {
                message.append(XPathHelper.getSchemaLocation(a1));
            }
        } else {
            if (newClassInfo.hasNature(XMLInfoNature.class.getName())) {
                XMLInfoNature xmlNature = new XMLInfoNature(newClassInfo);
                message.append(xmlNature.getNodeTypeName());
                message.append(" '");
                message.append(xmlNature.getNodeName());
            }
        }
        message.append("' and ");
        if (a2 != null) {
            message.append(SchemaNames.getStructureName(a2));
            message.append(" '");
            if (a2 instanceof ElementDecl) {
                ElementDecl element = (ElementDecl) a2;
                annotated2XPath = XPathHelper.getSchemaLocation(a2) + "[/complexType:" 
                + element.getType().getName() + "]";
                message.append(annotated2XPath);
            } else {
                message.append(XPathHelper.getSchemaLocation(a2));
            }
        } else {
            if (oldClassInfo.hasNature(XMLInfoNature.class.getName())) {
                XMLInfoNature xmlNature = new XMLInfoNature(oldClassInfo);
                message.append(xmlNature.getNodeTypeName());
                message.append(" '");
                message.append(xmlNature.getNodeName());
            }
        }
        message.append("'. Please use a Binding file to solve this problem.");
        LOG.warn(message);
        return state;
    }

    /**
     * Returns the name of the strategy.
     * @return the name of the strategy.
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy#getName()
     */
    public String getName() {
        return NAME;
    }

    /**
     * Sets the console dialog to use with this strategy.
     *
     * @param dialog the console dialog to use with this strategy.
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy#
     *      setConsoleDialog(org.exolab.castor.util.dialog.ConsoleDialog)
     */
    public void setConsoleDialog(final ConsoleDialog dialog) {
        // No console dialog needed.
    }

    /**
     * Presents the user with a console dialog, asking for confirmation whether
     * an existing file should be overwritten (or not).
     *
     * @param filename the filename to potentially overwrite.
     * @return whether or not the file should be overwritten.
     *
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy
     *      #dealWithFileOverwrite(java.lang.String, boolean)
     */
    public boolean dealWithFileOverwrite(final String filename) {
        LOG.warn(filename + " already exists, but will be overwritten.");
        return true;
    }

}
