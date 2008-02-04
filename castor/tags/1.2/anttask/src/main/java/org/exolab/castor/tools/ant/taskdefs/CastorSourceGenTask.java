/*
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
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: CastorSourceGenTask.java 6410 2006-11-17 12:06:24Z wguttmn $
 */
package org.exolab.castor.tools.ant.taskdefs;

import java.io.File;

import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.castor.anttask.CastorCodeGenTask;

/**
 * An <a href="http://ant.apache.org/">Ant</a> task to call the Castor
 * Source Generator. It can be passed a file, a directory, a Fileset or all
 * three.
 *
 * @author <a href="mailto:joel DOT farquhar AT montage-dmc DOT com">Joel Farquhar</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @version $Revision: 6543 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 * @deprecated Please use {@link org.castor.anttask.CastorCodeGenTask} instead.
 */
public final class CastorSourceGenTask extends MatchingTask {
    //--------------------------------------------------------------------------

    /** CastorCodeGenTask to delegate all work to. */
    private final CastorCodeGenTask _codeGen = new CastorCodeGenTask();

    //--------------------------------------------------------------------------

    /**
     * No-arg constructor.
     */
    public CastorSourceGenTask() { }

    //--------------------------------------------------------------------------

    /**
     * Sets the individual schema that will have code generated for it.
     * 
     * @param file One schema file.
     */
    public void setFile(final File file) {
        _codeGen.setFile(file);
    }

    /**
     * Sets the directory such that all schemas in this directory will have code
     * generated for them.
     * 
     * @param dir The directory containing schemas to process.
     */
    public void setDir(final File dir) {
        _codeGen.setDir(dir);
    }

    /**
     * Adds a fileset to process that contains schemas to process.
     * 
     * @param set An individual file set containing schemas.
     */
    public void addFileset(final FileSet set) {
        _codeGen.addFileset(set);
    }

    /**
     * Sets the package that generated code will belong to.
     * 
     * @param pack The package that generated code will belong to.
     */
    public void setPackage(final String pack) {
        _codeGen.setPackage(pack);
    }

    /**
     * Sets the directory into which code will be generated.
     * 
     * @param dest The directory into which code will be generated.
     */
    public void setTodir(final String dest) {
        _codeGen.setTodir(dest);
    }

    /**
     * Sets the binding file to be used for code generation.
     * 
     * @param bindingfile The binding file to be used for code generation.
     */
    public void setBindingfile(final String bindingfile) {
        _codeGen.setBindingfile(bindingfile);
    }

    /**
     * Sets the line seperator to use for code generation.
     * 
     * @param ls The line seperator to use for code generation.
     */
    public void setLineseparator(final String ls) {
        _codeGen.setLineseparator(ls);
    }

    /**
     * Sets the type factory for code generation.
     * 
     * @param tf The type factory to use for code generation.
     */
    public void setTypes(final String tf) {
        _codeGen.setTypes(tf);
    }

    /**
     * Sets whether or not code generation gives extra information about its work.
     * 
     * @param b If true, the code generator will be verbose.
     */
    public void setVerbose(final boolean b) {
        _codeGen.setVerbose(b);
    }

    /**
     * Sets whether or not non-fatal warnings should be suppressed.
     * 
     * @param b If true, non-fatal warnings will be suppressed. This additionally
     *        means that existing source files will be silently overwritten.
     */
    public void setWarnings(final boolean b) {
        _codeGen.setWarnings(b);
    }

    /**
     * Sets whether or not class descriptors are generated.
     * 
     * @param b If true, class descriptors are generated.
     */
    public void setNodesc(final boolean b) {
        _codeGen.setNodesc(b);
    }

    /**
     * Sets whether or not marshaling methods are generated.
     * 
     * @param b If true, marshaling methods are generated.
     * @deprecated For the correct spelling, see {@link #setNomarshal(boolean)}.
     */
    public void setNomarshall(final boolean b) {
        _codeGen.setNomarshal(b);
    }

    /**
     * Sets whether or not marshaling methods are generated.
     * 
     * @param b If true, marshaling methods are generated.
     */
    public void setNomarshal(final boolean b) {
        _codeGen.setNomarshal(b);
    }

    /**
     * Sets whether CTF framework code is generated.
     * 
     * @param b If true, the generated code will be instrumented for the CTF.
     */
    public void setTestable(final boolean b) {
        _codeGen.setTestable(b);
    }

    /**
     * Controls whether to generate code for imported schemas as well.
     * 
     * @param generateImportedSchemas True if code should be generated for imported schemas.
     */
    public void setGenerateImportedSchemas(final boolean generateImportedSchemas) {
        _codeGen.setGenerateImportedSchemas(generateImportedSchemas);
    }

    /**
     * Controls whether to generate SAX-1 compliant code.
     * 
     * @param sax1 True if SAX-1 compliant code should be generated.
     */
    public void setSAX1(final boolean sax1) {
        _codeGen.setSAX1(sax1);
    }

    /**
     * Controls whether enumerated type lookup should be performed in a case
     * insensitive manner.
     * 
     * @param caseInsensitive True if enumerated type lookup should be performed in a case
     *        insensitive manner
     */
    public void setCaseInsensitive(final boolean caseInsensitive) {
        _codeGen.setCaseInsensitive(caseInsensitive);
    }

    /**
     * Sets the file to use for castor builder properties.
     *
     * @param properties The properties to use.
     */
    public void setProperties(final String properties) {
        _codeGen.setProperties(properties);
    }

    //--------------------------------------------------------------------------

    /**
     * Public execute method -- entry point for the Ant task.  Loops over all
     * schema that need code generated and creates needed code generators, then
     * executes them. If anything goes wrong during execution of the Ant task a
     * BuildException will be thrown.
     * 
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() {
        System.out.println("The class org.exolab.castor.tools.ant.taskdefs.CastorSourceGenTask "
                + "has been deprecated. Please use org.castor.anttask.CastorCodeGenTask instead.");
        System.out.println();
        
        _codeGen.execute();
    }


    //--------------------------------------------------------------------------
}
