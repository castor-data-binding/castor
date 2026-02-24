package org.exolab.castor.xml.schema.facets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SimpleType;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test cases for MinInclusive facet class
 */
public class MinInclusiveTest {

    private MinInclusive minInclusive;
    private SimpleType mockOwningType;

    @Before
    public void setUp() {
        mockOwningType = mock(SimpleType.class);
        minInclusive = new MinInclusive("5");
        minInclusive.setOwningType(mockOwningType);
    }

    @Test
    public void testConstructor() {
        MinInclusive facet = new MinInclusive("10");
        assertNotNull(facet);
        assertEquals("10", facet.getValue());
        assertEquals(Facet.MIN_INCLUSIVE, facet.getName());
    }

    @Test
    public void testConstructorWithZero() {
        MinInclusive facet = new MinInclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    @Test
    public void testConstructorWithNegativeValue() {
        MinInclusive facet = new MinInclusive("-100");
        assertNotNull(facet);
        assertEquals("-100", facet.getValue());
    }

    @Test
    public void testConstructorWithDecimalValue() {
        MinInclusive facet = new MinInclusive("3.14");
        assertNotNull(facet);
        assertEquals("3.14", facet.getValue());
    }

    @Test
    public void testOverridesBaseMinExclusive() {
        Facet minExclusiveFacet = new MinExclusive("5");
        assertTrue(minInclusive.overridesBase(minExclusiveFacet));
    }

    @Test
    public void testOverridesBaseMinInclusive() {
        Facet minInclusiveFacet = new MinInclusive("3");
        assertTrue(minInclusive.overridesBase(minInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMaxInclusive() {
        Facet maxInclusiveFacet = new MaxInclusive("10");
        assertFalse(minInclusive.overridesBase(maxInclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideMaxExclusive() {
        Facet maxExclusiveFacet = new MaxExclusive("10");
        assertFalse(minInclusive.overridesBase(maxExclusiveFacet));
    }

    @Test
    public void testDoesNotOverrideOtherFacets() {
        Facet patternFacet = mock(Facet.class);
        when(patternFacet.getName()).thenReturn(Facet.PATTERN);
        assertFalse(minInclusive.overridesBase(patternFacet));
    }

    @Test
    public void testCheckConstraintsWithSelfReference() throws SchemaException {
        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minInclusive);
        when(mockOwningType.isNumericType()).thenReturn(true);

        // Should not throw exception when checking against self
        minInclusive.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMaxExclusiveValid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("5");
        minIncl.setOwningType(mockOwningType);

        MaxExclusive maxExcl = new MaxExclusive("10");
        maxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);
        localFacets.add(maxExcl);

        // Should not throw - minInclusive (5) < maxExclusive (10)
        minIncl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithMaxExclusiveInvalid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("10");
        minIncl.setOwningType(mockOwningType);

        MaxExclusive maxExcl = new MaxExclusive("10");
        maxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);
        localFacets.add(maxExcl);

        // Should throw - minInclusive (10) >= maxExclusive (10)
        try {
            minIncl.checkConstraints(localFacets.elements(), null);
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(
                e.getMessage().contains("minInclusive") &&
                    e.getMessage().contains("maxExclusive")
            );
        }
    }

    @Test
    public void testCheckConstraintsBaseMinInclusiveValid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("10");
        minIncl.setOwningType(mockOwningType);

        MinInclusive baseMinIncl = new MinInclusive("5");
        baseMinIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinIncl);

        // Should not throw - minInclusive (10) >= baseMinInclusive (5)
        minIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMinInclusiveInvalid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("3");
        minIncl.setOwningType(mockOwningType);

        MinInclusive baseMinIncl = new MinInclusive("5");
        baseMinIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinIncl);

        // Should throw - minInclusive (3) < baseMinInclusive (5)
        try {
            minIncl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveValid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("5");
        minIncl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("10");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should not throw - minInclusive (5) <= baseMaxInclusive (10)
        minIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxInclusiveInvalid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("15");
        minIncl.setOwningType(mockOwningType);

        MaxInclusive baseMaxIncl = new MaxInclusive("10");
        baseMaxIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxIncl);

        // Should throw - minInclusive (15) > baseMaxInclusive (10)
        try {
            minIncl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("greater than"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveValid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("10");
        minIncl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("5");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should not throw - minInclusive (10) > baseMinExclusive (5)
        minIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMinExclusiveInvalid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("5");
        minIncl.setOwningType(mockOwningType);

        MinExclusive baseMinExcl = new MinExclusive("5");
        baseMinExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMinExcl);

        // Should throw - minInclusive (5) <= baseMinExclusive (5)
        try {
            minIncl.checkConstraints(
                localFacets.elements(),
                baseFacets.elements()
            );
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(e.getMessage().contains("less than or equal"));
        }
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveValid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("5");
        minIncl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("10");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should not throw - minInclusive (5) < baseMaxExclusive (10)
        minIncl.checkConstraints(localFacets.elements(), baseFacets.elements());
    }

    @Test
    public void testCheckConstraintsBaseMaxExclusiveInvalid()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("10");
        minIncl.setOwningType(mockOwningType);

        MaxExclusive baseMaxExcl = new MaxExclusive("10");
        baseMaxExcl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        Vector<Facet> baseFacets = new Vector<>();
        baseFacets.add(baseMaxExcl);

        // Should throw - minInclusive (10) >= baseMaxExclusive (10)
        try {
            minIncl.checkConstraints(
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

        MinInclusive minIncl = new MinInclusive("5");
        minIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        // Should not throw for non-numeric types
        minIncl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testCheckConstraintsWithNullBaseFacets()
        throws SchemaException {
        when(mockOwningType.isNumericType()).thenReturn(true);

        MinInclusive minIncl = new MinInclusive("5");
        minIncl.setOwningType(mockOwningType);

        Vector<Facet> localFacets = new Vector<>();
        localFacets.add(minIncl);

        // Should not throw with null base facets
        minIncl.checkConstraints(localFacets.elements(), null);
    }

    @Test
    public void testGetNameReturnsMinInclusive() {
        assertEquals(Facet.MIN_INCLUSIVE, minInclusive.getName());
    }
}
