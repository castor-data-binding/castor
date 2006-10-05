
@echo off
REM Change the following line to set your JDK path
set JAVA_HOME=%JAVA_HOME%
set JAVA=%JAVA_HOME%\bin\java
set JAVAC=%JAVA_HOME%\bin\javac

@echo Create the classpath
set CP=.
for %%i in (..\..\..\lib\*.jar) do call cp.bat %%i
set CP=%CP%;..\..\..\build\classes;%JDK_BIN%\lib\tools.jar

@echo.
@echo Using classpath: %CP%

@echo Castor Test Cases
@echo.
@echo Generating classes...

@rem Java 2 style collection types
@rem %JAVA% org.exolab.castor.builder.SourceGeneratorMain -i invoice.xsd -f -types j2 -binding-file bindingInvoice.xml
@rem Java 1.1 collection types
%JAVA% -cp %CP% org.exolab.castor.builder.SourceGeneratorMain -i invoice.xsd -f -binding-file bindingInvoice.xml


@echo.
@echo About to compile generated source code...
@pause

%JAVAC% -classpath %CP% test\business\*.java test\*.java

@echo.
@echo Compiling test class...
@pause

%JAVAC% -classpath %CP%;. *.java

@echo.
@echo Ready to run test...
@pause
%JAVA% -cp %CP% InvoiceTest

@echo.
@echo Clean the directory
@pause
del *.class
deltree test
