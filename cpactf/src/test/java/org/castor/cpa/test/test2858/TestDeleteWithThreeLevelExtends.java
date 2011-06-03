package org.castor.cpa.test.test2858;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

public final class TestDeleteWithThreeLevelExtends extends CPATestCase {
    private static final String DBNAME = "test2858";
    private static final String MAPPING = "/org/castor/cpa/test/test2858/mapping.xml";

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestDeleteWithThreeLevelExtends.class.getName());

        suite.addTest(new TestDeleteWithThreeLevelExtends("createObjects"));
        suite.addTest(new TestDeleteWithThreeLevelExtends("testDeleteMoney"));
        
        return suite;
    }

    public TestDeleteWithThreeLevelExtends(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
//        return false;
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SAPDB);
    }
    
    public void createObjects() throws Exception {
        //InvoiceParcel
        InvoiceParcel invoiceParcel = new InvoiceParcel();
        invoiceParcel.setOid("AAAAIP01");
        invoiceParcel.setValue(new Double(100));
        invoiceParcel.setParcelNumber(new Integer(1));
        
        //TradeNote
        TradeNote tradeNote = new TradeNote();
        tradeNote.setOid("AAAATN01");
        tradeNote.setDate(new Date());
        tradeNote.setValue(new Double(100));
        tradeNote.setNoteNumber(new Integer(1));
        tradeNote.setBillCollectionType("DUPL");
        
        //Money that pay TradeNote
        Money money = new Money();
        money.setOid("AAAAMN01");
        money.setDate(tradeNote.getDate());
        money.setBankNoteValue(new Double(50));
        money.setQuantity(new Integer(2));
        money.calculate();
        money.setPaymentType("MONEY_US");
        
        //Create the Payment relation between TradeNote and Money
        Payment payment = new Payment();
        payment.setOid("AAAAPM01");
        tradeNote.addPayment(payment);
        money.addTitlePaid(payment);
        
        //Create the Relation that started the TradeNote
        FinanceTitleRelation financeTitleRelation = new FinanceTitleRelation();
        financeTitleRelation.setOid("AAAAFR01");
        financeTitleRelation.setMyFinanceTitle(tradeNote);
        
        invoiceParcel.addFinanceTitleRelation(financeTitleRelation);
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        db.create(invoiceParcel);
        db.create(tradeNote);
        db.create(money);
        db.create(payment);
        db.create(financeTitleRelation);
        
        db.commit();
        db.close();
    }
    
    public void testDeleteMoney() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        InvoiceParcel invoiceParcel = db.load(InvoiceParcel.class, "AAAAIP01");
        FinanceTitleRelation relation = invoiceParcel.getMyFinanceTitleRelation().elementAt(0);
        TradeNote tradeNote = (TradeNote) relation.getMyFinanceTitle();
        Payment payment = tradeNote.getMyPayments().firstElement();
        Money money = (Money) payment.getMyFinanceTitle();
        
        tradeNote.getMyPayments().clear();
        money.getMyTitlesPaid().clear();
        
        db.remove(invoiceParcel);
        db.remove(tradeNote);
        db.remove(relation);
        db.remove(payment);
        db.remove(money);
        
        db.commit();
        db.close();
    }
}
