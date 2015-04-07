# Performing a releaee

## Preparing to Release

* Make sure the site documentation is up to date and follows the Development Guidelines.
* All open JIRA issues should be closed or agreed to be scheduled for a future version.
* Verify that the project doesn't depend on SNAPSHOTs anymore (the maven-release-plugin will check this too)
* Check that the POM properly declares its dependencies, i.e. run mvn dependency:analyze and fix any problems.
* Be sure you have generated a gpg-key before performing the release. 