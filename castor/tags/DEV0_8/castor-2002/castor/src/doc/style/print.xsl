<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- Internal subset is broken, so resorting to this for a while -->
  <xsl:variable name="nbsp" select="'&#xA0;'"/>


  <!-- Include document for document transformation, widgets for some
       simple HTML styles (e-mail, url, etc
    -->
  <xsl:include href="document.xsl"/>

  <!-- Match the entire document and process it into a Web page.
       The document properties and body are processed separately
       in a uniform way. The background is provided by this style
   -->
  <xsl:template match="/">
    <xsl:variable name="project" select="document('../project.xml')/project"/>

    <html><head>
      <meta name="author" content="{document/properties/author/.}"/>
      <link rel="stylesheet" type="text/css" href="style/default.css"/>
      <xsl:if test="/document/properties/title">
        <title><xsl:value-of select="/document/properties/title"/></title>
      </xsl:if>
      <xsl:if test="/document/body/title">
        <title><xsl:value-of select="/document/body/title"/></title>
      </xsl:if>
    </head>

    <body>
      <xsl:if test="document/properties">
        <xsl:apply-templates select="document/properties"/>
        <br/>
      </xsl:if>

      <xsl:apply-templates select="document/body"/>
      <br/>

      <xsl:for-each select="$project/notice">
        <small><xsl:copy-of select="."/></small><br/>
      </xsl:for-each><br/>
    </body>
    </html>
  </xsl:template>

</xsl:stylesheet>


