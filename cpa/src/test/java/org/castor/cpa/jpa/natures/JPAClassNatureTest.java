package org.castor.cpa.jpa.natures;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.InheritanceType;

import org.castor.core.nature.PropertyHolder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPAClassNatureTest {
    private JPAClassNature _nature;
    @Mock
    private PropertyHolder _holder;
    private static final String CANONICAL_NAME = JPAClassNature.class.getCanonicalName();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setCachePropertiesSetsRelatedProperty() throws Exception {
        Properties cacheProperties = new Properties();
        cacheProperties.setProperty("type", "none");
        when(_holder.hasNature(CANONICAL_NAME)).thenReturn(true);
        when(
                _holder.getProperty(CANONICAL_NAME
                        + JPAClassNature.CACHE_PROPERTIES)).thenReturn(
                cacheProperties);
        _nature = new JPAClassNature(_holder);
        _nature.setCacheProperties(cacheProperties);
        assertEquals(cacheProperties, _nature.getCacheProperties());
    }

    @Test
    public void setInheritanceRelatedProperty() throws Exception {
        InheritanceType strategy = InheritanceType.JOINED;

        when(_holder.hasNature(CANONICAL_NAME)).thenReturn(true);
        when(_holder.getProperty(CANONICAL_NAME
                + JPAClassNature.INHERITANCE_STRATEGY)).thenReturn(strategy);
        _nature = new JPAClassNature(_holder);
        _nature.setInheritanceStrategy(strategy);
        assertEquals(strategy, _nature.getInheritanceStrategy());
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

        when(_holder.hasNature(CANONICAL_NAME)).thenReturn(true);
        when(_holder.getProperty(CANONICAL_NAME + JPAClassNature.NAMED_QUERY))
                .thenReturn(namedQueryMap);
        _nature = new JPAClassNature(_holder);
        _nature.setNamedQuery(namedQueryMap);
        assertEquals(namedQueryMap, _nature.getNamedQuery());
    }

    @Test
    public void setAbstractSetsRelatedProperty() throws Exception {
        final boolean hasMappedSuperclass = Boolean.TRUE;
        when(_holder.hasNature(CANONICAL_NAME)).thenReturn(true);
        when(
                _holder.getProperty(CANONICAL_NAME
                        + JPAClassNature.MAPPED_SUPERCLASS)).thenReturn(
                hasMappedSuperclass);
        _nature = new JPAClassNature(_holder);
        _nature.setMappedSuperclass(hasMappedSuperclass);
        assertEquals(hasMappedSuperclass, _nature.hasMappedSuperclass());
    }

    @Test
    public void setNamedNativeQueryValuesSetsRelatedProperty() throws Exception {
        final String name = "name";
        final String query = "query";

        Map<String, String> namedNativeQueryMap = new HashMap<String, String>();
        namedNativeQueryMap.put(name, query);

        when(_holder.hasNature(CANONICAL_NAME)).thenReturn(true);
        when(
                _holder.getProperty(CANONICAL_NAME
                        + JPAClassNature.NAMED_NATIVE_QUERY)).thenReturn(
                namedNativeQueryMap);
        _nature = new JPAClassNature(_holder);
        _nature.setNamedNativeQuery(namedNativeQueryMap);
        assertEquals(namedNativeQueryMap, _nature.getNamedNativeQuery());
    }
}
