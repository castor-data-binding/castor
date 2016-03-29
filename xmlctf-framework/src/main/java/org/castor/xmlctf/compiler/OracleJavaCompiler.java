/*
 * Copyright 2006 Ralf Jaochim
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.xmlctf.compiler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.castor.xmlctf.XMLTestCase;
import org.castor.xmlctf.util.FileServices;

/**
 * Compiles a directory tree, recursively. This class is built to use the Oracle tool JavaCompiler
 * contained in tools.jar. A IllegalStateException will be thrown if tools.jar is not on the
 * classpath at construction of the class and execution of the compileDirectory() method.
 *
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3.4
 */
public class OracleJavaCompiler implements Compiler {

  private static final Set<String> IGNORE_DIRS = new HashSet<String>();

  /** Java version of the JVM we are running in. */
  private static final float JAVA_VERSION =
      Float.parseFloat(System.getProperty("java.specification.version"));

  private String _javaVersion = null;

  private final File _baseDirectory;
  private final File _outputDirectory;

  static {
    IGNORE_DIRS.add(FileServices.CVS);
    IGNORE_DIRS.add(FileServices.SVN);
    IGNORE_DIRS.add(FileServices.GIT);
  }

  /**
   * Creates a compiler for a given directory.
   * 
   * @param baseDirectory The directory that holds the files to be compiled.
   */
  public OracleJavaCompiler(File baseDirectory) {
    if (baseDirectory == null) {
      throw new IllegalArgumentException("'baseDirectory' must not be null.");
    }
    _baseDirectory = baseDirectory;
    _outputDirectory = baseDirectory;
  }

  /**
   * Sets the Java source version the current test will be using.
   * 
   * @param javaSourceVersion The Java Source version to be used.
   */
  public void setJavaSourceVersion(float javaSourceVersion) {
    float srcVersion = javaSourceVersion;
    if (javaSourceVersion >= 5F && javaSourceVersion < 10F) {
      srcVersion = 1.0F + (javaSourceVersion / 10F);
    }
    _javaVersion = "" + srcVersion;
  }

  /**
   * Compiles the content of a directory. Throws a <code>CompilationException</code> if the build
   * fails.
   */
  public void compileDirectory() {
    List<File> filesList = findSourceFiles(_baseDirectory);
    if (!filesList.isEmpty()) {
      // filesList.addAll(0, getCompileArguments(_baseDirectory,
      // _outputDirectory));

      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
      // TODO: file list - do not convert to Strings
      Iterable<? extends JavaFileObject> compilationUnits =
          fileManager.getJavaFileObjectsFromFiles(filesList);
      Iterable<String> compileOptions = getCompileArguments(_baseDirectory, _outputDirectory);
      // Iterable<String> compileOptions = Arrays.asList("-g", "-d",
      // "-cp", CLASSPATH);
      Writer logger = new StringWriter();
      JavaCompiler.CompilationTask task =
          compiler.getTask(logger, fileManager, null, compileOptions, null, compilationUnits);
      boolean ok = task.call();
      try {
        fileManager.close();
      } catch (IOException ioe) {
        // TODO
        ioe.printStackTrace(System.err);
      }

      System.out.println(logger.toString());

      if (ok == false) {
        throw new IllegalStateException("Failed to call compile method.");
      }

    } else {
      throw new CompilationException("No files to compile: " + _baseDirectory);
    }
  }

  /**
   * Returns a list of arguments for the compiler.
   * 
   * @param srcDir The source directory for compilation
   * @param destDir The destination directory for compilation
   * @return a list of arguments for the compiler.
   */
  private List<String> getCompileArguments(final File srcDir, final File destDir) {
    List<String> args = new ArrayList<String>();

    args.add("-g");
    if (JAVA_VERSION == 1.5F || JAVA_VERSION == 1.6F || JAVA_VERSION == 1.7F) {
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
    String classPathOverriden = System.getProperty("xmlctf.classpath.override");
    if (classPathOverriden != null) {
      args.add(classPathOverriden + ";" + destDir.getAbsolutePath());
    } else {
      args.add(System.getProperty("java.class.path") + ";" + destDir.getAbsolutePath());
    }
    args.add("-d");
    args.add(destDir.getAbsolutePath());
    args.add("-sourcepath");
    args.add(srcDir.getAbsolutePath());
    args.add("-g");
    return args;
  }

  /**
   * Recursively searches the provided directory, returning a list of all Java files found.
   * 
   * @param sourceDirectory A directory to search for Java files.
   * @return a List of all Java files found in the provided directory
   */
  private List<File> findSourceFiles(File sourceDirectory) {
    List<File> files = new ArrayList<File>();

    File[] entries = sourceDirectory.listFiles();
    for (int i = 0; i < entries.length; i++) {
      File entry = entries[i];
      if (entry.getName().endsWith(".java")) {
        files.add(entry);
      } else if (entry.isDirectory() && !IGNORE_DIRS.contains(entry.getName())) {
        files.addAll(findSourceFiles(entry));
      }
    }

    return files;
  }

}
