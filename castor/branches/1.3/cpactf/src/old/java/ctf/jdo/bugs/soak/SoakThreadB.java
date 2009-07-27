package ctf.jdo.bugs.soak;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

import ctf.jdo.bugs.soak.model.Employee;

public final class SoakThreadB extends AbstractSoakThread {
    public SoakThreadB(final JDOManager manager) {
        super(manager);
    }

    public void test(final Database db) throws Exception {
        db.begin();
        String s = "SELECT e FROM " + Employee.class.getName() + " e "
                 + "WHERE e.holiday > $1";
        OQLQuery qry = db.getOQLQuery(s);
        qry.bind(50.0);
        QueryResults rst = qry.execute();
        while (rst.hasMore()) {
            Employee emp = (Employee) rst.next();
            emp.setHoliday(50.0f);
        }
        rst.close();
        qry.close();
        db.commit();
    }
}
