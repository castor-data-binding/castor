<?xml version="1.0" encoding="ISO-8859-1"?>

<!-- FAQ Stylesheet                                 -->
<!-- Ismael Ghalimi ghalimi@exoffice.com            -->
<!-- Copyright (c) Exoffice Technologies, Inc. 1999 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/XSL/Transform/1.0">

  <xsl:template match="faq">
    <ul>
      <xsl:for-each select="item">
        <li><a href="#{@keyword}"><xsl:copy-of select="question"/></a></li>
      </xsl:for-each>
    </ul>
    <faq-body>
      <xsl:copy-of select="*"/>
    </faq-body>
  </xsl:template>

  <xsl:template match="faq-body/item">
    <h1>
      <a name="#{@keyword}"><xsl:apply-templates select="question"/></a>
    </h1>
    <div>
      <xsl:apply-templates select="answer"/>
    </div>
  </xsl:template>

</xsl:stylesheet>