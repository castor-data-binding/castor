# Features

## Definition (from wikipedia.org) 

> Castor is an open source data binding framework for moving data from XML to Java programming language objects and from Java to databases.
           
           
Castor is made up of (independent) modules as follows:
        
* Castor XML
* Castor XML - Code generator
* Castor JDO - Persistence framework
* Castor JDO - DDL generator
* Additional tools
* Integration with other frameworks
       
## Castor XML
       
[XML data binding framework](/xml-framework.html) to bind XML artefacts to Java objects and vice versa.
            
* Employs default mapping conventions for mapping-less operation
* (Optional) mapping file for existing (more complex) object models
* XML-based [mapping file](xml-mapping.html) to specify bindings for existing object models.
* Support for schemaless Java-to-XML binding.
            
## Castor XML code generator

[Code generator][(./sourcegen.html) that generates Java source code from XML schema information.
            
* Generates Java domain classes based upon the type/element definitions in the given XML schema(s).
* Support for native Java 5 artifacts (e.g. enums, annotations)
* Support for enum-style classes for Java 1.4
* Additionally generates descriptor classes to be used during (un)marshalling to dramatically enhance performance.
* Extended support for in-object validation (through validation rules encoded in descriptor classes).

## Castor JDO

Java [persistence framework](jdo-introduction.html) to bind Java objects to database tables.
            
* XML-based [JDO mapping file](jdo-mapping.html) to specify bindings for (existing) object models.
* Support for ODMG [OQL queries](oql.html).
* EJB container managed persistence provider for OpenEJB
* Supports two-phase commits, object rollback and deadlock detection.
* Support for in-memory [caching](jdo-caching-detail.html) (various cache providers).
* Support for *write-at-commit* to reduce JDBC operations.

Castor JDO is not the same-as or compatible with Sun's JDO. We have a different approach to handling data object to RDBMS mappings. Please see the [JDO F.a.Q.](jdo-faq.html) for more information.
            
        
## Castor JDO DDL generator
        
Generates DDL statements from JDO mapping files.
        
## Additional tools
        
* MappingTool: Ability to create base mapping from existing Java classes
* XMLInstance2Schema: Ability to create an XML Schema from an XML input document
* DTDConvertor: Converts a DTD definition to an equivalent XML schema definition.

## Build management integration

Various artefacts to support build management systems.
            
* [Ant task definitions](srcgen-anttask.html) for XML code generator.
* [Maven plugin](http://mojo.codehaus.org/castor-maven-plugin/) for XML code generator.

## Integration with other frameworks
        
Castor is currently integrated with the following frameworks or has support for being integrated:
        
* [Spring ORM](./spring-orm-integration.html) support for Castor
* [Spring OXM](http://static.springframework.org/spring-ws/site/reference/html/oxm.html) for Castor
* [Spring XML](./spring-xml-intro.html) artefacts.
* [Web Service](./ws-integration.html) toolkits
* Apache Cocoon (Castor transformer)
* [extendedXML module](http://www.mulesource.org/display/EXTENDEDXML/) for Mule, offering enhanced XML-transformation support for Mule, using Castor 
    
See the [status page](status.html) for a more complete feature list and for more information.
