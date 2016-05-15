Castor XML - Tips & Tricks
==========================

Logging and Tracing
-------------------

When developing using Castor, we recommend that you use the various
`setLogWriter` methods to get detailed information and error messages.

Using a logger with `org.exolab.castor.mapping.Mapping` will provide
detailed information about mapping decisions made by Castor and will
show the SQL statements being used.

Using a logger with `org.exolab.castor.jdo.JDO` will provide trace
messages that show when Castor is loading, storing, creating and
deleting objects. All database operations will appear in the log; if an
object is retrieved from the cache or is not modified, there will be no
trace of load/store operations.

Using a logger with `org.exolab.castor.xml.Unmarshaller` will provide
trace messages that show conflicts between the XML document and loaded
objects.

A simple trace logger can be obtained from
`org.exolab.castor.util.Logger`. This logger uses the standard output
stream, but prefixes each line with a short message that indicates who
generated it. It can also print the time and date of each message. Since
logging is used for warning messages and simple tracing, Castor does not
require a sophisticated logging mechanism.

Interested in integratating Castor's logging with Log4J? Then see [this
question](http://www.castor.org/jdo-faq.html#How-can-I-integrate-Castor's-logging-with-a-logging-infrastructure-using-Log4J?)
in the JDO FAQ.

Indentation
-----------

By default the marshaler writes XML documents without indentation. When
developing using Castor or when debugging an application that uses
Castor, it might be desireable to use indentation to make the XML
documents human-readable. To turn indentation on, modify the Castor
properties file, or create a new properties file in the classpath (named
`castor.properties`) with the following content:

    org.exolab.castor.indent=true
          

Indentation inflates the size of the generated XML documents, and also
consumes more CPU. It is recommended not to use indentation in a
production environment.

XML:Marshal validation
----------------------

It is possible to disable the validation in the marshaling framework by
modifying the Castor properties file or by creating a new properties
file in the classpath (named `castor.properties`) with the following
content:

     org.exolab.castor.marshalling.validation=false
            

NoClassDefFoundError
--------------------

Check your CLASSPATH, check it often, there is no reason not to!

Mapping: auto-complete
----------------------

> **Note**
>
> This only works with Castor-XML.

To save time when writing your mappings, try using the *auto-complete*
attribute of *class*. When using auto-complete, Castor will introspect
your class and automatically fill in any missing fields.

**Example:**

``` {.xml}
<class name="com.acme.Foo" auto-complete="true"/>          
```

This is also compatible with generated descriptor files. You can use a
mapping file to override some of the behavior of a compiled descriptor
by using auto-complete.

> **Note**
>
> Be careful to make sure you use the exact field name as specified in
> the generated descriptor file in order to modify the behavior of the
> field descriptor! Otherwise, you'll probably end up with two fields
> being marshaled!

Create method
-------------

Castor requires that classes have a public, no-argument constructor in
order to provide the ability to marshal & unmarshal objects of that
type.

create-method is an optional attribute to the `<field>` mapping element
that can be used to overcome this restriction in cases where you have an
existing object model that consists of, say, singleton classes where
public, no-argument constructors must not be present by definition.

Assume for example that a class "`A`" that you want to be able to
unmarshal uses a singleton class as one of its properties. When
attempting to unmarshal class "`A`", you should get an exception because
the singleton property has no public no-arg constructor. Assuming that a
reference to the singleton can be obtained via a static getInstance()
method, you can add a "create method" to class `A` like this:

``` {.java}
public MySingleton getSingletonProperty() {
    return MySingleton.getInstance();
}      
```

and in the mapping file for class `A`, you can define the singleton
property like this:

``` {.xml}
 <field name="mySingletonProperty"
       type="com.u2d.MySingleton"
       create-method="getSingletonProperty">
    <bind-xml name="my-singleton-property" node="element" />
 </field>
```

This illustrates how the create-method attribute is quite a useful
mechanism for dealing with exceptional situations where you might want
to take advantage of marshaling even when some classes do not have
no-argument public constructors.

> **Note**
>
> As of this writing, the specified create-method must exist as a method
> in the current class (i.e. the class being described by the current
> `<class>` element). In the future it may be possible to use external
> static factory methods.

MarshalListener and UnmarshalListener
-------------------------------------

Castor allows control on the object being marshaled or unmarshaled by a
set of two listener interfaces: MarshalListener and UnmarshalListener.

The MarshalListener interface located in `org.exolab.castor.xml` listens
to two different events that are intercepted by the following methods:

-   preMarshal: this method is called before an object gets marshaled.

-   postMarshal: this method is called once an object has
    been marshaled.

The UnmarshalListener located also in `org.castor.xml` listens to four
different events that are intercepted by the following methods:

-   initialized: this method is called once an object has
    been instantiated.

-   attributesProcessed: this method is called when the attributes have
    just been read and processed.

-   fieldAdded: this method is called when an object is added to
    a parent.

-   unmarshalled: this method is called when an object has been
    **fully** unmarshaled

**Note:** The `UnmarshalListener` had been part of
`org.exolab.castor.xml` but as an extention of this interface had been
required a new interface in `org.castor.xml` was introduced. Currently
the `org.exolab.castor.xml.UnmarshalListener` interface can still be
used but is deprecated.
