# Castor - Data binding made easy

[![Build Status](https://travis-ci.org/castor-data-binding/castor.svg?branch=master)](https://travis-ci.org/castor-data-binding/castor) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.codehaus.castor/castor-xml/badge.png)](https://maven-badges.herokuapp.com/maven-central/org.codehaus.castor/castor-xml)

Castor is an open source data binding framework for Java[tm]. It provides direct, short paths 
between Java objects and XML documents. Castor essentially provides Java-to-XML binding and more.

Project documentation:
http://castor-data-binding.github.io/castor/

The persistence part of Castor has been migrated to a separate git repository, available at https://github.com/castor-data-binding/castor-jdo.git.

# Download

## Maven

To use Castor XML, you need to use following Maven dependency:

```
<dependency>
  <groupId>org.codehaus.castor</groupId>
  <artifactId>castor-xml</artifactId>
  <version>${castor-xml-version}</version>
</dependency>
```

or download jars from [Maven Central](http://repo1.maven.org/maven2/org/codehaus/.

To use Castor's source generator, you need to use the following Maven dependency:

```
<dependency>
  <groupId>org.codehaus.castor</groupId>
  <artifactId>castor-codegen</artifactId>
  <version>${castor-codegen-version}</version>
</dependency>
```

## Non-Maven

For non-Maven users, please download jars from [Maven Central](http://repo1.maven.org/maven2/org/codehaus/castor/).

# Participation

The easiest ways to participate beyond using Castor is to join one of Castor mailing lists [freelists.org](http://www.freelists.org):

* Castor Announce: Announcement-only list for new Castor releases and other events related to Castor
* Castor User: List dedicated for discussion on Castor usage
* Castor Dev: List for developers of Castor core components and modules, discussing implementation details, API changes.
