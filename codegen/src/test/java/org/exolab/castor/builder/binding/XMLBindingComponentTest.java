/*
 * Copyright 2024 (C) Intalio Inc. All Rights Reserved.
 * $Id$
 */
package org.exolab.castor.builder.binding;

import static org.junit.Assert.*;

import org.exolab.castor.builder.BuilderConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Comprehensive test suite for XMLBindingComponent class.
 * Targets >95% coverage of basic initialization and state management.
 *
 * @author Test Framework
 */
@RunWith(MockitoJUnitRunner.class)
public class XMLBindingComponentTest {

    private XMLBindingComponent component;
    private XMLBindingComponent component2;
    private XMLBindingComponent component3;

    @Mock
    private BuilderConfiguration mockConfig;

    @Before
    public void setUp() {
        component = new XMLBindingComponent(mockConfig, null);
        component2 = new XMLBindingComponent(mockConfig, null);
        component3 = new XMLBindingComponent(mockConfig, null);
    }

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructorWithValidConfig() {
        XMLBindingComponent comp = new XMLBindingComponent(mockConfig, null);
        assertNotNull("Component should be created with valid config", comp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConfigThrowsException() {
        new XMLBindingComponent(null, null);
    }

    @Test
    public void testConstructorWithNullGroupNaming() {
        XMLBindingComponent comp = new XMLBindingComponent(mockConfig, null);
        assertNotNull(
            "Component should be created with null GroupNaming",
            comp
        );
    }

    @Test
    public void testMultipleComponentsCreation() {
        XMLBindingComponent c1 = new XMLBindingComponent(mockConfig, null);
        XMLBindingComponent c2 = new XMLBindingComponent(mockConfig, null);
        XMLBindingComponent c3 = new XMLBindingComponent(mockConfig, null);

        assertNotNull("First component should be created", c1);
        assertNotNull("Second component should be created", c2);
        assertNotNull("Third component should be created", c3);
    }

    // ==================== Binding Tests ====================

    @Test
    public void testGetBindingInitiallyNull() {
        assertNull("Binding should be null initially", component.getBinding());
    }

    @Test
    public void testSetAndGetBinding() {
        ExtendedBinding binding = new ExtendedBinding();
        component.setBinding(binding);
        assertEquals(
            "Binding should be set correctly",
            binding,
            component.getBinding()
        );
    }

    @Test
    public void testSetBindingMultipleTimes() {
        ExtendedBinding binding1 = new ExtendedBinding();
        ExtendedBinding binding2 = new ExtendedBinding();

        component.setBinding(binding1);
        assertEquals(
            "First binding should be set",
            binding1,
            component.getBinding()
        );

        component.setBinding(binding2);
        assertEquals(
            "Second binding should replace first",
            binding2,
            component.getBinding()
        );
    }

    @Test
    public void testAllComponentsHaveIndependentBinding() {
        ExtendedBinding binding1 = new ExtendedBinding();
        ExtendedBinding binding2 = new ExtendedBinding();

        component.setBinding(binding1);
        component2.setBinding(binding2);

        assertEquals(
            "Component 1 should have binding1",
            binding1,
            component.getBinding()
        );
        assertEquals(
            "Component 2 should have binding2",
            binding2,
            component2.getBinding()
        );
    }

    // ==================== setView Tests ====================

    @Test(expected = IllegalArgumentException.class)
    public void testSetViewWithNullThrowsException() {
        component.setView(null);
    }

    // ==================== Component Binding Tests ====================

    @Test
    public void testGetComponentBindingIsNull() {
        assertNull(
            "Component binding should be null by default",
            component.getComponentBinding()
        );
    }

    // ==================== Type Tests ====================

    @Test
    public void testGetTypeIsNegativeOne() {
        assertEquals(
            "Type should be -1 by default",
            (short) -1,
            component.getType()
        );
    }

    @Test
    public void testAllComponentsHaveDefaultType() {
        assertEquals(
            "Component 1 type should be -1",
            (short) -1,
            component.getType()
        );
        assertEquals(
            "Component 2 type should be -1",
            (short) -1,
            component2.getType()
        );
        assertEquals(
            "Component 3 type should be -1",
            (short) -1,
            component3.getType()
        );
    }

    // ==================== Getters without Annotated ====================

    @Test
    public void testGetComponentBinding_ReturnsNull() {
        assertNull("Should return null", component.getComponentBinding());
    }

    @Test
    public void testGetAnnotated_ReturnsNull() {
        assertNull(
            "Should return null before setView",
            component.getAnnotated()
        );
    }

    @Test
    public void testGetEnumBinding_ReturnsNull() {
        assertNull("Should return null", component.getEnumBinding());
    }

    @Test
    public void testGetContentMemberName_ReturnsNull() {
        assertNull("Should return null", component.getContentMemberName());
    }

    @Test
    public void testGetImplements_ReturnsNull() {
        assertNull(
            "Should return null without class binding",
            component.getImplements()
        );
    }

    @Test
    public void testGetCollectionType_ReturnsNull() {
        assertNull("Should return null", component.getCollectionType());
    }

    @Test
    public void testGetValidator_ReturnsNull() {
        assertNull("Should return null", component.getValidator());
    }

    @Test
    public void testGetXMLFieldHandler_ReturnsNull() {
        assertNull("Should return null", component.getXMLFieldHandler());
    }

    @Test
    public void testGetVisiblity_ReturnsNull() {
        assertNull("Should return null", component.getVisiblity());
    }

    @Test
    public void testGetIsFinal_ReturnsFalse() {
        assertFalse("Should return false", component.isFinal());
    }

    // ==================== Equals Tests ====================

    @Test
    public void testEqualsWithNull() {
        assertFalse("Should not equal null", component.equals(null));
    }

    @Test
    public void testEqualsWithDifferentType() {
        assertFalse("Should not equal string", component.equals("test"));
    }

    @Test
    public void testEqualsWithInteger() {
        assertFalse("Should not equal integer", component.equals(42));
    }

    @Test
    public void testEqualsWithOtherObject() {
        assertFalse(
            "Should not equal another object",
            component.equals(new Object())
        );
    }

    // ==================== HashCode Tests ====================

    // ==================== State Tests ====================

    @Test
    public void testComponentsAreIndependent() {
        ExtendedBinding binding1 = new ExtendedBinding();
        component.setBinding(binding1);

        assertNotNull(
            "Component 1 should have binding",
            component.getBinding()
        );
        assertNull(
            "Component 2 should not have binding",
            component2.getBinding()
        );
        assertNull(
            "Component 3 should not have binding",
            component3.getBinding()
        );
    }

    @Test
    public void testComponentInitializationState() {
        assertNull("Binding should be null", component.getBinding());
        assertEquals("Type should be -1", (short) -1, component.getType());
        assertNull(
            "ComponentBinding should be null",
            component.getComponentBinding()
        );
        assertNull("Annotated should be null", component.getAnnotated());
    }

    @Test
    public void testMultipleComponentsIndependentState() {
        ExtendedBinding b1 = new ExtendedBinding();
        ExtendedBinding b2 = new ExtendedBinding();

        component.setBinding(b1);
        component2.setBinding(b2);
        component3.setBinding(null);

        assertEquals("Component 1 binding", b1, component.getBinding());
        assertEquals("Component 2 binding", b2, component2.getBinding());
        assertNull("Component 3 binding", component3.getBinding());
    }

    @Test
    public void testGetTypeAlwaysNegativeOne() {
        for (int i = 0; i < 5; i++) {
            assertEquals(
                "Type should always be -1",
                (short) -1,
                component.getType()
            );
        }
    }

    @Test
    public void testBindingCanBeChanged() {
        assertNull("Initially null", component.getBinding());

        ExtendedBinding b1 = new ExtendedBinding();
        component.setBinding(b1);
        assertEquals("Should be b1", b1, component.getBinding());

        ExtendedBinding b2 = new ExtendedBinding();
        component.setBinding(b2);
        assertEquals("Should be b2", b2, component.getBinding());

        component.setBinding(null);
        assertNull("Should be null again", component.getBinding());
    }

    @Test
    public void testDefaultNullReturnValues() {
        // All these should return null without a properly initialized state
        assertNull("ContentMemberName", component.getContentMemberName());
        assertNull("EnumBinding", component.getEnumBinding());
        assertNull("CollectionType", component.getCollectionType());
        assertNull("Validator", component.getValidator());
        assertNull("XMLFieldHandler", component.getXMLFieldHandler());
        assertNull("Visiblity", component.getVisiblity());
        assertNull("Implements", component.getImplements());
    }

    @Test
    public void testDefaultFalseBooleans() {
        assertFalse("IsFinal should be false", component.isFinal());
    }
}
