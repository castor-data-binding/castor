package ctf.jdo.bugs.soak;

import java.io.InputStream;

import org.exolab.castor.jdo.JDOManager;

public final class TestSoak {
    public static void main(final String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        String config = TestSoak.class.getResource("jdo-conf.xml").toString();
        JDOManager.loadConfiguration(config);
        JDOManager manager = JDOManager.createInstance("soak");

        AbstractSoakThread testA = new SoakThreadA(manager);
        AbstractSoakThread testB = new SoakThreadB(manager);

        testA.start();
        testB.start();

        System.out.println("Type 'q' and 'enter' to terminate the test!");

        InputStream r = System.in;
        int ch = 0;
        while (testA.isAlive() && testB.isAlive()) {
            if (r.available() > 0) {
                Thread.sleep(100);
            } else {
                ch = r.read();
                if ((ch == -1) || (ch == 'q')) {
                    break;
                }
            }
        }

        testA.cancel();
        testB.cancel();

        System.out.print("Stopping test .");

        while (testA.isAlive()) {
            System.out.print(".");
            testA.join(100);
        }

        while (testB.isAlive()) {
            System.out.print(".");
            testB.join(100);
        }

        System.out.println("Done!");
        System.out.println("Test duration: "
                + (System.currentTimeMillis() - startTime) / 1000 + " seconds.");
    }
    
    private TestSoak() { }
}
