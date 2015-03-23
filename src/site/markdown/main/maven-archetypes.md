# Maven archetypes for Castor

## Introduction

Maven has a very sophisticated feature named 'archetype' for bootstrapping development 
with e.g. frameworks such as Castor is. As we do have a very high appreciation
for the benefits of this feature, we provide a selection of Maven archetypes
for Castor.

* codegen-testcase: A template for a XML code generator test case, incl. JUnit test case, sample XML schema, builder properties and binding file.
* cpa-testcase. A template for a JDO test case, incl. JUnit test case, JDO configuration file, JDO mapping file and test entity.
	    
## Template for XML code generator test case
	    
This archetype creates a standard Maven project with a *preconfigured*
project structure that includes the following artefacts:

* JUnit test case
* Sample XML schema
* (Empty) builder properties file
* (Empty) XML code generator binding file
* A Maven POM with all the required dependencies (Castor XML, etc.).
    
In order to e.g. create a project based upon the `codegen-testcase` archetypes 
shown above, please issue the following Maven goal on your command line (of choice):
	       
```
> mvn archetype:create -DarchetypeGroupId=org.codehaus.castor \
                       -DarchetypeArtifactId=codegen-testcase-archetype \
                       -DarchetypeVersion=1.3.1  \
                       -DgroupId=<your.group.id> \
                       -DartifactId=<your.artitact.id> \
                       -DpackageName=    
```

## Template for JDO test case
        
This archetype creates a standard Maven project with a *preconfigured* 
project structure that includes the following artefacts:

* JUnit test case
* JDO configuration file
* JDO mapping file
* DDL statements
* A Maven POM with all the required dependencies (Castor JDO, etc.)
    
In order to e.g. create a project based upon the `cpa-testcase` archetypes 
shown above, please issue the following Maven goal on your command line (of choice):
           
```
> mvn archetype:create -DarchetypeGroupId=org.codehaus.castor \
                       -DarchetypeArtifactId=cpa-testcase-archetype \
                       -DarchetypeVersion=1.3.1  \
                       -DgroupId=<your.group.id> \
                       -DartifactId=<your.artitact.id> \
                       -DpackageName=    
```

## CodeHaus Nexus repositories

In case you do not find the required archetypes in maven central repo, use the following repos:

* *Release artifacts*: (https://nexus.codehaus.org/content/repositories/releases)
* *Snapshot artifacts*: (https://nexus.codehaus.org/content/repositories/snapshots)

### Snapshot releases of archetypes

In case you are trying to use an archetype that has been made available as a snapshot
release only, please makse sure you include the Codehaus Maven repository in the
command as shown above similar to ...

```
> mvn archetype:create -DarchetypeGroupId=org.codehaus.castor \
                       -DarchetypeArtifactId=codegen-testcase-archetype \
                       -DarchetypeVersion=<b>1.3-SNAPSHOT</b>  \
                       -DgroupId=&lt;your.group.id&gt; \
                       -DartifactId=&lt;your.artitact.id&gt; \
                       -DpackageName=    
          -DremoteRepositories=https://nexus.codehaus.org/content/repositories/snapshots
```

Once a specific Castor release has been propagated to Maven central 
(usually within 48 hours), adding just the dependency will be sufficient.

