<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Content Stylesheet                             -->
<!-- Ismael Ghalimi ghalimi@exoffice.com            -->
<!-- Copyright (c) Exoffice Technologies, Inc. 1999 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


  <!-- Include document for document transformation, widgets for some
       simple HTML styles (e-mail, url, etc
    -->
  <xsl:include href="document.xsl"/>
  <xsl:include href="widgets.xsl"/>


  <xsl:output method="html" indent="no"/>


  <!-- Color -->
  <xsl:variable name="color-alpha">#ffffff</xsl:variable>
  <xsl:variable name="color-beta" select="'#666699'"/>
  <xsl:variable name="color-gamma" select="'#424264'"/>
  <xsl:variable name="color-delta" select="'#54547f'"/>
  <xsl:variable name="color-epsilon" select="'#bfbffe'"/>
  <xsl:variable name="color-a" select="'#444466'"/>
  <xsl:variable name="color-b" select="'#4d4d73'"/>
  <xsl:variable name="color-c" select="'#565680'"/>
  <xsl:variable name="color-d" select="'#5e5e8c'"/>
  <xsl:variable name="color-e" select="'#676799'"/>
  <xsl:variable name="color-f" select="'#6f6fa6'"/>
  <xsl:variable name="color-g" select="'#7878b3'"/>
  <xsl:variable name="color-h" select="'#8080bf'"/>
  <xsl:variable name="color-i" select="'#8989cc'"/>
  <xsl:variable name="color-j" select="'#9292d9'"/>
  <xsl:variable name="color-k" select="'#9a9ae6'"/>
  <xsl:variable name="color-l" select="'#a2a2f2'"/>
  <xsl:variable name="color-m" select="'#ababff'"/>

  <!-- Internal subset is broken, so resorting to this for a while -->
  <xsl:variable name="nbsp" select="'&#xA0;'"/>


  <!-- Match the entire document and process it into a Web page.
       The document properties and body are processed separately
       in a uniform way. The background is provided by this style
   -->
  <xsl:template match="/">
    <xsl:variable name="project" select="document('../project.xml')/project"/>

    <html><head>
      <meta name="author" content="{document/properties/author/.}"/>
      <link rel="stylesheet" type="text/css" href="style/default.css"/>
      <xsl:choose>
        <xsl:when test="/document/properties/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
        <xsl:when test="/document/body/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
        <xsl:otherwise><title><xsl:value-of select="$project/title"/></title></xsl:otherwise>
      </xsl:choose>
    </head>

    <body bgcolor="{$color-e}" link="#bfbffe" vlink="#bfbffe" alink="#bfbffe" leftmargin="0" topmargin="0">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">

        <tr>
          <td bgcolor="{$color-alpha}" colspan="3">

            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                  <td bgcolor="{$color-beta}" align="left" width="254" rowspan="2">
                    <a href="http://www.exolab.org/">
                      <img src="style/images/exolab.gif" height="117" width="254" border="0"/>
                    </a>
                  </td>
                  <td bgcolor="{$color-beta}" align="left" height="11" width="3600">
                    <xsl:apply-templates select="$project/links"/>
                  </td>
                </tr>
                <tr>
                  <td bgcolor="{$color-alpha}" width="*" align="left" valign="top">
                    <xsl:value-of select="$nbsp"/><xsl:value-of select="$nbsp"/>
                    <a href="{$project/@href}"><img src="{$project/@image}" border="0" vspace="8" hspace="16"/></a>
                  </td>
                </tr>

            </table>

          </td>
        </tr>

        <tr>
          <td bgcolor="{$color-beta}" width="120" rowspan="2" valign="top">
            <xsl:if test="document/properties">
              <xsl:attribute name="rowspan">4</xsl:attribute>
            </xsl:if>
            <table>
              <xsl:for-each select="$project/menu">
                <tr>
                  <td align="left" colspan="2">
                    <font color="{$color-alpha}"><small><xsl:value-of select="@name"/></small></font>
                  </td>
                </tr>
                <xsl:for-each select="item">
                  <tr>
                    <td width="15" valign="top" align="right">
                      <img src="style/images/bullets/square-small-white.gif" alt="*" height="12" width="12"/>
                    </td>
                    <td><span class="alpha">
                      <xsl:variable name="href">
                        <xsl:call-template name="link-convertor">
                          <xsl:with-param name="href" select="@href"/>
                        </xsl:call-template>
                      </xsl:variable>
                      <font size="2"><a href="{$href}"><xsl:value-of select="@name"/></a></font>
                    </span></td>
                  </tr>
                </xsl:for-each>
              </xsl:for-each>
            </table>
          </td>
          <td bgcolor="{$color-alpha}" width="11" valign="top">
              <img src="style/images/corners/nw-small.gif" height="11" width="11" valign="top"/>
          </td>
          <td bgcolor="{$color-alpha}" height="45" width="3600">
            &#xA0;&#xA0;<img src="style/images/bullets/dots.gif" height="11" width="41" valign="top"/>&#xA0;&#xA0;
            <font size="4" color="{$color-c}"><b>
              <xsl:choose>
                <xsl:when test="/document/body/title"><xsl:value-of select="/document/body/title"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="$project/title"/></xsl:otherwise>
              </xsl:choose>
            </b></font>
           </td>
        </tr>

      <xsl:if test="document/properties">
        <tr>
          <td bgcolor="{$color-alpha}" width="11"><xsl:value-of select="$nbsp"/></td>
          <td bgcolor="{$color-alpha}" valign="top">
             <xsl:apply-templates select="document/properties"/>
          </td>
        </tr>

        <tr>
          <td bgcolor="{$color-beta}" width="11"><xsl:value-of select="$nbsp"/></td>
          <td bgcolor="{$color-beta}" valign="top"><xsl:value-of select="$nbsp"/></td>
        </tr>
      </xsl:if>

        <tr>
          <td bgcolor="{$color-alpha}" width="11">&#xA0;</td>
          <td bgcolor="{$color-alpha}" valign="top">
             <xsl:apply-templates select="document/body"/>
             <br/>
          </td>
        </tr>

        <tr>
          <td bgcolor="{$color-beta}" width="120"></td>
          <td bgcolor="{$color-beta}" height="11" width="11" valign="top">
            <img src="style/images/corners/sw-small.gif" height="11" width="11" valign="top"/>
          </td>
          <td bgcolor="{$color-alpha}" valign="top">
            <img src="style/images/blank.gif" height="11" width="11"/>
          </td>
        </tr>
        <tr>
          <td bgcolor="{$color-beta}" width="120"></td>
          <td bgcolor="{$color-beta}" width="11">
            <img src="style/images/blank.gif" height="11" width="11"/>
          </td>
          <td bgcolor="{$color-beta}" valign="top"></td>
        </tr>

        <xsl:if test="$project/notice">
          <tr>
            <td bgcolor="{$color-beta}" width="120"></td>
            <td bgcolor="{$color-beta}" width="11" valign="top"><img src="style/images/corners/nw-small.gif" height="11" width="11"/></td>
            <td bgcolor="{$color-alpha}" valign="top"><img src="style/images/blank.gif" height="11" width="11"/></td>
          </tr>
          <tr>
            <td bgcolor="{$color-beta}" width="120"></td>
            <td bgcolor="{$color-alpha}" width="11">&#xA0;</td>
            <td bgcolor="{$color-alpha}">
              <xsl:for-each select="$project/notice">
                <small><xsl:copy-of select="."/><br/>&#xA0;<br/></small>
              </xsl:for-each>
            </td>
          </tr>
          <tr>
            <td bgcolor="{$color-beta}" width="120"></td>
            <td bgcolor="{$color-beta}" width="11" valign="top"><img src="style/images/corners/sw-small.gif" height="11" width="11" valign="top"/></td>
            <td bgcolor="{$color-alpha}" valign="top"><img src="style/images/blank.gif" height="11" width="11"/></td>
          </tr>
        </xsl:if>

        <tr>
          <td bgcolor="{$color-beta}" width="120"></td>
          <td bgcolor="{$color-beta}" width="11" valign="top"></td>
          <td bgcolor="{$color-beta}" valign="top">
            <xsl:apply-templates select="$project/links"/>
          </td>
        </tr>

      </table>

    </body>
    </html>
  </xsl:template>

  <!-- Special handling for project-wide links which appear both at the
       top and bottom of the page
   -->
  <xsl:template match="project/links">
    <xsl:for-each select="link">
      <xsl:variable name="href">
        <xsl:call-template name="link-convertor">
          <xsl:with-param name="href" select="@href"/>
        </xsl:call-template>
      </xsl:variable>
      <a href="{$href}"><img src="{@image}" alt="{@name}" height="{@height}" width="{@width}" border="0" vspace="5"/></a>
    </xsl:for-each>
  </xsl:template>


  <xsl:template name="link-convertor">
    <xsl:param name="href" select="empty"/>
    <xsl:choose>
      <xsl:when test="starts-with($href,'http:')">
        <xsl:value-of select="$href"/>
      </xsl:when>
      <xsl:when test="not(contains($href,'.xml'))">
        <xsl:value-of select="$href"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="substring-before($href, '.xml')"/>.html
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- UL is processed into a table using graphical bullets -->
  <xsl:template match="ul">
    <table border="0" cellpadding="2" cellspacing="2">
      <tr><td colspan="2" height="5"></td></tr>
      <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="ul/li">
    <tr>
      <td align="left" valign="top" width="15">
        <img src="style/images/bullets/blue.gif" height="22" width="15" alt="*"/>
     </td>
      <td align="left" valign="top"><xsl:apply-templates/></td>
    </tr>
  </xsl:template>


</xsl:stylesheet>


