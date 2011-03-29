package org.castor.cpa.test.test2861;

import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPAThreadedTestCase;
import org.castor.cpa.test.framework.CPAThreadedTestRunnable;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestReferenceChange extends CPAThreadedTestCase {
    private static final String DBNAME = "test2861";
    private static final String MAPPING = "/org/castor/cpa/test/test2861/mapping.xml";
    private static final String MAPPING_RO = "/org/castor/cpa/test/test2861/mappingReadOnly.xml";
    private static final String DBNAME_RO = "test2861ReadOnly";

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestReferenceChange.class.getName());

        suite.addTest(new TestReferenceChange("createObjects"));
        suite.addTest(new TestReferenceChange("testCreateInvoice"));
        suite.addTest(new TestReferenceChange("testQuerySameInvoiceSubsequent"));
        suite.addTest(new TestReferenceChange("testQuerySameInvoiceMT"));
        suite.addTest(new TestReferenceChange("testQuerySameInvoiceReadOnlyMT"));
        
        return suite;
    }

    public TestReferenceChange(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SAPDB);
    }
    
    public void createObjects() throws Exception {
        LegalPerson acme = new LegalPerson();
        acme.setOid("AAAAacme");
        acme.setName("ACME INC.");
        acme.setFederalTaxNumber("1");
        
        NaturalPerson marvin = new NaturalPerson();
        marvin.setOid("AAmarvin");
        marvin.setName("MARVIN ACME");
        marvin.setSocialSecurityNumber("2");
        marvin.setCreator(acme);
        
        NaturalPerson baby = new NaturalPerson();
        baby.setOid("AAAAbaby");
        baby.setName("BABY HERMAN");
        baby.setSocialSecurityNumber("3");
        baby.setCreator(acme);
        
        NaturalPerson judge = new NaturalPerson();
        judge.setOid("AAAjudge");
        judge.setName("JUDGE DOOM");
        judge.setSocialSecurityNumber("4");
        judge.setCreator(acme);
        
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setOid("AAAAMT01");
        motorcycle.setChassisNumber("CHASSIS00001");
        motorcycle.setHolder(marvin);
        motorcycle.setReseller(acme);
        
        Motorcycle motorcycle2 = new Motorcycle();
        motorcycle2.setOid("AAAAMT02");
        motorcycle2.setChassisNumber("CHASSIS00002");
        motorcycle2.setHolder(acme);
        motorcycle2.setReseller(acme);
        
        Motorcycle motorcycle3 = new Motorcycle();
        motorcycle3.setOid("AAAAMT03");
        motorcycle3.setChassisNumber("CHASSIS00003");
        motorcycle3.setHolder(acme);
        motorcycle3.setReseller(acme);
        
        Parameter invoiceSeq = new Parameter();
        invoiceSeq.setOid("AAAINVSQ");
        invoiceSeq.setIdSys("INVSEQ");
        invoiceSeq.setIntValue(new Integer(0));
        invoiceSeq.setPerson(acme);
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(false);
        db.begin();
        
        db.create(acme);
        db.create(marvin);
        db.create(baby);
        db.create(judge);
        db.create(motorcycle);
        db.create(motorcycle2);
        db.create(motorcycle3);
        db.create(invoiceSeq);
        
        db.commit();
        db.close();
    }
    
    public void testCreateInvoice() throws Exception {
        testCreateInvoice1();
        testCreateInvoice2();
        testCreateInvoice3();
    }
    
    public void testCreateInvoice1() throws Exception {
        LegalPerson acme = null;
        NaturalPerson marvin = null;
        Motorcycle motorcycle = null;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(false);
        db.begin();
        acme = db.load(LegalPerson.class, "AAAAacme");
        marvin = db.load(NaturalPerson.class, "AAmarvin");
        motorcycle = db.load(Motorcycle.class, "AAAAMT01");
        db.commit();
        
        Vector<InvoiceItem> vInvoiceItem = new Vector<InvoiceItem>();
        Invoice invoice = new Invoice();
        invoice.setOid("AAAINV01");
        invoice.setEmitter(marvin);
        invoice.setBillTo(acme);
        invoice.setInvoiceItem(vInvoiceItem);
        
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setOid("AINVIT01");
        invoiceItem.setInvoice(invoice);
        invoiceItem.setProduct(motorcycle);
        invoiceItem.setQuantity(new Integer(1));
        invoiceItem.setPrice(new Double(12000));
        invoiceItem.setTotal(new Double(12000));
        vInvoiceItem.add(invoiceItem);
        
        motorcycle.setHolder(acme);
        
        db.setAutoStore(false);
        db.begin();
        
        OQLQuery oql = db.getOQLQuery(
                "SELECT obj FROM " + Parameter.class.getName() + " obj "
                + "WHERE person.oid = $1 AND idSys = $2");
        oql.bind(acme.getOid());
        oql.bind("INVSEQ");
        QueryResults results = oql.execute();

        Parameter invoiceSeq = null;
        if (results.hasMore()) {
            invoiceSeq = (Parameter) results.nextElement();
        }
        int newInvoiceSeq = invoiceSeq.getIntValue().intValue() + 1;
        invoiceSeq.setIntValue(new Integer(newInvoiceSeq));
        invoice.setNumber(invoiceSeq.getIntValue());
        
        oql.close();
        results.close();
        
        db.update(motorcycle);
        db.create(invoice);
        
        db.commit();
        
        db.begin();
        motorcycle = db.load(Motorcycle.class, "AAAAMT01");
        db.commit();
        
        assertEquals(motorcycle.getHolder().getName(), acme.getName());
        
        db.close();
    }
    
    public void testCreateInvoice2() throws Exception {
        LegalPerson acme = null;
        NaturalPerson baby = null;
        Motorcycle motorcycle = null;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(false);
        db.begin();
        acme = db.load(LegalPerson.class, "AAAAacme");
        baby = db.load(NaturalPerson.class, "AAAAbaby");
        motorcycle = db.load(Motorcycle.class, "AAAAMT02");
        db.commit();
        
        Vector<InvoiceItem> vInvoiceItem = new Vector<InvoiceItem>();
        Invoice invoice = new Invoice();
        invoice.setOid("AAAINV02");
        invoice.setEmitter(acme);
        invoice.setBillTo(baby);
        invoice.setInvoiceItem(vInvoiceItem);
        
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setOid("AINVIT02");
        invoiceItem.setInvoice(invoice);
        invoiceItem.setProduct(motorcycle);
        invoiceItem.setQuantity(new Integer(1));
        invoiceItem.setPrice(new Double(13000));
        invoiceItem.setTotal(new Double(13000));
        vInvoiceItem.add(invoiceItem);
        
        motorcycle.setHolder(baby);
        
        db.setAutoStore(false);
        db.begin();
        
        OQLQuery oql = db.getOQLQuery(
                "SELECT obj FROM " + Parameter.class.getName() + " obj "
                + "WHERE person.oid = $1 AND idSys = $2");
        oql.bind(acme.getOid());
        oql.bind("INVSEQ");
        QueryResults results = oql.execute();

        Parameter invoiceSeq = null;
        if (results.hasMore()) {
            invoiceSeq = (Parameter) results.nextElement();
        }
        int newInvoiceSeq = invoiceSeq.getIntValue().intValue() + 1;
        invoiceSeq.setIntValue(new Integer(newInvoiceSeq));
        invoice.setNumber(invoiceSeq.getIntValue());
        
        oql.close();
        results.close();
        
        db.update(motorcycle);
        db.create(invoice);
        
        db.commit();
        
        db.begin();
        motorcycle = db.load(Motorcycle.class, "AAAAMT02");
        db.commit();
        
        assertEquals(motorcycle.getHolder().getName(), baby.getName());
        
        db.close();
    }
    
    public void testCreateInvoice3() throws Exception {
        LegalPerson acme = null;
        NaturalPerson judge = null;
        Motorcycle motorcycle = null;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(false);
        db.begin();
        acme = db.load(LegalPerson.class, "AAAAacme");
        judge = db.load(NaturalPerson.class, "AAAjudge");
        motorcycle = db.load(Motorcycle.class, "AAAAMT03");
        db.commit();
        
        Vector<InvoiceItem> vInvoiceItem = new Vector<InvoiceItem>();
        Invoice invoice = new Invoice();
        invoice.setOid("AAAINV03");
        invoice.setEmitter(acme);
        invoice.setBillTo(judge);
        invoice.setInvoiceItem(vInvoiceItem);
        
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setOid("AINVIT03");
        invoiceItem.setInvoice(invoice);
        invoiceItem.setProduct(motorcycle);
        invoiceItem.setQuantity(new Integer(1));
        invoiceItem.setPrice(new Double(23000));
        invoiceItem.setTotal(new Double(23000));
        vInvoiceItem.add(invoiceItem);
        
        motorcycle.setHolder(judge);
        
        db.setAutoStore(false);
        db.begin();
        
        OQLQuery oql = db.getOQLQuery(
                "SELECT obj FROM " + Parameter.class.getName() + " obj "
                + "WHERE person.oid = $1 AND idSys = $2");
        oql.bind(acme.getOid());
        oql.bind("INVSEQ");
        QueryResults results = oql.execute();

        Parameter invoiceSeq = null;
        if (results.hasMore()) {
            invoiceSeq = (Parameter) results.nextElement();
        }
        int newInvoiceSeq = invoiceSeq.getIntValue().intValue() + 1;
        invoiceSeq.setIntValue(new Integer(newInvoiceSeq));
        invoice.setNumber(invoiceSeq.getIntValue());
        
        oql.close();
        results.close();
        
        db.update(motorcycle);
        db.create(invoice);
        
        db.commit();
        
        db.begin();
        motorcycle = db.load(Motorcycle.class, "AAAAMT03");
        db.commit();
        
        assertEquals(motorcycle.getHolder().getName(), judge.getName());
        
        db.close();
    }
    
    public void testQuerySameInvoiceSubsequent() throws Exception {
        // first try
        executeQuery();
        
        // second try
        executeQuery();
        
        // third try
        executeQuery();
    }
    
    public void testQuerySameInvoiceMT() throws Exception {
        // initialize JDOManager once before starting threads.
        getJDOManager(DBNAME, MAPPING).getDatabase();
        
        CPAThreadedTestRunnable[] tcr = new CPAThreadedTestRunnable[10];
        for (int i = 0; i < tcr.length; i++) {
            tcr[i] = new TestReferenceRunnable(this, false);
        }
        runTestRunnables(tcr);
    }
    
    protected void executeQuery() throws Exception {
        Invoice invoice = null;

        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        String oql = "SELECT obj FROM " + Invoice.class.getName() + " obj WHERE oid = $1 ";
        OQLQuery query = db.getOQLQuery(oql);
        query.bind("AAAINV01");
        QueryResults results = query.execute();
        if (results.hasMore()) {
            invoice = (Invoice) results.nextElement();
        }
        results.close();
        query.close();
        
        db.commit();
        db.close();
        
        assertTrue(invoice != null);
    }
    
    public void testQuerySameInvoiceReadOnlyMT() throws Exception {
        // initialize JDOManager once before starting threads.
        getJDOManager(DBNAME_RO, MAPPING_RO).getDatabase();
        
        CPAThreadedTestRunnable[] tcr = new CPAThreadedTestRunnable[10];
        for (int i = 0; i < tcr.length; i++) {
            tcr[i] = new TestReferenceRunnable(this, true);
        }
        runTestRunnables(tcr);
    }
    
    protected void executeQueryReadOnly() throws Exception {
        Invoice invoice = null;

        Database db = getJDOManager(DBNAME_RO, MAPPING_RO).getDatabase();
        db.begin();
        
        String oql = "SELECT obj FROM " + Invoice.class.getName() + " obj "
                   + "WHERE emitter = $1 and invoiceItem.quantity > $2";
        OQLQuery query = db.getOQLQuery(oql);
        query.bind("AAAAacme");
        query.bind(new Integer(0));
        QueryResults results = query.execute(Database.READONLY);
        int invoiceCount = 0;
        while (results.hasMore()) {
            invoice = (Invoice) results.nextElement();
            invoiceCount++;
        }
        results.close();
        query.close();
        
        db.commit();
        db.close();
        
        assertTrue(invoice != null);
        assertTrue(invoiceCount == 2);
    }
}
