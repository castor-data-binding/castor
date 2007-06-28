<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:variable name="second_col_width">150</xsl:variable>

  <xsl:output method="html" indent="no"/>

  <xsl:include href="topNav.xsl"/>
  <xsl:include href="leftNav.xsl"/>
  <xsl:include href="keywords.xsl"/>
  <xsl:include href="searchForm.xsl"/>
  <xsl:include href="rss.xsl"/>

  <!-- Template for document -->

  <xsl:template match="/">
  <xsl:variable name="project" select="document('../project.xml')/project"/>
  <html>

  <head>
    <xsl:choose>
      <xsl:when test="/document/properties/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
      <xsl:when test="/document/body/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
      <xsl:otherwise><title><xsl:value-of select="$project/title"/></title></xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates select="keywords"/>
    <xsl:apply-templates select="/document/properties/rss"/>
    <link rel="stylesheet" href="default.css"/>
  </head>

  <body bgcolor="#ffffff" link="#6763a9" vlink="#6763a9"
        topmargin="0" bottommargin="0" leftmargin="0" marginheight="0" marginwidth="0">

  <a name="top"/>

    <table border="0" cellpadding="0" cellspacing="0" height="400">

      <tr>
        <td width="10" valign="top" align="left" bgcolor="#7270c2"><img
            src="images/dotTrans.gif" width="1" height="1" border="0"/></td>
        <td valign="top" align="left" bgcolor="#7270c2"><xsl:attribute name="width"><xsl:value-of select="$second_col_width"></xsl:value-of></xsl:attribute><img
            src="images/dotTrans.gif" width="1" height="1" border="0"/></td>
        <td width="7" valign="top" align="left"><img src="images/dotTrans.gif" border="0"
            width="1" height="1"/></td>
        <td width="70" valign="top" align="left"><img
            src="images/dotTrans.gif" width="70" height="6" border="0"/></td>
        <td width="468" valign="top" align="left"><img
            src="images/top_2.gif"  width="468" height="6" border="0"/></td>
        <td width="120" valign="top" align="left"><xsl:element
            name="img"><xsl:attribute name="src">images/line_purple.gif</xsl:attribute>
          <xsl:attribute name="width">120</xsl:attribute>
          <xsl:attribute name="height">6</xsl:attribute>
          <xsl:attribute name="border">0</xsl:attribute>
          </xsl:element></td>
      </tr>

      <tr>
        <td width="10" bgcolor="#7270c2" valign="top" align="left"><img
            src="images/dotTrans.gif" border="0" width="1" height="1"/></td>
        <td bgcolor="#7270c2" valign="top" align="left"><xsl:attribute name="width"><xsl:value-of select="$second_col_width"></xsl:value-of></xsl:attribute><img
            src="images/dotTrans.gif" border="0" width="1" height="1"/></td>
        <td width="7" bgcolor="#ffffff" valign="top" align="left"></td>
        <td width="70" valign="top" align="left"><img
            src="images/dotTrans.gif" width="1" height="1" border="0"/></td>
        <td width="468" valign="middle" align="left">
          <xsl:apply-templates select="$project/topNav"/><br/>
          <img src="images/dotTrans.gif" width="1" height="2" border="0"/></td>
        <td width="120" height="20" valign="top" align="left">&#160;</td>
      </tr>

      <tr>
        <td width="10" bgcolor="#7270c2" valign="top" align="left"><img
            src="images/dotTrans.gif" width="10" height="3" border="0"/></td>
        <td bgcolor="#7270c2" valign="top" align="right"><xsl:attribute name="width"><xsl:value-of select="$second_col_width"></xsl:value-of></xsl:attribute><img
            src="images/line_sm.gif" width="105" height="3" border="0"/></td>
        <td width="7" bgcolor="#a9a5de" valign="top" align="left"><img
            src="images/line_sm.gif" width="7" height="3" border="0"/></td>
        <td width="70" valign="top" align="left"><img
            src="images/line_light.gif" width="70" height="3" border="0"/></td>
        <td width="468" valign="top" align="left"><img
            src="images/line_light.gif" width="468" height="3" border="0"/></td>
        <td width="120" valign="top" align="left"><img
            src="images/dotTrans.gif" border="0" width="1" height="1"/></td>
      </tr>

      <tr>
        <td bgcolor="#7270c2" valign="top" align="left"><img
            src="images/dotTrans.gif" width="10" height="10" border="0"/></td>

        <td bgcolor="#7270c2" valign="top" align="left"><xsl:attribute name="width"><xsl:value-of select="$second_col_width"></xsl:value-of></xsl:attribute><img src="images/dotTrans.gif" width="1" height="2" border="0"/>
        <br/><xsl:apply-templates select="$project/navSections"/></td>

        <td width="7" bgcolor="#a9a5de" valign="top" align="left">&#160;</td>
        <td width="70" valign="top" align="left">&#160;</td>
        <td rowspan="4" width="468" valign="top">
          <table cols="2" rows="2" border="0" cellpadding="0" cellspacing="0"
                 width="100%">
            <tr>
              <td valign="top" align="left"><br/><img border="0" height="34" hspace="0"
                  src="{$project/logo}" vspace="0" width="115"/><br/>
                <img border="0" height="10" hspace="0" src="images/dotTrans.gif"/>
              </td>
              <td width="120" height="5" valign="top" align="right">
              <!--<a href="http://www.exolab.org"><img
                     src="images/logo_exolab.gif" hspace="0" vspace="10" width="77"
                     height="20" border="0"/></a>-->
              </td>
            </tr>
          </table>

          <p/><p/><br/>

          <xsl:choose>
            <xsl:when test="/document/body/title">
              <h2 align="center">
                <xsl:value-of select="/document/body/title"/>
              </h2>
            </xsl:when>
            <xsl:when test="/document/properties/title">
              <h2 align="center">
                <xsl:value-of select="/document/properties/title"/>
              </h2>
            </xsl:when>
            <xsl:otherwise/>
          </xsl:choose>
          <xsl:if test="/document/properties/author">
            <p align="center">
              Documentation Author(s):<br/>
              <xsl:for-each select="/document/properties/author">
                <xsl:value-of select="."/><br/>
              </xsl:for-each>
            </p>
          </xsl:if>

          <xsl:apply-templates select="document/body/header"/>

          <!-- build the page navigation first, section by section -->
          <xsl:variable name="sections" select=".//section"/>
          <xsl:if test="$sections"><HR size="1"/>
          </xsl:if>

          <xsl:for-each select="$sections">
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

          <xsl:if test="$sections"><HR size="1"/></xsl:if>

          <br/>

          <!-- now show the sections themselves -->
          <xsl:apply-templates select="document/body/section"/>
        </td>
      </tr>

      <!-- line row -->

      <tr height="5">
        <td width="10" height="5" bgcolor="#7270c2" valign="top" align="left">&#160;</td>
        <td height="5" bgcolor="#7270c2" valign="top"><xsl:attribute name="width"><xsl:value-of select="$second_col_width"></xsl:value-of></xsl:attribute>
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
        <td bgcolor="#7270c2" valign="top" align="left"><xsl:attribute name="width"><xsl:value-of select="$second_col_width"></xsl:value-of></xsl:attribute><xsl:apply-templates select="searchForm"/></td>
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
        <td rowspan="2" height="100%" bgcolor="#7270c2" valign="bottom" align="left"><xsl:attribute name="width"><xsl:value-of select="$second_col_width"></xsl:value-of></xsl:attribute><img src="images/stripe105.gif" width="105" height="125" border="0"/></td>
        <td width="7" rowspan="2" height="100%" bgcolor="#a9a5de" valign="top"
            align="left">&#160;</td>
        <td width="70" height="100%" valign="top" align="left">&#160;</td>
        <td width="120" height="100%" valign="top" align="left">&#160;</td>
      </tr>

      <!-- extra  row -->

      <tr height="5">
        <!-- <td width="10" height="25" valign="top" align="left">&#160;</td>  -->
        <!-- <td width="125" height="25" valign="top" align="left">&#160;</td> -->
        <!-- <td width="7" height="25" valign="top" align="left">&#160;</td>   -->
        <td width="70" height="25" valign="top" align="left">&#160;</td>
        <td width="400" height="25" valign="bottom" align="left">
          <br/>
          <br/>
          <img src="images/line_light.gif"  border="0" width="400" height="3" /><br/>
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
        <xsl:if test="@title">
          <a name="{translate(@title,' ','-')}"><h2><xsl:value-of select="@title"/></h2></a>
        </xsl:if>
        <xsl:if test="@name">
          <a name="{translate(@name,' ','-')}"><h2><xsl:value-of select="@name"/></h2></a>
        </xsl:if>
      </xsl:when>
      <xsl:when test='$level=3'>
        <xsl:if test="@ref-id"><a name="{@ref-id}"/></xsl:if>
        <xsl:if test="@title">
          <a name="{translate(@title,' ','-')}"><h3><xsl:value-of select="@title"/></h3></a>
        </xsl:if>
        <xsl:if test="@name">
          <a name="{translate(@name,' ','-')}"><h3><xsl:value-of select="@name"/></h3></a>
        </xsl:if>
      </xsl:when>
      <xsl:when test='$level=4'>
        <xsl:if test="@ref-id"><a name="{@ref-id}"/></xsl:if>
        <xsl:if test="@title">
          <a name="{translate(@title,' ','-')}"><h4><xsl:value-of select="@title"/></h4></a>
        </xsl:if>
        <xsl:if test="@name">
          <a name="{translate(@name,' ','-')}"><h4><xsl:value-of select="@name"/></h4></a>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="@ref-id"><a name="{@ref-id}"/></xsl:if>
        <xsl:if test="@title">
          <a name="{translate(@title,' ','-')}"><h5><xsl:value-of select="@title"/></h5></a>
        </xsl:if>
        <xsl:if test="@name">
          <a name="{translate(@name,' ','-')}"><h5><xsl:value-of select="@name"/></h5></a>
        </xsl:if>
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
      <xsl:otherwise><xsl:value-of select="substring-before($href, '.xml')"/>.html</xsl:otherwise>
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
        <a href="javadoc/{translate(@package,'.','/')}/package-summary.html"><xsl:copy-of select="."/></a>
      </xsl:when>
      <xsl:when test="@class">
        <a href="javadoc/{translate(@class,'.','/')}.html#{.}"><xsl:value-of select="."/></a>
      </xsl:when>
      <xsl:otherwise>
        <a href="javadoc/{translate(.,'.','/')}.html"><xsl:copy-of select="."/></a>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="url">
    <xsl:choose>
      <xsl:when test="@title">
        <a href="{.}"><xsl:value-of select="@title"/></a>
      </xsl:when>
      <xsl:otherwise>
        <a href="{.}"><xsl:copy-of select="."/></a>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="email">
    <a href="mailto:{.}"><xsl:copy-of select="."/></a>
  </xsl:template>


  <!-- Templates for special content -->

  <xsl:template match="body-note">
    <hr size="1" noshadow=""/><span class="bodyGrey"><xsl:apply-templates/><hr size="1" noshadow=""/></span>
  </xsl:template>

  <xsl:template match="note">
    <p>
      <table width="100%" border="0" cellspacing="1" cellpadding="3" bgcolor="#7270c2">
        <tr>
          <td><span class="noteHeader">Note</span></td>
        </tr>
        <tr>
          <td>
            <table width="100%" border="0" cellspacing="1" cellpadding="4" bgcolor="#ededed">
              <tr>
                <td><span class="noteBody">
                  <xsl:apply-templates/>
                </span></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </p>
  </xsl:template>

  <xsl:template match="code-panel">
    <p/>
    <table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor="#7270c2">
      <xsl:if test="@top-caption">
        <tr>
          <td><i><font color="yellow"><xsl:value-of select="@top-caption"/></font></i></td>
        </tr>
      </xsl:if>
      <tr>
        <td>
          <table width="100%" border="0" cellspacing="1" cellpadding="4" bgcolor="#ededed">
            <tr>
              <td>
                <span class="bodyBlack">
                  <pre><xsl:apply-templates/></pre>
                </span>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <xsl:if test="@caption">
      <i><xsl:value-of select="@caption"/></i>
    </xsl:if>
    <p/>
  </xsl:template>

  <xsl:template match="source">
    <p/>
    <table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor="#7270c2">
      <xsl:if test="@top-caption">
        <tr>
          <td><i><font color="yellow"><xsl:value-of select="@top-caption"/></font></i></td>
        </tr>
      </xsl:if>
      <tr>
        <td>
          <table width="100%" border="0" cellspacing="1" cellpadding="4" bgcolor="#ededed">
            <tr>
              <td>
                <span class="bodyBlack">
                  <pre><xsl:apply-templates/></pre>
                </span>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <xsl:if test="@caption">
      <i><xsl:value-of select="@caption"/></i>
    </xsl:if>
    <p/>
  </xsl:template>

  <xsl:template match="code-panel/comment">
    <span class="bodyGrey">
      <font color="#7270c2"><xsl:apply-templates/></font>
    </span>
  </xsl:template>

  <xsl:template match="code-panel/highlight">
    <span class="bodyGrey">
      <b><xsl:apply-templates/></b>
    </span>
  </xsl:template>

  <xsl:template match="code-block">
    <table border="0" cellpadding="4">
      <tr>
        <td>
          <xsl:apply-templates/>
        </td>
      </tr>
    </table>
    <xsl:if test="@caption">
      <i><xsl:value-of select="@caption"/></i>
    </xsl:if>
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
    <xsl:variable name="sections" select="document(./@href)/document/body/section"/>
    <xsl:variable name="url" select="./@href"/>
    <xsl:variable name="tmp-count">
      <xsl:choose>
        <xsl:when test="@max-nodes"><xsl:value-of select="@max-nodes"/></xsl:when>
        <xsl:otherwise>9999999</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="max-count" select="number($tmp-count)"/>

    <xsl:choose>
      <xsl:when test="@select-node">
        <xsl:for-each select="$sections/*[local-name()=current()/@select-node]">
          <xsl:if test="position() &lt;= $max-count">
            <xsl:apply-templates select="."/>
          </xsl:if>
          <xsl:if test="position() = ($max-count+1)">
            <xsl:variable name="href">
              <xsl:call-template name="link-convertor">
                <xsl:with-param name="href" select="$url"/>
              </xsl:call-template>
            </xsl:variable>
            <p><a href="{$href}">More...</a></p>
          </xsl:if>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="$sections"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="mailing-lists">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="mailing-list">
    <div>
      [
      <a href="mailto:{@name}-subscribe@castor.{@server}">Subscribe</a> |
      <a href="mailto:{@name}-unsubscribe@castor.{@server}">Unsubscribe</a>
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
          <td bgcolor="{$color-epsilon}"><span class="bodyGrey">
            <b>Project</b></span>
          </td>
          <td bgcolor="{$color-epsilon}"><span class="bodyGrey">
            <b>Status</b></span>
          </td>
          <td bgcolor="{$color-epsilon}"><span class="bodyGrey">
            <b>Since</b></span>
          </td>
        </tr>
        <xsl:for-each select="../contributor[@type=$type]">
          <tr valign="top">
            <td><span class="bodyGrey">
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
            <td><span class="bodyGrey">
              <xsl:value-of select="project"/></span>
            </td>
            <td><span class="bodyGrey">
              <xsl:choose>
                <xsl:when test="status='active'">
                  Active
                </xsl:when>
                <xsl:otherwise>
                  Inactive
                </xsl:otherwise>
              </xsl:choose>
            </span></td>
            <td><span class="bodyGrey">
              <xsl:value-of select="since/date"/></span>
            </td>
          </tr>
        </xsl:for-each>
      </table>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="companies">
    <xsl:variable name="color-epsilon" select="'#ffffff'"/>
    <p><span class="bodyGrey"><b><xsl:value-of select="name"/></b></span></p>
    <p><span class="bodyGrey"><xsl:value-of select="description"/></span></p>
    <table cellpadding="4" cellspacing="2" width="90%">
      <xsl:for-each select="company">
        <xsl:variable name="name" select="name"/>
        <xsl:variable name="url" select="url"/>
        <xsl:variable name="description" select="description"/>
        <tr><td colspan="2">
          <h3>
            <a>
              <xsl:attribute name="href">
                <xsl:apply-templates select="url"/>
              </xsl:attribute>
              <xsl:value-of select="name"/>
            </a>
          </h3>
        </td></tr>

        <tr valign="top">
          <td bgcolor="{$color-epsilon}">
            <span class="bodyGrey">
              <b>Contact</b>
            </span>
          </td>
          <xsl:apply-templates select="contact"/>
        </tr>

        <tr valign="top">
          <td bgcolor="{$color-epsilon}">
            <span class="bodyGrey">
              <b>URL</b>
            </span>
          </td>
          <td>
            <span class="bodyGrey">
              <xsl:copy-of select="url"/>
            </span>
          </td>
        </tr>
        <tr valign="top">
          <td bgcolor="{$color-epsilon}">
            <span class="bodyGrey">
              <b>Description</b>
            </span>
          </td>
          <td>
            <span class="bodyGrey">
              <xsl:value-of select="description"/>
            </span>
          </td>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>

  <xsl:template match="contact">
    <td>
      <span class="bodyGrey">
        <xsl:copy-of select="name"/><br/>
        email: <xsl:copy-of select="email"/><br/>
        phone: <xsl:value-of select="phone"/>
      </span>
    </td>
  </xsl:template>

  <xsl:template match="contributor/email">
    <xsl:call-template name="munge-email">
      <xsl:with-param name="value" select="."/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="munge-email">
    <xsl:param name="value" select="''"/>
    <xsl:choose>
      <xsl:when test="contains($value, '@')">
        <xsl:value-of select="concat(substring-before($value, '@'), ' ')"/>
        AT
        <xsl:call-template name="replace-dot">
          <xsl:with-param name="value" select="substring-after($value, '@')"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$value"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="replace-dot">
    <xsl:param name="value" select="''"/>
    <xsl:choose>
      <xsl:when test="contains($value, '.')">
        <xsl:value-of select="substring-before($value, '.')"/>
        DOT
        <xsl:call-template name="replace-dot">
          <xsl:with-param name="value" select="substring-after($value, '.')"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$value"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="news">
    <xsl:param name="id" select="@id" />
    <br />
    <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#7270c2"><tr><td>
      <table width="100%" border="0" cellspacing="1" cellpadding="8" bgcolor="#ededed"><tr><td>
        <span class="newsTitle"><a name="item{$id}"><xsl:value-of select="title"/></a></span><br/><br/>
        <span class="newsSummary"><xsl:apply-templates select="summary"/><br/><br/>
          <xsl:if test="url">
            <div align="center"><a href="{url}">[Read More/Comment]</a></div><br/>
          </xsl:if>
          Submitted by <xsl:value-of select="author"/>
          <xsl:if test="date | time">,
            <xsl:if test="date"> on <xsl:value-of select="date"/></xsl:if>
            <xsl:if test="time"> @ <xsl:value-of select="time"/></xsl:if>
          </xsl:if>
        </span>
      </td></tr></table>
    </td></tr></table>
  </xsl:template>

  <xsl:template match="release">
    <br/>
    <hr/>
    <h1>Release <xsl:value-of select="@version"/></h1>
    <hr/>
    <table width="100%" border="0" cellspacing="1" cellpadding="2">
      <tr>
        <td>Description:</td><td><xsl:value-of select="description"/></td>
      </tr><tr>
        <td>released:</td><td><xsl:value-of select="date"/></td>
      </tr><tr>
        <td>managed by:</td><td><xsl:value-of select="managed-by"/></td>
      </tr>
    </table>
    <br/>
    <xsl:apply-templates select="summary"/><br/>
    <xsl:apply-templates select="bugs"/><br/>
  </xsl:template>

  <xsl:template match="bugs">
    <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#7270c2">
      <tr><td>
      <table width="100%" border="0" cellspacing="1" cellpadding="8" bgcolor="#ededed">
        <CAPTION style="font-weight:bold">
          Bug Fixes And Issues Addressed
        </CAPTION>
        <tr bgcolor="#7270c2">
          <th align="left">Id</th>
          <th align="left">Date</th>
          <th align="left">Fixed by</th>
          <th align="left">Committed by </th>
          <th align="left">Description</th>
          <th align="left">Type</th>
          <th align="left">Project</th>
          <th align="left">Module</th>
        </tr>
        <xsl:apply-templates select="bug"/>
      </table></td></tr>
    </table>
  </xsl:template>

  <xsl:template match="bug">

    <xsl:if test="normalize-space(.) != ''">

      <xsl:variable name="contributor-email">
        <xsl:call-template name="munge-email">
          <xsl:with-param name="value" select="contributor/email"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="submitter-email">
        <xsl:call-template name="munge-email">
          <xsl:with-param name="value" select="submitter/email"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="reporter-email">
        <xsl:call-template name="munge-email">
          <xsl:with-param name="value" select="reporter/email"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="contributor-name">
        <xsl:choose>
          <xsl:when test="contributor/name">
            <xsl:value-of select="contributor/name"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$contributor-email"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="submitter-name">
        <xsl:choose>
          <xsl:when test="submitter/name">
            <xsl:value-of select="submitter/name"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$submitter-email"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:variable name="reporter-name">
        <xsl:choose>
          <xsl:when test="reporter/name">
            <xsl:value-of select="reporter/name"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$reporter-email"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <tr valign="top">
        <xsl:if test="(position() mod 2) = 0">
          <xsl:attribute name="bgcolor">#FFFFFF</xsl:attribute>
        </xsl:if>
        <td>
          <xsl:choose>
            <xsl:when test="@id != ''">
              <xsl:choose>
                <xsl:when test="@tool = 'jira'">
                  <xsl:choose>
                    <xsl:when test="@id > 0">
                      <a href="http://jira.codehaus.org/browse/CASTOR-{@id}"><xsl:value-of select="@id" /></a>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="@id"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:choose>
                    <xsl:when test="@id > 0">
                      <a href="http://bugzilla.exolab.org/show_bug.cgi?id={@id}"><xsl:value-of select="@id" /></a>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="@id"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                   ----
             </xsl:otherwise>
          </xsl:choose>
        </td>
        <td><xsl:value-of select="date"/></td>
        <td><a href="mailto:{$contributor-email}"><xsl:value-of select="$contributor-name" /></a></td>
        <td><a href="mailto:{$submitter-email}"><xsl:value-of select="$submitter-name" /></a></td>
        <td><xsl:value-of select="description"/>
          <xsl:if test="$reporter-name != ''">
            <br/><i><b>Reporter: </b>
            <a href="mailto:{$reporter-email}"><xsl:value-of select="$reporter-name" /></a>
            </i>
          </xsl:if>
        </td>
        <td><xsl:value-of select="type"/></td>
        <td><xsl:value-of select="project"/></td>
        <td><xsl:value-of select="module"/></td>
      </tr>
    </xsl:if>
  </xsl:template>

  <xsl:template match="tip">
    <br />
    <table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor="#5A0F5E">
      <tr>
        <td>
          <table width="100%" border="0" cellspacing="1" cellpadding="4" bgcolor="#E6CFE6">
            <tr>
              <td><b>Tip:</b></td>
              <td><xsl:apply-templates/></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template match="ctable">
    <xsl:variable name="cellpadding">
      <xsl:choose>
        <xsl:when test="@cellpadding">
          <xsl:value-of select="@cellpadding"/>
        </xsl:when>
        <xsl:otherwise>8</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <table border="0" cellspacing="1" cellpadding="2" bgcolor="#7270c2">
      <tr>
        <td>
          <table border="0" cellspacing="1" cellpadding="{$cellpadding}" bgcolor="#ededed">
            <caption style="font-weight:bold">
              <xsl:value-of select="@caption" />
            </caption>
            <tr class="cheader">
              <xsl:apply-templates select="cheader" />
            </tr>
            <xsl:apply-templates select="crow" />
          </table>
        </td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template match="cheader">
  	<xsl:for-each select="td">
  		<td class="cheader"><xsl:value-of select="."/></td>
  	</xsl:for-each>
  </xsl:template>

  <xsl:template match="crow">
    <tr>
      <xsl:if test="(position() mod 2) = 0">
        <xsl:attribute name="bgcolor">#FFFFFF</xsl:attribute>
      </xsl:if>
      <xsl:if test="(position() mod 2) = 1">
        <xsl:attribute name="bgcolor">#DDDDDD</xsl:attribute>
      </xsl:if>
      <xsl:apply-templates/>
    </tr>
  </xsl:template>

</xsl:stylesheet>
