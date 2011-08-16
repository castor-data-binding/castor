package org.castor.cache.hashbelt.multithreaded;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;

/**
 * Test multithreaded hashbelt cache.
 */
public abstract class AbstractTestMultiThreadedHashbelt extends ThreadedTestCase {
    
    /** The cache */
    private Cache<String, String> _cache = null;
    
    /** The java object to be synchronized on */
    private static Object _lock = new Object();
    
    /** The time a transaction sleep and wait for another transaction to process. */
    private static final long WAIT = 200;
    
    /** The Exception */
    private Throwable _firstProblem = null;
    
    /** The Exception */
    private Throwable _secondProblem = null;

    public AbstractTestMultiThreadedHashbelt(final String name) {
        super(name);
    }

    protected abstract Cache<String, String> initialize() throws CacheAcquireException;
    
    private void runMultiThreadedTest (final Runnable[] runnables) {
       runTestRunnables(runnables);
        
        if (_firstProblem != null) {
            fail(_firstProblem.getMessage());
        }
        if (_secondProblem != null) {
            fail(_secondProblem.getLocalizedMessage());
        }
    }
    
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        
        _cache = initialize();
        _firstProblem = null;
        _secondProblem = null;
    }
    
    @Override
    protected final void tearDown() throws Exception {
        super.tearDown();
        
        _cache.clear();
        _cache = null;
        _firstProblem = null;
        _secondProblem = null;
    }
    
    public final void testPutThenGet() {
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable get = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertEquals("first value", _cache.get("first key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, get});
    }
    
    public final void testGetThenPut() {
        Runnable get = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.get("first key"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable put = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertNull(_cache.put("first key", "first value"));
                    assertEquals("first value", _cache.get("first key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {get, put});
    }
    
    public final void testContainsKey() {
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable containsKey = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertTrue(_cache.containsKey("first key"));
                    assertTrue(_cache.containsKey("second key"));
                    assertTrue(_cache.containsKey("third key"));
                    assertFalse(_cache.containsKey("fourth key"));
                    assertFalse(_cache.containsKey("fifth key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, containsKey});
    }
    
    public final void testContainsValue() {
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable containsValue = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertTrue(_cache.containsValue("first value"));
                    assertTrue(_cache.containsValue("second value"));
                    assertTrue(_cache.containsValue("third value"));
                    assertFalse(_cache.containsValue("fourth value"));
                    assertFalse(_cache.containsValue("fifth value"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, containsValue});
    }

    public final void testClear() {
        Runnable clear = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    _cache.clear();
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable check = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertFalse(_cache.containsKey("first key"));
                    assertFalse(_cache.containsKey("second key"));
                    assertFalse(_cache.containsKey("third key"));
                    assertFalse(_cache.containsKey("fourth key"));
                    assertFalse(_cache.containsKey("fifth key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {clear, check});
    }

    public final void testSize() {
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable size = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertEquals(3, _cache.size());
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, size});
    }

    public final void testIsEmpty() {
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    assertFalse(_cache.isEmpty());
                    _cache.clear();
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable isEmpty = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertTrue(_cache.isEmpty());
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, isEmpty});
    }

    public final void testRemove() {
        Runnable remove = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    assertEquals("third value", _cache.remove("third key"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable check = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertTrue(_cache.containsKey("first key"));
                    assertTrue(_cache.containsKey("second key"));
                    assertFalse(_cache.containsKey("third key"));
                    assertFalse(_cache.containsKey("fourth key"));
                    assertFalse(_cache.containsKey("fifth key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {remove, check});
    }

    public final void testPutAll() {
        Runnable putAll = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("first key", "first value");
                    map.put("second key", "second value");
                    _cache.putAll(map);
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable check = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    assertTrue(_cache.containsKey("first key"));
                    assertTrue(_cache.containsKey("second key"));
                    assertFalse(_cache.containsKey("third key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {putAll, check});
    }

    public final void testKeySet() {
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable keySet = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    Set<String> set = _cache.keySet();
                    assertEquals(3, set.size());
                    assertTrue(set.contains("first key"));
                    assertTrue(set.contains("second key"));
                    assertTrue(set.contains("third key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, keySet});
    }

    public final void testValues() {
        
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable values = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    Collection<String> col = _cache.values();
                    assertEquals(3, col.size());
                    assertTrue(col.contains("first value"));
                    assertTrue(col.contains("second value"));
                    assertTrue(col.contains("third value"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, values});
    }

    public final void testEntrySet() {  
        Runnable put = new Runnable () {
            public void run() {
                try {
                    Thread.sleep(WAIT);
                    assertNull(_cache.put("first key", "first value"));
                    assertNull(_cache.put("second key", "second value"));
                    assertNull(_cache.put("third key", "third value"));
                    synchronized (_lock) {
                        _lock.notify();
                    }
                } catch (Throwable e) {
                    _firstProblem = e;
                }
            }
        };
        
        Runnable entrySet = new Runnable () {
            public void run() {
                try {
                    synchronized (_lock) {
                        _lock.wait();
                    }
                    Set<Map.Entry<String, String>> set = _cache.entrySet();
                    
                    assertEquals(3, set.size());
                    
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (Map.Entry<String, String> entry : set) {
                        map.put(entry.getKey(), entry.getValue());
                    }

                    assertTrue(map.containsKey("first key"));
                    assertEquals("first value", map.get("first key"));

                    assertTrue(map.containsKey("second key"));
                    assertEquals("second value", map.get("second key"));

                    assertTrue(map.containsKey("third key"));
                    assertEquals("third value", map.get("third key"));
                } catch (Throwable e) {
                    _secondProblem = e;
                }
            }
        };
        
        runMultiThreadedTest(new Runnable[] {put, entrySet});
    }
}
