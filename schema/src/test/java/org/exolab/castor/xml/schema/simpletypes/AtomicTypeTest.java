package org.exolab.castor.xml.schema.simpletypes;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SchemaException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for AtomicType class covering all methods and branches
 */
public class AtomicTypeTest {

  private AtomicType atomicType;

  @Before
  public void setUp() {
    // Create a concrete implementation of AtomicType for testing
    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }
    };
  }

  @Test
  public void testGetPatternWhenPatternFacetExists() {
    String patternValue = "[a-z]+";
    Facet patternFacet = mock(Facet.class);
    when(patternFacet.getValue()).thenReturn(patternValue);

    // Mock the parent getFacet method behavior
    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        if (Facet.PATTERN.equals(name)) {
          return patternFacet;
        }
        return null;
      }
    };

    String result = atomicType.getPattern();
    assertEquals(patternValue, result);
  }

  @Test
  public void testGetPatternWhenPatternFacetDoesNotExist() {
    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        return null;
      }
    };

    String result = atomicType.getPattern();
    assertNull(result);
  }

  @Test
  public void testIsMinInclusiveWhenMinInclusiveExists() {
    Facet minInclusiveFacet = mock(Facet.class);

    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        if (Facet.MIN_INCLUSIVE.equals(name)) {
          return minInclusiveFacet;
        }
        return null;
      }
    };

    assertTrue(atomicType.isMinInclusive());
  }

  @Test
  public void testIsMinInclusiveWhenMinInclusiveDoesNotExist() {
    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        return null;
      }
    };

    assertFalse(atomicType.isMinInclusive());
  }

  @Test
  public void testIsMaxInclusiveWhenMaxInclusiveExists() {
    Facet maxInclusiveFacet = mock(Facet.class);

    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        if (Facet.MAX_INCLUSIVE.equals(name)) {
          return maxInclusiveFacet;
        }
        return null;
      }
    };

    assertTrue(atomicType.isMaxInclusive());
  }

  @Test
  public void testIsMaxInclusiveWhenMaxInclusiveDoesNotExist() {
    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        return null;
      }
    };

    assertFalse(atomicType.isMaxInclusive());
  }

  @Test
  public void testGetMinReturnsMinInclusiveFacet() {
    Facet minInclusiveFacet = mock(Facet.class);

    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        if (Facet.MIN_INCLUSIVE.equals(name)) {
          return minInclusiveFacet;
        }
        return null;
      }
    };

    Facet result = atomicType.getMin();
    assertEquals(minInclusiveFacet, result);
  }

  @Test
  public void testGetMinReturnsMinExclusiveFacetWhenMinInclusiveDoesNotExist() {
    Facet minExclusiveFacet = mock(Facet.class);

    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        if (Facet.MIN_EXCLUSIVE.equals(name)) {
          return minExclusiveFacet;
        }
        return null;
      }
    };

    Facet result = atomicType.getMin();
    assertEquals(minExclusiveFacet, result);
  }

  @Test
  public void testGetMinReturnsNullWhenNoMinFacetExists() {
    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        return null;
      }
    };

    Facet result = atomicType.getMin();
    assertNull(result);
  }

  @Test
  public void testGetMaxReturnsMaxInclusiveFacet() {
    Facet maxInclusiveFacet = mock(Facet.class);

    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        if (Facet.MAX_INCLUSIVE.equals(name)) {
          return maxInclusiveFacet;
        }
        return null;
      }
    };

    Facet result = atomicType.getMax();
    assertEquals(maxInclusiveFacet, result);
  }

  @Test
  public void testGetMaxReturnsMaxExclusiveFacetWhenMaxInclusiveDoesNotExist() {
    Facet maxExclusiveFacet = mock(Facet.class);

    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        if (Facet.MAX_EXCLUSIVE.equals(name)) {
          return maxExclusiveFacet;
        }
        return null;
      }
    };

    Facet result = atomicType.getMax();
    assertEquals(maxExclusiveFacet, result);
  }

  @Test
  public void testGetMaxReturnsNullWhenNoMaxFacetExists() {
    atomicType = new AtomicType() {
      @Override
      public String getName() {
        return "TestAtomicType";
      }

      @Override
      public Facet getFacet(String name) {
        return null;
      }
    };

    Facet result = atomicType.getMax();
    assertNull(result);
  }
}
