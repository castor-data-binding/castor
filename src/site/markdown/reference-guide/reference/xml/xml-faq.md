XML FAQ
=======

This section provides answers to frequently answered questions, i.e.
questions that have been asked repeatedly on one of the mailing lists.
Please check with these F.A.Q.s frequently, as addressing questions that
have been answered in the past already again and again places an
unnecessary burden on the committers/contributors.

General
-------

### How do I set the encoding?

Create a new instance of the `Marshaller` class and use the
`setEncoding` method. You'll also need to make sure the encoding for the
Writer is set properly as well:

``` {.java}
 ...
 String encoding = "ISO-8859-1";
 FileOutputStream fos = new FileOutputStream("result.xml");
 OutputStreamWriter osw = new OuputStreamWriter(fos, encoding);
 Marshaller marshaller = new Marshaller(osw);
 marshaller.setEncoding(encoding);
 ...
            
```

### I'm getting an error about 'xml' prefix already declared?

> **Note**
>
> For Castor 0.9.5.2 only

The issue occurs with newer versions of Xerces than the version 1.4 that
ships with Castor. The older version works OK. For some reason, when the
newer version of Xerces encounters an "xml" prefixed attribute, such as
"xml:lang", it tries to automatically start a prefix mapping for "xml".
Which, in my opinion, is technically incorrect. They shouldn't be doing
that. According to the w3c, the "xml" prefix should never be declared.

The reason it started appearing in the new Castor (0.9.5.2), is because
of a switch to SAX 2 by default during unmarshaling.

**Solution:** A built in work-around has been checked into the Castor
SVN and will automatically exist in any post 0.9.5.2 releases. For those
who are using 0.9.5.2 and can't upgrade, I found a simple workaround
(tested with Xerces 2.5). At first I thought about disabling namespace
processing in Xerces, but then realized that it's already disabled by
default by Castor ... so I have no idea why they call
\#startPrefixMapping when namespace processing has been disabled. But in
any event... explicitly enabling namespace processing seems to fix the
problem:

in the `castor.properties` file, change the following line:

``` {.java}
 org.exolab.castor.parser.namespaces=false
            
```

to:

``` {.java}
 org.exolab.castor.parser.namespaces=true
            
```

> **Note**
>
> This work-around has only been tested with Xerces 2.5 and above.

### Why is my 'get' method called twice?

The get method will be called a second time during the validation
process. To prevent this from happening, simply disable validation on
the Marshaller or Unmarshaller.

### How can I speed up marshalling/unmarshalling performance?

-   Cache the descriptors!

    ``` {.java}
     import org.exolab.castor.xml.ClassDescriptorResolver;
     import org.exolab.castor.xml.Unmarshaller;
     import org.exolab.castor.xml.util.ClassDescriptorResolverImpl;
     ...
     ClassDescriptorResolver cdr = new ClassDescriptorResovlerImpl(); 
     ...
     Unmarshaller unm = new Unmarshaller(...);
     unm.setResolver(cdr);
                        
    ```

    By reusing the same `ClassDescriptorResolver` any time you create an
    Unmarshaller instance, you will be reusing the existing class
    descriptors previously loaded.

-   Disable validation

    ``` {.java}
     unm.setValidation(false);
                        
    ```

-   Reuse objects

    To cut down on object creation, you can reuse an existing object
    model, but be careful because this is an experimental feature.
    Create an Unmarshaller with your existing root object and set object
    reuse to true...

    ``` {.java}
     Unmarshaller unm = new
     Unmarshaller(myObjectRoot);
                        
    ```

    ``` {.java}
     unm.setReuseObjects(true);
                        
    ```

-   If you have enabled pretty-printing (indenting), then disable it.
    The Xerces Serializer is much slower with indenting enabled.

-   Try changing parsers to something other than Xerces.

There are probably other approaches you can use as well, but those seem
to be the most popular ones. Let us know if you have a solution that you
think we should add here.

### How do I ignore elements during unmarshalling?

-   Use the `
                                Unmarshaller#setIgnoreExtraElements()
                            ` method:

    ``` {.java}
     Unmarshaller unm = new Unmarshaller(...);
     unm.setIgnoreExtraElements(true);
                        
    ```

    If any elements appear in the XML instance that Castor cannot find
    mappings for, they will be skipped.

-   You can also set the `
                                org.exolab.castor.xml.strictelements 
                            ` property in the `castor.properties` file:

    ``` {.java}
     org.exolab.castor.xml.strictelements=true
                        
    ```

### Where does Castor search for the castor.properties file?

Castor loads the castor.properties in the following order:

-   From classpath (usually from the jar file)

-   From {java.home}/lib (if present)

-   From the local working directory

Each properties file overrides the previous. So you don't have to come
up with a properties file with all the properties and values, just the
ones you want to change. This also means you don't have to touch the
properties file found in the jar file.

> **Note**
>
> Note: You can also use `
>                         LocalConfiguration.getInstance().getProperties()
>                     ` to change the properties values programatically.

### Can I programmatically change the properties found in the castor.properties file?

Yes, many of these properties can be set directly on the Marshaller or
Unmarshaller, but you can also use `
                    LocalConfiguration.getInstance().getProperties()
                ` to change the properties values programatically.

Introspection
-------------

### Can private methods be introspected?

Castor does not currently support introspection of private methods.
Please make sure proper public accesssor methods are available for all
fields that you wish to be handled by the Marshalling Framework.

Mapping {#xml.faq.mapping}
-------

### My mapping file seems to have no effect!

Make sure you are not using one of the *static* methods on the
Marshaller/Unmarshaller. Any configuration changes that you make to the
Marshaller or Unmarshaller are not available from the static methods.

### Are there any tools to automatically create a mapping file?

Yes! We provide one such tool, see `org.exolab.castor.tools.MappingTool`
. There are some [3rd party](http://www.castor.org/extras.html) tools as
well.

### How do I specify a namespace in the mapping file?

For a specific field you can use a QName for the value of the bind-xml
name attribute as such:

``` {.java}
 <bind-xml name="foo:bar" xmlns:foo="http://www.acme.com/foo"/>
            
```

Note: The namespace prefix is only used for qualification during the
loading of the mapping, it is not used during Marshaling. To map
namespace prefixes during marshaling you currently need to set these via
the Marshaler directly.

For a class mapping, use the &lt;map-to&gt; element. For more
information see the [XML Mapping
documentation](http://www.castor.org/xml-mapping.html) .

### How do I prevent a field from being marshaled?

Set the **transient** attribute on the &lt;bind-xml&gt; element to true:

``` {.java}
 <bind-xml transient="true"/>
            
```

Note: You can also set transient="true" on the &lt;field&gt; element.

Marshalling {#xml.faq.marshalling}
-----------

### The XML is marshalled on one line, how do I force line-breaks?

For all versions of Castor:

To enable pretty-printing (indenting, line-breaks) just modify the
***castor.properties*** file and uncomment the following:

``` {.java}
 # True if all documents should be indented on output by default
 #
 #org.exolab.castor.indent=true
            
```

Note: This will slow down the marshalling process

### What is the order of the marshalled XML elements?

If you are using Castor's default introspection to automatically map the
objects into XML, then there is no guarantee on the order. It simply
depends on the order in which the fields are returned to Castor using
the Java reflection API.

Note: If you use a mapping file Castor will generate the XML in the
order in which the mapping file is specified.

Source code generation {#xml.faq.generation}
----------------------

### Can I use a DTD with the source generator?

Not directly, however you can convert your DTD to an XML Schema fairly
easily. We provide a tool ( `
                    org.exolab.castor.xml.dtd.Converter
                ` ) to do this. You can also use any number of 3rd-party
tools such as XML Spy or XML Authority.

### My XML output looks incorrect, what could be wrong?

Also: I used the source code generator, but all my xml element names are
getting marshaled as lowercase with hyphens, what's up with that?

**Solution:** Are the generated class descriptors compiled? Make sure
they get compiled along with the source code for the object model.

### The generated source code has incorrect or missing imports for imported schema types

**Example:** Castor generates the following:

``` {.java}
 import types.Foo;
            
```

instead of:

``` {.java}
 import com.acme.types.Foo;
            
```

This usually happens when the namespaces for the imported schemas have
not been mapped to appropriate java packages in the
*castorbuilder.properties* file.

**Solution:**

-   Make sure the `
                                    castorbuilder.properties
                                ` is in your classpath when you run
    the SourceGenerator.

-   Uncomment and edit the `
                                    org.exolab.castor.builder.nspackages
                                ` property. Make sure to copy the value
    of the imported namespace exactly as it's referred to in the
    schema (i.e. trailing slashes and case-sensitivity matter!).

For those using 0.9.5.1, you'll need to upgrade due to a bug that is
fixed in later releases.

### How can I make the generated source code more JDO friendly?

For Castor 0.9.4 and above:

Castor JDO requires a reference to the actual collection to be returned
from the get-method. By default the source generator does not provide
such a method. To enable such methods to be created, simple add the
following line to your `castorbuilder.properties` file:

``` {.java}
 org.exolab.castor.builder.extraCollectionMethods=true
            
```

Note: The default `castorbuilder.properties` file has this line
commented out. Simply uncomment it.

Your mapping file will also need to be updated to include the proper
set/get method names.

Miscellaneous {#xml.faq.misc}
-------------

### Is there a way to automatically create an XML Schema from an XML instance?

Yes! We provide such a tool. Please see `
                    org.exolab.castor.xml.schema.util.XMLInstance2Schema
                ` . It's not 100% perfect, but it does a reasonable job.

### How to enable XML validation with Castor XML

To enable XML validation at the parser level, please add properties to
your `castor.properties` file as follows:

``` {.java}
 org.exolab.castor.parser.namespaces=true
 org.exolab.castor.sax.features=http://xml.org/sax/features/validation,\
 http://apache.org/xml/features/validation/schema,\
 http://apache.org/xml/features/validation/schema-full-checking
            
```

Please note that the example given relies on the use of Apache Xerces,
hence the `apache.org` properties; similar options should exist for
other parsers.

### Why is mapping ignored when using a FieldHandlerFactory

When using a custom FieldHandlerFactory as in the following example

``` {.java}
 Mapping mapping = ... ;
 FieldHandlerFactoyt factory = ...;
 Marshaller m = new Marshaller(writer);
 ClassDescriptorResolverImpl cdr = new ClassDescriptorResolverImpl();
 cdr.getIntrospector().addFieldHandlerFactory(factory);
 m.setResolver(cdr);
 marshaller.setMapping(mapping);
            
```

please make sure that you set the mapping file **after** you set the
ClassDescriptorResolver. You will note the following in the Javadoc for
`org.exolab.castor.xml.Marshaller.html#setResolver(org.exolab.castor.xml.ClassDescriptorResolver)`
:

> **Note**
>
> **Note:** This method will nullify any Mapping currently being used by
> this Marshaller

Serialization {#xml.faq.serialization}
-------------

### Is it true that the use of Castor XML mandates [Apache Xerces](http://xerces.apache.org) as XML parser?

Yes and no. It actually depends. When requiring *pretty printing* during
marshalling, Castor internally relies on Apache's Xerces to implement
this feature. As such, when not using this feature, Xerces is not a
requirement, and any JAXP-compliant XML parser can be used (for
unmarshalling).

In other words, with the latter use case, you do **not** have to
download (and use) Xerces separetely.

### Do I still have to download Xerces when using Castor XML with Java 5.0?

No. Starting with release 1.1, we have added support for using the
Xerces instance as shipped with the JRE/JDK for serialization. As such,
for Java 5.0 users, this removes the requirement to download Xerces
separately when wanting to use 'pretty printing' with Castor XML during
marshalling.

To enable this feature, please change the following properties in your
**local** `castor.properties` file (thus redefining the default value)
as shown below:

``` {.java}
 # Defines the XML parser to be used by Castor. 
 # The parser must implement org.xml.sax.Parser.
 org.exolab.castor.parser=org.xml.sax.helpers.XMLReaderAdapter

 # Defines the (default) XML serializer factory to use by Castor, which must
 # implement org.exolab.castor.xml.SerializerFactory; default is
 # org.exolab.castor.xml.XercesXMLSerializerFactory
 org.exolab.castor.xml.serializer.factory=org.exolab.castor.xml.XercesJDK5XMLSerializerFactory

 # Defines the default XML parser to be used by Castor.
 org.exolab.castor.parser=com.sun.org.apache.xerces.internal.parsers.SAXParser
            
```
