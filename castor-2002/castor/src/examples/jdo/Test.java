package jdo;


import myapp.*;
import java.util.Hashtable;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.odmg.Implementation;
import org.odmg.Database;
import org.odmg.Transaction;
import org.odmg.OQLQuery;
import org.exolab.testing.Timing;
import org.exolab.castor.util.Logger;
import org.exolab.castor.jdo.ODMG;



public class Test
{


    public static final String DatabaseFile = "database.xml";
    public static final String MappingFile  = "mapping.xml";
    public static final String Usage = "Usage: test.sh jdo [mapping|odmg|perf <driver-class> <jdbc-uri>]";


    public static void main( String[] args )
    {
	PrintWriter   logger;
	boolean       mapping = false;
	boolean       runPerf = false;
	boolean       runOdmg = false;
	
	logger = Logger.getSystemLogger();
	if ( args.length < 1 ) {
	    System.out.println( Usage );
	    System.exit( 1 );
	}
	if ( args[ 0 ].equalsIgnoreCase( "mapping" ) )
	    mapping = true;
	else if ( args[ 0 ].equalsIgnoreCase( "perf" ) )
	    runPerf = true;
	else if ( args[ 0 ].equalsIgnoreCase( "odmg" ) )
	    runOdmg = true;
	else {
	    System.out.println( Usage );
	    System.exit( 1 );
	}

	try {
	    ODMG          odmg;
	    Database      db;

	    odmg = new ODMG();
	    logger.println( "Reading Java-SQL mapping from " + MappingFile );
	    odmg.loadMapping( Test.class.getResource( MappingFile ).toString() );
	    logger.println( "Reading database sources from " + DatabaseFile );
	    odmg.loadMapping( Test.class.getResource( DatabaseFile ).toString() );

	    if ( mapping ) {
		// Simply dump the mapping to the logger
		odmg.setLogWriter( logger );
		db = odmg.newDatabase();
		db.open( "test", db.OPEN_READ_WRITE );
		db.close();
	    }

	    if ( runOdmg ) {
		// Run the ODMG API test, see odmgTest()
		db = odmg.newDatabase();
		db.open( "test", db.OPEN_READ_WRITE );
		odmgTest( odmg, db, logger );
		db.close();
	    }

	    if ( runPerf ) {
		// Run the performance test, see prefTest()
		db = odmg.newDatabase();
		db.open( "test", db.OPEN_EXCLUSIVE );
		perfTest( odmg, db, logger,
			  ( args.length > 1 ? args[ 1 ] : null ),
			  ( args.length > 2 ? args[ 2 ] : null ) );
		db.close();
	    }

	} catch ( Exception except ) {
	    logger.println( except );
	    except.printStackTrace( logger );
	}
    }


    public static void odmgTest( Implementation odmg, Database db, PrintWriter logger )
        throws Exception
    {
	Product       product;
	ProductGroup  group;
	Computer      computer;
	OQLQuery      productOql;
	OQLQuery      groupOql;
	OQLQuery      computerOql;
	Transaction   tx;

	// Must be associated with an open transaction in order to
	// use the ODMG database
	tx = odmg.newTransaction();
	tx.begin();
	logger.println( "Begin transaction" );

	// Create OQL queries for all three object types
	productOql = odmg.newOQLQuery();
	productOql.create( "SELECT p FROM myapp.Product p WHERE id = $1" );
	groupOql = odmg.newOQLQuery();
	groupOql.create( "SELECT g FROM myapp.ProductGroup g WHERE id = $1" );
	computerOql = odmg.newOQLQuery();
	computerOql.create( "SELECT c FROM myapp.Computer c WHERE id = $1" );


	// Look up the group and if found in the database,
	// delete this object from the database
	groupOql.bind( new Integer( 3 ) );
	group = (ProductGroup) groupOql.execute();
	if ( group != null ) {
	    logger.println( "Deleting existing group: " + group );
	    db.deletePersistent( group );
	}
	
	// Look up the product and if found in the database,
	// delete this object from the database
	productOql.bind( new Integer( 4 ) );
	product = (Product) productOql.execute();
	if ( product != null ) {
	    logger.println( "Deleting existing product: " + product );
	    db.deletePersistent(  product );
	}
	
	// Look up the computer and if found in the database,
	// delete this object from the database
	computerOql.bind( new Integer( 6 ) );
	computer = (Computer) computerOql.execute();
	if ( computer != null ) {
	    logger.println( "Deleting existing computer: " + computer );
	    db.deletePersistent( computer );
	}


	// Checkpoint commits all the updates to the database
	// but leaves the transaction (and locks) open
	logger.println( "Transaction checkpoint" );
	tx.checkpoint();

	
	// If no such group exists in the database, create a new
	// object and persist it
	groupOql.bind( new Integer( 3 ) );
	group = (ProductGroup) groupOql.execute();
	if ( group == null ) {
	    group = new ProductGroup();
	    group.id = 3;
	    group.name = "new group";
	    logger.println( "Creating new group: " + group );
	    db.makePersistent( group );
	} else {
	    logger.println( "Query result: " + group );
	}
	
	// If no such product exists in the database, create a new
	// object and persist it
	// Note: product uses group, so group object has to be
	//       created first, but can be persisted later
	productOql.bind( new Integer( 4 ) );
	product = (Product) productOql.execute();
	if ( product == null ) {
	    product = new Product();
	    product.id = 4;
	    product.name = "new product";
	    product.price = 55;
	    product.group = group;
	    product.inventory = new ProductInventory();
	    product.inventory.quantity = 50;
	    logger.println( "Creating new product: " + product );
	    db.makePersistent( product );
	} else {
	    logger.println( "Query result: " + product );
	}

	// If no such computer exists in the database, create a new
	// object and persist it
	// Note: computer uses group, so group object has to be
	//       created first, but can be persisted later
	computerOql.bind( new Integer( 6 ) );
	computer = (Computer) computerOql.execute();
	if ( computer == null ) {
	    computer = new Computer();
	    computer.id = 6;
	    computer.cpu = "Pentium";
	    computer.name = "new product";
	    computer.price = 300;
	    computer.group = group;
	    computer.inventory = new ProductInventory();
	    computer.inventory.quantity = 60;
	    logger.println( "Creating new computer: " + computer );
	    db.makePersistent( computer );
	} else {
	    logger.println( "Query result: " + computer );
	}


	logger.println( "Commit transaction" );
	tx.commit();
    }


    static void perfTest( Implementation odmg, Database db, PrintWriter logger,
			  String driverClass, String jdbcUri )
	throws Exception
    {
	Timing               timing;
	PreparedStatement    stmt;
	ResultSet            rs;
	int                  i, j;
	Product              product = null;
	ProductGroup         group;
	Connection           conn;
	Transaction          tx;
	OQLQuery             oql;

	// Use the command line arguments to determine the JDBC driver
	// and JDBC URL and open up a connection to the database
	if ( driverClass == null )
	    driverClass = "postgresql.Driver";
	logger.println( "Using JDBC driver " + driverClass );
	Class.forName( driverClass );
	if ( jdbcUri == null )
	    jdbcUri = "jdbc:postgresql:test?user=test&password=test";
	logger.println( "Using JDBC URI " + jdbcUri );
	conn = DriverManager.getConnection( jdbcUri );

	// Must be inside an ODMG transaction in order to create the
	// product and group we're about to retrieve
	tx = odmg.newTransaction();
	tx.begin();

	// If product or group are not found in the database,
	// create new objects and persist them. 
	oql = odmg.newOQLQuery();
	oql.create( "SELECT g FROM myapp.ProductGroup g WHERE id = $1" );
	oql.bind( new Integer( 3 ) );
	group = (ProductGroup) oql.execute();
	if ( group == null ) {
	    group = new ProductGroup();
	    group.id = 3;
	    group.name = "new group";
	    logger.println( "Creating new group: " + group );
	    db.makePersistent( group );
	}
	oql.create( "SELECT p FROM myapp.Product p WHERE id = $1" );
	oql.bind( new Integer( 4 ) );
	product = (Product) oql.execute();
	if ( product == null ) {
	    product = new Product();
	    product.id = 4;
	    product.name = "new product";
	    product.price = 55;
	    product.group = group;
	    product.inventory = new ProductInventory();
	    product.inventory.quantity = 50;
	    logger.println( "Creating new product: " + product );
	    db.makePersistent( product );
	}
	// Commit the transaction so we can now retrieve these
	// objects
	tx.commit();

	// JDBC retrieval of product object using shortest query
	// with inner joins performed 2000 time in a loop.
	// At the end the elapsed time and per/minute will be printed.
	timing = new Timing( "Query using direct JDBC" );
	timing.start();
	product = new Product();
	product.id = 4;
	product.group = new ProductGroup();
	for ( i = 0 ; i < 2000 ; ++i ) {
	    stmt = conn.prepareStatement( "SELECT prod.price,prod.name,prod_group.id,prod_group.name,prod_inv.quant " +
					  "FROM prod,prod_group,prod_inv WHERE prod.id=? AND " +
					  "prod.group_id=prod_group.id AND prod.id=prod_inv.prod_id" );
	    stmt.setInt( 1, product.id );
	    rs = stmt.executeQuery();
	    while ( rs.next() ) {
		product.price = rs.getDouble( 1 );
		product.name = rs.getString( 2 );
		product.group.id = rs.getInt( 3 );
		product.group.name = rs.getString( 4 );
		product.inventory = new ProductInventory();
		product.inventory.quantity = rs.getInt( 5 );
		product.inventory.product = product;
	    }
	    rs.close();
	    stmt.close();
	}
	timing.count( i );
	timing.stop();
	logger.println( timing.report() );
	logger.println( "Query result: " + product );

	// ODMG retrieval of product object using OQL query
	// with inner joins performed 2000 time in a loop.
	// At the end the elapsed time and per/minute will be printed.
	tx.begin();
	timing = new Timing( "Query using O/R framework" );
	timing.start();
	for ( i = 0 ; i < 2000 ; ++i ) {
	    // Specify the primary key id
	    oql.bind( new Integer( 4 ) );
	    // Retrieve
	    product = (Product) oql.execute();
	}
	timing.count( i );
	timing.stop();
	logger.println( timing.report() );
	logger.println( "Query result: " + product );
	tx.abort();
    }


}


