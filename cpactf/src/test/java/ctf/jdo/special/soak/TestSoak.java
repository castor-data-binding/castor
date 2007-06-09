package ctf.jdo.special.soak;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

import ctf.jdo.special.soak.model.Address;
import ctf.jdo.special.soak.model.Employee;
import ctf.jdo.special.soak.model.Phone;

public abstract class Test extends Thread {

    public static final String DATABASE_FILE = "/cvs/one/castor/src/tests/soak/database.xml";

    protected Thread      main;

    protected Object      lock        = new Object();

    protected boolean     stopped;

    protected Database    db;

    public static void main( String args[] )
            throws Exception {

        long startTime = System.currentTimeMillis();

        Test testA = new ThreadA( Thread.currentThread() );

        Test testB = new ThreadB( Thread.currentThread() );

        testA.start();
        testB.start();

        System.out.println( "Type 'q' and 'enter' to terminate the test!" );

        //BufferedReader r = new BufferedReader( new InputStreamReader( System.in ) );
        InputStream r = System.in;

        int ch = 0;
        while ( testA.isAlive() && testB.isAlive() ) {
            if ( r.available() > 0 )
                Thread.sleep(100);
            else if ((ch=r.read()) == -1 || ch == 'q')
                break;
        }

        testA.stopTest();
        testB.stopTest();

        System.out.print( "Stopping test ." );

        while ( testA.isAlive() ) {
            System.out.print( "." );
            testA.join( 100 );
        }

        while ( testB.isAlive() ) {
            System.out.print( "." );
            testB.join( 100 );
        }

        System.out.println( "Done!" );
        System.out.println( "Test duration: " + (System.currentTimeMillis() - startTime)/1000 + " seconds." );
    }


    public Test( Thread t ) {
        main = t;
    }


    public void setUp() throws Exception {
        JDO jdo = new JDO();
        jdo.setConfiguration(DATABASE_FILE);
        jdo.setDatabaseName( "test" );
        db = jdo.getDatabase();
    }


    public void stopTest() {
        synchronized( lock ) {
            stopped = true;
        }
    }


    public void run() {

        try {
            setUp();

            while ( true ) {
                synchronized( lock ) {
                    if ( stopped )
                        return;
                }

                test();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            if ( db != null ) {
                try {
                    db.close();
                } catch ( Exception ee ) {
                }
            }
        }
    }

    public abstract void test() throws Exception;

}

class ThreadB extends Test {

    public ThreadB( Thread t ) {
        super(t);
    }

    public void test() throws Exception {
        Employee emp;

        db.begin();
        OQLQuery q = db.getOQLQuery("SELECT e FROM soak.model.Employee e WHERE e.holiday > $1");
        q.bind( 50.0 );
        QueryResults r = q.execute();
        while ( r.hasMore() ) {
            emp = (Employee) r.next();
            emp.holiday = 50.0f;
        }
        db.commit();
        r.close();
        q.close();
    }
}

class ThreadA extends Test {

    public ThreadA( Thread t ) {
        super(t);
    }

    public void test() throws Exception {

        System.out.println( "New run" );
        Employee e, f, g, h, i;
        Integer  eid, fid, gid, hid, iid;

        // create
        e = new Employee();
        e.firstName         = "First";
        e.lastName          = "Employee";
        e.middleNameInitial = 'F';
        e.gender            = 'M';
        e.isPerm            = true;
        e.hourlyRate        = new BigDecimal(5);
        e.totalHours        = new BigDecimal(1000);
        e.SSN               = "000000001";
        e.holiday           = 0;
        e.startDate         = Calendar.getInstance().getTime();
        e.recordDate        = e.startDate;
        e.roomNumber        = 901;
        e.homeAddress       = new Address( "1", "North Pole", null, "Northest City", "NT", "Canada", "H1H 1H1", null );
        e.homePhone         = new Phone( 1, 111, 1111111l );
        e.skills            = new Vector(); e.skills.add( "Deer riding"); 
                              e.skills.add("Wall Claiming"); e.skills.add("Gift Wrapping");

        f = new Employee();
        f.firstName         = "Second";
        f.lastName          = "Employee";
        f.middleNameInitial = 'S';
        f.gender            = 'M';
        f.isPerm            = true;
        f.hourlyRate        = new BigDecimal(30);
        f.totalHours        = new BigDecimal(10);
        f.SSN               = "000000002";
        f.holiday           = 0;
        f.startDate         = Calendar.getInstance().getTime();
        f.recordDate        = f.startDate;
        f.roomNumber        = 902;
        f.homeAddress       = new Address( "2", "South Pole", null, "Far South", "50471", "Nowhere", "000111", null );
        f.homePhone         = new Phone( 123, 111, 1111111l );
        f.skills            = new Vector(); f.skills.add("Polar beer hunting"); f.skills.add("Pengine riding" );

        g = new Employee();
        g.firstName         = "Adam";
        g.lastName          = "None";
        g.middleNameInitial = 0;
        g.gender            = 'M';
        g.isPerm            = true;
        g.hourlyRate        = new BigDecimal(1000);
        g.totalHours        = new BigDecimal(10);
        g.SSN               = "000000003";
        g.holiday           = 0;
        g.startDate         = Calendar.getInstance().getTime();
        g.recordDate        = g.startDate;
        g.roomNumber        = 902;
        g.homeAddress       = new Address( "3", "Edin Road", null, "Edin", "", "Unknown", "1A1B2B", null );
        g.homePhone         = new Phone( 77, 111, 1111111l );
        g.skills            = new Vector(); g.skills.add("Animal Naming"); g.skills.add("Ganden Caring");

        h = new Employee();
        h.firstName         = "Eve";
        h.lastName          = "None";
        h.middleNameInitial = 0;
        h.gender            = 'F';
        h.isPerm            = true;
        h.hourlyRate        = new BigDecimal(1000);
        h.totalHours        = new BigDecimal(10);
        h.SSN               = "000000004";
        h.holiday           = 0;
        h.startDate         = Calendar.getInstance().getTime();
        h.recordDate        = h.startDate;
        h.roomNumber        = 902;
        h.homeAddress       = new Address( "3", "Edin Road", null, "Edin", "", "Unknown", "1A1B2B", null );
        h.homePhone         = new Phone( 77, 111, 1111111l );
        h.skills            = new Vector(); h.skills.add("Helping");

        i = new Employee();
        i.firstName         = "Rapheal";
        i.lastName          = "Angel";
        i.middleNameInitial = 0;
        i.gender            = '?';
        i.isPerm            = true;
        i.hourlyRate        = new BigDecimal(1000);
        i.totalHours        = new BigDecimal(10);
        i.SSN               = "000000005";
        i.holiday           = 0;
        i.startDate         = Calendar.getInstance().getTime();
        i.recordDate        = i.startDate;
        i.roomNumber        = 902;
        i.homeAddress       = new Address( "10", "Messager Road", null, "", "", "Heaven", "IIIIII", null );
        i.homePhone         = new Phone( 7, 111, 1111111l );
        i.skills            = new Vector(); i.skills.add("Flying"); i.skills.add("Messaging");

        
        db.begin();
        db.create( e );
        db.create( f );
        db.create( g );
        db.create( h );
        db.create( i );
        db.commit();

        eid = e.id;
        fid = f.id;
        gid = g.id;
        hid = h.id;
        iid = i.id;

        // update
        db.begin();
        e = (Employee) db.load( Employee.class, eid );
        f = (Employee) db.load( Employee.class, fid );
        g = (Employee) db.load( Employee.class, gid );

        BigDecimal rate = new BigDecimal( 1.2 );
        e.hourlyRate.multiply( rate );
        e.holiday += 10;
        e.skills.add( "Coffee making" );

        f.hourlyRate.multiply( rate );
        f.holiday += 10;
        f.skills.add( "Pengine traning" );

        g.hourlyRate.multiply( rate );
        g.holiday += 10;
        g.skills.add( "farming" );
        db.commit();

        // update
        db.begin();
        e = (Employee) db.load( Employee.class, eid );
        f = (Employee) db.load( Employee.class, fid );
        g = (Employee) db.load( Employee.class, gid );

        e.hourlyRate.multiply( rate );
        e.holiday += 10;
        e.skills.add( "Singing" );

        f.hourlyRate.multiply( rate );
        f.holiday += 10;
        f.skills.removeAllElements();

        g.hourlyRate.multiply( rate );
        g.holiday += 10;
        db.commit();

        // update
        db.begin();
        e = (Employee) db.load( Employee.class, eid );
        f = (Employee) db.load( Employee.class, fid );
        g = (Employee) db.load( Employee.class, gid );
        e.holiday += 15;
        f.holiday += 15;
        g.holiday += 15;
        db.commit();

        // update
        db.begin();
        e = (Employee) db.load( Employee.class, eid );
        f = (Employee) db.load( Employee.class, fid );
        g = (Employee) db.load( Employee.class, gid );
        e.holiday += 15;
        f.holiday += 15;
        g.holiday += 15;
        db.commit();

        // update
        db.begin();
        BigDecimal borderLine = new BigDecimal( 10 );
        BigDecimal reducedRate = new BigDecimal( 0.8 );
        OQLQuery q = db.getOQLQuery( "SELECT e FROM soak.model.Employee e WHERE e.hourlyRate >= $1" );
        q.bind( borderLine );
        QueryResults qr = q.execute();
        while ( qr.hasMore() ) {
            Employee overPriced = (Employee) qr.next();
            overPriced.hourlyRate.multiply( reducedRate );
        }
        db.commit();
        qr.close();
        q.close();

        // remove
        db.begin();
        e = (Employee) db.load( Employee.class, eid );
        f = (Employee) db.load( Employee.class, fid );
        g = (Employee) db.load( Employee.class, gid );
        h = (Employee) db.load( Employee.class, hid );
        i = (Employee) db.load( Employee.class, iid );

        db.remove( e );
        db.remove( f );
        db.remove( g );
        db.remove( h );
        db.remove( i );
        db.commit();

    }
}

