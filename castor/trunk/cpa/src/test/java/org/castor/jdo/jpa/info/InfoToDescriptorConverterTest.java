package org.castor.jdo.jpa.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.castor.jdo.jpa.natures.JPAClassNature;
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

    final static String NAME="name";
    final static String QUERY="query";

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
    public void namedNativeQueryInformationWillBeConverted() throws Exception{
        classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classInfo.setDescribedClass(JPANamedNativeQueryTestClass.class);
        JPAClassNature nature = new JPAClassNature(classInfo);

        Map<String,String> namedNativeQueryMap = new HashMap<String,String>();
        namedNativeQueryMap.put(NAME, QUERY);
        nature.setNamedNativeQuery(namedNativeQueryMap);

        descriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, resolver, descriptor);
        Map<String,NamedNativeQuery> returnedMap = (Map)descriptor
                .getProperty(ClassDescriptorJDONature.class.getCanonicalName()
                    + "namedNativeQueries");

        assertEquals(NAME, returnedMap.keySet().iterator().next());
        assertEquals(NAME, returnedMap.values().iterator().next().getName());
        assertEquals(QUERY, returnedMap.values().iterator().next().getQuery());
}

}
