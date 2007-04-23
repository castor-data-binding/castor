
A simple example using the Source Generator


files: 
  invoice.xsd        -  The example XML Schema file  
  invoice1.xml       -  A sample XML instance document of invoice.xsd
  InvoiceTest.java   -  A test class
  readme.txt         -  This file
  test.bat           -  A test batch file

To run the test, please update test.bat with your proper Java settings, and then
type test at the command prompt.

The batch file will:

  1. Call the source generator using invoice.xsd.
     This will create source in a package called "test". You will notice
     a new subdirectory called test.
  2. Compile the generated source 
  3. Compile as Test.java file.
  3. Run InvoiceTest.java


You will be prompted to continue at each step.

The example unmarshals "invoice1.xml" and displays the shipping information.
