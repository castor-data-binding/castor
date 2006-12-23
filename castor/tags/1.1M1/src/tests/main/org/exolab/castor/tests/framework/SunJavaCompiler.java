/*
 * Copyright 2006 Ralf Jaochim
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Compiles a directory tree, recursively. This class is built to use the Sun Javac
 * compiler contained in tools.jar. A IllegalStateException will be thrown if tools.jar
 * is not on the classpath at construction of the class and execution of the
 * compileDirectory() method.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0.5
 */
public class SunJavaCompiler implements Compiler {
    private static final String COMPILE_CLASSNAME = "com.sun.tools.javac.Main";
    private static final String COMPILE_METHODNAME = "compile";
    private static final Class[] COMPILE_PARAMTYPE = new Class[] {String[].class};

    private static final HashSet IGNORE_DIRS = new HashSet();

    private static Method _compileMethod = null;
    private static boolean _initialized = false;
    /** Java version of the JVM we are running in. */
    private static final float JAVA_VERSION = Float.parseFloat(System.getProperty("java.specification.version"));

    private String _javaVersion = null;

    private final File _baseDirectory;
    private final File _outputDirectory;

    /**
     * Creates a compiler for a given directory.
     * @param baseDirectory The directory that holds the files to be compiled.
     */
    public SunJavaCompiler(final File baseDirectory) {
        if (baseDirectory == null) {
            throw new IllegalArgumentException("'baseDirectory' must not be null.");
        }
        _baseDirectory   = baseDirectory;
        _outputDirectory = baseDirectory;

        if (!_initialized) { initialize(); }
    }

    public void setJavaSourceVersion(float javaSourceVersion) {
        if (javaSourceVersion >= 5F && javaSourceVersion < 10F) {
            javaSourceVersion = 1.0F + (javaSourceVersion / 10F);
        }
        _javaVersion = "" + javaSourceVersion;
    }

    private void initialize() {
        IGNORE_DIRS.add(FileServices.CVS);
        IGNORE_DIRS.add(FileServices.SVN);

        try {
            ClassLoader loader = this.getClass().getClassLoader();
            Class cls = loader.loadClass(COMPILE_CLASSNAME);
            _compileMethod = cls.getMethod(COMPILE_METHODNAME, COMPILE_PARAMTYPE);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to find compile method.");
        }

        _initialized = true;
    }

    /**
     * Compiles the content of a directory. Throws a <code>CompilationException</code>
     * if the build fails.
     */
    public void compileDirectory() {
        List filesList = findSourceFiles(_baseDirectory);
        if (filesList.size() > 0) {
            filesList.addAll(0, getCompileArguments(_baseDirectory, _outputDirectory));

            String[] args = new String[filesList.size()];
            args = (String[]) filesList.toArray(args);

            int status;
            try {
                Object result = _compileMethod.invoke(null, new Object[] {args});
                status = ((Integer) result).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to call compile method.");
            }

            switch (status) {
            case 0: break;
            case 1: throw new CompilationException("Compile status: ERROR");
            case 2: throw new CompilationException("Compile status: CMDERR");
            case 3: throw new CompilationException("Compile status: SYSERR");
            case 4: throw new CompilationException("Compile status: ABNORMAL");
            default: throw new CompilationException("Compile status: Unknown");
            }
        } else {
            throw new CompilationException("No files to compile: " + _baseDirectory);
        }
    }

    private List getCompileArguments(final File srcDir, final File destDir) {
        List args = new ArrayList();

        args.add("-g");
        if (JAVA_VERSION == 1.5F) {
            args.add("-Xlint:unchecked");
        }
        if (XMLTestCase._verbose) {
            args.add("-verbose");
        } else {
            args.add("-nowarn");
            args.add("-Xmaxwarns");
            args.add("0");
            args.add("-Xmaxerrs");
            args.add("5");
        }
        if (_javaVersion != null) {
            args.add("-source");
            args.add(_javaVersion);
        }
        args.add("-classpath");
        args.add(System.getProperty("java.class.path") + ";" + destDir.getAbsolutePath());
        args.add("-d");
        args.add(destDir.getAbsolutePath());
        args.add("-sourcepath");
        args.add(srcDir.getAbsolutePath());

        return args;
    }

    private List findSourceFiles(final File srcDir) {
        List files = new ArrayList();

        File[] entries = srcDir.listFiles();
        for (int i = 0; i < entries.length; i++) {
            File entry = entries[i];
            if (entry.getName().endsWith(".java")) {
                files.add(entry.getAbsolutePath());
            } else if (entry.isDirectory() && !IGNORE_DIRS.contains(entry.getName())) {
                files.addAll(findSourceFiles(entry));
            }
        }

        return files;
    }

}
