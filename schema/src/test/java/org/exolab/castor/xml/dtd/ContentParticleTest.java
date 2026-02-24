package org.exolab.castor.xml.dtd;

import junit.framework.TestCase;
import java.util.Enumeration;

/**
 * Comprehensive test coverage for DTD ContentParticle class
 */
public class ContentParticleTest extends TestCase {

    // Constructor Tests
    public void testConstructorNoArgs() {
        ContentParticle cp = new ContentParticle();
        assertNotNull(cp);
        assertTrue(cp.isOneOccurance());
    }

    public void testConstructorWithReference() {
        ContentParticle cp = new ContentParticle("child");
        assertNotNull(cp);
        assertTrue(cp.isReferenceType());
        assertEquals("child", cp.getReference());
        assertTrue(cp.isOneOccurance());
    }

    public void testConstructorWithNullReference() {
        try {
            new ContentParticle(null);
            fail("Should throw IllegalArgumentException for null reference");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("must not be empty"));
        }
    }

    public void testConstructorWithEmptyReference() {
        try {
            new ContentParticle("");
            fail("Should throw IllegalArgumentException for empty reference");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("must not be empty"));
        }
    }

    // Reference Type Tests
    public void testSetReferenceType() {
        ContentParticle cp = new ContentParticle();
        cp.setReferenceType("element");
        assertTrue(cp.isReferenceType());
        assertEquals("element", cp.getReference());
    }

    public void testSetReferenceTypeWithNullName() {
        ContentParticle cp = new ContentParticle();
        try {
            cp.setReferenceType(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("must not be empty"));
        }
    }

    public void testSetReferenceTypeWithEmptyName() {
        ContentParticle cp = new ContentParticle();
        try {
            cp.setReferenceType("");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("must not be empty"));
        }
    }

    public void testIsReferenceTypeFalse() {
        ContentParticle cp = new ContentParticle();
        assertFalse(cp.isReferenceType());
    }

    public void testGetReferenceNull() {
        ContentParticle cp = new ContentParticle();
        assertNull(cp.getReference());
    }

    // Sequence Type Tests
    public void testSetSeqType() {
        ContentParticle cp = new ContentParticle();
        cp.setSeqType();
        assertTrue(cp.isSeqType());
        assertFalse(cp.isReferenceType());
        assertFalse(cp.isChoiceType());
    }

    public void testIsSeqTypeFalse() {
        ContentParticle cp = new ContentParticle();
        assertFalse(cp.isSeqType());
    }

    // Choice Type Tests
    public void testSetChoiceType() {
        ContentParticle cp = new ContentParticle();
        cp.setChoiceType();
        assertTrue(cp.isChoiceType());
        assertFalse(cp.isReferenceType());
        assertFalse(cp.isSeqType());
    }

    public void testIsChoiceTypeFalse() {
        ContentParticle cp = new ContentParticle();
        assertFalse(cp.isChoiceType());
    }

    // Occurrence Tests - ONE
    public void testSetOneOccurance() {
        ContentParticle cp = new ContentParticle();
        cp.setOneOccurance();
        assertTrue(cp.isOneOccurance());
        assertFalse(cp.isZeroOrOneOccurance());
        assertFalse(cp.isOneOrMoreOccurances());
        assertFalse(cp.isZeroOrMoreOccurances());
    }

    public void testIsOneOccuranceDefault() {
        ContentParticle cp = new ContentParticle();
        assertTrue(cp.isOneOccurance());
    }

    // Occurrence Tests - ZERO_OR_ONE
    public void testSetZeroOrOneOccurance() {
        ContentParticle cp = new ContentParticle();
        cp.setZeroOrOneOccurance();
        assertTrue(cp.isZeroOrOneOccurance());
        assertFalse(cp.isOneOccurance());
        assertFalse(cp.isOneOrMoreOccurances());
        assertFalse(cp.isZeroOrMoreOccurances());
    }

    public void testIsZeroOrOneOccuranceFalse() {
        ContentParticle cp = new ContentParticle();
        assertFalse(cp.isZeroOrOneOccurance());
    }

    // Occurrence Tests - ONE_OR_MORE
    public void testSetOneOrMoreOccurances() {
        ContentParticle cp = new ContentParticle();
        cp.setOneOrMoreOccurances();
        assertTrue(cp.isOneOrMoreOccurances());
        assertFalse(cp.isOneOccurance());
        assertFalse(cp.isZeroOrOneOccurance());
        assertFalse(cp.isZeroOrMoreOccurances());
    }

    public void testIsOneOrMoreOccurancesFalse() {
        ContentParticle cp = new ContentParticle();
        assertFalse(cp.isOneOrMoreOccurances());
    }

    // Occurrence Tests - ZERO_OR_MORE
    public void testSetZeroOrMoreOccurances() {
        ContentParticle cp = new ContentParticle();
        cp.setZeroOrMoreOccurances();
        assertTrue(cp.isZeroOrMoreOccurances());
        assertFalse(cp.isOneOccurance());
        assertFalse(cp.isZeroOrOneOccurance());
        assertFalse(cp.isOneOrMoreOccurances());
    }

    public void testIsZeroOrMoreOccurancesFalse() {
        ContentParticle cp = new ContentParticle();
        assertFalse(cp.isZeroOrMoreOccurances());
    }

    // Children Tests - Sequence
    public void testAddChildToSeq() {
        ContentParticle parent = new ContentParticle();
        parent.setSeqType();
        ContentParticle child = new ContentParticle("child1");
        parent.addChild(child);

        Enumeration<ContentParticle> children = parent.getChildren();
        assertNotNull(children);
        assertTrue(children.hasMoreElements());
        assertEquals(child, children.nextElement());
    }

    public void testAddMultipleChildrenToSeq() {
        ContentParticle parent = new ContentParticle();
        parent.setSeqType();
        ContentParticle child1 = new ContentParticle("child1");
        ContentParticle child2 = new ContentParticle("child2");
        ContentParticle child3 = new ContentParticle("child3");

        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);

        Enumeration<ContentParticle> children = parent.getChildren();
        assertNotNull(children);
        int count = 0;
        while (children.hasMoreElements()) {
            children.nextElement();
            count++;
        }
        assertEquals(3, count);
    }

    public void testGetChildrenSeq() {
        ContentParticle parent = new ContentParticle();
        parent.setSeqType();
        ContentParticle child = new ContentParticle("child");
        parent.addChild(child);

        Enumeration<ContentParticle> children = parent.getChildren();
        assertNotNull(children);
    }

    // Children Tests - Choice
    public void testAddChildToChoice() {
        ContentParticle parent = new ContentParticle();
        parent.setChoiceType();
        ContentParticle child = new ContentParticle("choice1");
        parent.addChild(child);

        Enumeration<ContentParticle> children = parent.getChildren();
        assertNotNull(children);
        assertTrue(children.hasMoreElements());
    }

    public void testAddMultipleChildrenToChoice() {
        ContentParticle parent = new ContentParticle();
        parent.setChoiceType();
        for (int i = 1; i <= 5; i++) {
            parent.addChild(new ContentParticle("choice" + i));
        }

        Enumeration<ContentParticle> children = parent.getChildren();
        int count = 0;
        while (children.hasMoreElements()) {
            children.nextElement();
            count++;
        }
        assertEquals(5, count);
    }

    public void testGetChildrenChoice() {
        ContentParticle parent = new ContentParticle();
        parent.setChoiceType();
        parent.addChild(new ContentParticle("opt1"));
        parent.addChild(new ContentParticle("opt2"));

        Enumeration<ContentParticle> children = parent.getChildren();
        assertNotNull(children);
    }

    // Children Tests - Non-Seq/Choice
    public void testGetChildrenReference() {
        ContentParticle cp = new ContentParticle("elem");
        assertNull(cp.getChildren());
    }

    public void testGetChildrenWithNoType() {
        ContentParticle cp = new ContentParticle();
        assertNull(cp.getChildren());
    }

    // Complex Scenarios
    public void testReferenceWithOccurances() {
        ContentParticle cp = new ContentParticle("element");
        cp.setZeroOrMoreOccurances();

        assertTrue(cp.isReferenceType());
        assertEquals("element", cp.getReference());
        assertTrue(cp.isZeroOrMoreOccurances());
    }

    public void testSeqWithMixedOccurances() {
        ContentParticle parent = new ContentParticle();
        parent.setSeqType();

        ContentParticle child1 = new ContentParticle("first");
        child1.setOneOccurance();
        parent.addChild(child1);

        ContentParticle child2 = new ContentParticle("second");
        child2.setZeroOrOneOccurance();
        parent.addChild(child2);

        ContentParticle child3 = new ContentParticle("third");
        child3.setZeroOrMoreOccurances();
        parent.addChild(child3);

        assertTrue(parent.isSeqType());
        Enumeration<ContentParticle> children = parent.getChildren();
        int count = 0;
        while (children.hasMoreElements()) {
            children.nextElement();
            count++;
        }
        assertEquals(3, count);
    }

    public void testNestedContentParticles() {
        ContentParticle root = new ContentParticle();
        root.setSeqType();

        ContentParticle sub1 = new ContentParticle();
        sub1.setChoiceType();
        sub1.addChild(new ContentParticle("opt1"));
        sub1.addChild(new ContentParticle("opt2"));

        ContentParticle sub2 = new ContentParticle();
        sub2.setSeqType();
        sub2.addChild(new ContentParticle("elem1"));
        sub2.addChild(new ContentParticle("elem2"));

        root.addChild(sub1);
        root.addChild(sub2);

        Enumeration<ContentParticle> children = root.getChildren();
        int count = 0;
        while (children.hasMoreElements()) {
            children.nextElement();
            count++;
        }
        assertEquals(2, count);
    }

    public void testTypeTransition() {
        ContentParticle cp = new ContentParticle();
        cp.setReferenceType("elem");
        assertTrue(cp.isReferenceType());

        cp.setSeqType();
        assertTrue(cp.isSeqType());
        assertFalse(cp.isReferenceType());

        cp.setChoiceType();
        assertTrue(cp.isChoiceType());
        assertFalse(cp.isSeqType());
    }

    public void testOccuranceTransition() {
        ContentParticle cp = new ContentParticle();
        assertTrue(cp.isOneOccurance());

        cp.setZeroOrOneOccurance();
        assertTrue(cp.isZeroOrOneOccurance());
        assertFalse(cp.isOneOccurance());

        cp.setOneOrMoreOccurances();
        assertTrue(cp.isOneOrMoreOccurances());
        assertFalse(cp.isZeroOrOneOccurance());

        cp.setZeroOrMoreOccurances();
        assertTrue(cp.isZeroOrMoreOccurances());
        assertFalse(cp.isOneOrMoreOccurances());

        cp.setOneOccurance();
        assertTrue(cp.isOneOccurance());
        assertFalse(cp.isZeroOrMoreOccurances());
    }

    public void testChoiceWithManyChildren() {
        ContentParticle choice = new ContentParticle();
        choice.setChoiceType();

        for (int i = 1; i <= 10; i++) {
            choice.addChild(new ContentParticle("option" + i));
        }

        Enumeration<ContentParticle> children = choice.getChildren();
        int count = 0;
        while (children.hasMoreElements()) {
            children.nextElement();
            count++;
        }
        assertEquals(10, count);
    }

    public void testSeqWithChildren() {
        ContentParticle seq = new ContentParticle();
        seq.setSeqType();

        ContentParticle child1 = new ContentParticle("first");
        ContentParticle child2 = new ContentParticle("second");

        seq.addChild(child1);
        seq.addChild(child2);

        Enumeration<ContentParticle> children = seq.getChildren();
        assertNotNull(children);
        int count = 0;
        while (children.hasMoreElements()) {
            children.nextElement();
            count++;
        }
        assertEquals(2, count);
    }

    public void testReferenceNameWithSpecialChars() {
        ContentParticle cp = new ContentParticle("my-element_123");
        assertTrue(cp.isReferenceType());
        assertEquals("my-element_123", cp.getReference());
    }

    public void testEmptySeqType() {
        ContentParticle seq = new ContentParticle();
        seq.setSeqType();
        Enumeration<ContentParticle> children = seq.getChildren();
        assertNotNull(children);
        assertFalse(children.hasMoreElements());
    }

    public void testEmptyChoiceType() {
        ContentParticle choice = new ContentParticle();
        choice.setChoiceType();
        Enumeration<ContentParticle> children = choice.getChildren();
        assertNotNull(children);
        assertFalse(children.hasMoreElements());
    }

    public void testComplexHierarchy() {
        ContentParticle root = new ContentParticle();
        root.setChoiceType();
        root.setOneOrMoreOccurances();

        ContentParticle seq = new ContentParticle();
        seq.setSeqType();
        seq.setZeroOrOneOccurance();

        seq.addChild(new ContentParticle("a"));
        seq.addChild(new ContentParticle("b"));
        seq.addChild(new ContentParticle("c"));

        root.addChild(seq);
        root.addChild(new ContentParticle("alternative"));

        assertTrue(root.isChoiceType());
        assertTrue(root.isOneOrMoreOccurances());

        Enumeration<ContentParticle> children = root.getChildren();
        int count = 0;
        while (children.hasMoreElements()) {
            children.nextElement();
            count++;
        }
        assertEquals(2, count);
    }

}
