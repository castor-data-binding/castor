package org.castor.cpa.jpa.info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.persistence.sql.keygen.TableKeyGenerator;
import org.exolab.castor.jdo.engine.KeyGeneratorDescriptor;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.xml.NamedNativeQuery;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class InfoToDescriptorConverterTest {
    static final String NAME = "name";
    static final String QUERY = "query";
    static final String NAME2 = "name2";
    static final String QUERY2 = "query2";
    private ClassInfo _classInfo;
    @Mock
    private ClassDescriptorResolver _resolver;
    private ClassDescriptorImpl _descriptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cacheInformationWillBeConverted() throws Exception {

        _classInfo = new ClassInfo();
        _classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classInfo.setDescribedClass(JpaCacheTestClass.class);
        JPAClassNature nature = new JPAClassNature(_classInfo);

        Properties cacheProperties = new Properties();
        cacheProperties.setProperty("type", "none");

        nature.setCacheProperties(cacheProperties);

        _descriptor = new ClassDescriptorImpl();

        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);
        Properties properties = (Properties) _descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                        + "cacheParameters");
        assertEquals("none", properties.getProperty("type"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void namedQueriesInformationWillBeConverted() throws Exception {

        _classInfo = new ClassInfo();
        _classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classInfo.setDescribedClass(JpaNamedQueriesTestClass.class);
        JPAClassNature nature = new JPAClassNature(_classInfo);

        Map<String, String> namedQueriesMap = new HashMap<String, String>();
        namedQueriesMap.put(NAME, QUERY);
        namedQueriesMap.put(NAME2, QUERY2);
        nature.setNamedQuery(namedQueriesMap);

        _descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);
        Map<String, String> returnedMap = (Map<String, String>) _descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                        + "namedQueries");

        assertTrue(returnedMap.keySet().contains(NAME));
        assertTrue(returnedMap.keySet().contains(NAME2));
        assertEquals(QUERY, returnedMap.get(NAME));
        assertEquals(QUERY2, returnedMap.get(NAME2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void namedNativeQueryInformationWillBeConverted() throws Exception {
        _classInfo = new ClassInfo();
        _classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classInfo.setDescribedClass(JPANamedNativeQueryTestClass.class);
        JPAClassNature nature = new JPAClassNature(_classInfo);

        Map<String, String> namedNativeQueryMap = new HashMap<String, String>();
        namedNativeQueryMap.put(NAME, QUERY);
        nature.setNamedNativeQuery(namedNativeQueryMap);

        _descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);
        Map<String, NamedNativeQuery> returnedMap = (Map<String, NamedNativeQuery>) _descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                        + "namedNativeQueries");

        assertEquals(NAME, returnedMap.keySet().iterator().next());
        assertEquals(NAME, returnedMap.values().iterator().next().getName());
        assertEquals(QUERY, returnedMap.values().iterator().next().getQuery());
    }

    @Test
    public void sequenceGeneratedValueWillBeConverted() throws Exception {
        _classInfo = ClassInfoBuilder
                .buildClassInfo(SequenceGeneratedValueTestClass.class);
        _descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);

        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                _descriptor);
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
        _classInfo = ClassInfoBuilder
                .buildClassInfo(TableGeneratedValueTestClass.class);
        _descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);

        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                _descriptor);
        KeyGeneratorDescriptor generatorDescriptor = jdoDescriptor
                .getKeyGeneratorDescriptor();
        assertEquals("TABLE", generatorDescriptor.getKeyGeneratorFactoryName());
    }

    @Test
    public void primaryKeyTypeWillBeSetInTableGeneratorDescriptor()
            throws Exception {
        _classInfo = ClassInfoBuilder
                .buildClassInfo(TableGeneratedValueTestClass.class);
        _descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);

        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                _descriptor);
        KeyGeneratorDescriptor generatorDescriptor =
            jdoDescriptor.getKeyGeneratorDescriptor();
        JPATableGeneratorDescriptor jpaDescriptor =
            (JPATableGeneratorDescriptor) generatorDescriptor.getParams().get(
                    TableKeyGenerator.DESCRIPTOR_KEY);
        assertEquals(Long.class, jpaDescriptor.getPrimaryKeyType());
    }
    
    @Test
    public void versionFieldWillBeSet() throws Exception {
        _classInfo = ClassInfoBuilder
                .buildClassInfo(VersionTestClass.class);
        _descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);
        ClassDescriptorJDONature jdoDescriptor = new ClassDescriptorJDONature(
                _descriptor);
        assertEquals("version", jdoDescriptor.getVersionField());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void namedNativeQueriesInformationWillBeConverted() throws Exception {

        _classInfo = new ClassInfo();
        _classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classInfo.setDescribedClass(JpaNamedQueriesTestClass.class);
        JPAClassNature nature = new JPAClassNature(_classInfo);

        Map<String, String> namedNativeQueriesMap = new HashMap<String, String>();
        namedNativeQueriesMap.put(NAME, QUERY);
        namedNativeQueriesMap.put(NAME2, QUERY2);
        nature.setNamedNativeQuery(namedNativeQueriesMap);

        _descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(_classInfo, _resolver, _descriptor);
        Map<String, NamedNativeQuery> returnedMap = (Map<String, NamedNativeQuery>) _descriptor
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
