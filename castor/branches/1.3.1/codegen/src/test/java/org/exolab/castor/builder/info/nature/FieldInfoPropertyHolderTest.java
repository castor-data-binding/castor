/*
 * Copyright 2008 Lukas Lang
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
 *
 */
package org.exolab.castor.builder.info.nature;

import org.exolab.castor.builder.factory.FieldInfoFactory;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.javasource.JClass;

/**
 * Test the Implementation of PropertyHolder for ClassInfo.
 * 
 * @author Tobias Hochwallner
 * @since 1.2.1
 */
public final class FieldInfoPropertyHolderTest extends BasePropertyHolderTest {

    /**
     * SetUp sets the PropertyHolder to test.
     */
    protected void setUp() throws Exception {
        FieldInfoFactory factory = new FieldInfoFactory();
        _holder = factory.createFieldInfo(
                new XSClass(new JClass("Book")), "isbn"); 
    }

}
