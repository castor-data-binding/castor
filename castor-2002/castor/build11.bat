@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set cp=
for %%i in (lib\*.jar) do call cp.bat %%i
set CP=lib\ant_1.2_lib.jar;build\classes;%JAVA_HOME%\lib\classes.zip;%CP%
%JAVA% -nojit -mx128M -classpath %CP% -Dant.home=lib org.apache.tools.ant.Main -Dexcludes="**/package.html,org/exolab/castor/mapping/loader/J2CollectionHandlers.java,org/exolab/castor/util/IteratorEnumeration.java,org/exolab/castor/xml/dtd/**,org/exolab/castor/jdo/**,org/exolab/castor/gui/**,org/exolab/castor/tools/**,org/exolab/castor/dax/**,org/exolab/castor/persist/**,org/exolab/castor/dtx/**" -Dexampleexcludes="SourceGenerator/*,dax/*,**/jdo/**,**/dtx/**,**/oes/**,**/servlet/**,**/myapp/**" -Dtestexcludes="**/package.html,**/jdo/**,**/introspection/**,**/tests/framework/**" -Dapipackagenames="org.exolab.castor.xml,org.exolab.castor.dsml,org.exolab.castor.mapping" -Dallpackagenames="org.exolab.castor.xml.*,org.exolab.castor.dsml.*,org.exolab.castor.tools.*,org.exolab.castor.types.*,org.exolab.castor.util.*,org.exolab.castor.builder.*,org.exolab.castor.mapping.*" %1 %2 %3 %4 %5 %6 -buildfile src\build.xml
rem ,**/xml/xml2java/**,
