# Castor - Data binding made easy

[![Build Status](https://travis-ci.org/castor-data-binding/castor.svg?branch=master)](https://travis-ci.org/castor-data-binding/castor) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.codehaus.castor/castor-xml/badge.png)](https://maven-badges.herokuapp.com/maven-central/org.codehaus.castor/castor-xml)

Castor is an open source data binding framework for Java[tm]. It provides direct, short paths 
between Java objects and XML documents. Castor essentially provides Java-to-XML binding and more.

Project documentation:
http://castor-data-binding.github.io/castor/

**Note**: The persistence part of Castor has been migrated to a separate [GitHub repository](https://github.com/castor-data-binding/castor-jdo.git).

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

or download jars from [Maven Central](http://repo1.maven.org/maven2/org/codehaus/castor/).

To use Castor's source generator, you need to use the following Maven dependency:

```
<dependency>
  <groupId>org.codehaus.castor</groupId>
  <artifactId>castor-codegen</artifactId>
  <version>${castor-codegen-version}</version>
</dependency>
```

## Non-Maven

If you are not using Maven as build too, you can still get the binary JARs for a particular release in form of a binary distribution.

Please browse to [Sonatype's OSS repository](https://oss.sonatype.org/content/groups/public/org/codehaus/castor/castor-distribution) or [Sonatype's OSS snapshot repository](https://oss.sonatype.org/content/groups/public/org/codehaus/castor/castor-distribution/), select the required (snapshot) release and download one of the available distributions:

* Castor XML binaries only (named castor-distribution-x.y.z-xml-bin.zip)
* Castor XML binaries with dependencies (e.g. castor-distribution-x.y.z-xml-bin-with-dependencies.zip)

or 

* Castor XML codegen binaries only (named castor-distribution-x.y.z-codegen-bin.zip)
* Castor XML codegen binaries with dependencies (e.g. castor-distribution-x.y.z-codegen-bin-with-dependencies.zip)


# Participation

The easiest ways to participate beyond using Castor is to join one or many of the Castor mailing lists at [freelists.org](http://www.freelists.org):

* [Castor Announce](http://www.freelists.org/list/castor.announce): Announcement-only list for new Castor releases and other events related to Castor
* [Castor User](http://www.freelists.org/list/castor.user): List dedicated for discussion on Castor usage
* [Castor Dev](http://www.freelists.org/list/castor.dev): List for developers of Castor core components and modules, discussing implementation details, API changes.

# Documentation

## Web sites

* [Documentation](http://castor-data-binding.github.io/castor/main/index.html)
* [Wiki](https://github.com/castor-data-binding/castor/wiki)

