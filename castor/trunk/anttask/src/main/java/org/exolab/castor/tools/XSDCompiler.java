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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: XSDCompiler.java 6543 2006-12-18 23:07:56Z wguttmn $
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
 * Ant task that enables code generation from an XML _schema from within Ant.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision: 6543 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 * @deprecated Please use {@link org.exolab.castor.tools.ant.taskdefs.CastorSourceGenTask} instead.
 */
public class XSDCompiler extends Task {

  /** Schema to use to generate code. */
  private File    _schema;
  /** Package to generate code into. */
  private String  _pkgName;
  /** Line seperator to use. */
  private String  _lineSep;
  /** If true, suppress non-fatal warnings. */
  private boolean _force;
  /** Custom type factory to supply to the code generator. */
  private String  _typeFactory;
  /** Directory into which to generate code. */
  private File    _destDir;

  /**
   * Creates a new XSDCompiler Task.
   */
  public XSDCompiler() {
    // No action needed
  } //-- XSDCompiler

  /**
   * Executes the task.
   * @throws BuildException if anything goes wrong during execution of the Ant task.
   */
  public void execute() throws BuildException {
    if (_schema == null || !_schema.exists()) {
      throw new BuildException("Schema file is required");
    }

    if (_lineSep != null) {
      if ("win".equals(_lineSep) || "\r\n".equals(_lineSep)) {
        project.log("Using Windows style line separation.", Project.MSG_VERBOSE);
        _lineSep = "\r\n";
      } else if ("unix".equals(_lineSep) || "\n".equals(_lineSep)) {
        project.log("Using UNIX style line separation.", Project.MSG_VERBOSE);
        _lineSep = "\n";
      } else if ("mac".equals(_lineSep) || "\r".equals(_lineSep)) {
        project.log("Using Macintosh style line separation.", Project.MSG_VERBOSE);
        _lineSep = "\r";
      } else {
        throw new BuildException("Invalid line-separator style.");
      }
    } else {
      _lineSep = "\n"; // default
    }

    SourceGenerator sgen = null;
    if (_typeFactory != null) {
      try {
        sgen = new SourceGenerator((FieldInfoFactory) Class.forName(_typeFactory).newInstance());
      } catch (Exception ex) {
        project.log("Type factory " + _typeFactory + " is invalid.", Project.MSG_INFO);
        throw new BuildException(ex);
      }
    } else {
      sgen = new SourceGenerator(); // default
    }

    sgen.setLineSeparator(_lineSep);
    sgen.setSuppressNonFatalWarnings(_force);
    sgen.setDestDir(_destDir.toString());
    if (_force) {
      project.log("Suppressing non fatal warnings.", Project.MSG_VERBOSE);
    }

    try {
      sgen.generateSource(_schema.getAbsolutePath(), _pkgName);
    } catch (IOException ex) {
      project.log("Failed to compile " + _schema, Project.MSG_INFO);
      throw new BuildException(ex);
    }
  } //-- execute

  /**
   * Set the schema file name.
   * @param schema The schema to be used for code generation.
   */
  public void setSchema(final String schema) {
    this._schema = project.resolveFile(schema);
  }

  /**
   * Set the target package name.
   * @param pkgName the target package name.
   */
  public void setPackage(final String pkgName) {
    this._pkgName = pkgName;
  }

  /**
   * Set the line separator.
   * @param lineSep the line seperator to use for this platform.
   */
  public void setLineseperator(final String lineSep) {
    this._lineSep = lineSep;
  }

  /**
   * Set overwriting existing files.
   * @param force if true, existing files will be silently overwritten and non-fatal
   * warnings will be ignored
   */
  public void setForce(final boolean force) {
    this._force = force;
  }

  /**
   * Set the type factory.
   * @param typeFactory name of the custom type factory class for collections.
   */
  public void setTypefactory(final String typeFactory) {
    this._typeFactory = typeFactory;
  }

  /**
   * Set the destination directory into which the Java sources
   * should be copied to.
   * @param dirName the name of the destination directory
   */
  public void setDestdir(final String dirName) {
    _destDir = project.resolveFile(dirName);
  } //-- setDestDir

} //-- XSDCompiler
