Usage of Castor and XML parsers
===============================

SAX/DOM
-------

Being an **XML data binding framework** by definition, Castor XML relies
on the availability of an XML parser at run-time. In Java, an XML parser
is by default accessed though either the DOM or the SAX APIs: that
implies that the XML Parser used needs to comply with either (or both)
of these APIs.

With the creation of the JAXP API (and its addition to the Java language
definition as of Java 5.0), Castor internally has been enabled to allow
usage of the JAXP interfaces to interface to XML parsers. As such,
Castor XML allows the use of a JAXP-compliant XML parser as well.

By default, Castor ships with [Apache
Xerces](http://xml.apache.org/xerces) 2.6.2. You may, of course, upgrade
to a newer version of [Apache Xerces](http://xml.apache.org/xerces) at
your convenience, or switch to any other XML parser as long as it is
JAXP compliant or implements a particular SAX interface. Please note
that users of Java 5.0 and above do not need to have Xerces available at
run-time, as JAXP and Xerces have both been integrated into the run-time
library of Java.

For marshalling, Castor XML can equally use any JAXP complaint XML
parser (or interact with an XML parser that implements the SAX API),
with the exception of the following special case: when using 'pretty
printing' during marshalling (by setting the corresponding property in
`castor.properties` to `true`) with Java 1.4 or below, [Apache
Xerces](http://xml.apache.org/xerces) has to be on the classpath, as
Castor XML internally uses Xerces' `XMLSerializer` to implement this
feature.

The following table enlists the requirements relative to the Java
version used in your environment.

  Java 1.4 and below    Java 5.0 and above
  -------------------- --------------------
  Xerces 2.6.2                  -
  XML APIs                      -

  : XML APIs on various Java versions

StAX
----

As of Castor 1.3.2, Castor XML can be used with a StAX-compliant parser
to unmarshal from XML. Please see
[???](#xml.framework.introspection.ouput) for StAX-specific unmarshal
methods added to `org.exolab.castor.xml.Unmarshaller`.
