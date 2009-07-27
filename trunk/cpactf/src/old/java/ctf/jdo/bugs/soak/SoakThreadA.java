package ctf.jdo.bugs.soak;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

import ctf.jdo.bugs.soak.model.Address;
import ctf.jdo.bugs.soak.model.Employee;
import ctf.jdo.bugs.soak.model.Phone;

public final class SoakThreadA extends AbstractSoakThread {
    public SoakThreadA(final JDOManager manager) {
        super(manager);
    }
    
    public void test(final Database db) throws Exception {
        System.out.println("New run");
        
        // create
        Employee e = initEmployeeFirst();
        Employee f = initEmployeeSecond();
        Employee g = initEmployeeAdam();
        Employee h = initEmployeeEve();
        Employee i = initEmployeeRapheal();
        
        db.begin();
        db.create(e);
        db.create(f);
        db.create(g);
        db.create(h);
        db.create(i);
        db.commit();

        Integer eid = e.getId();
        Integer fid = f.getId();
        Integer gid = g.getId();
        Integer hid = h.getId();
        Integer iid = i.getId();

        
        // update
        db.begin();
        e = (Employee) db.load(Employee.class, eid);
        f = (Employee) db.load(Employee.class, fid);
        g = (Employee) db.load(Employee.class, gid);

        BigDecimal rate = new BigDecimal(1.2);
        e.getHourlyRate().multiply(rate);
        e.setHoliday(e.getHoliday() + 10);
        e.getSkills().add("Coffee making");

        f.getHourlyRate().multiply(rate);
        f.setHoliday(f.getHoliday() + 10);
        f.getSkills().add("Pengine traning");

        g.getHourlyRate().multiply(rate);
        g.setHoliday(g.getHoliday() + 10);
        g.getSkills().add("farming");
        db.commit();

        
        // update
        db.begin();
        e = (Employee) db.load(Employee.class, eid);
        f = (Employee) db.load(Employee.class, fid);
        g = (Employee) db.load(Employee.class, gid);

        e.getHourlyRate().multiply(rate);
        e.setHoliday(e.getHoliday() + 10);
        e.getSkills().add("Singing");

        f.getHourlyRate().multiply(rate);
        f.setHoliday(f.getHoliday() + 10);
        f.getSkills().removeAllElements();

        g.getHourlyRate().multiply(rate);
        g.setHoliday(g.getHoliday() + 10);
        db.commit();

        
        // update
        db.begin();
        e = (Employee) db.load(Employee.class, eid);
        f = (Employee) db.load(Employee.class, fid);
        g = (Employee) db.load(Employee.class, gid);
        e.setHoliday(e.getHoliday() + 15);
        f.setHoliday(f.getHoliday() + 15);
        g.setHoliday(g.getHoliday() + 15);
        db.commit();

        
        // update
        db.begin();
        e = (Employee) db.load(Employee.class, eid);
        f = (Employee) db.load(Employee.class, fid);
        g = (Employee) db.load(Employee.class, gid);
        e.setHoliday(e.getHoliday() + 15);
        f.setHoliday(f.getHoliday() + 15);
        g.setHoliday(g.getHoliday() + 15);
        db.commit();

        
        // update
        db.begin();
        BigDecimal borderLine = new BigDecimal(10.0);
        BigDecimal reducedRate = new BigDecimal(0.8);
        String s = "SELECT e FROM " + Employee.class.getName() + " e "
                 + "WHERE e.hourlyRate >= $1";
        OQLQuery qry = db.getOQLQuery(s);
        qry.bind(borderLine);
        QueryResults rst = qry.execute();
        while (rst.hasMore()) {
            Employee overPriced = (Employee) rst.next();
            overPriced.getHourlyRate().multiply(reducedRate);
        }
        db.commit();
        rst.close();
        qry.close();

        
        // remove
        db.begin();
        e = (Employee) db.load(Employee.class, eid);
        f = (Employee) db.load(Employee.class, fid);
        g = (Employee) db.load(Employee.class, gid);
        h = (Employee) db.load(Employee.class, hid);
        i = (Employee) db.load(Employee.class, iid);
        db.remove(e);
        db.remove(f);
        db.remove(g);
        db.remove(h);
        db.remove(i);
        db.commit();
    }
    
    private Employee initEmployeeFirst() {
        Employee e = new Employee();
        
        e.setFirstName("First");
        e.setLastName("Employee");
        e.setMiddleNameInitial('F');
        e.setGender('M');
        e.setPerm(true);
        e.setHourlyRate(new BigDecimal(5));
        e.setTotalHours(new BigDecimal(1000));
        e.setSSN("000000001");
        e.setHoliday(0);
        e.setStartDate(Calendar.getInstance().getTime());
        e.setRecordDate(e.getStartDate());
        e.setRoomNumber(901);
        e.setHomeAddress(new Address("1", "North Pole", null, "Northest City", "NT",
                "Canada", "H1H 1H1"));
        e.setHomePhone(new Phone(1, 111, 1111111L));
        e.setSkills(new Vector());
        e.getSkills().add("Deer riding");
        e.getSkills().add("Wall Claiming");
        e.getSkills().add("Gift Wrapping");
        
        return e;
    }

    private Employee initEmployeeSecond() {
        Employee f = new Employee();
        
        f.setFirstName("Second");
        f.setLastName("Employee");
        f.setMiddleNameInitial('S');
        f.setGender('M');
        f.setPerm(true);
        f.setHourlyRate(new BigDecimal(30));
        f.setTotalHours(new BigDecimal(10));
        f.setSSN("000000002");
        f.setHoliday(0);
        f.setStartDate(Calendar.getInstance().getTime());
        f.setRecordDate(f.getStartDate());
        f.setRoomNumber(902);
        f.setHomeAddress(new Address("2", "South Pole", null, "Far South", "50471",
                "Nowhere", "000111"));
        f.setHomePhone(new Phone(123, 111, 1111111L));
        f.setSkills(new Vector());
        f.getSkills().add("Polar beer hunting");
        f.getSkills().add("Pengine riding");
        
        return f;
    }
        
    private Employee initEmployeeAdam() {
        Employee g = new Employee();
        
        g.setFirstName("Adam");
        g.setLastName("None");
        g.setMiddleNameInitial((char) 0);
        g.setGender('M');
        g.setPerm(true);
        g.setHourlyRate(new BigDecimal(1000));
        g.setTotalHours(new BigDecimal(10));
        g.setSSN("000000003");
        g.setHoliday(0);
        g.setStartDate(Calendar.getInstance().getTime());
        g.setRecordDate(g.getStartDate());
        g.setRoomNumber(902);
        g.setHomeAddress(new Address("3", "Edin Road", null, "Edin", "", "Unknown",
                "1A1B2B"));
        g.setHomePhone(new Phone(77, 111, 1111111L));
        g.setSkills(new Vector());
        g.getSkills().add("Animal Naming");
        g.getSkills().add("Ganden Caring");
        
        return g;
    }
    
    private Employee initEmployeeEve() {
        Employee h = new Employee();
        
        h.setFirstName("Eve");
        h.setLastName("None");
        h.setMiddleNameInitial((char) 0);
        h.setGender('F');
        h.setPerm(true);
        h.setHourlyRate(new BigDecimal(1000));
        h.setTotalHours(new BigDecimal(10));
        h.setSSN("000000004");
        h.setHoliday(0);
        h.setStartDate(Calendar.getInstance().getTime());
        h.setRecordDate(h.getStartDate());
        h.setRoomNumber(902);
        h.setHomeAddress(new Address("3", "Edin Road", null, "Edin", "", "Unknown",
                "1A1B2B"));
        h.setHomePhone(new Phone(77, 111, 1111111L));
        h.setSkills(new Vector());
        h.getSkills().add("Helping");
        
        return h;
    }
    
    private Employee initEmployeeRapheal() {
        Employee i = new Employee();
        
        i.setFirstName("Rapheal");
        i.setLastName("Angel");
        i.setMiddleNameInitial((char) 0);
        i.setGender('?');
        i.setPerm(true);
        i.setHourlyRate(new BigDecimal(1000));
        i.setTotalHours(new BigDecimal(10));
        i.setSSN("000000005");
        i.setHoliday(0);
        i.setStartDate(Calendar.getInstance().getTime());
        i.setRecordDate(i.getStartDate());
        i.setRoomNumber(902);
        i.setHomeAddress(new Address("10", "Messager Road", null, "", "", "Heaven",
                "IIIIII"));
        i.setHomePhone(new Phone(7, 111, 1111111L));
        i.setSkills(new Vector());
        i.getSkills().add("Flying");
        i.getSkills().add("Messaging");
        
        return i;
    }
}
