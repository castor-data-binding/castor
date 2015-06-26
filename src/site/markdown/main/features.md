# Features

## Definition (from wikipedia.org) 

> Castor is an open source data binding framework for moving data from XML to Java programming language objects and from Java to databases.
           
           
Castor is made up of (independent) modules as follows:
        
* Castor XML
* Castor XML - Code generator
* Additional tools
* Integration with other frameworks
       
## Castor XML
       
XML data binding framework to bind XML artefacts to Java objects and vice versa.
            
* Employs default mapping conventions for mapping-less operation
* (Optional) mapping file for existing (more complex) object models
* XML-based mapping file to specify bindings for existing object models.
* Support for schemaless Java-to-XML binding.
            
## Castor XML code generator

Code generator that generates Java source code from XML schema information.
            
* Generates Java domain classes based upon the type/element definitions in the given XML schema(s).
* Support for native Java 5 artifacts (e.g. enums, annotations)
* Support for enum-style classes for Java 1.4
* Additionally generates descriptor classes to be used during (un)marshalling to dramatically enhance performance.
* Extended support for in-object validation (through validation rules encoded in descriptor classes).
        
## Additional tools
        
* MappingTool: Ability to create base mapping from existing Java classes
* XMLInstance2Schema: Ability to create an XML Schema from an XML input document
* DTDConvertor: Converts a DTD definition to an equivalent XML schema definition.

## Build management integration

Various artefacts to support build management systems.
            
* Ant task definitions for XML code generator.
* [Maven plugin](http://mojo.codehaus.org/castor-maven-plugin/) for XML code generator.

## Integration with other frameworks
        
Castor is currently integrated with the following frameworks or has support for being integrated:
        
* [Spring OXM](http://static.springframework.org/spring-ws/site/reference/html/oxm.html) for Castor
* [Spring XML](./spring-xml-intro.html) artefacts.
* Apache Cocoon (Castor transformer)
* [extendedXML module](http://www.mulesource.org/display/EXTENDEDXML/) for Mule, offering enhanced XML-transformation support for Mule, using Castor. 
    
