XML code generation - Extensions 
================================

XML code generation extensions - Motivation 
===========================================

With Castor 1.2 and earlier releases it has already been possible to
generate Java classes from an XML schema and use these classes for XML
data binding **without** having to write a mapping file.

This is possible because the Castor XML code generator generated - in
addition to the domain classes - a set of XML descriptor classes as
well, with one descriptor class generated per generated domain class.
It's this XML descriptor class that holds all the information required
to map Java classes and/or field members to XML artifacts, as set out in
the original XML schema definitions. This includes ....

-   artifact names

-   XML namespace URIs

-   XML namespace prefix

-   validation code

Starting with Castor 1.3, a mechanism has been added to the XML code
generator that allows extension of these core offerings so that either
additional content is added to the generated domain classes additional
descriptor classes are generated.
