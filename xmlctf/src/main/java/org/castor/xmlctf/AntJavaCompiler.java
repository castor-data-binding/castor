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
package org.castor.xmlctf;

import java.io.File;
import java.util.HashSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

/**
 * Compiles a directory tree, recursively. This class is built around the use of
 * the ANT JAVAC task.
 */
public class AntJavaCompiler implements Compiler {
    private static final HashSet IGNORE_DIRS = new HashSet();

    static {
        IGNORE_DIRS.add(FileServices.CVS);
        IGNORE_DIRS.add(FileServices.SVN);
        IGNORE_DIRS.add("org");
        IGNORE_DIRS.add("com");
        IGNORE_DIRS.add("net");
    }

    private final File  _baseDirectory;
    private final File  _outputDirectory;

    private String _javaVersion = null;
    private Javac _compiler;

    /**
     * Creates a compiler for a given directory.
     *
     * @param baseDirectory The directory that holds the files to be compiled.
     */
    public AntJavaCompiler(final File baseDirectory) {
        if (baseDirectory == null) {
            throw new IllegalArgumentException("'baseDirectory' must not be null.");
        }
        _baseDirectory = baseDirectory;
        _outputDirectory = baseDirectory;
    }

    public void setJavaSourceVersion(float javaSourceVersion) {
        if (javaSourceVersion >= 5F && javaSourceVersion < 10F) {
            javaSourceVersion = 1.0F + (javaSourceVersion / 10F);
        }
        _javaVersion = "" + javaSourceVersion;
    }

    /**
     * Compiles the content of a directory. Throws a <code>CompilationException</code>
     * if the build fails.
     */
    public void compileDirectory() {
        _compiler = makeCompiler(_baseDirectory, _outputDirectory);
        compileDirectory(_baseDirectory, _outputDirectory);
    }

    /**
     * Creates and returns a Ant Javac compiler.
     *
     * @return Ant Javac compiler
     */
    private Javac makeCompiler(final File srcDir, final File destDir) {
        Project project = new Project();
        project.init();
        project.setBasedir(srcDir.getAbsolutePath());

        Javac compiler = new Javac();
        compiler.setProject(project);
        compiler.setDestdir(destDir.getAbsoluteFile());
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
        if (_javaVersion != null) {
            compiler.setSource(_javaVersion);
        }
        Path classpath = compiler.createClasspath();
        classpath.setPath(System.getProperty("java.class.path"));
        classpath.add(new Path(project, destDir.getAbsolutePath()));
        compiler.setClasspath(classpath);
        return compiler;
    }

    /**
     * Compiles a directory tree. Throws a <code>CompilationException</code> if build
     * fails.
     *
     * @param srcDir Source directory holding the files to be compiled.
     * @param destDir Destination directory to put the compiled classes in.
     */
    private void compileDirectory(final File srcDir, final File destDir) {
        File[] entries = srcDir.listFiles();
        for (int i = 0; i < entries.length; i++) {
            File entry = entries[i];
            if (entry.isDirectory() && !IGNORE_DIRS.contains(entry.getName())) {
                compileDirectory(entry, destDir);
            }
        }
        entries = null;

        try {
            Path srcPath = _compiler.createSrc();
            srcPath.setLocation(destDir);
            _compiler.setSrcdir(srcPath);
            _compiler.execute();
        } catch (BuildException ex) {
            throw new CompilationException("Problem compiling directory " + srcDir, ex);
        }
    }

}
