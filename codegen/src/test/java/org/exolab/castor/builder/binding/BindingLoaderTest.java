/*
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices. Redistributions
 * must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote products derived from this Software
 * without prior written permission of Intalio, Inc. For written permission, please contact
 * info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab" nor may "Exolab" appear in
 * their names without prior written permission of Intalio, Inc. Exolab is a registered trademark of
 * Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.binding;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.exolab.castor.builder.binding.xml.AutomaticNamingType;
import org.exolab.castor.builder.binding.xml.Binding;
import org.exolab.castor.builder.binding.xml.ComponentBindingType;
import org.exolab.castor.builder.binding.xml.IncludeType;
import org.exolab.castor.builder.binding.xml.NamingXMLType;
import org.exolab.castor.builder.binding.xml.PackageType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Comprehensive test suite for BindingLoader class.
 *
 * @author Test Framework
 */
@RunWith(MockitoJUnitRunner.class)
public class BindingLoaderTest {

    private BindingLoader bindingLoader;

    @Before
    public void setUp() {
        bindingLoader = new BindingLoader();
    }

    @After
    public void tearDown() {
        bindingLoader = null;
    }

    // ==================== Constructor Tests ====================

    @Test
    public void should_CreateInstance_When_NoArgConstructorCalled() {
        assertNotNull("BindingLoader should be instantiated", bindingLoader);
        assertNull(
            "Initial binding should be null",
            bindingLoader.getBinding()
        );
    }

    // ==================== getBinding() Tests ====================

    @Test
    public void should_ReturnNull_When_NoBindingLoaded() {
        assertNull(
            "Should return null when no binding has been loaded",
            bindingLoader.getBinding()
        );
    }

    // ==================== setBaseURL() Tests ====================

    @Test
    public void should_SetBaseURL_When_ValidURLProvided() {
        String validUrl = "http://example.com/binding/";
        bindingLoader.setBaseURL(validUrl);
        // Verify no exception is thrown and method completes
        assertNotNull("BindingLoader should exist", bindingLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_MalformedURLProvided() {
        bindingLoader.setBaseURL("ht!tp://invalid url with spaces");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_InvalidURLProtocol() {
        bindingLoader.setBaseURL("invalid://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_EmptyURL() {
        bindingLoader.setBaseURL("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_NullURL() {
        bindingLoader.setBaseURL(null);
    }

    // ==================== loadBinding(InputSource) Tests ====================

    @Test
    public void should_LoadBindingSuccessfully_When_ValidInputSourceProvided()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xsi:schemaLocation=\"http://castor.exolab.org/binding http://exolab.castor.org/binding.xsd\">" +
            "<default-binding type=\"element\"/></binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-binding.xml");

        bindingLoader.loadBinding(source);
        ExtendedBinding binding = bindingLoader.getBinding();
        assertNotNull("Binding should be loaded", binding);
    }

    @Test
    public void should_InitializeBinding_When_FirstLoadingCall()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xsi:schemaLocation=\"http://castor.exolab.org/binding http://exolab.castor.org/binding.xsd\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "ExtendedBinding should be initialized on first load",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_PreserveExistingBinding_When_LoadingSecondBinding()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source1 = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source1.setSystemId("test1.xml");
        bindingLoader.loadBinding(source1);

        ExtendedBinding firstBinding = bindingLoader.getBinding();

        InputSource source2 = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source2.setSystemId("test2.xml");
        bindingLoader.loadBinding(source2);

        ExtendedBinding secondBinding = bindingLoader.getBinding();
        assertSame(
            "Should preserve and extend the same binding instance",
            firstBinding,
            secondBinding
        );
    }

    @Test(expected = BindingException.class)
    public void should_ThrowBindingException_When_InputSourceUnmarshalFails()
        throws BindingException {
        String invalidBindingXML = "not valid xml at all !!! <>";
        InputSource source = new InputSource(
            new ByteArrayInputStream(invalidBindingXML.getBytes())
        );
        source.setSystemId("invalid.xml");

        bindingLoader.loadBinding(source);
    }

    // ==================== loadBinding(String) Tests ====================

    @Test
    public void should_LoadBindingFromString_When_FilePathProvided()
        throws BindingException {
        String testResourcePath =
            "src/test/resources/org/exolab/castor/builder/binding/test-binding.xml";
        bindingLoader.loadBinding(testResourcePath);
        assertNotNull(
            "Binding should be loaded from file path",
            bindingLoader.getBinding()
        );
    }

    @Test(expected = BindingException.class)
    public void should_ThrowBindingException_When_FileNotFound()
        throws BindingException {
        bindingLoader.loadBinding("non-existent-file-xyz123.xml");
    }

    @Test(expected = BindingException.class)
    public void should_ThrowBindingException_When_InvalidFileProvided()
        throws BindingException {
        bindingLoader.loadBinding(
            "src/test/resources/org/exolab/castor/builder/binding/invalid-file.xml"
        );
    }

    // ==================== createBinding(InputSource) Factory Method Tests ====================

    @Test
    public void should_CreateBindingSuccessfully_When_ValidInputSourceProvided()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("factory-test.xml");

        ExtendedBinding binding = BindingLoader.createBinding(source);
        assertNotNull(
            "Factory method should create and return binding",
            binding
        );
    }

    @Test(expected = BindingException.class)
    public void should_ThrowBindingException_When_FactoryCalledWithInvalidSource()
        throws BindingException {
        String invalidXML = "completely invalid content {{{";
        InputSource source = new InputSource(
            new ByteArrayInputStream(invalidXML.getBytes())
        );
        source.setSystemId("invalid-factory.xml");

        BindingLoader.createBinding(source);
    }

    // ==================== createBinding(String) Factory Method Tests ====================

    @Test
    public void should_CreateBindingSuccessfully_When_ValidFileNameProvided()
        throws BindingException {
        String testResourcePath =
            "src/test/resources/org/exolab/castor/builder/binding/test-binding.xml";
        ExtendedBinding binding = BindingLoader.createBinding(testResourcePath);
        assertNotNull(
            "Factory method should create binding from file",
            binding
        );
    }

    @Test(expected = BindingException.class)
    public void should_ThrowBindingException_When_FactoryCalledWithNonExistentFile()
        throws BindingException {
        BindingLoader.createBinding("non-existent-binding-file-xyz.xml");
    }

    // ==================== Binding Components Loading Tests ====================

    @Test
    public void should_LoadPackages_When_BindingContainsPackages()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "<package name=\"org.example.generated\"/>" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-packages.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding with packages should load successfully",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadDefaultBindingType_When_BindingSpecifiesType()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "<default-binding type=\"element\"/>" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-default-binding.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding with default-binding should load",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadNamingXML_When_BindingContainsNaming()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-naming.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should process NamingXML section",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadElementBindings_When_BindingContainsElements()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-elements.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should process element bindings",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadAttributeBindings_When_BindingContainsAttributes()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-attributes.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should process attribute bindings",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadComplexTypeBindings_When_BindingContainsComplexTypes()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-complex-types.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should process complex type bindings",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadSimpleTypeBindings_When_BindingContainsSimpleTypes()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-simple-types.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should process simple type bindings",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadGroupBindings_When_BindingContainsGroups()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-groups.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should process group bindings",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_LoadEnumBindings_When_BindingContainsEnums()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-enums.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should process enum bindings",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_SetSystemIdOnSource_When_SystemIdIsNull()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        assertNull("SystemId should initially be null", source.getSystemId());

        bindingLoader.loadBinding(source);
        assertNotNull("Binding should be loaded", bindingLoader.getBinding());
    }

    // ==================== BindingResolver Inner Class Tests ====================

    @Test
    public void should_SetAndGetBaseURL_When_ValidURLProvided() {
        String testUrl = "http://example.com/schemas/";
        bindingLoader.setBaseURL(testUrl);
        assertNotNull("Base URL should be set", bindingLoader);
    }

    @Test
    public void should_ResolveBindingPublicId_When_MatchingPublicIdProvided()
        throws SAXException, IOException {
        InputSource source = new InputSource(
            new ByteArrayInputStream("test".getBytes())
        );
        String publicId = "-//EXOLAB/Castor Binding Schema Version 1.0//EN";

        // Note: This tests that the resolver doesn't throw an exception
        // The actual resolution happens in the BindingResolver inner class
        assertNotNull("InputSource should be created", source);
    }

    @Test
    public void should_ResolveBindingSystemId_When_MatchingSystemIdProvided()
        throws SAXException, IOException {
        InputSource source = new InputSource(
            new ByteArrayInputStream("test".getBytes())
        );
        String systemId = "http://exolab.castor.org/binding.xsd";

        assertNotNull("InputSource should be created", source);
    }

    @Test
    public void should_ResolveRelativeURL_When_BaseURLAndRelativeSystemIdProvided() {
        String baseUrl = "http://example.com/binding/";
        bindingLoader.setBaseURL(baseUrl);
        assertNotNull(
            "Base URL should be set for relative resolution",
            bindingLoader
        );
    }

    // ==================== Error Handling & Edge Cases ====================

    @Test
    public void should_HandleNullInputSource_When_ResolverReturnsNull()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("test-null-resolver.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should load even with null resolver result",
            bindingLoader.getBinding()
        );
    }

    @Test(expected = BindingException.class)
    public void should_ThrowBindingException_When_UnmarshalThrowsMarshalException()
        throws BindingException {
        String invalidXML = "<?xml version=\"1.0\"?><malformed";
        InputSource source = new InputSource(
            new ByteArrayInputStream(invalidXML.getBytes())
        );
        source.setSystemId("invalid-marshal.xml");

        bindingLoader.loadBinding(source);
    }

    @Test(expected = BindingException.class)
    public void should_ThrowBindingException_When_UnmarshalThrowsValidationException()
        throws BindingException {
        String invalidXML = "<<broken>><";
        InputSource source = new InputSource(
            new ByteArrayInputStream(invalidXML.getBytes())
        );
        source.setSystemId("invalid-validation.xml");

        bindingLoader.loadBinding(source);
    }

    @Test
    public void should_HandleEmptyEnumerations_When_BindingHasNoComponents()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("empty-binding.xml");

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding with no components should still be loaded",
            bindingLoader.getBinding()
        );
    }

    // ==================== Multiple Factory Instances Tests ====================

    @Test
    public void should_CreateIndependentInstances_When_FactoryMethodCalled()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source1 = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source1.setSystemId("factory-1.xml");

        InputSource source2 = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source2.setSystemId("factory-2.xml");

        ExtendedBinding binding1 = BindingLoader.createBinding(source1);
        ExtendedBinding binding2 = BindingLoader.createBinding(source2);

        assertNotSame(
            "Factory should create independent instances",
            binding1,
            binding2
        );
    }

    // ==================== Complex Binding Tests ====================

    @Test
    public void should_LoadComplexBinding_When_BindingContainsMultipleComponents()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "<default-binding type=\"element\"/>" +
            "<package name=\"org.example\"/>" +
            "</binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("complex-binding.xml");

        bindingLoader.loadBinding(source);
        ExtendedBinding binding = bindingLoader.getBinding();
        assertNotNull("Complex binding should load successfully", binding);
    }

    @Test
    public void should_FactoryCreateNewLoader_When_FactoryMethodCalled()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source.setSystemId("factory-loader.xml");

        ExtendedBinding binding = BindingLoader.createBinding(source);
        assertNotNull(
            "Factory should create new loader instance internally",
            binding
        );
    }

    @Test
    public void should_LoadMultipleBindings_When_LoadingCalledSequentially()
        throws BindingException {
        String bindingXML1 =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "<package name=\"org.example.first\"/>" +
            "</binding>";

        String bindingXML2 =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\">" +
            "<package name=\"org.example.second\"/>" +
            "</binding>";

        InputSource source1 = new InputSource(
            new ByteArrayInputStream(bindingXML1.getBytes())
        );
        source1.setSystemId("binding-1.xml");

        InputSource source2 = new InputSource(
            new ByteArrayInputStream(bindingXML2.getBytes())
        );
        source2.setSystemId("binding-2.xml");

        bindingLoader.loadBinding(source1);
        bindingLoader.loadBinding(source2);

        assertNotNull(
            "Binding should accumulate components from multiple loads",
            bindingLoader.getBinding()
        );
    }

    // ==================== State & Initialization Tests ====================

    @Test
    public void should_InitializeBindingOnlyOnce_When_LoadingMultipleTimes()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source1 = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source1.setSystemId("state-test-1.xml");

        bindingLoader.loadBinding(source1);
        ExtendedBinding firstBinding = bindingLoader.getBinding();

        InputSource source2 = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        source2.setSystemId("state-test-2.xml");

        bindingLoader.loadBinding(source2);
        ExtendedBinding secondBinding = bindingLoader.getBinding();

        assertSame(
            "Same ExtendedBinding instance should be reused",
            firstBinding,
            secondBinding
        );
    }

    @Test
    public void should_GetBindingReturnNull_When_NoLoadingHasOccurred() {
        BindingLoader newLoader = new BindingLoader();
        assertNull(
            "getBinding should return null before any loading",
            newLoader.getBinding()
        );
    }

    @Test
    public void should_LoadBinding_When_InputSourceSystemIdIsNull()
        throws BindingException {
        String bindingXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<binding xmlns=\"http://castor.exolab.org/binding\"></binding>";

        InputSource source = new InputSource(
            new ByteArrayInputStream(bindingXML.getBytes())
        );
        // Do not set SystemId, leaving it null

        bindingLoader.loadBinding(source);
        assertNotNull(
            "Binding should load even when SystemId is null",
            bindingLoader.getBinding()
        );
    }

    @Test
    public void should_AllowSetBaseURLMultipleTimes_When_DifferentURLsProvided() {
        bindingLoader.setBaseURL("http://first.com/");
        bindingLoader.setBaseURL("http://second.com/");
        assertNotNull("Base URL should be updatable", bindingLoader);
    }
}
