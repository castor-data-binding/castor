package jdo.tc167;

import jdo.JDOCategory;
import harness.CastorTestCase;
import harness.TestHarness;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public class TestEnum extends CastorTestCase  {
    private JDOCategory     _category;

    public TestEnum( TestHarness category ) {
        super( category, "tempTC167", "TestEnum" );
        _category = (JDOCategory) category;
    }

    public TestEnum( TestHarness category, String name, String description ) {
        super( category, name, description );
        _category = (JDOCategory) category;
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void runTest() throws Exception {
        testCreateLoadUpdateDelete();
        testQuery();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateLoadUpdateDelete() throws Exception {
        Database database;
        
        // create product
        database = _category.getDatabase();
        database.begin();
        
        Product pc = new Product(1, "LCD", KindEnum.MONITOR);
        database.create(pc);

        database.commit();
        database.close();

        // load created product
        database = _category.getDatabase();
        database.begin();
        
        Product pl1 = new Product(1, "LCD", KindEnum.MONITOR);
        Product pl2 = (Product)database.load(Product.class, new Integer(1));
        assertEquals(pl1, pl2);

        database.commit();
        database.close();

        // update product
        database = _category.getDatabase();
        database.begin();
        
        Product pu = (Product)database.load(Product.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindEnum.PRINTER);

        database.commit();
        database.close();

        // load updated product
        database = _category.getDatabase();
        database.begin();
        
        Product pl3 = new Product(1, "Laser", KindEnum.PRINTER);
        Product pl4 = (Product)database.load(Product.class, new Integer(1));
        assertEquals(pl3, pl4);

        database.commit();
        database.close();

        // delete product
        database = _category.getDatabase();
        database.begin();
        
        Product pd = (Product)database.load(Product.class, new Integer(1));
        database.remove(pd);

        database.commit();
        database.close();
    }

    public void testQuery() throws Exception {
        Database database;
        
        // create some products
        database = _category.getDatabase();
        database.begin();
        
        database.create(new Product(1, "LCD", KindEnum.MONITOR));
        database.create(new Product(2, "Laser", KindEnum.PRINTER));
        database.create(new Product(3, "Desktop", KindEnum.COMPUTER));
        database.create(new Product(4, "Notebook", KindEnum.COMPUTER));

        database.commit();
        database.close();

        // query and delete all product
        database = _category.getDatabase();
        database.begin();
        
        Product pq;
        OQLQuery query = database.getOQLQuery("select p from "
                + jdo.tc167.Product.class.getName() + " p order by p.id");
        QueryResults results = query.execute();
        
        pq = (Product) results.next();
        assertEquals(pq, new Product(1, "LCD", KindEnum.MONITOR));
        database.remove(pq);

        pq = (Product) results.next();
        assertEquals(pq, new Product(2, "Laser", KindEnum.PRINTER));
        database.remove(pq);
        
        pq = (Product) results.next();
        assertEquals(pq, new Product(3, "Desktop", KindEnum.COMPUTER));
        database.remove(pq);
        
        pq = (Product) results.next();
        assertEquals(pq, new Product(4, "Notebook", KindEnum.COMPUTER));
        database.remove(pq);
        
        assertFalse(results.hasMore());
        results.close();
        query.close();

        database.commit();
        database.close();
    }
}
