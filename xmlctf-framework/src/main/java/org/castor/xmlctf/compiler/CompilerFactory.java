/*
 * Copyright 2015 Werner Guttmann
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
package org.castor.xmlctf.compiler;

import java.io.File;

/**
 * A factory for obtaining the correct {@link Compiler} instance, based on the version
 * of the JVM executing the XMLCTF tests.
 */
public class CompilerFactory {
	
	private static final String JAVA_SPECIFICATION_VERSION = "java.specification.version";

	/**
	 * Factory method for obtaining a {@link Compiler} instance.
	 * @param directory The source directory holding compilation units.
	 * @return A {@link Compiler} instance
	 */
	public static Compiler createInstance(File directory) {
		float javaVersion = Float.parseFloat(System.getProperty(JAVA_SPECIFICATION_VERSION));
		
		if (javaVersion >= 1.6) {
			return new OracleJavaCompiler(directory);
		}
		return new SunJavaCompiler(directory);
	}

}
