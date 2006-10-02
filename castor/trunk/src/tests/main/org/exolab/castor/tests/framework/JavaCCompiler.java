/*
 * Copyright 2005 Edward Kuns
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
package org.exolab.castor.tests.framework;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

/**
 * Compiles a directory tree, recursively. This class is built around the use of
 * the ANT JAVAC task.
 */
public class JavaCCompiler implements Compiler {

    private final File  _baseDirectory;
    private final File  _outputDirectory;
    private final Javac _compiler;

    /**
     * Creates a compiler for a given directory.
     * @param baseDirectory The directory that holds the files to be compiled.
     */
    public JavaCCompiler(final File baseDirectory) {
        if (baseDirectory == null) {
            throw new IllegalArgumentException("'baseDirectory' must not be null.");
        }
        _baseDirectory   = baseDirectory;
        _outputDirectory = baseDirectory;
        _compiler        = makeCompiler();
    }

    /**
     * Creates a compiler for a given directory, specifying a destination directory as well.
     * @param baseDirectory The directory that holds the files to be compiled.
     * @param destDir The destination directory. 
     */
    public JavaCCompiler(final File baseDirectory, final File destDir) {
        if (baseDirectory == null) {
            throw new IllegalArgumentException("'baseDirectory' must not be null.");
        }
        if (destDir == null) {
            throw new IllegalArgumentException("'destDir' must not be null.");
        }
        _baseDirectory   = baseDirectory;
        _outputDirectory = destDir;
        _compiler        = makeCompiler();
    }

    /**
     * Compiles the content of a directory.
     * @throws CompilationException If the build fails.
     */
    public void compileDirectory() throws CompilationException {
        try {
            compileDirectory(_baseDirectory, _outputDirectory);
        } 
        catch (BuildException e) {
            throw new CompilationException ("Problem compiling directory " + _baseDirectory, e);
        }
    } //-- compileDirectory

    /**
     * Creates and returns a Ant Javac compiler.
     * @return a Ant Javac compiler
     */
    private Javac makeCompiler() {
        Project project = new Project();
        project.init();
        project.setBasedir(_baseDirectory.getAbsolutePath());

        Javac compiler = new Javac();
        compiler.setProject(project);
        compiler.setDestdir(_outputDirectory.getAbsoluteFile());
        compiler.setOptimize(false);
        compiler.setDebug(true);
        compiler.setDebugLevel("lines,vars,source");
        compiler.setIncludejavaruntime(true);
        if (XMLTestCase._verbose) {
            compiler.setListfiles(true);
            compiler.setVerbose(true);
        } else {
            compiler.setNowarn(true);
        }

        Path classpath = compiler.createClasspath();
        classpath.setPath(System.getProperty("java.class.path"));
        classpath.add(new Path(project, _outputDirectory.getAbsolutePath()));
        compiler.setClasspath(classpath);
        return compiler;
    }

    /**
     * Compiles a directory tree.
     * @param srcDir Source directory (holding the files to be compiled).
     * @param root
     * @throws BuildException If the build failes.
     */
    private void compileDirectory(File srcDir, File root) throws BuildException {
        File[] entries = srcDir.listFiles();
        for (int i=0; i<entries.length; i++) {
            File entry = entries[i];
            if (entry.isDirectory() && !entry.getName().endsWith("CVS")
                    && !entry.getName().equals(".svn")
                    && !entry.getName().equals("org")
                    && !entry.getName().equals("com")
                    && !entry.getName().equals("net")) {
                compileDirectory(entry, root);
            }
        }
        entries = null;

        Path sourcepath = _compiler.createSrc();
        sourcepath.setLocation(root);
        _compiler.setSrcdir(sourcepath);
        _compiler.execute();
    }

}
