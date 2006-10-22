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


package org.exolab.castor.tools;


import java.io.IOException;
import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.builder.FieldInfoFactory;


/**
 * A Task to process via XSLT a set of XML documents. This is
 * useful for building views of XML based documentation.
 * arguments:
 * <ul>
 * <li>basedir
 * <li>destdir
 * <li>style
 * <li>includes
 * <li>excludes
 * </ul>
 * Of these arguments, the <b>sourcedir</b> and <b>destdir</b> are required.
 * <p>
 * This task will recursively scan the sourcedir and destdir
 * looking for XML documents to process via XSLT. Any other files,
 * such as images, or html files in the source directory will be 
 * copied into the destination directory.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 **/
public class XSDCompiler
    extends Task
{


    private File schema;


    private String pkgName;


    private String lineSep;


    private boolean force;


    private String  typeFactory;
 

    private File    destDir;

    
    /**
     * Creates a new XSDCompiler Task. 
     **/
    public XSDCompiler()
    {
    } //-- XSDCompiler
    

    /**
     * Executes the task.
     **/
    public void execute()
        throws BuildException
    {
        if (schema == null || !schema.exists())
            throw new BuildException("Schema file is required");

        if (lineSep != null) {
            if ("win".equals(lineSep) || "\r\n".equals(lineSep)) {
                project.log("Using Windows style line separation.",Project.MSG_VERBOSE);
                lineSep = "\r\n";
            } else if ("unix".equals(lineSep) || "\n".equals(lineSep)) {
                project.log("Using UNIX style line separation.",Project.MSG_VERBOSE);
                lineSep = "\n";
            } else if ("mac".equals(lineSep) || "\r".equals(lineSep)) {
                project.log("Using Macintosh style line separation.",Project.MSG_VERBOSE);
                lineSep = "\r";
            } else
                throw new BuildException("Invalid line-separator style.");
        } else
            lineSep = "\n"; // default

        SourceGenerator sgen = null;
        if (typeFactory != null) {
            try {
                sgen = new SourceGenerator((FieldInfoFactory)Class.forName(typeFactory).newInstance());
            } catch(Exception ex) {
                project.log("Type factory "+typeFactory+" is invalid.",Project.MSG_INFO);
                throw new BuildException(ex);
            }
        } else {
            sgen = new SourceGenerator(); // default
        }
        
        sgen.setLineSeparator(lineSep);
        sgen.setSuppressNonFatalWarnings(force);
        sgen.setDestDir(destDir.toString());
        if (force)
            project.log("Suppressing non fatal warnings.",Project.MSG_VERBOSE);
        
        try {
            sgen.generateSource(schema.getAbsolutePath(), pkgName);
        } catch(IOException ex) {
	    project.log("Failed to compile " + schema,Project.MSG_INFO);
            throw new BuildException(ex);
        }
    } //-- execute
    


    /**
     * Set the schema file name.
     **/
    public void setSchema(String schema)
    {
        this.schema = project.resolveFile(schema);
    }


    /**
     * Set the target package name.
     **/
    public void setPackage(String pkgName)
    {
        this.pkgName = pkgName;
    }


    /**
     * Set the line separator.
     **/
    public void setLineseperator(String lineSep)
    {
        this.lineSep = lineSep;
    }


    /**
     * Set overwriting existing files.
     **/
    public void setForce(boolean force)
    {
        this.force = force;
    }


    /**
     * Set the type factory.
     **/
    public void setTypefactory(String typeFactory)
    {
        this.typeFactory = typeFactory;
    }


    /**
     * Set the destination directory into which the Java sources
     * should be copied to
     * @param dirName the name of the destination directory
     **/
    public void setDestdir(String dirName)
    {
	    destDir = project.resolveFile(dirName);
    } //-- setDestDir

    
} //-- XSDCompiler

