package org.exolab.castor.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.Structure;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Comprehensive test suite for GroupNaming class targeting >95% coverage.
 */
public class GroupNamingTest {

    private JavaNaming javaNaming;
    private Group mockGroup;
    private Group mockParentGroup;
    private ModelGroup mockModelGroup;
    private ComplexType mockComplexType;
    private ComplexType mockParentComplexType;
    private ElementDecl mockElement;
    private GroupNaming groupNaming;
    private static final String PACKAGE_NAME = "org.test.package";
    private static final String GROUP_NAME = "TestGroup";
    private static final String CONVERTED_NAME = "TestGroup";
    private static final String MODEL_GROUP_NAME = "ModelGroupName";

    @Before
    public void setUp() {
        javaNaming = mock(JavaNaming.class);
        mockGroup = mock(Group.class);
        mockParentGroup = mock(Group.class);
        mockModelGroup = mock(ModelGroup.class);
        mockComplexType = mock(ComplexType.class);
        mockParentComplexType = mock(ComplexType.class);
        mockElement = mock(ElementDecl.class);
        groupNaming = new GroupNaming(javaNaming);
    }

    // ======================== Happy Path Tests ========================

    @Test
    public void should_ReturnConvertedName_When_GroupHasExplicitName() {
        when(mockGroup.getName()).thenReturn(GROUP_NAME);
        when(javaNaming.toJavaClassName(GROUP_NAME)).thenReturn(CONVERTED_NAME);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals(CONVERTED_NAME, result);
        verify(javaNaming).toJavaClassName(GROUP_NAME);
    }

    @Test
    public void should_ReturnNull_When_ParentIsNull() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_UseParentGroupName_When_ParentIsGroup() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn("ParentGroup");
        when(javaNaming.toJavaClassName("ParentGroup")).thenReturn(
            "ParentGroup"
        );
        when(mockGroup.getOrder()).thenReturn(Order.choice);
        when(javaNaming.toJavaClassName("choice")).thenReturn("Choice");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ParentGroupChoice", result);
    }

    @Test
    public void should_UseModelGroupName_When_ParentIsModelGroup() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockModelGroup);
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn(MODEL_GROUP_NAME);
        when(javaNaming.toJavaClassName(MODEL_GROUP_NAME)).thenReturn(
            "ModelGroupName"
        );

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ModelGroupName", result);
    }

    @Test
    public void should_UseComplexTypeName_When_ParentIsComplexType() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("ComplexTypeName");
        when(javaNaming.toJavaClassName("ComplexTypeName")).thenReturn(
            "ComplexTypeName"
        );

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ComplexTypeName", result);
    }

    @Test
    public void should_UseParentElementName_When_ComplexTypeIsAnonymous() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);
        when(mockComplexType.getParent()).thenReturn(mockElement);
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("ElementName");
        when(javaNaming.toJavaClassName("ElementName")).thenReturn(
            "ElementName"
        );

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ElementName", result);
    }

    @Test
    public void should_ReturnNull_When_AnonymousComplexTypeHasNoParent() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);
        when(mockComplexType.getParent()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_AppendCounterOnNameCollision_When_NameAlreadyExists() {
        GroupNaming groupNaming1 = new GroupNaming(javaNaming);

        Group group1 = mock(Group.class);
        when(group1.getName()).thenReturn(null);
        when(group1.getParent()).thenReturn(mockModelGroup);
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn("Model");
        when(javaNaming.toJavaClassName("Model")).thenReturn("Model");

        Group group2 = mock(Group.class);
        when(group2.getName()).thenReturn(null);
        when(group2.getParent()).thenReturn(mockModelGroup);

        String result1 = groupNaming1.createClassName(group1, PACKAGE_NAME);
        String result2 = groupNaming1.createClassName(group2, PACKAGE_NAME);

        assertEquals("Model", result1);
        assertEquals("Model2", result2);
    }

    @Test
    public void should_IncrementCounterUntilUnique_When_MultipleCollisions() {
        GroupNaming groupNaming1 = new GroupNaming(javaNaming);

        Group group1 = mock(Group.class);
        Group group2 = mock(Group.class);
        Group group3 = mock(Group.class);

        ModelGroup parentModelGroup = mock(ModelGroup.class);
        when(parentModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(parentModelGroup.getName()).thenReturn("Dup");
        when(javaNaming.toJavaClassName("Dup")).thenReturn("Dup");

        when(group1.getName()).thenReturn(null);
        when(group1.getParent()).thenReturn(parentModelGroup);

        when(group2.getName()).thenReturn(null);
        when(group2.getParent()).thenReturn(parentModelGroup);

        when(group3.getName()).thenReturn(null);
        when(group3.getParent()).thenReturn(parentModelGroup);

        String result1 = groupNaming1.createClassName(group1, PACKAGE_NAME);
        String result2 = groupNaming1.createClassName(group2, PACKAGE_NAME);
        String result3 = groupNaming1.createClassName(group3, PACKAGE_NAME);

        assertEquals("Dup", result1);
        assertEquals("Dup2", result2);
        assertEquals("Dup3", result3);
    }

    @Test
    public void should_AppendOrderToName_When_ParentIsGroup() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn("Parent");
        when(javaNaming.toJavaClassName("Parent")).thenReturn("Parent");
        when(mockGroup.getOrder()).thenReturn(Order.all);
        when(javaNaming.toJavaClassName("all")).thenReturn("All");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ParentAll", result);
    }

    @Test
    public void should_NotAppendOrder_When_ParentIsModelGroup() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockModelGroup);
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn("Model");
        when(javaNaming.toJavaClassName("Model")).thenReturn("Model");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("Model", result);
    }

    @Test
    public void should_NotAppendOrder_When_ParentIsComplexType() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("ComplexType");
        when(javaNaming.toJavaClassName("ComplexType")).thenReturn(
            "ComplexType"
        );

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ComplexType", result);
    }

    @Test
    public void should_ReturnNull_When_ParentStructureTypeIsUnknown() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockElement);
        when(mockElement.getStructureType()).thenReturn((short) 99);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_TrackGroupsPerPackage_WhenDifferentPackages() {
        String package1 = "com.package1";
        String package2 = "com.package2";

        Group group1 = mock(Group.class);
        Group group2 = mock(Group.class);

        when(group1.getName()).thenReturn(null);
        when(group1.getParent()).thenReturn(mockModelGroup);
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn("SameName");
        when(javaNaming.toJavaClassName("SameName")).thenReturn("SameName");

        when(group2.getName()).thenReturn(null);
        when(group2.getParent()).thenReturn(mockModelGroup);

        String result1 = groupNaming.createClassName(group1, package1);
        String result2 = groupNaming.createClassName(group2, package2);

        assertEquals("SameName", result1);
        assertEquals("SameName", result2);
    }

    @Test
    public void should_HandleChoiceOrder_WhenGroupOrderIsChoice() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn("Parent");
        when(javaNaming.toJavaClassName("Parent")).thenReturn("Parent");
        when(mockGroup.getOrder()).thenReturn(Order.choice);
        when(javaNaming.toJavaClassName("choice")).thenReturn("Choice");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ParentChoice", result);
    }

    @Test
    public void should_HandleSequenceOrder_WhenGroupOrderIsSequence() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn("Parent");
        when(javaNaming.toJavaClassName("Parent")).thenReturn("Parent");
        when(mockGroup.getOrder()).thenReturn(Order.sequence);
        when(javaNaming.toJavaClassName("sequence")).thenReturn("Sequence");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ParentSequence", result);
    }

    @Test
    public void should_InitializeWithJavaNaming_WhenConstructorCalled() {
        assertNotNull(groupNaming);
    }

    @Test
    public void should_ResolveDeepNestedGroups_WhenGroupContainsGroupParent() {
        Group grandparentGroup = mock(Group.class);

        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn(null);
        when(mockParentGroup.getParent()).thenReturn(grandparentGroup);
        when(grandparentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(grandparentGroup.getName()).thenReturn("GrandParent");
        when(javaNaming.toJavaClassName("GrandParent")).thenReturn(
            "GrandParent"
        );
        when(mockParentGroup.getOrder()).thenReturn(Order.choice);
        when(javaNaming.toJavaClassName("choice")).thenReturn("Choice");
        when(mockGroup.getOrder()).thenReturn(Order.all);
        when(javaNaming.toJavaClassName("all")).thenReturn("All");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("GrandParentChoiceAll", result);
    }

    @Test
    public void should_HandleComplexTypeWithElementParent_WhenNestedStructure() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentComplexType);
        when(mockParentComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockParentComplexType.getName()).thenReturn(null);
        when(mockParentComplexType.getParent()).thenReturn(mockElement);
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("MyElement");
        when(javaNaming.toJavaClassName("MyElement")).thenReturn("MyElement");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("MyElement", result);
    }

    @Test
    public void should_PreserveCasedNames_WhenJavaNamingConvertsCase() {
        when(mockGroup.getName()).thenReturn("test-group");
        when(javaNaming.toJavaClassName("test-group")).thenReturn("TestGroup");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("TestGroup", result);
    }

    @Test
    public void should_HandleNullComplexTypeParentGracefully() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);
        when(mockComplexType.getParent()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_HandleEmptyStringGroupName() {
        when(mockGroup.getName()).thenReturn("");
        when(javaNaming.toJavaClassName("")).thenReturn("");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("", result);
    }

    @Test
    public void should_ReturnCachedName_When_GroupRetrievedSecondTime() {
        GroupNaming gn = new GroupNaming(javaNaming);
        Group g1 = mock(Group.class);
        Group g2 = mock(Group.class);

        when(g1.getName()).thenReturn(null);
        when(g1.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn("Parent");
        when(javaNaming.toJavaClassName("Parent")).thenReturn("Parent");
        when(g1.getOrder()).thenReturn(Order.choice);
        when(javaNaming.toJavaClassName("choice")).thenReturn("Choice");

        String result1 = gn.createClassName(g1, PACKAGE_NAME);
        assertEquals("ParentChoice", result1);

        when(g2.getName()).thenReturn(null);
        when(g2.getParent()).thenReturn(mockParentGroup);
        when(g2.getOrder()).thenReturn(Order.choice);

        String result2 = gn.createClassName(g2, PACKAGE_NAME);
        assertEquals("ParentChoice2", result2);
    }

    @Test
    public void should_HandleGroupWithNullName_AndNullParent() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_ReturnNullWhenParentHasNoName() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn(null);
        when(mockParentGroup.getParent()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_ConstructorAcceptsNullJavaNaming() {
        GroupNaming gn = new GroupNaming(null);
        assertNotNull(gn);
    }

    @Test
    public void should_HandleModelGroupWithoutName() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockModelGroup);
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_HandleComplexTypeWithoutName() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);
        when(mockComplexType.getParent()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_HandleMultiplePackagesWithSameGroupName() {
        GroupNaming gn = new GroupNaming(javaNaming);

        Group g1 = mock(Group.class);
        Group g2 = mock(Group.class);
        Group g3 = mock(Group.class);

        ModelGroup mg1 = mock(ModelGroup.class);
        when(mg1.getStructureType()).thenReturn(Structure.MODELGROUP);
        when(mg1.getName()).thenReturn("Base");
        when(javaNaming.toJavaClassName("Base")).thenReturn("Base");

        when(g1.getName()).thenReturn(null);
        when(g1.getParent()).thenReturn(mg1);

        when(g2.getName()).thenReturn(null);
        when(g2.getParent()).thenReturn(mg1);

        when(g3.getName()).thenReturn(null);
        when(g3.getParent()).thenReturn(mg1);

        String result1 = gn.createClassName(g1, "com.pkg1");
        String result2 = gn.createClassName(g2, "com.pkg1");
        String result3 = gn.createClassName(g3, "com.pkg2");

        assertEquals("Base", result1);
        assertEquals("Base2", result2);
        assertEquals("Base", result3);
    }

    @Test
    public void should_HandleGroupOrderAll() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockParentGroup);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getName()).thenReturn("Container");
        when(javaNaming.toJavaClassName("Container")).thenReturn("Container");
        when(mockGroup.getOrder()).thenReturn(Order.all);
        when(javaNaming.toJavaClassName("all")).thenReturn("All");

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ContainerAll", result);
    }

    @Test
    public void should_HandleComplexTypeNamedWithParentGroup() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("MyComplexType");
        when(javaNaming.toJavaClassName("MyComplexType")).thenReturn(
            "MyComplexType"
        );

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("MyComplexType", result);
    }

    @Test
    public void should_HandleCacheHitForSameGroupAndPackage() {
        GroupNaming gn = new GroupNaming(javaNaming);
        Group group = mock(Group.class);

        ModelGroup parent = mock(ModelGroup.class);
        when(parent.getStructureType()).thenReturn(Structure.MODELGROUP);
        when(parent.getName()).thenReturn("Parent");
        when(javaNaming.toJavaClassName("Parent")).thenReturn("Parent");

        when(group.getName()).thenReturn(null);
        when(group.getParent()).thenReturn(parent);

        String result1 = gn.createClassName(group, PACKAGE_NAME);
        String result2 = gn.createClassName(group, PACKAGE_NAME);

        assertEquals("Parent", result1);
        assertEquals("Parent", result2);
    }

    @Test
    public void should_HandleNullElementParentInComplexType() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);
        when(mockComplexType.getParent()).thenReturn(null);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_HandleGroupNameNotNullReturnEarly() {
        when(mockGroup.getName()).thenReturn("ExplicitName");
        when(javaNaming.toJavaClassName("ExplicitName")).thenReturn(
            "ExplicitName"
        );

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ExplicitName", result);
        verify(mockGroup).getName();
    }

    @Test
    public void should_HandleComplexTypeParentNotElement() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);

        Structure nonElementParent = mock(Structure.class);
        when(nonElementParent.getStructureType()).thenReturn(Structure.GROUP);
        when(mockComplexType.getParent()).thenReturn(nonElementParent);

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertNull(result);
    }

    @Test
    public void should_HandleComplexTypeWithElementParentReturningName() {
        when(mockGroup.getName()).thenReturn(null);
        when(mockGroup.getParent()).thenReturn(mockComplexType);
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);
        when(mockComplexType.getParent()).thenReturn(mockElement);
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("ContainerElement");
        when(javaNaming.toJavaClassName("ContainerElement")).thenReturn(
            "ContainerElement"
        );

        String result = groupNaming.createClassName(mockGroup, PACKAGE_NAME);

        assertEquals("ContainerElement", result);
    }
}
