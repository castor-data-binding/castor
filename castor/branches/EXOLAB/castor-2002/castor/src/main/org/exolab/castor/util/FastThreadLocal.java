/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.util;


/**
 * This is an efficient implementation of {@link java.lang.ThreadLocal}
 * which uses a background thread to remove stale thread entries,
 * doing away with the inefficiencies of <tt>WeakHashMap</tt>. This
 * implementation does not support <tt>ThreadLocal.initialValue()</tt>.
 * <p>
 * <br>
 * The default ThreadLocal implementation in Sun's JDK 1.2 is implemented
 * very efficiently, if you count lines of code. But blunt reuse of
 * WeakHashMap doesn't lead to overly efficient use of CPU power.
 * <p>
 * One problem with ThreadLocal, which we're all aware of, is
 * synchronization that through use of a generic adaptor applies to both
 * lookup and insert/removal, whether justified or not. I estimate that
 * getting rid of this will improve performance 10% - way less interesting
 * than the optimizations to follow on.
 * <p>
 * The second problem with ThreadLocal is the use of WeakReference.
 * Each time we look up an entry in the hash map we have to get the actual
 * object from the weak reference and performs an equals operation on it.
 * Each time we add a new entry we have to create a WeakReference object.
 * If we can just get rid of that overhead, the critical loop condition
 * will change from <tt>entry.key.get().equals( thread )</tt> to
 * <tt>entry.key == thread</tt> and we save one object creation.
 * <p>
 * The third problem with ThreadLocal is the obscure logic required to
 * make it work with a generic HashMap. Because HashMap does not allow null
 * values, each value must be encapsulated in an Entry object, another
 * object creation and indirect access. In addition, each entry requires
 * at least one get() and one set() methods calls.
 * <p>
 * Optimizing ThreadLocal is easier done that described. First, I have to
 * explain how we can do without WeakReference. We assume that threads are
 * managed outside our code (e.g. by the RMI layer) and so are created and
 * destroyed at will. ThreadLocal has no knowledge of when they expire, and
 * certainly does not want to hold expired objects. WeakReference is one
 * solution.
 * <p>
 * A Thread typically has three states: prior to run(), during run() and
 * after run(). Prior to run() the thread does not participate in ThreadLocal,
 * so we simply ignore this state. After run() the thread is no longer
 * needed with ThreadLocal (it's value cannot be retrieved), so we can
 * remove it, whether or not it has been garbage collected (no need to
 * wait for WeakReference).
 * <p>
 * We can use isActive() to find what state a thread is in. The Thread will
 * only be active during run(), and since it won't appear in the table prior
 * to run(), we know that an in-active thread has terminated and may be
 * removed.
 * <p>
 * The burden of removing entries for stale threads now shifts from
 * WeakReference and the garbage collector to a background running thread
 * that simply removes all non isActive() threads. There is no need to
 * rehash the table since it never changes in size, so the loop is very
 * simple and efficient. Running at a low priority every 10 minutes is
 * good enough for most cases.
 * <p>
 * If we constantly create and destroy threads, the daemon will have a lot
 * of cleaning up to do. But in such a scenario, we waste so much CPU on
 * thread creation/destruction, the daemon is the least of our worries.
 * Once a thread is no longer alive, there is no reason to remove it
 * immediately from the table. An in-active Thread is just an object taking
 * a few bytes in memory, it is no longer a scarce OS resource as a live
 * thread.
 * <p>
 * Once I took WeakReference out of the equation, the code became indirect
 * and way more efficient. Since I only needed simple operations (get, set,
 * remove), implementing the hashtable was a breeze. And since my
 * implementation accepts null values, I do not need a separate Entry object,
 * get() can perform a set() from initialValue() automatically with no need
 * for a separate call to get(), etc.
 * <p>
 * For the heck of it, I also cut down on the use of synchronization.
 * We know that the same hashtable entry will never be looked/inserted/removed
 * from two threads concurrently, since it's tied to the active thread.
 * So synchronization on lookup is no longer necessary, and synchronization
 * on insert/remove is limited to a short sequence of code.
 * 
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class FastThreadLocal
    // extends java.lang.ThreadLocal // Removed for 1.1 compatibility
    implements Runnable
{


    /**
     * Underneath a hashtable there is always an array. The array
     * holds a linked list of entries, all of which happen to have
     * the exact same hashcode (or a modulo of it)
     */
    private Entry[] _table;


    /**
     * This is how often we run over the table and remove entries for
     * dead threads. The time element is specified in milliseconds.
     */
    private int    _staleTimeout = 600 * 1000; // Ten minutes


    /**
     * Determines the size of the hash table. This must be a prime
     * value and within range of the average number of threads we
     * want to deal with. Potential values are:
     * <pre>
     * Threads  Prime
     * -------  -----
     *   256     239
     *   512     521
     *   1024    1103
     *   2048    2333
     *   4096    4049
     * </pre>
     */
    private static final int TABLE_SIZE = 1103;


    public FastThreadLocal()
    {
	this( TABLE_SIZE );
    }


    public FastThreadLocal( int size)
    {
	_table = new Entry[ size ];

	// This object will now start as a background thread to remove
	// entries to stale (dead) threads.
	Thread thread;

	thread = new Thread( this, Messages.message( "castor.misc.threadLocalDaemonName" ) );
	thread.setPriority( Thread.MIN_PRIORITY );
	thread.setDaemon( true );
	thread.start();
    }


    public Object get()
    {
	Thread thread;
	int    hash;
	Entry  entry;

	thread = Thread.currentThread();
	hash = ( thread.hashCode() & 0x7FFFFFFF ) % _table.length;

	// Lookup the first entry that maps to the has code and
        // continue iterating to the last entry until a matching entry
        // is found. Even if the current entry is removed, we expect
        // entry.next to point to the next entry.
	entry = _table[ hash ];
	while ( entry != null && entry.thread != thread )
	    entry = entry.next;

	if ( entry != null )
	    return entry.value;
	else {
	    // ! Comment the following section if you don't intend to
            // support initialValue !

            /*
	    // If there is no value, we have two options. We can
	    // simply return null, or we can create a default value
	    // with initialValue and return it. Since initialValue
	    // might be costly, if we ever decide to use it, we must
	    // place even a null value in the table. So either have
	    // this code, or comment it all and just return null.
	    // The logic is copied verbatim from get().
	    entry = new Entry();
	    entry.value = initialValue();
	    entry.thread = thread;
	    
	    synchronized ( _table ) {
		entry.next = _table[ hash ];
		_table[ hash ] = entry;
	    }
	    return entry.value;
	    */
            return null;
	}
    }


    public void set( Object value )
    {
	Thread thread;
	int    hash;
	Entry  entry;

	thread = Thread.currentThread();
	hash = ( thread.hashCode() & 0x7FFFFFFF ) % _table.length;

	// This portion is idential to lookup, but if we find the entry
	// we change it's value and return.
	entry = _table[ hash ];
	while ( entry != null && entry.thread != thread )
	    entry = entry.next;
	if ( entry != null ) {
	    entry.value = value;
	    return;
	}

	// No such entry found, so we must create it. Create first to
	// minimize contention period. (Object creation is such a
	// length operation)
	entry = new Entry();
	entry.value = value;
	entry.thread = thread;

	// This operation must be synchronized, otherwise, two concurrent
	// set() methods might insert only one entry. (Not even talking
	// about what remove() would cause).
	synchronized ( _table ) {
	    entry.next = _table[ hash ];
	    _table[ hash ] = entry;
	}
    }


    public Object get( Thread thread )
    {
	int    hash;
	Entry  entry;

	hash = ( thread.hashCode() & 0x7FFFFFFF ) % _table.length;

	// Lookup the first entry that maps to the has code and
        // continue iterating to the last entry until a matching entry
        // is found. Even if the current entry is removed, we expect
        // entry.next to point to the next entry.
	entry = _table[ hash ];
	while ( entry != null && entry.thread != thread )
	    entry = entry.next;

	if ( entry != null )
	    return entry.value;
	else
            return null;
    }


    /**
     * Called to remove an entry for a given thread. Not that useful,
     * <tt>set( null )</tt> is a better option and the background
     * thread will remove stale threads. This code is just here to
     * illustrate how to perform synchronized removal on this table.
     */
    void remove( Thread thread )
    {
	int   hash;
	Entry entry;

	// We synchronize on the thread here, in case remove is called
	// on behalf of the same thread from two separate threads.
	synchronized ( thread ) {
	    hash = ( thread.hashCode() & 0x7FFFFFFF ) % _table.length;

	    // This operation must be synchronized because it messes
	    // with the entry in the table, and set() likes to mess
	    // with the same entry.
	    synchronized ( _table ) {
		entry = _table[ hash ];
		// No such entry, quit. This is the entry, remove
		// it and quit.
		if ( entry == null )
		    return;
		if ( entry.thread == thread ) {
		    _table[ hash ] = entry.next;
		    return;
		}
	    }

	    // Not the first entry. We can only remove the next
	    // entry by changing the next reference on this entry,
	    // so we have to iterate on this entry to remove the
	    // next entry and so our last entry is the one before
	    // last. Sigh.
	    while ( entry.next != null && entry.next.thread != thread )
		entry = entry.next;
	    // No need to synchronized, but keep in mind that get()
	    // expect next to be current, even if the entry has been
	    // removed, so don't reset next!
	    if ( entry.next != null )
		entry.next = entry.next.next;
	}
    }


    public Thread[] listThreads( Object value )
    {
	Entry    entry;
	int      i;
	Thread[] threads;
	Thread[] newThreads;

	threads = null;
	for ( i = 0 ; i < _table.length ; ++i ) {
	    entry = _table[ i ];
	    while ( entry != null ) {
		if ( value == null || entry.value == value ) {
		    if ( threads == null ) {
			threads = new Thread[ 1 ];
			threads[ 0 ] = entry.thread;
		    } else {
			newThreads = new Thread[ threads.length + 1 ];
			System.arraycopy( newThreads, 0, threads, 0, threads.length );
			newThreads[ threads.length ] = entry.thread;
			threads = newThreads;
		    }
		}
		entry = entry.next;
	    }
	}
	return threads;
    }


    public void run()
    {
	Entry entry;
	int   i;

	while ( true ) {
	    // Go to sleep for a while, nothing will happen. This
	    // process can be speeded up by interrupting the thread.
	    // Timeout is in seconds.
	    try {
		Thread.sleep( _staleTimeout );
	    } catch ( InterruptedException e ) {  }

	    // Iterate through all the records in the table and see
	    // which one is stale (the thread is dead) and remove it.
	    for ( i = 0 ; i < _table.length ; ++i ) {

		// This operation must be synchronized because it
		// messes with the entry in the table, and set() likes
		// to mess with the same entry.
		synchronized ( _table ) {
		    entry = _table[ i ];
		    while ( entry != null && ! entry.thread.isAlive() ) {
			_table[ i ] = entry.next;
			entry = entry.next;
		    }
		}

		// More than one entry in the table - keep going.
		// No need to synchronized, but keep in mind that
		// get() expect next to be current, even if the entry
		// has been removed, so don't reset next!
		if ( entry != null ) {
		    while ( entry.next != null ) {
			if ( ! entry.next.thread.isAlive() )
			    entry.next = entry.next.next;
			else
			    entry = entry.next;
		    }
		}

	    }

	}
    }


    /**
     * Test case. Runs 1000 iterations in 1000 threads over a table
     * of 5 entries to see how well it fares.
     */
    /*
    public static void main( String[] args )
    {
	try {
	    FastThreadLocal local;
	    int             i;
	    Test            thread;

	    local = new FastThreadLocal( 5 );
	    thread = null;
	    for ( i = 0 ; i < 1000 ; ++i ) {
		thread = new Test();
		thread.local = local;
		thread.start();
	    }
	    thread.join();
	    System.out.println( "Number of entries in table: " + local.listThreads( null ).length );

	} catch ( Exception except ) {
	    System.out.println( except );
	    except.printStackTrace();
	}
    }


    static class Test
	extends Thread
    {


	ThreadLocal local;


	public void run()
	{
	    int i;
	    
	    for ( i = 0 ; i < 1000 ; ++i ) {
		String value;
		
		value = (String) local.get();
		if ( value == null )
		    local.set( Thread.currentThread().getName() );
		else {
		    if ( ! value.equals( Thread.currentThread().getName() ) )
			System.out.println( "Error: not the value we expected!" );
		    local.set( null );
		}
	    }
	}
    }
    */


    /**
     * Each entry in the table has a key (thread), a value or null
     * (we don't remove on null) and a reference to the next entry in
     * the same table position.
     */
    class Entry
    {

	Object  value;

	Entry   next;

	Thread  thread;

    }


}
