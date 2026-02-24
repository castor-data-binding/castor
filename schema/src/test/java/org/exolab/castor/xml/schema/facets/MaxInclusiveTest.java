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
 * Comprehensive test cases for MaxInclusive facet class
 */
public class MaxInclusiveTest {

    private MaxInclusive maxInclusive;
    private SimpleType mockOwningType;

    @Before
    public void setUp() {
        mockOwningType = mock(SimpleType.class);
        maxInclusive = new MaxInclusive("100");
        maxInclusive.setOwningType(mockOwningType);
    }

    @Test
    public void testConstructor() {
        MaxInclusive facet = new MaxInclusive("50");
        assertNotNull(facet);
        assertEquals("50", facet.getValue());
        assertEquals(Facet.MAX_INCLUSIVE, facet.getName());
    }

    @Test
    public void testConstructorWithZero() {
        MaxInclusive facet = new MaxInclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    @Test
    public void testConstructorWithNegativeValue() {
        MaxInclusive facet = new MaxInclusive("-100");
        assertNotNull(facet);
        assertEquals("-100", facet.getValue());
    }

    @Test
    public void testConstructorWithDecimalValue() {
        MaxInclusive facet = new MaxInclusive("3.14");
        assertNotNull(facet);
        assertEquals("3.14", facet.getValue());
    }

    @Test
    public void testOverridesBaseMaxExclusive() {
        Facet maxExclusiveFacet = new MaxExclusive("100");
        assertTrue(maxInclusive.overridesBase(maxExclusiveFacet));
    }

    @Test
    public void testOverridesBaseMaxInclusive() {
        Facet maxInclusiveFacet = new MaxInclusive("100");
        assertTrue(maxInclusive.overridesBase(maxInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMinInclusive() {
        Facet minInclusiveFacet = new MinInclusive("10");
        assertFalse(maxInclusive.overridesBase(minInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMinExclusive() {
        Facet minExclusiveFacet = new MinExclusive("10");
        assertFalse(maxInclusive.overridesBase(minExclusiveFacet));
    }

    @Test
    public void testCheckConstraintsWithSelfReference() throws SchemaException {
        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxInclusive);
        when(mockOwningType.isNumericType()).thenReturn(true);

        // Should not throw exception when checking against self
        maxInclusive.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMinInclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("100");
        maxIncl.setOwningType(mockOwningType);

        MinInclusive minIncl = new MinInclusive("10");
        minIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);
        localFacets.add(minIncl);

        // Should not throw - maxInclusive (100) > minInclusive (10)
        maxIncl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMinInclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("10");
        maxIncl.setOwningType(mockOwningType);

        MinInclusive minIncl = new MinInclusive("10");
        minIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);
        localFacets.add(minIncl);

        // Should throw - maxInclusive (10) <= minInclusive (10)
        try {
            maxIncl.checkConstraints(localFacets.elements(), null);
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(
                e.getMessage().contains("maxInclusive") &&
                    e.getMessage().contains("minInclusive")
            );
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("50");
        maxIncl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("100");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should not throw - maxInclusive (50) <= baseMaxInclusive (100)
        maxIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("150");
        maxIncl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("100");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should throw - maxInclusive (150) > baseMaxInclusive (100)
        try {
            maxIncl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("greater than"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMinInclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("100");
        maxIncl.setOwningType(mockOwningType);

        MinInclusive baseMinIncl = new MinInclusive("50");
        baseMinIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinIncl);

        // Should not throw - maxInclusive (100) >= baseMinInclusive (50)
        maxIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMinInclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("30");
        maxIncl.setOwningType(mockOwningType);

        MinInclusive baseMinIncl = new MinInclusive("50");
        baseMinIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinIncl);

        // Should throw - maxInclusive (30) < baseMinInclusive (50)
        try {
            maxIncl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("100");
        maxIncl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("50");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should not throw - maxInclusive (100) > baseMinExclusive (50)
        maxIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("50");
        maxIncl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("50");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should throw - maxInclusive (50) <= baseMinExclusive (50)
        try {
            maxIncl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than or equal"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("50");
        maxIncl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("100");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should not throw - maxInclusive (50) < baseMaxExclusive (100)
        maxIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("100");
        maxIncl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("100");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should throw - maxInclusive (100) >= baseMaxExclusive (100)
        try {
            maxIncl.checkConstraints(
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

        MaxInclusive maxIncl = new MaxInclusive("100");
        maxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        // Should not throw for non-numeric types
        maxIncl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithNullBaseFacets() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxInclusive maxIncl = new MaxInclusive("100");
        maxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxIncl);

        // Should not throw with null base facets
        maxIncl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testGetNameReturnsMaxInclusive() {
        assertEquals(Facet.MAX_INCLUSIVE, maxInclusive.getName());
    }
}
