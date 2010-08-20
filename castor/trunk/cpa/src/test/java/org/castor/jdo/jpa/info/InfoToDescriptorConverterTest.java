package org.castor.jdo.jpa.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.castor.cpa.persistence.sql.keygen.TableKeyGenerator;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.exolab.castor.jdo.engine.KeyGeneratorDescriptor;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.xml.NamedNativeQuery;
import org.exolab.castor.xml.ClassDescriptorResolver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InfoToDescriptorConverterTest {

    final static String NAME = "name";
    final static String QUERY = "query";
    final static String NAME2 = "name2";
    final static String QUERY2 = "query2";
    ClassInfo classInfo;
    @Mock
    ClassDescriptorResolver resolver;
    ClassDescriptorImpl descriptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cacheInformationWillBeConverted() throws Exception {

        classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classInfo.setDescribedClass(JpaCacheTestClass.class);
        JPAClassNature nature = new JPAClassNature(classInfo);

        Properties cacheProperties = new Properties();
        cacheProperties.setProperty("type", "none");

        nature.setCacheProperties(cacheProperties);

        descriptor = new ClassDescriptorImpl();

        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);
        Properties properties = (Properties) descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                        + "cacheParameters");
        assertEquals("none", properties.getProperty("type"));
    }

    @Test
    public void namedQueriesInformationWillBeConverted() throws Exception {

        classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classInfo.setDescribedClass(JpaNamedQueriesTestClass.class);
        JPAClassNature nature = new JPAClassNature(classInfo);

        Map<String, String> namedQueriesMap = new HashMap<String, String>();
        namedQueriesMap.put(NAME, QUERY);
        namedQueriesMap.put(NAME2, QUERY2);
        nature.setNamedQuery(namedQueriesMap);

        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);
        Map<String, String> returnedMap = (Map) descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                        + "namedQueries");

        assertTrue(returnedMap.keySet().contains(NAME));
        assertTrue(returnedMap.keySet().contains(NAME2));
        assertEquals(QUERY, returnedMap.get(NAME));
        assertEquals(QUERY2, returnedMap.get(NAME2));
    }

    @Test
    public void namedNativeQueryInformationWillBeConverted() throws Exception {
        classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classInfo.setDescribedClass(JPANamedNativeQueryTestClass.class);
        JPAClassNature nature = new JPAClassNature(classInfo);

        Map<String, String> namedNativeQueryMap = new HashMap<String, String>();
        namedNativeQueryMap.put(NAME, QUERY);
        nature.setNamedNativeQuery(namedNativeQueryMap);

        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);
        Map<String, NamedNativeQuery> returnedMap = (Map) descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                        + "namedNativeQueries");

        assertEquals(NAME, returnedMap.keySet().iterator().next());
        assertEquals(NAME, returnedMap.values().iterator().next().getName());
        assertEquals(QUERY, returnedMap.values().iterator().next().getQuery());
    }

    @Test
    public void sequenceGeneratedValueWillBeConverted() throws Exception {
        classInfo = ClassInfoBuilder
                .buildClassInfo(SequenceGeneratedValueTestClass.class);
        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);

        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                descriptor);
        KeyGeneratorDescriptor generatorDescriptor = jdoDescriptor
                .getKeyGeneratorDescriptor();
        assertEquals("SEQUENCE",
                generatorDescriptor.getKeyGeneratorFactoryName());
        Properties generatorParameters = generatorDescriptor.getParams();
        assertEquals("test_sequence",
                generatorParameters.getProperty("sequence"));
    }

    @Test
    public void tableGeneratedValueWillBeConverted() throws Exception {
        classInfo = ClassInfoBuilder
                .buildClassInfo(TableGeneratedValueTestClass.class);
        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);

        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                descriptor);
        KeyGeneratorDescriptor generatorDescriptor = jdoDescriptor
                .getKeyGeneratorDescriptor();
        assertEquals("TABLE", generatorDescriptor.getKeyGeneratorFactoryName());
    }

    @Test
    public void primaryKeyTypeWillBeSetInTableGeneratorDescriptor()
            throws Exception {
        classInfo = ClassInfoBuilder
                .buildClassInfo(TableGeneratedValueTestClass.class);
        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);

        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                descriptor);
        KeyGeneratorDescriptor generatorDescriptor = jdoDescriptor
                .getKeyGeneratorDescriptor();
        JPATableGeneratorDescriptor jpaDescriptor = (JPATableGeneratorDescriptor) generatorDescriptor
                .getParams().get(TableKeyGenerator.DESCRIPTOR_KEY);
        assertEquals(Long.class, jpaDescriptor.getPrimaryKeyType());
    }
    
    @Test
    public void versionFieldWillBeSet() throws Exception {
        classInfo = ClassInfoBuilder
                .buildClassInfo(VersionTestClass.class);
        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);
        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                descriptor);
        assertEquals("version", jdoDescriptor.getVersionField());
    }
    
    @Test
    public void namedNativeQueriesInformationWillBeConverted() throws Exception {

        classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classInfo.setDescribedClass(JpaNamedQueriesTestClass.class);
        JPAClassNature nature = new JPAClassNature(classInfo);

        Map<String, String> namedNativeQueriesMap = new HashMap<String, String>();
        namedNativeQueriesMap.put(NAME, QUERY);
        namedNativeQueriesMap.put(NAME2, QUERY2);
        nature.setNamedNativeQuery(namedNativeQueriesMap);

        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);
        Map<String, NamedNativeQuery> returnedMap = (Map) descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                        + "namedNativeQueries");

        assertTrue(returnedMap.keySet().contains(NAME));
        assertTrue(returnedMap.keySet().contains(NAME2));
        assertEquals(NAME, returnedMap.get(NAME).getName());
        assertEquals(NAME2, returnedMap.get(NAME2).getName());
        assertEquals(QUERY, returnedMap.get(NAME).getQuery());
        assertEquals(QUERY2, returnedMap.get(NAME2).getQuery());
    }

}
