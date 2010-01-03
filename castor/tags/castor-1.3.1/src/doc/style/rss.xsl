<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="rss">
    <xsl:element name="link">
      <xsl:attribute name="rel">alternate</xsl:attribute>
      <xsl:attribute name="type">application/rss+xml</xsl:attribute>
      <xsl:attribute name="title">Castor RSS Feed</xsl:attribute>
      <xsl:attribute name="href">
          <xsl:value-of select="."/>
      </xsl:attribute>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
