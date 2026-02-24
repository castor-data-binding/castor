package org.exolab.castor.xml.schema;

import junit.framework.TestCase;

public class SchemaPrimitiveTest extends TestCase {

    public void testFormQualified() {
        Form form = Form.Qualified;
        assertNotNull(form);
        assertEquals(Form.QUALIFIED_VALUE, form.toString());
    }

    public void testFormUnqualified() {
        Form form = Form.Unqualified;
        assertNotNull(form);
        assertEquals(Form.UNQUALIFIED_VALUE, form.toString());
    }

    public void testFormQualifiedValue() {
        assertEquals("qualified", Form.QUALIFIED_VALUE);
    }

    public void testFormUnqualifiedValue() {
        assertEquals("unqualified", Form.UNQUALIFIED_VALUE);
    }

    public void testFormToStringQualified() {
        Form form = Form.Qualified;
        String str = form.toString();
        assertNotNull(str);
        assertTrue(str.equals("qualified") || str.contains("qualified"));
    }

    public void testFormToStringUnqualified() {
        Form form = Form.Unqualified;
        String str = form.toString();
        assertNotNull(str);
        assertTrue(str.equals("unqualified") || str.contains("unqualified"));
    }

    public void testFormQualifiedEquals() {
        Form form1 = Form.Qualified;
        Form form2 = Form.Qualified;
        assertEquals(form1, form2);
    }

    public void testFormUnqualifiedEquals() {
        Form form1 = Form.Unqualified;
        Form form2 = Form.Unqualified;
        assertEquals(form1, form2);
    }

    public void testFormQualifiedNotEqualsUnqualified() {
        Form form1 = Form.Qualified;
        Form form2 = Form.Unqualified;
        assertNotSame(form1, form2);
    }

    public void testBlockListConstruction() {
        BlockList list = new BlockList();
        assertNotNull(list);
    }

    public void testBlockListConstructionWithString() {
        BlockList list = new BlockList("restriction extension");
        assertNotNull(list);
    }

    public void testBlockListHasAll() {
        BlockList list = new BlockList(BlockList.ALL);
        assertTrue(list.hasAll());
    }

    public void testBlockListHasRestriction() {
        BlockList list = new BlockList(BlockList.RESTRICTION);
        assertTrue(list.hasRestriction());
    }

    public void testBlockListHasExtension() {
        BlockList list = new BlockList(BlockList.EXTENSION);
        assertTrue(list.hasExtension());
    }

    public void testBlockListHasSubstitution() {
        BlockList list = new BlockList(BlockList.SUBSTITUTION);
        assertTrue(list.hasSubstitution());
    }

    public void testBlockListMultipleValues() {
        BlockList list = new BlockList("restriction extension");
        assertTrue(list.hasRestriction());
        assertTrue(list.hasExtension());
    }

    public void testBlockListHashCode() {
        BlockList list1 = new BlockList(BlockList.RESTRICTION);
        BlockList list2 = new BlockList(BlockList.RESTRICTION);
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    public void testBlockListToString() {
        BlockList list = new BlockList(BlockList.RESTRICTION);
        String str = list.toString();
        assertNotNull(str);
    }

    public void testBlockListEmpty() {
        BlockList list = new BlockList();
        assertFalse(list.hasRestriction());
        assertFalse(list.hasExtension());
        assertFalse(list.hasSubstitution());
        assertFalse(list.hasAll());
    }

    public void testFinalListConstruction() {
        FinalList list = new FinalList();
        assertNotNull(list);
    }

    public void testFinalListConstructionWithString() {
        FinalList list = new FinalList("restriction extension");
        assertNotNull(list);
    }

    public void testFinalListHasAll() {
        FinalList list = new FinalList(FinalList.ALL);
        assertTrue(list.hasAll());
    }

    public void testFinalListHasRestriction() {
        FinalList list = new FinalList(FinalList.RESTRICTION);
        assertTrue(list.hasRestriction());
    }

    public void testFinalListHasExtension() {
        FinalList list = new FinalList(FinalList.EXTENSION);
        assertTrue(list.hasExtension());
    }

    public void testFinalListMultipleValues() {
        FinalList list = new FinalList("restriction extension");
        assertTrue(list.hasRestriction());
        assertTrue(list.hasExtension());
    }

    public void testFinalListHashCode() {
        FinalList list1 = new FinalList(FinalList.RESTRICTION);
        FinalList list2 = new FinalList(FinalList.RESTRICTION);
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    public void testFinalListToString() {
        FinalList list = new FinalList(FinalList.RESTRICTION);
        String str = list.toString();
        assertNotNull(str);
    }

    public void testFinalListEmpty() {
        FinalList list = new FinalList();
        assertFalse(list.hasRestriction());
        assertFalse(list.hasExtension());
        assertFalse(list.hasAll());
    }

    public void testBlockListAllValue() {
        assertEquals("#all", BlockList.ALL);
    }

    public void testBlockListExtensionValue() {
        assertEquals("extension", BlockList.EXTENSION);
    }

    public void testBlockListRestrictionValue() {
        assertEquals("restriction", BlockList.RESTRICTION);
    }

    public void testBlockListSubstitutionValue() {
        assertEquals("substitution", BlockList.SUBSTITUTION);
    }

    public void testFinalListAllValue() {
        assertEquals("#all", FinalList.ALL);
    }

    public void testFinalListExtensionValue() {
        assertEquals("extension", FinalList.EXTENSION);
    }

    public void testFinalListRestrictionValue() {
        assertEquals("restriction", FinalList.RESTRICTION);
    }

    public void testBlockListEquality() {
        BlockList list1 = new BlockList(BlockList.RESTRICTION);
        BlockList list2 = new BlockList(BlockList.RESTRICTION);
        assertEquals(list1, list2);
    }

    public void testFinalListEquality() {
        FinalList list1 = new FinalList(FinalList.RESTRICTION);
        FinalList list2 = new FinalList(FinalList.RESTRICTION);
        assertEquals(list1, list2);
    }

    public void testBlockListWithSpace() {
        BlockList list = new BlockList("restriction extension substitution");
        assertTrue(list.hasRestriction());
        assertTrue(list.hasExtension());
        assertTrue(list.hasSubstitution());
    }

    public void testFinalListWithSpace() {
        FinalList list = new FinalList("restriction extension");
        assertTrue(list.hasRestriction());
        assertTrue(list.hasExtension());
    }

    public void testBlockListNegativeRestriction() {
        BlockList list = new BlockList(BlockList.EXTENSION);
        assertFalse(list.hasRestriction());
    }

    public void testFinalListNegativeExtension() {
        FinalList list = new FinalList(BlockList.RESTRICTION);
        assertFalse(list.hasExtension());
    }

    public void testFormHashCode() {
        Form form1 = Form.Qualified;
        Form form2 = Form.Qualified;
        assertEquals(form1.hashCode(), form2.hashCode());
    }

    public void testBlockListHashCodeDifferent() {
        BlockList list1 = new BlockList(BlockList.RESTRICTION);
        BlockList list2 = new BlockList(BlockList.EXTENSION);
        assertFalse(list1.hashCode() == list2.hashCode());
    }

    public void testFinalListHashCodeDifferent() {
        FinalList list1 = new FinalList(FinalList.RESTRICTION);
        FinalList list2 = new FinalList(FinalList.EXTENSION);
        assertFalse(list1.hashCode() == list2.hashCode());
    }

    public void testBlockListComplex() {
        BlockList list = new BlockList("restriction extension substitution");
        assertTrue(list.toString().length() > 0);
    }

    public void testFinalListComplex() {
        FinalList list = new FinalList("restriction extension");
        assertTrue(list.toString().length() > 0);
    }
}
