package jdo;


import java.io.PrintWriter;

import myapp.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO2;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.util.Logger;
import org.exolab.castor.xml.Marshaller;

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

    private static Log _log =
        LogFactory.getFactory().getInstance( Test.class );

    public static final String JdoConfFile = "jdo-conf.xml";

    
    public static final String MappingFile = "mapping.xml";


    public static final String Usage = "Usage: example jdo";


    private Mapping  _mapping;


    private JDO2      _jdo;


    public static void main( String[] args )
    {
        Test          test;
        
        PrintWriter writer = new Logger( System.out ).setPrefix( "test" );

        try 
        {
            test = new Test( writer );
            test.run( writer );
        } 
        catch ( Exception except ) 
        {
            _log.error( except );
            except.printStackTrace( writer );
        }
    }


    public Test( PrintWriter writer )
        throws Exception
    {
        // Load the mapping file
        _mapping = new Mapping( getClass().getClassLoader() );
         _mapping.loadMapping( getClass().getResource( MappingFile ) );

         String jdoConf =  getClass().getResource( JdoConfFile ).toString();
         JDO2.loadConfiguration (jdoConf);
         _jdo = JDO2.createInstance("test");
    }


    public void run( PrintWriter writer )
        throws Exception
    {
        Database      db;
        Product       product = null;
        ProductGroup  group;
        Category      category;
        ProductDetail detail;
        Computer      computer = null;
        OQLQuery      productOql;
        OQLQuery      groupOql;
        OQLQuery      categoryOql;
        OQLQuery      computerOql;
        QueryResults  results;

        db = _jdo.getDatabase();

        db.begin();
        _log.info( "Begin transaction to remove Product objects" );

        // Look up the products and if found in the database,
        // delete them from the database
        productOql = db.getOQLQuery( "SELECT p FROM myapp.Product p WHERE p.id = $1" );

        for ( int i = 4; i < 10; ++i )
        {
            _log.debug( "Executing OQL" );
            productOql.bind( i );
            results = productOql.execute();

            while ( results.hasMore() ) 
            {
                product = ( Product ) results.next();
                _log.debug( "Deleting existing product: " + product );
                db.remove( product );
            }
        }

        _log.info( "End transaction to remove Product objects" );
        db.commit();

        db.begin();
        _log.info( "Begin transaction to remove Computer object" );
        
        // Look up the computer and if found in the database,
        // delete ethis object from the database
        computerOql = db.getOQLQuery( "SELECT c FROM myapp.Computer c WHERE c.id = $1" );
        computerOql.bind( 44 );
        results = computerOql.execute();

        while ( results.hasMore() ) 
        {
            computer = ( Computer ) results.next();
            _log.debug( "Deleting existing computer: " + computer );
            db.remove( computer );
        }

        _log.info( "End transaction to remove Computer objects" );
        db.commit();

        db.begin();
        _log.info( "Begin transaction to remove Category objects" );

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
                _log.debug( "Deleting existing category: " + category );
                db.remove( category );
            }
        }
        
        _log.info( "End transaction to remove Category objects" );
        db.commit();

        db.begin();
        _log.info( "Begin transaction: one-to-one, one-to-many, and dependent relations" );
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
            _log.debug( "Creating new group: " + group );
        } 
        else 
        {
            group = ( ProductGroup ) results.next();
            _log.debug( "Query result: " + group );
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
            _log.debug( "Creating new product: " + product );
            db.create( product );
        } 
        else 
        {
            _log.debug( "Query result: " + results.next() );
        }

        // If no such computer exists in the database, create a new
        // object and persist it
        // Note: computer uses group, so group object has to be
        //       created first, but can be persisted later
        computerOql.bind( 44 );
        results = computerOql.execute();

        if ( ! results.hasMore() ) {
            computer = new Computer();
            computer.setId( 44 );
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
            _log.debug( "Creating new computer: " + computer );
            db.create( computer );
        } else {
            _log.debug( "Query result: " + results.next() );
        }
        _log.info( "End transaction: one-to-one, one-to-many and dependent relations" );
        db.commit();



        // Many-to-many example using one existing product
        db.begin();
        _log.info( "Begin transaction: one-to-one and dependent relations" );

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
                _log.debug( "Creating new product: " + product );
                db.create( product );
            } 
            else 
            {
                _log.debug( "Query result: " + results.next() );
            }
        }

        _log.info( "End transaction: one-to-one and dependent relations " );
        db.commit();


        db.begin();
        _log.info( "Begin transaction: many-to-many relations" );

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
                _log.debug( "Creating new category: " + category );
            } 
            else 
            {
                category = ( Category ) results.next();
                _log.debug( "Query result: " + category );
            }
        }

        _log.info( "End transaction: many-to-many relations" );
        db.commit();

        product.setPrice( 333 );
        _log.info( "Updated Product price: " + product );

        db.begin();
        _log.info( "Begin transaction: long transaction" );

        //
        // Don't forget to implement TimeStampable for the long transaction!!!
        //
        db.update( product );
        _log.info( "End transaction: long transaction" );
        db.commit();


        db.begin();
        _log.info( "Begin transaction: update extends relation in long transaction " );

        computerOql.bind( 44 );
        results = computerOql.execute();

        while ( results.hasMore() ) 
        {
            computer = new Computer();
            computer = ( Computer ) results.next();
            _log.debug( "Found existing computer: " + computer );
        }

        _log.info( "End transaction: update extends relation in long transaction" );
        db.commit();

        computer.setPrice( 425 );
        _log.info( "Updated Computer price: " + product );

        db.begin();
        _log.info( "Begin transaction: update extends relation in long transaction " );

        //
        // Don't forget to implement TimeStampable for the long transaction!!!
        //
        db.update( computer );

        _log.info( "End transaction: update extends relation in long transaction" );
        db.commit();

        db.begin();
        _log.info( "Begin transaction: simple load and update within the same transaction" );

        computerOql.bind( 44 );
        results = computerOql.execute();

        if ( results.hasMore() )
        {
            computer = ( Computer ) results.next();
            computer.setCpu( "Opteron" );
        }

        _log.info( "End transaction: simple load and update within the same transaction" );
        db.commit();

        db.begin();
        _log.info( "Begin transaction: simple load to test the previous change" );

        computerOql.bind( 44 );
        results = computerOql.execute();

        if ( results.hasMore() )
        {
            _log.info( "Loaded computer:" + ( Computer ) results.next() );
        }

        _log.info( "End transaction: simple load to test the previous change" );
        db.commit();

        Marshaller     marshaller;

        marshaller = new Marshaller( writer );
        marshaller.setMapping( _mapping );

        db.begin();
        _log.info( "Begin transaction: marshalling objects to XML" );

        computerOql = db.getOQLQuery( "SELECT c FROM myapp.Computer c WHERE c.id >= $1" );
        computerOql.bind( 10 );
        results = computerOql.execute();

        while( results.hasMore() )
            marshaller.marshal( results.next() );

        _log.info( "End transaction: marshalling objects to XML" );
        db.commit();

        db.close();
        _log.info( "Test complete" );

        // --------------------------------------------------------------------
        _log.info( "Begin: Walking the mapping descriptor via objects" );

        MappingRoot mappingRoot = _mapping.getRoot();
        ClassMapping classMap;
        FieldMapping fieldMap;
        FieldMapping[] fieldMaps;

        int mappingCount = mappingRoot.getClassMappingCount();

        // loop over the classes
        for ( int i = 0; i < mappingCount; ++i )
        {
            classMap = mappingRoot.getClassMapping( i );
            _log.debug( "Class name: " + classMap.getName() );

            int fieldCount = classMap.getFieldMappingCount();
            fieldMaps = classMap.getFieldMapping();

            _log.debug( "fieldMaps.length: " + fieldMaps.length );

            // loop over the fields in each class
            for ( int j = 0; j < fieldMaps.length; ++j )
            {
                fieldMap = fieldMaps[j];
                _log.debug( "        Field name: " + fieldMap.getName() );
                _log.debug( "        Field type: " + fieldMap.getType() );
            }
        }

        _log.info( "End: Walking the mapping descriptor via objects" );
        // --------------------------------------------------------------------
    }
}
