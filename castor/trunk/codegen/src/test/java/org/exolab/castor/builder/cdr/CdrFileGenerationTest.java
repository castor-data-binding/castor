/*
 * Copyright 2008 Sebastian Gabmeyer
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
package org.exolab.castor.builder.cdr;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.exolab.castor.builder.SourceGenerator;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * Test case checking the correct implementation of CDR file generation
 * for both XML and JDO.
 *  
 * @author Sebastian Gabmeyer
 * @since 1.2.1
 * 
 */
public class CdrFileGenerationTest extends TestCase {

    private SourceGenerator _generator;
    private String _xmlSchema;
    private String _cdrDirectoryName;
    private String _destDir = "./codegen/src/test/java";

    public final void setUp() throws Exception {
        super.setUp();
        _generator = new SourceGenerator();
        _generator.setDestDir(_destDir);
        _generator.setSuppressNonFatalWarnings(true);
        _generator.setJdoDescriptorCreation(true);
    }

    public final void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testJDOCDRFileGeneration() throws Exception {
        _xmlSchema = getClass().getResource("simple.xsd").toExternalForm();
        InputSource inputSource = new InputSource(_xmlSchema);
        String pkgName = getClass().getPackage().getName()
                + ".generated.simple";
        _cdrDirectoryName = pkgName.replace('.', File.separatorChar);
        _generator.generateSource(inputSource, pkgName);
        File cdrFile = new File((new File(_destDir, _cdrDirectoryName))
                .getPath(), ".castor.jdo.cdr");

        assertTrue(cdrFile.exists());

        Properties props = new Properties();
        props.load(new FileInputStream(cdrFile));
        
        String fatherDescrName = props
                .getProperty("org.exolab.castor.builder.cdr.generated.simple.Father");
        assertEquals(
                "org.exolab.castor.builder.cdr.generated.simple.jdo_descriptors.FatherJDODescriptor",
                fatherDescrName);
        
        // this check could be removed as it belongs to the scope of the JDOClassDescriptorFactory
        File fatherDescrFile = new File(_destDir, 
                fatherDescrName.replace('.', File.separatorChar) + ".java");
        
        assertTrue(fatherDescrFile.exists());
    }

    public final void testNoJDOCDRFileGeneration() throws Exception {
        _xmlSchema = getClass().getResource("schema-entity-non-jdo.xsd")
                .toExternalForm();
        InputSource inputSource = new InputSource(_xmlSchema);
        String pkgName = getClass().getPackage().getName()
                + ".generated.nonjdo";
        _cdrDirectoryName = pkgName.replace('.', File.separatorChar);
        _generator.generateSource(inputSource, pkgName);
        File cdrFile = new File((new File(_destDir, _cdrDirectoryName))
                .getPath(), ".jdo.castor.cdr");

        assertFalse(cdrFile.exists());
    }
}
