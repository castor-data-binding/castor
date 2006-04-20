<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="searchForm">
  <table width="95" border="0" cellpadding="0" cellspacing="0">
    <form method="POST" action="index.html">
      <xsl:element name="input">
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="name">dir</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="/page/dir"/></xsl:attribute>
      </xsl:element>
      <tr>
        <td><img src="images/dotTrans.gif" width="20" height="1" border="0"/></td>
        <td><img src="images/dotTrans.gif" width="75" height="1" border="0"/></td>
      </tr>

      <tr>
        <td colspan="2" align="center"><span class="bodyGrey"><input type="text" size="8" name="query"/></span></td>
      </tr>
      <tr>
        <td><img src="images/dotTrans.gif" width="20" height="1" border="0"/></td>
        <td><span class="subMenuOff">Search</span></td>
      </tr>
    </form>
  </table>
</xsl:template>

</xsl:stylesheet>

