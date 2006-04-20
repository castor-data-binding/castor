<?xml version="1.0" encoding="ISO-8859-1"?>

<!-- Common Widgets Stylesheet                      -->
<!-- Ismael Ghalimi ghalimi@exoffice.com            -->
<!-- Copyright (c) Exoffice Technologies, Inc. 1999 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="email">
    <a href="mailto:{.}"><xsl:copy-of select="."/></a>
  </xsl:template>

  <xsl:template match="url">
    <a href="{.}"><xsl:copy-of select="."/></a>
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


  <xsl:template match="headline">
    <div>
      <span class="small"><xsl:apply-templates/></span>
    </div>
  </xsl:template>


  <xsl:template match="mailing-list">
    <div>
      [
      <a href="mailto:{@manager}@{@server}?subject=subscribe {@name}">Subscribe</a> |
      <a href="mailto:{@manager}@{@server}?subject=unsubscribe {@name}">Unsubscribe</a> |
      <a href="mailto:{@name}@{@server}">Post</a>
      ]
    </div>
  </xsl:template>

  <xsl:template match="mailing-list/title">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="mailing-list/description">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="mailing-lists">
    <xsl:apply-templates/>
  </xsl:template>


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
      <xsl:for-each select="@*[name(.)!='href']">
        <xsl:copy select="."/>
      </xsl:for-each>
      <xsl:apply-templates/>
    </a>
  </xsl:template>


  <xsl:template match="code">
    <pre><xsl:apply-templates/></pre>
  </xsl:template>

  <xsl:template match="code/comment">
    <font color="red"><xsl:apply-templates/></font>
  </xsl:template>


  <!-- Everything else in the document is considered HTML and
       produced as such with the proper processing.
   -->
  <xsl:template match="*|@*">
    <xsl:copy>
      <xsl:apply-templates select="@*|*|text()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
