<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:variable name="leftTopNavPos" select="5"/>
<xsl:template match="$project/topNav">
  <xsl:for-each select="navLink">
    <xsl:variable name="url" select="url"/>
    <xsl:choose>
      <xsl:when test="position()=$leftTopNavPos"><a href="{$url}"><span class="menuTopOn"><xsl:value-of 
          select="display"/></span></a>&#160;&#160;&#160;&#160;&#160;</xsl:when>
      <xsl:otherwise><a href="{$url}"><span class="menuTopOff"><xsl:value-of 
          select="display"/></span></a>&#160;&#160;&#160;&#160;&#160;</xsl:otherwise>
    </xsl:choose>
  </xsl:for-each>
</xsl:template>

</xsl:stylesheet>

