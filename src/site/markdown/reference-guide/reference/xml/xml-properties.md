XML configuration file
======================

News
----

-   Added a section on how to access the properties as defined in the
    Castor properties file from within code.

-   **Release 1.2.1:** : Added new `
                            org.exolab.castor.xml.lenient.integer.validation
                        ` property to allow configuration of leniency
    for validation for Java properties generated from `<xs:integer>`
    types during code generation.

-   **Release 1.2:** : Access to the
    `org.exolab.castor.util.LocalConfiguration` class has been
    removed completely. To access the properties as used by Castor from
    code, please refer to the below section.

-   **Release 1.1.3:** Added special processing of proxied classes. The
    property org.exolab.castor.xml.proxyInterfaces allows you to specify
    a list of interfaces that such proxied objects implement. If your
    object implements one of these interfaces Castor will not use the
    class itself but its superclass at introspection or to find class
    mappings and ClassDescriptors.

-   **Release 0.9.7:** Added new org.exolab.castor.persist.useProxies
    property to allow configuration of JDBC proxy classes. If enabled,
    JDBC proxy classes will be used to wrap `java.sql.Connection` and
    `java.sql.PreparedStatement` instances, to allow for more detailed
    and complete JDBC statements to be output during logging. When
    turned off, no logging statements will be generated at all.

Introduction
------------

Castor uses a configuration file for environmental properties that are
shared across all the Castor sub systems. The configuration file is
specified as a Java properties file with the name `castor.properties`.

By definition, a default configuration file is included with the Castor
XML JAR. Custom properties can be supplied using one of the following
methods. Please note that the custom properties specified will
**override** the default configuration.

-   Place a file named `castor.properties` anywhere on the classpath of
    your application.

-   Place a file named `castor.properties` in the working directory of
    your application.

-   Use the system property `org.castor.user.properties.location` to
    specify the location of your custom properties.

Please note that Castor XML - upon startup - will try the methods given
above in exactly the sequence as stated above; if it managed to find a
custom property file using any of the given methods, it will cancel its
search.

When running the provided examples, Castor will use the configuration
file located in the examples directory which specifies additional
debugging information as well as pretty printing of all produced XML
documents.

The following properties are currently supported in the configuration
file:

  Name                                                    Description                                                                                                                                                                                                                                                                                            Values                                                                                                                Default                                                                                                                                                                                Since
  ------------------------------------------------------- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ --------------------------------------------------------------------------------------------------------------------- -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- -----------
  org.exolab.castor.xml.introspector.primitive.nodetype   Property specifying the type of XML node to use for primitive values, either `element` or `attribute`                                                                                                                                                                                                  `element` or `attribute`                                                                                              `attribute`                                                                                                                                                                            -
  org.exolab.castor.parser                                Property specifying the class name of the SAX XML parser to use.                                                                                                                                                                                                                                       -                                                                                                                     -                                                                                                                                                                                      -
  org.exolab.castor.parser.validation                     Specifies whether to perform XML document validation by default.                                                                                                                                                                                                                                       `true` and `false`                                                                                                    `false`                                                                                                                                                                                -
  org.exolab.castor.parser.namespaces                     Specifies whether to support XML namespaces by default.                                                                                                                                                                                                                                                `false` and `true`                                                                                                    `false`                                                                                                                                                                                -
  org.exolab.castor.xml.nspackages                        Specifies a list of XML namespace to Java package mappings.                                                                                                                                                                                                                                            -                                                                                                                     -                                                                                                                                                                                      -
  org.exolab.castor.xml.naming                            Property specifying the 'type' of the XML naming conventions to use. Values of this property must be either `mixed` , `lower` , or the name of a class which extends `org.exolab.castor.xml.XMLNaming` .                                                                                               `mixed` , `lower` , or the name of a class which extends `org.exolab.castor.xml.XMLNaming`                            `lower`                                                                                                                                                                                -
  org.castor.xml.java.naming                              Property specifying the 'type' of the Java naming conventions to use. Values of this property must be either `null` or the name of a class which extends `link org.castor.xml.JavaNaming`.                                                                                                             `null` or the name of a class which extends `link org.castor.xml.JavaNaming`.                                         `null`                                                                                                                                                                                 -
  org.exolab.castor.marshalling.validation                Specifies whether to use validation during marshalling.                                                                                                                                                                                                                                                `false` or `true`                                                                                                     `true`                                                                                                                                                                                 -
  org.exolab.castor.indent                                Specifies whether XML documents (as generated at marshalling) should use indentation or not.                                                                                                                                                                                                           `false` or `true`                                                                                                     `false`                                                                                                                                                                                -
  org.exolab.castor.sax.features                          Specifies additional features for the XML parser.                                                                                                                                                                                                                                                      A comma separated list of SAX (parser) features (that might or might not be supported by the specified SAX parser).   `http://apache.org/xml/features/disallow-doctype-decl`                                                                                                                                 -
  org.exolab.castor.sax.features-to-disable               Specifies features to be disbaled on the underlying SAX parser.                                                                                                                                                                                                                                        A comma separated list of SAX (parser) features to be disabled.                                                       `http://xml.org/sax/features/external-general-entities`, `http://xml.org/sax/features/external-parameter-entities`, `http://apache.org/xml/features/nonvalidating/load-external-dtd`   **1.0.4**
  org.exolab.castor.regexp                                Specifies the regular expression validator to use.                                                                                                                                                                                                                                                     A class that implements `org.exolab.castor.util.RegExpValidator`.                                                     -                                                                                                                                                                                      -
  org.exolab.castor.xml.strictelements                    Specifies whether to apply strictness to elements when unmarshalling. When enabled, the existence of elements in the XML document, which cannot be mapped to a class, causes a {@link SAXException} to be thrown. If set to false, these 'unknown' elements are ignored.                               `false` or `true`                                                                                                     `false`                                                                                                                                                                                -
  org.exolab.castor.xml.loadPackageMappings               Specifies whether the ClassDescriptorResolver should (automatically) search for and consult with package mapping files ( `.castor.xml` ) to retrieve class descriptor information                                                                                                                      `false` or `true`                                                                                                     `true`                                                                                                                                                                                 **1.0.2**
  org.exolab.castor.xml.serializer.factory                Specifying what XML serializers factory to use.                                                                                                                                                                                                                                                        A class name                                                                                                          org.exolab.castor.xml.XercesXMLSerializerFactory                                                                                                                                       **1.0**
  org.exolab.castor.xml.lenient.sequence.order            Specifies whether sequence order validation should be lenient.                                                                                                                                                                                                                                         `false` or `true`                                                                                                     `false`                                                                                                                                                                                **1.1**
  org.exolab.castor.xml.lenient.id.validation             Specifies whether id/href validation should be lenient.                                                                                                                                                                                                                                                `false` or `true`                                                                                                     `false`                                                                                                                                                                                **1.1**
  org.exolab.castor.xml.proxyInterfaces                   Specifies whether or not to search for an proxy interface at marshalling. If property is not empty the objects to be marshalled will be searched if they implement one of the given interface names. If the interface is implemented, the superclass will be marshalled instead of the class itself.   A list of proxy interfaces                                                                                            -                                                                                                                                                                                      **1.1.3**
  org.exolab.castor.xml.lenient.integer.validation        Specifies whether validation for Java properties generated from &lt;xs:integer&gt; should be lenient, i.e. allow for `int` s as well.                                                                                                                                                                  `false` or `true`                                                                                                     `false`                                                                                                                                                                                **1.2.1**
  org.exolab.castor.xml.version                           Specifies the XML document version number to be used during marshalling; defaults to 1.0.                                                                                                                                                                                                              `1.0` or `1.1`                                                                                                        `1.0`                                                                                                                                                                                  **1.3.2**

> **Note**
>
> As of Castor 1.3.3, the default values for
> org.exolab.castor.sax.features
> and
> org.exolab.castor.sax.features-to-disable
> have changed to include/disable selected features.

Accessing the properties from within code
-----------------------------------------

As of Castor 1.1, it is possible to read and set the value of properties
programmatically using the `getProperty(String)` and
`setProperty(String,String)` on the following classes:

-   `org.exolab.castor.xml.Unmarshaller`

-   `org.exolab.castor.xml.Marshaller`

-   `org.exolab.castor.xml.XMLContext`

Whilst using the setter methods on the first two classes will change the
settings of the respective instances only, using the `setProperty()`
method on the `org.exolab.castor.xml.XMLContext` class will change the
configuration globally, and affect all
`org.exolab.castor.xml.Unmarshaller` and
`org.exolab.castor.xml.Marshaller` instances created thereafter using
the org.exolab.castor.xml.XMLContext.createUnmarshaller() and
org.exolab.castor.xml.XMLContext.createMarshaller() methods.
