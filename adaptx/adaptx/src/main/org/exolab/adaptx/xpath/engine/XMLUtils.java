/**
 * Copyright (C) 2000, Intalio Inc.
 *
 * The program(s) herein may be used and/or copied only with the
 * written permission of Intalio Inc. or in accordance with the terms
 * and conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *
 * $Id$
 */


package org.exolab.adaptx.xpath.engine;


/**
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$
 */
public final class XMLUtils
{


    /**
     * Null character
     */
    public final static char NULL  = '\u0000';
    
    /**
     * Single space character
     */
    public final static char SPACE = '\u0020';
    
    /**
     * Tab character
     */
    public final static char TAB   = '\u0009';
    
    /**
     * Carriage Return character
     */
    public final static char CR    = '\r';
    
    /**
     * Linefeed character
     */
    public final static char LF    = '\n';
    
    /**
     * Empty String
     */
    public final static String EMPTY = "";


    public static String toQualified( String uri, String local )
    {
        if ( local == null || local.length() == 0 )
            throw new IllegalArgumentException( "Argument local is null or an empty string" );
        if ( uri == null || uri.length() == 0 )
            return local;
        return "{" + uri + "}" + local;
    }


    public static String[] fromQualified( String qname )
    {
        String[] names;
        int      index;

        if ( qname == null || qname.length() == 0 )
            throw new IllegalArgumentException( "Argument qname is null or an empty string" );
        names = new String[ 2 ];
        if ( qname.charAt( 0 ) == '{' ) {
            index = qname.indexOf( '}', 1 );
            if ( index > 0 ) {
                names[ 0 ] = qname.substring( 1, index );
                names[ 1 ] = qname.substring( index + 1 );
            } else {
                names[ 0 ] = "";
                names[ 1 ] = qname;
            }
        } else {
            names[ 0 ] = "";
            names[ 1 ] = qname;
        }
        return names;
    }


    /**
     * Strips whitespace from the given String. Newlines (#xD),
     * tabs (#x9), and consecutive spaces (#x20) are converted to
     * a single space (#x20). This method is useful for processing
     * consective Strings since any leading spaces will be converted
     * to a single space.
     *
     * @param data the String to strip whitespace from
     */
    public static String stripSpace( String data )
    {
        return stripSpace( data, false, false );
    }

    
    /**
     * Strips whitespace from the given String. Newlines (#xD),
     * tabs (#x9), and consecutive spaces (#x20) are converted to
     * a single space (#x20).
     *
     * @param data the String to strip whitespace from
     * @param stripAllLeadSpace, a boolean indicating whether or not to
     * strip all leading space. If true all whitespace from the start of the
     * given String will be stripped. If false, all whitespace from the start 
     * of the given String will be converted to a single space.
     * @param stripAllTrailSpace, a boolean indicating whether or not to
     * strip all trailing space. If true all whitespace at the end of the
     * given String will be stripped. If false, all whitespace at the end 
     * of the given String will be converted to a single space.
     */
    public static String stripSpace ( String data, boolean stripAllLeadSpace,
                                      boolean stripAllTrailSpace ) 
    {
        if ( data == null )
            return data;
        
        char lastToken, token;
        char[] oldChars = data.toCharArray();
        char[] newChars = new char[ oldChars.length ];
        
        lastToken = NULL;
        int total = 0;
        
        // indicates we have seen at least one
        // non whitespace charater
        boolean validChar = false; 
        for ( int i = 0 ; i < oldChars.length ; i++) {
            token = oldChars[ i ];
            switch( token ) {
            case SPACE:
            case TAB:
                if ( stripAllLeadSpace && !validChar )
                    break;
                if ( TAB != lastToken && SPACE != lastToken )
                    newChars[ total++ ] = SPACE;
                lastToken = SPACE;
                break;
            case CR:
            case LF:
                if ( stripAllLeadSpace && !validChar )
                    break;
                //-- fix from Majkel Kretschmar
                if ( TAB != lastToken && SPACE != lastToken )
                    newChars[ total++ ] = SPACE;
                    //-- end fix    
                lastToken = SPACE;
                break;
            default:
                newChars[ total++ ] = token;
                lastToken = token;
                //-- added 19990318 to make sure we don't have
                //-- empty text nodes
                validChar = true;
                break;
            }
        }
        //-- remove last trailing space if necessary
        if ( stripAllTrailSpace ) 
           if ( total > 0 && newChars[ total-1 ] == SPACE )
               --total;
        if ( validChar )
            return new String( newChars, 0, total );
        else
            return EMPTY;
    }


    /**
     * Strips whitespace from the given String. Newlines (#xD),
     * tabs (#x9), and consecutive spaces (#x20) are converted to
     * a single space (#x20).
     *
     * @param data the chars to strip whitespace from
     * @param stripAllLeadSpace, a boolean indicating whether or not to
     * strip all leading space. If true all whitespace from the start of the
     * given String will be stripped. If false, all whitespace from the start 
     * of the given String will be converted to a single space.
     * @param stripAllTrailSpace, a boolean indicating whether or not to
     * strip all trailing space. If true all whitespace at the end of the
     * given String will be stripped. If false, all whitespace at the end 
     * of the given String will be converted to a single space.
     * @return the new length of the array
    */
    public static int stripSpace( char[] data, boolean stripAllLeadSpace,
                                  boolean stripAllTrailSpace )
    {
        if ( data == null )
            return 0;
        
        char ch, prev;
        
        prev = NULL;
        // indicates we have seen at least one
        // non whitespace charater
        boolean validChar = false; 
        int total = 0;
        for ( int i = 0 ; i < data.length ; i++ ) {
            ch = data[ i ];
            switch( ch ) {
            case SPACE:
            case TAB:
                if ( stripAllLeadSpace && !validChar )
                    break;
                if ( TAB != prev && SPACE != prev )
                    data[ total++ ] = SPACE;
                prev = SPACE;
                break;
            case CR:
            case LF:
                if ( stripAllLeadSpace && !validChar )
                    break;
                //-- fix from Majkel Kretschmar
                if ( TAB != prev && SPACE != prev )
                    data[ total++] = SPACE;
                //-- end fix    
                prev = SPACE;
                break;
            default:
                data[ total++ ] = ch;
                prev = ch;
                //-- added 19990318 to make sure we don't have
                //-- empty text nodes
                validChar = true;
                break;
            }
        }
        //-- remove last trailing space if necessary
        if ( stripAllTrailSpace ) 
           if ( total > 0 && data[ total-1 ] == SPACE )
               --total;
        if ( validChar )
            return total;
        else 
            return 0;
    }


}
