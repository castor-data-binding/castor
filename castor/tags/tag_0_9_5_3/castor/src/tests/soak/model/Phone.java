/**
 * Copyright(c) Intalio 2001  All rights reserved.
 */

package soak.model;



import java.math.BigDecimal;
import java.util.Vector;
import java.util.Date;



public class Phone implements java.io.Serializable {

    private short countryCode;

    private short areaCode;

    private long  number;

    public Phone( int countryCode, int areaCode, long number ) {
        this.countryCode    = (short) countryCode;
        this.areaCode       = (short) areaCode;
        this.number         = number;
    }

    public boolean equals( Object o ) {
        if ( !( o instanceof Phone ) )
            return false;

        Phone pho = (Phone) o;
        return countryCode  == pho.countryCode
            && areaCode     == pho.areaCode
            && number       == pho.number;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if ( countryCode != 0 )
            sb.append( countryCode );

        sb.append( '(' );
        if ( areaCode != 0 )
            sb.append( areaCode );
        else
            sb.append( "   " );
        sb.append( ')' );
        return sb.toString();
    }

}

/*
public class Address {

    private String poBox;

    private String streetNumber;

    private String street;

    private String apt;

    private String city;

    private String state;

    private String country;

    private String zip;

    private String zipExt;

    public Address( String poBox, String city, String state, String country, String zip, String zipExt ) {
        this.poBox           = poBox;
        this.city            = city;
        this.state           = state;
        this.country         = country;
        this.zip             = zip;
        this.zipExt          = zipExt;
    }
    public Address( String streetNumber, String street, String apt, String city,
             String state, String country, String zip, String zipExt ) {
        this.streetNumber    = streetNumber;
        this.street          = street;
        this.apt             = apt;
        this.city            = city;
        this.state           = state;
        this.country         = country;
        this.zip             = zip;
        this.zipExt          = zipExt;
    }

    public boolean equals( Object other ) {
        if ( !( other instanceof Address) )
            return false;

        Address addr = (Address) other;
        return equals( poBox,           addr.poBox )
            && equals( streetNumber,    addr.streetNumber )
            && equals( street,          addr.street )
            && equals( apt,             addr.apt )
            && equals( city,            addr.city )
            && equals( state,           addr.state )
            && equals( country,         addr.country )
            && equals( zip,             addr.zip )
            && equals( zipExt,          addr.zipExt );
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if ( poBox != null )
            sb.append( poBox );
        else {
            sb.append( streetNumber );
            sb.append( '\t' );
            sb.append( street );
            sb.append( '\t' );
            sb.append( apt );
        }
        sb.append( '\n' );
        sb.append( city );
        sb.append( '\t' );
        sb.append( state );
        sb.append( '\t' );
        sb.append( zip );
        sb.append( '-' );
        sb.append( zipExt );
        sb.append( '\n' );
        sb.append( country );
        return sb.toString();
    }

    private static boolean equals( String s, String t ) {
        if ( s == t )
            return true;

        if ( s == null || t == null )
            return true;

        return s.equals( t );
    }
}
*/