@echo off
REM $Id$
REM Ant infrastructure
set CP=lib\ant-1.6.jar;lib\ant-1.6-launcher.jar;build\classes
REM Sun's JDK tools
set CP=%CP;"%JAVA_HOME%\lib\classes.zip"
REM Xerces 1.4, disable for JDK 1.4 and above
set CP=%CP%;"..\lib\xerces-J_1.4.0.jar"
"%JAVA_HOME%\bin\java-nojit" -mx128M -classpath %CP% -Dant.home=lib org.apache.tools.ant.Main -Dexcludes="**/package.html,org/exolab/castor/mapping/loader/J2CollectionHandlers.java,org/exolab/castor/util/IteratorEnumeration.java,org/exolab/castor/xml/dtd/**,org/exolab/castor/jdo/**,org/exolab/castor/gui/**,org/exolab/castor/tools/**,org/exolab/castor/dax/**,org/exolab/castor/persist/**,org/exolab/castor/dtx/**" -Dexampleexcludes="SourceGenerator/*,dax/*,**/jdo/**,**/dtx/**,**/oes/**,**/servlet/**,**/myapp/**" -Dtestexcludes="**/package.html,**/jdo/**,**/introspection/**,**/tests/framework/**" -Dapipackagenames="org.exolab.castor.xml,org.exolab.castor.dsml,org.exolab.castor.mapping" -Dallpackagenames="org.exolab.castor.xml.*,org.exolab.castor.dsml.*,org.exolab.castor.tools.*,org.exolab.castor.types.*,org.exolab.castor.util.*,org.exolab.castor.builder.*,org.exolab.castor.mapping.*" %1 %2 %3 %4 %5 %6 -buildfile ..\src\build.xml
rem ,**/xml/xml2java/**,
