package org.castor.xml;

import org.castor.xml.JavaNamingNGImplTest.ForTestingPurposes;
import org.junit.Assert;
import org.junit.Test;

public abstract class JavaNamingBaseTest {

  static final String[] VALID_NAMES = {"name", "myName", "my_name", "NAME"};

  static final String[] INVALID_NAMES = {"my-name", "my----name"};

  static final String PS = System.getProperty("file.separator");

  private JavaNaming javaNaming;

  public void setJavaNaming(JavaNaming javaNaming) {
    this.javaNaming = javaNaming;
  }

  /**
   * Test method for {@link org.castor.xml.JavaNamingImpl#isKeyword(java.lang.String)}.
   */
  @Test
  public final void testIsKeyword() {
    Assert.assertFalse("A null String is no keyword", javaNaming.isKeyword(null));
    Assert.assertFalse("An empty String is no keyword", javaNaming.isKeyword(""));
    for (int i = 0; i < VALID_NAMES.length; i++) {
      String aName = VALID_NAMES[i];
      Assert.assertFalse("Name: " + aName + " is no keyword!", javaNaming.isKeyword(aName));
    }
  }

  /**
   * Test method for {@link org.castor.xml.JavaNamingImpl#isValidJavaIdentifier(java.lang.String)}.
   */
  @Test
  public final void testIsValidJavaIdentifier() {
    Assert.assertFalse("A null String is no identifier", javaNaming.isValidJavaIdentifier(null));
    Assert.assertFalse("An empty String is no identifier", javaNaming.isValidJavaIdentifier(""));
    for (int i = 0; i < VALID_NAMES.length; i++) {
      String aName = VALID_NAMES[i];
      Assert.assertTrue("Name: " + aName + " should be valid!",
          javaNaming.isValidJavaIdentifier(aName));
    }
    for (int i = 0; i < INVALID_NAMES.length; i++) {
      String aName = INVALID_NAMES[i];
      Assert.assertFalse("Name: " + aName + " should be invalid!",
          javaNaming.isValidJavaIdentifier(aName));
    }
  }

  /**
   * Test method for {@link org.castor.xml.JavaNamingImpl#toJavaClassName(java.lang.String)}.
   */
  @Test
  public final void testToJavaClassName() {
    Assert.assertEquals("For null in and out should be equals", null,
        javaNaming.toJavaClassName(null));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.toJavaClassName(""));
  }

  /**
   * Test method for {@link org.castor.xml.JavaNamingImpl#toJavaMemberName(java.lang.String)}.
   */
  @Test
  public final void testToJavaMemberNameString() {
    Assert.assertEquals("For null in and out should be equals", null,
        javaNaming.toJavaMemberName(null));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.toJavaMemberName(""));
    Assert.assertEquals("first", javaNaming.toJavaMemberName("first"));
    Assert.assertEquals("firstMember", javaNaming.toJavaMemberName("first-member"));
  }

  /**
   * Test method for
   * {@link org.castor.xml.JavaNamingImpl#toJavaMemberName(java.lang.String, boolean)}.
   */
  @Test
  public final void testToJavaMemberNameStringBoolean() {
    Assert.assertEquals("For null in and out should be equals", null,
        javaNaming.toJavaMemberName(null, true));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.toJavaMemberName("", true));
    Assert.assertEquals("For null in and out should be equals", null,
        javaNaming.toJavaMemberName(null, false));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.toJavaMemberName("", false));
    Assert.assertEquals("first", javaNaming.toJavaMemberName("first", true));
    Assert.assertEquals("firstMember", javaNaming.toJavaMemberName("first-member", true));

    Assert.assertEquals("class", javaNaming.toJavaMemberName("class", false));
    Assert.assertEquals("clazz", javaNaming.toJavaMemberName("class", true));

  }

  @Test
  public void testToJavaMemberNameStringBoolean2() {
    Assert.assertEquals("native", javaNaming.toJavaMemberName("native", true));
  }

  /**
   * Test method for {@link org.castor.xml.JavaNamingImpl#packageToPath(java.lang.String)}.
   */
  @Test
  public final void testPackageToPath() {
    Assert.assertEquals("For null in and out should be equals", null,
        javaNaming.packageToPath(null));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.packageToPath(""));

    Assert.assertEquals("org" + PS + "castor" + PS + "xml",
        javaNaming.packageToPath("org.castor.xml"));
    Assert.assertEquals("org", javaNaming.packageToPath("org"));
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testPackageToPath2() {
    javaNaming.packageToPath("org.");
    Assert.fail("Invalid package name must not be converted!");
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testPackageToPath3() {
    javaNaming.packageToPath(".org");
    Assert.fail("Invalid package name must not be converted!");
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testPackageToPath4() {
    javaNaming.packageToPath(".");
  }

  /**
   * Test method for
   * {@link org.castor.xml.JavaNamingImpl#getQualifiedFileName(java.lang.String,java.lang.String)}.
   */
  @Test
  public final void testGetQualifiedFileName() {
    Assert.assertEquals("For null in and out should be equals", null,
        javaNaming.getQualifiedFileName(null, null));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.getQualifiedFileName("", ""));
    Assert.assertEquals("For an empty string in and out should be equals", null,
        javaNaming.getQualifiedFileName(null, ""));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.getQualifiedFileName("", null));

    Assert.assertEquals("org" + PS + "castor" + PS + "xml/",
        javaNaming.getQualifiedFileName("", "org.castor.xml"));
    Assert.assertEquals("org" + PS + "castor" + PS + "xml/A.b",
        javaNaming.getQualifiedFileName("A.b", "org.castor.xml"));
    Assert.assertEquals("A.b", javaNaming.getQualifiedFileName("A.b", null));
    Assert.assertEquals("A.b", javaNaming.getQualifiedFileName("A.b", ""));
    Assert.assertEquals("org/A.b", javaNaming.getQualifiedFileName("A.b", "org"));
    Assert.assertEquals("org/Anton", javaNaming.getQualifiedFileName("Anton", "org"));
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testGetQualifiedFileName2() {
    javaNaming.getQualifiedFileName("A.b", ".");
    Assert.fail("Shouldn't succeed with invalid package name!");
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testGetQualifiedFileName3() {
    javaNaming.getQualifiedFileName("A.b", ".org");
    Assert.fail("Shouldn't succeed with invalid package name!");
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testGetQualifiedFileName4() {
    javaNaming.getQualifiedFileName("A.b", "org.");
    Assert.fail("Shouldn't succeed with invalid package name!");
  }

  /**
   * Test method for {@link org.castor.xml.JavaNamingImpl#getPackageName(java.lang.String)}.
   */
  @Test
  public final void testGetPackageName() {
    Assert.assertEquals("For null in and out should be equals", null,
        javaNaming.getPackageName(null));
    Assert.assertEquals("For an empty string in and out should be equals", "",
        javaNaming.getPackageName(""));

    Assert.assertEquals("Extracted package name doesn't match",
        JavaNamingImpl.class.getPackage().getName(),
        javaNaming.getPackageName(JavaNamingImpl.class.getName()));
    Assert.assertEquals("Extracted package name doesn't match", "", javaNaming.getPackageName("A"));
  }

  /**
   * Test method for {@link org.castor.xml.JavaNamingImpl#isAddMethod(java.lang.reflect.Method)}.
   */
  @Test
  public final void testIsAddMethod() {
    Assert.assertFalse(javaNaming.isAddMethod(null));
  }

  @Test
  public final void testGetClassName() {
    Assert.assertNull(javaNaming.getClassName(null));
    Assert.assertEquals("JavaNamingNGImplTest",
        javaNaming.getClassName(JavaNamingNGImplTest.class));
    Assert.assertEquals("JavaNamingNGImplTest$ForTestingPurposes",
        javaNaming.getClassName(ForTestingPurposes.class));
  }

  @Test
  public void EnumIsKeyWordSucceeds() {
    Assert.assertTrue(javaNaming.isKeyword("enum"));
  }

}
