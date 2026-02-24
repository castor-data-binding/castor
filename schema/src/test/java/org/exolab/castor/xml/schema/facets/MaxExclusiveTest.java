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
 * Comprehensive test cases for MaxExclusive facet class
 */
public class MaxExclusiveTest {

    private MaxExclusive maxExclusive;
    private SimpleType mockOwningType;

    @Before
    public void setUp() {
        mockOwningType = mock(SimpleType.class);
        maxExclusive = new MaxExclusive("100");
        maxExclusive.setOwningType(mockOwningType);
    }

    @Test
    public void testConstructor() {
        MaxExclusive facet = new MaxExclusive("50");
        assertNotNull(facet);
        assertEquals("50", facet.getValue());
        assertEquals(Facet.MAX_EXCLUSIVE, facet.getName());
    }

    @Test
    public void testConstructorWithZero() {
        MaxExclusive facet = new MaxExclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    @Test
    public void testConstructorWithNegativeValue() {
        MaxExclusive facet = new MaxExclusive("-100");
        assertNotNull(facet);
        assertEquals("-100", facet.getValue());
    }

    @Test
    public void testConstructorWithDecimalValue() {
        MaxExclusive facet = new MaxExclusive("3.14");
        assertNotNull(facet);
        assertEquals("3.14", facet.getValue());
    }

    @Test
    public void testOverridesBaseMaxExclusive() {
        Facet maxExclusiveFacet = new MaxExclusive("100");
        assertTrue(maxExclusive.overridesBase(maxExclusiveFacet));
    }

    @Test
    public void testOverridesBaseMaxInclusive() {
        Facet maxInclusiveFacet = new MaxInclusive("100");
        assertTrue(maxExclusive.overridesBase(maxInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMinInclusive() {
        Facet minInclusiveFacet = new MinInclusive("10");
        assertFalse(maxExclusive.overridesBase(minInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMinExclusive() {
        Facet minExclusiveFacet = new MinExclusive("10");
        assertFalse(maxExclusive.overridesBase(minExclusiveFacet));
    }

    @Test
    public void testCheckConstraintsWithSelfReference() throws SchemaException {
        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExclusive);
        when(mockOwningType.isNumericType()).thenReturn(true);

        // Should not throw exception when checking against self
        maxExclusive.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMinExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("100");
        maxExcl.setOwningType(mockOwningType);

        MinExclusive minExcl = new MinExclusive("10");
        minExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);
        localFacets.add(minExcl);

        // Should not throw - maxExclusive (100) > minExclusive (10)
        maxExcl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMinExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("10");
        maxExcl.setOwningType(mockOwningType);

        MinExclusive minExcl = new MinExclusive("10");
        minExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);
        localFacets.add(minExcl);

        // Should throw - maxExclusive (10) <= minExclusive (10)
        try {
            maxExcl.checkConstraints(localFacets.elements(), null);
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(
                e.getMessage().contains("maxExclusive") &&
                    e.getMessage().contains("minExclusive")
            );
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("50");
        maxExcl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("100");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should not throw - maxExclusive (50) <= baseMaxExclusive (100)
        maxExcl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("150");
        maxExcl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("100");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should throw - maxExclusive (150) > baseMaxExclusive (100)
        try {
            maxExcl.checkConstraints(
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

        MaxExclusive maxExcl = new MaxExclusive("100");
        maxExcl.setOwningType(mockOwningType);

        MinInclusive baseMinIncl = new MinInclusive("50");
        baseMinIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinIncl);

        // Should not throw - maxExclusive (100) > baseMinInclusive (50)
        maxExcl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMinInclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("30");
        maxExcl.setOwningType(mockOwningType);

        MinInclusive baseMinIncl = new MinInclusive("50");
        baseMinIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinIncl);

        // Should throw - maxExclusive (30) <= baseMinInclusive (50)
        try {
            maxExcl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than or equal"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("100");
        maxExcl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("50");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should not throw - maxExclusive (100) > baseMinExclusive (50)
        maxExcl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("50");
        maxExcl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("50");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should throw - maxExclusive (50) <= baseMinExclusive (50)
        try {
            maxExcl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than or equal"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveValid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("100");
        maxExcl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("100");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should not throw - maxExclusive (100) >= baseMaxInclusive (100)
        maxExcl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveInvalid() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("50");
        maxExcl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("100");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should throw - maxExclusive (50) < baseMaxInclusive (100)
        try {
            maxExcl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than"));
        }
    }

    @Test
    public void testCheckConstraintsNonNumericType() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(false);

        MaxExclusive maxExcl = new MaxExclusive("100");
        maxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        // Should not throw for non-numeric types
        maxExcl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithNullBaseFacets() throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MaxExclusive maxExcl = new MaxExclusive("100");
        maxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(maxExcl);

        // Should not throw with null base facets
        maxExcl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testGetNameReturnsMaxExclusive() {
        assertEquals(Facet.MAX_EXCLUSIVE, maxExclusive.getName());
    }
}
