XML Mapping
===========

Introduction
------------

Castor XML mapping is a way to simplify the binding of java classes to
XML document. It allows to transform the data contained in a java object
model into/from an XML document.

Although it is possible to rely on Castor's default behavior to marshal
and unmarshal Java objects into an XML document, it might be necessary
to have more control over this behavior. For example, if a Java object
model already exists, Castor XML Mapping can be used as a bridge between
the XML document and that Java object model.

Castor allows one to specify some of its marshalling/unmarshalling
behavior using a mapping file. This file gives explicit information to
Castor on how a given XML document and a given set of Java objects
relate to each other.

A Castor mapping file is a good way to dissociate the changes in the
structure of a Java object model from the changes in the corresponding
XML document format.

Overview
--------

The mapping information is specified by an XML document. This document
is written from the point of view of the Java object and describes how
the properties of the object have to be translated into XML. One
constraint for the mapping file is that Castor should be able to infer
unambiguously from it how a given XML element/attribute has to be
translated into the object model during unmarshalling.

The mapping file describes for each object how each of its fields have
to be mapped into XML. A field is an abstraction for a property of an
object. It can correspond directly to a public class variable or
indirectly to a property via some accessor methods (setters and
getters).

It is possible to use the mapping and Castor default behavior in
conjunction: when Castor has to handle an object or an XML data but
can't find information about it in the mapping file, it will rely on its
default behavior. Castor will use the Java Reflection API to introspect
the Java objects to determine what to do.

**Note:** Castor can't handle all possible mappings. In some complex
cases, it may be necessary to rely on an XSL transformation in
conjunction with Castor to adapt the XML document to a more friendly
format.

### Marshalling Behavior

For Castor, a Java class has to map into an XML element. When Castor
marshals an object, it will:

-   use the mapping information, if any, to find the name of the element
    to create

or

-   by default, create a name using the name of the class

It will then use the fields information from the mapping file to
determine how a given property of the object has to be translated into
one and only one of the following:

-   an attribute

-   an element

-   text content

-   nothing, as we can choose to ignore a particular field

This process will be recursive: if Castor finds a property that has a
class type specified elsewhere in the mapping file, it will use this
information to marshal the object.

By default, if Castor finds no information for a given class in the
mapping file, it will introspect the class and apply a set of default
rules to guess the fields and marshal them. The default rules are as
follows:

-   All primitive types, including the primitive type wrappers (Boolean,
    Short, etc...) are marshalled as attributes.

-   All other objects are marshalled as elements with either text
    content or element content.

### Unmarshalling Behavior

When Castor finds an element while unmarshalling a document, it will try
to use the mapping information to determine which object to instantiate.
If no mapping information is present, Castor will use the name of the
element to try to guess the name of a class to instantiate (for example,
for an element named 'test-element', Castor will try to instantiate a
class named 'TestElement' if no information is given in the mapping
file). Castor will then use the field information of the mapping file to
handle the content of the element.

If the class is not described in the mapping file, Castor will
instrospect the class using the Java Reflection API to determine if
there is any function of the form getXxxYyy()/setXxxYyy(&lt;type&gt; x).
This accessor will be associated with XML element/attribute named
'xxx-yyy'. In the future, we will provide a way to override this default
behavior.

Castor will introspect object variables and use direct access \_only\_
if no get/set methods have been found in the class. In this case, Castor
will look for public variables of the form:

``` {.java}
public <type> xxxYYY;        
```

and expect an element/attribute named 'xxx-yyy'. The only handled
collections for &lt;type&gt; are java.lang.Vector and array. (up to
version 0.8.10)

For primitive &lt;type&gt;, Castor will look for an attribute first and
then an element. If &lt;type&gt; is not a primitive type, Castor will
look for an element first and then an attribute.

The Mapping File
----------------

The following sections define the syntax for each of the mapping file
artefacts and their semantical meaning.

### Sample domain objects

This section defines a small domain model that will be referenced by
various mapping file (fragments/samples) in the following sections. The
model consists of two two classes `Order` and `OrderItem`, where an
order holds a list of order items.

``` {.java}
public class Order {

    private List orderItems;
    private String orderNumber;
    
    public List getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List orderItems) {
        this.orderItems = orderItems;
    }
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}

public class OrderItem {
    
    private String id;
    private Integer orderQuantity;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getOrderQuantity() {
        return orderQuantity;
    }
    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
```

As shown above in bold, the `Order` instance has a (private) field
`'orderItems'` to hold a collection of `OrderItem` instances. This field
is publically exposed by corresponding getter and setter methods.

### The &lt;mapping&gt; element

``` {.java}
<!ELEMENT mapping ( description?, include*, field-handler*, class*, key-generator* )>        
```

The &lt;mapping&gt; element is the root element of a mapping file. It
contains:

-   an optional description

-   zero or more &lt;include&gt; which facilitates reusing mapping files

-   zero of more &lt;field-handler&gt; defining custom, configurable
    field handlers

-   zero or more &lt;class&gt; descriptions: one for each class we
    intend to give mapping information

-   zero or more &lt;key-generator&gt;: not used for XML mapping

A mapping file look like this:

``` {.java}
<?xml version="1.0"?>

<!DOCTYPE mapping PUBLIC "-//EXOLAB/Castor Mapping DTD Version 1.0//EN"
      castor.org
                         "http://castor.org/mapping.dtd">

<mapping>
    <description>Description of the mapping</description>
    
    <include href="other_mapping_file.xml"/>
    
    <!-- mapping for class 'A' -->
    <class name="A">
            .........
    </class>
    
    <!-- mapping for class 'B' -->
    <class name="B">
            .........
    </class>

</mapping>
```

### The &lt;class&gt; element

    <!ELEMENT class ( description?, cache-type?, map-to?, field+ )>
    <!ATTLIST class
              name            ID       #REQUIRED
              extends         IDREF    #IMPLIED
              depends         IDREF    #IMPLIED
              auto-complete   ( true |false ) "false"
              identity        CDATA    #IMPLIED
              access          ( read-only | shared | exclusive | db-locked )  "shared"
              key-generator   IDREF    #IMPLIED >

The `<class>` element contains all the information used to map a Java
class into an XML document. The content of `<class>` is mainly used to
describe the fields that will be mapped.

|  Name                | Description
| ----- | -----
| **name**            | The fuly-qualified name of the Java class that we want to map to.
|   **extends**         | The fully qualified name of a parent class. This attribute should be used **only** if this class extends another class for which a class mapping is provided. It should **not** be used if there's no class maping for the extended class.
|   **depends**         | Used with Castor JDO only; for more information on this field, please see the [JDO documentation](http://www.castor.org/jdo-mapping.html).
|   **auto-complete**   | If true, the class will be introspected to determine its field and the fields specified in the mapping file will be used to overide the fields found during the introspection.
|   **identity**        | Used with Castor JDO only; for more information on this field, please see see the [JDO documentation](http://www.castor.org/jdo-mapping.html).
|   **access**          | Used with Castor JDO only; for more information on this field, please see see the [JDO documentation](http://www.castor.org/jdo-mapping.html).
|   **key-generator**   | Used with Castor JDO only; for more information on this field, please see see the [JDO documentation](http://www.castor.org/jdo-mapping.html).
[Description of the attributes]

The auto-complete attributes is interesting as it allow a fine degree of
control of the introspector: it is possible to specifiy only the fields
whose Castor default behavior does not suite our needs. These feature
should simplify the handling of complexe class containing many fields.
Please see below for an example usage of this attribute.

|   Name              | Description
| - | -
|   **description**   | An optional description.
|   **cache-type**    | Used with Castor JDO only; for more information on this field, please see see the [JDO documentation](http://www.castor.org/jdo-mapping.html).
|   **map-to**        | Used if the name of the element is not the name of the class. By default, Castor will infer the name of the element to be mapped from the name of the class: a Java class named 'XxxYyy' will be transformed in 'xxx-yyy'. If you don't want Castor to generate the name, you need to use &lt;map-to&gt; to specify the name you want to use. &lt;map-to&gt; is only used for the root element.
|   **field**         | Zero or more &lt;field&gt; elements, which are used to describe the properties of the Java class being mapped.
[Description of the content]

#### Sample &lt;class&gt; mappings

The following mapping fragment defines a class mapping for the
`OrderItem` class:

``` {.java}
<class name="mypackage.OrderItem>
           
   <map-to xml="item"/>

   <field name="id" type="string">
      <bind-xml name="identity" node="attribute"/>
   </field>

   </field name="orderQuantity" type="integer">
      <bind-xml name="quantity" node="element"/>
   </field>

</class>
```

When marshalling an `OrderItem` instance, this yields the following XML:

``` {.java}
<?xml version="1.0" ?>        
<item identity="12">
   <quantity>100</quantity>
</item>
```

The following mapping fragment defines a class mapping for the same
class, where for all properties but `id` introspection should be used;
the use of the `auto-complete` attribute instructs Castor XML to use
introspection for all attributes other than `'id'`, where the given
field mapping will be used.

``` {.java}
<class name="mypackage.OrderItem auto-complete="true">
           
   <map-to xml="item"/>

   <field name="id" type="string">
      <bind-xml name="identity" node="attribute"/>
   </field>

</class>
```

When marshalling the very same `OrderItem` instance, this yields the
following XML:

``` {.java}
<?xml version="1.0" ?>        
<item identity="12">
   <order-quantity>100</order-quantity>
</item>
```

By removing the &lt;map-to&gt; element from above class mapping, ...

``` {.java}
<class name="mypackage.OrderItem auto-complete="true">
           
   <field name="id" type="string">
      <bind-xml name="identity" node="attribute"/>
   </field>

</class>
```

... Castor will use introspection to infer the element name from the
Java class name (`OrderItem`), applying a default naming convention
scheme.

When marshalling the very same `OrderItem` instance, this yields the
following XML:

``` {.java}
<?xml version="1.0" ?>        
<order-item identity="12">
   <order-quantity>100</order-quantity>
</order-item>
```

### The &lt;map-to&gt; element

    <!ELEMENT map-to EMPTY>
    <!ATTLIST map-to
              table               NMTOKEN  #IMPLIED
              xml                 NMTOKEN  #IMPLIED
              ns-uri              NMTOKEN  #IMPLIED
              ns-prefix           NMTOKEN  #IMPLIED
              ldap-dn             NMTOKEN  #IMPLIED
              element-definition  (true|false) "false"     NEW as of 1.0M3
              ldap-oc             NMTOKEN  #IMPLIED>

`<map-to>` is used to specify the name of the element that should be
associated with the given class. `<map-to>` is only used for the root
class. If this information is not present, Castor will:

-   for marshalling, infer the name of the element to be mapped from the
    name of the class: a Java class named 'XxxYyy' will be transformed
    into 'xxx-yyy'.

-   for unmarshalling, infer the name of the class from the name of the
    element: for an element named 'test-element' Castor will try to use
    a class named 'TestElement'

Please note that it is possible to change the naming scheme used by
Castor to translate between the XML name and the Java class name in the
`castor.properties` file.

| Attribute | Description
| - | -
|   **xml**                  | Name of the element that the class is associated to.
|   **ns-uri**               | Namespace URI
|   **ns-prefix**            | Desired namespace
|   **element-definition**   | `True` if the descriptor as created from a schema definition that was of type element (as opposed to a &lt;complexType&gt; definition). This only is useful in the context of source code generation.
|   **ldap-dn**              | Not used for Castor XML
|   **ldap-oc**              | Not used for Castor XML
[Description of attributes]

#### &lt;map-to&gt; samples

The following mapping fragment defines a &lt;map-to element for the
`OrderItem` class, manually setting the element name to a value of
`'item'`.

``` {.xml}
<class name="myPackage.OrderItem">
   ...
   <map-to xml="item" />
   ...
</class>
```

The following mapping fragment instructs Castor to assign a namespace
URI of `http://castor.org/sample/mapping/` to the &lt;item&gt; element,
and use a namespace prefix of `'castor'` during un-/marshalling.

``` {.xml}
<class name="myPackage.OrderItem">
   ...
   <map-to xml="item" ns-uri="http://castor.org/sample/mapping/"
           ns-prefix="castor"/>
   ...
</class>
```

When marshalling an `OrderItem` instance, this will yield the following
XML:

``` {.java}
<?xml version="1.0" ?>        
<castor:order-item xmlns:castor="http://castor.org/sample/mapping/" identity="12">
   <castor:order-quantity>100</castor:order-quantity>
</castor:order-item>
```

### The &lt;field&gt; element

    <!ELEMENT field ( description?, sql?, bind-xml?, ldap? )>
    <!ATTLIST field
        name           NMTOKEN  #REQUIRED
        type           NMTOKEN  #IMPLIED
        handler        NMTOKEN  #IMPLIED
        required       ( true | false )  "false"
        direct         ( true | false )  "false"
        lazy           ( true | false )  "false"
        transient      ( true | false )  "false"
        nillable       ( true | false )  "false"
        container      ( true | false )  "false"
        get-method     NMTOKEN  #IMPLIED
        set-method     NMTOKEN  #IMPLIED
        create-method  NMTOKEN  #IMPLIED
        collection     ( array | vector | hashtable | collection | set | map )  #IMPLIED>
            

`<field>` is used to describe a property of a Java object we want to
marshal/unmarshal. It gives:

-   its identity ('name')

-   its type (infered from 'type' and 'collection')

-   its access method (infered from 'direct',
    'get-method', 'set-method')

From this information, Castor is able to access a given property in the
Java class.

In order to determine the signature that Castor expects, there are two
easy rules to apply.

**1. Determine &lt;type&gt;.**

-   **If there is no 'collection' attribute**, the &lt;type&gt; is just
    the Java type specified in &lt;type\_attribute&gt; (the value of the
    'type' attribute in the XML document). The value of
    &lt;type\_attribute&gt; can be a fully qualified Java object like
    'java.lang.String' or one of the allowed short name:

|       short name    | Primitive type?   | Java Class
|       ------------- | ----------------- | --------------------------
|       other         | N                 | java.lang.Object
|       string        | N                 | java.lang.String
|       integer       | Y                 | java.lang.Integer.TYPE
|       long          | Y                 | java.lang.Long.TYPE
|       boolean       | Y                 | java.lang.Boolean.TYPE
|       double        | Y                 | java.lang.Double.TYPE
|       float         | Y                 | java.lang.Float.TYPE
|       big-decimal  |  N                 | java.math.BigDecimal
|       byte          | Y                 | java.lang.Byte.TYPE
|       date          | N                 | java.util.Date
|       short         | Y                 | java.lang.Short.TYPE
|       char          | Y                 | java.lang.Character.TYPE
|       bytes         | N                 | byte\[\]
|       chars         | N                 | char\[\]
|       strings       | N                 | String\[\]
|       locale        | N                 | java.util.Locale
[Type shortnames]

Castor will try to cast the data in the XML file in the proper
Java type.

-   **If there is a collection attribute** , you can use the following
    table:

|       name         | &lt;type&gt;                  | default implementation
|       ------------ | ----------------------------- | -----------------------------
|       array        | &lt;type\_attribute&gt;\[\]   | &lt;type\_attribute&gt;\[\]
|       arraylist    | java.util.List                | java.util.Arraylist
|       vector       | java.util.Vector              | java.util.Vector
|       hashtable    | java.util.Hashtable           | java.util.Hashtable
|       collection   | java.util.Collection          | java.util.Arraylist
|       set          | java.util.Set                 | java.util.Hashset
|       map          | java.util.Map                 | java.util.Hashmap
|       sortedset    | java.util.SortedSet           | java.util.TreeSet
[Type implementations]

The type of the object inside the collection
is &lt;type\_attribute&gt;. The 'default implementation' is the type
used if the object holding the collection is found to be null and
need to be instantiated.

For hashtable and maps (since 0.9.5.3), Castor will save both key
and values. When marshalling output &lt;key&gt; and
&lt;value&gt; elements. These names can be controlled by using a
top-level or nested class mapping for the
org.exolab.castor.mapping.MapItem class.

Note: for backward compatibility with prior versions of Castor, the
*saveMapKeys* property can be set to false in the
castor.properties file.

For versions prior to 0.9.5.3, hashtable and maps, Castor will save
only the value during marshalling and during unmarshalling will add
a map entry using the object as both the key and value, e.g.
map.put(object, object).

It is necessary to use a collection when the content model of the
element expects more than one element of the specified type.

**Determine the signature of the function**

-   **If 'direct' is set to true**, Castor expects to find a class
    variable with the given signature:

``` {.java}
    public <type> <name>;
```

-   **If 'direct' is set to false or omitted**, Castor will access the
    property though accessor methods. Castor determines the signature of
    the accessors as follow: If the 'get-method' or 'set-method'
    attributes are supplied, it will try to find a function with the
    following signature:

``` {.java}
    public <type> <get-method>();
```

or

``` {.java}
    public void <set-method>(<type> value);
```

If 'get-method' and 'set-method' attributes are not provided, Castor
will try to find the following function:

``` {.java}
    public <type> get<capitalized-name>();
```

or

``` {.java}
    public void set<capitalized-name>(<type> value);
```

&lt;capitalized-name&gt; means that Castor takes the &lt;name&gt;
attribute and put its first letter in uppercase without modifying
the other letters.

The content of &lt;field&gt; will contain the information on how to
map this given field to SQL, XML, ...

-   **Exceptions concerning collection fields:**

The default is to treat the 'get-method' as a simple getter
returning the collection field, and the 'set-method' as a simple
getter used to set a new instance on the collection field.

      
##### 'get-method':

If a 'get-method' is provided for a collection field, Castor - in adition to the default behaviour described above - will deviate from the standard case for the following special prefixes:
                     
``` {.java}
public Iterator iterate...();
```
                     
A 'get-method' starting with the prefix ' `iterate` ' is treated as Iterator method for the given collection field.
                     
``` {.java}
public Enumeration enum...();
```
                     
A 'get-method' starting with ' `enum` ' is treated as Enumeration method for the given collection field.

##### 'set-method'

If 'set-method' is provided for a collection field, Castor - in addition to the default behaviour described above - will accept an 'add' prefix and expect the following signature:
                     
``` {.java}
public void add...(<type> value);
```
                     
This method is called for each collection element while unmarshalling.      

| Name | Description
| - | -
| **name**            | The field 'name' is required even if no such field exists in the class. If 'direct' access is used, 'name' should be the name of a public instance member in the object to be mapped (the field must be public, not static and not transient). If no direct access and no 'get-/set-method' is specified, this name will be used to infer the name of the accessors methods.
| **type**            | The Java type of the field. It is used to access the field. Castor will use this information to cast the XML information (like string into integer). It is also used to define the signature of the accessor methods. If a collection is specified, this is used to specify the type of the objects held by the collection. See description above for more details.
|   **required**        | A field can be optional or required.
|   **nillable**        | A field can be of content '`nil`'.
|   **transient**       | If true, this field will be ignored during the marshalling. This is usefull when used together with the auto-complete="true" option.
|   **direct**          | If true, Castor will expect a public variable in the containing class and will access it directly (for both reading and writing).
|   **container**       | Indicates whether the field should be treated as a container, i.e. only it's fields should be persisted, but not the containing class itself. In this case, the container attribute should be set to true (supported in Castor XML only).
|   **collection**      | If a parent expects more than one occurrence of one of its element, it is necessary to specify which collection Castor will use to handle them. The type specified is used to define the type of the content inside the collection.
|   **get-method**      | Optional name of the 'get method' Castor should use. If this attribute is not set and the set-method attribute is not set, then Castor will try to infer the name of this method with the algorithm described above.
|   **set-method**     |  Optional name of the 'set method' Castor should use. If this attribute is not set and the get-method attribute is not set, then Castor will try to infer the name of this method with the algorithm described above.
|   **create-method**   | Optionally defines a factory method for the instantiation of a FieldHandler
|   **handler**         | If present, specifies one of the following:
                      -   The fully-qualified class name of a custom field handler implementation, or
                      -   The (short) name of a [configurable field handler](http://www.castor.org/xml-fieldhandlers.html#Use-ConfigurableFieldHandler-for-more-flexibility) definition.
[Description of the attributes]

### Description of the content

In the case of XML mapping, the content of a field element should be one
and only one **&lt;bind-xml&gt;** element describing how this given
field will be mapped into the XML document.

#### Mapping constructor arguments (since 0.9.5)

Starting with release 0.9.5, for *attribute* mapped fields, support has
been added to map a constructor field using the `set-method` attribute.

To specify that a field (mapped to an attribute) should be used as a
constructor argument during object initialization, please specify a
`set-method` attribute on the `<field>` mapping and use "%X" as the
value of the `set-method` attribute, where `X` is a positive integer
number, e.g. `%1` or `%21`.

For example:

``` {.java}
<field name="foo" set-method="%1" get-method="getFoo" type="string">
   <bind-xml node="attribute"/>
</field>
```

Note that because the `set-method` is specified, the `get-method` also
must be specified.

**Tip**: the XML HOW-TO section has a HOW-TO document for mapping
constructor arguments, incl. a fully working mapping.

#### Sample 1: Defining a custom field handler

The following mapping fragment defines a `<field>` element for the
`member` property of the `org.some.package.Root` class, specifying a
custom `org.exolab.castor.mapping.FieldHandler` implementation.

``` {.java}
<class name="org.some.package.Root">
   <field name="member" type="string" handler="org.some.package.CustomFieldHandlerImpl"/>
</class>
```

#### Sample 2: Defining a custom configurable field handler

The same custom field handler as in the previous sample can be defined
with a separate configurable &lt;field-handler&gt; definition, where
additional configuration can be provided.

``` {.java}
<field-handler name="myHandler" class="org.some.package.CustomFieldHandlerImpl">
   <param name="date-format" value="yyyyMMddHHmmss"/>
</field-handler>
```

and subsequently be referred to by its **name** as shown in the
following field mapping:

``` {.java}
<class name="org.some.package.Root">
   <field name="member" type="string" handler="myHandler"/>
</class>
```

#### Sample 3: Using the container attribute

Assume you have a class mapping for a class `Order` which defines -
amongst others - a field mapping as follows, where the field `item`
refers to an instance of a class `Item`.

``` {.java}
<class name="some.example.Order">
            
   ...
   <field name="item" type="some.example.Item" >
      <bind-xml> name="item" node="element" />
   </field>
   ...
</class>

<class name="some.example.Item">
   <field name="id" type="long" />
   <field name="description" type="string" />
</class>
```

Marshalling an instance of `Order` would produce XML as follows:

``` {.xml}
<order>
    ...
    <item>
        <id>100</id>
        <description>...</description>
    </item>
</order>
```

If you do not want the `Item` instance to be marshalled, but only its
fields, change the field mapping for the `item` member to be as follows:

``` {.xml}
<field name="item" type="some.example.Item" container="false" >
   <bind-xml> name="item" node="element" />
</field>
```

The resulting XML would look as follows:

``` {.xml}
<order>
    ...
    <id>100</id>
    <description>...</description>
</order>
```

### The &lt;bind-xml&gt; element

#### Grammar

    <!ELEMENT bind-xml (class?, property*)>
    <!ATTLIST bind-xml
              name     NMTOKEN     #IMPLIED
              type     NMTOKEN     #IMPLIED
              location CDATA       #IMPLIED
              matches  NMTOKENS    #IMPLIED
              QName-prefix NMTOKEN #IMPLIED
              reference   ( true | false ) "false"
              node        ( attribute | element | text )    #IMPLIED
              auto-naming ( deriveByClass | deriveByField ) #IMPLIED
              transient   ( true | false ) "false">

##### Definiton 

The `<bind-xml>` element is used to describe how a given Java field
should appear in an XML document. It is used both for marshalling and
unmarshalling.

| Field | Description
| - | - 
| **name**                       | The name of the element or attribute. The name is a QName, and a namespace prefix may be used to indicate the element or attribute belongs to a certain namespace. Note the prefix is not preserved or used during marshaling, it's simply used for qualification of which namespace the element or attribute belongs.
|   **auto-naming**                | If no name is specified, this attribute controls how castor will automatically create a name for the field. Normally, the name is created using the field name, however many times it is necessary to create the name by using the class type instead (such as heterogeneous collections).
|   **type**                       | XML Schema type (of the value of this field) that requires specific handling in the Castor Marshaling Framework (such as 'QName' for instance).
|   **location** (since 0.9.4.4)   | Allows the user to specify the "sub-path" for which the value should be marshaled to and from. This is useful for "wrapping" values in elements or for mapping values that appear on sub-elements to the current "element" represented by the class mapping. For more information, see the location attribute.
|   **QName-prefix**               | When the field represents a QName value, a prefix can be provided that is used when marshaling value of type QName. More information on the use of 'QName-prefix' can be found in the [SourceGenerator Documentation](http://www.castor.org/SourceGeneratorUser.pdf)
|   **reference**                  | Indicates if this field has to be treated as a reference by the unmarshaler. In order to work properly, you must specify the node type to 'attribute' for both the 'id' and the 'reference'. In newer versions of Castor, 'element' node for reference is allowed. Remember to make sure that an *identity* field is specified on the `<class>` mapping for the object type being referenced so that Castor knows what the object's identity is.
|   **matches**                    | Allows overriding the matches rules for the name of the element. It is a standard regular expression and will be used instead of the 'name' field. A '\*' will match any XML name, however it will only be matched if no other field exists that matches the xml name.
|   **node**                       | Indicates if the name corresponds to an attribute, an element, or text content. By default, primitive types are assumed to be an attribute, otherwise the node is assumed to be an element
|   **transient**                  | Allows for making this field transient for XML. The default value is inherited from the &lt;field&gt; element.
[Description of the attributes]

#### Nested class mapping

Since 0.9.5.3, the bind-xml element supports a nested class mapping,
which is often useful when needing to specify more than one mapping for
a particular class. A good example of this is when mapping
Hashtable/HashMap/Map.

``` {.java}
<bind-xml ...>
   <class name="org.exolab.castor.mapping.MapItem">
      <field name="key" type="java.lang.String">
        <bind-xml name="id"/>
      </field>
      <field name="value" type="com.acme.Foo"/>
   </class>
</bind-xml>
```

Usage Pattern
-------------

Here is an example of how Castor Mapping can be used. We want to map an
XML document like the following one (called 'order.xml'). model.

``` {.java}
<Order reference="12343-AHSHE-314159">
  <Client>
    <Name>Jean Smith</Name>
    <Address>2000, Alameda de las Pulgas, San Mateo, CA 94403</Address>
  </Client>

  <Item reference="RF-0001">
    <Description>Stuffed Penguin</Description>
    <Quantity>10</Quantity>
    <UnitPrice>8.95</UnitPrice>
  </Item>

  <Item reference="RF-0034">
    <Description>Chocolate</Description>
    <Quantity5</Quantity>
    <UnitPrice>28.50</UnitPrice>
  </Item>

  <Item reference="RF-3341">
     <Description>Cookie</Description>
     <Quantity>30</Quantity>
     <UnitPrice>0.85</UnitPrice>
  </Item>
</Order>
```

Into the following object model composed of 3 classes:

-   **MyOrder:** represent an order

-   **Client:** used to store information on the client

-   **Item:** used to store item in an order

The sources of these classes follow.

``` {.java}
import java.util.Vector;
import java.util.Enumeration;

public class MyOrder {

    private String _ref;
    private ClientData _client;
    private Vector _items;
    private float _total;

    public void setReference(String ref) {
        _ref = ref;
    }

    public String getReference() {
        return _ref;
    }

    public void setClientData(ClientData client) {
        _client = client;
    }

    public ClientData getClientData() {
        return _client;
    }

    public void setItemsList(Vector items) {
        _items = items;
    }

    public Vector getItemsList() {
        return _items;
    }


    public void setTotal(float total) {
        _total = total;
    }

    public float getTotal() {
        return _total;
    }

    // Do some processing on the data
    public float getTotalPrice() {
        float total = 0.0f;

        for (Enumeration e = _items.elements() ; e.hasMoreElements() ;) {
            Item item = (Item) e.nextElement();
            total += item._quantity * item._unitPrice;
        }

        return total;
    }
}
```

``` {.java}
public class ClientData {

    private String _name;
    private String _address;

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public String getAddress() {
        return _address;
    }
}
```

``` {.java}
public class Item {
    public String _reference;
    public int    _quantity;
    public float  _unitPrice;
    public String _description;
}
```

The XML document and the java object model can be connected by using the
following mapping file:

``` {.java}
<?xml version="1.0"?>
<!DOCTYPE mapping PUBLIC "-//EXOLAB/Castor Mapping DTD Version 1.0//EN"
                         "http://castor.org/mapping.dtd">

<mapping>
  <class name="MyOrder">
    <map-to xml="Order"/>

    <field name="Reference"
           type="java.lang.String">
      <bind-xml name="reference" node="attribute"/>
    </field>

    <field name="Total"
           type="float">
      <bind-xml name="total-price" node="attribute"/>
    </field>

    <field name="ClientData"
           type="ClientData">
      <bind-xml name="Client"/>
    </field>

    <field name="ItemsList"
           type="Item"
              collection="vector">
      <bind-xml name="Item"/>
    </field>
  </class>

  <class name="ClientData">
    <field name="Name"
           type="java.lang.String">
      <bind-xml name="Name" node="element"/>
    </field>

    <field name="Address"
           type="java.lang.String">
      <bind-xml name="Address" node="element"/>
    </field>
  </class>

  <class name="Item">
    <field name="_reference"
           type="java.lang.String"
           direct="true">
      <bind-xml name="reference" node="attribute"/>
    </field>

    <field name="_quantity"
           type="integer"
           direct="true">
      <bind-xml name="Quantity" node="element"/>
    </field>

    <field name="_unitPrice"
           type="float"
           direct="true">
      <bind-xml name="UnitPrice" node="element"/>
    </field>

    <field name="_description"
           type="string"
           direct="true">
      <bind-xml name="Description" node="element"/>
    </field>
  </class>

</mapping>
```

The following class is an example of how to use Castor XML Mapping to
manipulate the file 'order.xml'. It unmarshals the document 'order.xml',
computes the total price, sets the total price in the java object and
marshals the object model back into XML with the calculated price.

``` {.java}
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;

import java.io.IOException;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import org.xml.sax.InputSource;

public class main {

    public static void main(String args[]) {

        Mapping      mapping = new Mapping();

        try {
            // 1. Load the mapping information from the file
            mapping.loadMapping( "mapping.xml" );

            // 2. Unmarshal the data
            Unmarshaller unmar = new Unmarshaller(mapping);
            MyOrder order = (MyOrder)unmar.unmarshal(new InputSource(new FileReader("order.xml")));

            // 3. Do some processing on the data
            float total = order.getTotalPrice();
            System.out.println("Order total price = " + total);
            order.setTotal(total);

            // 4. marshal the data with the total price back and print the XML in the console
            Marshaller marshaller = new Marshaller(new OutputStreamWriter(System.out));
            marshaller.setMapping(mapping);
            marshaller.marshal(order);

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }
}       
```

xsi:type
--------

Ordinarily, a mapping will only reference types that are concrete
classes (i.e. not interfaces nor abstract classes). The reason is that
to unmarshal a type requires instantiating it and one cannot instantiate
an interface. However, in many real situations, object models depend on
the use of interfaces. Many class properties are defined to have
interface types to support the ability to swap implementations. This is
often the case in frameworks.

The problem is that a different mapping must be used each time the same
model is to be used to marshal/unmarshal an implementation that uses
different concrete types. This is not convenient. The mapping should
represent the model and the specific concrete type used to unmarshal a
document is a configuration parameter; it should be specified in the
instance document to be unmarshalled, not the mapping.

For example, assume a very simple object model of an engine that has one
property that is a processor:

``` {.java}
public interface IProcessor {
    public void process();
}

public class Engine {
    private IProcessor processor;
    public IProcessor getProcessor() {
        return processor;
    }
    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }
}         
```

A typical mapping file for such a design may be:

``` {.java}
<mapping>
    <class name="Engine">
        <map-to xml="engine" />

        <field name="processor" type="IProcessor" required="true">
           <bind-xml name="processor" node="element" />
        </field>

     </class>
  </mapping>
```

It is possible to use such a mapping and still have the
marshal/unmarshal process work by specifying the concrete implementation
of IProcessor in the document to be unmarshalled, using the xsi:type
attribute, as follows:

``` {.java}
  <engine>
     <processor xsi:type="java:com.abc.MyProcessor" />
  </engine>            
```

In this manner, one is still able to maintain only a single mapping, but
vary the manner in which the document is unmarshalled from one instance
document to the next. This flexibility is powerful because it enables
the support of polymorphism within the castor xml marshalling framework.

Suppose we wanted the following XML instead:

``` {.java}
  <engine>
     <myProcessor/>
  </engine>
```

In the above output our XML name changed to match the type of the class
used instead of relying on the xsi:type attribute. This can be achieved
by modifying the mapping file as such:

``` {.java}
  <mapping>
     <class name="Engine">
        <map-to xml="engine" />
        <field name="processor" type="IProcessor" required="true">
           <bind-xml auto-naming="deriveByClass" node="element" />
        </field>
     </class>

     <class name="MyProcessor">
        <map-to xml="myProcessor" />
     </class>

  </mapping>
```

Location attribute
------------------

Since version 0.9.5

The location attribute allows the user to map fields from nested
elements or specify a wrapper element for a given field. Wrapper
elements are simply elements which appear in the XML instance, but do
not have a direct mapping to an object or field within the object model.

For example to map an instance of the following class:

``` {.java}
public class Foo {

    private Bar bar = null;

    public Foo();

    public getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }
}
```

into the following XML instance:

``` {.xml}
<?xml version="1.0"?>
<foo>;
   <abc>
      <bar>...</bar>
   </abc>
</foo>
```

*(notice that an 'abc' field doesn't exist in the Bar class)* One would
use the following mapping:

``` {.xml}
<?xml version="1.0"?>
   ...
   <class name="Foo">
      <field name="bar" type="Bar">
         <bind-xml name="bar" location="abc"/>
      </field>
   </class>
   ...
</mapping>
```

Note the "location" attribute. The value of this attribute is the name
of the wrapper element. To use more than one wrapper element, the name
is separated by a forward-slash as such:

``` {.xml}
<bind-xml name="bar" location="abc/xyz" />
```

Note that the name of the element is not part of the location itself and
that the location is always relative to the class in which the field is
being defined. This works for attributes also:

``` {.xml}
<bind-xml name="bar" location="abc" node="attribute" />
```

will produce the following:

``` {.xml}
<?xml version="1.0"?>
<foo>
   <abc bar="..."/>;
</foo>
```

Tips
----

Some helpful hints...

### Automatically create a mapping file

Castor comes with a tool that can automatically create a mapping from
class files. Please see the [XML FAQ](xml-faq.html) for more information.

### Create your own FieldHandler

Sometimes to handle complex situations you'll need to create your own
FieldHandler. Normally a FieldHandler deals with a specific class and
field, however generic, reusable FieldHandlers can also be created by
extending org.exolab.castor.mapping.GeneralizedFieldHandler or
org.exolab.castor.mapping.AbstractFieldHandler. The FieldHandler can be
specified on the &lt;field&gt; element.

For more information on writing a custom FieldHandler please see the
following: XML FieldHandlers.

### Mapping constructor arguments (since 0.9.5)

You may map any attributes to constructor arguments. For more
information on how to map constructor arguments see the information
available in the section on set-method above.

Please note that mapping **elements** to constructor arguments is not
yet supported.

**Tip**: the [XML HOW-TO section](xml-howto.html) has a HOW-TO document for
mapping constructor arguments.

### Preventing Castor from checking for a default constructor (since 0.9.5)

Sometimes it's useful to prevent Castor from checking for a default
constructor, such as when trying to write a mapping for an interface or
type-safe enum. You can use the "undocumented"
verify-constructable="false" attribute on the &lt;class&gt; element to
prevent Castor from looking for the default constructor.

### Type safe enumeration mapping (since 0.9.5)

While you can always use your own custom FieldHandler for handling
type-safe enumeration classes, Castor does have a built-in approach to
dealing with these types of classes. If the type-safe enum class has a
**public static &lt;type&gt; valueOf(String)** method Castor will call
that method so that the proper instance of the enumeration is returned.
Note: You'll also need to disable the default constructor check in the
mapping file (see section 7.4 above to see more on this).
