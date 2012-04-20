/*
 * Copyright 2005-2008 Werner Guttmann
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
package org.exolab.castor.builder.printing;

/**
 * Factory class to create {@link JClassPrinter} instances.
 * 
 * @since 1.2.1
 */
public interface JClassPrinterFactory {

    /**
     * Returns a short name for this {@link JClassPrinterFactory} instance.
     * @return a short name (used for setting the JClassPrinter type on the {@link SourceGenerator}.
     * 
     * @see SourceGenerator#setJClassPrinterType(String)
     */
    String getName();
    
    /**
     * Returns the {@link JClassPrinter} instance as identified by this {@link JClassPrinterFactory}
     * instance.
     * @return Returns the actual {@link JClassPrinter} instance 
     */
    JClassPrinter getJClassPrinter();
    
}
