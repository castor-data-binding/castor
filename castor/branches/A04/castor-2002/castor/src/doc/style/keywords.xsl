<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0">
  <xsl:template match="keywords">
    <xsl:element name="meta">
      <xsl:attribute name="http-equiv">Keywords</xsl:attribute>
      <xsl:attribute name="content">
        <xsl:for-each select="keyword">
          <xsl:value-of select="."/>
            <xsl:choose>
              <xsl:when test="position() = last()"></xsl:when>
              <xsl:otherwise>, </xsl:otherwise>
            </xsl:choose>
          </xsl:for-each>
        </xsl:attribute>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
