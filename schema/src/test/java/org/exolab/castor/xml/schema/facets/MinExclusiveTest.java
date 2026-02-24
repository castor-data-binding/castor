package org.exolab.castor.xml.schema.facets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Vector;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SimpleType;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test cases for MinExclusive facet class
 */
public class MinExclusiveTest {

    private MinExclusive minExclusive;
    private SimpleType mockOwningType;

    @Before
    public void setUp() {
        mockOwningType = mock(SimpleType.class);
        minExclusive = new MinExclusive("5");
        minExclusive.setOwningType(mockOwningType);
    }

    @Test
    public void testConstructor() {
        MinExclusive facet = new MinExclusive("10");
        assertNotNull(facet);
        assertEquals("10", facet.getValue());
        assertEquals(Facet.MIN_EXCLUSIVE, facet.getName());
    }

    @Test
    public void testConstructorWithZero() {
        MinExclusive facet = new MinExclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    @Test
    public void testConstructorWithNegativeValue() {
        MinExclusive facet = new MinExclusive("-100");
        assertNotNull(facet);
        assertEquals("-100", facet.getValue());
    }

    @Test
    public void testConstructorWithDecimalValue() {
        MinExclusive facet = new MinExclusive("3.14");
        assertNotNull(facet);
        assertEquals("3.14", facet.getValue());
    }

    @Test
    public void testOverridesBaseMinExclusive() {
        Facet minExclusiveFacet = new MinExclusive("5");
        assertTrue(minExclusive.overridesBase(minExclusiveFacet));
    }

    @Test
    public void testOverridesBaseMinInclusive() {
        Facet minInclusiveFacet = new MinInclusive("3");
        assertTrue(minExclusive.overridesBase(minInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMaxInclusive() {
        Facet maxInclusiveFacet = new MaxInclusive("10");
        assertFalse(minExclusive.overridesBase(maxInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMaxExclusive() {
        Facet maxExclusiveFacet = new MaxExclusive("10");
        assertFalse(minExclusive.overridesBase(maxExclusiveFacet));
    }

    @Test
    public void testCheckConstraintsWithSelfReference() throws SchemaException {
        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExclusive);
        when(mockOwningType.isNumericType()).thenReturn(true);

        // Should not throw exception when checking against self
        minExclusive.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMaxExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("5");
        minExcl.setOwningType(mockOwningType);

        MaxExclusive maxExcl = new MaxExclusive("10");
        maxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);
        localFacets.add(maxExcl);

        // Should not throw - minExclusive (5) < maxExclusive (10)
        minExcl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMaxExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("10");
        minExcl.setOwningType(mockOwningType);

        MaxExclusive maxExcl = new MaxExclusive("10");
        maxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);
        localFacets.add(maxExcl);

        // Should throw - minExclusive (10) >= maxExclusive (10)
        try {
            minExcl.checkConstraints(localFacets.elements(), null);
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(
                e.getMessage().contains("minExclusive") &&
                    e.getMessage().contains("maxExclusive")
            );
        }
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("10");
        minExcl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("5");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should not throw - minExclusive (10) >= baseMinExclusive (5)
        minExcl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("3");
        minExcl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("5");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should throw - minExclusive (3) < baseMinExclusive (5)
        try {
            minExcl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("5");
        minExcl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("10");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should not throw - minExclusive (5) < baseMaxInclusive (10)
        minExcl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("15");
        minExcl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("10");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should throw - minExclusive (15) > baseMaxInclusive (10)
        try {
            minExcl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("greater than"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("5");
        minExcl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("10");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should not throw - minExclusive (5) < baseMaxExclusive (10)
        minExcl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("10");
        minExcl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("10");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should throw - minExclusive (10) >= baseMaxExclusive (10)
        try {
            minExcl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("greater than or equal"));
        }
    }

    @Test
    public void testCheckConstraintsNonNumericType() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(false);

        MinExclusive minExcl = new MinExclusive("5");
        minExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        // Should not throw for non-numeric types
        minExcl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithNullBaseFacets() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinExclusive minExcl = new MinExclusive("5");
        minExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minExcl);

        // Should not throw with null base facets
        minExcl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testGetNameReturnsMinExclusive() {
        assertEquals(Facet.MIN_EXCLUSIVE, minExclusive.getName());
    }
}
