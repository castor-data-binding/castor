package org.exolab.castor.xml.schema.facets;

import java.util.Vector;
import junit.framework.TestCase;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SchemaException;

public class FacetTest extends TestCase {

    public void testMaxInclusiveConstruction() {
        MaxInclusive facet = new MaxInclusive("100");
        assertNotNull(facet);
        assertEquals(Facet.MAX_INCLUSIVE, facet.getName());
        assertEquals("100", facet.getValue());
    }

    public void testMaxInclusiveOverridesBase() {
        MaxInclusive facet = new MaxInclusive("100");
        MaxExclusive baseFacet = new MaxExclusive("150");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMaxInclusiveOverridesBaseMaxInclusive() {
        MaxInclusive facet = new MaxInclusive("100");
        MaxInclusive baseFacet = new MaxInclusive("200");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMaxInclusiveNotOverridesBase() {
        MaxInclusive facet = new MaxInclusive("100");
        MinExclusive baseFacet = new MinExclusive("50");
        assertFalse(facet.overridesBase(baseFacet));
    }

    public void testMaxExclusiveConstruction() {
        MaxExclusive facet = new MaxExclusive("100");
        assertNotNull(facet);
        assertEquals(Facet.MAX_EXCLUSIVE, facet.getName());
        assertEquals("100", facet.getValue());
    }

    public void testMaxExclusiveOverridesBase() {
        MaxExclusive facet = new MaxExclusive("100");
        MaxInclusive baseFacet = new MaxInclusive("150");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMaxExclusiveOverridesBaseMaxExclusive() {
        MaxExclusive facet = new MaxExclusive("100");
        MaxExclusive baseFacet = new MaxExclusive("200");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMaxExclusiveNotOverridesBase() {
        MaxExclusive facet = new MaxExclusive("100");
        MinInclusive baseFacet = new MinInclusive("50");
        assertFalse(facet.overridesBase(baseFacet));
    }

    public void testMinInclusiveConstruction() {
        MinInclusive facet = new MinInclusive("10");
        assertNotNull(facet);
        assertEquals(Facet.MIN_INCLUSIVE, facet.getName());
        assertEquals("10", facet.getValue());
    }

    public void testMinInclusiveOverridesBase() {
        MinInclusive facet = new MinInclusive("10");
        MinExclusive baseFacet = new MinExclusive("5");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMinInclusiveOverridesBaseMinInclusive() {
        MinInclusive facet = new MinInclusive("10");
        MinInclusive baseFacet = new MinInclusive("5");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMinInclusiveNotOverridesBase() {
        MinInclusive facet = new MinInclusive("10");
        MaxExclusive baseFacet = new MaxExclusive("100");
        assertFalse(facet.overridesBase(baseFacet));
    }

    public void testMinExclusiveConstruction() {
        MinExclusive facet = new MinExclusive("10");
        assertNotNull(facet);
        assertEquals(Facet.MIN_EXCLUSIVE, facet.getName());
        assertEquals("10", facet.getValue());
    }

    public void testMinExclusiveOverridesBase() {
        MinExclusive facet = new MinExclusive("10");
        MinInclusive baseFacet = new MinInclusive("5");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMinExclusiveOverridesBaseMinExclusive() {
        MinExclusive facet = new MinExclusive("10");
        MinExclusive baseFacet = new MinExclusive("5");
        assertTrue(facet.overridesBase(baseFacet));
    }

    public void testMinExclusiveNotOverridesBase() {
        MinExclusive facet = new MinExclusive("10");
        MaxInclusive baseFacet = new MaxInclusive("100");
        assertFalse(facet.overridesBase(baseFacet));
    }

    public void testMaxInclusiveWithZeroValue() {
        MaxInclusive facet = new MaxInclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    public void testMaxInclusiveWithNegativeValue() {
        MaxInclusive facet = new MaxInclusive("-50");
        assertNotNull(facet);
        assertEquals("-50", facet.getValue());
    }

    public void testMaxExclusiveWithZeroValue() {
        MaxExclusive facet = new MaxExclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    public void testMaxExclusiveWithNegativeValue() {
        MaxExclusive facet = new MaxExclusive("-50");
        assertNotNull(facet);
        assertEquals("-50", facet.getValue());
    }

    public void testMinInclusiveWithZeroValue() {
        MinInclusive facet = new MinInclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    public void testMinInclusiveWithNegativeValue() {
        MinInclusive facet = new MinInclusive("-50");
        assertNotNull(facet);
        assertEquals("-50", facet.getValue());
    }

    public void testMinExclusiveWithZeroValue() {
        MinExclusive facet = new MinExclusive("0");
        assertNotNull(facet);
        assertEquals("0", facet.getValue());
    }

    public void testMinExclusiveWithNegativeValue() {
        MinExclusive facet = new MinExclusive("-50");
        assertNotNull(facet);
        assertEquals("-50", facet.getValue());
    }

    public void testMaxInclusiveWithDecimalValue() {
        MaxInclusive facet = new MaxInclusive("99.99");
        assertNotNull(facet);
        assertEquals("99.99", facet.getValue());
    }

    public void testMaxExclusiveWithDecimalValue() {
        MaxExclusive facet = new MaxExclusive("99.99");
        assertNotNull(facet);
        assertEquals("99.99", facet.getValue());
    }

    public void testMinInclusiveWithDecimalValue() {
        MinInclusive facet = new MinInclusive("0.01");
        assertNotNull(facet);
        assertEquals("0.01", facet.getValue());
    }

    public void testMinExclusiveWithDecimalValue() {
        MinExclusive facet = new MinExclusive("0.01");
        assertNotNull(facet);
        assertEquals("0.01", facet.getValue());
    }

    public void testFacetNames() {
        assertEquals("maxInclusive", Facet.MAX_INCLUSIVE);
        assertEquals("maxExclusive", Facet.MAX_EXCLUSIVE);
        assertEquals("minInclusive", Facet.MIN_INCLUSIVE);
        assertEquals("minExclusive", Facet.MIN_EXCLUSIVE);
    }

    public void testMaxInclusiveGetName() {
        MaxInclusive facet = new MaxInclusive("100");
        assertEquals(Facet.MAX_INCLUSIVE, facet.getName());
    }

    public void testMaxExclusiveGetName() {
        MaxExclusive facet = new MaxExclusive("100");
        assertEquals(Facet.MAX_EXCLUSIVE, facet.getName());
    }

    public void testMinInclusiveGetName() {
        MinInclusive facet = new MinInclusive("10");
        assertEquals(Facet.MIN_INCLUSIVE, facet.getName());
    }

    public void testMinExclusiveGetName() {
        MinExclusive facet = new MinExclusive("10");
        assertEquals(Facet.MIN_EXCLUSIVE, facet.getName());
    }

    public void testMaxInclusiveGetValue() {
        String value = "100";
        MaxInclusive facet = new MaxInclusive(value);
        assertEquals(value, facet.getValue());
    }

    public void testMaxExclusiveGetValue() {
        String value = "100";
        MaxExclusive facet = new MaxExclusive(value);
        assertEquals(value, facet.getValue());
    }

    public void testMinInclusiveGetValue() {
        String value = "10";
        MinInclusive facet = new MinInclusive(value);
        assertEquals(value, facet.getValue());
    }

    public void testMinExclusiveGetValue() {
        String value = "10";
        MinExclusive facet = new MinExclusive(value);
        assertEquals(value, facet.getValue());
    }

    public void testMaxInclusiveWithLargeValue() {
        MaxInclusive facet = new MaxInclusive("999999999999");
        assertNotNull(facet);
    }

    public void testMaxExclusiveWithLargeValue() {
        MaxExclusive facet = new MaxExclusive("999999999999");
        assertNotNull(facet);
    }

    public void testMinInclusiveWithLargeValue() {
        MinInclusive facet = new MinInclusive("999999999999");
        assertNotNull(facet);
    }

    public void testMinExclusiveWithLargeValue() {
        MinExclusive facet = new MinExclusive("999999999999");
        assertNotNull(facet);
    }
}
