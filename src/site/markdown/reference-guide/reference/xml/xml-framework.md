XML framework
=============

Introduction
------------

Castor XML is an XML data binding framework. Unlike the two main XML
APIs, DOM (Document Object Model) and SAX (Simple API for XML) which
deal with the structure of an XML document, Castor enables you to deal
with the data defined in an XML document through an object model which
represents that data.

Castor XML can marshal almost any "bean-like" Java Object to and from
XML. In most cases the marshalling framework uses a set of
ClassDescriptors and FieldDescriptors to describe how an Object should
be marshalled and unmarshalled from XML.

For those not familiar with the terms "marshal" and "unmarshal", it's
simply the act of converting a stream (sequence of bytes) of data to and
from an Object. The act of "marshalling" consists of converting an
Object to a stream, and "unmarshalling" from a stream to an Object.

Castor XML - The XML data binding framework
-------------------------------------------

The XML data binding framework, as it's name implies, is responsible for
doing the conversion between Java and XML. The framework consists of two
worker classes, `org/exolab/castor/xml/Marshaller` and
`org.exolab.castor.xml.Unmarshaller` respectively, and a bootstrap class
`org.exolab.castor.xml.XMLContext` used for configuration of the XML
data binding framework and instantiation of the two worker objects.

Lets walk through a very simple example. Assume we have a simple
`Person` class as follows:

``` {.java}
 import java.util.Date;

 /** An simple person class */
 public class Person implements java.io.Serializable {

	 private String name = null;

	 private Date dob = null;

	 public Person() {
	 	super();
	 }

	 public Person(String name) { 
	 	this.name = name; 
	 }

	 public Date getDateOfBirth() { 
	 	return dob; 
	 }

	 public String getName() { 
	 	return name; 
	 }

	 public void setDateOfBirth(Date dob) { 
	 	this.dob = dob; 
	 }

	 public void setName(String name) { 
	 	this.name = name; 
	 }
}  
```

To (un-)marshal data to and from XML, Castor XML can be used in one of
three modes:

-   introspection mode
-   mapping mode
-   descriptor mode (aka generation mode)

The following sections discuss each of these modes at a high level.

### Introspection mode

The *introspection mode* is the simplest mode to use from a user
perspective, as it does not require any configuration from the user. As
such, the user does not have to provide any mapping file(s), nor point
Castor to any generated descriptor classes (as discussed in the
'descriptor mode' section).

In this mode, the user makes use of **static** methods on the
`org.exolab.castor.xml.Marshaller` and
`org.exolab.castor.xml.Unmarshaller` classes, providing all required
data as parameters on these method calls.

To marshal an instance of the person class you simply call the
`org.exolab.castor.xml.Marshaller` as follows:

``` {.java}
// Create a new Person
Person person = new Person("Ryan 'Mad Dog' Madden");
person.setDateOfBirth(new Date(1955, 8, 15));

// Create a File to marshal to
writer = new FileWriter("test.xml");

// Marshal the person object
Marshaller.marshal(person, writer);         
```

This produces the XML shown in

``` {.xml}
               XML to written
            
```

To unmarshal an instance of the person class from XML, you simply call
the `org.exolab.castor.xml.Unmarshaller` as follows:

``` {.java}
// Create a Reader to the file to unmarshal from
reader = new FileReader("test.xml");

// Marshal the person object
Person person = (Person) Unmarshaller.unmarshal(Person.class, reader);
```

Marshalling and unmarshalling is basically that simple.

> **Note**
>
> Note: The above example uses the static
> methods of the marshalling framework, and as such no Marshaller and/or
> Unmarshaller instances need to be created. A common mistake in this
> context when using a mapping file
> is to call the
> org.exolab.castor.xml.Marshaller
> or
> org.exolab.castor.xml.Unmarshaller
> as in the above example. This won't work, as the mapping will be
> ignored.

In *introspection mode* , Castor XML uses Java reflection to establish
the binding between the Java classes (and their properties) and the XML,
following a set of (default) naming rules. Whilst it is possible to
change to a different set of naming rules, there's no way to override
this (default) naming for individual artifacts. In such a case, a
*mapping file* should be used.

### Mapping mode

In *mapping mode* , the user provides Castor XML with a user-defined
mapping (in form of a mapping file) that allows the (partial) definition
of a customized mapping between Java classes (and their properties) and
XML.

When you are using a mapping file, create an instance of the
`org.exolab.castor.xml.XMLContext` class and use the
org.exolab.castor.xml.XMLContext.addMapping(Mapping) method to provide
Castor XML with one of more mapping files.

To start using Castor XML for marshalling and/or unmarshalling based
upon your custom mapping, create instances of
`org.exolab.castor.xml.Marshaller` and
`org.exolab.castor.xml.Unmarshaller` as needed using one of the
following methods:

| Method name |  Description
| ----------- | ------------
| [createMarshaller](org.exolab.castor.xml.XMLContext) | Creates a [Marshaller](org.exolab.castor.xml.Marshaller) instance.
| [createUnmarshaller](org.exolab.castor.xml.XMLContext) | Creates a [Unmarshaller](org.exolab.castor.xml.Unmarshaller) instance.
[Methods on XMLContext to create Un-/Marshaller objects]

and call any of the **non-static** (un)marshal methods to trigger data binding in either way.

Below code shows a full example that demonstrates unmarshalling a
`Person` instance from XML using a `org.exolab.castor.xml.Unmarshaller`
instance as obtained from an XMLContext previously configured to your
needs.

``` {.java}
   import org.exolab.castor.xml.XMLContext; 
   import org.exolab.castor.mapping.Mapping; 
   import org.exolab.castor.xml.Unmarshaller;

   // Load Mapping
   Mapping mapping = new Mapping();
   mapping.loadMapping("mapping.xml");

   // initialize and configure XMLContext
   XMLContext context = new XMLContext();
   context.addMapping(mapping);

   // Create a Reader to the file to unmarshal from
   reader = new FileReader("test.xml");

   // Create a new Unmarshaller
   Unmarshaller unmarshaller = context.createUnmarshaller();
   unmarshaller.setClass(Person.class);

   // Unmarshal the person object
   Person person = (Person)
   unmarshaller.unmarshal(reader);
```

To marshal the very same `Person` instance to XML using a
`org.exolab.castor.xml.Marshaller` obtained from the **same**
`org.exolab.castor.xml.XMLContext` , use code as follows:

``` {.java}
   import org.exolab.castor.xml.Marshaller;

   // create a Writer to the file to marshal to
   Writer writer = new FileWriter("out.xml");

   // create a new Marshaller
   Marshaller marshaller = context.createMarshaller();
   marshaller.setWriter(writer);

   // marshal the person object
   marshaller.marshal(person);
```

Please have a look at [XML
Mapping](http://www.castor.org/xml-mapping.html) for a detailed
discussion of the mapping file and its structure.

For more information on how to effectively deal with loading mapping
file(s) especially in multi-threaded environments, please check the
[best practice](http://www.castor.org/xml-best-practice.html) section.

Sources and destinations
------------------------

Castor supports multiple sources and destinations from which objects can
be marshalled and unmarshalled:

| Destination | Description
| ----------- | -----------
| `marshal(java.io.Writer)` | The character stream.
| `marshal(org.xml.sax.DocumentHandler)` | The SAX document handler.
| `marshal(org.xml.sax.ContentHandler)` | The SAX content handler.
| `marshal(org.w3c.dom.Node)` | The DOM node to marshall object into.
| `marshal(javax.xml.stream.XMLStreamWriter)` | The STaX cursor API.
| `marshal(javax.xml.stream.XMLEventWriter)` | The STaX iterator API.
| `marshal(javax.xml.transform.Result)` | `javax.xml.transform.dom.DOMResult` , `javax.xml.transform.sax.SAXResult` and `javax.xml.transform.stream.StreamResult` are supported.
[Marshalling destinations.]

| Source | Description
| ------ | -----------
| `unmarshal(java.io.Reader)` | A character stream.
| `unmarshal(org.xml.sax.InputSource)` | A SAX input source.
| `unmarshal(org.w3c.dom.Node)` | A W3C DOM node which will be used for unmarshalling.
| `unmarshal(javax.xml.stream.XMLStreamReader)` | A StAX cursor.
| `unmarshal(javax.xml.stream.XMLEventReader)` | A StAX iterator.
| `unmarshal(javax.xml.transform.Source)` | Supports `javax.xml.transform.dom.DOMSource` , `javax.xml.transform.sax.SAXSource` and `javax.xml.transform.stream.StreamSource` .
[Unmarshalling sources]

Castor 1.3.2 and 1.3.3 introduced support for the STaX API for both for
marshalling and unmarshalling. The framework fully supports the STaX
cursor and iterator API.

An example of marshalling using STaX:

``` {.java}
// marshalling using STaX
StringWriter writer = new StringWriter();
XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
XMLStreamWriter xmlStreamWriter =
outputFactory.createXMLStreamWriter(writer);

marshaller.setXmlStreamWriter(xmlStreamWriter);
marshaller.marshal(object);
         
```

Also beginning from version 1.3.3, the framework has been modified to
support Source and Result interfaces. Now it is possible to use
SAXSource, DOMSource and StreamSource for unmarshalling and
corresponding classes for marshalling.

Below an example of marshalling into Result:

``` {.java}
// instance of object to be marshalled
Object obj = ...

// marshalling into DOM node
XMLContext xmlContext = ... // creates the xml context

// creates marshaller
Marshaller marshaller = xmlContext.createMarshaller();

// creates DOM factory
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();

// creates document
Document document = builder.newDocument();

// sets the DOM result for the marshaller
marshaller.setResult(new DOMResult(document));

// marshalls object
marshaller.marshall(obj);
 
```

Another example of unmarshalling from Source:

``` {.java}
// unmarshalling from SAX InputSource
XMLContext xmlContext = ... // creates the xml context

// creates unmarshaller
Unmarshaller unmarshaller = xmlContext.createUnmarshaller();

// creates SAX input source
InputSource inputSource = new InputSource(new StringReader(xml));

// creates instance of SAXSource
SAXSource saxSource = new SAXSource(inputSource);

// unmarshalls object 
Object result = unmarshaller.unmarshal(saxSource);
         
```

XMLContext - A consolidated way to bootstrap Castor
---------------------------------------------------

With Castor 1.1.2, the `org.exolab.castor.xml.XMLContext` class has been
added to the Castor marshalling framework. This new class provides a
bootstrap mechanism for Castor XML, and allows easy (and efficient)
instantiation of `org.exolab.castor.xml.Marshaller` and
`org.exolab.castor.xml.Unmarshaller` instances as needed.

As shown above, the `org.exolab.castor.xml.XMLContext` class offers
various factory methods to obtain a new
`org.exolab.castor.xml.Marshaller` ,
`org.exolab.castor.xml.Unmarshaller` .

When you need more than one `org.exolab.castor.xml.Unmarshaller`
instance in your application, please call
org.exolab.castor.xml.XMLContext.createUnmarshaller() as required. As
all `Unmarshaller` instances are created from the very same `XMLContext`
instance, overhead will be minimal. Please note, though, that use of one
`Unmarshaller` instance is not thread-safe.

Using existing Classes/Objects
------------------------------

Castor can marshal "almost" any arbitrary Object to and from XML. When
descriptors are not available for a specific Class, the marshaling
framework uses reflection to gain information about the object.

> **Note**
>
> Actually an in memory set of descriptors are created for the object
> and we will soon have a way for saving these descriptors as Java
> source, so that they may be modified and compiled with little effort.

If a set of descriptors exist for the classes, then Castor will use
those to gain information about how to handle the marshaling.

There is one main restrictions to marshaling objects. These classes
must have have a public default constructor (ie. a constructor with no
arguments) and adequate "getter" and "setter" methods to be properly be
marshaled and unmarshaled.

The example illustrated in the previous section demonstrates how
to use the framework with existing classes.

Class Descriptors
-----------------

Class descriptors provide the "Castor Framework" with necessary
information so that the Class can be marshaled properly. The class
descriptors can be shared between the JDO and XML frameworks.

Class descriptors contain a set of field descriptors.

XML Class descriptors provide the marshaling framework with the
information it needs about a class in order to be marshaled to and from
XML. The XMLClassDescriptor `org.exolab.castor.xml.XMLClassDescriptor` .

XML Class Descriptors are created in four main ways. Two of these are
basically run-time, and the other two are compile time.

### Compile-Time Descriptors

To use "compile-time" class descriptors, one can either implement the
`org.exolab.castor.xml.XMLClassDescriptor
               ` interface for each class which needs to be "described",
or have the [Source Code Generator](http://www.castor.org/sourcegen.xml)
create the proper descriptors.

The main advantage of compile-time descriptors is that they are faster
than the run-time approach.

### Run-Time Descriptors

To use "run-time" class descriptors, one can either simply let Castor
introspect the classes, a mapping file can be provided, or a combination
of both "default introspection" and a specified mapping file may be
used.

For "default introspection" to work the class being introspected must
have adequate setter/getter methods for each field of the class that
should be marshaled and unmarshaled. If no getter/setter methods
exist, Castor can handle direct field access to public fields. It does
not do both at the same time. So if the respective class has any
getter/setter methods at all, then no direct field access will take
place.

There is nothing to do to enable "default introspection". If a
descriptor cannot be found for a class, introspection occurs
automatically.

Some behaviour of the introspector may be controlled by setting the
appropriate properties in the *castor.properties* file. Such behaviour
consists of changing the naming conventions, and whether primitive types
are treated as attributes or elements. See *castor.properties* file for
more information.

A mapping file may also be used to "describe" the classes which are to
be marshaled. The mapping is loaded before any
marshaling/unmarshaling takes place. See `org.exolab.castor.mapping.Mapping`

The main advantage of run-time descriptors is that it takes very little
effort to get something working.
