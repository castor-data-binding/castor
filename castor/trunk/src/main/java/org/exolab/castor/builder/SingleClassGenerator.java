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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id: $
 */
package org.exolab.castor.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JComment;
import org.exolab.javasource.JNaming;

/**
 * Writes a single class (and any associated inner classes) to a file.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a> - Main author.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a> - Contributions.
 * @author <a href="mailto:nsgreen@thazar.com">Nathan Green</a> - Contributions.
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a> - Separated from SourceGenerator
 * @version $Revision: 0000 $ $Date:  $
 */
public class SingleClassGenerator {
    /**
     * The default code header. Please leave "$" and "Id" separated with "+" so
     * that the CVS server does not expand it here. */
    private static final String DEFAULT_HEADER = "This class was automatically generated with \n"
                                                 + "<a href=\"" + SourceGenerator.APP_URI + "\">"
                                                 + SourceGenerator.APP_NAME + " "
                                                 + SourceGenerator.VERSION
                                                 + "</a>, using an XML Schema.\n$" + "Id" + "$";

    /** Name of the CDR (Class Descriptor Resolver) file. */
    private static final String CDR_FILE = ".castor.cdr";

    /** True if the user should be prompted to overwrite when a file already exists. */
    private boolean _promptForOverwrite = true;
    /** Destination directory where all our output goes. */
    private String _destDir = null;
    /** The line separator to use for output. */
    private String _lineSeparator = null;
    /** A flag indicating whether or not to create descriptors for the generated classes. */
    private boolean _createDescriptors = true;

    /** The header at the top of each generated file. */
    private final JComment _header;
    /** Console dialog used to prompt the user when something is wrong. */
    private final ConsoleDialog _dialog;
    /** The DescriptorSourceFactory instance. */
    private final DescriptorSourceFactory _descSourceFactory;
    /** The MappingFileSourceFactory instance. */
    private final MappingFileSourceFactory _mappingSourceFactory;
    /** The SourceGenerator instance that created us. */
    private final SourceGenerator _sourceGenerator;

    /**
     * Creates an instance of this class.
     * @param dialog A ConsoleDialog instance
     * @param sourceGenerator A SourceGenerator instance
     */
    public SingleClassGenerator(final ConsoleDialog dialog, final SourceGenerator sourceGenerator) {
        this._dialog = dialog;
        this._sourceGenerator = sourceGenerator;
        this._header = new JComment(JComment.HEADER_STYLE);
        this._header.appendComment(DEFAULT_HEADER);
        this._descSourceFactory = new DescriptorSourceFactory(_sourceGenerator);
        this._mappingSourceFactory = new MappingFileSourceFactory(_sourceGenerator);
    }

    /**
     * Sets the destination directory.
     *
     * @param destDir the destination directory.
     */
    public void setDestDir(final String destDir) {
       _destDir = destDir;
    }

    /**
     * Sets the line separator to use when printing the source code.
     *
     * @param lineSeparator
     *            the line separator to use when printing the source code. This
     *            method is useful if you are generating source on one platform,
     *            but will be compiling the source on a different platform.
     *            <B>Note:</B>This can be any string, so be careful. I
     *            recommend either using the default or using one of the
     *            following:
     * <PRE>
     * windows systems use: "\r\n"
     * unix systems use: "\n"
     * mac systems use: "\r"
     * </PRE>
     */
    public void setLineSeparator(final String lineSeparator) {
        _lineSeparator = lineSeparator;
    } //-- setLineSeparator

    /**
     * Sets whether or not to create ClassDescriptors for the generated classes.
     * By default, descriptors are generated.
     *
     * @param createDescriptors
     *            a boolean, when true indicates to generated ClassDescriptors
     */
    public void setDescriptorCreation(final boolean createDescriptors) {
        _createDescriptors = createDescriptors;
    } //-- setDescriptorCreation

    /**
     * Sets whether or not to prompt when we would otherwise overwrite an
     * existing JClass.  If set to false, then it is always OK to overwrite
     * an existing class.  If set to true, the user will be prompted.
     *
     * @param promptForOverwrite the new value
     */
    public void setPromptForOverwrite(final boolean promptForOverwrite) {
        this._promptForOverwrite = promptForOverwrite;
    } //-- setPromptForOverwrite

    /**
     * Processes the JClass mapped by the provided key unless the JClass has
     * already been processed.
     *
     * @param state
     *            SourceGenerator state
     * @param classKeys Enumeration over a collection of keys to ClassInfos
     *
     * @return true if processing is allowed to continue, false if the
     *         SourceGenerator state is STOP_STATUS,
     * @throws IOException
     *             If an already existing '.castor.cdr' file can not be loaded or found
     */
    boolean processIfNotAlreadyProcessed(final Enumeration classKeys,
                                         final SGStateInfo state) throws IOException {
        while (classKeys.hasMoreElements()) {
            ClassInfo classInfo = state.resolve(classKeys.nextElement());
            JClass jClass = classInfo.getJClass();
            if (!state.processed(jClass)) {
                process(jClass, state);
                if (state.getStatusCode() == SGStateInfo.STOP_STATUS) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Processes the given JClasses, one by one, stopping if the SourceGenerator
     * state indicates STOP after processing one class.
     *
     * @param classes
     *            Array of classes to process
     * @param state
     *            SourceGenerator state
     * @return true if processing is allowed to continue, false if the
     *         SourceGenerator state is STOP_STATUS,
     * @throws IOException
     *             If an already existing '.castor.cdr' file can not be loaded or found
     */
    boolean process(final JClass[] classes, final SGStateInfo state) throws IOException {
        for (int i = 0; i < classes.length; i++) {
            process(classes[i], state);
            if (state.getStatusCode() == SGStateInfo.STOP_STATUS) {
                return false;
            }
        }
        return true;
    }

    /**
     * Processes the given JClass by checking for class name conflicts, and if
     * there are none, making the class as processed and then printing the class
     * and, if appropriate, its class descriptors.
     * <p>
     * If there is a class name conflict, at best the user stops the source
     * generation and at worst the user continues, skipping this class.
     *
     * @param jClass
     *            the class to process
     * @param state
     *            SourceGenerator state
     * @return true if processing is allowed to continue, false if the
     *         SourceGenerator state is STOP_STATUS,
     * @throws IOException
     *             If an already existing '.castor.cdr' file can not be loaded or found
     */
    boolean process(final JClass jClass, final SGStateInfo state) throws IOException {
        if (state.getStatusCode() == SGStateInfo.STOP_STATUS) {
            return false;
        }

        if (state.processed(jClass)) {
            return true;
        }

        //--Make sure this class's name doesn't conflict with a java.lang.* class
        checkNameNotReserved(jClass.getName(), state);

        ClassInfo classInfo = state.resolve(jClass);

        //-- Have we already processed a class with this name?
        JClass conflict = state.getProcessed(jClass.getName());
        if (conflict != null && !state.getSuppressNonFatalWarnings()) {
            warnAboutClassNameConflict(state, classInfo, conflict);
            return state.getStatusCode() != SGStateInfo.STOP_STATUS;
        }

        //-- Mark the current class as processed
        state.markAsProcessed(jClass);

        //-- Print the class
        if (checkAllowPrinting(jClass)) {
            //hack for the moment
            //to avoid the compiler complaining with java.util.Date
            jClass.removeImport("org.exolab.castor.types.Date");
            jClass.setHeader(_header);

            jClass.print(_destDir, _lineSeparator);
        }

        //-- Process and print the class descriptor
        if (classInfo != null) {
            processClassDescriptor(jClass, state, classInfo);
        }

        return state.getStatusCode() != SGStateInfo.STOP_STATUS;
    } //-- processJClass

    /**
     * Processes the Class Descriptor for the provided JClass.
     * @param jClass
     *            the classInfo to process
     * @param state
     *            SourceGenerator state
     * @param classInfo
     *            the XML Schema element declaration
     * @throws IOException
     *             If an already existing '.castor.cdr' file can not be loaded or found
     */
    private void processClassDescriptor(final JClass jClass, final SGStateInfo state,
                                        final ClassInfo classInfo) throws IOException {
        if (_createDescriptors) {
            JClass desc = _descSourceFactory.createSource(classInfo);
            if (checkAllowPrinting(desc)) {
                updateCDRFile(jClass, desc, state);
                desc.setHeader(_header);
                desc.print(_destDir, _lineSeparator);
            }
        } else {
            //-- TODO: cleanup mapping file integration (what does this TODO mean?)
            //-- create a class mapping
            String pkg = state._packageName;
            if (pkg == null) {
                pkg = "";
            }
            MappingRoot mapping = state.getMapping(pkg);
            if (mapping == null) {
                mapping = new MappingRoot();
                state.setMapping(pkg, mapping);
            }
            mapping.addClassMapping(_mappingSourceFactory.createMapping(classInfo));
        }
    }

    /**
     * Checks for a class name conflict. If one is found, notifies the user and
     * prompts (depending on various srcgen settings) on whether or not to
     * continue source generation.
     *
     * @param state
     *            SourceGenerator state
     * @param newClassInfo
     *            XML Schema element declaration for the class to check
     * @param conflict
     *            JClass for the class to check
     *
     */
    private void warnAboutClassNameConflict(final SGStateInfo state,
                                            final ClassInfo newClassInfo,
                                            final JClass conflict) {
        //-- if the ClassInfo are equal, we can just return
        ClassInfo oldClassInfo = state.resolve(conflict);
        if (oldClassInfo == newClassInfo) {
            return;
        }

        //-- Find the Schema structures that are conflicting
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

        StringBuffer error = new StringBuffer();
        error.append("Warning: A class name generation conflict has occurred between ");
        if (a1 != null) {
            error.append(SchemaNames.getStructureName(a1));
            error.append(" '");
            error.append(ExtendedBinding.getSchemaLocation(a1));
        } else {
            error.append(newClassInfo.getNodeTypeName());
            error.append(" '");
            error.append(newClassInfo.getNodeName());
        }
        error.append("' and ");
        if (a2 != null) {
            error.append(SchemaNames.getStructureName(a2));
            error.append(" '");
            error.append(ExtendedBinding.getSchemaLocation(a2));
        } else {
            error.append(oldClassInfo.getNodeTypeName());
            error.append(" '");
            error.append(oldClassInfo.getNodeName());
        }
        error.append("'. Please use a Binding file to solve this problem.");
        error.append("Continue anyway [not recommended] ");

        char ch = _dialog.confirm(error.toString(), "yn", "y = yes, n = no");
        if (ch == 'n') {
            state.setStatusCode(SGStateInfo.STOP_STATUS);
        }
    }

    /**
     * Checks to see if we will write the provided JClass to disk. If we have
     * been configured to not prompt for overwrite, then it is assumed and
     * overwriting an existing file is always OK. If the file does not exist, it
     * is always OK to write it. Only if we are configured to prompt for
     * overwrite and the file already exists do we need to issue a dialog and
     * get the user's permission.
     *
     * @param jClass
     *            a JClass to check to see if we can write
     * @return true if we can write out the provided jClass
     */
    private boolean checkAllowPrinting(final JClass jClass) {
        if (!_promptForOverwrite) {
            return true;
        }

        String filename = jClass.getFilename(_destDir);
        File file = new File(filename);

        if (!file.exists()) {
            return true;
        }

        boolean allowPrinting = true;

        String message = filename + " already exists. overwrite";
        char ch = _dialog.confirm(message, "yna", "y = yes, n = no, a = all");
        switch (ch) {
            case 'a':
                _promptForOverwrite = false;
                allowPrinting = true;
                break;
            case 'y':
                allowPrinting = true;
                break;
            default:
                allowPrinting = false;
                break;
        }
        return allowPrinting;
    }

    /**
     * Checks the given name against various naming conflicts.  If a conflict is
     * found, then this method generates an appropriate error message and throws
     * an IllegalArgumentException.
     * @param elementName element name to check against lists of reserved names
     * @param sInfo source generator state
     */
    private void checkNameNotReserved(final String elementName, final SGStateInfo sInfo) {
        if (elementName == null) {
            return;
        }

        String nameToCompare = elementName.substring(0, 1).toUpperCase() + elementName.substring(1);
        if (JNaming.isInJavaLang(nameToCompare)) {
            String err = "'" + nameToCompare
                      + "' conflicts with a class in java.lang.* and may cause a conflict during\n"
                      + " compilation. If you get this complaint during compilation, you need to\n"
                      + " use a mapping file or change the name of the schema element.";
            sInfo.getDialog().notify(err);
        }

        if (JNaming.isReservedByCastor(nameToCompare)) {
            String warn = "'" + nameToCompare + "' might conflict with a field name used"
                    + " by Castor.  If you get a complaint\nabout a duplicate name, you will"
                    + " need to use a mapping file or change\nthe name of the conflicting"
                    + " schema element.";
            sInfo.getDialog().notify(warn);
        }
    }

    /**
     * Updates the CDR (ClassDescriptorResolver) file with the
     * classname->descriptor mapping.
     *
     * @param jClass
     *            JClass instance describing the entity class
     * @param jDesc
     *            JClass instance describing is *Descriptor class
     * @param sInfo
     *            state info
     * @throws IOException
     *             If an already existing '.castor.cdr' file can not be found or loaded
     */
    private void updateCDRFile(final JClass jClass, final JClass jDesc, final SGStateInfo sInfo)
                                                      throws IOException {
        String entityFilename = jClass.getFilename(_destDir);
        File file = new File(entityFilename);
        File parentDirectory = file.getParentFile();
        File cdrFile = new File(parentDirectory, CDR_FILE);
        String cdrFilename = cdrFile.getAbsolutePath();

        Properties props = sInfo.getCDRFile(cdrFilename);

        if (props == null) {
            // check for existing .castor.xml file
            props = new Properties();
            if (cdrFile.exists()) {
                props.load(new FileInputStream(cdrFile));
            }
            sInfo.setCDRFile(cdrFilename, props);
        }
        props.setProperty(jClass.getName(), jDesc.getName());
    } //-- updateCDRFile

}
