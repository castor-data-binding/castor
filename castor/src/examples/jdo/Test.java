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
import org.exolab.castor.persist.spi.Complex;
import org.exolab.castor.util.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.mapping.Mapping;
import org.apache.xml.serialize.*;

/**
 * This example is only intended to show how castor can be set up 
 * in a standalone environment. For detailed examples on the mapping file, 
 * database schemas, supported features and their expected behaviors, 
 * please consult the JDO test cases instead. The JDO test cases can be 
 * found in the full CVS snapshot and located under the directory of 
 * src/tests/jdo and src/tests/myapp.
 */
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
        try 
        {
            test = new Test( writer );
            test.run( writer );
        } 
        catch ( Exception except ) 
        {
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
        Product       product = null;
        ProductGroup  group;
        Category      category;
        ProductDetail detail;
        Computer      computer;
        OQLQuery      productOql;
        OQLQuery      groupOql;
        OQLQuery      categoryOql;
        OQLQuery      computerOql;
        QueryResults  results;

        db = _jdo.getDatabase();

        db.begin();
        writer.println( "Begin transaction to remove Product objects" );

        // Look up the products and if found in the database,
        // delete them from the database
        productOql = db.getOQLQuery( "SELECT p FROM myapp.Product p WHERE p.id = $1" );

        for ( int i = 4; i < 10; ++i )
        {
            productOql.bind( i );
            results = productOql.execute();

            while ( results.hasMore() ) 
            {
                product = ( Product ) results.next();
                writer.println( "Deleting existing product: " + product );
                db.remove( product );
            }
        }

        writer.println( "End transaction to remove Product objects" );
        db.commit();

        db.begin();
        writer.println( "Begin transaction to remove Computer object" );
        
        // Look up the computer and if found in the database,
        // delete ethis object from the database
        computerOql = db.getOQLQuery( "SELECT c FROM myapp.Computer c WHERE c.id = $1" );
        computerOql.bind( 11 );
        results = computerOql.execute();

        while ( results.hasMore() ) 
        {
            computer = ( Computer ) results.next();
            writer.println( "Deleting existing computer: " + computer );
            db.remove( computer );
        }

        writer.println( "End transaction to remove Computer objects" );
        db.commit();

        db.begin();
        writer.println( "Begin transaction to remove Category objects" );

        // Look up the categories and if found in the database,
        // delete this object from the database
        categoryOql = db.getOQLQuery( "SELECT c FROM myapp.Category c WHERE c.id = $1" );

        // Still debugging this area because deletion of Category objects is not 
        // working the second time around
        for ( int i = 7; i < 10; ++i )
        {
            categoryOql.bind( i );
            results = categoryOql.execute();
            while ( results.hasMore() ) 
            {
                category = ( Category ) results.next();
                writer.println( "Deleting existing category: " + category );
                db.remove( category );
            }
        }
        
        writer.println( "End transaction to remove Category objects" );
        db.commit();

        db.begin();
        writer.println( "Begin transaction: one-to-one, one-to-many, and dependent relations" );
        // If no such group exists in the database, create a new
        // object and persist it
        groupOql = db.getOQLQuery( "SELECT g FROM myapp.ProductGroup g WHERE id = $1" );
        groupOql.bind( 3 );
        results = groupOql.execute();

        if ( ! results.hasMore() ) 
        {
            group = new ProductGroup();
            group.setId( 3 );
            group.setName( "a group" );
            db.create( group );
            writer.println( "Creating new group: " + group );
        } 
        else 
        {
            group = ( ProductGroup ) results.next();
            writer.println( "Query result: " + group );
        }

        // If no such product exists in the database, create a new
        // object and persist it
        // Note: product uses group, so group object has to be
        //       created first, but can be persisted later
        productOql.bind( 4 );
        results = productOql.execute();

        if ( ! results.hasMore() ) 
        {
            product = new Product();
            product.setId( 4 );
            product.setName( "product4" );
            product.setPrice( 200 );
            product.setGroup( group );
            detail = new ProductDetail();
            detail.setId( 1 );
            detail.setName( "keyboard" );
            product.addDetail( detail );
            detail = new ProductDetail();
            detail.setId( 2 );
            detail.setName( "mouse" );
            product.addDetail( detail );
            detail = new ProductDetail();
            detail.setId( 3 );
            detail.setName( "monitor" );
            product.addDetail( detail );
            writer.println( "Creating new product: " + product );
            db.create( product );
        } 
        else 
        {
            writer.println( "Query result: " + results.next() );
        }

        // If no such computer exists in the database, create a new
        // object and persist it
        // Note: computer uses group, so group object has to be
        //       created first, but can be persisted later
        computerOql.bind( 11 );
        results = computerOql.execute();

        if ( ! results.hasMore() ) {
            computer = new Computer();
            computer.setId( 11 );
            computer.setCpu( "Pentium" );
            computer.setName( "MyPC" );
            computer.setPrice( 400 );
            computer.setGroup( group );
            detail = new ProductDetail();
            detail.setId( 4 );
            detail.setName( "network card" );
            computer.addDetail( detail );
            detail = new ProductDetail();
            detail.setId( 5 );
            detail.setName( "scsi card" );
            computer.addDetail( detail );
            writer.println( "Creating new computer: " + computer );
            db.create( computer );
        } else {
            writer.println( "Query result: " + results.next() );
        }
        writer.println( "End transaction: one-to-one, one-to-many and dependent relations" );
        db.commit();



        // Many-to-many example using one existing product
        db.begin();
        writer.println( "Begin transaction: one-to-one and dependent relations" );

        // If no such products with ids 5-8 exist, create new
        // objects and persist them
        for ( int i = 5; i < 10; ++i )
        {
            int j = i + 1;
            productOql.bind( j );
            results = productOql.execute();

            if ( ! results.hasMore() ) 
            {
                product = new Product();
                product.setId( i );
                product.setName( "product" + product.getId() );
                product.setPrice( 300 );
                product.setGroup( group );
                detail = new ProductDetail();
                detail.setId( j );
                detail.setName( "detail" + detail.getId() );
                product.addDetail( detail );
                writer.println( "Creating new product: " + product );
                db.create( product );
            } 
            else 
            {
                writer.println( "Query result: " + results.next() );
            }
        }

        writer.println( "End transaction: one-to-one and dependent relations " );
        db.commit();


        db.begin();
        writer.println( "Begin transaction: many-to-many relations" );

        for ( int x = 4; x < 7; ++x )
        {
            int y = x + 3;
            product = ( Product ) db.load( Product.class, new Integer( y ) ); 

            // If no such categories exists in the database, create new
            // objects and persist them
            categoryOql.bind( x );
            results = categoryOql.execute();

            if ( ! results.hasMore() ) 
            {
                category = new Category();
                category.setId( x );
                category.setName( "category" + category.getId() );
                category.addProduct( product );
                db.create( category );
                writer.println( "Creating new category: " + category );
            } 
            else 
            {
                category = ( Category ) results.next();
                writer.println( "Query result: " + category );
            }
        }

        writer.println( "End transaction: many-to-many relations" );
        db.commit();

        product.setPrice( 333 );
        writer.println( "Updated price: " + product );

        db.begin();
        writer.println( "Begin transaction: long transaction" );

        //
        // Don't forget to implement TimeStampable for the long transaction!!!
        //
        db.update( product );
        writer.println( "End transaction: long transaction" );
        db.commit();


        Marshaller     marshaller;

        marshaller = new Marshaller( writer );
        marshaller.setMapping( _mapping );

        db.begin();
        writer.println( "Begin transaction: marshalling objects to XML" );

        computerOql = db.getOQLQuery( "SELECT c FROM myapp.Computer c" );
        results = computerOql.execute();

        while( results.hasMore() )
            marshaller.marshal( results.next() );

        writer.println( "End transaction: marshalling objects to XML" );
        db.commit();

        db.close();
        writer.println( "Test complete" );
    }


}


