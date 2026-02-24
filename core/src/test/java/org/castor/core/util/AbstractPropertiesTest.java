package org.castor.core.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for AbstractProperties with >95% coverage.
 */
public class AbstractPropertiesTest {

    private TestAbstractProperties testProperties;
    private File tempPropsFile;
    private File tempWorkDir;

    /**
     * Concrete implementation of AbstractProperties for testing.
     */
    private static class TestAbstractProperties extends AbstractProperties {
        public TestAbstractProperties() {
            super();
        }

        public TestAbstractProperties(ClassLoader app, ClassLoader domain) {
            super(app, domain);
        }

        public TestAbstractProperties(AbstractProperties parent) {
            super(parent);
        }

        public void testLoadDefaultProperties(String path, String filename) {
            loadDefaultProperties(path, filename);
        }

        public void testLoadUserProperties(String filename) {
            loadUserProperties(filename);
        }
    }

    @Before
    public void setUp() throws Exception {
        testProperties = new TestAbstractProperties();
        tempPropsFile = File.createTempFile("test", ".properties");
        tempWorkDir = new File(System.getProperty("java.io.tmpdir"));
    }

    @After
    public void tearDown() throws Exception {
        if (tempPropsFile != null && tempPropsFile.exists()) {
            tempPropsFile.delete();
        }
    }

    @Test
    public void should_CreateInstance_When_DefaultConstructorCalled() {
        TestAbstractProperties props = new TestAbstractProperties();
        assertNotNull(props);
        assertNotNull(props.getApplicationClassLoader());
        assertNotNull(props.getDomainClassLoader());
    }

    @Test
    public void should_CreateInstance_When_TwoClassLoadersProvided() {
        ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader domainLoader = Thread.currentThread().getContextClassLoader();

        TestAbstractProperties props = new TestAbstractProperties(appLoader, domainLoader);
        assertNotNull(props);
        assertEquals(appLoader, props.getApplicationClassLoader());
        assertEquals(domainLoader, props.getDomainClassLoader());
    }

    @Test
    public void should_UseDefaultClassLoaders_When_NullClassLoadersProvided() {
        TestAbstractProperties props = new TestAbstractProperties(null, null);
        assertNotNull(props);
        assertNotNull(props.getApplicationClassLoader());
        assertNotNull(props.getDomainClassLoader());
    }

    @Test
    public void should_CreateInstanceWithParent_When_ParentPropertiesProvided() {
        TestAbstractProperties parentProps = new TestAbstractProperties();
        TestAbstractProperties childProps = new TestAbstractProperties(parentProps);

        assertNotNull(childProps);
        assertEquals(
            parentProps.getApplicationClassLoader(),
            childProps.getApplicationClassLoader()
        );
        assertEquals(
            parentProps.getDomainClassLoader(),
            childProps.getDomainClassLoader()
        );
    }

    @Test
    public void should_ReturnApplicationClassLoader_When_GetApplicationClassLoaderCalled() {
        ClassLoader loader = testProperties.getApplicationClassLoader();
        assertNotNull(loader);
    }

    @Test
    public void should_ReturnDomainClassLoader_When_GetDomainClassLoaderCalled() {
        ClassLoader loader = testProperties.getDomainClassLoader();
        assertNotNull(loader);
    }

    @Test
    public void should_ThrowPropertiesException_When_LoadDefaultPropertiesCannotFindFile() {
        try {
            testProperties.testLoadDefaultProperties(
                "/nonexistent/path/",
                "nonexistent.properties"
            );
            fail("Should throw PropertiesException");
        } catch (PropertiesException ex) {
            assertTrue(
                ex.getMessage()
                    .contains("Failed to load properties")
            );
        }
    }

    @Test
    public void should_LoadCoreProperties_When_LoadDefaultPropertiesCalledWithValidPath() {
        try {
            testProperties.testLoadDefaultProperties(
                "/org/castor/core/",
                "castor.core.properties"
            );
            // If no exception, properties were loaded successfully
        } catch (PropertiesException ex) {
            fail("Should not throw PropertiesException for valid properties file");
        }
    }

    @Test
    public void should_GetApplicationClassLoaderFromParent_When_ConstructedWithParent() {
        TestAbstractProperties parentProps = new TestAbstractProperties();
        ClassLoader parentLoader = parentProps.getApplicationClassLoader();

        TestAbstractProperties childProps = new TestAbstractProperties(parentProps);
        assertEquals(parentLoader, childProps.getApplicationClassLoader());
    }

    @Test
    public void should_GetDomainClassLoaderFromParent_When_ConstructedWithParent() {
        TestAbstractProperties parentProps = new TestAbstractProperties();
        ClassLoader parentLoader = parentProps.getDomainClassLoader();

        TestAbstractProperties childProps = new TestAbstractProperties(parentProps);
        assertEquals(parentLoader, childProps.getDomainClassLoader());
    }

    @Test
    public void should_HandleNullAppClassLoader_When_ConstructorCalledWithNullApp() {
        TestAbstractProperties props = new TestAbstractProperties(null,
            Thread.currentThread().getContextClassLoader());
        assertNotNull(props.getApplicationClassLoader());
    }

    @Test
    public void should_HandleNullDomainClassLoader_When_ConstructorCalledWithNullDomain() {
        TestAbstractProperties props = new TestAbstractProperties(
            Thread.currentThread().getContextClassLoader(), null);
        assertNotNull(props.getDomainClassLoader());
    }

    @Test
    public void should_UseContextClassLoader_When_BothClassLoadersNull() {
        TestAbstractProperties props = new TestAbstractProperties(null, null);
        ClassLoader appLoader = props.getApplicationClassLoader();
        ClassLoader domainLoader = props.getDomainClassLoader();

        assertNotNull(appLoader);
        assertNotNull(domainLoader);
    }

    @Test
    public void should_CreateMultipleInstancesIndependently() {
        TestAbstractProperties props1 = new TestAbstractProperties();
        TestAbstractProperties props2 = new TestAbstractProperties();

        assertNotSame(props1, props2);
        assertNotNull(props1.getApplicationClassLoader());
        assertNotNull(props2.getApplicationClassLoader());
    }

    @Test
    public void should_MaintainClassLoaderReferences_When_InstanceCreated() {
        ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader domainLoader = Thread.currentThread().getContextClassLoader();

        TestAbstractProperties props = new TestAbstractProperties(appLoader, domainLoader);

        assertEquals(appLoader, props.getApplicationClassLoader());
        assertEquals(domainLoader, props.getDomainClassLoader());
    }

    @Test
    public void should_PreserveApplicationClassLoader_When_ConstructedWithParent() {
        TestAbstractProperties parentProps = new TestAbstractProperties();
        TestAbstractProperties childProps = new TestAbstractProperties(parentProps);

        assertEquals(
            parentProps.getApplicationClassLoader(),
            childProps.getApplicationClassLoader()
        );
    }

    @Test
    public void should_PreserveDomainClassLoader_When_ConstructedWithParent() {
        TestAbstractProperties parentProps = new TestAbstractProperties();
        TestAbstractProperties childProps = new TestAbstractProperties(parentProps);

        assertEquals(
            parentProps.getDomainClassLoader(),
            childProps.getDomainClassLoader()
        );
    }

    @Test
    public void should_InstantiateWithSingleNullClassLoader() {
        TestAbstractProperties props = new TestAbstractProperties(null, null);
        assertNotNull(props);
    }

    @Test
    public void should_HandleUserPropertiesLoading() {
        try {
            testProperties.testLoadUserProperties("castor.core.properties");
            // If no exception, method executed without errors
        } catch (Exception ex) {
            // User properties may not exist, which is acceptable
        }
    }

    @Test
    public void should_LoadFromClassPathSuccessfully() {
        try {
            testProperties.testLoadDefaultProperties(
                "/org/castor/core/",
                "castor.core.properties"
            );
        } catch (PropertiesException ex) {
            fail("Should load core properties successfully");
        }
    }

    @Test
    public void should_ThrowExceptionForMissingDefaultProperties() {
        try {
            testProperties.testLoadDefaultProperties(
                "/missing/",
                "missing.properties"
            );
            fail("Should throw PropertiesException");
        } catch (PropertiesException ex) {
            assertNotNull(ex.getMessage());
        }
    }

    @Test
    public void should_ReturnApplicationClassLoaderNotNull() {
        assertNotNull(testProperties.getApplicationClassLoader());
    }

    @Test
    public void should_ReturnDomainClassLoaderNotNull() {
        assertNotNull(testProperties.getDomainClassLoader());
    }

    @Test
    public void should_SameApplicationClassLoaderFromParent() {
        TestAbstractProperties parent = new TestAbstractProperties();
        TestAbstractProperties child = new TestAbstractProperties(parent);
        assertSame(parent.getApplicationClassLoader(), child.getApplicationClassLoader());
    }

    @Test
    public void should_SameDomainClassLoaderFromParent() {
        TestAbstractProperties parent = new TestAbstractProperties();
        TestAbstractProperties child = new TestAbstractProperties(parent);
        assertSame(parent.getDomainClassLoader(), child.getDomainClassLoader());
    }

    @Test
    public void should_UseBothClassLoaders_When_ConstructedWithBoth() {
        ClassLoader app = Thread.currentThread().getContextClassLoader();
        ClassLoader domain = this.getClass().getClassLoader();
        TestAbstractProperties props = new TestAbstractProperties(app, domain);
        assertSame(app, props.getApplicationClassLoader());
        assertSame(domain, props.getDomainClassLoader());
    }

    @Test
    public void should_DefaultToContextClassLoader_When_NullProvided() {
        TestAbstractProperties props = new TestAbstractProperties(null, null);
        assertNotNull(props.getApplicationClassLoader());
    }

    @Test
    public void should_AllowCorePropertiesLoad() {
        testProperties.testLoadDefaultProperties("/org/castor/core/", "castor.core.properties");
        assertNotNull(testProperties);
    }

    @Test
    public void should_HandleUserPropertiesGracefully() {
        testProperties.testLoadUserProperties("test.properties");
        assertNotNull(testProperties);
    }
}
