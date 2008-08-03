package ctf.jdo.bugs.soak;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;

public abstract class AbstractSoakThread extends Thread {
    private final Object _lock = new Object();
    private final JDOManager _manager;
    private boolean _canceled;

    public AbstractSoakThread(final JDOManager manager) {
        _manager = manager;
    }

    public final void run() {
        Database db = null;
        
        try {
            db = _manager.getDatabase();

            while (true) {
                synchronized (_lock) {
                    if (_canceled) {
                        return;
                    }
                }

                test(db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception ee) {
                }
            }
        }
    }

    public final void cancel() {
        synchronized (_lock) {
            _canceled = true;
        }
    }

    public abstract void test(final Database db) throws Exception;
}
