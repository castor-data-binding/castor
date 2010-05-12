package org.castor.util;

import org.castor.core.util.CycleBreaker;
import org.junit.Assert;
import org.junit.Test;

public class CycleBreakerTest {

    @Test
    public void testMemLeak() {
        long startfreeMemory = Runtime.getRuntime().freeMemory();
        for (int i = 1; i < 1000; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    CycleBreaker.startingToCycle(this);
                    CycleBreaker.releaseCycleHandle(this);
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }
        long endfreeMemory = Runtime.getRuntime().freeMemory();

        // Check if memory usage was higher than 1MB 
        Assert.assertFalse("Memory Leak", (startfreeMemory - endfreeMemory) / (1024 * 1024) > 1);
    }
}
