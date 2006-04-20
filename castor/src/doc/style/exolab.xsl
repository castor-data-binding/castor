<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html" indent="no"/>

  <xsl:include href="topNav.xsl"/>
  <xsl:include href="leftNav.xsl"/>
  <xsl:include href="keywords.xsl"/>
  <xsl:include href="searchForm.xsl"/>


  <!-- Template for document -->

  <xsl:template match="/">
  <xsl:variable name="project" select="document('../project.xml')/project"/>
  <html>

  <head>
    <xsl:apply-templates select="keywords"/>
    <xsl:choose>
      <xsl:when test="/document/properties/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
      <xsl:when test="/document/body/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
      <xsl:otherwise><title><xsl:value-of select="$project/title"/></title></xsl:otherwise>
    </xsl:choose>
    <link rel="stylesheet" href="default.css"/>
  </head>

  <body bgcolor="#ffffff" link="#6763a9" vlink="#6763a9"
        topmargin="0" bottommargin="0" leftmargin="0" marginheight="0" marginwidth="0">

  <a name="top"/>

    <table border="0" cellpadding="0" cellspacing="0" height="400">

      <tr><td width="10" valign="top" align="left" bgcolor="#7270c2"><img
        src="images/dotTrans.gif" width="1" height="1" border="0"/></td>
        <td width="125" valign="top" align="left" bgcolor="#7270c2"><img
          src="images/dotTrans.gif" width="1" height="1" border="0"/></td>
        <td width="7" valign="top" align="left"><img src="images/dotTrans.gif" border="0"
          width="1" height="1"/></td>
        <td width="70" valign="top" align="left"><img
          src="images/dotTrans.gif" width="70" height="6" border="0"/></td>
        <td width="400" valign="top" align="left"><img
          src="images/top_2.gif"  width="400" height="6" border="0"/></td>
    <td width="120" valign="top" align="left"><xsl:element
          name="img"><xsl:attribute name="src">images/line_purple.gif</xsl:attribute>
        <xsl:attribute name="width">120</xsl:attribute>
        <xsl:attribute name="height">6</xsl:attribute>
        <xsl:attribute name="border">0</xsl:attribute>
      </xsl:element></td></tr>
      <tr>
        <td width="10" bgcolor="#7270c2" valign="top" align="left"><img
          src="images/dotTrans.gif" border="0" width="1" height="1"/></td>
        <td width="125" bgcolor="#7270c2" valign="top" align="left"><img
          src="images/dotTrans.gif" border="0" width="1" height="1"/></td>
        <td width="7" bgcolor="#ffffff" valign="top" align="left"></td>
        <td width="70" valign="top" align="left"><img
          src="images/dotTrans.gif" width="1" height="1" border="0"/></td>
        <td width="400" valign="middle" align="left">
        <xsl:apply-templates select="$project/topNav"/><br/>
        <img src="images/dotTrans.gif" width="1" height="2" border="0"/></td>
        <td width="120" height="20" valign="top" align="left">&#160;</td>
      </tr>
      <tr>
        <td width="10" bgcolor="#7270c2" valign="top" align="left"><img
          src="images/dotTrans.gif" width="10" height="3" border="0"/></td>
        <td width="125" bgcolor="#7270c2" valign="top" align="right"><img
          src="images/line_sm.gif" width="105" height="3" border="0"/></td>
        <td width="7" bgcolor="#a9a5de" valign="top" align="left"><img
          src="images/line_sm.gif" width="7" height="3" border="0"/></td>
        <td width="70" valign="top" align="left"><img
          src="images/line_light.gif" width="70" height="3" border="0"/></td>
        <td width="400" valign="top" align="left"><img
          src="images/line_light.gif" width="400" height="3" border="0"/></td>
        <td width="120" valign="top" align="left"><img
          src="images/dotTrans.gif" border="0" width="1" height="1"/></td>
      </tr>

      <tr>
        <td bgcolor="#7270c2" valign="top" align="left"><img
          src="images/dotTrans.gif" width="10" height="10" border="0"/></td>

        <td width="125" bgcolor="#7270c2" valign="top" align="left"><img src="images/dotTrans.gif" width="1" height="2" border="0"/><br/>
<xsl:apply-templates select="$project/navSections"/></td>

        <td width="7" bgcolor="#a9a5de" valign="top" align="left">&#160;</td>
        <td width="70" valign="top" align="left">&#160;</td>
        <td rowspan="4" width="400" valign="top">
          <table cols="2" rows="2" border="0" cellpadding="0" cellspacing="0" width="400">
            <tr>
              <td valign="top" align="left"><br/><img border="0" height="34" hspace="0"
                  src="{$project/logo}" vspace="0" width="115"/><br/><img border="0" height="10" hspace="0"
                  src="images/dotTrans.gif"/>
              </td>
          <td width="120" height="5" valign="top"
        align="right"><a href="http://www.exolab.org"><img
        src="images/logo_exolab.gif" hspace="0" vspace="10" width="77" height="20" border="0"/></a></td>
            </tr>
          </table><p/><p/><br/>

          <xsl:if test="/document/properties/title">
            <span class="header"><xsl:value-of select="/document/properties/title"/></span><br/><br/>
          </xsl:if>

          <xsl:apply-templates select="document/body/header"/>

          <!-- build the page navigation first, section by section -->
          <xsl:for-each select=".//section">
          <span class="bodyGrey">
            <xsl:if test="@title">
              <xsl:variable name="level" select="count(ancestor::*)"/>
              <xsl:choose>
                <xsl:when test='$level=2'>
                  <b><a href="#{translate(@title,' ','-')}"><xsl:value-of select="@title"/></a></b><br/>
                </xsl:when>
                <xsl:when test='$level=3'>
                  <a href="#{translate(@title,' ','-')}">&#xA0;&#xA0;&#xA0;&#xA0;<xsl:value-of select="@title"/></a><br/>
                </xsl:when>
                <xsl:otherwise>
                  &#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;<a href="#{translate(@title,' ','-')}"><xsl:value-of select="@title"/></a><br/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:if>
          </span>
        </xsl:for-each>

        <br/>

        <!-- now show the sections themselves -->
        <xsl:apply-templates select="document/body/section"/>
      </td>
      </tr>

      <!-- line row -->

      <tr height="5">
        <td width="10" height="5" bgcolor="#7270c2" valign="top" align="left">&#160;</td>
        <td width="125" height="5" bgcolor="#7270c2" valign="top">
        <img src="images/dotTrans.gif" width="1" height="15" border="0"/><br/>
        <img src="images/line_sm.gif" width="105" height="3" border="0" align="right"/>


        </td>
        <td width="7" height="5" bgcolor="#a9a5de" valign="top" align="left">&#160;</td>
        <td width="70" height="5" valign="top" align="left">&#160;</td>
        <td width="120" height="5" valign="top" align="left">&#160;</td>
      </tr>

      <!-- content row -->

      <tr>
        <td width="10" height="5" bgcolor="#7270c2" valign="top" align="left">&#160;</td>
        <td width="125" bgcolor="#7270c2" valign="top"
          align="left"><xsl:apply-templates select="searchForm"/></td>
        <td width="7" bgcolor="#a9a5de" valign="top" align="left">
          <img src="images/dotTrans.gif" width="1" height="25" border="0"/>
        </td>
        <td width="70" valign="top" align="left">
          <img src="images/dotTrans.gif" width="1" height="25" border="0"/>
        </td>
        <td width="120" valign="top" align="left">&#160;</td>
      </tr>

      <!-- final row -->

      <tr height="5">
        <td width="10" rowspan="2" height="100%" bgcolor="#7270c2" valign="bottom"
          align="left"><img src="images/stripes1.gif" width="10" height="125" border="0"/></td>
        <td width="125" rowspan="2" height="100%" bgcolor="#7270c2" valign="bottom"
          align="left"><img src="images/stripe105.gif" width="105" height="125" border="0"/></td>
        <td width="7" rowspan="2" height="100%" bgcolor="#a9a5de" valign="top"
          align="left">&#160;</td>
        <td width="70" height="100%" valign="top" align="left">&#160;</td>
        <td width="120" height="100%" valign="top" align="left">&#160;</td>
      </tr>

      <!-- extra  row -->

      <tr height="5">
        <!--td width="10" height="25" valign="top" align="left">&#160;</td>
        <td width="125" height="25" valign="top" align="left">&#160;</td>
        <td width="7" height="25" valign="top" align="left">&#160;</td-->
        <td width="70" height="25" valign="top" align="left">&#160;</td>
        <td width="400" height="25" valign="bottom" align="left">
          <br/><br/>
          <img src="images/line_light.gif"  border="0" width="400" height="3"  /><br/>
          <p/>
          <span class="bodyGrey">
            <xsl:for-each select="$project/notice">
              <small><xsl:copy-of select="."/><br/>&#xA0;<br/></small>
            </xsl:for-each>
          </span>
          <p/>
          &#160;
        </td>
        <td width="120" height="25" valign="top" align="left">&#160;</td>
      </tr>

    </table>

  </body>

  </html>
  </xsl:template>


  <!-- Templates for sections and headers -->

  <xsl:template match="document//section">
    <xsl:variable name="level" select="count(ancestor::*)"/>
    <xsl:choose>
      <xsl:when test='$level=2'>
        <xsl:if test="@ref-id"><a name="{@ref-id}"/></xsl:if>
        <a name="{translate(@title,' ','-')}"><h2><xsl:value-of select="@title"/></h2></a>
      </xsl:when>
      <xsl:when test='$level=3'>
        <xsl:if test="@ref-id"><a name="{@ref-id}"/></xsl:if>
        <a name="{translate(@title,' ','-')}"><h3><xsl:value-of select="@title"/></h3></a>
      </xsl:when>
      <xsl:when test='$level=4'>
        <xsl:if test="@ref-id"><a name="{@ref-id}"/></xsl:if>
        <a name="{translate(@title,' ','-')}"><h4><xsl:value-of select="@title"/></h4></a>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="@ref-id"><a name="{@ref-id}"/></xsl:if>
        <a name="{translate(@title,' ','-')}"><h5><xsl:value-of select="@title"/></h5></a>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="header">
    <xsl:apply-templates select="*"/>
  </xsl:template>


  <!-- Templates for HTML correction -->

  <xsl:template match="*|@*">
    <xsl:copy>
      <xsl:apply-templates select="*|@*|text()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="small">
    <span class="bodyGrey">
      <xsl:copy-of select="*|text()"/>
    </span>
  </xsl:template>

  <xsl:template match="p">
    <p>
      <span class="bodyGrey">
        <xsl:apply-templates select="*|@*|text()"/>
      </span>
    </p>
  </xsl:template>

  <xsl:template match="td">
    <td>
      <xsl:copy-of select="@*"/>
      <span class="bodyGrey">
      <xsl:apply-templates select="*|@*|text()"/>
      </span>
    </td>
  </xsl:template>

  <xsl:template match="ul">
    <table border="0" cellpadding="2" cellspacing="2">
      <tr><td colspan="2" height="5"></td></tr>
      <span class="bodyGrey"><xsl:apply-templates/></span>
    </table>
  </xsl:template>

  <xsl:template match="ul/li">
    <tr>
      <td align="left" valign="top" width="10">-</td>
      <td align="left" valign="top"><span class="bodyGrey"><xsl:apply-templates/></span></td>
    </tr>
  </xsl:template>

  <xsl:template match="pre">
    <span class="bodyGrey">
      <pre><xsl:apply-templates/></pre>
    </span>
  </xsl:template>


  <!-- Templates for links -->

  <xsl:template match="a">
    <a>
      <xsl:if test="@href">
        <xsl:variable name="href">
          <xsl:call-template name="link-convertor">
            <xsl:with-param name="href" select="@href"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:attribute name="href">
          <xsl:value-of select="$href"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:for-each select="@*[not(name(.)='href')]">
        <xsl:copy-of select="."/>
      </xsl:for-each>
      <xsl:apply-templates/>
    </a>
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

  <xsl:template name="image-grabber">
    <xsl:param name="image" select="empty" />
    <xsl:value-of select="$image" />
  </xsl:template>

  <xsl:template match="javadoc">
    <xsl:choose>
      <xsl:when test="@type='package'">
        <a href="javadoc/{translate(.,'.','/')}/package-summary.html"><xsl:copy-of select="."/></a>
      </xsl:when>
      <xsl:otherwise>
        <a href="javadoc/{translate(.,'.','/')}.html"><xsl:copy-of select="."/></a>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="api">
    <xsl:choose>
      <xsl:when test="@package">
        <a href="api/{translate(@package,'.','/')}/package-summary.html"><xsl:copy-of select="."/></a>
      </xsl:when>
      <xsl:when test="@class">
        <a href="api/{translate(@class,'.','/')}.html#{.}"><xsl:value-of select="."/></a>
      </xsl:when>
      <xsl:otherwise>
        <a href="api/{translate(.,'.','/')}.html"><xsl:copy-of select="."/></a>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="url">
    <a href="{.}"><xsl:copy-of select="."/></a>
  </xsl:template>

  <xsl:template match="email">
    <a href="mailto:{.}"><xsl:copy-of select="."/></a>
  </xsl:template>


  <!-- Templates for special content -->

  <xsl:template match="body-note">
    <hr size="1" noshadow=""/><span class="bodyGrey"><xsl:apply-templates/><hr size="1" noshadow=""/></span>
  </xsl:template>

  <xsl:template match="code">
    <span class="bodyBlack">
      <pre><xsl:apply-templates/></pre>
    </span>
  </xsl:template>

  <xsl:template match="code/comment">
    <span class="bodyGrey">
      <font color="red"><xsl:apply-templates/></font>
    </span>
  </xsl:template>

  <xsl:template match="include">
      <xsl:apply-templates select="document(./@href)/document/body/section"/>
  </xsl:template>

  <xsl:template match="mailing-lists">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="mailing-list">
    <div>
      [
      <a href="mailto:{@manager}@{@server}?subject=subscribe {@name}">Subscribe</a> |
      <a href="mailto:{@manager}@{@server}?subject=unsubscribe {@name}">Unsubscribe</a>
      <!-- | <a href="mailto:{@name}@{@server}">Post Message</a> -->
      <xsl:if test="@archive">
         | <a href="{@archive}">Archive</a>
    </xsl:if>
      ]
    </div>
  </xsl:template>

  <xsl:template match="mailing-list/title">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="mailing-list/description">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="contributors">
    <xsl:for-each select="type">
      <xsl:variable name="type" select="@name"/>
      <xsl:variable name="color-epsilon" select="'#ffffff'"/>
      <p><span class="bodyGrey"><b><xsl:value-of select="@name"/></b></span></p>
      <p><span class="bodyGrey"><xsl:value-of select="."/></span></p>
      <table cellpadding="4" cellspacing="2" width="90%">
        <tr>
          <td bgcolor="{$color-epsilon}"><span class="bodyGrey">
            <b>Name</b></span>
          </td>
          <td bgcolor="{$color-epsilon}"><span class="bodyGrey">
            <b>Contribution</b></span>
          </td>
          <td bgcolor="{$color-epsilon}"><span class="bodyGrey">
            <b>Company</b></span>
          </td>
        </tr>
        <xsl:for-each select="../contributor[@type=$type]">
          <tr>
            <td valign="top"><span class="bodyGrey">
              <!-- a href="mailto:{email}" -->
              <a>
                  <xsl:attribute name="href">mailto:<xsl:apply-templates select="email"/></xsl:attribute>
                  <xsl:value-of select="name/@given"/>&#xA0;<xsl:value-of select="name/@surname"/>
              </a></span>
            </td>
            <td><span class="bodyGrey">
               <xsl:value-of select="description"/></span>
            </td>
            <td><span class="bodyGrey">
               <xsl:variable name="company-id" select="company/@id"/>
               <xsl:variable name="company" select="../company[@id=$company-id]"/>
               <a href="http://{$company/url}"><xsl:value-of select="$company/name"/></a>
               &#xA0;</span>
            </td>
          </tr>
        </xsl:for-each>
      </table>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="contributor/email">
      <xsl:value-of select="substring-before(., '@')"/>@remove-no-spam.<xsl:value-of select="substring-after(., '@')"/>
  </xsl:template>

  <xsl:template match="news">
      <br />
    <!--xsl:variable name="border" />
    <xsl:variable name="fill" />
    <xsl:when test="release">
      <xsl:variable name="border">#9966cc</xsl:variable>
      <xsl:variable name="fill">#9999ff</xsl:variable>
    </xsl:when>
    <xsl:otherwise>
      <xsl:variable name="border">#a9a5de</xsl:variable>
      <xsl:variable name="fill">#ededed</xsl:variable>
    </xsl:otherwise-->
      <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#7270c2"><tr><td>
          <table width="100%" border="0" cellspacing="1" cellpadding="8" bgcolor="#ededed"><tr><td>
              <span class="newsTitle"><xsl:value-of select="title"/></span><br/><br/>
              <span class="newsSummary"><xsl:apply-templates select="summary"/><br/><br/>
                  <xsl:when test="url">
                    <div align="center"><a href="{url}">[Read More/Comment]</a></div><br/>
                  </xsl:when>
                  Submitted by <xsl:value-of select="author"/>, on <xsl:value-of select="date"/>
                  @ <xsl:value-of select="time"/>
              </span>
          </td></tr></table>
      </td></tr></table>
  </xsl:template>

</xsl:stylesheet>

