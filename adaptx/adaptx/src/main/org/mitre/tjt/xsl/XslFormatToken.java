/*
 * File: XslFormatToken.java
 *
 * $Id$
 */

package org.mitre.tjt.xsl;

import java.text.NumberFormat;

/**
 * This class represents a single format template in an XSL stylesheet
 * numbering template.  An XSL numbering template can contain multiple levels,
 * each with a different format.  For example, Chapter elements may be
 * numbered with a sequence that goes A, B, ... while sections within chapters
 * are numbered with decimal digits, or roman numerals.
 * 
 * @author Tim Taylor (tltaylor@mediaone.net)
 * @version 1.0
 */
public class XslFormatToken {
    
    public final static String DEFAULT_FORMAT_TEMPLATE = "1";
    public final static String DEFAULT_SEPARATOR = ".";

    protected String formatTemplate;
    protected String preSeparator;
    protected String postSeparator;

    /**
     * By default, this will point to this object.  If another token is created
     * afterwards, it will be changed to point to the next token.  The last
     * token in the list will always point to itself.  This allows the last
     * token to be used repeatedly if there are more counts to be processed than
     * formating tokens.
     */
    private XslFormatToken nextToken = null;

    protected NumberFormatFactory formatFactory = new NumberFormatFactory();

    public XslFormatToken(String format, String preSep, String postSep) {
        
        if ((format == null) || (format.length() == 0))
            formatTemplate = DEFAULT_FORMAT_TEMPLATE;
        else
            formatTemplate = format;
            
        preSeparator   = preSep;
        postSeparator  = postSep;
    } //-- XslFormatToken

    public XslFormatToken(String format, String preSep) {
        this(format, preSep, "");
    }

    public static XslFormatToken parseFormat(String format) {
        XslFormatToken head = null,
                    tail = null;
        boolean firstToken = true;

        if(format == null || format.length() == 0)
            format = DEFAULT_FORMAT_TEMPLATE;
            
        int len = format.length();

        int pos = 0;
        StringBuffer sep = new StringBuffer();
        StringBuffer fmt = new StringBuffer();
        while(pos < len) {
            char c = format.charAt(pos);

            // get the non-alphanumeric token
            while((pos < len) && ! Character.isLetterOrDigit(c)) {
                sep.append(c);
                if(++pos < len) c = format.charAt(pos);
            }

            // Are we done yet?
            if(pos == len) break;

            // get the alphanumeric token
            while((pos < len) && Character.isLetterOrDigit(c)) {
                fmt.append(c);
                if(++pos < len) c = format.charAt(pos);
            }

            // Create a new token object
            String sepStr = sep.toString();
            if((sepStr.length() == 0) && ! firstToken)
                sepStr = DEFAULT_SEPARATOR;
            XslFormatToken newToken = new XslFormatToken(fmt.toString(), sepStr);
            if(null == head) head = newToken;
            else tail.nextToken = newToken;

            tail = newToken;
            firstToken = false;
              
            sep.setLength(0);
            fmt.setLength(0);
        }

        // if the length of sep is non-zero, the format ended with non-alphanumeric
        // token.  Add this to the last format token as a post separator.
        if(sep.length() > 0) tail.setPostSeparator(sep.toString());

        return head;
    }

    public String format(int count) {
        
        char lastFormatChar = formatTemplate.charAt(formatTemplate.length() - 1);
        NumberFormat formater = formatFactory.getFormat(lastFormatChar);
        String strCount = formater.format(count);

        // If the format template is longer than the formated count, pad
        StringBuffer buf = new StringBuffer();
        if (preSeparator != null) buf.append(preSeparator);
        
        for(int x = formatTemplate.length() - strCount.length(); x > 0; x--)
            buf.append(formatTemplate.charAt(0));
            
        buf.append(strCount);
        
        return buf.toString();
    } //-- format

    public String getPreSeparator() {
        return preSeparator;
    }

    public void setPreSeparator(String pre) {
        preSeparator = pre;
    }

    public String getPostSeparator() {
        // Find the last token in the list
        XslFormatToken tok = this;
        while(tok.nextToken != null) tok = tok.nextToken;
        return tok.postSeparator;
    }

    public void setPostSeparator(String post) {
        postSeparator = post;
    }

    public NumberFormatFactory setFormatFactory(NumberFormatFactory factory) {
        NumberFormatFactory oldFactory = formatFactory;
        formatFactory = factory;
        return oldFactory;
    }

    public XslFormatToken nextToken() {
        return nextToken;
    } //-- nextToken
    
    /**
     * Returns the String representation of this XslFormatToken
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        XslFormatToken tok = this;
        while (tok != null) {
            sb.append(tok.preSeparator);
            sb.append(tok.formatTemplate);
            sb.append(tok.postSeparator);
            tok = tok.nextToken;
        }
        return sb.toString();
    } //-- toString
}
