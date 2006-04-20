<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0">

<xsl:template match="$project/navSections">
  <xsl:for-each select="section">
  <xsl:variable name="sectionName" select="@name"/>
  <table width="95" border="0" cellpadding="0" cellspacing="0">
  <xsl:for-each select="navLink">
    <xsl:variable name="url">
      <xsl:call-template name="link-convertor">
        <xsl:with-param name="href" select="url"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="position()=$leftBottomNavPos">
        <tr>
          <td width="95" valign="top" align="left"><a href="{$url}"><span class="subMenuOff"><xsl:value-of select="display"/></span></a></td>
        </tr>
      </xsl:when>
      <xsl:otherwise>
        <tr>
          <td width="95" valign="top" align="left"><a href="{$url}"><span class="subMenuOff"><xsl:value-of select="display"/></span></a></td>
        </tr>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:for-each>
  </table>
  <br/>
  </xsl:for-each>
</xsl:template>

</xsl:stylesheet>




