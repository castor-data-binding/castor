XML code generation
===================

Introduction
============

News
----

### Source generation & Java field naming conventions

Starting with **release 1.3.3**, the Castor source generator supports a
new naming scheme for Java field names, which will be enabled by
default. As such, Java field names as generated will follow more closely
the standard Java property naming conventions. Should there be a need to
keep using the old naming schema, please amend the following property in
your custom `castorbuilder.properties` file:

``` {.xml}
#
# Property specifying whether for Java field names the old naming conventions
# should be used.
#
# Possible values:
# - true
# - false (default)
# 
# 
# org.exolab.castor.builder.field-naming.old = false
# 
#
org.exolab.castor.builder.field-naming.old=true
            
```

### Source generation & Java 5.0

1.  Since **release 1.0.2**, the Castor source generator supports the
    optional the generation of Java 5.0 compliant code.

2.  With **release 1.3**, the XML code generator will generate Java 5.0
    compliant code by default.

With support for Java 5.0 enabled, the generated code will support the
following Java 5.0-specific artifacts:

-   Use of parameterized collections, e.g. `ArrayList<String>`.

-   Use of `@Override` annotations with the generated methods that
    require it.

-   Use of `@SuppressWarnings` with "unused" method parameters on the
    generated methods that needed it.

-   Added "enum" to the list of reserved keywords.

To disable this feature (on by default), please amend the following
property in your custom `castorbuilder.properties` file:

``` {.xml}
# Specifies whether the sources generated should be source compatible with
# Java 1.4 or Java 5.0. Legal values are "1.4" and "5.0".  When "5.0" is
# selected, generated source will use Java 5 features such as generics and
# annotations.
# Defaults to "5.0".
#
org.exolab.castor.builder.javaVersion=5.0
        
```

Introduction
------------

Castor's Source Code Generator creates a set of Java classes which
represent an object model for an XML Schema (W3C XML Schema 1.0 Second
Edition, Recommendation), as well as the necessary Class Descriptors
used by the [marshaling framework](../xml-framework.html) to obtain
information about the generated classes.

> **Note**
>
> The generated source files will need to be compiled. A later release
> may add an Ant taskdef to handle this automatically.

Invoking the XML code generator
-------------------------------

The XML code generator can be invoked in many ways, including by command
line, via an Ant task and via Maven.

XML Schema
----------

The input file for the source code generator is an XML
schema[^1]footnote&gt;. The currently supported version is the **W3C XML
Schema 1.0, Second Edition** [^2].

Properties
==========

Overview
--------

Please find below a list of properties that can be configured through
the builder configuration properties, as defined in either the default
or a custom XML code generator configuration file. These properties
allow you to control various advanced options of the XML source
generator.

| Option | Description | Values | Default | Since version
| ------ | ----------- | ------ | ------- | -------------
| [org.exolab.castor.builder.javaVersion](#xml.code.generator.properties.detailed.java50) | Compliance with Java version | 1.4/5.0 | 1.4 | 1.0.2
| [org.exolab.castor.builder.forceJava4Enums](#xml.code.generator.properties.detailed.enumerations.3) | Forces the code generator to create 'old' Java 1.4 enumeration classes even in Java 5 mode. | `true`/`false` | `false` | 1.1.3                                                                                                                    
| [org.exolab.castor.builder.boundproperties](#xml.code.generator.properties.detailed.bound.properties) | Generation of bound properties | `true`/`false` | `false` | 0.8.9
| [org.exolab.castor.builder.javaclassmapping](#xml.code.generator.properties.detailed.class.creation) | Class generation mode | `element`/`type` | `element` | 0.9.1
| [org.exolab.castor.builder.superclass](#xml.code.generator.properties.detailed.super.class) | Global super class (for all classes generated) | Any valid class name |  | 0.8.9
| [org.exolab.castor.builder.nspackages](#xml.code.generator.properties.detailed.namespace.mapping) | XML namespace to package name mapping | A series of mappings |  | 0.8.9
| [org.exolab.castor.builder.equalsmethod](#xml.code.generator.properties.detailed.equals) | Generation of `equals`/`hashCode()` method | `true`/`false` | `false` | 0.9.1
| [org.exolab.castor.builder.useCycleBreaker](#xml.code.generator.properties.detailed.use.cyclebreaker) | Use of cycle breaker code in generated `equals`/`hashCode()` method | `true`/`false` | `true` | 1.3.2
| [org.exolab.castor.builder.primitivetowrapper](#xml.code.generator.properties.detailed.primitive.wrappers) | Generation of Object wrappers instead of primitives | `true`/`false` | `false` | 0.9.4
| [org.exolab.castor.builder.automaticConflictResolution](#xml.code.generator.properties.detailed.name.conflict.resolution) | Specifies whether **automatic class name conflict resolution** should be used or not | `true`/`false` | `false` | **1.1.1**
| [org.exolab.castor.builder.extraCollectionMethods](#xml.code.generator.properties.detailed.extra.collection) | Specifies whether **extra** (additional) methods should be created for collection-style fields. Set this to true if you want your code to be more compatible with Castor JDO or other persistence frameworks. | `true`/`false` | `false` | 0.9.1
| [org.exolab.castor.builder.jclassPrinterFactories](#xml.code.generator.properties.detailed.class.printing) | Enlists the available modes for (J)Class *printing* during XML code generation. | `org.exolab.castor.builder.printing.WriterJClassPrinterFactory`/ `org.exolab.castor.builder.printing.TemplateJClassPrinterFactory` | n/a | **1.2.1**
| [org.exolab.castor.builder.extraDocumentationMethods](#xml.code.generator.properties.detailed.extra.documentation) | specifying whether extra members/methods for extracting XML schema documentation should be made available. | `true`/`false` | `false` | **1.2**
[&lt;column&gt; - Definitions]

Customization - Lookup mechanism
--------------------------------

By default, the Castor XML code generator will look for such a property
file in the following places:

1.  If no custom property file is specified, the Castor XML code
    generator will use the default builder configuration properties at
    `org/exolab/castor/builder/castorbuilder.properties` as shipped as
    part of the XML code generator JAR.

2.  If a file named `castorbuilder.properties` is available on the
    CLASSPATH, the Castor XML code generator will use each of the
    defined property values to override the default value as defined in
    the default builder configuration properties. This file is commonly
    referred to as a **custom** builder configuration file.

Detailed descriptions
---------------------

### Source generation & Java 5.0

As of **Castor 1.0.2**, the Castor source generator now supports the
generation of Java 5.0 compliant code. The generated code - with the new
feature enabled - will make use of the following Java 5.0-specific
artifacts:

-   Use of parameterized collections, e.g. ArrayList&lt;String&gt;.

-   Use of @Override annotations with the generated methods that
    require it.

-   Use of @SupressWarnings with "unused" method parameters on the
    generated methods that needed it.

-   Added "enum" to the list of reserved keywords.

To enable this feature (off by default), please uncomment the following
property in your custom `castorbuilder.properties` file:

``` {.xml}
# This property specifies whether the sources generated
# should comply with java 1.4 or 5.0; defaults to 1.4
org.exolab.castor.builder.javaVersion=5.0
```

### SimpleType Enumerations

In previous versions, castor only supported (un)marshalling of "simple"
java5 enums, meaning enums where all facet values are valid java
identifiers. In these cases, every enum constant name can be mapped
directly to the xml value. See the following example:

``` {.xml}
<xs:simpleType name="AlphabeticalType">
  <xs:restriction base="xs:string">
    <xs:enumeration value="A"/>
    <xs:enumeration value="B"/>
    <xs:enumeration value="C"/>
  </xs:restriction>
</xs:simpleType>
```

``` {.java}
public enum AlphabeticalType {
    A, B, C
}
```

``` {.xml}
<root>
  <AlphabeticalType>A</AlphabeticalType>    
</root>    
```

So if there is at least ONE facet that cannot be mapped directly to a
valid java identifier, we need to extend the enum pattern. Examples for
these cases are value="5" or value="-s". Castor now introduces an
extended pattern, similar to the jaxb2 enum handling. The actual value
of the enumeration facet is stored in a private String property, the
name of the enum constant is translated into a valid identifier.
Additionally, some convenience methods are introduced, details about
these methods are described after the following example:

``` {.xml}
<xs:simpleType name="CompositeType">
  <xs:restriction base="xs:string">
    <xs:enumeration value="5"/>
    <xs:enumeration value="10"/>
  </xs:restriction>
</xs:simpleType>
```

``` {.java}
public enum CompositeType {
    VALUE_5("5"),
    VALUE_10("10");

    private final java.lang.String value;

    private CompositeType(final java.lang.String value) {
        this.value = value;
    }

    public static CompositeType fromValue(final java.lang.String value) {
        for (CompositeType c: CompositeType.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }

    public java.lang.String value() {
        return this.value;
    }
    
    public java.lang.String toString() {
        return this.value;
    }
}
```

``` {.xml}
<root>
  <CompositeType>5</CompositeType>    
</root>
```

#### Unmarshalling of complex enums

Castor uses the static void fromValue(String value) method to retrieve
the correct instance from the value in the XML input file. In our
example, the input is "5", fromValue returns `CompositeType.VALUE_5`.

#### Marshalling of complex enums

Currently, we have to distinguish between enums with a class descriptor
and the ones without. If you are using class descriptors, the
EnumerationHandler uses the value() method to write the xml output.

If no descriptor classes are available, castor uses per default the
toString() method to marshall the value. In this case, the override of
the java.lang.Enum.toString() method is mandatory, because
java.lang.Enum.toString() returns the NAME of the facet instead of the
VALUE. So in our example, `VALUE_10` would be returned instead of "10".
To avoid this, castor expects an implementation of toString() that
returns `this.value`.

#### Source Generation of complex enums

If the java version is set to "5.0", the new default behavior of castor
is to generate complex java5 enums for simpleType enumerations, as
described above. In java 1.4 mode, nothing has changed and the old style
enumeration classes using a HashMap are created.

Users, who are in java5 mode and still want to use the old style java
1.4 classes, can force this by setting the new
`org.exolab.castor.builder.forceJava4Enums` property to true as follows:

     # Forces the code generator to create 'old' Java 1.4 enumeration classes instead 
     # of Java 5 enums for xs:simpleType enumerations, even in Java 5 mode.
     #
     # Possible values:
     # - false (default)
     # - true
    org.exolab.castor.builder.forceJava4Enums=false

### Bound Properties

Bound properties are "properties" of a class, which when updated the
class will send out a `java.beans.PropertyChangeEvent` to all registered
`java.beans.PropertyChangeListeners`.

To enable bound properties, please add a property definition to your
custom builder configuration file as follows:

    # To enable bound properties uncomment the following line. Please
    # note that currently *all* fields will be treated as bound properties
    # when enabled. This will change in the future when we introduce
    # fine grained control over each class and it's properties.
    #
    org.exolab.castor.builder.boundproperties=true

When enabled, **all** properties will be treated as bound properties.
For each class that is generated a `setPropertyChangeListener` method is
created as follows:

``` {.java}
/**
 * Registers a PropertyChangeListener with this class.
 * @param pcl The PropertyChangeListener to register.
 **/

public void addPropertyChangeListener (java.beans.PropertyChangeListener pcl)
{
   propertyChangeListeners.addElement(pcl);
}
```

Whenever a property of the class is changed, a
`java.beans.PropertyChangeEvent` will be sent to all registered
listeners. The property name, the old value and the new value will be
set in the **java.beans.PropertyChangeEvent**.

> **Note**
>
> To prevent unnecessary overhead, if the property is a collection, the
> old value will be
> null
> .

### Class Creation/Mapping

The source generator can treat the XML Schema structures such as
`<complexType>` and `<element>` in two main ways. The first, and current
default method is called the "element" method. The other is called the
"type" method.


| Method | Explanation
| ------ | -----------
| 'element' | The "element" method creates classes for all elements whose type is a &lt;complexType&gt;. Abstract classes are created for all top-level &lt;complexType&gt;s. Any elements whose type is a top-level type will have a new class create that extends the abstract class which was generated for that top-level complexType. Classes are not created for elements whose type is a &lt;simpleType&gt;.
| 'type' | The "type" method creates classes for all top-level &lt;complexType&gt;s, or elements that contain an "anonymous" (in-lined) &lt;complexType&gt;. Classes will not be generated for elements whose type is a top-level type.
[column - Definitions]

To change the "method" of class creation, please add the following
property definition to your custom builder configuration file:

``` {.xml}
# Java class mapping of <xsd:element>'s and <xsd:complexType>'s
#
org.exolab.castor.builder.javaclassmapping=type
```

Please note that setting this property will not affect class creation
when the `defaultBindingType` is explicitely used in a binding file. In
that case, the value set there will take precedence.

### Setting a super class

The source generator enables the user to set a super class to **all**
the generated classes (of course, class descriptors are not affected by
this option). Please note that, though the binding file, it is possible
to define a super class for individual classes

To set the global super class, please add the following property
definition to your custom builder configuration file:

``` {.xml}
# This property allows one to specify the super class of *all*
# generated classes
#
org.exolab.castor.builder.superclass=com.xyz.BaseObject
```

### Mapping XML namespaces to Java packages

An XML Schema instance is identified by a namespace. For data-binding
purposes, especially code generation it may be necessary to map
namespaces to Java packages.

This is needed for imported schema in order for Castor to generate the
correct imports during code generation for the primary schema.

To allow the mapping between namespaces and Java packages , edit the
castorbuilder.properties file :

``` {.xml}
# XML namespace mapping to Java packages
#
#org.exolab.castor.builder.nspackages=\
   http://www.xyz.com/schemas/project=com.xyz.schemas.project,\
   http://www.xyz.com/schemas/person=com.xyz.schemas.person
```

### Generate equals()/hashCode() method

Since version: 0.9.1

The Source Generator can override the `equals()` and `hashCode()` method
for the generated objects.

To have `equals()` and `hashCode()` methods generated, override the
following property in your custom castorbuilder.properties file:

``` {.xml}
# Set to true if you want to have an equals() and 
# hashCode() method generated for each generated class;
# false by default
org.exolab.castor.builder.equalsmethod=true
```

### Use CycleBreaker for generation of equals()/hashcode() methods.

Since version: 1.3.2

Specifies whether cycle breaker code should be added to generated
methods `equals()` and `hashcode()`.

``` {.xml}
# Property specifying whether cycle breaker code should be added
# to generated methods 'equals' and 'hashcode'. 
#
# Possible values:
# - true (default)
# - false
#
# <pre>
# org.exolab.castor.builder.useCycleBreaker
# </pre>
org.exolab.castor.builder.useCycleBreaker=true
```

### Maps java primitive types to wrapper object

Since version 0.9.4

It may be convenient to use java objects instead of primitives, the
Source Generator provides a way to do it. Thus the following mapping can
be used:

-   boolean to java.lang.Boolean
-   byte to java.lang.Byte
-   double to java.lang.Double
-   float to java.lang.Float
-   int and integer to java.lang.Integer
-   long to java.lang.Long
-   short to java.lang.Short

To enable this property, edit the castor `builder.properties` file:

``` {.xml}
# Set to true if you want to use Object Wrappers instead
# of primitives (e.g Float instead of float).
# false by default.
#org.exolab.castor.builder.primitivetowrapper=false
```

### Automatic class name conflict resolution

Since version 1.1.1

With this property enabled, the XML code generator will use a new
automatic class name resolution mode that has special logic implemented
to automatically resolve class name conflicts.

This new mode deals with various class name conflicts where previously a
binding file had to be used to resolve these conflicts manually.

To enable this feature (turned off by default), please add the following
property definitio to your custom `castorbuilder.properties` file:

``` {.xml}
# Specifies whether automatic class name conflict resolution
# should be used or not; defaults to false.
#
org.exolab.castor.builder.automaticConflictResolution=true
```

### Extra collection methods

Specifies whether **extra** (additional) methods should be created for
collection-style fields. Set this to `true` if you want your code to be
more compatible with Castor JDO (or other persistence frameworks in
general).

By setting this property to `true`, additional getter/setter methods for
the field in question, such as get/set by reference and set as copy
methods, will be added. In order to have these additional methods
generated, please override the following code generator property in a
custom `castorbuilder.properties` as shown:

``` {.xml}
# Enables generation of extra methods for collection fields, such as get/set by
# reference and set as copy.  Extra methods are in addition to the usual
# collection get/set methods.  Set this to true if you want your code to be
# more compatible  with Castor JDO.
#
# Possible values:
# - false (default) 
# - true
org.exolab.castor.builder.extraCollectionMethods=true            
```

### Class printing

As of release 1.2, Castor supports the use of Velocity-based code
templates for code generation. For the time being, Castor will support
two modes for code generation, i.e. the new Velocity-based and an old
legacy mode. **Default** will be the *legacy* mode; this will be changed
with a later release of Castor.

In order to use the new Velocity-based code generation, please call the
method setJClassPrinterType(String) on
`org.exolab.castor.builder.SourceGenerator` with a value of `velocity`.

As we consider the code stable enough for a major release, we do
encourage users to use the new Velocity-based mode and to provide us
with (valuable) feedback.

Please note that we have changed the mechanics of changing the JClass
printing type between releases 1.2 and 1.2.1.

### Extra documentation methods

As of release 1.2, the Castor XML code generator - if configured as
shown below - now supports generation of additional methods to allow
programmatic access to &lt;xs:documentation&gt; elements for top-level
type/element definitions as follows:

``` {.xml}
public java.lang.String getXmlSchemaDocumentation(final java.lang.String source);
public java.util.Map getXmlSchemaDocumentations();
```

In order to have these additional methods generated as shown above,
please override the following code generator property in a custom
`castorbuilder.properties` as shown:

``` {.xml}
# Property specifying whether extra members/methods for extracting XML schema
# documentation should be made available; defaults to false
org.exolab.castor.builder.extraDocumentationMethods=true
```

Custom bindings
===============

This section defines the Castor XML binding file and describes - based
upon the use of examples - how to use it.

The default binding used to generate the Java Object Model from an XML
schema may not meet your expectations. For instance, the default binding
doesn't deal with naming collisions that can appear because XML Schema
allows an element declaration and a complexType definition to use the
same name. The source generator will attempt to create two Java classes
with the same qualified name. However, the latter class generated will
simply overwrite the first one.

Another example of where the default source generator binding may not
meet your expectations is when you want to change the default datatype
binding provided by Castor or when you want to add validation rules by
implementing your own validator and passing it to the Source Generator.

Binding File
------------

The binding declaration is an XML-based language that allows the user to
control and tweak details about source generation for the generated
classes. The aim of this section is to provide an overview of the
binding file and a definition of the several XML components used to
define this binding file.

### &lt;binding&gt; element

``` {.xml}
<binding
    defaultBindingType = (element|type)>
    (include*,
     package*,
     namingXML?,
     elementBinding*,
     attributeBinding,
     complexTypeBinding,
     groupBinding)
</binding>
```

The binding element is the root element and contains the binding information.

| Name | Description | Default | Required?
| defaultBindingType | Controls the class creation mode for details on the available modes. Please note that the mode specified in this attribute will override the binding type specified in the `castorbuilder.properties` file. | `element` |  No
[&lt;column&gt; - Definitions]

### &lt;include&gt; element

``` {.xml}
<include
    URI = xsd:anyURI/>
```

This element allows you to include a binding declaration defined in
another file. This allows reuse of binding files defined for various XML
schemas.

**URI:**

:   The URI of the binding file to include.

### &lt;package&gt; element

``` {.xml}
<package>
    name = xsd:string
    (namespace|schemaLocation) = xsd:string>
</package>
```

| Name            | Description
| --------------- | -----------------------------------------------------------------------------------------------
| name            |  A fully qualified java package name.
| namespace       | An XML namespace that will be mapped to the package name defined by the *name* element.
| schemaLocation  | A URL that locates the schema to be mapped to the package name defined by the *name* element.
[package - Definitions]

The `targetNamespace` attribute of an XML schema identifies the
namespace in which the XML schema elements are defined. This language
namespace is defined in the generated Java source as a package
declaration. The `<package/>` element allows you to define the mapping
between an XML namespace and a Java package.

Moreover, XML schema allows you to factor the definition of an XML
schema identified by a unique namespace by including several XML schemas
instances to build one XML schema using the `<xsd:include/>` element.
Please make sure you understand the difference between `<xsd:include/>`
and `<xsd:import/>`. `<xsd:include/>` \# relies on the URI of the
included XML schema. This element allows you to keep the structure
hierarchy defined in XML schema in a single generated Java package. Thus
the binding file allows you to define the mapping between a
`schemaLocation` attribute and a Java package.

### &lt;namingXML&gt; element

``` {.xml}
<namingXML>
   (elementName,complexTypeName,modelGroupName)
</namingXML>

<elementName|complexTypeName|modelGroupName>
    (prefix?, suffix?) = xsd:string
</elementName|complexTypeName|modelGroupName>
```

| Name       | Description
| ---------- | -----------------------------------------------------------------
| *prefix*   | The prefix to add to the names of the generated classes.
| *suffix*   | The suffix to append to the the names of the generated classes.
[namingXML - Definitions]

One of the aims of the binding file is to avoid naming collisions.
Indeed, XML schema allows &lt;element&gt;s and &lt;complexType&gt;s to
share the same name, resulting in name collisions when generating
sources. Defining a binding for each element and complexType that share
the same name is not always a convenient solution (for instance the BPML
XML schema and the UDDI v2.0 XML schema use the same names for top-level
complexTypes and top-level elements).

The main aim of the `<namingXML/>` element is to define default prefices
and suffices for the names of the classes generated for an
&lt;element&gt;, a &lt;complexType&gt; or a model group definition.

> **Note**
>
> It is not possible to control the names of the classes generated to
> represent nested model groups (all, choice, and sequence).

### &lt;componentBinding&gt; element

``` {.xml}
<elementBinding|attributeBinding|complexTypeBinding|groupBinding
    name = xsd:string>
   ((java-class|interface|member|contentMember),
     elementBinding*,
     attributeBinding*,
     complexTypeBinding*,
     groupBinding*)
</elementBinding|attributeBinding|complexTypeBinding|groupBinding>
```

| Name   | Description
| ------ | ---------------------------------------------------------------------------
| name   | The name of the XML schema component for which we are defining a binding.
[componentBinding - Definitions]

These elements are the tenets of the binding file since they contain the
binding definition for an XML schema element, attribute, complex type
and model group definition. The first child element (`<java-class/>`,
`<interface>`, `<member>` or `<contentMember/>`) will determine the type
of binding one is defining. Please note that defining a `<java-class>`
binding on an XML schema attribute will have absolutely no effect.

The binding file is written from an XML schema point of view; there are
two distinct ways to define the XML schema component for which we are
defining a binding.

1.  (XPath-style) name

2.  Embedded definitions

#### Name

First we can define it through the `name` attribute.

The value of the name attribute uniquely identifies the XML schema
component. It can refer to the top-level component using the NCName of
that component or it can use a location language based on
[XPath](http://www.w3.org/TR/xpath). The grammar of that language can be
defined by the following
[BNF](http://en.wikipedia.org/wiki/Backus-Naur_form):

    [1]Path         ::= '/'LocationPath('/'LocationPath)*
    [2]LocationPath ::= (Complex|ModelGroup|Attribute|Element|Enumeration)
    [3]Complex      ::= 'complexType:'(NCName)
    [4]ModelGroup   ::= 'group:'NCName
    [5]Attribute    ::= '@'NCName
    [6]Element      ::= NCName
    [7]Enumeration  ::= 'enumType':(NCName)
                        

Please note that all values for the `name` attribute have to start with
a `'/'`.

#### Embedded definitions

The second option to identify an XML schema component is to embed its
binding definition inside its parent binding definition.

Considering below XML schema fragment ...

``` {.xml}
<complexType name="fooType">
    <sequence>
        <element name="foo" type="string" />
    </sequence>
</complexType>
```

the following binding definitions are equivalent and identify the
&lt;element&gt; `foo` defined in the top-level &lt;complexType&gt;
`fooType`.

``` {.xml}
<elementBinding name="/complexType:fooType/foo>
   <member name="MyFoo" handler="mypackage.myHandler"/>
</elementBinding>

<complexTypeBinding name="/fooType">
   <elementBinding name="/foo>
      <member name="MyFoo" handler="mypackage.myHandler"/>
   </elementBinding>
<complexTypeBinding>
```

### &lt;java-class&gt;

``` {.xml}
<java-class
    name? = xsd:string
    package? = xsd:string
    final? = xsd:boolean
    abstract? = xsd:boolean
    equals? = xsd:boolean
    bound? = xsd:boolean
    (implements*,extends?)
</java-class>
```

This element defines all the options for the class to be generated,
including common properties such as class name, package name, and so on.

**name:**

:   The name of the class that will be generated.

**package:**

:   The package of the class to be generated. if set, this option
    overrides the mapping defined in the `<package/>` element.
    
**final:**

:   If true, the generated class will be final.

**abstract:**

:   If true, the generated class will be abstract.

**equals:**

:   If true, the generated class will implement the `equals()` and
    `hashCode()` method.
    
**bound:**

:   If true, the generated class will implement bound properties,
    allowing property change notification.

For instance, the following binding definition instructs the source
generator to generate a class `CustomTest` for a global element named
'test', replacing the default class name `Test` with `CustomTest`.

``` {.xml}
<elementBinding name="/test">
   <java-class name="CustomTest" final="true"/>
</elementBinding>
```

In addition to the properties listed above, it is possible to define
that the class generated will extend a class given and/or implement one
or more interfaces.

For instance, the following binding definition instructs the source
generator to generate a class `TestWithInterface` that implements the
interface `org.castor.sample.SomeInterface` in addition to
`java.io.Serializable`.

``` {.xml}
<elementBinding name="/test">
   <java-class name="TestWithInterface">
      <implements>org.castor.sample.SomeInterface</implements>
   </java-class>
</elementBinding>
                 
```

The subsequent binding definition instructs the source generator to
generate a class `TestWithExtendsAndInterface` that implements the
interface `org.castor.sample.SomeInterface` in addition to
`java.io.Serializable`, and extends from a (probably abstract) base
class `SomeAbstractBaseClass`.

``` {.xml}
<elementBinding name="/test">
   <java-class name="TestWithExtendsAndInterface">
      <extends>org.castor.sample.SomeAbstractBaseClass</extends>
      <implements>org.castor.sample.SomeInterface</implements>
   </java-class>
</elementBinding>                 
```

The generated class `SomeAbstractBaseClass` will have a class signature
as shown below:

``` {.java}
...

public class TestWithExtendsAndInterface
   extends SomeAbstractBaseClass
   implements SomeInterface, java.io.Serializable {
   ...                 
```

### &lt;member&gt; element

``` {.xml}
 <member
  name? = xsd:string
  java-type? = xsd:string
  wrapper? = xsd:boolean
  handler? = xsd:string
  visibility? = (public|protected|private)
  collection? = (array|vector|arraylist|hashtable|collection|odmg|set|map|sortedset)
  validator? = xsd:string/>
```

This element represents the binding for class member. It allows the
definition of its name and java type as well as a custom implementation
of FieldHandler to help the Marshaling framework in handling that
member. Defining a validator is also possible. The names given for the
validator and the fieldHandler must be fully qualified.

| Name         | Description
| ------------ | -----------
| name         | The name of the class member that will be generated.
| java-type    | Fully qualified name of the java type.
| wrapper      | If true, a wrapper object will be generated in case the Java type is a java primitive.
| handler      | Fully qualified name of the custom FieldHandler to use.
| collection   | If the schema component can occur more than once then this attribute allows specifying the collection to use to represent the component in Java.
| validator    | Fully qualified name of the FieldValidator to use.
| visibility  |  A custom visibility of the content class member generated, with the default being `public`.
[member - Definitions]

For instance, the following binding definition:

``` {.xml}
<elementBinding name="/root/members">
   <member collection="set"/>
</elementBinding>
```

instructs the source generator to generate -- within a class `Root` -- a
Java member named `members` using the collection type `java.util.Set`
instead of the default `java.util.List`:

``` {.java}
         public class Root {
         
            private java.util.Set members;
         
            ...
         
         }
```

The following (slightly amended) binding element:

``` {.xml}
<elementBinding name="/root/members">
   <member name="memberSet" collection="set"/>
</elementBinding>
```

instructs the source generator to generate -- again within a class
`Root` -- a Java member named `memberSet` (of the same collection type
as in the previous example), overriding the name of the member as
specified in the XML schema:

``` {.java}
public class Root {

   private java.util.Set memberSet;

   ...

}
```

### &lt;contentMember&gt; element

``` {.xml}
          <contentMember
           name? = xsd:string
           visiblity? = (public|protected|private)
```

This element represents the binding for *content* class member generated
as a result of a mixed mode declaration of a complex type definition. It
allows the definition of its name and its visibility

**name:**

:   The name of the class member that will be generated, overriding the
    default name of `_content`.

**visibility:**

:   A custom visibility of the content class member generated, with the
    default being `public`.

For a complex type definition declared to be *mixed* such as follows ...

``` {.xml}
<complexType name="RootType" mixed="true">
   <sequence>
      ...
   >/sequence>
>/complexType>
```

... the following binding definition ...

``` {.xml}
<elementBinding name="/complexType:RootType">
   <contentMember name="customContentMember"/>
</elementBinding>
```

instructs the source generator to generate -- within a class `RootType`
-- a Java member named `customContentMember` of type `java.lang.String`:

``` {.java}
public class RootType {

   private java.util.String customContentMember;

   ...
}
```

### &lt;enumBinding&gt; element

``` {.xml}
<enumBinding>
   (enumDef)
</enumBinding>

<enumDef>
   (enumClassName = xsd:string, enumMember*)
</enumDef>

<enumMember>
   (name = xsd:string, value = xsd:string)
</enumMember>
```

The `<enumBinding>` element allows more control on the code generated
for type-safe enumerations, which are used to represent an XML Schema
`<simpleType>` enumeration.

For instance, given the following XML schema enumeration definition:

``` {.xml}
<xs:simpleType name="durationUnitType">
  <xs:restriction base='xs:string'>
    <xs:enumeration value='Y' />
    <xs:enumeration value='M' />
    <xs:enumeration value='D' />
    <xs:enumeration value='h' />
    <xs:enumeration value='m' />
    <xs:enumeration value='s' />
  </xs:restriction>
</simpleType>
```

the Castor code generator would generate code where the default naming
convention used during the generation would overwrite the first constant
definition for value '`M`' with the one generated for value '`m`'.

The following binding definition defines -- through the means of an
`<enumMember>` definition for the enumeration value '`M`' -- a special
binding for this value:

``` {.xml}
<enumBinding name="/enumType:durationUnitType">
  <enum-def>
    <enumMember>
      <value>M</value>
      <javaName>CUSTOM_M</javaName>
    </enumMember>
  </enum-def>
</enumBinding>
```

and instructs the source generator to generate -- within a class
`DurationUnitType` -- a constant definition named `CUSTOM_M` for the
enumeration value `M`.

### Not implemented yet

#### &lt;javadoc&gt;

The `<javadoc>` element allows one to enter the necessary JavaDoc
representing the generated classes or members.

#### &lt;interface&gt; element

``` {.xml}
<interface>
  name = xsd:string
</interface>
```

-   **name:**The name of the interface to generate.

This element specifies the name of the interface to be generated for an
XML schema component.

Class generation conflicts
--------------------------

As mentioned previously, you use a binding file for two main reasons:

-   To customize the Java code generated

-   To avoid class generation conflicts.

For the latter case, you'll (often) notice such collisions by looking at
generated Java code that frequently does not compile. Whilst this is
relatively easy for small(ish) XML schema(s), this task gets tedious for
more elaborate XML schemas. To ease your life in the context of this
'collision detection', the Castor XML code generator provides you with a
few advanced features. The following sections cover these features in
detail.

### Collision reporting

During code generation, the Castor XML code generator will run into
situations where a class (about to be generated, and as such about to be
written to the file system) will overwrite an already existing class.
This, for example, is the case if within one XML schema there's two
(local) element definitions within separate complex type definitions
with the same name. In such a case, Castor will emit warning messages
that inform the user that a class will be overwritten.

As of release 1.1, the Castor XML code generator supports two *reporting
modes* that allow different levels of control in the event of such
collisions, `warnViaConsoleDialog` and `informViaLog` mode.

| Mode                     | Description                                                                 | Since
| ------------------------ | --------------------------------------------------------------------------- | -------
| `warnViaConsoleDialog`   | Emits warning messages to `stdout` and ask the users whether to continue.   | 0.9
| `informViaLog`           | Emits warning messages only via the standard logger.                        | 1.1
[column - Definitions]

Please select the reporting mode of your choice according to your needs,
the default being `warnViaConsoleDialog`. Please note that the
`informViaLog` reporting mode should be the preferred choice when using
the XML code generator in an automated environment.

In general, the warning messages produced are very useful in assisting
you in your creation of the binding file, as shown in below example for
the `warnViaConsoleDialog` mode:

     Warning: A class name generation conflict has occurred between element
             '/Data/OrderReceipt/LineItem' and element '/Data/PurchaseOrder/LineItem'.
             Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
     Warning: A class name generation conflict has occurred between element
             '/Data/OrderReceipt/LineItem' and element '/Data/PurchaseOrder/LineItem'.
             Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
     Warning: A class name generation conflict has occurred between element
             '/Data/OrderReceipt/LineItem' and element '/Data/PurchaseOrder/LineItem'.
             Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
     Warning: A class name generation conflict has occurred between element
             'complexType:ReceiptLineItemType/Sku' and element 'complexType:LineItemType/Sku'.
             Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
     Warning: A class name generation conflict has occurred between element
             'complexType:ReceiptLineItemType/Sku' and element 'complexType:LineItemType/Sku'.
             Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
     Warning: A class name generation conflict has occurred between element
             'complexType:ReceiptLineItemType/Sku' and element 'complexType:LineItemType/Sku'.
             Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
                             

#### Reporting mode 'warnViaConsoleDialog'

As already mentioned, this mode emits warning messages to `stdout`, and
asks you whether you want to continue with the code generation or not.
This allows for very fine grained control over the extent of the code
generation.

Please note that there is several *setter* methods on the
`org.exolab.castor.builder.SourceGenerator` that allow you to fine-tune
various settings for this reporting mode. Genuinely, we believe that for
automated code generation through either Ant or Maven, the new
`informViaLog` is better suited for these needs.

### Automatic collision resolution

As of Castor 1.1.1, support has been added to the Castor XML code
generator for a (nearly) automatic conflict resolution. To enable this
new mode, please override the following property in your custom property
file as shown below:

     # Specifies whether automatic class name conflict resolution
     # should be used or not; defaults to false.
     #
     org.exolab.castor.builder.automaticConflictResolution=true

As a result of enabling automatic conflict resolution, Castor will try
to resolve such name collisions automatically, using one of the
following two strategies:

| Name      | Description                                                          | Since   | Default
| --------- | -------------------------------------------------------------------- | ------- | ---------
| `xpath`   | Prepends an XPATH fragment to make the suggested Java name unique.   | 1.1.1   | Yes
| `type`    | Appends type information to the suggested Java name.                 | 1.1.1   | No
[column - Definitions]

#### Selecting the strategy

For selecting one of the two strategies during XML code generation,
please see the documentation for the following code artifacts:

-   setClassNameConflictResolver on  `org.exolab.castor.builder.SourceGenerator`
-   `org.exolab.castor.builder.SourceGeneratorMain"`
-   Ant task definition
-   Maven plugin for Castor XML

In order to explain the *modus operandi* of these two modes, please
assume two complex type definitions `AType` and `BType` in an XML
schema, with both of them defining a local element named `c`.

``` {.xml}
<xs:complexType name="AType">
    <xs:sequence>
        <xs:element name="c" type="CType1" />
    </xs:sequence>
</xs:complexType>            

<xs:complexType name="BType">
    <xs:sequence>
        <xs:element name="c" type="CType2" />
    </xs:sequence>
</xs:complexType>
```

Without automatic collision resolution enabled, Castor will create
identically named classes `C.java` for both members, and one will
overwrite the other. Please note the different types for the two `c`
element definitions, which requires two class files to be generated in
order not to lose this information.

#### 'XPATH' strategy

This strategy will prepend an XPATH fragment to the default Java name as
derived during code generation, the default name (frequently) being the
name of the XML schema artifact, e.g. the element name of the complex
type name. The XPATH fragment being prepended is minimal in the sense
that the resulting rooted XPATH is unique for the XML schema artifact
being processed.

With automatic collision resolution enabled and the strategy 'XPATH'
selected, Castor will create the following two classes, simply
prepending the name of the complex type to the default element name:

-   ATypeC.java

-   BTypeC.java

#### 'TYPE' strategy

This strategy will append 'type' information to the default Java name as
derived during code generation, the default name (frequently) being the
name of the XML schema artifact, e.g. the element name of the complex
type name.

With automatic collision resolution enabled and the strategy 'TYPE'
selected, Castor will create the following two classes, simply appending
the name of the complex type to the default element name (with a default
'`By`' inserted):

-   CByCType1.java

-   CByCType2.java

To override the default '`By`' inserted between the default element name
and the type information, please override the following property in your
custom property file as shown below:

    # Property specifying the 'string' used in type strategy to be inserted 
    # between the actual element name and the type name (during automatic class name 
    # conflict resolution); defaults to 'By'.
    org.exolab.castor.builder.automaticConflictResolutionTypeSuffix=ByBy

#### Conflicts covered

The Castor XML code generator, with automatic collision resolution
enabled, is capable of resolving the following collisions automatically:

-   Name of local element definition same as name of a global element

-   Name of local element definition same as name of another local
    element definition.

> **Note**
>
> Please note that *collision resolution* for a local to local collision
> will only take place for the second local element definition
> encountered (and subsequent ones).

Invoking the XML code generator
===============================

Ant task
--------

An alternative to using the command line as shown in the previous
section, the Castor Source Generator Ant Task can be used to call the
source generator for class generation. The only requirement is that the
castor-&lt;version&gt;-codegen-antask.jar must additionally be on the
CLASSPATH.

### Specifying the source for generation

As shown in the subsequent table, there's multiple ways of specifying
the input for the Castor code generator. **At least one** input source
has to be specified.

| Attribute       | Description                                                                                       | Required   | Since
| --------------- | ------------------------------------------------------------------------------------------------- | ---------- | --------
| **file**        | The XML schema, to be used as input for the source code generator.                                | No.        |  
| **dir**         | Sets a directory such that all XML schemas in this directory will have code generated for them.   | No         |  
| **schemaURL**   | URL to an XML schema, to be used as input for the source code generator.                          | No.        | **1.2**
[column - Definitions]

In addition, a nested **&lt;fileset&gt;** can be specified as the source
of input. Please refer to the samples shown below.

### Parameters

Please find below the complete list of parameters that can be set on the
Castor source generator to fine-tune the execution behavior.

  Attribute                       Description                                                                                                                                                                                                                                       Required                                                            Since
  ------------------------------- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ------------------------------------------------------------------- -----------
  **package**                     The default package to be used during source code generation.                                                                                                                                                                                     No; if not given, all classes will be placed in the root package.   -
  **todir**                       The destination directory to be used during source code generation. In this directory all generated Java classes will be placed.                                                                                                                  No                                                                  -
  **bindingfile**                 A Castor source generator binding file.                                                                                                                                                                                                           No                                                                  -
  **lineseparator**               Defines whether to use Unix- or Windows- or Mac-style line separators during source code generation. Possible values are: 'unix', 'win' or 'mac'.                                                                                                 No; if not set, system property 'line.separator' is used instead.   -
  **types**                       Defines what collection types to use (Java 1 vs. Java 2). Possible values: 'vector', 'arraylist' (aka 'j2') or 'odmg'.                                                                                                                            No; if not set, the default collection used will be Java 1 type     -
  **verbose**                     Whether to output any logging messages as emitted by the source generator                                                                                                                                                                         No                                                                  -
  **warnings**                    Whether to suppress any warnings as otherwise emitted by the source generator                                                                                                                                                                     No                                                                  -
  **nodesc**                      If used, instructs the source generator not to generate \*Descriptor classes.                                                                                                                                                                     No                                                                  -
  **generateMapping**             If used, instructs the source generator to (additionally) generate a mapping file.                                                                                                                                                                No                                                                  -
  **nomarshal**                   If specified, instructs the source generator not to create (un)marshalling methods within the Java classes generated.                                                                                                                             No                                                                  -
  **caseInsensitive**             If used, instructs the source generator to generate code for enumerated type lookup in a case insensitive manner.                                                                                                                                 No                                                                  -
  **sax1**                        If used, instructs the source generator to generate SAX-1 compliant code.                                                                                                                                                                         No                                                                  -
  **generateImportedSchemas**     If used, instructs the source generator to generate code for imported schemas as well.                                                                                                                                                            No                                                                  -
  **nameConflictStrategy**        If used, sets the name conflict strategy to use during XML code generation; possible values are '`warnViaConsoleDialog`' and '`informViaLog`'.                                                                                                    No                                                                  -
  **properties**                  Location of file defining a set of properties to be used during source code generation. This overrides the default mechanisms of configuring the source generator through a `castorbuilder.properties` (that has to be placed on the CLASSPATH)   No                                                                  -
  **automaticConflictStrategy**   If used, sets the name conflict resolution strategy used during XML code generation; possible values are '`type`' and '`xpath`' (default being '`xpath`').                                                                                        No                                                                  -
  **jClassPrinterType**           Sets the mode for printing JClass instances during XML code generation; possible values are '`standard`' and '`velocity`' (default being '`standard`').                                                                                           No                                                                  **1.2.1**
  **generateJdoDescriptors**      If used, instructs the source generator to generate JDO class descriptors as well; default is false.                                                                                                                                              No                                                                  **1.3**
  **resourceDestination**         Sets the destination directory for (generated) resources, e.g. `.castor.cdr` files.                                                                                                                                                               No                                                                  **1.3.1**

  : Ant task properties

### Examples

#### Using a file

Below is an example of how to use this task from within an Ant target
definition named 'castor:gen:src':

``` {.xml}
<target name="castor:gen:src" depends="init"
         description="Generate Java source files from XSD.">

    <taskdef name="castor-srcgen"
             classname="org.castor.anttask.CastorCodeGenTask"
             classpathref="castor.class.path" />
    <mkdir dir="generated" />
    <castor-srcgen file="src/schema/sample.xsd"
                   todir="generated-source"
                   package="org.castor.example.schema"
                   types="j2"
                   warnings="true" />
</target>            
```

#### Using an URL

Below is the same sample as above, this time using the **url** attribute
as the source of input instead:

``` {.xml}
<target name="castor:gen:src" depends="init"
         description="Generate Java source files from XSD.">

    <taskdef name="castor-srcgen"
             classname="org.castor.anttask.CastorCodeGenTask"
             classpathref="castor.class.path" />
    <mkdir dir="generated" />
    <castor-srcgen schemaURL="http://some.domain/some/path/sample.xsd"
                   todir="generated-source"
                   package="org.castor.example.schema"
                   types="j2"
                   warnings="true" />
</target>            
```

#### Using a nested &lt;fileset&gt;

Below is the same sample as above, this time using the **url** attribute
as the source of input instead:

``` {.xml}
<target name="castor:gen:src" depends="init"
         description="Generate Java source files from XSD.">

    <taskdef name="castor-srcgen"
             classname="org.castor.anttask.CastorCodeGenTask"
             classpathref="castor.class.path" />
    <mkdir dir="generated" />
    <castor-srcgen todir="generated-source"
                   package="org.castor.example.schema"
                   types="j2"
                   warnings="true" >
       <fileset dir="${basedir}/src/schema">
          <include name="**/*.xsd"/>
       </fileset>
    </castor-srcgen>
</target>            
```

Maven 2 plugin
--------------

For those working with Maven 2 instead of Ant, the Maven 2 plugin
for Castor can be used to integrate source code generation from XML
schemas with the Castor XML code generator as part of the standard Maven
build life-cycle. The following sections show how to configure the Maven
2 Castor plugin and hwo to instruct Maven 2 to generate sources from
your XML schemas.

### Configuration

To be able to start source code generation from XML schema from within
Maven, you will have to configure the Maven 2 Castor plugin as follows:

``` {.xml}
<plugin>
   <groupId>org.codehaus.mojo</groupId>
   <artifactId>castor-maven-plugin</artifactId>
   <version>2.0</version>
</plugin>
```

Above configuration will trigger source generation using the default
values as explained at the [Castor plugin
page](http://mojo.codehaus.org/castor-maven-plugin/howto.html), assuming
that the XML schema(s) are located at `src/main/castor`, and code will
be saved at `target/generated-sources/castor`. When generating sources
for multiple schemas at the same time, you can put namespace to package
mappings into `src/main/castor/castorbuilder.properties`.

To e.g. change some of these default locations, please add a
&lt;configuration&gt; section to the plugin configuration as follows:

``` {.xml}
<plugin>
   <groupId>org.codehaus.mojo</groupId>
   <artifactId>castor-maven-plugin</artifactId>
   <version>2.0</version>
   <configuration>
      <schema>src/main/resources/org/exolab/castor/builder/binding/binding.xsd</schema>
      <packaging>org.exolab.castor.builder.binding</packaging>
      <properties>src/main/resources/org/exolab/castor/builder/binding.generation.properties</properties>
   </configuration>
 </plugin>      
```

Details on the available configuration properties can be found
[here](http://mojo.codehaus.org/castor-maven-plugin/generate-mojo.html).

By default, the Maven Castor plugin has been built and tested against a
particular version of Castor. To switch to a newer version of Castor
(not the plugin itself), please use a &lt;dependencies&gt; section as
shown below to point the plugin to e.g. a newer version of Castor:

``` {.xml}
<plugin>
   <groupId>org.codehaus.mojo</groupId>
   <artifactId>castor-maven-plugin</artifactId>
   <version>2.0</version>
   <dependencies>
     <dependency> 
       <groupId>org.codehaus.castor</groupId>
       <artifactId>castor</artifactId>
       <version>1.3.1-SNAPSHOT</version>
     </dependency>
   </dependencies>
 </plugin>      
```

### Integration into build life-cycle

To integrate source code generation from XML schema into your standard
build life-cycle, you will have to add an &lt;executions&gt; section to
your standard plugin configuration as follows:

``` {.xml}
<plugin>
   <groupId>org.codehaus.mojo</groupId>
   <artifactId>castor-maven-plugin</artifactId>
   <version>2.0</version>
   <executions>
      <execution>
         <goals>
            <goal>generate</goal>
         </goals>
      </execution>
   </executions>            
</plugin>       
```

### Example

Below command shows how to instruct Maven (manually) to generate Java
sources from the XML schemas as configured above.

``` {.xml}
> mvn castor:generate
```

Command line
------------

### First steps

``` {.java}
java org.exolab.castor.builder.SourceGeneratorMain -i foo-schema.xsd \
    -package com.xyz            
```

This will generate a set of source files from the the XML Schema
`foo-schema.xsd` and place them in the package `com.xyz`.

To compile the generated classes, simply run `javac` or your favorite
compiler:

    javac com/xyz/*.java

Created class will have marshal and unmarshal methods which are used to
go back and forth between XML and an Object instance.

### Source Generator - command line options

The source code generator has a number of different options which may be
set. Some of these are done using the command line and others are done
using a properties file located by default at
`org/exolab/castor/builder/castorbuilder.properties`.

#### Specifying the input source

There's more than one way of specifying the input for the Castor code
generator. **At least one** input source must be specified.

|  Option  | Args         | Description                 | Version
| -------- | ------------ | --------------------------- | -------------------
| i        | *filename*   | The input XML Schema file   | 
| is       | *URL*        | URL of an XML Schema        | **1.2 and newer**
[Input sources]

#### Other command Line Options

| Option                       | Arguments                                | Description                                                                                                                                                                                                                                   | Optional?
| ---------------------------- | ---------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- -----------
|  `-package`                  | package-name                             | The package for the generated source.                                                                                                                                                                                                         | Optional
| `-dest`                      | path                                     | The destination directory in which to create the generated source                                                                                                                                                                             | Optional
| `-line-separator`            | unix / mac / win                         | Sets the line separator style for the desired platform. This is useful if you are generating source on one platform, but will be compiling/modifying on another platform.                                                                     | Optional
| `-types`                     | type-factory                             | Sets which type factory to use. This is useful if you want JDK 1.2 collections instead of JDK 1.1 or if you want to pass in your own FieldInfoFactory.   | Optional
|  `-h`                        |                                          | Shows the help/usage information.                                                                                                                                                                                                             | Optional
|  `-f`                        |                                          | Forces the source generator to suppress all non-fatal errors, such as overwriting pre-existing files.                                                                                                                                         | Optional
| `-nodesc`                    |                                          | Do not generate the class descriptors                                                                                                                                                                                                         | Optional
| `-gen-mapping`               |                                          | (Additionally) Generate a mapping file.                                                                                                                                                                                                       Optional
| `-nomarshall`                |                                          | Do not generate the marshaling framework methods (marshal, unmarshal, validate)                                                                                                                                                               | Optional
| `-testable`                  |                                          | Generate the extra methods used by the CTF (Castor Testing Framework)                                                                                                                                                                         | Optional
| `-sax1`                      |                                          | Generate marshaling methods that use the SAX1 framework (default is false).                                                                                                                                                                   | Optional
| `-binding-file`             |  &lt;&lt;binding file name&gt;&gt;.       | Configures the use of a Binding File to allow finely-grained control of the generated classes                                                                                                                                                 | Optional
| `-generateImportedSchemas`   |                                          | Generates sources for imported XML Schemas in addition to the schema provided on the command line (default is false).                                                                                                                         | Optional
|   `-case-insensitive`        |                                          | The generated classes will use a case insensitive method for looking up enumerated type values.                                                                                                                                               | Optional
| `-verbose`                   |                                          | Enables extra diagnostic output from the source generator                                                                                                                                                                                     | Optional
| `-nameConflictStrategy`      | &lt;&lt;conflict strategy name&gt;&gt;   | Sets the name conflict strategy to use during XML code generation                                                                                                                                                                             | Optional
| `-fail`                      |                                          | Instructs the source generator to fail on the first error. When you are trying to figure out what is failing during source generation, this option will help.                                                                                 | Optional
| `-classPrinter`              | &lt;&lt;JClass printing mode&gt;&gt;.    | Specifies the JClass printing mode to use during XML code generation; possible values are`standard` (default) and `velocity`; if no value is specified, the default mode is `standard`.                                                       | Optional
| `-gen-jdo-desc`              |                                          | (Additionally) generate JDO class descriptors.                                                                                                                                                                                                | Optional
| `-resourcesDestination`      | &lt;destination&gt;                      | An (optional) destination for (generated) resources                                                                                                                                                                                           | Optional
[Other command line options]

##### Collection Types

The source code generator has the ability to use the following types of
collections when generating source code, using the `-type` option:

| Option value    | Type       | Default
| --------------- | ---------- | ------------------------
| `-types j1`     | Java 1.1   | `java.util.Vector`
| `-type j2`      | Java 1.2   | `java.util.Collection`
| `-types odmg`   | ODMG 3.0   | `odmg.DArray`
[Collection types]

The Java class name shown in above table indicates the default
collection type that will be emitted during generation.

You can also write your own FieldInfoFactory to handle specific
collection types. All you have to do is to pass in the fully qualified
name of that FieldInfoFactory as follows:

``` {.xml}
-types com.personal.MyCoolFactory
```

> **Tip**
>
> For additional information about the Source Generator and its options,
> you can download the
> Source Generator User Document (PDF)
> . Please note that the use of a binding file is not dicussed in that
> document.

XML schema support
==================

Castor XML supports the [W3C XML Schema 1.0 Second Edition
Recommendation document (10/28/2004)](http://www.w3.org/TR/xmlschema-1/)
The Schema Object Model (located in the package
org.exolab.castor.xml.schema) provides an in-memory representation of a
given XML schema whereas the XML code generator provides a binding
between XML schema data types and structures into the corresponding ones
in Java.

The Castor Schema Object Model can read
(org.exolab.castor.xml.schema.reader) and write
(org.exolab.castor.xml.schema.writer) an XML Schema as defined by the
W3C recommandation. It allows you to create and manipulate an in-memory
view of an XML Schema.

The Castor Schema Object Model supports the W3C XML Schema
recommendation with no limitation. However the Source Generator does
currently not offer a one to one mapping from an XML Schema component to
a Java component for every XML Schema components; some limitations
exist. The aim of the following sections is to provide a list of
supported features in the Source Generator. Please keep in mind that the
Castor Schema Object Model again can handle any XML Schema without
limitations.

Some Schema types do not have a corresponding type in Java. Thus the
Source Generator uses Castor implementation of these specific types
(located in the org.exolab.castor.types package). For instance the
`duration` type is implemented directly in Castor. Remember that the
representation of XML Schema datatypes does not try to fit the W3C XML
Schema specifications exactly. The aim is to map an XML Schema type to
the Java type that is the best fit to the XML Schema type.

You will find next a list of the supported XML Schema data types and
structures in the Source Code Generator. 

Supported XML Schema Built-in Datatypes
---------------------------------------

The following is a list of the supported datatypes with the
corresponding facets and the Java mapping type.

### Primitive Datatypes

| XML Schema Type   | Supported Facets                                                                                                                    | Java mapping type
| ----------------- | ----------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------
| anyURI            | enumeration                                                                                                                         | `java.lang.String`
| base64Binary      |                                                                                                                                     | `byte[]`
| boolean           | pattern                                                                                                                             | `boolean` or `java.lang.Boolean`[^3]
| date              | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace[^4]                                        | `org.exolab.castor.types.Date`
| dateTime          | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `java.util.Date`
| decimal           | totalDigits, fractionDigits, pattern, whiteSpace, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace   | `java.math.BigDecimal`
| double            | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `double` or `java.lang.Double`
| duration          | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `org.exolab.castor.types.Duration`
| float             | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `float` or `java.lang.Float`
| gDay             |  enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `org.exolab.castor.types.GDay`
| gMonth            | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `org.exolab.castor.types.GMonth`
| gMonthDay         | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `org.exolab.castor.types.GMonthDay`
| gYear             | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `org.exolab.castor.types.GYear`
| gYearMonth        | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `org.exolab.castor.types.GYearMonth`
| hexBinary         |                                                                                                                                     | `byte[]`
| QName             | length, minLength, maxLength, pattern, enumeration                                                                                  | `java.lang.String`
| string            | length, minLength, maxLength, pattern, enumeration, whiteSpace                                                                      | `java.lang.String`
| time              | enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, pattern, whitespace                                            | `org.exolab.castor.types.Time`
[Supported primitive data types]

### Derived Datatypes

| Type                 | Supported Facets                                                                                                                | Java mapping type
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------
| byte                 | totalDigits, fractionDigits[^5], pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace[^6]   | `byte`/`java.lang.Byte` [^7]
| ENTITY               |                                                                                                                                 | **Not implemented**
| ENTITIES              |                                                                                                                                | **Not implemented**
| ID                   | enumeration                                                                                                                     | `java.lang.String`
| IDREF                |                                                                                                                                 | `java.lang.Object`
| IDREFS                |                                                                                                                                | `java.util.Vector` of `java.lang.Object`
| int                  | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `int`/`java.lang.Integer`
| integer              | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `long`/`java.lang.Long`
| language             | length, minLength, maxLength, pattern, enumeration, whiteSpace                                                                  | treated as a `xsd:string`[^8]
| long                 | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `long`/`java.lang.Long`
| Name                 |                                                                                                                                 | **Not implemented**
| NCName               | enumeration                                                                                                                     | `java.lang.String`
| negativeInteger      | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `long`/`java.lang.Long`
| NMTOKEN              | enumeration, length, maxlength, minlength                                                                                       | `java.lang.String`
| NMTOKENS             |                                                                                                                                 | `java.util.Vector` of `java.lang.String`
|   NOTATION           |                                                                                                                                   | **Not implemented**
| nonNegativeInteger   | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `long`/`java.lang.Long`
| nonPositiveInteger   | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `long`/`java.lang.Long`
| normalizedString     | enumeration, length, minLength, maxLength, pattern                                                                              | `java.lang.String`
| positiveInteger      | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `long`/`java.lang.Long`
| short                | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `short`/`java.lang.Short`
| token                | length, minLength, maxLength, pattern, enumeration, whiteSpace                                                                  | treated as a `xsd:string`,
| unsignedByte         | totalDigits, fractionDigits, maxExclusive, minExclusive, maxInclusive, minInclusive, pattern, whitespace                        | `short`/`java.lang.Short`
| unsignedInt          | totalDigits, fractionDigits, maxExclusive, minExclusive, maxInclusive, minInclusive, pattern, whitespace                        | `long`/`java.lang.Long`
| unsignedLong         | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `java.math.BigInteger`
| unsignedShort        | totalDigits, fractionDigits, pattern, enumeration, maxInclusive, maxExclusive, minInclusive, minExclusive, whitespace           | `int` or `java.lang.Integer`
[Supported derived data types]

Supported XML Schema Structures
-------------------------------

Supporting XML schema structures is a constant work. The main structures
are already supported with some limitations. The following will give you
a rough list of the supported structures. For a more detailed support of
XML Schema structure in the Source Generator or in the Schema Object
Model, please refer to [Source Generator User Document
(PDF)](http://www.castor.org/SourceGeneratorUser.pdf).

Supported schema components:

-   Attribute declaration (`<attribute>`)

-   Element declaration (`<element>`)

-   Complex type definition (`<complexType>`)

-   Attribute group definition (`<attributeGroup>`)

-   Model group definition (`<group>`)

-   Model group (`<all>`, `<choice>` and `<sequence>`)

-   Annotation (`<annotation>`)

-   Wildcard (`<any>`)

-   Simple type definition (`<simpleType>`)

### Groups

Grouping support covers both **model group definitions** (`<group>`) and
**model groups** (`<all>`, `<choice>` and `<sequence>`). In this section
we will label as a 'nested group' any model group whose first parent is
another model group.

-   For each top-level model group definition, a class is generated
    either when using the 'element' mapping property or the 'type' one.

-   If a group -- nested or not -- appears to have `maxOccurs > 1` ,
    then a class is generated to represent the items contained in
    the group.

-   For each nested group, a class is generated. The name of the
    generated class will follow this naming convention:
    `Name,Compositor+,Counter?` where

    -   'Name' is name of the top-level component (element, complexType
        or group).

    -   'Compositor' is the compositor of the nested group. For
        instance, if a 'choice' is nested inside a sequence, the value
        of Compositor will be `SequenceChoice` ('Sequence'+'Choice').
        Note: if the 'choice' is inside a Model Group and that Model
        Group **parent** is a Model Group Definition or a complexType
        then the value of'Compositor' will be only 'Choice'.

    -   'Counter' is a number that prevents naming collision.

### Wildcard

`<any>` is supported and will be mapped to an `AnyNode` instance.
However, full namespace validation is not yet implemented, even though
an `AnyNode` structure is fully namespace aware.

`<anyAttribute>` is currently not supported. It is a work in progress.

Examples
========

In this section we illustrate the use of the XML code generator by
discussing the classes generated from given XML schemas. The XML code
generator is going to be used with the “java class mapping” property set
to *element* (default value).

The invoice XML schema
----------------------

### The schema file

The input file is the schema file given with the XML code generator
example in the distribution of Castor (under
/src/examples/SourceGenerator/invoice.xsd).

``` {.xml}
<?xml version="1.0"?>
<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    targetNamespace="http://castor.exolab.org/Test/Invoice">

    <xsd:annotation>
        <xsd:documentation>
            This is a test XML Schema for Castor XML.
        </xsd:documentation>
    </xsd:annotation>

    <!--
        A simple representation of an invoice. This is simply an example
        and not meant to be an exact or even complete representation of an invoice.
    -->
    <xsd:element name="invoice">
        <xsd:annotation>
            <xsd:documentation>
                A simple representation of an invoice
            </xsd:documentation>
        </xsd:annotation>

        <xsd:complexType>
            <xsd:sequence>
                <xsd:element name="ship-to">
                    <xsd:complexType>
                        <xsd:group ref="customer" />
                    </xsd:complexType>
                </xsd:element>
                <xsd:element ref="item"
                    maxOccurs="unbounded" minOccurs="1" />
                <xsd:element ref="shipping-method" />
                <xsd:element ref="shipping-date" />
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>

    <!-- Description of a customer -->
    <xsd:group name="customer">
        <xsd:sequence>
            <xsd:element name="name" type="xsd:string" />
            <xsd:element ref="address" />
            <xsd:element name="phone"
                type="TelephoneNumberType" />
        </xsd:sequence>
    </xsd:group>

    <!-- Description of an item -->
    <xsd:element name="item">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:element name="Quantity"
                    type="xsd:integer" minOccurs="1" maxOccurs="1" />
                <xsd:element name="Price" type="PriceType"
                    minOccurs="1" maxOccurs="1" />
            </xsd:sequence>
            <xsd:attributeGroup ref="ItemAttributes" />
        </xsd:complexType>
    </xsd:element>

    <!-- Shipping Method -->
    <xsd:element name="shipping-method">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:element name="carrier"
                    type="xsd:string" />
                <xsd:element name="option"
                    type="xsd:string" />
                <xsd:element name="estimated-delivery"
                    type="xsd:duration" />
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>

    <!-- Shipping date -->
    <xsd:element name="shipping-date">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:element name="date" type="xsd:date" />
                <xsd:element name="time" type="xsd:time" />
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>

    <!-- A simple U.S. based Address structure -->
    <xsd:element name="address">
        <xsd:annotation>
            <xsd:documentation>
                Represents a U.S. Address
            </xsd:documentation>
        </xsd:annotation>

        <xsd:complexType>
            <xsd:sequence>
                <!-- street address 1 -->
                <xsd:element name="street1"
                    type="xsd:string" />
                <!-- optional street address 2 -->
                <xsd:element name="street2"
                    type="xsd:string" minOccurs="0" />
                <!-- city-->
                <xsd:element name="city" type="xsd:string" />
                <!-- state code -->
                <xsd:element name="state"
                    type="stateCodeType" />
                <!-- zip-code -->
                <xsd:element ref="zip-code" />
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>

    <!-- A U.S. Zip Code -->
    <xsd:element name="zip-code">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string">
                <xsd:pattern value="[0-9]{5}(-[0-9]{4})?" />
            </xsd:restriction>
        </xsd:simpleType>
    </xsd:element>

    <!-- State Code
        obviously not a valid state code....but this is just
        an example and I don't feel like creating all the valid
        ones.
    -->
    <xsd:simpleType name="stateCodeType">
        <xsd:restriction base="xsd:string">
            <xsd:pattern value="[A-Z]{2}" />
        </xsd:restriction>
    </xsd:simpleType>

    <!-- Telephone Number -->
    <xsd:simpleType name="TelephoneNumberType">
        <xsd:restriction base="xsd:string">
            <xsd:length value="12" />
            <xsd:pattern value="[0-9]{3}-[0-9]{3}-[0-9]{4}" />
        </xsd:restriction>
    </xsd:simpleType>

    <!-- Cool price type -->
    <xsd:simpleType name="PriceType">
        <xsd:restriction base="xsd:decimal">
            <xsd:fractionDigits value="2" />
            <xsd:totalDigits value="5" />
            <xsd:minInclusive value="1" />
            <xsd:maxInclusive value="100" />
        </xsd:restriction>
    </xsd:simpleType>

    <!-- The attributes for an Item -->
    <xsd:attributeGroup name="ItemAttributes">
        <xsd:attribute name="Id" type="xsd:ID" minOccurs="1"
            maxOccurs="1" />
        <xsd:attribute name="InStock" type="xsd:boolean"
            default="false" />
        <xsd:attribute name="Category" type="xsd:string"
            use="required" />
    </xsd:attributeGroup>
</xsd:schema>
```

The structure of this schema is simple: it is composed of a top-level
element which is a complexType with references to other elements inside.
This schema represents a simple invoice: an invoice is a customer
(`customer` top-level group), an article (`item` element), a shipping
method (`shipping-method` element) and a shipping date (`shipping-date`
element). Notice that the `ship-to` element uses a reference to an
`address` element. This `address` element is a top-level element that
contains a reference to a non-top-level element (the `zip-cod` element).
At the end of the schema we have two simpleTypes for representing a
telephone number and a price. The Source Generator is used with the
`element` property set for class creation so a class is going to be
generated for all top-level elements. No classes are going to be
generated for complexTypes and simpleTypes since the simpleType is not
an enumeration.

To summarize, we can expect 7 classes : `Invoice`, `Customer`,
`Address`, `Item`, `ShipTo`, `ShippingMethod` and `ShippingDate` and the
7 corresponding class descriptors. Note that a class is generated for
the top-level group `customer`


To run the source generator and create the source from the `invoice.xsd`
file in a package `test`, we just call in the command line:

``` {.xml}
java -cp %CP% org.exolab.castor.builder.SourceGeneratorMain -i invoice.xsd -package test
```

### The generated code

#### The Item.java class

To simplify this example we now focus on the `item` element.

``` {.xml}
<!-- Description of an item -->
<xsd:element name="item">
  <xsd:complexType>
    <xsd:sequence>
      <xsd:element name="Quantity" type="xsd:integer"
                   minOccurs="1" maxOccurs="1" />
      <xsd:element name="Price" type="PriceType"
                   minOccurs="1" maxOccurs="1" />
    </xsd:sequence>
    <xsd:attributeGroup ref="ItemAttributes" />
  </xsd:complexType>
</xsd:element>

<!-- Cool price type -->
<xsd:simpleType name="PriceType">
  <xsd:restriction base="xsd:decimal">
    <xsd:fractionDigits value="2" />
    <xsd:totalDigits value="5" />
    <xsd:minInclusive value="1" />
    <xsd:maxInclusive value="100" />
  </xsd:restriction>
</xsd:simpleType>

<!-- The attributes for an Item -->
<xsd:attributeGroup name="ItemAttributes">
  <xsd:attribute name="Id" type="xsd:ID" minOccurs="1" maxOccurs="1" />
  <xsd:attribute name="InStock" type="xsd:boolean" default="false" />
  <xsd:attribute name="Category" type="xsd:string" use="required" />
</xsd:attributeGroup>
```

To represent an `Item` object, we need to know its `Id`, the `Quantity`
ordered and the `Price` for one item. So we can expect to find a least
three private variables: a string for the `Id` element, an `int` for the
`quantity` element (see the section on XML Schema support if you want to
see the mapping between a W3C XML Schema type and a java type), but what
type for the `Price` element?

While processing the `Price` element, Castor is going to process the
type of `Price` i.e. the simpleType `PriceType` which base is `decimal`.
Since derived types are automatically mapped to parent types and W3C XML
Schema `decimal` type is mapped to a `java.math.BigDecimal`, the price
element will be a `java.math.BigDecimal`. Another private variable is
created for `quantity`: quantity is mapped to a primitive java type, so
a boolean `has_quantity` is created for monitoring the state of the
quantity variable. The rest of the code is the *getter/setter* methods
and the Marshalling framework specific methods. Please find below the
complete `Item` class (with Javadoc comments stripped off):

``` {.java}
/** 
 * This class was automatically generated with 
 * Castor 1.0.4,
 * using an XML Schema.
 */

package test;

public class Item implements java.io.Serializable {

   //--------------------------/ 
   //- Class/Member Variables -/
   //--------------------------/

   private java.lang.String _id; 

   private int _quantity;

   /** 
    * keeps track of state for field: _quantity 
    */ 
   private boolean _has_quantity;

   private java.math.BigDecimal _price;

   //----------------/ 
   //- Constructors -/ 
   //----------------/

   public Item() { 
      super(); 
   } //-- test.Item()


   //-----------/ 
   //- Methods -/ 
   //-----------/

   public java.lang.String getId() { 
      return this._id; $
   } //-- java.lang.String getId()

   public java.math.BigDecimal getPrice() { 
      return this._price;
   } //-- java.math.BigDecimal getPrice()

   public int getQuantity() {
      return this._quantity;
   } //-- int getQuantity()

   public boolean hasQuantity() { 
      return this._has_quantity;
   } //-- boolean hasQuantity()

   public boolean isValid() {
      try { 
         validate();
      } catch (org.exolab.castor.xml.ValidationException vex) { 
         return false;
      }
      return true;
   } //-- boolean isValid()

   public void marshal(java.io.Writer out) 
   throws org.exolab.castor.xml.MarshalException,org.exolab.castor.xml.ValidationException {
      Marshaller.marshal(this, out);
   } //-- void marshal(java.io.Writer)

   public void marshal(org.xml.sax.DocumentHandler handler) 
   throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
      Marshaller.marshal(this, handler);
   } //-- void marshal(org.xml.sax.DocumentHandler)

   public void setId(java.lang.String _id) {
      this._id = _id;
   } //-- void setId(java.lang.String)

   public void setPrice(java.math.BigDecimal _price) {
      this._price = _price;
   } //-- void setPrice(java.math.BigDecimal)

   public void setQuantity(int _quantity) {
      this._quantity = _quantity;
      this._has_quantity = true;
   } //-- void setQuantity(int)

   public static test.Item unmarshal(java.io.Reader reader) 
   throws org.exolab.castor.xml.MarshalException,org.exolab.castor.xml.ValidationException {
      return (test.Item) Unmarshaller.unmarshal(test.Item.class, reader);
   } //-- test.Item unmarshal(java.io.Reader)

   public void validate() 
   throws org.exolab.castor.xml.ValidationException {
      org.exolab.castor.xml.Validator.validate(this, null);
   } //-- void validate()

}
```

The ItemDescriptor class is a bit more complex. This class is containing
inner classes which are the XML field descriptors for the different
components of an ‘Item’ element i.e. id, quantity and price.

#### The PriceType.java class

TODO ...

#### The Invoice.java class

In this section, we focus on the 'invoice' element as shown again below:

``` {.xml}
<xsd:element name="invoice">
   <xsd:complexType>
      <xsd:sequence>
         <xsd:element name="ship-to">
            <xsd:complexType>
               <xsd:group ref="customer" />
            </xsd:complexType>
         </xsd:element>
         <xsd:element ref="item"    minOccurs="1" maxOccurs="unbounded" />
         <xsd:element ref="shipping-method" />
         <xsd:element ref="shipping-date" />
      </xsd:sequence>
   </xsd:complexType>
</xsd:element>
```

Amongst other things, an `<invoice>` is made up of at least one, but
potentially many `<item>` elements. The Castor XML code generator
creates a Java collection named 'itemList' for this unbounded element
declaration, of type `java.util.List` if the scode generator is used
with the '`arraylist`' field factory.

``` {.java}
    private java.util.List _itemList;
```

If the '`j1`' field factory is used, this will be replaced with ...

``` {.java}
    private java.util.Vector _itemList;
```

The complete class as generated (with irrelevant code parts removed) in
'`j2`' (aka '`arraylist`') mode is shown below:

``` {.java}
public class Invoice implements java.io.Serializable {


    ...
    
    private java.util.List _itemList;
    
    ...

    public Invoice() 
     {
        super();
        this._itemList = new java.util.ArrayList();
    } //-- xml.c1677.invoice.generated.Invoice()

    ...

    public void addItem(xml.c1677.invoice.generated.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        this._itemList.add(vItem);
    } //-- void addItem(xml.c1677.invoice.generated.Item) 

    public void addItem(int index, xml.c1677.invoice.generated.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        this._itemList.add(index, vItem);
    } //-- void addItem(int, xml.c1677.invoice.generated.Item) 

    public java.util.Enumeration enumerateItem()
    {
        return java.util.Collections.enumeration(this._itemList);
    } //-- java.util.Enumeration enumerateItem() 

    public xml.c1677.invoice.generated.Item getItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._itemList.size()) {
            throw new IndexOutOfBoundsException("getItem: Index value '" + index 
               + "' not in range [0.." + (this._itemList.size() - 1) + "]");
        }
        
        return (xml.c1677.invoice.generated.Item) _itemList.get(index);
    } //-- xml.c1677.invoice.generated.Item getItem(int) 

    public xml.c1677.invoice.generated.Item[] getItem()
    {
        int size = this._itemList.size();
        xml.c1677.invoice.generated.Item[] array = new xml.c1677.invoice.generated.Item[size];
        for (int index = 0; index < size; index++){
            array[index] = (xml.c1677.invoice.generated.Item) _itemList.get(index);
        }
        
        return array;
    } //-- xml.c1677.invoice.generated.Item[] getItem() 

    public int getItemCount()
    {
        return this._itemList.size();
    } //-- int getItemCount() 

    public java.util.Iterator iterateItem()
    {
        return this._itemList.iterator();
    } //-- java.util.Iterator iterateItem() 

    public void removeAllItem()
    {
        this._itemList.clear();
    } //-- void removeAllItem() 

    public boolean removeItem(xml.c1677.invoice.generated.Item vItem)
    {
        boolean removed = _itemList.remove(vItem);
        return removed;
    } //-- boolean removeItem(xml.c1677.invoice.generated.Item) 

    public xml.c1677.invoice.generated.Item removeItemAt(int index)
    {
        Object obj = this._itemList.remove(index);
        return (xml.c1677.invoice.generated.Item) obj;
    } //-- xml.c1677.invoice.generated.Item removeItemAt(int) 

    public void setItem(int index, xml.c1677.invoice.generated.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._itemList.size()) {
            throw new IndexOutOfBoundsException("setItem: Index value '" 
               + index + "' not in range [0.." + (this._itemList.size() - 1) + "]");
        }
        
        this._itemList.set(index, vItem);
    } //-- void setItem(int, xml.c1677.invoice.generated.Item) 

    public void setItem(xml.c1677.invoice.generated.Item[] vItemArray)
    {
        //-- copy array
        _itemList.clear();
        
        for (int i = 0; i < vItemArray.length; i++) {
                this._itemList.add(vItemArray[i]);
        }
    } //-- void setItem(xml.c1677.invoice.generated.Item) 

}
```

Non-trivial real world example
------------------------------

Two companies wish to trade with each other using a Supply Chain
messaging system. This system sends and receives Purchase Orders and
Order Receipt messages. After many months of discussion they have
finally decided upon the structure of the Version 1.0 of their message
XSD and both are presently developing solutions for it. One of the
companies decides to use Java and Castor XML support for (un)marshaling
and Castor's code generator to accelerate their development process.

### The Supply Chain XSD

``` {.xml}
            supplyChainV1.0.xsd
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           elementFormDefault="qualified"
           attributeFormDefault="unqualified">

    <xs:element name="Data">
      <xs:annotation>
        <xs:documentation>
          This section contains the supply chain message data
        </xs:documentation>
      </xs:annotation>
      <xs:complexType>
        <xs:choice>
          <xs:element name="PurchaseOrder">
            <xs:complexType>
              <xs:sequence>
                <xs:element name="LineItem" type="LineItemType" maxOccurs="unbounded"/>
              </xs:sequence>
              <xs:attribute name="OrderNumber" type="xs:string" use="required"/>
            </xs:complexType>
          </xs:element>
          <xs:element name="OrderReceipt">
            <xs:complexType>
              <xs:sequence>
                <xs:element name="LineItem" type="ReceiptLineItemType" maxOccurs="unbounded"/>
              </xs:sequence>
              <xs:attribute name="OrderNumber" type="xs:string" use="required"/>
            </xs:complexType>
          </xs:element>
        </xs:choice>
      </xs:complexType>
    </xs:element>

    <xs:complexType name="SkuType">
      <xs:annotation>
        <xs:documentation>Contains Product Identifier</xs:documentation>
      </xs:annotation>
      <xs:sequence>
        <xs:element name="Number" type="xs:integer"/>
        <xs:element name="ID" type="xs:string"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="ReceiptSkuType">
      <xs:annotation>
        <xs:documentation>Contains Product Identifier</xs:documentation>
      </xs:annotation>
      <xs:complexContent>
        <xs:extension base="SkuType">
          <xs:sequence>
            <xs:element name="InternalID" type="xs:string"/>
          </xs:sequence>
        </xs:extension>
      </xs:complexContent>
    </xs:complexType>

    <xs:complexType name="LineItemType">
      <xs:sequence>
        <xs:element name="Sku" type="SkuType"/>
        <xs:element name="Value" type="xs:double"/>
        <xs:element name="BillingInstructions" type="xs:string"/>
        <xs:element name="DeliveryDate" type="xs:date"/>
        <xs:element name="Number" type="xs:integer"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="ReceiptLineItemType">
      <xs:sequence>
        <xs:element name="Sku" type="ReceiptSkuType"/>
        <xs:element name="Value" type="xs:double"/>
        <xs:element name="PackingDescription" type="xs:string"/>
        <xs:element name="ShipDate" type="xs:dateTime"/>
        <xs:element name="Number" type="xs:integer"/>
      </xs:sequence>
    </xs:complexType>
</xs:schema>
```

### Binding file? -- IT IS REQUIRED!

If you run the Castor CodeGenerator on the above XSD you end up with the
following set of classes. (You also get lots of warning messages with
the present version.)

    Data.java
    DataDescriptor.java
    LineItem.java
    LineItemDescriptor.java
    LineItemType.java
    LineItemTypeDescriptor.java
    OrderReceipt.java
    OrderReceiptDescriptor.java
    PurchaseOrder.java
    PurchaseOrderDescriptor.java
    ReceiptLineItemType.java
    ReceiptLineItemTypeDescriptor.java
    ReceiptSkuType.java
    ReceiptSkuTypeDescriptor.java
    Sku.java
    SkuDescriptor.java
    SkuType.java
    SkuTypeDescriptor.java

The problem here is that there are two different elements with the same
name in different locations in the XSD. This causes a Java code
generation conflict. By default, Castor uses the element name as the
name of the class. So the second class generated for the LineItem
definition, which is different than the first, overwrites the first
class generated.

A binding file is therefore necessary to help the Castor code generator
differentiate between these generated classes and as such avoid such
generation conflicts. That is, you can 'bind' an element in the XML
schema to a differently named class file that you want to generate. This
keeps different elements separate and ensures that source is properly
generated for each XML Schema object.

> **Tip**
>
> The warning messages for Castor 0.99+ are very useful in assisting you
> in your creation of the binding file. For the example the warning
> messages for the example are:
>
>         Warning: A class name generation conflict has occurred between element
>                 '/Data/OrderReceipt/LineItem' and element '/Data/PurchaseOrder/LineItem'.
>                 Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
>         Warning: A class name generation conflict has occurred between element
>                 '/Data/OrderReceipt/LineItem' and element '/Data/PurchaseOrder/LineItem'.
>                 Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
>         Warning: A class name generation conflict has occurred between element
>                 '/Data/OrderReceipt/LineItem' and element '/Data/PurchaseOrder/LineItem'.
>                 Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
>         Warning: A class name generation conflict has occurred between element
>                 'complexType:ReceiptLineItemType/Sku' and element 'complexType:LineItemType/Sku'.
>                 Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
>         Warning: A class name generation conflict has occurred between element
>                 'complexType:ReceiptLineItemType/Sku' and element 'complexType:LineItemType/Sku'.
>                 Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
>         Warning: A class name generation conflict has occurred between element
>                 'complexType:ReceiptLineItemType/Sku' and element 'complexType:LineItemType/Sku'.
>                 Please use a Binding file to solve this problem.Continue anyway [not recommended] (y|n|?)y
>               

The following binding file definition will overcome the naming issues
for the generated classes:

``` {.xml}
            
<binding xmlns="http://www.castor.org/SourceGenerator/Binding"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.castor.org/SourceGenerator/Binding C:\\Castor\\xsd\\binding.xsd"
         defaultBinding="element">

   <elementBinding name="/Data/PurchaseOrder/LineItem">
      <java-class name="PurchaseOrderLineItem"/>
   </elementBinding>

   <elementBinding name="/Data/OrderReceipt/LineItem">
      <java-class name="OrderReceiptLineItem"/>
   </elementBinding>

   <elementBinding name="/complexType:ReceiptLineItemType/Sku">
      <java-class name="OrderReceiptSku"/>
   </elementBinding>

   <elementBinding name="/complexType:LineItemType/Sku">
      <java-class name="PurchaseOrderSku"/>
   </elementBinding>

</binding>
```

One thing to notice in the above `binding.xml` file is that the name
path used is relative to the root of the XSD **and not** the root of the
target XML. Also notice that the two complex types have the
"complexType:" prefix to identify them followed by the name path
relative to the root of the XSD.

The new list of generated classes is:

    Data.java
    DataDescriptor.java
    LineItem.java
    LineItemDescriptor.java
    LineItemType.java
    LineItemTypeDescriptor.java
    OrderReceipt.java
    OrderReceiptDescriptor.java
    OrderReceiptLineItem.java
    OrderReceiptLineItemDescriptor.java
    OrderReceiptSku.java
    OrderReceiptSkuDescriptor.java
    PurchaseOrder.java
    PurchaseOrderDescriptor.java
    PurchaseOrderLineItem.java
    PurchaseOrderLineItemDescriptor.java
    PurchaseOrderSku.java
    PurchaseOrderSkuDescriptor.java
    ReceiptLineItemType.java
    ReceiptLineItemTypeDescriptor.java
    ReceiptSkuType.java
    ReceiptSkuTypeDescriptor.java
    Sku.java
    SkuDescriptor.java
    SkuType.java
    SkuTypeDescriptor.java

The developers can now use these generated classes with Castor to
(un)marshal the supply chain messages sent by their business partner.

## Footnotes

[^1]: XML Schema is a [W3C](http://www.w3.org) Recommendation

[^2]: Castor supports the [XML Schema 1.0 Second
    Edition](http://www.w3.org/TR/XMLschema-1)

[^3]: For the various numerical types, the default behavior is to
    generate primitive types. However, if the use of wrappers is enabled
    by the following line in the `castorbuilder.properties` file:
    `org.exolab.castor.builder.primitivetowrapper=true` then the
    `java.lang.*` wrapper objects (as specified above) will be used
    instead.

[^4]: For the date/time and numeric types, the only supported value for
    whitespace is "collapse".

[^5]: For the integral types, the only allowed value for fractionDigits
    is 0.

[^6]: For the date/time and numeric types, the only supported value for
    whitespace is "collapse".

[^7]: For the various numerical types, the default behavior is to
    generate primitive types. However, if the use of wrappers is enabled
    by the following line in the `castorbuilder.properties` file:
    `org.exolab.castor.builder.primitivetowrapper=true` then the
    `java.lang.*` wrapper objects (as specified above) will be generated
    instead.

[^8]: Currently, `<xsd:language>` and `<xsd:token>` are treated as if
    they were `<xsd:string>`.
