<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="$project/navSections">
    <xsl:for-each select="section">
      <table border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td valign="top" align="left">
            <xsl:choose>
              <xsl:when test="@url">
                <xsl:variable name="url">
                  <xsl:call-template name="link-convertor">
                    <xsl:with-param name="href" select="@url"/>
                    </xsl:call-template>
                  </xsl:variable>
                  <a href="{$url}"><span class="subMenuOn"><xsl:value-of select="@name"/></span></a>
                </xsl:when>
                <xsl:otherwise>
                  <span class="subMenuOn"><xsl:value-of select="@name"/></span>
                </xsl:otherwise>
              </xsl:choose>
          </td>
        </tr>
        <xsl:for-each select="navLink">
          <xsl:variable name="url">
            <xsl:call-template name="link-convertor">
              <xsl:with-param name="href" select="url"/>
              </xsl:call-template>
            </xsl:variable>
            <tr>
              <td valign="top" align="left">
		&#160;&#160;
                <a href="{$url}"><span class="subMenuOff"><xsl:value-of select="display"/></span></a>
              </td>
            </tr>
          </xsl:for-each>
      </table>
      <br/>
    </xsl:for-each>
  </xsl:template>
  
</xsl:stylesheet>




