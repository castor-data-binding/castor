
set JDK_BIN=c:\jdk1.3\bin
@echo Castor Test Cases
@echo.
@echo Generating classes...

@rem Java 2 style collection types
@rem java org.exolab.castor.builder.SourceGenerator -i invoice.xsd -f -types j2 -package test
@rem Java 1.1 collection types
@echo [Pattern Facet Test]
java org.exolab.castor.builder.SourceGenerator -i invoice.xsd -f -package test


@echo.
@echo About to compile generated source code...
@pause

%JDK_BIN%\javac test\*.java

@echo.
@echo Compiling test class...
@pause
%JDK_BIN%\javac *.java

@echo.
@echo Ready to run test...
@pause
%JDK_BIN%\java InvoiceTest
