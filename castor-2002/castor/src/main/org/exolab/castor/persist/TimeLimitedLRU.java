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
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 */

package org.exolab.castor.persist;


import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * <p>A time limted LRU hashtable for caching objects.
 * 
 * <p>
 * Every object being put in the hashtable will live for specified
 * amount of time and it will be disposed. Unless it is <code>put
 * </code> or <code>get</code> before it is disposed, then the time 
 * is reset. 
 *
 * <p>
 * If you are interested in the disposing object, extend it class
 * and override the method <code>dispose(Object o)</code>.
 * Otherwise, the object is silently discarded.
 *
 * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
 */
public class TimeLimitedLRU implements LRU {
	/**
	 *	The Default precision in millisecond is 1000. Percision is the interval 
	 *  between each time which the timer thread will wake up and trigger 
	 *  clean up of Least Recently Used Object.
	 */
	public final static int DEFAULT_PRECISION = 1000;

	private TimeThread ticker = new TimeThread( DEFAULT_PRECISION );

	private int interval;
	private int tailtime;
	private QueueItem head;
	private QueueItem tail;
	private Hashtable map;

	/**
	 * Constructor
	 *
	 * @param interval the number of ticks an object live before it is disposed.
     * @param tick precision in msec.
	 */
	public TimeLimitedLRU( int interval ) {
		map = new Hashtable();
		this.interval = DEFAULT_PRECISION+1;
		ticker.addListener( this );
	}
	/**
	 * ...work like Hashtable's <code>put</code>...except it's time limited
	 */
	public synchronized Object put(Object key, Object value) {
		QueueItem oldItem = (QueueItem) map.get(key);
		if ( oldItem != null ) {
			Object oldObject = oldItem.item;
			oldItem.item = value;
			remove(oldItem);
			add(oldItem);
			return oldObject;
		} else {
			QueueItem newitem = new QueueItem(key,value);
			map.put(key,newitem);
			add(newitem);
			return null;
		}
	}
	/**
	 * ...work like Hashtable's <code>get</code>...except it's time limited
	 */
	public synchronized Object get(Object key) {
		Object o = map.get(key);
		if ( o == null )
			return null;
		else 
			return ((QueueItem)o).item;
	}
	/**
	 * ...work like Hashtable's <code>remove</code>...except it's time limited
	 */
	public synchronized Object remove(Object key) {

		Object o = map.remove(key);
		if ( o == null ) {
			//System.out.println("TimeLimiteLRU: not in cache ... remove("+key+")");
			return null;
		} else {
			//System.out.println("TimeLimiteLRU: remove("+key+") = "+((QueueItem)o).item);
			return ((QueueItem)o).item;
		}
	}
	/*
	 * ...work like Hashtable's <code>elements</code>...except it's time limited
	 */
	public synchronized Enumeration elements() {
		return new ValuesEnumeration(map.elements());
	}
	/**
	 * This method is called when an object is disposed.
	 * Override this method if you interested in the disposed object.
	 */
	protected void dispose( Object o ) {
		//System.out.println("dispose: "+o+" by TimeLimiteLRU.");
	}

	private void remove(QueueItem item) {
		//System.out.println( "removing: <" + item + "> while...head=<"+head+"> tail=<"+tail+">" );
		QueueItem temp;
		if ( item == null ) 
			throw new NullPointerException();

		if ( item == head ) {
			temp = item;

			head = head.next;
			if ( head == null ) {
				tail = null;
			} else {
				head.prev = null;
				head.time += temp.time;
			}
			temp.prev = null;
			temp.next = null;
			temp.time = 0;
		} else if ( item == tail ) {
			tail = tail.prev;
			tailtime = 0;
		} else {
			temp = item;

			temp.prev.next = temp.next;
			temp.next.prev = temp.prev;
			temp.next.time += temp.time;

			temp.prev = null;
			temp.next = null;
			temp.time = 0;
		}
	}

	private void add(QueueItem item) {
		ticker.startTick();
		if ( head == null ) {
			head = tail = item;
			item.prev = null;
			item.next = null;
			item.time = interval;
			tailtime = interval;
		} else {
			tail.next = item;
			item.prev = tail;
			item.next = null;
			item.time = interval-tailtime;
			tailtime = interval;
			tail = item;
		}
	}

	/* 
	 * call by ticker daemon
	 */
	private synchronized void tick() {
		QueueItem temp;
		Object o;

		if ( head != null ) {
			head.time--;
			tailtime--;
		}

		while ( head != null && head.time <= 0 ) {
			temp = head;

			o = head.item;
			remove(temp);
			map.remove(temp.key);
			dispose(o);
		}
//		if ( head == null ) {
//			ticker.stopTick();
//		}
	}

	private class ValuesEnumeration implements Enumeration {
		private Vector v = new Vector();
		private int cur;

		private ValuesEnumeration( Enumeration e ) {
			while ( e.hasMoreElements() ) {
				v.add(e.nextElement());
			}
			v.trimToSize();
		}
		public boolean hasMoreElements() {
			if ( v.size() > cur ) 
				return true;
			return false;
		}
		public Object nextElement() throws NoSuchElementException {
			if ( v.size() <= cur )
				throw new NoSuchElementException();
			Object o = v.get(cur++);
			if ( o != null )
				return ((QueueItem)o).item;
			else 
				return null;
		}
	}

	private class QueueItem {
		private QueueItem next;
		private QueueItem prev;
		private Object key;
		private Object item;
		private int time;

		private QueueItem( Object key, Object item ) {
			this.key = key;
			this.item = item;
		}
	}

	/*
	 * Ticker daemon. Generate tick in fixed interval of time.
	 */
	private static class TimeThread extends Thread {
		private int[] listenerLock = new int[0];
		private LinkList listener;
		private int[] lock = new int[0];
		private int tick;
		private long lastTime;
		private boolean isStopped;
		private boolean isStarted;

		public TimeThread(int tick) {
			this.tick = tick;
			setDaemon(true);
			setPriority( MIN_PRIORITY );
			isStopped = true;
			start();
		}
		public void startTick() {
			//System.out.println( "start tick" );
			if ( isStarted && isStopped ) {
				synchronized(lock) {
					lastTime = System.currentTimeMillis();
					lock.notify();
				}
			}
		}
		public void stopTick() {
			//System.out.println( "stop tick" );
			isStopped = true;
		}
		public void run() {
			isStarted = true;
			try {
				while ( true ) {
					if ( isStopped ) {
						synchronized(lock) { lock.wait(); }
						isStopped = false;
					} else {
						long time = System.currentTimeMillis();
						if ( time - lastTime < tick ) 
							sleep(tick-(time-lastTime));
						lastTime = time;
					}

					LinkList temp = listener;
					while ( temp != null ) {
						temp.t.tick();
						temp = temp.next;
					}
				}
			} catch ( InterruptedException e ) {
			}
		}
		public void addListener( TimeLimitedLRU t ) {
			synchronized ( listenerLock ) {
				listener = new LinkList( listener, t );
			}
		}
		private class LinkList {
			private LinkList next;
			private TimeLimitedLRU t;
			LinkList( LinkList next, TimeLimitedLRU t ) {
				this.next = next;
				this.t = t;
			}
		}
	}
	/*
	 * inner class for putting test cases only. Safe to delete.
	 */
	private static class Test {
		public static void main (String args[]) throws Exception {
			class TestLRU extends TimeLimitedLRU {
				public TestLRU(int count) {
					super(count);
				}
				protected void dispose(Object o) {
					Enumeration e = this.elements();
					System.out.println("disposing: " + o );
					System.out.println( "list after disposed!" );
					while ( e.hasMoreElements() ) {
						System.out.print( e.nextElement() + "\t" );
					}
					System.out.println();
					super.dispose(o);
				}
			}
			TimeLimitedLRU cl = new TestLRU(50);
			Thread t = Thread.currentThread();

			Enumeration e = cl.elements();
			System.out.println( "<empty>" );
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "</empty>" );

			System.out.println( "<put(a,a)>" );
			cl.put("a","#a");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			t.sleep(1000);
			System.out.println( "\n</put(a,a)>" );


			System.out.println( "<put[a,b,c,d,e,f,g,h]>" );
			cl.put("a","#a"); System.out.println( "put a" );
			t.sleep(5);

			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println();

			cl.put("b","#b"); System.out.println( "put b" );
			t.sleep(50);

			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println();

			cl.put("c","#c"); System.out.println( "put c" );
			t.sleep(500);

			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println();

			cl.put("d","#d"); System.out.println( "put d" );
			t.sleep(100);

			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println();

			cl.put("e","#e"); System.out.println( "put e" );
			t.sleep(100);
			cl.put("f","#f"); System.out.println( "put f" );
			t.sleep(100);
			cl.put("g","#g"); System.out.println( "put g" );
			t.sleep(100);
			cl.put("h","#h"); System.out.println( "put h" );
			t.sleep(100);
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println();

			for (int i=0; i<50; i++ ) {
				t.sleep(100);
				e = cl.elements();
				while ( e.hasMoreElements() ) {
					System.out.print( e.nextElement() + "\t" );
				}
				System.out.println();
			}

			System.out.println( "\n</put[a,b,c,d,e,f,g,h]>" );

		}
	}
}
