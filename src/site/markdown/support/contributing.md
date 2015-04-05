# Contributing to Castor

## Introduction

The Castor project is an open source project hosted at [github](github.com/castor-data-binding)
and it is released under a very
open license. This means there are many ways to contribute to the
project by e.g. coding, documenting, answering questions on the mailing lists,
proposing ideas, reporting bugs, suggesting bug-fixes, and so on.

To begin with, we suggest you to subscribe to the [mailing lists](mailing-lists.html)
We recommend that you listen for a while to hear how others make contributions.

You can get your local working copy of the current code base (of any particular
release) from the [git repository](http://github.com/castor-data-binding). Review the list
of unassigned issues in [TBD Jira](http://jira.codehaus.org/browse/CASTOR)
and choose a task -- or perhaps you have noticed something that needs patching.
(Before you start changing any code, please create a Jira task for your changes
if one does not already exist.)  Make your changes, test those changes, submit a pull request.

Document writers are usually the rarest and most wanted people, so if you would
like to help but you're not familiar with the innermost technical details, don't
worry.  Other committers will spend time with you and will assist you with their
knowledge. We're a friendly bunch and we appreciate anyone who is willing to
commit their most valuable resource -- time -- to help out..

## Useful links

* [How to run CTF test suite](../how-to/xml/how-to-run-xml-ctf-suite.html)
* [How to submit an XML-specific bug report](../how-to/xml/how-to-submit-an-xml-bug.html)

## Guidelines for code contributions

All code contributions must be under the license and copyright of the
project to which you contribute. By contributing code you agree that we
can distribute the contribution under the terms of the license, that it
can be distributed without any royalties, copyright, trademark, patent or
other legal impediments. Open source means no discrimination against any
group or individual that may wish to use the code.

When making a contribution you are granting us a world wide, royalty free,
unlimited-in-time license to re-distribute the code under the Exolab and/or
Apache license. In case you wonder, you remain the original author and
copyright holder of the contribution, you just give other people a license
to use it as well, including the creation of derivative works (as long
as the derivative works comply with all license terms, of course). Thank you.

It's perfectly ok to put your name and e-mail address in the code.

It is very important that you include test cases along with a
new feature or bug fix.  A test case will help the committers assess the
validity of the problem in question as well as your proposed solution.
In general, a test case -- showing a feature being added or bug being
fixed -- proves that the patch plays along nicely with other code and does
not introduce any side effects.

Committers are encouraged to commit a patch only if (s)he fully
understands the patch. A test case that assists the committer in full
understanding -- as mandated per the above statement -- ensures that
the committer understands what the patch does.  As a result, the test
case encourages a prompt review and check-in.

A test case is also the easiest and most efficient way to ensure your
contribution will not be broken by a future patch. It becomes even more
important if your project that makes use of Castor depends on a feature
or bug fix that you're contributing.

The last requirement for contributing code is to create a ticket at 
[TBD Jira](http://jira.codehaus.org/browse/CASTOR) and to attach
your test cases to this ticket.  A ticket 
allows your request to be fully traceable, as well as describing and
documenting your request.

## Guidelines For committers

*Familiarize yourself*: Take some time to understand the directory
structure, build environment, interaction between components, coding and
commenting style. While nothing out of the ordinary, not all projects are
identical. Code maintenance is substantially easier when all code in a
project uses a similar style.

*Advertise before you start*: It's pointless to have two people
working on the same feature. Send an e-mail to the developer mailing
list and announce the what and how of your intentions. If you don't get a
reply within a day then you can assume the coast is clear.

*Test before you commit*: Before committing any changes run the
test cases (ideally both XML and JDO) to make sure nothing is broken.

*Commit all at once*: If the change involves more than a single file,
make sure you commit all the changes together. A partially committed feature 
is not a pretty sight.

*Be ready to receive complaints*: Hopefully everything works fine, but
if changes break existing code, people will complain. Be ready to answer
their e-mails and apply the proper fixes. No going on vacation five minutes
after a commit!

*Put your name so people know who to credit*: (Also who to blame).
Initials work just fine, your full name and e-mail address will already be
on the main page.  If you've added a new file, feel free to put your name
and e-mail address as the author and put your name as the copyright owner.
If you're fixing a file, put your initials on the comment.  Remember that
JavaDoc only allows an @author tag on a class and not on a method or field.

*Observe release time*: We're going to announce a new release at least
five hours prior to making it (often 48 hours prior). That gives you
sufficient time to commit your changes in time for the release, making sure
that nothing breaks. If you can't make it, there's always the next release.

*Document what you've done*: In-code documentation, git commit messages,
and the tickets. Major changes should always be recorded in the changelog 
in addition.

## Licensing policy

We have a simple policy regarding distributable code (referring, for
example, to third-party JAR files that are to be included as part of the
Castor source distribution). Distributable code must either be open source
and compatible in license, or an API that is freely distributable.

BSD-like and MPL-like licenses are compatible and can be mixed in the
same code base. Liberal licenses and public domain are also fine.

APIs need not be open source, but they must be freely distributable.
As a policy we like to stick with standard APIs and never modify them
to break compatibility with the API, so the license has little affect.
We do favor public domains APIs like SAX over tightly controlled APIs,
and hopefully we can all do something about that.

Pay special attention to pre-release availability and trademark issues
before including code with Castor.  Several committees and companies
require proper trademark acknowledgement in documentation. Some packages
or APIs are available for distribution only once they have been formally
released. For example, this policy applies to all APIs coming from Sun.

## Building Castor

Castor relies on [Maven](http://maven.apache.org) as build management system. Please refer to the following sections as well 
to get an idea about the project layout, the individual modules and the structure within a module. 
        
Support for Ant as build tool has been dropped completely. Please note, though, that the code base still includes Ant build files. Please
do not use them! Those Ant files are in the process of being removed, which will take us some time.
        
### Project layout
        
Castor follows a standard Maven multi-module project structure.
            
Currently, the following main modules are available:

| Name | Description | Since
|------|-------------|------
| anttask | Ant task definitions for Castor tools | 1.0.5
| core | Castor core classes | 1.2.1
| cpa | Castor JDO - persistence framework | 1.1
| xml | XML data binding framework | 1.2.1
| codegen | XML code generator | 1.1.1
| schema | XML schema support (classes) | 1.2
| ddlgen | Castor JDO DDL generator | 1.1
| examples | Examples for all functional areas | 1.0.5
            
In addition, there's various modules available that represent integration and performance test suites for the main modules:

| Name | Description | Since
|------|-------------|------
| xmlctf | Integration test suite for Castor XML | 1.2
| cpactf | Integration test suite for Castor JDO | 1.2
| cpaptf | Performance test suite for Castor JDO | 1.2
| xmlctf-framework | Framework code for XMLCTF | 1.2
| maven-plugins| Maven plugins supporting execution from XMLCTF within Maven | 1.2

        
Finally, there's a module to provide Maven archetypes for Castor:

| Name | Description | Since
|------|-------------|------
| codegen-testcase-archetype | Maven archetype for XML code generator | 1.2
| cpa-testcase-archetype | Maven archetype for XML code generator | 1.2


Last but not least, there's a module to provide the project documentation for Castor:

| Name | Description | Since
|------|-------------|------
| doc | Project documentation (reference guide) | 1.3rc1

## Directory Structures (globally)

| Directory | Description of its content
|------|-------------
| bin | Script/batch files required for building Castor, running samples, CTF et alias
| src/doc | Documentation (in XML form)
| src/schema | DTD and XML schema for Castor mapping files
| src/examples | Examples for the various packages
| src/etc | Additional files that are not part of the code base
| lib | JARs required to build Castor and distributable JARs


## Directory Structure within a module

Castor follows the Maven standards for folder hierarchies.


### Builing code

To build Castor using Maven - assuming that you have got Maven locally installed - please issue the standard Maven commands.

Please note the following Castor-specific additions:
                
```                
> mvn (clean) verify
```                

will include all the integration tests as defined in the xmlctf module.

By default, JUnit summary test output is excluded from both unit and integration tests. To have this output (re)enabled, please
use the following Maven goals:

```                
> mvn clean verify -Dsurefire.print.summary=true
> mvn clean test -Dsurefire.print.summary=true
```                
It is also possible to execute just a subset of the master test suite, simply by specifying the `castor.xmlctf.root`property. For example:

```                
> mvn clean verify -Dcastor.root.test=MasterTestSuite/mapping
```                

The above command will effect in running only the subset of tests located under the `MasterTestSuite/mapping` directory.
                
For more information about how to execute the CTF test suite and its internals, please visit (development/test-framework.html).
                
### Several Castor checkouts parallel in Eclipse
      
In case you need to check out Castor more than once - as you may be working on several Jira issues at the same time -
you'll come across a subtle problem related to how multi-module Maven projects are being integrated with Eclipse.
        
By definition, when executing `mvn eclipse:eclipse` to generate the Eclipse project files (`.project` and
`.classpath`, amongst others) for all the modules, Maven will use the artifact names of the module as Eclipse project name - which 
is fine as long as you are working with one checkout only at a time. When wanting to use several checkouts in parallel, this 
will cause name collisions.
        
In order to avoid this, please execute `mvn eclipse:eclipse` as follows, passing an arbitrary string to the eclipse plugin
to append to the artifact name when deducing the project name.
        
```
mvn -Declipse.projectNameTemplate=[artifactId]-2612
```

In above sample, a Jira issue number has been used to uniquely identify the individual modules in Eclipse.
         
### Castor and Eclipse
    
In order to develop Castor sources with Eclipse, you will have to perform the following steps so that you are fully enabled to code with Eclipse. 
        
1. `mvn compile` 
2. `mvn eclipse:eclipse`
3. Import all Castor modules into your Eclipse workspace.

## References

* [The testing framework](development/test-framework.html)
