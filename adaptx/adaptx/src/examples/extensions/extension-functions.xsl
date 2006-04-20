<?xml version="1.0"?>
<xsl:stylesheet
     xmlns:xsl="http://www.w3.org/1999/xsl/Transform" version="1.0">

<!-- for read-ability use HTML output and enable indenting -->
<xsl:output method="html" indent="yes"/>

<!-- matches document node -->
<xsl:template match="/">
   <HTML>
      <HEAD>
         <TITLE>Extension Function Example</TITLE>
      </HEAD>
      <BODY xmlns:ext="http://www.adaptx.org/functions/Samples">

      This is a simple example which shows how to use extension functions.
      I've created an extension function (see
      <a href="SampleFunctionResolver.java">SampleFunctionResolver.java</a>)
      which contains the implementation for a function called "result-type".
      This function returns, as a String, the type of the parameter which is
      passed as the argument to the function.
      <P>
      <B>Tips on usage:</B>
      <UL>
         <LI>To use extension functions you make sure you declare
             the namespace of the extension functions</LI>
         <LI>Make sure you add your FunctionResolver to the XSLTProcessor</LI>
      </UL>
      </P>

      <HR/>
      <B>Results:</B><P/>

      <xsl:variable name="test1" select="'any old string'"/>
      The result type of test1 is:
      <xsl:value-of select="ext:result-type($test1)"/>
      <P/>

      <xsl:variable name="test2" select="false()"/>
      The result type of test2 is:
      <xsl:value-of select="ext:result-type($test2)"/>
      <P/>

      <xsl:variable name="test3" select="1+1"/>
      The result type of test3 is:
      <xsl:value-of select="ext:result-type($test3)"/>
      <P/>

      </BODY>
   </HTML>
</xsl:template>

</xsl:stylesheet>
