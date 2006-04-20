<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


  <xsl:template match="/">
    <html><head>
      <meta name="author" content="Castor XML"/>
      <title>Product list</title>
    </head>
    <body bgcolor="#FFFFFF" text="#000000">

      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td bgcolor="#666699" align="left" width="254">
             <a href="http://www.exolab.org/">
                <img src="../exolab.gif" height="117" width="254" border="0"/>
             </a>
          </td>
          <td bgcolor="#ffffff" align="left" height="11" width="3600"></td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td bgcolor="#666699" align="left" width="100">&#xA0;</td>
          <td bgcolor="#ffffff" align="left" height="11" width="3600">
            <blockquote><br/>
              <xsl:apply-templates select="products"/>
            </blockquote>
          </td>
        </tr>
      </table>
    </body></html>
  </xsl:template>


  <xsl:template match="products">
    <table cellpadding="4" cellspacing="2" border="1" bordercolor="#666699">
      <tr>
        <td><b>ID</b></td>
        <td><b>Name</b></td>
        <td><b>Price</b></td>
        <td><b>Group</b></td>
        <td><b>Details</b></td>
      </tr>
      <xsl:apply-templates select="product"/>
    </table>
  </xsl:template>


  <xsl:template match="product">
    <tr>
      <td><xsl:value-of select="@id"/></td>
      <td><xsl:value-of select="@name"/></td>
      <td><xsl:value-of select="@price"/></td>
      <td><xsl:value-of select="group/@name"/></td>
      <td><small><xsl:apply-templates select="detail"/></small></td>
    </tr>
  </xsl:template>


  <xsl:template match="detail">
    <xsl:value-of select="@name"/><br/>
  </xsl:template>


</xsl:stylesheet>
