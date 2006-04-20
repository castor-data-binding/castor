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
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
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
        Enumeration   enum;

        db = _jdo.getDatabase();

        db.begin();
        writer.println( "Begin transaction" );
        
        // Look up the product and if found in the database,
        // delete this object from the database
        productOql = db.getOQLQuery( "SELECT p FROM myapp.Product p WHERE id = $1" );
        productOql.bind( 4 );
        enum = productOql.execute();
        if ( enum.hasMoreElements() ) {
            product = (Product) enum.nextElement();
            writer.println( "Deleting existing product: " + product );
            db.remove(  product );
        }
        
        // Look up the computer and if found in the database,
        // delete ethis object from the database
        computerOql = db.getOQLQuery( "SELECT c FROM myapp.Computer c WHERE id = $1" );
        computerOql.bind( 6 );
        enum = computerOql.execute();
        if ( enum.hasMoreElements() ) {
            computer = (Computer) enum.nextElement();
            writer.println( "Deleting existing computer: " + computer );
            db.remove( computer );
        }
        
        // Look up the group and if found in the database,
        // delete this object from the database
        groupOql = db.getOQLQuery( "SELECT g FROM myapp.ProductGroup g WHERE id = $1" );
        groupOql.bind( 3 );
        enum = groupOql.execute();
        if ( enum.hasMoreElements() ) {
            group = (ProductGroup) enum.nextElement();
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
        enum = groupOql.execute();
        if ( ! enum.hasMoreElements() ) {
            group = new ProductGroup();
            group.setId( 3 );
            group.setName( "a group" );
            writer.println( "Creating new group: " + group );
        } else {
            group = (ProductGroup) enum.nextElement();
            writer.println( "Query result: " + group );
        }
        
        // If no such product exists in the database, create a new
        // object and persist it
        // Note: product uses group, so group object has to be
        //       created first, but can be persisted later
        productOql.bind( 4 );
        enum = productOql.execute();
        if ( ! enum.hasMoreElements() ) {
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
            writer.println( "Query result: " + enum.nextElement() );
        }
        
        // If no such computer exists in the database, create a new
        // object and persist it
        // Note: computer uses group, so group object has to be
        //       created first, but can be persisted later
        computerOql.bind( 6 );
        enum = computerOql.execute();
        if ( ! enum.hasMoreElements() ) {
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
            writer.println( "Query result: " + enum.nextElement() );
        }
        writer.println( "Commit transaction" );
        db.commit();

        Serializer  ser;
        Marshaller  marshal;

        ser = new XMLSerializer( new OutputFormat( Method.XML, null, true ) );
        ser.setOutputCharStream( writer );
        marshal = new Marshaller( ser.asDocumentHandler() );
        marshal.setMapping( _mapping );

        db.begin();
        productOql = db.getOQLQuery( "SELECT p FROM myapp.Product p" );
        enum = productOql.execute();
        ser.asDocumentHandler().startDocument();
        ser.asDocumentHandler().startElement( "products", null );
        while( enum.hasMoreElements() )
            marshal.marshal( enum.nextElement() );
        ser.asDocumentHandler().endElement( "products" );
        ser.asDocumentHandler().endDocument();

        db.commit();
        db.close();
    }


}


