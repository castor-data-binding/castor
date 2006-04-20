package jdo;


import myapp.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.xml.sax.ContentHandler;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.util.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.mapping.Mapping;
import org.apache.xml.serialize.*;


public class Test
{


    public static final String DatabaseFile = "database.xml";

    
    public static final String MappingFile = "mapping.xml";


    public static final String Usage = "Usage: example jdo";


    private Mapping  _mapping;


    private JDO      _jdo;
    
    
    public static void main( String[] args )
    {
        PrintWriter   writer;
        Test          test;
        
        writer = new Logger( System.out ).setPrefix( "test" );
        try {
            test = new Test( writer );
            test.run( writer );
        } catch ( Exception except ) {
            writer.println( except );
            except.printStackTrace( writer );
        }
    }


    public Test( PrintWriter writer )
        throws Exception
    {
        // Load the mapping file
        _mapping = new Mapping( getClass().getClassLoader() );
        _mapping.setLogWriter( writer );
        _mapping.loadMapping( getClass().getResource( MappingFile ) );

        _jdo = new JDO();
        _jdo.setLogWriter( writer );
        _jdo.setConfiguration( getClass().getResource( DatabaseFile ).toString() );
        _jdo.setDatabaseName( "test" );
    }
    
    
    public void run( PrintWriter writer )
        throws Exception
    {
        Database      db;
        Product       product;
        ProductGroup  group;
        ProductDetail detail;
        Computer      computer;
        OQLQuery      productOql;
        OQLQuery      groupOql;
        OQLQuery      computerOql;
        QueryResults  results;
        
        db = _jdo.getDatabase();

        db.begin();
        writer.println( "Begin transaction" );
        
        // Look up the product and if found in the database,
        // delete this object from the database
        productOql = db.getOQLQuery( "SELECT p FROM myapp.Product p WHERE id = $1" );
        productOql.bind( 4 );
        results = productOql.execute();
        while ( results.hasMore() ) {
            product = (Product) results.next();
            writer.println( "Deleting existing product: " + product );
            db.remove(  product );
        }
        
        // Look up the computer and if found in the database,
        // delete ethis object from the database
        computerOql = db.getOQLQuery( "SELECT c FROM myapp.Computer c WHERE id = $1" );
        computerOql.bind( 6 );
        results = computerOql.execute();
        while ( results.hasMore() ) {
            computer = (Computer) results.next();
            writer.println( "Deleting existing computer: " + computer );
            db.remove( computer );
        }

        // Look up the group and if found in the database,
        // delete this object from the database
        groupOql = db.getOQLQuery( "SELECT g FROM myapp.ProductGroup g WHERE id = $1" );
        groupOql.bind( 3 );
        results = groupOql.execute();
        while ( results.hasMore() ) {
            group = (ProductGroup) results.next();
            writer.println( "Deleting existing group: " + group );
            db.remove( group );
        }
        
        // Checkpoint commits all the updates to the database
        // but leaves the transaction (and locks) open
        writer.println( "Transaction checkpoint" );
        db.commit();

        db.begin();
        // If no such group exists in the database, create a new
        // object and persist it
        groupOql.bind( 3 );
        results = groupOql.execute();
        if ( ! results.hasMore() ) {
            group = new ProductGroup();
            group.setId( 3 );
            group.setName( "a group" );
            db.create( group );
            writer.println( "Creating new group: " + group );
        } else {
            group = (ProductGroup) results.next();
            writer.println( "Query result: " + group );
        }
        
        // If no such product exists in the database, create a new
        // object and persist it
        // Note: product uses group, so group object has to be
        //       created first, but can be persisted later
        productOql.bind( 4 );
        results = productOql.execute();
        if ( ! results.hasMore() ) {
            product = new Product();
            product.setId( 4 );
            product.setName( "some product" );
            product.setPrice( 55 );
            product.setGroup( group );
            detail = new ProductDetail();
            detail.setId( 1 );
            detail.setName( "one" );
            product.addDetail( detail );
            detail = new ProductDetail();
            detail.setId( 2 );
            detail.setName( "two" );
            product.addDetail( detail );
            writer.println( "Creating new product: " + product );
            db.create( product );
        } else {
            writer.println( "Query result: " + results.next() );
        }
        
        // If no such computer exists in the database, create a new
        // object and persist it
        // Note: computer uses group, so group object has to be
        //       created first, but can be persisted later
        computerOql.bind( 6 );
        results = computerOql.execute();
        if ( ! results.hasMore() ) {
            computer = new Computer();
            computer.setId( 6 );
            computer.setCpu( "Pentium" );
            computer.setName( "MyPC" );
            computer.setPrice( 300 );
            computer.setGroup( group );
            detail = new ProductDetail();
            detail.setId( 4 );
            detail.setName( "mouse" );
            computer.addDetail( detail );
            detail = new ProductDetail();
            detail.setId( 5 );
            detail.setName( "screen" );
            computer.addDetail( detail );
            writer.println( "Creating new computer: " + computer );
            db.create( computer );
        } else {
            writer.println( "Query result: " + results.next() );
        }
        writer.println( "Commit transaction" );
        db.commit();

        Serializer     ser;
        Marshaller     marshal;
        ContentHandler handler;

        ser = new XMLSerializer( new OutputFormat( Method.XML, null, true ) );
        ser.setOutputCharStream( writer );
        handler = ser.asContentHandler();
        marshal = new Marshaller( ser.asDocumentHandler() );
        marshal.setMapping( _mapping );

        db.begin();
        productOql = db.getOQLQuery( "SELECT p FROM myapp.Product p" );
        results = productOql.execute();
        handler.startDocument();
        handler.startElement( null, null, "products", null );
        while( results.hasMore() )
            marshal.marshal( results.next() );
        handler.endElement( null, null, "products" );
        handler.endDocument();

        db.commit();

        db.close();
    }


}


