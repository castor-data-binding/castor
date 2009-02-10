/*
 * Copyright 2008 Werner Guttmann
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

import java.io.File;
import java.io.FileWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.exolab.javasource.JClass;

/**
 * Prints the given JClass to the filesystem using velocity templates.
 * 
 * @since 1.2
 */
public class TemplateJClassPrinter implements JClassPrinter {

    /**
     * The package that contains the velocity templates.
     */
    public static final String TEMPLATE_PACKAGE = "/org/exolab/castor/builder/printing/templates/";

    /**
     * Indicates whether Velocity has been already initialized.
     */
    private boolean _initialized = false;

    /**
     * Initialises the Velocity engine.
     */
    private void initializeVelocity() {
        // init velocity
        Velocity.setProperty("velocimacro.permissions.allowInline", "true");
        Velocity.setProperty("velocimacro.library",
                "/org/exolab/castor/builder/printing/templates/library.vm");
        Velocity.setProperty("resource.loader", "classPathResource");
        Velocity
                .setProperty("classPathResource.resource.loader.class",
                        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception e) {
            System.out.println("init fails!");
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.builder.printing.JClassPrinter#printClass(
     *      org.exolab.javasource.JClass, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public void printClass(final JClass jClass, final String outputDir,
            final String lineSeparator, final String header) {

        if (!_initialized) {
            initializeVelocity();
            _initialized = true;
        }

        try {

            // provide objects
            VelocityContext context = new VelocityContext();
            context.put("jClass", jClass);
            context.put("helper", new TemplateHelper());

            // print the class
            Template template = 
                Velocity.getTemplate(TEMPLATE_PACKAGE + "main.vm");
            FileWriter fileWriter = 
                new FileWriter(new File(jClass.getFilename(outputDir)));
            template.merge(context, fileWriter);
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
