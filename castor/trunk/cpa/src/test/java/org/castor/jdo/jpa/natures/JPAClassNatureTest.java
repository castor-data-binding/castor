package org.castor.jdo.jpa.natures;

import java.util.Map;
import java.util.HashMap;
import static org.junit.Assert.*;

import java.util.Properties;

import org.castor.core.nature.PropertyHolder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class JPAClassNatureTest {

    JPAClassNature nature;
    @Mock
    PropertyHolder holder;
    private static final String canonicalName = JPAClassNature.class
            .getCanonicalName();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setCachePropertiesSetsRelatedProperty() throws Exception {
        Properties cacheProperties = new Properties();
        cacheProperties.setProperty("type", "none");
        when(holder.hasNature(canonicalName)).thenReturn(true);
        when(
                holder.getProperty(canonicalName
                        + JPAClassNature.CACHE_PROPERTIES)).thenReturn(
                cacheProperties);
        nature = new JPAClassNature(holder);
        nature.setCacheProperties(cacheProperties);
        assertEquals(cacheProperties, nature.getCacheProperties());
    }

    @Test
    public void setNamedQueriesValuesSetsRelatedProperty() throws Exception {
        final String name = "name";
        final String query = "query";
        final String name2 = "name2";
        final String query2 = "query2";

        Map<String, String> namedQueryMap = new HashMap<String, String>();
        namedQueryMap.put(name, query);
        namedQueryMap.put(name2, query2);

        when(holder.hasNature(canonicalName)).thenReturn(true);
        when(holder.getProperty(canonicalName + JPAClassNature.NAMED_QUERY))
                .thenReturn(namedQueryMap);
        nature = new JPAClassNature(holder);
        nature.setNamedQuery(namedQueryMap);
        assertEquals(namedQueryMap, nature.getNamedQuery());
    }

    @Test
    public void setAbstractSetsRelatedProperty() throws Exception {
        final boolean hasMappedSuperclass = Boolean.TRUE;
        when(holder.hasNature(canonicalName)).thenReturn(true);
        when(
                holder.getProperty(canonicalName
                        + JPAClassNature.MAPPED_SUPERCLASS)).thenReturn(
                hasMappedSuperclass);
        nature = new JPAClassNature(holder);
        nature.setMappedSuperclass(hasMappedSuperclass);
        assertEquals(hasMappedSuperclass, nature.hasMappedSuperclass());
    }

    @Test
    public void setNamedNativeQueryValuesSetsRelatedProperty() throws Exception {
        final String name = "name";
        final String query = "query";

        Map<String, String> namedNativeQueryMap = new HashMap<String, String>();
        namedNativeQueryMap.put(name, query);

        when(holder.hasNature(canonicalName)).thenReturn(true);
        when(
                holder.getProperty(canonicalName
                        + JPAClassNature.NAMED_NATIVE_QUERY)).thenReturn(
                namedNativeQueryMap);
        nature = new JPAClassNature(holder);
        nature.setNamedNativeQuery(namedNativeQueryMap);
        assertEquals(namedNativeQueryMap, nature.getNamedNativeQuery());
    }
}
