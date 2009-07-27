/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.jdo.util;

/**
 * Common static methods for Castor JDO related to class loading.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * @since 1.0 M2
 */
public final class ClassLoadingUtils {
    /**
     * Hide utility class constructor.
     */
    private ClassLoadingUtils() { }

    /**
     * Loads a class with a given name.
     * 
     * @param classLoader The class loader to use; null if the default class loader
     *        should be used.
     * @param classname Name of the class to be loaded.
     * @return The class loaded as specified by the class name.
     * @throws ClassNotFoundException If the given class can not be loaded.
     */
      public static Class loadClass(final ClassLoader classLoader, final String classname)
      throws ClassNotFoundException {
          Class classToLoad = null;
          if (classLoader == null) {
              classToLoad = Class.forName(classname);
          } else {
              classToLoad = classLoader.loadClass(classname);
          }
          return classToLoad;
      }
}
