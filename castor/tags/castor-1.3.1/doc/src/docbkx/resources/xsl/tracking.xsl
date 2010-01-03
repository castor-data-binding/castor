<?xml version="1.0" ?>
<!-- 
    Adds HTML tracking codes (Google Analytics) to the Castor reference documentation.
    Imported from html.xsl and html_chunk.xsl.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns="http://www.w3.org/TR/xhtml1/transitional">
    <!-- Google Analytics -->
    <xsl:template name="user.head.content">
        <xsl:comment>begin Google Analytics code</xsl:comment>
        <script type="text/javascript"> 
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript"> 
            var pageTracker = _gat._getTracker("UA-3544187-1");
            <!-- pageTracker._setDomainName("none");  -->
            <!-- pageTracker._setAllowLinker(true);  -->
            pageTracker._trackPageview();
        </script>
        <xsl:comment>end Google Analytics code</xsl:comment>
    </xsl:template>
</xsl:stylesheet>
